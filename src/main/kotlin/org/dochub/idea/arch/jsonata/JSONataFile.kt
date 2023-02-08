package org.dochub.idea.arch.jsonata

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider

class JSONataFile(viewProvider: FileViewProvider) :
    PsiFileBase(viewProvider, JSONataLanguage.INSTANCE) {
    override fun getFileType(): FileType {
        return JSONataFileType.INSTANCE
    }

    override fun toString(): String {
        return "JSONata File"
    }
}