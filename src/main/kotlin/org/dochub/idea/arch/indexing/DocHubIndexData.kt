package org.dochub.idea.arch.indexing

import com.intellij.psi.PsiFile
import org.yaml.snakeyaml.Yaml
import java.io.*
import java.nio.charset.StandardCharsets
import java.util.*

class DocHubIndexData : HashMap<String, DocHubIndexData.Section> {
    inner class Section {
        var locations: MutableList<String> = mutableListOf()
        var ids: MutableList<String> = mutableListOf()
        var imports = mutableListOf<String>()
        val isEmpty: Boolean
            get() = locations.size + ids.size + imports.size == 0

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Section

            if (locations != other.locations) return false
            if (ids != other.ids) return false
            if (imports != other.imports) return false

            return true
        }

        override fun hashCode(): Int {
            var result = locations.hashCode()
            result = 31 * result + ids.hashCode()
            result = 31 * result + imports.hashCode()
            return result
        }


    }

    fun stringify(out: DataOutput) {
        out.writeInt(this.size)
        for (secKey in keys) {
            val secKeyBin = secKey.toByteArray(StandardCharsets.UTF_8)
            out.writeInt(secKeyBin.size)
            out.write(secKeyBin)
            val section = this[secKey]

            // Идентификаторы
            out.writeInt(section!!.ids.size)
            for (i in section.ids.indices) {
                val idBin = section.ids[i].toByteArray(StandardCharsets.UTF_8)
                out.writeInt(idBin.size)
                out.write(idBin)
            }

            // Локации
            out.writeInt(section.locations.size)
            for (i in section.locations.indices) {
                val locationBin = section.locations[i].toByteArray(StandardCharsets.UTF_8)
                out.writeInt(locationBin.size)
                out.write(locationBin)
            }

            // Импорты
            out.writeInt(section.imports.size)
            for (i in section.imports.indices) {
                val importBin = section.imports[i].toByteArray(StandardCharsets.UTF_8)
                out.writeInt(importBin.size)
                out.write(importBin)
            }
        }
    }

    fun parse(`in`: DataInput) {
        val secCount = `in`.readInt()
        for (i in 0 until secCount) {
            val secKeyBin = ByteArray(`in`.readInt())
            `in`.readFully(secKeyBin)
            val secKey = String(secKeyBin, StandardCharsets.UTF_8)
            val section: Section = Section()
            val idsCount = `in`.readInt()
            for (n in 0 until idsCount) {
                val idBin = ByteArray(`in`.readInt())
                `in`.readFully(idBin)
                val id = String(idBin, StandardCharsets.UTF_8)
                section.ids.add(id)
            }
            val locationsCount = `in`.readInt()
            for (n in 0 until locationsCount) {
                val locationBin = ByteArray(`in`.readInt())
                `in`.readFully(locationBin)
                val location = String(locationBin, StandardCharsets.UTF_8)
                section.locations.add(location)
            }
            val importsCount = `in`.readInt()
            for (n in 0 until importsCount) {
                val importBin = ByteArray(`in`.readInt())
                `in`.readFully(importBin)
                val import_ = String(importBin, StandardCharsets.UTF_8)
                section.imports.add(import_)
            }
            if (!section.isEmpty) this[secKey] = section
        }
    }

    fun makeCacheDataImports(yaml: Map<String, Any>) {
        val result = yaml["imports"] as MutableList<String>
        if (result != null && result.size > 0) {
            val section = Section()
            section.imports = result
            this["imports"] = section
        }
    }

    fun makeCacheDataSection(yaml: Map<String, Any>, section: String) {
        val secData = Section()
        try {
            val keys = yaml[section] as Map<String, Any>?
            if (keys != null) {
                for (id in keys.keys) {
                    secData.ids.add(id)
                    val `object` = yaml[id]
                    if (`object` is Map<*, *>) {
                        val location = `object`["location"]
                        if (location is String) {
                            secData.locations.add(location as String)
                        }
                    }
                }
            }
            if (secData.locations.size > 0 || secData.ids.size > 0) this[section] = secData
        } catch (e: ClassCastException) {
        }
    }

    fun makeCacheDataManifest(file: PsiFile) {
        val vFile = file.virtualFile
        if (vFile != null) {
            val path = vFile.path
            try {
                val inputStream: InputStream = FileInputStream(path)
                val yaml = Yaml()
                val sections = yaml.load<Map<String, Any>>(inputStream)
                if (sections != null) {
                    makeCacheDataImports(sections)
                    makeCacheDataSection(sections, "components")
                    makeCacheDataSection(sections, "aspects")
                    makeCacheDataSection(sections, "contexts")
                    makeCacheDataSection(sections, "docs")
                    makeCacheDataSection(sections, "datasets")
                }
            } catch (e: Exception) {
            }
        }
    }

    constructor(file: PsiFile) {
        makeCacheDataManifest(file)
    }

    constructor(`in`: DataInput) {
        parse(`in`)
    }
}
