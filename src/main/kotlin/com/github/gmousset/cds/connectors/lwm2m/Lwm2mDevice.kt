package com.github.gmousset.cds.connectors.lwm2m

import com.github.gmousset.cds.device.BooleanProperty
import com.github.gmousset.cds.device.ByteArrayProperty
import com.github.gmousset.cds.device.DateProperty
import com.github.gmousset.cds.device.Device
import com.github.gmousset.cds.device.DoubleProperty
import com.github.gmousset.cds.device.LongProperty
import com.github.gmousset.cds.device.StringProperty
import com.github.gmousset.cds.providers.RandomValueProvider
import org.eclipse.leshan.client.LeshanClientBuilder
import org.eclipse.leshan.client.californium.endpoint.CaliforniumClientEndpointsProvider
import org.eclipse.leshan.client.`object`.Security
import org.eclipse.leshan.client.`object`.Server
import org.eclipse.leshan.client.resource.BaseInstanceEnabler
import org.eclipse.leshan.client.resource.ObjectsInitializer
import org.eclipse.leshan.client.servers.ServerInfo
import org.eclipse.leshan.core.LwM2mId.SECURITY
import org.eclipse.leshan.core.LwM2mId.SERVER
import org.eclipse.leshan.core.model.LwM2mModelRepository
import org.eclipse.leshan.core.model.ObjectLoader
import org.eclipse.leshan.core.model.ObjectModel
import org.eclipse.leshan.core.model.ResourceModel
import java.net.InetAddress
import java.net.URI

class Lwm2mDevice(
    private val endpoint: String,
    private val models: List<ObjectModel>
) : Device, BaseInstanceEnabler() {

    private val properties = models.map { objectModel ->
        val objectId = objectModel.id
        objectModel.resources.entries.mapNotNull { entry ->
            val resourceId = entry.key
            val resourceModel = entry.value
            val resourceName = "/$objectId/0/$resourceId"

            when (resourceModel.type) {
                ResourceModel.Type.INTEGER -> LongProperty(resourceName) { RandomValueProvider.getLong() }
                ResourceModel.Type.FLOAT -> DoubleProperty(resourceName) { RandomValueProvider.getDouble() }
                ResourceModel.Type.STRING -> StringProperty(resourceName) { RandomValueProvider.getString() }
                ResourceModel.Type.BOOLEAN -> BooleanProperty(resourceName) { RandomValueProvider.getBoolean() }
                ResourceModel.Type.OPAQUE -> ByteArrayProperty(resourceName) { RandomValueProvider.getByteArray() }
                ResourceModel.Type.TIME -> DateProperty(resourceName) { RandomValueProvider.getDate() }
                else -> null
            }
        }
    }.flatten()

    companion object {
        private const val SERVER_ID = 123
        private const val SESSION_LIFETIME = 3600L
    }

    fun connect(
        server: String
    ) {
        val endpointsProviderBuilder = CaliforniumClientEndpointsProvider.Builder()
//        val inet = InetAddress.getLocalHost()
        val inet = InetAddress.getLoopbackAddress()
        endpointsProviderBuilder.setClientAddress(inet)

        val serverInfo = ServerInfo()
        serverInfo.serverUri = URI(server)

        val objectModels = ObjectLoader.loadAllDefault()
        val repository = LwM2mModelRepository(objectModels)
        val objectsInitializer = ObjectsInitializer(repository.lwM2mModel)

        this.models.forEach {
            when (it.id) {
                SECURITY -> objectsInitializer.setInstancesForObject(SECURITY, Security.noSec(server, SERVER_ID))
                SERVER -> objectsInitializer.setInstancesForObject(SERVER, Server(SERVER_ID, SESSION_LIFETIME))
                else -> objectsInitializer.setInstancesForObject(it.id, this)
            }
        }

        val objectEnablers = objectsInitializer.createAll()
        val leshanClient = LeshanClientBuilder(this.endpoint)
            .setEndpointsProviders(endpointsProviderBuilder.build())
            .setObjects(objectEnablers)
            .build()
        leshanClient.start()
    }

    override fun getProperties() = this.properties
}