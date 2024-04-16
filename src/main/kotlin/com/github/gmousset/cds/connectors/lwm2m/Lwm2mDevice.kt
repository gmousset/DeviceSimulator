package com.github.gmousset.cds.connectors.lwm2m

import com.github.gmousset.cds.device.Device
import org.eclipse.leshan.client.LeshanClientBuilder
import org.eclipse.leshan.client.californium.endpoint.CaliforniumClientEndpointsProvider
import org.eclipse.leshan.client.`object`.Security
import org.eclipse.leshan.client.`object`.Server
import org.eclipse.leshan.client.resource.ObjectsInitializer
import org.eclipse.leshan.client.servers.ServerInfo
import org.eclipse.leshan.core.LwM2mId.ACCESS_CONTROL
import org.eclipse.leshan.core.LwM2mId.DEVICE
import org.eclipse.leshan.core.LwM2mId.SECURITY
import org.eclipse.leshan.core.LwM2mId.SERVER
import org.eclipse.leshan.core.model.LwM2mModelRepository
import org.eclipse.leshan.core.model.ObjectModel
import java.net.InetAddress
import java.net.URI

class Lwm2mDevice(
    private val endpoint: String,
    private val models: List<ObjectModel>
) : Device {

    companion object {
        private const val SERVER_ID = 123
        private const val SESSION_LIFETIME = 3600L
    }

    override fun connect(
        server: String
    ) {
        val endpointsProviderBuilder = CaliforniumClientEndpointsProvider.Builder()
//        val inet = InetAddress.getLocalHost()
        val inet = InetAddress.getLoopbackAddress()
        endpointsProviderBuilder.setClientAddress(inet)

        val serverInfo = ServerInfo()
        serverInfo.serverUri = URI(server)
        val repository = LwM2mModelRepository(this.models)
        val objectsInitializer = ObjectsInitializer(repository.lwM2mModel)

        this.models.forEach {
            when (it.id) {
                SECURITY -> objectsInitializer.setInstancesForObject(SECURITY, Security.noSec(server, SERVER_ID))
                SERVER -> objectsInitializer.setInstancesForObject(SERVER, Server(SERVER_ID, SESSION_LIFETIME))
                ACCESS_CONTROL -> {}
//                DEVICE -> objectsInitializer.setInstancesForObject(DEVICE, ObjectEnabler(it))
                else -> objectsInitializer.setInstancesForObject(it.id, ObjectEnabler())
            }
        }

        val objectEnablers = objectsInitializer.createAll()
        val leshanClient = LeshanClientBuilder(this.endpoint)
            .setEndpointsProviders(endpointsProviderBuilder.build())
            .setObjects(objectEnablers)
            .build()
        leshanClient.start()
    }
}