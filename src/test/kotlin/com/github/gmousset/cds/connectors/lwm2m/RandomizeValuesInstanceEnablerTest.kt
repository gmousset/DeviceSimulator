package com.github.gmousset.cds.connectors.lwm2m

import io.mockk.mockk
import org.eclipse.leshan.client.servers.LwM2mServer
import org.eclipse.leshan.core.model.ObjectModel
import org.eclipse.leshan.core.model.ResourceModel
import org.eclipse.leshan.core.model.ResourceModel.Operations
import org.eclipse.leshan.core.node.LwM2mSingleResource
import java.util.Date
import kotlin.test.*

class RandomizeValuesInstanceEnablerTest {

    private val lwM2mServer = mockk<LwM2mServer>()
    private val objectModel = ObjectModel(
        999,
        "my model",
        "model for unit test",
        "1.0",
        false,
        false,
        listOf(
            ResourceModel(
                1,
                "a string",
                Operations.R,
                false,
                false,
                ResourceModel.Type.STRING,
                null,
                null,
                "a string resource"
            ),
            ResourceModel(
                2,
                "a enum string",
                Operations.R,
                false,
                false,
                ResourceModel.Type.STRING,
                "ONE, TWO",
                null,
                "a enum string resource"
            ),
            ResourceModel(
                3,
                "a time resource",
                Operations.R,
                false,
                false,
                ResourceModel.Type.TIME,
                null,
                null,
                "a time resource"
            ),
            ResourceModel(
                4,
                "a boolean resource",
                Operations.R,
                false,
                false,
                ResourceModel.Type.BOOLEAN,
                null,
                null,
                "a boolean resource"
            ),
            ResourceModel(
                5,
                "an integer resource",
                Operations.R,
                false,
                false,
                ResourceModel.Type.INTEGER,
                null,
                null,
                "an integer resource"
            ),
            ResourceModel(
                6,
                "an enum integer resource",
                Operations.R,
                false,
                false,
                ResourceModel.Type.INTEGER,
                "0, 1, 2",
                null,
                "an enum integer resource"
            ),
            ResourceModel(
                7,
                "an range integer resource",
                Operations.R,
                false,
                false,
                ResourceModel.Type.INTEGER,
                "11..27",
                null,
                "an range integer resource"
            ),
            ResourceModel(
                8,
                "a random float resource",
                Operations.R,
                false,
                false,
                ResourceModel.Type.FLOAT,
                "",
                null,
                "a random float resource"
            ),
            ResourceModel(
                9,
                "an enum float resource",
                Operations.R,
                false,
                false,
                ResourceModel.Type.FLOAT,
                "10.2, 14.5",
                null,
                "an enum float resource"
            ),
            ResourceModel(
                10,
                "an range float resource",
                Operations.R,
                false,
                false,
                ResourceModel.Type.FLOAT,
                "0.5..24.6",
                null,
                "an range float resource"
            ),
            ResourceModel(
                11,
                "an opaque resource",
                Operations.R,
                false,
                false,
                ResourceModel.Type.OPAQUE,
                null,
                null,
                "an opaque resource"
            ),
            ResourceModel(
                12,
                "a none resource",
                Operations.R,
                false,
                false,
                ResourceModel.Type.NONE,
                null,
                null,
                "a none resource"
            )
        )
    )

    private val randomEnabler = RandomizeValuesInstanceEnabler()

    @BeforeTest
    fun beforeTest() {
        this.randomEnabler.model = this.objectModel
    }

    @Test
    fun `test read random string`() {
        val response = this.randomEnabler.read(lwM2mServer, 1)
        assertTrue(response.isSuccess)
        val content = response.content as LwM2mSingleResource
        assertTrue(content.value is String)
        val value = content.value as String
        assertEquals(10, value.length)
    }

    @Test
    fun `test read enum string`() {
        val response = this.randomEnabler.read(lwM2mServer, 2)
        assertTrue(response.isSuccess)
        val content = response.content as LwM2mSingleResource
        assertTrue(content.value is String)
        val value = content.value as String
        assertTrue(value in listOf("ONE", "TWO"))
    }

    @Test
    fun `test read time`() {
        val response = this.randomEnabler.read(lwM2mServer, 3)
        assertTrue(response.isSuccess)
        val content = response.content as LwM2mSingleResource
        assertTrue(content.value is Date)
        val value = content.value as Date
        assertNotNull(value)
    }

    @Test
    fun `test read boolean`() {
        val response = this.randomEnabler.read(lwM2mServer, 4)
        assertTrue(response.isSuccess)
        val content = response.content as LwM2mSingleResource
        assertTrue(content.value is Boolean)
        val value = content.value as Boolean
        assertTrue(value || !value)
    }

    @Test
    fun `test read random integer`() {
        val response = this.randomEnabler.read(lwM2mServer, 5)
        assertTrue(response.isSuccess)
        val content = response.content as LwM2mSingleResource
        assertTrue(content.value is Long)
        val value = content.value as Long
        assertNotNull(value)
    }

    @Test
    fun `test read enum integer`() {
        for (i in 0..100) {
            val response = this.randomEnabler.read(lwM2mServer, 6)
            assertTrue(response.isSuccess)
            val content = response.content as LwM2mSingleResource
            assertTrue(content.value is Long)
            val value = content.value as Long
            assertTrue(value in listOf(0L, 1L, 2L))
        }
    }

    @Test
    fun `test read range integer`() {
        for (i in 0..100) {
            val response = this.randomEnabler.read(lwM2mServer, 7)
            assertTrue(response.isSuccess)
            val content = response.content as LwM2mSingleResource
            assertTrue(content.value is Long)
            val value = content.value as Long
            assertTrue(value in 11..27)
        }
    }

    @Test
    fun `test read random float`() {
        val response = this.randomEnabler.read(lwM2mServer, 8)
        assertTrue(response.isSuccess)
        val content = response.content as LwM2mSingleResource
        assertTrue(content.value is Double)
        val value = content.value as Double
        assertNotNull(value)
    }

    @Test
    fun `test read enum float`() {
        for (i in 0..100) {
            val response = this.randomEnabler.read(lwM2mServer, 9)
            assertTrue(response.isSuccess)
            val content = response.content as LwM2mSingleResource
            assertTrue(content.value is Double)
            val value = content.value as Double
            assertTrue(value in listOf(10.2, 14.5))
        }
    }

    @Test
    fun `test read range float`() {
        for (i in 0..100) {
            val response = this.randomEnabler.read(lwM2mServer, 10)
            assertTrue(response.isSuccess)
            val content = response.content as LwM2mSingleResource
            assertTrue(content.value is Double)
            val value = content.value as Double
            assertTrue(value in 0.5..24.6)
        }
    }

    @Test
    fun `test read opaque`() {
        val response = this.randomEnabler.read(lwM2mServer, 11)
        assertTrue(response.isSuccess)
        val content = response.content as LwM2mSingleResource
        assertTrue(content.value is ByteArray)
        val value = content.value as ByteArray
        assertTrue(value.isNotEmpty())
    }

    @Test
    fun `test unmanaged type`() {
        val response = this.randomEnabler.read(lwM2mServer, 12)
        assertTrue(response.isFailure)
    }
}
