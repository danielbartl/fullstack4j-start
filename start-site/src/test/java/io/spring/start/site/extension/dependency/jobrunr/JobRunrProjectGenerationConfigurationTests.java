/*
 * Copyright 2012 - present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
