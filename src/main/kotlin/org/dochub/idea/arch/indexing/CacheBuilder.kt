package org.dochub.idea.arch.indexing

import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.util.Key
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import com.intellij.psi.util.CachedValue
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.util.PsiModificationTracker
import com.intellij.util.indexing.FileBasedIndex
import org.dochub.idea.arch.utils.findFile
import java.io.*


private fun isFileExists(project: Project, filename: String): Boolean {
    return File(project.basePath + "/" + filename).exists()
}

private fun parseEnvFile(filename: String): Map<String, String?> {
    val result: MutableMap<String, String?> = HashMap()
    try {
        BufferedReader(FileReader(filename)).use { br ->
            var line: String
            while (br.readLine().also { line = it } != null) {
                val lineStruct =
                    line.split("\\=".toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray()
                result[lineStruct[0]] = if (lineStruct.size > 1) lineStruct[1] else null
            }
        }
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return result
}

private fun manifestMerge(context: MutableMap<String, SectionData>, data: DocHubIndexData, source: VirtualFile) {
    for (sectionKey in data.keys) {
        if (sectionKey == "imports") continue

        // Получаем секцию
        val section: DocHubIndexData.Section? = data.get(sectionKey)
        var sectionData = context[sectionKey]
        if (sectionData == null) {
            sectionData = SectionData()
            context[sectionKey] = sectionData
        }

        // Разбираем секцию
        // Идентификаторы
        if (section != null) {
            for (i in 0 until section.ids.size) {
                val id: String = section.ids.get(i)
                var sources = sectionData.ids[id]
                if (sources == null) {
                    sources = mutableListOf<String>()
                    sectionData.ids[id] = sources
                }
                sources += source
            }
        }
        // Локации
        if (section != null) {
            for (i in 0 until section.locations.size) {
                val location: String = section.locations.get(i)
                if (!sectionData.locations.contains(location)) section.locations.add(location)
            }
        }
    }
}

private fun parseYamlManifest(project: Project, path: String, context: MutableMap<String, SectionData>) {
    val vFile = findFile(path, project)
    if (vFile != null) {
        val targetFile = PsiManager.getInstance(project).findFile(vFile)
        if (targetFile != null) {
            val index = FileBasedIndex.getInstance().getFileData(DocHubIndex.INDEX_ID, vFile, project)
            for (key in index.keys) {
                val data: DocHubIndexData = index[key] as DocHubIndexData
                val imports: DocHubIndexData.Section? = data.get("imports")
                if (imports != null) {
                    for (i in 0 until imports.imports.size) {
                        val importPath = (vFile.parent.path + "/" + imports.imports.get(i))
                            .substring(project.basePath!!.length)
                        parseYamlManifest(project, importPath, context)
                    }
                }
                manifestMerge(context, data, vFile)
            }
        }
    }
}

private fun getFromEnv(project: Project): String? {
    val names = arrayOf(".env.local", ".env")
    for (name in names) {
        if (isFileExists(project, name)) {
            val env = parseEnvFile(project.basePath + "/" + name)
            return "public/" + env["VUE_APP_DOCHUB_ROOT_MANIFEST"]
        }
    }
    return null
}

fun getRootManifestName(project: Project): String {
    var rootManifest: String? = null
    rootManifest = if (isFileExists(project, "dochub.yaml")) // Если это проект DocHub
        "dochub.yaml" else getFromEnv(project)
    return rootManifest ?: "dochub.yaml"
}

private val cacheProjectKey: Key<CachedValue<Any>> = Key.create("dochub-global")

private val globalCacheProviders: MutableMap<Project, GlobalCacheProvider> = HashMap()

fun getProjectCache(project: Project):  MutableMap<String, SectionData> {
    var globalCacheProvider = globalCacheProviders[project]
    if (globalCacheProvider == null) {
        globalCacheProvider = GlobalCacheProvider(project)
        globalCacheProviders[project] = globalCacheProvider
    }
    val cacheManager = CachedValuesManager.getManager(project)
    return cacheManager.getCachedValue(
        project,
        cacheProjectKey,
        globalCacheProvider,
        false
    ) as MutableMap<String, SectionData>
}



class SectionData {
    var locations = mutableListOf<String>()
    var ids: MutableMap<String, List<*>> = mutableMapOf()
}

private class GlobalCacheProvider(private val project: Project) :
    CachedValueProvider<Any> {
    override fun compute(): CachedValueProvider.Result<Any> {
        val manifest: MutableMap<String, SectionData> = mutableMapOf()
        val rootManifest = getRootManifestName(project)
        parseYamlManifest(project, rootManifest, manifest)
        return CachedValueProvider.Result.create(
            manifest,
            PsiModificationTracker.MODIFICATION_COUNT,
            ProjectRootManager.getInstance(project)
        )
    }
}

