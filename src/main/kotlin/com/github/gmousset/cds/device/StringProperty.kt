package com.github.gmousset.cds.device

class StringProperty(
    name: String,
    valueProvider: () -> String
) : Property<String>(name, valueProvider) {
}