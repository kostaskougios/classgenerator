package com.googlecode.classgenerator

import org.junit.runner.RunWith
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import java.lang.reflect.Field

/**
 * @author kostantinos.kougios
 *
 * 4 Jun 2012
 */
@RunWith(classOf[JUnitRunner])
class DeepFieldVisitorSuite extends FunSuite with ShouldMatchers {
	val rm = new ReflectionManager
	val dfv = new DeepFieldVisitor(rm)
	test("deep visit") {
		val result = dfv.visit(Person("kostas", Dog("greta"))) { (o, field, v) =>
			(field.getName, v)
		}
		result.toSet should be === Set(
			("name", "kostas"),
			("pet", Dog("greta")),
			("name", "greta")
		)
	}

	case class Person(name: String, pet: Dog)
	case class Dog(name: String)
}