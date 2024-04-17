package com.github.gmousset.cds.device

class LongProperty(
    name: String,
    description: String?,
    valueProvider: () -> Long
) : Property<Long>(name, description, valueProvider)
