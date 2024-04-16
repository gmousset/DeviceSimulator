package com.github.gmousset.cds.device

class DoubleProperty(
    name: String,
    description: String?,
    valueProvider: () -> Double
) : Property<Double>(name, description, valueProvider) {
}