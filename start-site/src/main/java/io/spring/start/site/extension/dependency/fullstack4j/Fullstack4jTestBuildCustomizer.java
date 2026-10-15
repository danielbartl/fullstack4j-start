/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.fullstack4j;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.buildsystem.DependencyScope;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * A {@link BuildCustomizer} that adds {@code fullstack4j-spring-boot-starter-test} when
 * fullstack4j is selected. It brings WicketTester and {@code @WicketTest}, Spring
 * Modulith's test support, Testcontainers and {@code MongoDbReplicaSetConfiguration}.
 *
 * @author Daniel Bartl
 */
class Fullstack4jTestBuildCustomizer implements BuildCustomizer<Build> {

	@Override
	public void customize(Build build) {
		Dependency fullstack4j = build.dependencies().get("fullstack4j");
		build.dependencies()
			.add("fullstack4j-test",
					Dependency.withCoordinates(fullstack4j.getGroupId(), fullstack4j.getArtifactId() + "-test")
						.version(fullstack4j.getVersion())
						.scope(DependencyScope.TEST_COMPILE));
	}

}
