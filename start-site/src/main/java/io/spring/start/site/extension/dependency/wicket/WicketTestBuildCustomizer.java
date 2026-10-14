/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.wicket;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.buildsystem.DependencyScope;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * A {@link BuildCustomizer} that automatically adds
 * {@code wicket-spring-boot-starter-test} when Apache Wicket is selected. It brings
 * {@code WicketTester} at the starter's Wicket version and {@code @WicketTest}.
 *
 * @author Daniel Bartl
 */
public class WicketTestBuildCustomizer implements BuildCustomizer<Build> {

	@Override
	public void customize(Build build) {
		Dependency wicket = build.dependencies().get("wicket");
		build.dependencies()
			.add("wicket-test",
					Dependency.withCoordinates(wicket.getGroupId(), wicket.getArtifactId() + "-test")
						.version(wicket.getVersion())
						.scope(DependencyScope.TEST_COMPILE));
	}

}
