package de.maju.unit

import io.quarkus.test.h2.H2DatabaseTestResource
import io.quarkus.test.junit.QuarkusTestProfile

class Unit : QuarkusTestProfile {
    override fun getConfigOverrides(): MutableMap<String, String> {
        val config = mutableMapOf<String, String>()
        config["quarkus.datasource.url"] = "jdbc:h2:tcp://localhost/mem:test"
        config["quarkus.datasource.driver"] = "org.h2.Driver"
        return super.getConfigOverrides()
    }

    override fun testResources(): MutableList<QuarkusTestProfile.TestResourceEntry> {
        return mutableListOf(QuarkusTestProfile.TestResourceEntry(H2DatabaseTestResource::class.java))
    }
}
