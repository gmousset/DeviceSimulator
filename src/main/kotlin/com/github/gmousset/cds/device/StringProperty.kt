package com.github.gmousset.cds.device

class StringProperty(
    name: String,
    description: String?,
    valueProvider: () -> String
) : Property<String>(name, description, valueProvider)
