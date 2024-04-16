package com.github.gmousset.cds.providers

import org.apache.commons.lang3.RandomStringUtils
import java.util.Date
import java.util.concurrent.ThreadLocalRandom

class RandomValueProvider {
    companion object {
        fun getInt() = ThreadLocalRandom.current().nextInt()
        fun getLong() = ThreadLocalRandom.current().nextLong()
        fun getFloat() = ThreadLocalRandom.current().nextFloat()
        fun getDouble() = ThreadLocalRandom.current().nextDouble()
        fun getString() = RandomStringUtils.randomAlphabetic(15)
        fun getBoolean() = ThreadLocalRandom.current().nextBoolean()
        fun getByteArray() = RandomStringUtils.random(15).toByteArray()
        fun getDate() = Date(ThreadLocalRandom.current().nextLong())
    }
}

