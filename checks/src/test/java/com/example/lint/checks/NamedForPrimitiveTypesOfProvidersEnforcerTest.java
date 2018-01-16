package com.example.lint.checks;

import org.intellij.lang.annotations.Language;
import org.junit.Test;

import static com.android.tools.lint.checks.infrastructure.TestFiles.java;
import static com.android.tools.lint.checks.infrastructure.TestLintTask.lint;

public class NamedForPrimitiveTypesOfProvidersEnforcerTest {

	@Test
	public void whenCodeHasNoPrimitives_shouldPass() {

		@Language("JAVA") final String codeWithNoPrimitives = "package foo;"
				+ "@dagger.Module\n"
				+ "public abstract class MainModule {\n"
				+ "\n"
				+ "\t@dagger.Provides\n"
				+ "\tstatic NonPrimitive provides() {\n"
				+ "\t\treturn \"Unused Data\";\n"
				+ "\t}\n"
				+ "\n"
				+ "\tstatic int providesAnother() {\n"
				+ "\t\treturn \"Unused Data\";\n"
				+ "\t}\n"
				+ "\n"
				+ "}";

		lint()
				.allowMissingSdk()
				.files(java(codeWithNoPrimitives))
				.allowCompilationErrors()
				.issues(NamedForPrimitiveTypesOfProvidersEnforcer.ISSUE)
				.run()
				.expectClean();
	}

	@Test
	public void whenCodeHasPrimitives_shouldFail() {

		@Language("JAVA") final String codeWithPrimitives = "package foo;"
				+ "@dagger.Module\n"
				+ "public abstract class MainModule {\n"
				+ "\n"
				+ "\t@dagger.Provides\n"
				+ "\tstatic int providesUnusedData() {\n"
				+ "\t\treturn 0;\n"
				+ "\t}\n"
				+ "\n"
				+ "}";

		lint()
				.allowMissingSdk()
				.files(java(codeWithPrimitives))
				.allowCompilationErrors()
				.issues(NamedForPrimitiveTypesOfProvidersEnforcer.ISSUE)
				.run()
				.expectCount(1, NamedForPrimitiveTypesOfProvidersEnforcer.SEVERITY);
	}

	@Test
	public void whenCodeHasPrimitivesAndNamed_thenPass() {

		@Language("JAVA") final String codeWithPrimitives = "package foo;"
				+ "@dagger.Module\n"
				+ "public abstract class MainModule {\n"
				+ "\n"
				+ "\t@dagger.Provides\n"
				+ "\t@javax.inject.Named(\"\")\n"
				+ "\tstatic int providesUnusedData() {\n"
				+ "\t\treturn 0;\n"
				+ "\t}\n"
				+ "\n"
				+ "}";

		lint()
				.allowMissingSdk()
				.files(java(codeWithPrimitives))
				.allowCompilationErrors()
				.issues(NamedForPrimitiveTypesOfProvidersEnforcer.ISSUE)
				.run()
				.expectClean();
	}
}
