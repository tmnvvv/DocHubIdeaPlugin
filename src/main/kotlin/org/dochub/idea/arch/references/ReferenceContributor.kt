package org.dochub.idea.arch.references

import com.intellij.psi.PsiReferenceContributor
import com.intellij.psi.PsiReferenceRegistrar
import org.dochub.idea.arch.references.providers.*

class ReferenceContributor : PsiReferenceContributor() {
    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        for (provider in providers) {
            registrar.registerReferenceProvider(
                provider.refPattern,
                provider
            )
        }
    }

    companion object {
        private val providers: Array<BaseReferencesProvider> = arrayOf(
            RefImportsSource(),
            RefComponentID(),
            RefAspectID(),
            RefDocsID(),
            RefDocSource(),
            RefContextID()
        )
    }
}
