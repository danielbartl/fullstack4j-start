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

import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.initializr.generator.spring.properties.ApplicationPropertiesCustomizer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for generation of projects that depend on JobRunr.
 *
 * @author Daniel Bartl
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnRequestedDependency("jobrunr")
class JobRunrProjectGenerationConfiguration {

	/**
	 * JobRunr ships with its background job server and dashboard switched off, so a new
	 * project would enqueue jobs that never run.
	 * @return the customizer that switches both on
	 */
	@Bean
	ApplicationPropertiesCustomizer jobRunrServerAndDashboardApplicationPropertiesCustomizer() {
		return (properties) -> {
			properties.add("jobrunr.background-job-server.enabled", true);
			properties.add("jobrunr.dashboard.enabled", true);
		};
	}

	/**
	 * JobRunr refuses to start against MongoDB unless the driver's UUID representation is
	 * set explicitly, which it is not by default since version 4 of the driver.
	 * @return the customizer that sets the UUID representation
	 */
	@Bean
	@ConditionalOnRequestedDependency("data-mongodb")
	ApplicationPropertiesCustomizer jobRunrMongoDbApplicationPropertiesCustomizer() {
		return (properties) -> properties.add("spring.mongodb.representation.uuid", "standard");
	}

}
