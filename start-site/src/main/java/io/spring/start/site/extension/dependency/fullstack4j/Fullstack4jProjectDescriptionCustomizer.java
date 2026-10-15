/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.fullstack4j;

import java.util.List;

import io.spring.initializr.generator.project.MutableProjectDescription;
import io.spring.initializr.generator.project.ProjectDescriptionCustomizer;

/**
 * Removes the dependencies that the fullstack4j starters already bring from projects that
 * depend on them. Left in, they would repeat what the starters declare, and
 * Testcontainers would add a MongoDB container that is not a replica set next to the one
 * from {@code MongoDbReplicaSetConfiguration}.
 *
 * @author Daniel Bartl
 */
public class Fullstack4jProjectDescriptionCustomizer implements ProjectDescriptionCustomizer {

	private static final List<String> BUNDLED_DEPENDENCIES = List.of("wicket", "data-mongodb", "modulith",
			"testcontainers");

	@Override
	public void customize(MutableProjectDescription description) {
		if (description.getRequestedDependencies().containsKey("fullstack4j")) {
			BUNDLED_DEPENDENCIES.forEach(description::removeDependency);
		}
	}

}
