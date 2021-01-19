package de.maju.util

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager
import org.testcontainers.containers.GenericContainer

class DockerTestResource : QuarkusTestResourceLifecycleManager {

    private val listOfContainerCreator = listOf(
        PostgresContainerCreator(),
        KeycloakContainerCreator()
    )

    private val listOfContainer = mutableListOf<GenericContainer<*>>()


    override fun start(): MutableMap<String, String> {
        val resultConfig = mutableMapOf<String, String>()

        for (creator in listOfContainerCreator) {
            val config = creator.getConfig()
            val container = creator.getContainer()

            println("Starting ${container.dockerImageName}")
            if (!container.isRunning) container.start()

            with(listOfContainer) {
                if (!contains(container)) add(container)
            }

            println("Started ${container.dockerImageName}")
            resultConfig.putAll(config)
        }


        println("Starting with config: $resultConfig")
        return resultConfig
    }


    override fun stop() {
        listOfContainer.forEach { container ->
            println("Stopping ${container.getDockerImageName()}")
            if (container.isRunning()) container.stop()
            println("Stopped ${container.getDockerImageName()}")
        }
    }


}
