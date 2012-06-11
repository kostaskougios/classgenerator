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
		val result = dfv.visit(Person("kostas", Dog("greta", 5), 20)) { (o, field, v) =>
			(field.getName, v)
		}
		result.toSet should be === Set(
			("name", "kostas"),
			("pet", Dog("greta", 5)),
			("age", 20),
			("name", "greta"),
			("age", 5)
		)
	}
	test("deep visit, nulls") {
		val result = dfv.visit(Person("kostas", null, 20)) { (o, field, v) =>
			(field.getName, v)
		}
		result.toSet should be === Set(
			("name", "kostas"),
			("pet", null),
			("age", 20)
		)
	}

	case class Person(name: String, pet: Dog, age: Int)
	case class Dog(name: String, age: Int)
}