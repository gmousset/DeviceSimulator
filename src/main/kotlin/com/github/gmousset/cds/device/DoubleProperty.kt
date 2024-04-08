package com.github.gmousset.cds.device

class DoubleProperty(
    name: String,
    valueProvider: () -> Double
) : Property<Double>(name, valueProvider) {
}