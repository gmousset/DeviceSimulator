package com.github.gmousset.cds.connectors.lwm2m

import com.github.gmousset.cds.device.BooleanProperty
import com.github.gmousset.cds.device.ByteArrayProperty
import com.github.gmousset.cds.device.DateProperty
import com.github.gmousset.cds.device.DoubleProperty
import com.github.gmousset.cds.device.LongProperty
import com.github.gmousset.cds.device.Property
import com.github.gmousset.cds.device.StringProperty
import org.eclipse.leshan.client.resource.BaseInstanceEnabler
import org.eclipse.leshan.client.servers.LwM2mServer
import org.eclipse.leshan.core.LwM2mId
import org.eclipse.leshan.core.response.ReadResponse

class ObjectEnabler(
    private val propertiesById: Map<Int, Property<*>>
) : BaseInstanceEnabler() {

    companion object {
        private const val SUPPORTED_BINDING_AND_MODES = 16
    }

    override fun read(
        server: LwM2mServer?,
        resourceId: Int
    ): ReadResponse {
        return if (this.model.id == LwM2mId.SERVER && resourceId == SUPPORTED_BINDING_AND_MODES) {
            ReadResponse.success(resourceId, "U")
        } else {
            val property = this.propertiesById[resourceId]
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
