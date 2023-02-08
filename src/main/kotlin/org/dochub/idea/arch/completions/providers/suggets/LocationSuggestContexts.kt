package org.dochub.idea.arch.completions.providers.suggets

open class LocationSuggestContexts : LocationSuggest() {
    override val section: String
        protected get() = "contexts"
}