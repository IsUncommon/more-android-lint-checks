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

import com.android.tools.lint.client.api.IssueRegistry

/*
 * The list of issues that will be checked when running <code>lint</code>.
 */
class SampleIssueRegistry : IssueRegistry() {

	override val api = com.android.tools.lint.detector.api.CURRENT_API

	override val issues = listOf(
			RxUsedEnforcer.ISSUE,
			ImplementsSerializableDetector.ISSUE
	)
}

