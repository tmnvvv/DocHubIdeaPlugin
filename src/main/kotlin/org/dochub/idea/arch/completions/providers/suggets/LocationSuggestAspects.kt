package org.dochub.idea.arch.completions.providers.suggets

open class LocationSuggestAspects : LocationSuggest() {
    override val section: String
        get() = "aspects"
}