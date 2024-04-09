package com.github.gmousset.cds.device

class BooleanProperty(
    name: String,
    valueProvider: () -> Boolean
) : Property<Boolean>(name, valueProvider) {
}