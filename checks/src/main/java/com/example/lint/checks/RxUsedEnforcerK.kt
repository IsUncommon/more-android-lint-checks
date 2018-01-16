package com.example.lint.checks

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.*
import com.android.tools.lint.detector.api.Detector.UastScanner
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.ULocalVariable
import org.jetbrains.uast.asRecursiveLogString
import org.jetbrains.uast.java.JavaUCompositeQualifiedExpression
import org.jetbrains.uast.java.JavaUReturnExpression
import java.util.*

/**
 * Created by rakshak_cont on 12/01/18.
 */

class RxUsedEnforcerK : Detector(), UastScanner {

	override fun getApplicableUastTypes() =
			listOf(UCallExpression::class.java)

	override fun createUastHandler(context: JavaContext): UElementHandler? {
		println(context.uastFile?.asRecursiveLogString())

		return object : UElementHandler() {
			override fun visitCallExpression(node: UCallExpression) {
				val returnType = node.returnType ?: return

				val valid = RX_PRIMITIVE_CANONICAL_NAMES.any { it.startsWith(returnType.canonicalText) } &&
						node.uastParent !is JavaUCompositeQualifiedExpression &&
						node.uastParent !is ULocalVariable &&
						node.uastParent !is JavaUReturnExpression

				println("reporting from visitCallExpression => $node => ${node.uastParent!!.javaClass.canonicalName} => ${returnType.canonicalText} => valid=$valid")
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

		private val RX_PRIMITIVE_CANONICAL_NAMES = Arrays.asList(
				"io.reactivex.Observable",
				"io.reactivex.Single",
				"io.reactivex.Completable",
				"io.reactivex.Maybe",
				"io.reactivex.Flowable"
		)

		internal val ISSUE = Issue.create(
				RxUsedEnforcerK::class.java.simpleName,
				"Make sure function returning Rx type is being used (typically `subscribe()`)",
				"",
				Category.CORRECTNESS,
				10,
				Severity.ERROR,
				Implementation(RxUsedEnforcerK::class.java, Scope.JAVA_FILE_SCOPE)
		)
	}
}
