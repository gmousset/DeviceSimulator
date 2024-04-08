package com.github.gmousset.cds.device

class FloatProperty(
    name: String,
    valueProvider: () -> Float
) : Property<Float>(name, valueProvider) {
}