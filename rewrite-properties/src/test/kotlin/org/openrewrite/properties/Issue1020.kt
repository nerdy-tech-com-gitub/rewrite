/*
 * Copyright 2021 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openrewrite.properties

import org.junit.jupiter.api.Test
import org.openrewrite.Issue

@Suppress("UnusedProperty")
class Issue1020 : PropertiesRecipeTest {

    @Issue("https://github.com/openrewrite/rewrite/issues/1020")
    @Test
    fun removalOfDoublePound() = assertUnchanged(
        recipe = ChangePropertyKey("server.port", "chassis.name", null, null),
        before = """
            key=**##**chassis.management.metrics.export.cloudwatch.awsAccessKey
        """
    )

    @Issue("https://github.com/openrewrite/rewrite/issues/1020")
    @Test
    fun removalOfSlashPound() = assertUnchanged(
        recipe = ChangePropertyValue("server.tomcat.accesslog.enabled", "true", null, null, null),
        before = """
            boot.features=https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle**/#**boot-features-jersey
            server.tomcat.accesslog.enabled=true
        """
    )
}
