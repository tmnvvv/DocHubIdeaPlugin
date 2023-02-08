package org.dochub.idea.arch.utils

import com.intellij.psi.PsiElement
import com.intellij.util.ObjectUtils
import org.jetbrains.yaml.psi.YAMLDocument
import org.jetbrains.yaml.psi.YAMLKeyValue
import org.yaml.snakeyaml.Yaml
import java.io.*
import java.util.*


fun scanDirByContext(basePath: String, context: String, extensions: Array<String>): List<String> {
    val result: MutableList<String> = ArrayList()
    val prefix =
        if (context.startsWith("../")) context.substring(3) else if (context == "." || context == "..") "" else context
    if (context.endsWith("/.") || context == ".") {
        result.add("$prefix./")
    } else if (context.endsWith("/..") || context == "..") {
        result.add("$prefix/")
    } else {
        val dirName = if (context.endsWith("/") || context.length == 0) context else File(context).parent + "/"
        val dir = File("$basePath/$dirName")
        val listFiles = dir.listFiles()
        if (listFiles != null) {
            for (f in listFiles) {
                val suggest = ((if (context.startsWith("../")) dirName.substring(3) else dirName)
                        + f.name)
                if (f.isDirectory) {
                    result.add("$suggest/")
                } else for (ext in extensions) {
                    if (f.name.endsWith(ext!!)) {
                        result.add(suggest)
                    }
                }
            }
        }
    }
    return result
}

fun appendDividerItem(list: MutableList<String>, item: String, context: String, divider: String) {

    val contextParts = "$context ".split("\\$divider".toRegex()).dropLastWhile { it.isEmpty() }
        .toTypedArray()
    val itemParts = item.split("\\$divider".toRegex()).dropLastWhile { it.isEmpty() }
        .toTypedArray()
    if (item.startsWith(context) && item.length > context.length) {
        var suggest = if (contextParts.size > 0) java.lang.String.join(
            divider,
            *Arrays.copyOfRange(contextParts, 0, contextParts.size - 1)
        ) else ""
        suggest += (if (suggest.length > 0) divider else "") + itemParts[contextParts.size - 1]
        if (list.indexOf(suggest) < 0) list.add(suggest)
    }
}

fun scanYamlStreamToID(stream: InputStream, section: String, context: String): List<String> {
    val result: MutableList<String> = ArrayList()
    val yml = Yaml()
    val document = yml.load<Map<String, Any>>(stream)
    if (document != null) {
        for ((key, value) in document) {
            if (key == section) {
                val components = value as Map<String, Any>
                for ((key) in components) {
                    appendDividerItem(result, key, context, ".")
                }
            }
        }
    }
    return result
}

fun scanYamlPsiTreeToID(document: PsiElement, section: String): MutableList<String> {
    val result: MutableList<String> = mutableListOf()
    val yamlSections = document.firstChild.children
    for (yamlSection in yamlSections) {
        val yamlKey = ObjectUtils.tryCast(yamlSection, YAMLKeyValue::class.java)
        if (yamlKey != null && getText(yamlKey.key) == section) {
            val yamlIDs = yamlSection.lastChild.children
            for (id in yamlIDs) {
                val yamlID = ObjectUtils.tryCast(id, YAMLKeyValue::class.java)
                if (yamlID != null) {
                    // appendDividerItem(result, PsiUtils.getText(yamlID.getKey()), context, ".");
                    result.add(getText(yamlID.key))
                }
            }
        }
    }
    return result
}

fun scanYamlPsiTreeToLocation(element: PsiElement, section: String): MutableList<String> {
    var document = element
    val result: MutableList<String> = ArrayList()
    while (document != null) {
        if (ObjectUtils.tryCast(document, YAMLDocument::class.java) != null) break
        document = document.parent
    }
    if (document != null) {
        val yamlSections = document.firstChild.children
        // Обход корневых секций
        for (yamlSection in yamlSections) {
            val yamlKey = ObjectUtils.tryCast(
                yamlSection,
                YAMLKeyValue::class.java
            )
            if (yamlKey != null && getText(yamlKey.key) == section) {
                // Обход нужной секции
                val yamlIDs = yamlSection.lastChild.children
                for (id in yamlIDs) {
                    val yamlID = ObjectUtils.tryCast(
                        id,
                        YAMLKeyValue::class.java
                    )
                    if (yamlID != null) {
                        // Обход полей
                        val fields = id.lastChild.children
                        for (field in fields) {
                            val yamlField = ObjectUtils.tryCast(
                                field,
                                YAMLKeyValue::class.java
                            )
                            // Если нашли поле location
                            val key = getText(yamlField!!.key)
                            val location = getText(yamlField.value)
                            if (yamlField != null && key == "location" && location.length > 0) {
                                // appendDividerItem(result, PsiUtils.getText(field.getLastChild()), context, "/");
                                result.add(getText(yamlField.value))
                            }
                        }
                    }
                }
            }
        }
    }
    return result
}

fun scanYamlStringToID(data: String, section: String, context: String): List<String> {
    return scanYamlStreamToID(ByteArrayInputStream(data.toByteArray()), section, context)
}


fun scanYamlFileToID(path: String, section: String, context: String): List<String> {
    return scanYamlStreamToID(FileInputStream(path), section, context)
}
