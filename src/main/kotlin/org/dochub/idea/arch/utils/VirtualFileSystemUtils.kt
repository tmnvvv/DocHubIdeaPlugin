package org.dochub.idea.arch.utils

import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.vfs.VirtualFile

fun findFile(path: String, project: Project): VirtualFile? {
    for (root in ProjectRootManager.getInstance(project).contentSourceRoots) {
        val rel = root.findFileByRelativePath(path)
        if (rel != null) {
            return rel
        }
    }
    for (root in ProjectRootManager.getInstance(project).contentRoots) {
        val rel = root.findFileByRelativePath(path)
        if (rel != null) {
            return rel
        }
    }
    return null
}