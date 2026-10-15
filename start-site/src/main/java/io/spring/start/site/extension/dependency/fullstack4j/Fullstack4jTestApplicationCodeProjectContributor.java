/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.fullstack4j;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.nio.file.Path;
import java.util.function.Supplier;

import io.spring.initializr.generator.io.IndentingWriterFactory;
import io.spring.initializr.generator.language.ClassName;
import io.spring.initializr.generator.language.CodeBlock;
import io.spring.initializr.generator.language.Parameter;
import io.spring.initializr.generator.language.SourceCode;
import io.spring.initializr.generator.language.SourceCodeWriter;
import io.spring.initializr.generator.language.groovy.GroovyMethodDeclaration;
import io.spring.initializr.generator.language.groovy.GroovySourceCode;
import io.spring.initializr.generator.language.groovy.GroovySourceCodeWriter;
import io.spring.initializr.generator.language.groovy.GroovyTypeDeclaration;
import io.spring.initializr.generator.language.java.JavaMethodDeclaration;
import io.spring.initializr.generator.language.java.JavaSourceCode;
import io.spring.initializr.generator.language.java.JavaSourceCodeWriter;
import io.spring.initializr.generator.language.java.JavaTypeDeclaration;
import io.spring.initializr.generator.language.kotlin.KotlinFunctionDeclaration;
import io.spring.initializr.generator.language.kotlin.KotlinSourceCode;
import io.spring.initializr.generator.language.kotlin.KotlinSourceCodeWriter;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.contributor.ProjectContributor;

import org.springframework.boot.SpringApplication;

/**
 * Contributes {@code Test<Application>}, which starts the application with a MongoDB
 * replica set from Testcontainers, for {@code ./mvnw spring-boot:test-run} and
 * {@code ./gradlew bootTestRun}.
 *
 * @param <S> language-specific source code
 * @author Daniel Bartl
 */
abstract class Fullstack4jTestApplicationCodeProjectContributor<S extends SourceCode<?, ?>>
		implements ProjectContributor {

	static final ClassName MONGODB_REPLICA_SET_CONFIGURATION_CLASS_NAME = ClassName
		.of("dev.jbaby.fullstack4j.springboot.test.MongoDbReplicaSetConfiguration");

	private final ProjectDescription description;

	private final Supplier<S> sourceFactory;

	private final SourceCodeWriter<S> sourceWriter;

	Fullstack4jTestApplicationCodeProjectContributor(ProjectDescription description, Supplier<S> sourceFactory,
			SourceCodeWriter<S> sourceWriter) {
		this.description = description;
		this.sourceFactory = sourceFactory;
		this.sourceWriter = sourceWriter;
	}

	@Override
	public void contribute(Path projectRoot) throws IOException {
		S sourceCode = this.sourceFactory.get();
		contributeCode(sourceCode, this.description.getPackageName(), "Test" + this.description.getApplicationName(),
				this.description.getApplicationName());
		this.sourceWriter.writeTo(
				this.description.getBuildSystem().getTestSource(projectRoot, this.description.getLanguage()),
				sourceCode);
	}

	protected abstract void contributeCode(S sourceCode, String packageName, String testApplicationName,
			String applicationName);

	/**
	 * {@link Fullstack4jTestApplicationCodeProjectContributor} for Java.
	 */
	static class Java extends Fullstack4jTestApplicationCodeProjectContributor<JavaSourceCode> {

		Java(IndentingWriterFactory indentingWriterFactory, ProjectDescription description) {
			super(description, JavaSourceCode::new, new JavaSourceCodeWriter(indentingWriterFactory));
		}

		@Override
		protected void contributeCode(JavaSourceCode sourceCode, String packageName, String testApplicationName,
				String applicationName) {
			JavaTypeDeclaration type = sourceCode.createCompilationUnit(packageName, testApplicationName)
				.createTypeDeclaration(testApplicationName);
			type.modifiers(Modifier.PUBLIC);
			type.addMethodDeclaration(JavaMethodDeclaration.method("main")
				.modifiers(Modifier.PUBLIC | Modifier.STATIC)
				.returning("void")
				.parameters(Parameter.of("args", String[].class))
				.body(CodeBlock.ofStatement("$T.from($L::main).with($T.class).run(args)", SpringApplication.class,
						applicationName, MONGODB_REPLICA_SET_CONFIGURATION_CLASS_NAME)));
		}

	}

	/**
	 * {@link Fullstack4jTestApplicationCodeProjectContributor} for Kotlin.
	 */
	static class Kotlin extends Fullstack4jTestApplicationCodeProjectContributor<KotlinSourceCode> {

		Kotlin(IndentingWriterFactory indentingWriterFactory, ProjectDescription description) {
			super(description, KotlinSourceCode::new,
					new KotlinSourceCodeWriter(description.getLanguage(), indentingWriterFactory));
		}

		@Override
		protected void contributeCode(KotlinSourceCode sourceCode, String packageName, String testApplicationName,
				String applicationName) {
			sourceCode.createCompilationUnit(packageName, testApplicationName)
				.addTopLevelFunction(KotlinFunctionDeclaration.function("main")
					.parameters(Parameter.of("args", "Array<String>"))
					.body(CodeBlock.ofStatement("$T<$L>().$T($T::class).run(*args)",
							"org.springframework.boot.fromApplication", applicationName,
							"org.springframework.boot.with", MONGODB_REPLICA_SET_CONFIGURATION_CLASS_NAME)));
		}

	}

	/**
	 * {@link Fullstack4jTestApplicationCodeProjectContributor} for Groovy.
	 */
	static class Groovy extends Fullstack4jTestApplicationCodeProjectContributor<GroovySourceCode> {

		Groovy(IndentingWriterFactory indentingWriterFactory, ProjectDescription description) {
			super(description, GroovySourceCode::new, new GroovySourceCodeWriter(indentingWriterFactory));
		}

		@Override
		protected void contributeCode(GroovySourceCode sourceCode, String packageName, String testApplicationName,
				String applicationName) {
			GroovyTypeDeclaration type = sourceCode.createCompilationUnit(packageName, testApplicationName)
				.createTypeDeclaration(testApplicationName);
			type.modifiers(Modifier.PUBLIC);
			type.addMethodDeclaration(GroovyMethodDeclaration.method("main")
				.modifiers(Modifier.PUBLIC | Modifier.STATIC)
				.returning("void")
				.parameters(Parameter.of("args", String[].class))
				.body(CodeBlock.ofStatement("$T.from($L::main).with($T).run(args)", SpringApplication.class,
						applicationName, MONGODB_REPLICA_SET_CONFIGURATION_CLASS_NAME)));
		}

	}

}
