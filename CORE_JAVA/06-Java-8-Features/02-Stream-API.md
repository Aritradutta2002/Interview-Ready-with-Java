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

### Advanced Pattern: Custom Collector
```java
// Collect to immutable list
List<String> immutableList = stream.collect(
    Collectors.collectingAndThen(
        Collectors.toList(),
        Collections::unmodifiableList
    )
);

// Collect with custom logic
Collector<Person, ?, Map<String, Person>> toMap = Collector.of(
    HashMap::new,                           // Supplier
    (map, person) -> map.put(person.getId(), person),  // Accumulator
    (map1, map2) -> { map1.putAll(map2); return map1; },  // Combiner
    Collector.Characteristics.IDENTITY_FINISH
);
```

### Advanced Pattern: Teeing (Java 12+)
```java
// Calculate average and count in one pass
record Stats(double average, long count) {}

Stats stats = numbers.stream()
    .collect(Collectors.teeing(
        Collectors.averagingDouble(Integer::doubleValue),
        Collectors.counting(),
        Stats::new
    ));
```

### Advanced Pattern: Partitioning with Downstream
```java
// Partition by age and count each partition
Map<Boolean, Long> partitioned = persons.stream()
    .collect(Collectors.partitioningBy(
        p -> p.getAge() >= 18,
        Collectors.counting()
    ));
// {false=5, true=15} (5 minors, 15 adults)
```

### Advanced Pattern: Multi-level Grouping
```java
// Group by city, then by age group
Map<String, Map<String, List<Person>>> grouped = persons.stream()
    .collect(Collectors.groupingBy(
        Person::getCity,
        Collectors.groupingBy(p -> 
            p.getAge() < 18 ? "Minor" : 
            p.getAge() < 65 ? "Adult" : "Senior"
        )
    ));
```

### Advanced Pattern: Filtering After Grouping
```java
// Group by city, keep only cities with >5 people
Map<String, List<Person>> filtered = persons.stream()
    .collect(Collectors.groupingBy(
        Person::getCity,
        Collectors.filtering(
            p -> p.getAge() > 18,
            Collectors.toList()
        )
    ));
```

### Advanced Pattern: Mapping After Grouping
```java
// Group by city, collect only names
Map<String, List<String>> namesByCity = persons.stream()
    .collect(Collectors.groupingBy(
        Person::getCity,
        Collectors.mapping(
            Person::getName,
            Collectors.toList()
        )
    ));
```

### Advanced Pattern: FlatMapping After Grouping (Java 9+)
```java
// Group by department, collect all skills
Map<String, Set<String>> skillsByDept = employees.stream()
    .collect(Collectors.groupingBy(
        Employee::getDepartment,
        Collectors.flatMapping(
            e -> e.getSkills().stream(),
            Collectors.toSet()
        )
    ));
```

### Advanced Pattern: Reducing After Grouping
```java
// Group by city, sum salaries
Map<String, Integer> salaryByCity = persons.stream()
    .collect(Collectors.groupingBy(
        Person::getCity,
        Collectors.reducing(
            0,
            Person::getSalary,
            Integer::sum
        )
    ));
```

### Advanced Pattern: Joining with Custom Delimiter
```java
// Join names with comma, prefix, suffix
String names = persons.stream()
    .map(Person::getName)
    .collect(Collectors.joining(", ", "Names: [", "]"));
// "Names: [John, Jane, Bob]"
```

### Advanced Pattern: Summarizing Statistics
```java
IntSummaryStatistics stats = persons.stream()
    .mapToInt(Person::getAge)
    .summaryStatistics();

System.out.println("Count: " + stats.getCount());
System.out.println("Sum: " + stats.getSum());
System.out.println("Min: " + stats.getMin());
System.out.println("Max: " + stats.getMax());
System.out.println("Average: " + stats.getAverage());
```

### Advanced Pattern: Distinct by Property
```java
// Distinct by name (not entire object)
List<Person> distinctByName = persons.stream()
    .collect(Collectors.toMap(
        Person::getName,
        p -> p,
        (p1, p2) -> p1  // Keep first on duplicate
    ))
    .values()
    .stream()
    .collect(Collectors.toList());

// Or using custom collector
List<Person> distinctByName = persons.stream()
    .collect(Collectors.collectingAndThen(
        Collectors.toCollection(() -> 
            new TreeSet<>(Comparator.comparing(Person::getName))
        ),
        ArrayList::new
    ));
```

### Advanced Pattern: Conditional Accumulation
```java
// Sum salaries only for adults
int totalAdultSalary = persons.stream()
    .filter(p -> p.getAge() >= 18)
    .mapToInt(Person::getSalary)
    .sum();

// Or using reduce with condition
int total = persons.stream()
    .reduce(0,
        (sum, p) -> p.getAge() >= 18 ? sum + p.getSalary() : sum,
        Integer::sum
    );
```

### Advanced Pattern: Top N Elements
```java
// Top 5 highest paid employees
List<Person> top5 = persons.stream()
    .sorted(Comparator.comparingInt(Person::getSalary).reversed())
    .limit(5)
    .collect(Collectors.toList());
```

### Advanced Pattern: Pagination
```java
// Page 3, size 10 (skip 20, take 10)
List<Person> page = persons.stream()
    .skip(2 * 10)
    .limit(10)
    .collect(Collectors.toList());
```

### Advanced Pattern: Conditional Mapping
```java
// Map to uppercase if length > 3, else lowercase
List<String> result = words.stream()
    .map(s -> s.length() > 3 ? s.toUpperCase() : s.toLowerCase())
    .collect(Collectors.toList());
```

### Advanced Pattern: Nested Optional Handling
```java
// Find person by ID, then get their address city
Optional<String> city = findPersonById(id)
    .flatMap(Person::getAddress)
    .map(Address::getCity);
```

### Advanced Pattern: Parallel Stream with Custom Pool
```java
// Use custom ForkJoinPool instead of common pool
ForkJoinPool customPool = new ForkJoinPool(4);
try {
    List<String> result = customPool.submit(() ->
        list.parallelStream()
            .map(String::toUpperCase)
            .collect(Collectors.toList())
    ).get();
} catch (Exception e) {
    e.printStackTrace();
}
```

### Real-World Pattern: CSV Processing
```java
// Read CSV, parse, filter, transform
List<Employee> employees = Files.lines(Paths.get("employees.csv"))
    .skip(1)  // Skip header
    .map(line -> line.split(","))
    .filter(parts -> parts.length == 4)
    .map(parts -> new Employee(
        parts[0],
        Integer.parseInt(parts[1]),
        parts[2],
        Double.parseDouble(parts[3])
    ))
    .filter(e -> e.getSalary() > 50000)
    .collect(Collectors.toList());
```

### Real-World Pattern: Word Frequency Count
```java
Map<String, Long> wordFrequency = Files.lines(Paths.get("text.txt"))
    .flatMap(line -> Arrays.stream(line.split("\\s+")))
    .map(String::toLowerCase)
    .collect(Collectors.groupingBy(
        Function.identity(),
        Collectors.counting()
    ));
```

### Real-World Pattern: Finding Duplicates
```java
// Find duplicate names
Set<String> duplicates = persons.stream()
    .map(Person::getName)
    .collect(Collectors.groupingBy(
        Function.identity(),
        Collectors.counting()
    ))
    .entrySet().stream()
    .filter(e -> e.getValue() > 1)
    .map(Map.Entry::getKey)
    .collect(Collectors.toSet());
```

### Real-World Pattern: Merging Maps
```java
Map<String, Integer> map1 = Map.of("A", 1, "B", 2);
Map<String, Integer> map2 = Map.of("B", 3, "C", 4);

Map<String, Integer> merged = Stream.of(map1, map2)
    .flatMap(m -> m.entrySet().stream())
    .collect(Collectors.toMap(
        Map.Entry::getKey,
        Map.Entry::getValue,
        Integer::sum  // Merge function for duplicates
    ));
// {A=1, B=5, C=4}
```

---

## Interview Q&A

### Basic Level

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

**Q: What is short-circuiting in streams?**
A: Operations that can terminate early: findFirst, findAny, anyMatch, allMatch, noneMatch, limit.

**Q: What is the difference between intermediate and terminal operations?**
A: Intermediate operations return a Stream and are lazy (filter, map, sorted). Terminal operations produce a result and consume the stream (collect, forEach, reduce).

**Q: What is the difference between forEach() and forEachOrdered()?**
A: forEach() may process elements in any order in parallel streams. forEachOrdered() maintains encounter order even in parallel.

### Intermediate Level

**Q: When to use parallel stream?**
A: Large datasets (>10,000 elements), CPU-intensive operations, stateless operations, independent elements. Avoid for I/O operations or small datasets.

**Q: What is the difference between Stream.of() and Arrays.stream()?**
A: Stream.of() creates a stream from varargs. Arrays.stream() creates a stream from an array. For primitive arrays, Arrays.stream() returns IntStream/LongStream/DoubleStream, while Stream.of() wraps the entire array as a single element.

**Q: What is the difference between reduce() and collect()?**
A: reduce() combines elements into a single result using a binary operator. collect() accumulates elements into a mutable container (List, Set, Map).

**Q: What is the difference between filter() and skip()?**
A: filter() removes elements based on a predicate. skip(n) removes the first n elements regardless of content.

**Q: What is the difference between limit() and takeWhile()?**
A: limit(n) takes first n elements. takeWhile() takes elements while predicate is true, then stops (Java 9+).

**Q: What is the difference between anyMatch() and findAny()?**
A: anyMatch() returns boolean (does any element match?). findAny() returns Optional<T> (give me any matching element).

**Q: What is the difference between count() and size()?**
A: count() is a terminal operation on Stream. size() is a method on Collection. count() processes the stream, size() is instant.

**Q: What is the difference between Collectors.toList() and Collectors.toCollection()?**
A: toList() returns an unspecified List implementation. toCollection() lets you specify the exact collection type (e.g., ArrayList, LinkedList).

### Advanced Level

**Q: How does parallel stream work internally?**
A: Uses ForkJoinPool (default common pool with parallelism = CPU cores - 1). Splits stream into chunks, processes in parallel, then combines results.

**Q: What is the difference between stateless and stateful operations?**
A: Stateless operations (filter, map) process each element independently. Stateful operations (sorted, distinct) need to see all elements or maintain state across elements.

**Q: What is the performance impact of sorted() in streams?**
A: O(n log n) time complexity. Stateful operation that buffers all elements. Expensive for large streams. Consider using TreeSet or pre-sorted data.

**Q: What is the difference between peek() and forEach()?**
A: peek() is intermediate (returns Stream, for debugging). forEach() is terminal (consumes stream, for side effects).

**Q: How does reduce() work with parallel streams?**
A: Uses three-argument form: identity, accumulator, combiner. Combiner merges partial results from different threads. Must be associative.

**Q: What is the difference between Collectors.groupingBy() and Collectors.partitioningBy()?**
A: groupingBy() groups by any classifier (returns Map<K, List<V>>). partitioningBy() groups by boolean predicate (returns Map<Boolean, List<V>>).

**Q: What is the difference between Optional.of() and Optional.ofNullable()?**
A: Optional.of() throws NullPointerException if value is null. Optional.ofNullable() returns Optional.empty() if value is null.

**Q: How do you handle exceptions in stream operations?**
A: Wrap in try-catch inside lambda, use custom wrapper method, or use libraries like Vavr. Streams don't handle checked exceptions well.

**Q: What is the difference between IntStream.range() and IntStream.rangeClosed()?**
A: range(1, 10) generates 1-9 (exclusive end). rangeClosed(1, 10) generates 1-10 (inclusive end).

### Scenario-Based Questions

**Q: What will this code print?**
```java
List<Integer> list = Arrays.asList(1, 2, 3);
list.stream()
    .peek(x -> System.out.print(x + " "))
    .filter(x -> x > 5);
```
A: Nothing. No terminal operation, so the stream is never executed (lazy evaluation).

**Q: What will this code print?**
```java
Stream.of("a", "b", "c")
    .filter(s -> {
        System.out.println("filter: " + s);
        return true;
    })
    .map(s -> {
        System.out.println("map: " + s);
        return s.toUpperCase();
    })
    .findFirst();
```
A: "filter: a" then "map: a". Short-circuiting - stops after finding first element.

**Q: What's wrong with this code?**
```java
List<String> list = new ArrayList<>();
IntStream.range(0, 1000)
    .parallel()
    .forEach(i -> list.add("Item" + i));
```
A: Race condition. ArrayList is not thread-safe. Use Collectors.toList() or ConcurrentHashMap instead.

**Q: What will this code return?**
```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
numbers.parallelStream()
    .reduce(10, Integer::sum);
```
A: 65 (not 25). In parallel streams, identity is added multiple times (once per thread). Use reduce(0, Integer::sum) + 10 instead.

**Q: What's the output?**
```java
Stream<String> stream = Stream.of("a", "b", "c");
stream.forEach(System.out::println);
stream.forEach(System.out::println);
```
A: IllegalStateException on second forEach. Stream already consumed.

**Q: What will this code print?**
```java
List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
list.stream()
    .filter(x -> x % 2 == 0)
    .map(x -> x * 2)
    .forEach(System.out::println);
```
A: 4, 8 (even numbers doubled).

**Q: What's the difference between these two?**
```java
// Option 1
list.stream().filter(x -> x > 5).count();
// Option 2
list.stream().filter(x -> x > 5).collect(Collectors.counting());
```
A: Both return count, but count() is more efficient (returns long directly). Collectors.counting() is for use with groupingBy/partitioningBy.

### Tricky Interview Scenarios

**Q: What will this code print?**
```java
List<String> list = Arrays.asList("a", "b", "c");
list.stream()
    .peek(s -> list.add("d"))
    .forEach(System.out::println);
```
A: ConcurrentModificationException. Modifying source collection during stream operation.

**Q: What's the output?**
```java
Stream.of(1, 2, 3, 4, 5)
    .filter(x -> {
        System.out.println("filter: " + x);
        return x % 2 == 0;
    })
    .map(x -> {
        System.out.println("map: " + x);
        return x * 2;
    })
    .collect(Collectors.toList());
```
A: "filter: 1", "filter: 2", "map: 2", "filter: 3", "filter: 4", "map: 4", "filter: 5". Operations are fused - each element goes through entire pipeline before next element starts.

**Q: What will this return?**
```java
List<String> list = Arrays.asList("a", "b", "c");
list.stream()
    .flatMap(s -> Stream.of(s, s.toUpperCase()))
    .collect(Collectors.toList());
```
A: [a, A, b, B, c, C]. flatMap produces 2 elements per input.

**Q: What's wrong with this code?**
```java
Stream<Integer> stream = Stream.iterate(0, n -> n + 1);
stream.forEach(System.out::println);
```
A: Infinite loop. Stream.iterate() creates infinite stream. Use limit() to bound it.

**Q: What will this code print?**
```java
Optional<String> opt = Optional.of("hello");
opt.map(String::toUpperCase)
   .filter(s -> s.length() > 10)
   .ifPresent(System.out::println);
```
A: Nothing. filter() returns empty Optional because "HELLO".length() is 5, not > 10.


---

## Common Traps

### ❌ Trap 1: Reusing Streams

**Why it's wrong**:
Streams can only be consumed once. After terminal operation, stream is closed.

**Incorrect Code**:
```java
Stream<String> stream = list.stream();
long count = stream.count();
List<String> result = stream.collect(Collectors.toList());  // ❌ IllegalStateException!
```

**Correct Code**:
```java
// Create new stream for each operation
long count = list.stream().count();
List<String> result = list.stream().collect(Collectors.toList());

// Or store the result
List<String> result = list.stream().collect(Collectors.toList());
long count = result.size();
```

**Interview Tip**:
Streams are single-use. Create a new stream from the source for each pipeline.

---

### ❌ Trap 2: Modifying Source During Stream Operations

**Why it's wrong**:
Modifying the source collection during stream operations causes ConcurrentModificationException or unpredictable behavior.

**Incorrect Code**:
```java
List<String> list = new ArrayList<>(Arrays.asList("A", "B", "C"));
list.stream()
    .peek(s -> list.add("D"))  // ❌ Modifying source!
    .forEach(System.out::println);
// ConcurrentModificationException or unpredictable behavior
```

**Correct Code**:
```java
List<String> list = new ArrayList<>(Arrays.asList("A", "B", "C"));
List<String> newList = list.stream()
    .collect(Collectors.toList());
newList.add("D");  // ✅ Modify result, not source
```

**Interview Tip**:
Never modify the source collection during stream operations. Streams should be side-effect free.

---

### ❌ Trap 3: Using Parallel Streams with Shared Mutable State

**Why it's wrong**:
Parallel streams execute concurrently. Shared mutable state causes race conditions.

**Incorrect Code**:
```java
List<Integer> results = new ArrayList<>();  // ❌ Not thread-safe
IntStream.range(0, 1000)
    .parallel()
    .forEach(i -> results.add(i));  // Race condition!
// results.size() might be < 1000
```

**Correct Code**:
```java
// Use thread-safe collection
List<Integer> results = IntStream.range(0, 1000)
    .parallel()
    .boxed()
    .collect(Collectors.toList());  // ✅ Thread-safe collector

// Or use ConcurrentHashMap for complex cases
Map<Integer, String> map = IntStream.range(0, 1000)
    .parallel()
    .boxed()
    .collect(Collectors.toConcurrentMap(i -> i, i -> "Value" + i));
```

**Interview Tip**:
Avoid shared mutable state in parallel streams. Use collectors or thread-safe collections.

---

### ❌ Trap 4: Incorrect Use of reduce() with Parallel Streams

**Why it's wrong**:
Without proper identity and combiner, reduce() produces wrong results in parallel streams.

**Incorrect Code**:
```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
Integer sum = numbers.parallelStream()
    .reduce(10, Integer::sum);  // ❌ Wrong result!
// Result: 40 (10 added multiple times in parallel)
// Expected: 25 (10 + 1 + 2 + 3 + 4 + 5)
```

**Correct Code**:
```java
// Option 1: Use identity 0 for sum
Integer sum = numbers.parallelStream()
    .reduce(0, Integer::sum);  // ✅ Correct: 15

// Option 2: Add initial value after reduction
Integer sum = numbers.parallelStream()
    .reduce(0, Integer::sum) + 10;  // ✅ Correct: 25

// Option 3: Use three-argument reduce with combiner
Integer sum = numbers.parallelStream()
    .reduce(10, Integer::sum, Integer::sum);  // Still wrong for this case

// Best: Use sum() for primitive streams
int sum = numbers.stream()
    .mapToInt(Integer::intValue)
    .sum() + 10;  // ✅ Correct: 25
```

**Interview Tip**:
Identity value in reduce() must be neutral (0 for sum, 1 for product). For parallel streams, ensure associativity.

---

### ❌ Trap 5: Overusing Parallel Streams

**Why it's wrong**:
Parallel streams have overhead. For small datasets or I/O operations, they're slower than sequential.

**Incorrect Code**:
```java
// Small dataset - parallel overhead > benefit
List<Integer> small = Arrays.asList(1, 2, 3, 4, 5);
int sum = small.parallelStream()  // ❌ Slower than sequential!
    .mapToInt(Integer::intValue)
    .sum();

// I/O operation - parallel doesn't help
List<String> urls = getUrls();
urls.parallelStream()  // ❌ I/O bound, not CPU bound
    .forEach(url -> downloadFile(url));
```

**Correct Code**:
```java
// Use sequential for small datasets
List<Integer> small = Arrays.asList(1, 2, 3, 4, 5);
int sum = small.stream()  // ✅ Sequential is faster
    .mapToInt(Integer::intValue)
    .sum();

// Use parallel only for CPU-intensive operations on large datasets
List<Integer> large = IntStream.range(0, 1_000_000).boxed().collect(Collectors.toList());
int sum = large.parallelStream()  // ✅ Parallel beneficial here
    .mapToInt(i -> expensiveComputation(i))
    .sum();
```

**Interview Tip**:
Use parallel streams only for large datasets with CPU-intensive operations. Measure performance before using parallel.

---

## Related Topics

- [Lambda & Functional Interfaces](./01-Lambda-Functional-Interfaces.md) - Foundation for stream operations
- [Optional & CompletableFuture](./03-Optional-CompletableFuture.md) - Handling optional results from streams
- [Collections Framework](../04-Collections-Framework/01-List-Implementations.md) - Source of streams
- [Generics](../05-Generics/01-Generics.md) - Type safety in streams
- [Multithreading & Concurrency](../08-Multithreading-Concurrency/01-Thread-Basics.md) - Parallel stream execution
- [Exception Handling](../03-Exception-Handling/01-Exception-Handling.md) - Handling exceptions in stream operations

---

*Last Updated: February 2026*
*Java Version: 8, 11, 17, 21*
