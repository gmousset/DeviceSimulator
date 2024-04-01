package com.github.gmousset.cds.connectors.lwm2m

import org.eclipse.leshan.client.LeshanClientBuilder
import org.eclipse.leshan.client.californium.endpoint.CaliforniumClientEndpointsProvider
import org.eclipse.leshan.client.resource.ObjectsInitializer
import org.eclipse.leshan.client.servers.ServerInfo
import org.eclipse.leshan.core.model.LwM2mModelRepository
import org.eclipse.leshan.core.model.ObjectLoader
import java.net.InetAddress
import java.net.URI

import org.eclipse.leshan.core.LwM2mId.*
import org.eclipse.leshan.client.`object`.Security.*
import org.eclipse.leshan.client.`object`.Server

class SimpleClient(
    val endpoint: String,
    val server: String
) {

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
        objectsInitializer.setInstancesForObject(SECURITY, noSec(server, 123))
        objectsInitializer.setInstancesForObject(SERVER, Server(123, 3600))
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