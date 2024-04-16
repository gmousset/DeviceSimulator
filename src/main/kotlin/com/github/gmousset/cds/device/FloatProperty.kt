package com.github.gmousset.cds.device

class FloatProperty(
    name: String,
    description: String?,
    valueProvider: () -> Float
) : Property<Float>(name, description, valueProvider) {
}