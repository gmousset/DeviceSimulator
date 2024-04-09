package com.github.gmousset.cds

import com.github.gmousset.cds.connectors.lwm2m.old.SimpleClient

fun main() {
    val simpleClient = SimpleClient(
        endpoint = "001",
        server = "coap://localhost:5683"
    )
    simpleClient.connect()
}
