package com.github.gmousset.cds.device

open class Property<T>(
    val name: String,
    val valueProvider: () -> T
) {
}