package com.github.gmousset.cds.device

class ByteArrayProperty(
    name: String,
    valueProvider: () -> ByteArray
) : Property<ByteArray>(name, valueProvider) {
}