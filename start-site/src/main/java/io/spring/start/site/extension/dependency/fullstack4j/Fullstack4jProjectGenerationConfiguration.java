/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.fullstack4j;

import io.spring.initializr.generator.condition.ConditionalOnLanguage;
import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.initializr.generator.io.IndentingWriterFactory;
import io.spring.initializr.generator.language.ClassName;
import io.spring.initializr.generator.language.TypeDeclaration;
import io.spring.initializr.generator.language.groovy.GroovyLanguage;
import io.spring.initializr.generator.language.java.JavaLanguage;
import io.spring.initializr.generator.language.kotlin.KotlinLanguage;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import io.spring.initializr.generator.spring.code.TestApplicationTypeCustomizer;

import org.springframework.context.annotation.Bean;

/**
 * Configuration for generation of projects that depend on the fullstack4j Spring Boot
 * Starter. They get its test starter, and run their tests and {@code Test<Application>}
 * against a MongoDB replica set from Testcontainers, which Spring Modulith's event
 * publication registry needs for its transactions.
 *
 * @author Daniel Bartl
 */
@ProjectGenerationConfiguration
@ConditionalOnRequestedDependency("fullstack4j")
class Fullstack4jProjectGenerationConfiguration {

	@Bean
	Fullstack4jTestBuildCustomizer fullstack4jTestBuildCustomizer() {
		return new Fullstack4jTestBuildCustomizer();
	}

	@Bean
	TestApplicationTypeCustomizer<TypeDeclaration> importMongoDbReplicaSetConfigurationTestApplicationTypeCustomizer() {
		return (typeDeclaration) -> typeDeclaration.annotations()
			.addSingle(ClassName.of("org.springframework.context.annotation.Import"), (annotation) -> annotation.set(
					"value",
					Fullstack4jTestApplicationCodeProjectContributor.MONGODB_REPLICA_SET_CONFIGURATION_CLASS_NAME));
	}

	@Bean
	@ConditionalOnLanguage(JavaLanguage.ID)
	Fullstack4jTestApplicationCodeProjectContributor.Java javaFullstack4jTestApplicationCodeProjectContributor(
			IndentingWriterFactory indentingWriterFactory, ProjectDescription description) {
		return new Fullstack4jTestApplicationCodeProjectContributor.Java(indentingWriterFactory, description);
	}

	@Bean
	@ConditionalOnLanguage(KotlinLanguage.ID)
	Fullstack4jTestApplicationCodeProjectContributor.Kotlin kotlinFullstack4jTestApplicationCodeProjectContributor(
			IndentingWriterFactory indentingWriterFactory, ProjectDescription description) {
		return new Fullstack4jTestApplicationCodeProjectContributor.Kotlin(indentingWriterFactory, description);
	}

	@Bean
	@ConditionalOnLanguage(GroovyLanguage.ID)
	Fullstack4jTestApplicationCodeProjectContributor.Groovy groovyFullstack4jTestApplicationCodeProjectContributor(
			IndentingWriterFactory indentingWriterFactory, ProjectDescription description) {
		return new Fullstack4jTestApplicationCodeProjectContributor.Groovy(indentingWriterFactory, description);
	}

}
