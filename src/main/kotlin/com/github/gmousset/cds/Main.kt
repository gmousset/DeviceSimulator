package com.github.gmousset.cds

import com.github.gmousset.cds.connectors.lwm2m.Lwm2mDevice
import org.eclipse.leshan.core.model.ObjectLoader
import org.eclipse.leshan.core.model.ObjectModel


fun main() {
    val myDevice = Lwm2mDevice(
        endpoint = "device-lwm2m-001",
        models = ObjectLoader.loadDefault()
    )
    myDevice.connect(server = "coap://localhost:5683")
}
