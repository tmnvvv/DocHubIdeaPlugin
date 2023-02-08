package org.dochub.idea.arch.tools

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.fileChooser.FileChooserFactory
import com.intellij.openapi.fileChooser.FileSaverDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import java.io.BufferedWriter
import java.io.FileWriter
import java.io.IOException
import java.util.*

fun download(content: String, title: String, description: String) {

    val timer = Timer("Download diagram")
    timer.schedule(object : TimerTask() {
        override fun run() {
            ApplicationManager.getApplication().invokeLater {
                val descriptor =
                    FileSaverDescriptor(title, description, *arrayOf("svg"))
                val dialog = FileChooserFactory.getInstance()
                    .createSaveFileDialog(descriptor, null as Project?)
                val vf = dialog.save(null as VirtualFile?, "diagram") ?: return@invokeLater
                val file = vf.file
                try {
                    val writer = BufferedWriter(FileWriter(file, false))
                    writer.append(content)
                    writer.close()
                } catch (e: IOException) {
                    throw RuntimeException(e)
                }
            }
        }
    }, 100L)
}


