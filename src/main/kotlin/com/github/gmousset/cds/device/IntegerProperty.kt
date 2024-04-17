package com.github.gmousset.cds.device

class IntegerProperty(
    name: String,
    description: String?,
    valueProvider: () -> Int
) : Property<Int>(name, description, valueProvider)
