# Match me tender - idiomatic pattern matching for Java

### Example:
```java
import static com.github.strangepleasures.match.Match.*;

public static long factorial(int n) {
	return Match.match(n,
		on(k -> k < 0, k -> { throw new IllegalArgumentException(); }),
		on(0, () -> 1L),
		on(1, () -> 1L),
		k -> k * factorial(k - 1)
	);
}

public double eval(Expr expr) {
	return match(expr,
		on(null, () -> { throw new IllegalArgumentException(); }),
		(Const x) -> x.value,
		(Add add) -> eval(add.left) + eval(add.right),
		(Sub sub) -> eval(sub.left) - eval(sub.right),
		(Mul mul) -> eval(mul.left) * eval(mul.right),
		(Div div) -> eval(div.left) / eval(div.right),
		(unknown) -> { throw new UnsupportedOperationException(unknown.toString()); }
	);
}
```

### Akka example:
```java
import akka.actor.*;

import static com.github.strangepleasures.match.Match.*;

public class MatchingActor extends UntypedActor {
	private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);

	public void onReceive(Object message) throws Exception {
		match(message, 
			(Foo foo) -> log.info("received a Foo: " + foo),
			(Bar bar) -> log.info("received a Bar: " + bar),
			(Baz baz) -> log.info("received a Baz: " + baz),
			(unknown) -> unhandled(unknown)
		);
	}
}
```