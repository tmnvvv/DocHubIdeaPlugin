package org.dochub.idea.arch.annotators

import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import java.util.regex.Pattern

object DocHubAnnotator {

    fun parseComment(element: PsiElement): List<EntityRef> {
        val literalExpression = element as PsiComment
        val comment = if (literalExpression.text is String) literalExpression.text as String else null
        val result = ArrayList<EntityRef>()
        val groups = Pattern.compile("(\\@dochub\\:)([a-z]+)\\/([a-zA-Z0-9\\.\\_\\-]+)").matcher(comment)
        while (groups.find()) {
            val ref = EntityRef()
            ref.start = groups.start()
            ref.prefix = groups.group(1)
            ref.entity = groups.group(2)
            ref.id = groups.group(3)
            result.add(ref)
        }
        return result
    }

    class EntityRef {
        var start = 0
        var prefix: String? = null
        var entity: String? = null
        var id: String? = null
    }
}
