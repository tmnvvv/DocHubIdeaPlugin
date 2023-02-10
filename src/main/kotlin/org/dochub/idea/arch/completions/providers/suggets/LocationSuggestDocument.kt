package org.dochub.idea.arch.completions.providers.suggets

open class LocationSuggestDocument : LocationSuggest() {
    override val section: String
        get() = "docs"
}