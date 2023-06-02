/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.lint.checks

import com.android.tools.lint.checks.infrastructure.LintDetectorTest.java
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import com.zybang.lint_rules.reference.VersionCodeReference
import org.junit.Test

@Suppress("UnstableApiUsage")
class VersionCodeFinderTest {
    @Test
    fun testBasic() {
        lint().allowMissingSdk().files(
            java(
                """
                    package test.pkg;
                    public class BuildConfig {
                        public static final int VERSION_CODE = 1;
                    }
                    public class TestClass1 {
                        // In a comment, mentioning "lint" has no effect
                        private static String s1 = "Ignore non-word usages: linting";
                        private static int s2 = BuildConfig.VERSION_CODE;
                    }
                    """
            ).indented()
        )
            .issues(VersionCodeReference.ISSUE)
            .run()
            .expect(
                """
                    src/test/pkg/BuildConfig.java:8: Error: Using BuildConfig.VERSION_CODE to get app version code is not recommended [BuildVersionCode]
    private static int s2 = BuildConfig.VERSION_CODE;
                                        ~~~~~~~~~~~~
1 errors, 0 warnings
                    """
            )
    }
}
