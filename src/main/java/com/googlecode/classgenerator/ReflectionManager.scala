package com.googlecode.classgenerator

import java.lang.reflect.Field
import java.util.concurrent.ConcurrentHashMap
import java.lang.reflect.Method
import java.lang.reflect.Constructor

/**
 * @author kostantinos.kougios
 *
 * 15 Apr 2012
 */
class ReflectionManager {
	def constructor[T](clz: Class[T], idx: Int): Constructor[T] =
		clz.getConstructors()(idx).asInstanceOf[Constructor[T]]

	def newInstance[T](clz: Class[T], constructorIdx: Int, args: Array[Any]) =
		constructor(clz, constructorIdx).newInstance(args.asInstanceOf[Array[Object]]: _*)

	def set[T, V](fieldName: String, o: T, v: V) {
		if (o == null) throw new NullPointerException("o is null")
		val fo = field(o.getClass, fieldName)
		val f = fo.get
		set(f, o, v)
	}

	def set[T, V](field: Field, o: T, v: V) {
		field.setAccessible(true)
		field.set(o, v)
	}

	def get[T, V](fieldName: String, o: T): V = {
		if (o == null) throw new NullPointerException("o is null")
		val fo = field(o.getClass, fieldName)
		val f = fo.get
		get(f, o)
	}

	def get[T, V](field: Field, o: T): V = {
		field.setAccessible(true)
		field.get(o).asInstanceOf[V]
	}

	def field(clz: Class[_], name: String): Option[Field] = {
		if (name == null) throw new NullPointerException("name is null")
		fields(clz).find(_.getName == name)
	}

	private val fieldsCache = new ConcurrentHashMap[Class[_], List[Field]]
	def fields(clz: Class[_]): List[Field] = {
		if (clz == null) throw new NullPointerException("clz is null")
		val fs = fieldsCache.get(clz)
		if (fs == null) {
			val ccfs = clz.getDeclaredFields.toList
			val r = if (clz.getSuperclass != null) {
				ccfs ::: fields(clz.getSuperclass)
			} else ccfs
			fieldsCache.put(clz, r)
			r
		} else fs
	}

	def method(clz: Class[_], name: String): Option[Method] = {
		if (name == null) throw new NullPointerException("name is null")
		methods(clz).find(_.getName == name)
	}

	private val methodsCache = new ConcurrentHashMap[Class[_], List[Method]]
	def methods(clz: Class[_]): List[Method] = {
		if (clz == null) throw new NullPointerException("clz is null")
		val ms = methodsCache.get(clz)
		if (ms == null) {
			val ccms = clz.getDeclaredMethods.toList
			val r = if (clz.getSuperclass != null) {
				ccms ::: methods(clz.getSuperclass)
			} else ccms
			methodsCache.put(clz, r)
			r
		} else ms
	}

	def methods[T](implicit m: ClassManifest[T]): List[Method] = methods(m.erasure)

	def copy(clz: Class[_], from: Any, to: Any) {
		fields(clz).foreach { field =>
			val v = get[Any, Any](field, from)
			set[Any, Any](field, to, v)
		}
	}
}