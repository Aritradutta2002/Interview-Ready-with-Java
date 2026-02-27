# Java 8 Features - Stream API

## What is Stream?

A sequence of elements supporting sequential and parallel aggregate operations.

```java
// Traditional approach
List<String> names = new ArrayList<>();
for (String s : list) {
    if (s.length() > 3) {
        names.add(s.toUpperCase());
    }
}

// Stream approach
List<String> names = list.stream()
    .filter(s -> s.length() > 3)
    .map(String::toUpperCase)
    .collect(Collectors.toList());
```

---

## Stream Creation

```java
// From Collection
Stream<String> stream = list.stream();
Stream<String> parallel = list.parallelStream();

// From Array
Stream<String> stream = Arrays.stream(array);

// From values
Stream<String> stream = Stream.of("A", "B", "C");

// Infinite streams
Stream<Integer> infinite = Stream.iterate(0, n -> n + 1);
Stream<Integer> infinite = Stream.iterate(0, n -> n < 100, n -> n + 1);  // Java 9+
Stream<Double> randoms = Stream.generate(Math::random);

// Primitive streams
IntStream intStream = IntStream.range(1, 10);      // 1-9
IntStream intStream = IntStream.rangeClosed(1, 10); // 1-10
LongStream longStream = LongStream.of(1L, 2L, 3L);
DoubleStream doubleStream = DoubleStream.of(1.0, 2.0);
```

---

## Intermediate Operations (Lazy)

Operations that return a new Stream. Not executed until terminal operation.

### filter()
```java
Stream<T> filter(Predicate<? super T> predicate)

list.stream()
    .filter(s -> s.length() > 3)
    .filter(s -> s.startsWith("A"));  // Multiple filters
```

### map()
```java
<R> Stream<R> map(Function<? super T, ? extends R> mapper)

list.stream()
    .map(String::toUpperCase)
    .map(s -> s.length());
```

### flatMap()
```java
<R> Stream<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper)

List<List<Integer>> nested = Arrays.asList(
    Arrays.asList(1, 2),
    Arrays.asList(3, 4)
);

// Without flatMap
Stream<Stream<Integer>> streamStream = nested.stream().map(List::stream);

// With flatMap - flattens nested structures
Stream<Integer> flatStream = nested.stream().flatMap(List::stream);
// Result: 1, 2, 3, 4

// Split strings into characters
List<String> words = Arrays.asList("Hello", "World");
Stream<String> chars = words.stream().flatMap(s -> s.chars().mapToObj(c -> (char) c));
```

### distinct()
```java
list.stream().distinct()  // Removes duplicates (uses equals)
```

### sorted()
```java
list.stream().sorted()                           // Natural order
list.stream().sorted(Comparator.reverseOrder())  // Reverse
list.stream().sorted(Comparator.comparing(Person::getName))  // Custom
list.stream().sorted(Comparator.comparing(Person::getAge).thenComparing(Person::getName))
```

### peek()
```java
// For debugging - performs action on each element
list.stream()
    .peek(s -> System.out.println("Processing: " + s))
    .map(String::toUpperCase)
    .collect(Collectors.toList());
```

### limit() & skip()
```java
list.stream().limit(5)   // First 5 elements
list.stream().skip(5)    // Skip first 5, rest elements
list.stream().skip(10).limit(5)  // Pagination
```

---

## Terminal Operations

Operations that produce a result. Stream is consumed after terminal operation.

### collect()
```java
// To List
List<String> list = stream.collect(Collectors.toList());

// To Set
Set<String> set = stream.collect(Collectors.toSet());

// To Map
Map<String, Integer> map = stream.collect(
    Collectors.toMap(s -> s, String::length)
);

// To Map with merge function (handle duplicates)
Map<String, Integer> map = stream.collect(
    Collectors.toMap(s -> s, String::length, (v1, v2) -> v1)
);

// To Collection
LinkedList<String> list = stream.collect(Collectors.toCollection(LinkedList::new));

// Joining
String joined = stream.collect(Collectors.joining());        // "ABC"
String joined = stream.collect(Collectors.joining(", "));    // "A, B, C"
String joined = stream.collect(Collectors.joining(", ", "[", "]"));  // "[A, B, C]"

// Grouping
Map<Integer, List<String>> grouped = stream.collect(
    Collectors.groupingBy(String::length)
);

// Grouping with downstream collector
Map<Integer, Long> countByLength = stream.collect(
    Collectors.groupingBy(String::length, Collectors.counting())
);

// Partitioning
Map<Boolean, List<String>> partitioned = stream.collect(
    Collectors.partitioningBy(s -> s.length() > 3)
);

// Summarizing
IntSummaryStatistics stats = stream.collect(
    Collectors.summarizingInt(String::length)
);
// stats.getCount(), getSum(), getAverage(), getMin(), getMax()
```

### reduce()
```java
// Sum
Optional<Integer> sum = stream.reduce((a, b) -> a + b);
Optional<Integer> sum = stream.reduce(Integer::sum);

// Sum with identity
Integer sum = stream.reduce(0, Integer::sum);

// Product
Integer product = stream.reduce(1, (a, b) -> a * b);

// Find longest string
Optional<String> longest = stream.reduce((s1, s2) -> 
    s1.length() > s2.length() ? s1 : s2
);

// Three-argument reduce (for parallel streams)
Integer sum = stream.reduce(0, Integer::sum, Integer::sum);
```

### forEach() & forEachOrdered()
```java
stream.forEach(System.out::println);           // May be unordered in parallel
stream.forEachOrdered(System.out::println);    // Maintains encounter order
```

### toArray()
```java
String[] array = stream.toArray(String[]::new);
Object[] array = stream.toArray();
```

### count(), min(), max()
```java
long count = stream.count();
Optional<String> min = stream.min(Comparator.naturalOrder());
Optional<String> max = stream.max(Comparator.naturalOrder());
```

### findFirst() & findAny()
```java
Optional<String> first = stream.findFirst();  // Always first element
Optional<String> any = stream.findAny();      // Any element (faster in parallel)
```

### anyMatch(), allMatch(), noneMatch()
```java
boolean hasA = stream.anyMatch(s -> s.startsWith("A"));
boolean allLong = stream.allMatch(s -> s.length() > 3);
boolean noneEmpty = stream.noneMatch(String::isEmpty);
```

---

## Primitive Streams

```java
IntStream intStream = list.stream().mapToInt(String::length);
LongStream longStream = list.stream().mapToLong(String::length);
DoubleStream doubleStream = list.stream().mapToDouble(String::length);

// Special operations
int sum = intStream.sum();
OptionalDouble avg = intStream.average();
OptionalInt max = intStream.max();
IntSummaryStatistics stats = intStream.summaryStatistics();

// Convert back
Stream<Integer> stream = intStream.boxed();
```

---

## Parallel Streams

```java
// Create parallel stream
Stream<String> parallel = list.parallelStream();
Stream<String> parallel = list.stream().parallel();

// Check if parallel
boolean isParallel = stream.isParallel();

// Convert to sequential
Stream<String> sequential = parallel.sequential();

// Best practices
// ✅ Good for: Large datasets, CPU-intensive operations
// ❌ Bad for: Small datasets, I/O operations, shared mutable state
```

---

## Common Patterns

### Filter and Map
```java
List<String> names = persons.stream()
    .filter(p -> p.getAge() > 18)
    .map(Person::getName)
    .collect(Collectors.toList());
```

### Group and Count
```java
Map<String, Long> countByCity = persons.stream()
    .collect(Collectors.groupingBy(Person::getCity, Collectors.counting()));
```

### Find Max/Min
```java
Optional<Person> oldest = persons.stream()
    .max(Comparator.comparingInt(Person::getAge));
```

### Sum/Average
```java
double avgAge = persons.stream()
    .mapToInt(Person::getAge)
    .average()
    .orElse(0);
```

### Flat Map Nested Lists
```java
List<String> allNames = departments.stream()
    .flatMap(d -> d.getEmployees().stream())
    .map(Employee::getName)
    .distinct()
    .collect(Collectors.toList());
```

---

## Interview Q&A

**Q: What is the difference between map() and flatMap()?**
A: map() produces one output per input. flatMap() can produce multiple outputs (flattens nested structures).

**Q: Why are intermediate operations lazy?**
A: They don't process until terminal operation. Enables optimization and short-circuiting.

**Q: What is the difference between findFirst() and findAny()?**
A: findFirst() returns first element (deterministic). findAny() returns any element (faster in parallel).

**Q: Can stream be reused?**
A: No. After terminal operation, stream is consumed. Create new stream from source.

**Q: What is the difference between Collection.stream() and Collection.parallelStream()?**
A: stream() processes sequentially. parallelStream() splits work across threads.

**Q: When to use parallel stream?**
A: Large datasets, CPU-intensive operations, stateless operations, independent elements.

**Q: What is short-circuiting in streams?**
A: Operations that can terminate early: findFirst, findAny, anyMatch, allMatch, noneMatch, limit.
