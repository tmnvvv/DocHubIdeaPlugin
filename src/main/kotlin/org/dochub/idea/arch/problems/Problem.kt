package org.dochub.idea.arch.problems

import com.intellij.psi.PsiElement
import org.dochub.idea.arch.utils.compare


class Problem(
    private val target: PsiElement,
    private val message: String,
    private val problemLevel: ProblemLevel,
    private val line: Int,
    private val column: Int,
    private val sourceName: String,
    private val afterEndOfLine: Boolean,
    private val suppressErrors: Boolean
) : Comparable<Problem?> {

    override fun compareTo(other: Problem?): Int {
        val lineComparison = Integer.compare(line, other!!.line)
        if (lineComparison == 0) {
            val columnComparison = Integer.compare(column, other.column)
            if (columnComparison == 0) {
                val severityComparison: Int = -problemLevel.compareTo(other.problemLevel)
                return if (severityComparison == 0) {
                    compare(message, other.message)
                } else severityComparison
            }
            return columnComparison
        }
        return lineComparison
    }
}
