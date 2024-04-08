package com.github.gmousset.cds.device

class IntegerProperty(
    name: String,
    valueProvider: () -> Int
) : Property<Int>(name, valueProvider) {
}