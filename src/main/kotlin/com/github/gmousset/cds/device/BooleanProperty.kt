package com.github.gmousset.cds.device

class BooleanProperty(
    name: String,
    description: String?,
    valueProvider: () -> Boolean
) : Property<Boolean>(name, description, valueProvider) {
}