/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.jobrunr;

import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link JobRunrProjectGenerationConfiguration}.
 *
 * @author Daniel Bartl
 */
class JobRunrProjectGenerationConfigurationTests extends AbstractExtensionTests {

	@Test
	void backgroundJobServerAndDashboardAreEnabled() {
		ProjectRequest request = createProjectRequest("jobrunr", "data-jpa", "h2");
		assertThat(applicationProperties(request)).lines()
			.contains("jobrunr.background-job-server.enabled=true", "jobrunr.dashboard.enabled=true")
			.doesNotContain("spring.mongodb.representation.uuid=standard");
	}

	@Test
	void uuidRepresentationIsSetWithMongoDb() {
		ProjectRequest request = createProjectRequest("jobrunr", "data-mongodb");
		assertThat(applicationProperties(request)).lines().contains("spring.mongodb.representation.uuid=standard");
	}

	@Test
	void nothingIsAddedWithoutJobRunr() {
		ProjectRequest request = createProjectRequest("data-mongodb");
		assertThat(applicationProperties(request)).lines()
			.noneMatch((line) -> line.startsWith("jobrunr.") || line.startsWith("spring.mongodb.representation"));
	}

}
