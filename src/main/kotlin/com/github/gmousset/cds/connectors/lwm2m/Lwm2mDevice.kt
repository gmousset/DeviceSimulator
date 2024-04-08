package com.github.gmousset.cds.connectors.lwm2m

import com.github.gmousset.cds.device.Device
import com.github.gmousset.cds.device.DoubleProperty
import com.github.gmousset.cds.device.LongProperty
import com.github.gmousset.cds.device.Property
import com.github.gmousset.cds.providers.getDoubleValue
import com.github.gmousset.cds.providers.getLongValue
import org.eclipse.leshan.core.model.ObjectModel
import org.eclipse.leshan.core.model.ResourceModel

class Lwm2mDevice(
    properties: List<Property<out Any>>
) : Device(properties) {

    companion object {
        fun instantiate(
            models: List<ObjectModel>
        ): Lwm2mDevice {
            val properties = models.flatMap { it.resources.entries }.map {
                val resourceId = it.key
                val resourceModel = it.value
                val resourceName = resourceModel.name

                when (resourceModel.type) {
                    ResourceModel.Type.INTEGER -> LongProperty(resourceName) { getLongValue() }
                    ResourceModel.Type.FLOAT -> DoubleProperty(resourceName) { getDoubleValue() }
                    else -> null
                }
            }.filterNotNull()
            return Lwm2mDevice(properties)
        }
    }
}