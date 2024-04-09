package com.github.gmousset.cds.device

import java.util.Date

class DateProperty(
    name: String,
    valueProvider: () -> Date
) : Property<Date>(name, valueProvider) {
}