package org.dochub.idea.arch.utils

fun <T : Comparable<T>?> compare(obj1: T, obj2: T?): Int {
    if (obj1 == null && obj2 == null) {
        return 0
    } else if (obj1 == null) {
        return -1
    } else if (obj2 == null) {
        return 1
    }
    return obj1.compareTo(obj2)
}