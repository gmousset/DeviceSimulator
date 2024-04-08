package com.github.gmousset.cds.device

class LongProperty(
    name: String,
    valueProvider: () -> Long
) : Property<Long>(name, valueProvider) {
}