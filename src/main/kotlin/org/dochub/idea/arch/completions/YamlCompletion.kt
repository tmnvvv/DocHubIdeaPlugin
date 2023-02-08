package org.dochub.idea.arch.completions

import com.intellij.codeInsight.completion.CompletionContributor
import org.dochub.idea.arch.completions.providers.*
import org.dochub.idea.arch.completions.providers.contexts.*
import org.dochub.idea.arch.completions.providers.components.*
import org.dochub.idea.arch.completions.providers.forms.*
import org.dochub.idea.arch.completions.providers.aspects.*
import org.dochub.idea.arch.completions.providers.namespaces.*
import org.dochub.idea.arch.completions.providers.docs.*
import org.dochub.idea.arch.completions.providers.technologies.*
import org.dochub.idea.arch.completions.providers.imports.*
import org.dochub.idea.arch.completions.providers.datasets.*


class YamlCompletion: CompletionContributor() {
    private val providers: Array<CustomProvider> = arrayOf<CustomProvider>(
        Root(),
        Contexts(),
        ContextID(),
        Extralinks(),
        Uml(),
        UmlNotations(),
        ContextComponents(),
        ContextLocation(),
        Components(),
        ComponentID(),
        ComponentEntity(),
        ComponentLinks(),
        ComponentLinksID(),
        ComponentLinksDirection(),
        ComponentAspects(),
        ComponentContractID(),
        Forms(),
        FormItem(),
        FormItemField(),
        FormItemFieldRequired(),
        Aspects(),
        AspectID(),
        AspectLocation(),
        Namespaces(),
        NamespaceID(),
        Docs(),
        DocID(),
        DocSource(),
        DocSubjects(),
        DocType(),
        DocLocation(),
        DocOrigin(),
        Technologies(),
        SectionItem(),
        ItemsItem(),
        Imports(),
        ImportItem(),
        Dataset(),
        DatasetOrigin()
    )

    fun YamlCompletion() {
        for (provider in providers) {
            provider.appendToCompletion(this)
        }
    }
}