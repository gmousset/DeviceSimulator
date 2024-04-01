package com.github.gmousset.cds.connectors.lwm2m

import org.apache.commons.lang3.RandomStringUtils
import org.eclipse.leshan.client.resource.BaseInstanceEnabler
import org.eclipse.leshan.client.servers.LwM2mServer
import org.eclipse.leshan.core.model.ResourceModel
import org.eclipse.leshan.core.model.ResourceModel.Type
import org.eclipse.leshan.core.response.ReadResponse
import java.util.Date
import java.util.concurrent.ThreadLocalRandom

open class RandomizeValuesInstanceEnabler : BaseInstanceEnabler() {

    private val groupMin = "min"
    private val groupMax = "max"
    private val rangeRegex = """(?<$groupMin>\d+)..(?<$groupMax>\d+)""".toRegex()

    override fun read(
        server: LwM2mServer?,
        resourceId: Int
    ): ReadResponse {
        println("ask read for ${this.model.id} $resourceId")
        
        val resourceModel = this.model.resources[resourceId]
        val response = when (resourceModel?.type) {
            Type.STRING -> ReadResponse.success(resourceId, getStringValue(resourceModel))
            Type.TIME -> ReadResponse.success(resourceId, getTimeValue())
            Type.BOOLEAN -> ReadResponse.success(resourceId, getBooleanValue())
            Type.INTEGER -> ReadResponse.success(resourceId, getIntValue(resourceModel))
            Type.FLOAT -> ReadResponse.success(resourceId, getFloatValue(resourceModel))
            Type.OPAQUE -> ReadResponse.success(resourceId, getOpaqueValue())
            else -> ReadResponse.notFound()
        }
        return response
    }

    private fun getStringValue(
        model: ResourceModel
    ): String {
        return if (isEnum(model)) {
            enumValues(model).random()
        } else {
            RandomStringUtils.randomAlphabetic(7, 12)
        }
    }

    private fun getBooleanValue() = Math.random() > .5

    private fun getTimeValue(): Date {

        val time = ThreadLocalRandom.current().nextLong(
            System.currentTimeMillis() - (3600L * 24 * 30 * 1000),
            System.currentTimeMillis()
        )
        return Date(time)
    }

    private fun getOpaqueValue(): ByteArray = RandomStringUtils.randomAlphabetic(100).toByteArray()

    private fun getIntValue(
        model: ResourceModel
    ): Long {
        return getFloatValue(model).toLong()
    }

    private fun getFloatValue(
        model: ResourceModel
    ): Double {
        return if (isRange(model)) {
            val range = range(model)
            if (range != null) {
                (Math.random() * (range.second + 1) + range.first)
            } else {
                (Math.random() * 100)
            }
        } else {
            (Math.random() * 100)
        }
    }

    private fun isEnum(
        model: ResourceModel
    ): Boolean {
        val rangeEnumeration = model.rangeEnumeration
        return rangeEnumeration?.contains(",") ?: false
    }

    private fun enumValues(
        model: ResourceModel
    ): List<String> {
        return model.rangeEnumeration.split(",").map { s -> s.trim() }
    }

    private fun isRange(
        model: ResourceModel
    ): Boolean {
        val rangeEnumeration = model.rangeEnumeration
        return if (rangeEnumeration != null) {
            rangeRegex.matches(rangeEnumeration)
        } else {
            false
        }
    }

    private fun range(
        model: ResourceModel
    ): Pair<Double, Double>? {
        val result = rangeRegex.matchEntire(model.rangeEnumeration)
        return if (result != null) {
            val min = result.groups[groupMin]?.value?.toDouble()
            val max = result.groups[groupMax]?.value?.toDouble()
            if (min != null && max != null) {
                Pair(min, max)
            } else {
                null
            }
        } else {
            null
        }
    }
}