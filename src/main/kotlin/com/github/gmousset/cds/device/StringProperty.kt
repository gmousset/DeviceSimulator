package com.github.gmousset.cds.device

import java.beans.PropertyDescriptor

class StringProperty(
    name: String,
    description: String?,
    valueProvider: () -> String
) : Property<String>(name, description, valueProvider) {
}