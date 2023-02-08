package org.dochub.idea.arch.completions

class CompletionKey(val key: String, val valueType: ValueType) {

    constructor(key: String): this(key, ValueType.TEXT)

    enum class ValueType {
        TEXT, MAP, LIST
    }
}