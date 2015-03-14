Generates new classes, implementing and overriding super classes and interfaces and provides easy implementation of patterns i.e. proxy and lazy loading.

ClassGenerator is based on javassist, and provides various builders for dynamically generating classes.


```

class TestSuperclass {
	val superVal = "x"
}

val clz = classManager.buildNewClass("test.Proceed1")
	.superClass[TestSuperclass]
	.method("superVal")
	.returnType[String]
	.enableSuperMethodInvocation
	.implementation
	.get
val i = clz.newInstance
i.methodImplementation { args =>
	val s = args.callSuper
	s + "-mod"
}
i.superVal should be("x-mod")
```



### Examples ###

Please look at the test cases for usage examples:

[Examples](https://code.google.com/p/classgenerator/source/browse/#git%2Fsrc%2Ftest%2Fjava%2Fcom%2Fgooglecode%2Fclassgenerator)

### Maven ###

```
<dependency>
	<groupId>com.googlecode.classgenerator</groupId>
	<artifactId>classgenerator</artifactId>
	<version>...</version>
</dependency>
```