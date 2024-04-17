package com.github.gmousset.cds.device

class ByteArrayProperty(
    name: String,
    description: String?,
    valueProvider: () -> ByteArray
) : Property<ByteArray>(name, description, valueProvider)
