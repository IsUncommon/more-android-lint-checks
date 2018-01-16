package com.example.lint.checks;

import com.android.tools.lint.client.api.UElementHandler;
import com.android.tools.lint.detector.api.*;
import com.intellij.psi.PsiClassType;
import org.jetbrains.uast.UClass;
import org.jetbrains.uast.UElement;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

/**
 * Flags classes that implement Serializable and suggests using Parcelable, Moshi, etc.
 */
public class ImplementsSerializableDetector extends Detector implements Detector.UastScanner {

	static final Severity ISSUE_SEVERITY = Severity.ERROR;
	private static final String ISSUE_ID = ImplementsSerializableDetector.class.getSimpleName();
	private static final String ISSUE_BRIEF_DESCRIPTION = "Avoid using Serializable";
	private static final String ISSUE_EXPLANATION = "Serialization uses runtime reflection and should be avoided for favoring "
			+ "performance. Prefer using Parcelable, Moshi, or anything else that involves generating an 'adapter' at compile time.";
	private static final int ISSUE_PRIORITY = 10;   // Highest.
	static final Issue ISSUE = Issue.create(
			ISSUE_ID,
			ISSUE_BRIEF_DESCRIPTION,
			ISSUE_EXPLANATION,
			Category.PERFORMANCE,
			ISSUE_PRIORITY,
			ISSUE_SEVERITY,
			new Implementation(ImplementsSerializableDetector.class, Scope.JAVA_FILE_SCOPE)
	);

	@Override
	public EnumSet<Scope> getApplicableFiles() {
		return Scope.JAVA_FILE_SCOPE;
	}

	@Override
	public List<Class<? extends UElement>> getApplicableUastTypes() {
		return Collections.singletonList(UClass.class);
	}

	@Override
	public UElementHandler createUastHandler(JavaContext context) {
		return new UElementHandler() {
			@Override
			public void visitClass(UClass uClass) {
				PsiClassType[] superTypes = uClass.getSuperTypes();
				for (PsiClassType superType : superTypes) {
					if ("java.io.Serializable".equals(superType.getCanonicalText())) {
						context.report(ISSUE, uClass, context.getLocation((UElement) uClass),
								"Do not implement Serializable. Use Parcelable/Moshi or anything else that involves generating an 'adapter' at compile time instead.");
					}
				}
			}
		};
	}
}
