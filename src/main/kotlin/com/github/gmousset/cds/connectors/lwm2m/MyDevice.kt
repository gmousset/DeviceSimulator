package com.github.gmousset.cds.connectors.lwm2m

import org.eclipse.leshan.client.servers.LwM2mServer
import org.eclipse.leshan.core.response.ReadResponse

class MyDevice : RandomizeValuesInstanceEnabler() {

    override fun read(
        server: LwM2mServer?,
        resourceId: Int
    ): ReadResponse {
        println("ask read for ${this.model.id} $resourceId")
        val response = when (resourceId) {
            16 -> ReadResponse.success(resourceId, "U")
            else -> super.read(server, resourceId)
        }
        return response
    }
}
