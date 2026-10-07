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

import java.util.Collection;

import io.spring.initializr.generator.language.Language;
import io.spring.initializr.generator.project.MutableProjectDescription;
import io.spring.initializr.generator.project.ProjectDescriptionCustomizer;
import io.spring.initializr.generator.project.ProjectDescriptionField;
import io.spring.start.site.project.JavaVersionProjectDescriptionCustomizer;

/**
 * Caps the Java version of projects that depend on Apache Wicket at 25. Wicket 10.11
 * cannot create its {@code @SpringBean} proxies on Java 26 or later, where ByteBuddy may
 * no longer define classes through {@code Unsafe}; Wicket 10.12 fixes this.
 *
 * @author Daniel Bartl
 */
public class WicketJavaVersionProjectDescriptionCustomizer implements ProjectDescriptionCustomizer {

	private static final int MAX_JAVA_VERSION = 25;

	@Override
	public int getOrder() {
		return JavaVersionProjectDescriptionCustomizer.ORDER + 10;
	}

	@Override
	public void customize(MutableProjectDescription description) {
		Collection<String> dependencyIds = description.getRequestedDependencies().keySet();
		if (!dependencyIds.contains("wicket")) {
			return;
		}
		Integer javaGeneration = determineJavaGeneration(description.getLanguage().jvmVersion());
		if (javaGeneration != null && javaGeneration > MAX_JAVA_VERSION) {
			String version = String.valueOf(MAX_JAVA_VERSION);
			description.setLanguage(Language.forId(description.getLanguage().id(), version));
			description.getChanges()
				.add(ProjectDescriptionField.JVM_VERSION, "The JVM level was changed to '" + version
						+ "' as Apache Wicket 10.11 does not support Java 26 or later yet.");
		}
	}

	private Integer determineJavaGeneration(String javaVersion) {
		try {
			return Integer.parseInt(javaVersion);
		}
		catch (NumberFormatException ex) {
			return null;
		}
	}

}
