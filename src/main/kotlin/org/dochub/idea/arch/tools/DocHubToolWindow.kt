package org.dochub.idea.arch.tools

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.SystemInfoRt
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.openapi.vfs.newvfs.BulkFileListener
import com.intellij.openapi.vfs.newvfs.events.VFileContentChangeEvent
import com.intellij.openapi.vfs.newvfs.events.VFileDeleteEvent
import com.intellij.openapi.vfs.newvfs.events.VFileEvent
import com.intellij.ui.jcef.JBCefBrowser
import com.intellij.ui.jcef.JBCefBrowserBase
import com.intellij.ui.jcef.JBCefJSQuery
import org.apache.commons.io.FilenameUtils
import org.dochub.idea.arch.indexing.getRootManifestName
import org.dochub.idea.arch.jsonschema.applySchema
import org.dochub.idea.arch.manifests.PlantUMLDriver
import org.dochub.idea.arch.settings.SettingsState
import org.dochub.idea.arch.wizard.RootManifest
import java.io.File
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import java.util.regex.Matcher

class DocHubToolWindow(project: Project) : JBCefBrowser("/") {

    private val sourceQuery: JBCefJSQuery
    private val project: Project
    private val navigation: Navigation
    private val jsGateway: JSGateway

    private val injectionSettings: String
        get() {
            val settingsState: SettingsState = SettingsState.instance
            val settings: MutableMap<String, Any> = HashMap()
            val render: MutableMap<String, Any> = HashMap()
            render["mode"] = settingsState.renderMode
            render["external"] = settingsState.renderIsExternal
            render["server"] = settingsState.serverRendering
            settings["render"] = render
            return try {
                val mapper = ObjectMapper()
                Matcher.quoteReplacement(
                    mapper.writeValueAsString(settings)
                )
            } catch (e: JsonProcessingException) {
                throw RuntimeException(e)
            }
        }

    fun reloadHtml() {
        val input = javaClass.classLoader.getResourceAsStream("html/plugin.html")
        var html: String
        try {
            assert(input != null)
            html = String(input.readAllBytes(), StandardCharsets.UTF_8)
            val injectionCode = sourceQuery.inject("data", "resolve", "reject")
            html = html.replace("\"API_INJECTION\"".toRegex(), Matcher.quoteReplacement(injectionCode))
                .replace("\"SETTING_INJECTION\"".toRegex(), injectionSettings)
        } catch (e: IOException) {
            html = e.toString()
        }
        val currentURL = cefBrowser.url
        if (currentURL.length > 0) {
            loadHTML(html, currentURL)
        } else {
            loadHTML(html)
        }
        if (!SystemInfoRt.isWindows) cefBrowser.uiComponent.isFocusable = false
    }

    private fun requestProcessing(json: String): JBCefJSQuery.Response {
        // openDevtools();
        val result = StringBuilder()
        try {
            val mapper = ObjectMapper()
            val jsonObj = mapper.readTree(json)
            val jsonURL = jsonObj["url"]
            if (jsonURL != null) {
                val url = jsonURL.asText()
                if (url == ROOT_SOURCE_URI) {
                    val response: MutableMap<String, String> = mutableMapOf()
                    val rootName = project.basePath + "/" + getRootManifestName(project)
                    val rootFile = File(rootName)
                    if (!rootFile.exists()) {
                        return JBCefJSQuery.Response("", 404, "No found: $url")
                    }
                    response["contentType"] = rootName.substring(rootName.length - 4).lowercase()
                    response["data"] = Files.readString(Path.of(rootName))
                    result.append(mapper.writeValueAsString(response))
                } else if (url == PLANTUML_RENDER_SVG_URI) {
                    val jsonSource = jsonObj["source"]
                    val source: String = if (jsonSource != null) jsonSource.asText() else "@startuml\n@enduml"
                    val response: MutableMap<String, String?> = mutableMapOf()
                    response["data"] = PlantUMLDriver.makeSVG(source)
                    result.append(mapper.writeValueAsString(response))
                } else if (url == NAVI_GOTO_SOURCE_URI) {
                    navigation.go(jsonObj)
                } else if (url == WIZARD_INIT_URI) {
                    val jsonMode = jsonObj["mode"]
                    val mode = if (jsonMode != null) jsonMode.asText() else "production"
                    if (mode == "example") {
                        RootManifest().createExampleManifest(project)
                    } else {
                        RootManifest().createRootManifest(project)
                    }
                    reloadHtml()
                } else if (url.length > 20 && url.startsWith(ROOT_SOURCE_PATH)) {
                    val basePath = project.basePath + "/"
                    val parentPath: String = File(getRootManifestName(project)).getParent()
                    val sourcePath = (basePath
                            + ("$parentPath/")
                            + url.substring(20).split("\\?".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0])
                    val file = File(sourcePath)
                    if (!file.exists() || file.isDirectory) {
                        return JBCefJSQuery.Response("", 404, "No found: $url")
                    }
                    val response: MutableMap<String, Any> = HashMap()
                    val contentType = FilenameUtils.getExtension(sourcePath).lowercase()
                    response["contentType"] = contentType
                    if (contentType == "jpg" || contentType == "jpeg" || contentType == "svg" || contentType == "png") {
                        response["data"] = Files.readAllBytes(Path.of(sourcePath))
                    } else response["data"] = Files.readString(Path.of(sourcePath))
                    result.append(mapper.writeValueAsString(response))
                } else if (url == ACTION_PULL_URI) {
                    val response: MutableMap<String, Any?> = mutableMapOf()
                    response["contentType"] = "json"
                    response["data"] = jsGateway.pullJSONMessage()
                    result.append(mapper.writeValueAsString(response))
                } else if (url == ACTION_DOWNLOAD_URI) { // Сохранение файлов из WEB морды
                    val jsonContent = jsonObj["content"]
                    val jsonTitle = jsonObj["title"]
                    val jsonDescription = jsonObj["description"]
                    if (jsonContent != null) {
                        download(
                            jsonContent.asText(),
                            if (jsonTitle != null) jsonTitle.asText() else "",
                            if (jsonDescription != null) jsonDescription.asText() else ""
                        )
                    }
                } else if (url == DEVTOOL_SHOW_URI) {
                    openDevtools()
                    cefBrowser.executeJavaScript("console.info('GO!!');", "events.js", 0)
                } else if (url == HTML_RELOAD_URI) {
                    reloadHtml()
                } else if (url == ENTITIES_APPLY_SCHEMA) {
                    val schema = jsonObj["schema"]
                    applySchema(project, schema.asText())
                } else if (url == CLIPBOARD_COPY) {
                    copy(jsonObj["data"].asText())
                } else {
                    return JBCefJSQuery.Response("", 404, "No found: $url")
                }
            }
        } catch (e1: IOException) {
            return JBCefJSQuery.Response("", 500, e1.toString())
        }
        return JBCefJSQuery.Response(result.toString())
    }

    init {
        PlantUMLDriver().init()

        this.project = project
        val eventBus = project.messageBus.connect()
        navigation = Navigation(project)
        jsGateway = JSGateway(project)
        eventBus.subscribe(VirtualFileManager.VFS_CHANGES, object : BulkFileListener {
            override fun after(events: List<VFileEvent>) {
                val rootManifest: String = getRootManifestName(project)
                events.forEach { event: VFileEvent? ->
                    if (event!!.file != null &&
                        (event is VFileContentChangeEvent
                                || event is VFileDeleteEvent)
                    ) {
                        val path = event.file!!.path
                        val bpLength = Objects.requireNonNull(project.basePath)?.length
                        if (bpLength != null && path.length > bpLength) {
                            var source = path.substring(bpLength + 1)
                            if (source == rootManifest) source = ROOT_SOURCE
                            jsGateway.appendMessage(ACTION_SOURCE_CHANGED, ROOT_SOURCE_PATH + source, null)
                        }
                    }
                }
            }
        })
        sourceQuery = JBCefJSQuery.create((this as JBCefBrowserBase))
        sourceQuery.addHandler { json: String ->
            requestProcessing(
                json
            )
        }
        reloadHtml()
    }
}
