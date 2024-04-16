package com.github.gmousset.cds.device

import java.util.Date

class DateProperty(
    name: String,
    description: String?,
    valueProvider: () -> Date
) : Property<Date>(name, description, valueProvider)
