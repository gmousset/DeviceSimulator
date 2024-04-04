package com.github.gmousset.cds.connectors.lwm2m

import org.eclipse.leshan.client.LeshanClientBuilder
import org.eclipse.leshan.client.californium.endpoint.CaliforniumClientEndpointsProvider
import org.eclipse.leshan.client.`object`.Security
import org.eclipse.leshan.client.resource.ObjectsInitializer
import org.eclipse.leshan.client.servers.ServerInfo
import org.eclipse.leshan.core.model.LwM2mModelRepository
import org.eclipse.leshan.core.model.ObjectLoader
import java.net.InetAddress
import java.net.URI

import org.eclipse.leshan.core.LwM2mId.SERVER
import org.eclipse.leshan.client.`object`.Server
import org.eclipse.leshan.core.LwM2mId.DEVICE
import org.eclipse.leshan.core.LwM2mId.LOCATION
import org.eclipse.leshan.core.LwM2mId.SECURITY

class SimpleClient(
    val endpoint: String,
    val server: String
) {

    companion object {
        private const val SERVER_ID  = 123
        private const val SESSION_LIFETIME = 3600L
    }

    private val serverUri = URI(server)

    fun connect() {
        val endpointsProviderBuilder = CaliforniumClientEndpointsProvider.Builder()
//        val inet = InetAddress.getLocalHost()
        val inet = InetAddress.getLoopbackAddress()
        endpointsProviderBuilder.setClientAddress(inet)

        val serverInfo = ServerInfo()
        serverInfo.serverUri = serverUri

        val objectModels = ObjectLoader.loadAllDefault()
        val repository = LwM2mModelRepository(objectModels)
        val objectsInitializer = ObjectsInitializer(repository.lwM2mModel)
        objectsInitializer.setInstancesForObject(SECURITY, Security.noSec(server, SERVER_ID))
        objectsInitializer.setInstancesForObject(SERVER, Server(SERVER_ID, SESSION_LIFETIME))
        objectsInitializer.setInstancesForObject(DEVICE, MyDevice())
        objectsInitializer.setInstancesForObject(LOCATION, RandomizeValuesInstanceEnabler())
        val objectEnablers = objectsInitializer.createAll()
        val leshanClient = LeshanClientBuilder(endpoint)
            .setEndpointsProviders(endpointsProviderBuilder.build())
            .setObjects(objectEnablers)
            .build()
        leshanClient.start()
    }
}
