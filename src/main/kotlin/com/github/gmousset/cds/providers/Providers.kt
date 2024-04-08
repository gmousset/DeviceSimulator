package com.github.gmousset.cds.providers

import java.util.concurrent.ThreadLocalRandom

fun getIntValue() = ThreadLocalRandom.current().nextInt()
fun getLongValue() = ThreadLocalRandom.current().nextLong()
fun getFloatValue() = ThreadLocalRandom.current().nextFloat()
fun getDoubleValue() = ThreadLocalRandom.current().nextDouble()