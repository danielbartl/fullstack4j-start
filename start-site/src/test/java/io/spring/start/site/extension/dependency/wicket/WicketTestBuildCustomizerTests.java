/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.wicket;

import io.spring.initializr.metadata.Dependency;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.SupportedBootVersion;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link WicketTestBuildCustomizer}.
 *
 * @author Daniel Bartl
 */
class WicketTestBuildCustomizerTests extends AbstractExtensionTests {

	private static final SupportedBootVersion BOOT_VERSION = SupportedBootVersion.V4_0;

	@Test
	void wicketTestIsAddedWithWicket() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "wicket");
		assertThat(mavenPom(request)).hasDependency(wicket())
			.hasDependency(Dependency.createSpringBootStarter("test", Dependency.SCOPE_TEST))
			.hasDependency(wicketTest())
			.hasDependenciesSize(3);
	}

	@Test
	void wicketTestIsAddedAtTheVersionOfTheStarter() {
		String version = getDependency(BOOT_VERSION, "wicket").getVersion();
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "wicket");
		assertThat(mavenPom(request)).hasDependency("dev.jbaby", "wicket-spring-boot-starter", version)
			.hasDependency("dev.jbaby", "wicket-spring-boot-starter-test", version, "test");
	}

	@Test
	void wicketTestIsAddedToGradleBuilds() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "wicket");
		assertThat(gradleBuild(request)).contains("testImplementation 'dev.jbaby:wicket-spring-boot-starter-test:");
	}

	@Test
	void wicketTestIsNotAddedWithoutWicket() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "web");
		Dependency dependency = wicketTest();
		assertThat(mavenPom(request)).doesNotHaveDependency(dependency.getGroupId(), dependency.getArtifactId());
	}

	private static Dependency wicket() {
		return Dependency.withId("wicket", "dev.jbaby", "wicket-spring-boot-starter");
	}

	private static Dependency wicketTest() {
		Dependency dependency = Dependency.withId("wicket-test", "dev.jbaby", "wicket-spring-boot-starter-test");
		dependency.setScope(Dependency.SCOPE_TEST);
		return dependency;
	}

}
