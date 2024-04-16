package com.github.gmousset.cds.connectors.lwm2m

import com.github.gmousset.cds.device.BooleanProperty
import com.github.gmousset.cds.device.ByteArrayProperty
import com.github.gmousset.cds.device.DateProperty
import com.github.gmousset.cds.device.DoubleProperty
import com.github.gmousset.cds.device.LongProperty
import com.github.gmousset.cds.device.Property
import com.github.gmousset.cds.device.StringProperty
import com.github.gmousset.cds.providers.RandomValueProvider
import org.eclipse.leshan.client.resource.BaseInstanceEnabler
import org.eclipse.leshan.client.servers.LwM2mServer
import org.eclipse.leshan.core.LwM2mId
import org.eclipse.leshan.core.model.ObjectModel
import org.eclipse.leshan.core.model.ResourceModel
import org.eclipse.leshan.core.response.ReadResponse

class ObjectEnabler(
//    private val objectModel: ObjectModel
) : BaseInstanceEnabler() {

    private var propertiesById: Map<Int, Property<*>>? = null

    /*
    private val objectId = objectModel.id
    private val propertiesById = this.objectModel.resources.entries.mapNotNull { entry ->
        val resourceId = entry.key
        val resourceModel = entry.value
        val resourcePath = "/$objectId/0/$resourceId"
        val resourceName = resourceModel.name

        val prop = when (resourceModel.type) {
            ResourceModel.Type.INTEGER -> LongProperty(resourcePath, resourceName) { RandomValueProvider.getLong() }
            ResourceModel.Type.FLOAT -> DoubleProperty(
                resourcePath,
                resourceName
            ) { RandomValueProvider.getDouble() }

            ResourceModel.Type.STRING -> StringProperty(
                resourcePath,
                resourceName
            ) { RandomValueProvider.getString() }

            ResourceModel.Type.BOOLEAN -> BooleanProperty(
                resourcePath,
                resourceName
            ) { RandomValueProvider.getBoolean() }

            ResourceModel.Type.OPAQUE -> ByteArrayProperty(
                resourcePath,
                resourceName
            ) { RandomValueProvider.getByteArray() }

            ResourceModel.Type.TIME -> DateProperty(resourcePath, resourceName) { RandomValueProvider.getDate() }
            else -> null
        }

        if (prop == null) {
            null
        } else {
            Pair(resourceId, prop)
        }
    }.toMap()
*/
    companion object {
        private const val SUPPORTED_BINDING_AND_MODES = 16
    }

    override fun setModel(
        model: ObjectModel
    ) {
        super.setModel(model)
        val objectId = model.id
        this.propertiesById = model.resources.entries.mapNotNull { entry ->
            val resourceId = entry.key
            val resourceModel = entry.value
            val resourcePath = "/$objectId/0/$resourceId"
            val resourceName = resourceModel.name

            val prop = when (resourceModel.type) {
                ResourceModel.Type.INTEGER -> LongProperty(resourcePath, resourceName) { RandomValueProvider.getLong() }
                ResourceModel.Type.FLOAT -> DoubleProperty(
                    resourcePath,
                    resourceName
                ) { RandomValueProvider.getDouble() }

                ResourceModel.Type.STRING -> StringProperty(
                    resourcePath,
                    resourceName
                ) { RandomValueProvider.getString() }

                ResourceModel.Type.BOOLEAN -> BooleanProperty(
                    resourcePath,
                    resourceName
                ) { RandomValueProvider.getBoolean() }

                ResourceModel.Type.OPAQUE -> ByteArrayProperty(
                    resourcePath,
                    resourceName
                ) { RandomValueProvider.getByteArray() }

                ResourceModel.Type.TIME -> DateProperty(resourcePath, resourceName) { RandomValueProvider.getDate() }
                else -> null
            }

            if (prop == null) {
                null
            } else {
                Pair(resourceId, prop)
            }
        }.toMap()
    }

    override fun read(
        server: LwM2mServer?,
        resourceId: Int
    ): ReadResponse {
        return if (this.model.id == LwM2mId.DEVICE && resourceId == SUPPORTED_BINDING_AND_MODES) {
            ReadResponse.success(resourceId, "U")
        } else {
            val property = this.propertiesById?.get(resourceId)
            if (property != null) {
                when (property) {
                    is LongProperty -> ReadResponse.success(resourceId, property.valueProvider())
                    is DoubleProperty -> ReadResponse.success(resourceId, property.valueProvider())
                    is StringProperty -> ReadResponse.success(resourceId, property.valueProvider())
                    is BooleanProperty -> ReadResponse.success(resourceId, property.valueProvider())
                    is ByteArrayProperty -> ReadResponse.success(resourceId, property.valueProvider())
                    is DateProperty -> ReadResponse.success(resourceId, property.valueProvider())
                    else -> ReadResponse.notFound()
                }
            } else {
                ReadResponse.notFound()
            }
        }
    }
}
