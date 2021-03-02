package de.maju.integration

import de.maju.integration.util.docker.IntegrationDockerTestResource
import io.quarkus.test.junit.QuarkusTestProfile

class Integration : QuarkusTestProfile {
    override fun testResources(): MutableList<QuarkusTestProfile.TestResourceEntry> {
        return mutableListOf(QuarkusTestProfile.TestResourceEntry(IntegrationDockerTestResource::class.java))
    }
}
