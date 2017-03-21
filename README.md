# Match me tender

## Pattern matching for Java


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