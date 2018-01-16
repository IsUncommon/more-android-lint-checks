package com.example.lint.checks;

import org.intellij.lang.annotations.Language;
import org.junit.Test;

import static com.android.tools.lint.checks.infrastructure.TestFiles.java;
import static com.android.tools.lint.checks.infrastructure.TestLintTask.lint;

public class ImplementsSerializableDetectorTest {

	@Language("JAVA")
	private static final String CODE_WITH_SERIALIZABLE =
			"package foo;\n"
					+ "import java.io.Serializable;"
					+ "public class PoopClass implements Serializable {\n"
					+ "  public class SomeClass extends SomeClass2 {}\n"
					+ "\n"
					+ "  public class SomeClass2 implements Serializable {}\n" +
					"}\n";

	@Test
	public void whenCheckResultIsNotPresent_shouldFail() {
		lint()
				.allowMissingSdk()
				.files(java(CODE_WITH_SERIALIZABLE))
				.allowCompilationErrors()
				.issues(ImplementsSerializableDetector.ISSUE)
				.run()
				.expectCount(2, ImplementsSerializableDetector.ISSUE_SEVERITY);
	}
}
