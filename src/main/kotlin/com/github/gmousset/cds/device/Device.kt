package com.github.gmousset.cds.device

interface Device {
    fun getProperties(): List<Property<*>>
}