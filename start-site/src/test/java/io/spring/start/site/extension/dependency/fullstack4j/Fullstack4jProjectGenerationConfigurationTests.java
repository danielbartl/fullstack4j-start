/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.fullstack4j;

import io.spring.initializr.generator.test.project.ProjectStructure;
import io.spring.initializr.metadata.Dependency;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.SupportedBootVersion;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link Fullstack4jProjectGenerationConfiguration} and
 * {@link Fullstack4jProjectDescriptionCustomizer}.
 *
 * @author Daniel Bartl
 */
class Fullstack4jProjectGenerationConfigurationTests extends AbstractExtensionTests {

	private static final SupportedBootVersion BOOT_VERSION = SupportedBootVersion.V4_1;

	@Test
	void fullstack4jTestIsAddedAtTheVersionOfTheStarter() {
		String version = getDependency(BOOT_VERSION, "fullstack4j").getVersion();
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "fullstack4j");
		assertThat(mavenPom(request)).hasDependency("dev.jbaby", "fullstack4j-spring-boot-starter", version)
			.hasDependency("dev.jbaby", "fullstack4j-spring-boot-starter-test", version, "test")
			.hasDependency(Dependency.createSpringBootStarter("test", Dependency.SCOPE_TEST))
			.hasDependenciesSize(3)
			.hasBomsSize(0);
	}

	@Test
	void fullstack4jTestIsAddedToGradleBuilds() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "fullstack4j");
		assertThat(gradleBuild(request))
			.contains("testImplementation 'dev.jbaby:fullstack4j-spring-boot-starter-test:");
	}

	@Test
	void dependenciesThatFullstack4jBringsAreRemoved() {
		String version = getDependency(BOOT_VERSION, "fullstack4j").getVersion();
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "fullstack4j", "wicket", "data-mongodb", "modulith",
				"testcontainers");
		assertThat(mavenPom(request)).hasDependency("dev.jbaby", "fullstack4j-spring-boot-starter", version)
			.hasDependency("dev.jbaby", "fullstack4j-spring-boot-starter-test", version, "test")
			.hasDependency(Dependency.createSpringBootStarter("test", Dependency.SCOPE_TEST))
			.hasDependenciesSize(3)
			.hasBomsSize(0);
		assertThat(generateProject(request))
			.doesNotContainFiles("src/test/java/com/example/demo/TestcontainersConfiguration.java");
	}

	@Test
	void otherDependenciesAreKept() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "fullstack4j", "actuator");
		assertThat(mavenPom(request)).hasDependency("dev.jbaby", "fullstack4j-spring-boot-starter")
			.hasDependency(Dependency.createSpringBootStarter("actuator"));
	}

	@Test
	void dependenciesAreKeptWithoutFullstack4j() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "wicket", "data-mongodb");
		assertThat(mavenPom(request)).hasDependency("dev.jbaby", "wicket-spring-boot-starter")
			.hasDependency(Dependency.createSpringBootStarter("data-mongodb"));
	}

	@Test
	void javaAbove25IsLoweredTo25() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "fullstack4j");
		request.setJavaVersion("27");
		assertThat(mavenPom(request)).hasProperty("java.version", "25");
	}

	@Test
	void applicationTestsImportMongoDbReplicaSetConfiguration() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "fullstack4j");
		assertThat(generateProject(request)).textFile("src/test/java/com/example/demo/DemoApplicationTests.java")
			.isEqualToNormalizingNewlines("""
					package com.example.demo;

					import dev.jbaby.fullstack4j.springboot.test.MongoDbReplicaSetConfiguration;
					import org.junit.jupiter.api.Test;
					import org.springframework.boot.test.context.SpringBootTest;
					import org.springframework.context.annotation.Import;

					@Import(MongoDbReplicaSetConfiguration.class)
					@SpringBootTest
					class DemoApplicationTests {

						@Test
						void contextLoads() {
						}

					}
					""");
	}

	@Test
	void testApplicationWithJavaIsContributed() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "fullstack4j");
		request.setLanguage("java");
		ProjectStructure project = generateProject(request);
		assertThat(project).textFile("src/test/java/com/example/demo/TestDemoApplication.java")
			.isEqualToNormalizingNewlines(
					"""
							package com.example.demo;

							import dev.jbaby.fullstack4j.springboot.test.MongoDbReplicaSetConfiguration;
							import org.springframework.boot.SpringApplication;

							public class TestDemoApplication {

								public static void main(String[] args) {
									SpringApplication.from(DemoApplication::main).with(MongoDbReplicaSetConfiguration.class).run(args);
								}

							}
							""");
	}

	@Test
	void testApplicationWithKotlinIsContributed() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "fullstack4j");
		request.setLanguage("kotlin");
		ProjectStructure project = generateProject(request);
		assertThat(project).textFile("src/test/kotlin/com/example/demo/TestDemoApplication.kt")
			.isEqualToNormalizingNewlines("""
					package com.example.demo

					import dev.jbaby.fullstack4j.springboot.test.MongoDbReplicaSetConfiguration
					import org.springframework.boot.fromApplication
					import org.springframework.boot.with


					fun main(args: Array<String>) {
						fromApplication<DemoApplication>().with(MongoDbReplicaSetConfiguration::class).run(*args)
					}
					""");
	}

	@Test
	void testApplicationWithGroovyIsContributed() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "fullstack4j");
		request.setLanguage("groovy");
		ProjectStructure project = generateProject(request);
		assertThat(project).textFile("src/test/groovy/com/example/demo/TestDemoApplication.groovy")
			.isEqualToNormalizingNewlines("""
					package com.example.demo

					import dev.jbaby.fullstack4j.springboot.test.MongoDbReplicaSetConfiguration
					import org.springframework.boot.SpringApplication

					class TestDemoApplication {

						static void main(String[] args) {
							SpringApplication.from(DemoApplication::main).with(MongoDbReplicaSetConfiguration).run(args)
						}

					}
					""");
	}

	@Test
	void testApplicationIsNotContributedWithoutFullstack4j() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "wicket");
		assertThat(generateProject(request))
			.doesNotContainFiles("src/test/java/com/example/demo/TestDemoApplication.java");
	}

}
