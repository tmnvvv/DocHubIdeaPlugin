package org.dochub.idea.arch.utils

import com.google.gson.Gson
import org.dochub.idea.arch.jsonschema.EntityJSONSchemaProviderFactory
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URI
import java.nio.charset.StandardCharsets
import java.nio.file.FileSystem
import java.nio.file.FileSystems
import java.util.stream.Collectors


object JsonUtils {

    // Cache
    private val fileSystems: MutableMap<String, FileSystem> = mutableMapOf()


        fun <T> fromJson(stringPath: String, clazz: Class<T>): T {
            val reader = InputStreamReader(this::class.java.classLoader.getResourceAsStream(stringPath), StandardCharsets.UTF_8)
            val value = BufferedReader(reader).lines().collect(Collectors.joining("\n"))
            return Gson().fromJson(value, clazz)
        }

        fun getFileSystem(path: String): FileSystem? {
            val key = path.replace("///", "/").split("!")[0]
            if (fileSystems.get(key) == null) {
                fileSystems.put(key, FileSystems.newFileSystem(URI(path), emptyMap<String, Any>()))
            }
            return fileSystems.get(key)
        }

}