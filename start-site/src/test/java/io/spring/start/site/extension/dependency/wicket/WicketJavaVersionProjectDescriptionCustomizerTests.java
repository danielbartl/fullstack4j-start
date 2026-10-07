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

package io.spring.start.site.extension.dependency.wicket;

import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.SupportedBootVersion;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link WicketJavaVersionProjectDescriptionCustomizer}.
 *
 * @author Daniel Bartl
 */
class WicketJavaVersionProjectDescriptionCustomizerTests extends AbstractExtensionTests {

	private static final SupportedBootVersion BOOT_VERSION = SupportedBootVersion.latest();

	@ParameterizedTest
	@ValueSource(strings = { "17", "21", "25" })
	void javaUpTo25IsLeftAsIs(String jvmVersion) {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "wicket");
		request.setJavaVersion(jvmVersion);
		assertThat(mavenPom(request)).hasProperty("java.version", jvmVersion);
	}

	@Test
	void javaAbove25IsLoweredTo25() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "wicket");
		request.setJavaVersion("27");
		assertThat(mavenPom(request)).hasProperty("java.version", "25");
	}

	@Test
	void warningAddedWhenJavaVersionIsLowered() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "wicket");
		request.setJavaVersion("27");
		assertThat(helpDocument(request)).lines()
			.containsSubsequence("# Read Me First",
					"* The JVM level was changed to '25' as Apache Wicket 10.11 does not support Java 26 or later yet.");
	}

	@Test
	void javaVersionIsLeftAsIsWithoutWicket() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "web");
		request.setJavaVersion("27");
		assertThat(mavenPom(request)).hasProperty("java.version", "27");
	}

}
