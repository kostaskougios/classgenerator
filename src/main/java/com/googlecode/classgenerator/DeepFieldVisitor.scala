package com.googlecode.classgenerator

import java.lang.reflect.Field
import java.util.IdentityHashMap

/**
 * @author kostantinos.kougios
 *
 * 4 Jun 2012
 */
class DeepFieldVisitor(
		reflectionManager: ReflectionManager) {

	def visit[T, R](t: T)(visitor: (Any, Field, Any) => R): List[R] = visitInner(t, new IdentityHashMap, visitor)

	private def visitInner[R](o: Any, m: IdentityHashMap[Any, Any], visitor: (Any, Field, Any) => R): List[R] =
		if (!m.containsKey(o)) {
			m.put(o, o)
			val fields = reflectionManager.fields(o.getClass)
			fields.map { field =>
				val v = reflectionManager.get[Any, Any](field, o)
				val r = visitor(o, field, v)
				r :: (if (v == null) Nil else visitInner(v, m, visitor))
			}.flatten
		} else Nil
}