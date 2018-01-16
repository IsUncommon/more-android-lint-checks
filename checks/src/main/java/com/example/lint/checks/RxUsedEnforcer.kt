package com.example.lint.checks

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.*
import com.android.tools.lint.detector.api.Detector.UastScanner
import org.jetbrains.uast.*

/**
 * Created by rakshak_cont on 12/01/18.
 */

class RxUsedEnforcer : Detector(), UastScanner {

	override fun getApplicableUastTypes() =
			listOf(UCallExpression::class.java)

	override fun createUastHandler(context: JavaContext): UElementHandler? {
		println(context.uastFile?.asRecursiveLogString())

		return object : UElementHandler() {
			override fun visitCallExpression(node: UCallExpression) {
				val returnType = node.returnType ?: return

				val valid = RX_PRIMITIVE_CANONICAL_NAMES.any { returnType.canonicalText.startsWith(it) } &&
						node.uastParent !is UQualifiedReferenceExpression &&
						node.uastParent !is ULocalVariable &&
						node.uastParent !is UReturnExpression

				if (valid) {
					context.report(ISSUE,
							context.getCallLocation(node, false, false),
							"Make use of the returned Rx type"
					)
				}
			}
		}

	}

	companion object {

		private val RX_PRIMITIVE_CANONICAL_NAMES = listOf(
				"io.reactivex.Observable",
				"io.reactivex.Single",
				"io.reactivex.Completable",
				"io.reactivex.Maybe",
				"io.reactivex.Flowable"
		)

		internal val ISSUE = Issue.create(
				RxUsedEnforcer::class.java.simpleName,
				"Make sure function returning Rx type is being used (typically `subscribe()`)",
				"",
				Category.CORRECTNESS,
				10,
				Severity.ERROR,
				Implementation(RxUsedEnforcer::class.java, Scope.JAVA_FILE_SCOPE)
		)
	}
}
