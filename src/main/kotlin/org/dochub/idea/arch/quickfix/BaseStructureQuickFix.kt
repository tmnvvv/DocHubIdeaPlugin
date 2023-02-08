package org.dochub.idea.arch.quickfix

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.util.IntentionFamilyName
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.patterns.ElementPattern
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiParserFacade
import com.intellij.util.IncorrectOperationException
import org.dochub.idea.arch.utils.getText
import org.jetbrains.yaml.YAMLElementGenerator
import org.jetbrains.yaml.psi.YAMLKeyValue
import org.jetbrains.yaml.psi.YAMLMapping
import java.util.*

open class BaseStructureQuickFix : BaseQuickFix {

    protected val requiredStructure: Array<String> = arrayOf()

    var needToAppendProps: List<String> = listOf()

    constructor(element: PsiElement, needToAppendProps: List<String>) : super(element) {
        this.needToAppendProps = needToAppendProps
    }

    constructor(): super(null)

    override fun getFixPattern(element: PsiElement): ElementPattern<out PsiElement>? {
        return null
    }

    override fun makeFix(element: PsiElement, holder: AnnotationHolder) {
        val componentID = element.parent
        var props = componentID.children
        var result: MutableList<String>? = mutableListOf()
        val foundProps: MutableList<String> = mutableListOf()
        if (props.size == 1) {
            props = props[0].children
            for (prop in props) {
                if (prop is YAMLKeyValue) {
                    foundProps.add(getText(prop.key))
                }
            }
            for (require in requiredStructure) {
                if (foundProps.indexOf(require) < 0) {
                    if (result != null) {
                        result.add(require)
                    }
                }
            }
        } else result = requiredStructure?.asList()?.toMutableList()
        if (result != null) {
            if (result.size > 0) {
                holder.newAnnotation(HighlightSeverity.ERROR, "Lost properties")
                    .range(element)
                    .highlightType(ProblemHighlightType.LIKE_UNKNOWN_SYMBOL)
                    .withFix(BaseStructureQuickFix(componentID, result))
                    .create()
            }
        }
    }

    override fun getText(): String {
        return "Create properties"
    }

    override fun getFamilyName(): @IntentionFamilyName String {
        return "Create properties"
    }

    override fun isAvailable(project: Project, editor: Editor, file: PsiFile): Boolean {
        return true
    }

    private fun getLastKeyFromMap(map: PsiElement): PsiElement? {
        var result: PsiElement? = null
        val keyMapChildren = map.children
        var index = keyMapChildren.size - 1
        while (result !is YAMLKeyValue && index >= 0) {
            result = keyMapChildren[index]
            index--
        }
        return result
    }

    @Throws(IncorrectOperationException::class)
    override fun invoke(project: Project, editor: Editor, file: PsiFile) {
        val builder = StringBuilder("\n")
        for (prop in needToAppendProps!!) {
            builder.append("  $prop: \n")
        }
        val yamlFile = YAMLElementGenerator.getInstance(file.project).createDummyYamlWithText(builder.toString())
        val mapping = (yamlFile.documents[0].topLevelValue as YAMLMapping?)!!
        if (element!!.children.size > 0) {
            val keyMap = element!!.lastChild
            val newLineNode = PsiParserFacade. getInstance(project).createWhiteSpaceFromText("\n")
            val lastKey = getLastKeyFromMap(keyMap)
            if (lastKey == null) {
                element!!.addBefore(mapping, element!!.lastChild)
            } else {
                val insertKeys = mapping.children
                for (index in insertKeys.indices.reversed()) {
                    val key = insertKeys[index]
                    key.add(newLineNode)
                    keyMap.addBefore(key, lastKey)
                }
            }
        } else element!!.add(mapping)
    }
}
