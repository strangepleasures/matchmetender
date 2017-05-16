# Match me tender - a simple pattern matching DSL for Java

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.strangepleasures/matchmetender/badge.svg?style=plastic)](https://maven-badges.herokuapp.com/maven-central/com.github.strangepleasures/matchmetender)


### How to use
To include Match Me Tender into your project add a Maven dependency
```xml
<dependency>
	<groupId>com.github.strangepleasures</groupId>
	<artifactId>matchmetender</artifactId>
	<version>1.1.0</version>
</dependency>
```
Most of the time the only class you need is *com.github.strangepleasures.match.Match*. Importing it with star import enables using it's methods as a DSL:
```java
import static com.github.strangepleasures.match.Match.*;
```
The core of Match Me Tender is two overloaded *match* methods:
```java
public static <T> void match(T value, MatchingConsumer<? extends T>... consumers) { ... }
public static <T, R> R match(T value, MatchingFunction<? extends T, ? extends R>... functions) { ... }
```
The first form accepts a value to match and a number of *MatchingConsumer* lambdas. When called *match* determines the type of the provided value and passes it to the first consumer with an appropriate signature (if any). A consumer wihout an explicit type (e.g. *(unknown) -> ...*) matches any non-null value. 
This form can be useful for handling multiple message types in Akka actors:
```java
public void onReceive(Object message) throws Exception {
	match(message, // a value to match
		(Foo foo) -> { 
			log.info("received a Foo: " + foo); 
			onFoo(foo);
		}, 
		(Bar bar) -> log.info("received a Bar: " + bar),
		(Baz baz) -> log.info("received a Baz: " + baz),
		(unknown) -> unhandled(unknown)
	); // this form doesn't return a value
}
```
Another *match* method has a similar semantics but accepts a list of lambda functions instead of consumers and returns a result from the matching function (or *null* if the value doesn't match any signature).
```java
String description = match(message, 
	(Foo foo) -> "It's a Foo", 
	(Bar bar) -> "It's a Bar",
	(Baz baz) -> "It's a Baz",
	(unknown) -> "I don't know what it is"
); 
```
In addition to the simple class-based matching described above, it's also possible to perform value- or condition-based matching. A family of *on* methods allows to create *MatchingConsumer*s and *MatchingFunction*s checking additional conditions:
```java
public static <T> MatchingConsumer<T> on(MatchingFunction<T, Boolean> filter, MatchingConsumer<? super T> action) { ... }
public static <T, R> MatchingFunction<T, R> on(MatchingFunction<T, Boolean> filter, MatchingFunction<? super T, R> mapper) { ... }
public static <T> MatchingConsumer<T> on(T expected, Runnable runnable) { ... }
public static <T, R> MatchingFunction<T, R> on(T expected, RunnableSupplier<R> supplier) { ... }
```
This simple example explains how to use *match* in combination with *on*:
```java
public static String classify(Integer value) {
	return match(value,
		on(null, () -> "Null"), 	 // value-based, the only way to match null
		on(n -> n < 0, n -> "Negative"), // conditional
		on(0, () -> "Zero"),             // value-based
		n -> "Positive"                  // you can mix "on" conditions with simple lambdas
	);
}
```
### Modification of local variables in the enclosing environment
A family of *com.github.strangepleasures.refs* classes allows you to modify local variables outside of *match* statements:
```java
public static void countFruits(List<Fruit> fruits) {
	IntRef apples = new IntRef(0);  // Effectively final but mutable!
	IntRef pears  = new IntRef(0);
	fruits.forEach(fruit -> match(fruit,
		(Apple apple) -> apples.value++,
		(Pear   pear) -> pears.value++
	));
	System.out.println("Found " + apples.value + " apples and " + pears.value + " pears.");
}
```
