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

    companion object {
        private const val STRING_SIZE = 10
        private const val OPAQUE_LENGTH = 100
        private const val MAX_IN_PAST = 3600L * 24 * 30 * 1000
    }

    private val groupMin = "min"
    private val groupMax = "max"
    private val intRangeRegex = """(?<${groupMin}>\d+)\.\.(?<$groupMax>\d+)""".toRegex()
    private val floatRangeRegex = """(?<${groupMin}>\d+.\d+)\.\.(?<$groupMax>\d+.\d+)""".toRegex()
    private val intEnumRegex = """(?<${groupMin}>\d+)\w*,\w*(?<$groupMax>\d+)""".toRegex()
    private val floatEnumRegex = """(?<${groupMin}>\d+.\d+)\w*,\w*(?<$groupMax>\d+.\d+)""".toRegex()

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
            RandomStringUtils.randomAlphabetic(STRING_SIZE)
        }
    }

    private fun getBooleanValue() = Math.random() > .5

    private fun getTimeValue(): Date {
        val time = ThreadLocalRandom.current().nextLong(
            System.currentTimeMillis() - MAX_IN_PAST,
            System.currentTimeMillis()
        )
        return Date(time)
    }

    private fun getOpaqueValue() = RandomStringUtils.randomAlphabetic(OPAQUE_LENGTH).toByteArray()

    private fun getIntValue(
        model: ResourceModel
    ): Long = when {
        isIntRange(model) -> {
            val range = intRange(model)
            ThreadLocalRandom.current().nextLong(range.first, range.second)
        }
        isEnum(model) -> enumValues(model).map { it.toLong() }.random()
        else -> (Math.random() * Long.MAX_VALUE).toLong()
    }

    private fun getFloatValue(
        model: ResourceModel
    ): Double = when {
        isFloatRange(model) -> {
            val range = floatRange(model)
            ThreadLocalRandom.current().nextDouble(range.first, range.second)
        }
        isEnum(model) -> enumValues(model).map { it.toDouble() }.random()
        else -> Math.random() * Float.MAX_VALUE
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
        return model.rangeEnumeration.split(",").map { it.trim() }
    }

    private fun isIntRange(
        model: ResourceModel
    ) = isRange(model, intRangeRegex)

    private fun isFloatRange(
        model: ResourceModel
    ) = isRange(model, floatRangeRegex)

    private fun isRange(
        model: ResourceModel,
        regex: Regex
    ): Boolean {
        val rangeEnumeration = model.rangeEnumeration
        return if (rangeEnumeration != null) {
            regex.matches(rangeEnumeration)
        } else {
            false
        }
    }

    private fun intRange(
        model: ResourceModel
    ): Pair<Long, Long>  = range(model = model, regex = intRangeRegex, converter = { it.toLong() })

    private fun floatRange(
        model: ResourceModel
    ): Pair<Double, Double> = range(model = model, regex = floatRangeRegex, converter = { it.toDouble() })

    private fun <T> range(
        model: ResourceModel,
        regex: Regex,
        converter: (String) -> T
    ): Pair<T, T> {
        val result = regex.matchEntire(model.rangeEnumeration)
        return if (result != null) {
            val min = result.groups[groupMin]?.value?.let { converter(it) }
            val max = result.groups[groupMax]?.value?.let { converter(it) }
            if (min != null && max != null) {
                Pair(min, max)
            } else {
                throw Exception("unexpected range")
            }
        } else {
            throw Exception("unexpected range")
        }
    }
}
