package de.maju.rest.util

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager

class EmptyTestResource: QuarkusTestResourceLifecycleManager {

    override fun start(): MutableMap<String, String> {
        println("Start")
        return mutableMapOf()
    }

    override fun stop() {
        println("stop")
    }
}
