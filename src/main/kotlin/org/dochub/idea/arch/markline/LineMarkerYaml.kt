package org.dochub.idea.arch.markline

import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProviderDescriptor
import com.intellij.psi.PsiElement
import org.dochub.idea.arch.references.providers.RefAspectID
import org.dochub.idea.arch.references.providers.RefComponentID
import org.dochub.idea.arch.references.providers.RefContextID
import org.dochub.idea.arch.references.providers.RefDocsID
import org.jetbrains.yaml.psi.YAMLKeyValue
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl

class LineMarkerYaml : LineMarkerProviderDescriptor() {

    override fun collectSlowLineMarkers(
        elements: MutableList<out PsiElement>,
        result: MutableCollection<in LineMarkerInfo<*>>
    ) {
        var i = 0
        val size = elements.size
        while (i < size) {
            val element = elements[i]
            collectNavigationMarkers(element, result)
            i++
        }
    }


    protected fun collectNavigationMarkers(
        element: PsiElement,
        result: MutableCollection<in LineMarkerInfo<*>>
    ) {
    }

    override fun getName(): String? {
        return null
    }

    private fun isRegisteredComponent(element: PsiElement, id: String): Boolean {
//        Map<String, Object> cache = CacheBuilder.getProjectCache(element.getProject());
//        Map<String, Object> components = cache == null ? null : (Map<String, Object>) cache.get("components");
//        PsiElement document = PsiUtils.getYamlDocumentByPsiElement(element);
//        List<String> suggest = SuggestUtils.scanYamlPsiTreeToID(document, "components");
//        return components.get(id) != null || (suggest.indexOf(id) >= 0);
        return true // todo Здесь нужно проверять на действительную регистрацию компонента
    }

    private fun isRegisteredDocument(element: PsiElement, id: String): Boolean {
        return true // todo Здесь нужно проверять на действительную регистрацию компонента
    }

    interface ElementExplain {
        fun register(id: String?): DocHubNavigationHandler? {
            return null
        }
    }

    fun explainElement(element: PsiElement, explain: ElementExplain): LineMarkerInfo<*>? {
        var result: LineMarkerInfo<*>? = null
        var id: String? = null
        var markElement: PsiElement = element
        if (element is YAMLKeyValue) {
            markElement = element.getFirstChild()
            id = element.name
        } else if (element is YAMLPlainTextImpl) {
            markElement = element.getFirstChild()
            id = element.getText()
        }
        if (id != null && isRegisteredComponent(element, id)) {
            result = makeLineMarkerInfo(
                explain.register(id),
                markElement
            )
        }
        return result
    }

    private fun getLineMarkerInfoForComponent(element: PsiElement): LineMarkerInfo<*>? {
        return explainElement(element, object : ElementExplain {
            override fun register(id: String?): DocHubNavigationHandler {
                return DocHubNavigationHandler("component", id)
            }
        })
    }

    private fun getLineMarkerInfoForDocument(element: PsiElement): LineMarkerInfo<*>? {
        return explainElement(element, object : ElementExplain {
            override fun register(id: String?): DocHubNavigationHandler {
                return DocHubNavigationHandler("document", id)
            }
        })
    }

    private fun getLineMarkerInfoForAspect(element: PsiElement): LineMarkerInfo<*>? {
        return explainElement(element, object : ElementExplain {
            override fun register(id: String?): DocHubNavigationHandler {
                return DocHubNavigationHandler("aspect", id)
            }
        })
    }

    private fun getLineMarkerInfoForContext(element: PsiElement): LineMarkerInfo<*>? {
        return explainElement(element, object : ElementExplain {
            override fun register(id: String?): DocHubNavigationHandler {
                return DocHubNavigationHandler("context", id)
            }
        })
    }

    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<*>? {
        var result: LineMarkerInfo<*>? = null
        if (RefComponentID.pattern().accepts(element)) {
            result = getLineMarkerInfoForComponent(element)
        } else if (RefDocsID.pattern().accepts(element)) {
            result = getLineMarkerInfoForDocument(element)
        } else if (RefAspectID.pattern().accepts(element)) {
            result = getLineMarkerInfoForAspect(element)
        } else if (RefContextID.pattern().accepts(element)) {
            result = getLineMarkerInfoForContext(element)
        }
        return result
    }
}
