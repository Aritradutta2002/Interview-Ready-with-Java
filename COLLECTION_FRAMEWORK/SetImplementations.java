package COLLECTION_FRAMEWORK;

import java.util.*;

/**
 * Complete Set Implementations for Interview Preparation
 * 
 * INTERVIEW TOPICS COVERED:
 * 1. HashSet vs LinkedHashSet vs TreeSet
 * 2. Internal implementation details
 * 3. Performance characteristics
 * 4. NavigableSet and SortedSet operations
 * 5. Custom equals() and hashCode() impact
 * 6. Set operations (union, intersection, difference)
 */
public class SetImplementations {
    
    public static void main(String[] args) {
        demonstrateBasicSetOperations();
        compareSetImplementations();
        demonstrateHashSetInternals();
        demonstrateLinkedHashSet();
        demonstrateTreeSetOperations();
        demonstrateNavigableSetOperations();
        demonstrateSetMathOperations();
        analyzePerformance();
        showBestPractices();
    }
    
    /**
     * Basic Set operations common to all implementations
     */
    public static void demonstrateBasicSetOperations() {
        System.out.println("=== BASIC SET OPERATIONS ===\n");
        
        Set<String> fruits = new HashSet<>();
        
        // Adding elements (duplicates ignored)
        fruits.add("Apple");
        fruits.add("Banana");
        fruits.add("Cherry");
        fruits.add("Apple"); // Duplicate - will be ignored
        
        System.out.println("Set after additions: " + fruits);
        System.out.println("Size: " + fruits.size());
        
        // Checking membership
        System.out.println("Contains 'Apple': " + fruits.contains("Apple"));
        System.out.println("Contains 'Grape': " + fruits.contains("Grape"));
        
        // Removing elements
        boolean removed = fruits.remove("Banana");
        System.out.println("Removed 'Banana': " + removed);
        System.out.println("Set after removal: " + fruits);
        
        // Iteration
        System.out.println("Iterating through set:");
        for (String fruit : fruits) {
            System.out.println("- " + fruit);
        }
        
        // Set operations with collections
        Set<String> moreFruits = Set.of("Date", "Elderberry", "Apple");
        fruits.addAll(moreFruits); // Union operation
        System.out.println("After adding more fruits: " + fruits);
        
        // Clear and empty check
        fruits.clear();
        System.out.println("Is empty after clear: " + fruits.isEmpty());
        
        System.out.println("\nKEY SET CHARACTERISTICS:");
        System.out.println("✓ No duplicate elements");
        System.out.println("✓ At most one null element (except TreeSet)");
        System.out.println("✓ Mathematical set abstraction");
        System.out.println("✓ Efficient membership testing");
    }
    
    /**
     * Compare different Set implementations
     */
    public static void compareSetImplementations() {
        System.out.println("\n=== SET IMPLEMENTATIONS COMPARISON ===\n");
        
        // Same data in different Set implementations
        String[] data = {"Charlie", "Alice", "Bob", "David", "Alice"}; // Note: duplicate Alice
        
        Set<String> hashSet = new HashSet<>();
        Set<String> linkedHashSet = new LinkedHashSet<>();
        Set<String> treeSet = new TreeSet<>();
        
        // Add same data to all sets
        for (String item : data) {
            hashSet.add(item);
            linkedHashSet.add(item);
            treeSet.add(item);
        }
        
        System.out.println("Original data: " + Arrays.toString(data));
        System.out.println("HashSet (no order guarantee): " + hashSet);
        System.out.println("LinkedHashSet (insertion order): " + linkedHashSet);
        System.out.println("TreeSet (sorted order): " + treeSet);
        
        System.out.println("\nFEATURE COMPARISON:");
        System.out.println("┌─────────────────┬─────────────┬─────────────────┬─────────────┐");
        System.out.println("│ Feature         │ HashSet     │ LinkedHashSet   │ TreeSet     │");
        System.out.println("├─────────────────┼─────────────┼─────────────────┼─────────────┤");
        System.out.println("│ Ordering        │ None        │ Insertion       │ Sorted      │");
        System.out.println("│ Add/Remove/Find │ O(1)        │ O(1)            │ O(log n)    │");
        System.out.println("│ Null values     │ One allowed │ One allowed     │ Not allowed │");
        System.out.println("│ Interface       │ Set         │ Set             │ NavigableSet│");
        System.out.println("│ Internal struct │ Hash table  │ Hash + LinkedList│ Red-Black Tree│");
        System.out.println("│ Memory overhead │ Low         │ Medium          │ High        │");
        System.out.println("└─────────────────┴─────────────┴─────────────────┴─────────────┘");
        
        // Demonstrate null handling
        System.out.println("\nNULL HANDLING:");
        hashSet.add(null);
        linkedHashSet.add(null);
        System.out.println("HashSet with null: " + hashSet);
        System.out.println("LinkedHashSet with null: " + linkedHashSet);
        
        try {
            treeSet.add(null);
        } catch (NullPointerException e) {
            System.out.println("TreeSet doesn't allow null: " + e.getClass().getSimpleName());
        }
    }
    
    /**
     * HashSet internal implementation details
     */
    public static void demonstrateHashSetInternals() {
        System.out.println("\n=== HASHSET INTERNALS ===\n");
        
        System.out.println("INTERNAL IMPLEMENTATION:");
        System.out.println("1. Backed by HashMap<E, Object>");
        System.out.println("2. Elements stored as keys, dummy Object as values");
        System.out.println("3. Uses element's hashCode() and equals() methods");
        System.out.println("4. Load factor: 0.75, Initial capacity: 16");
        System.out.println("5. Rehashing when load factor exceeded");
        
        // Demonstrate impact of hashCode() and equals()
        System.out.println("\nIMPACT OF HASHCODE() AND EQUALS():");
        
        Set<Person> personSet = new HashSet<>();
        Person p1 = new Person("John", 25);
        Person p2 = new Person("John", 25); // Same content, different object
        Person p3 = new Person("Jane", 30);
        
        personSet.add(p1);
        personSet.add(p2); // Should not be added if equals/hashCode properly implemented
        personSet.add(p3);
        
        System.out.println("Person set: " + personSet);
        System.out.println("Set size: " + personSet.size());
        System.out.println("Contains p2: " + personSet.contains(p2));
        
        // Bad hashCode example
        Set<BadHashPerson> badSet = new HashSet<>();
        BadHashPerson bp1 = new BadHashPerson("Alice", 28);
        BadHashPerson bp2 = new BadHashPerson("Alice", 28);
        
        badSet.add(bp1);
        badSet.add(bp2); // Will be added even though logically same
        
        System.out.println("\nBad hash implementation:");
        System.out.println("Bad person set: " + badSet);
        System.out.println("Bad set size: " + badSet.size() + " (should be 1!)");
        
        // Collision demonstration
        demonstrateHashCollisions();
    }
    
    /**
     * LinkedHashSet specific features
     */
    public static void demonstrateLinkedHashSet() {
        System.out.println("\n=== LINKEDHASHSET FEATURES ===\n");
        
        System.out.println("LINKEDHASHSET CHARACTERISTICS:");
        System.out.println("✓ Maintains insertion order");
        System.out.println("✓ Hash table + doubly-linked list");
        System.out.println("✓ Predictable iteration order");
        System.out.println("✓ Slightly slower than HashSet due to linked list overhead");
        
        LinkedHashSet<String> orderedSet = new LinkedHashSet<>();
        
        // Add elements in specific order
        orderedSet.add("Third");
        orderedSet.add("First");
        orderedSet.add("Second");
        orderedSet.add("Fourth");
        
        System.out.println("LinkedHashSet (insertion order): " + orderedSet);
        
        // Remove and re-add
        orderedSet.remove("First");
        orderedSet.add("First"); // Will be added at the end
        
        System.out.println("After remove and re-add: " + orderedSet);
        
        // Demonstrate with LRU-like behavior
        System.out.println("\nLRU-LIKE BEHAVIOR SIMULATION:");
        LRUSet<String> lruSet = new LRUSet<>(3);
        lruSet.add("A");
        lruSet.add("B");
        lruSet.add("C");
        System.out.println("LRU set after adding A, B, C: " + lruSet);
        
        lruSet.add("D"); // Should remove A (oldest)
        System.out.println("After adding D: " + lruSet);
        
        lruSet.access("B"); // Move B to end
        System.out.println("After accessing B: " + lruSet);
        
        lruSet.add("E"); // Should remove C (oldest after B moved)
        System.out.println("After adding E: " + lruSet);
    }
    
    /**
     * TreeSet operations and features
     */
    public static void demonstrateTreeSetOperations() {
        System.out.println("\n=== TREESET OPERATIONS ===\n");
        
        System.out.println("TREESET CHARACTERISTICS:");
        System.out.println("✓ Implements NavigableSet interface");
        System.out.println("✓ Red-Black tree implementation");
        System.out.println("✓ Elements must be Comparable or use Comparator");
        System.out.println("✓ O(log n) for basic operations");
        System.out.println("✓ Sorted order maintained automatically");
        
        TreeSet<Integer> numbers = new TreeSet<>();
        numbers.addAll(Arrays.asList(50, 30, 70, 20, 40, 60, 80));
        
        System.out.println("TreeSet (automatically sorted): " + numbers);
        
        // Basic TreeSet operations
        System.out.println("First (minimum): " + numbers.first());
        System.out.println("Last (maximum): " + numbers.last());
        
        // Range operations
        System.out.println("Lower than 50: " + numbers.lower(50)); // < 50
        System.out.println("Floor of 55: " + numbers.floor(55));   // <= 55
        System.out.println("Ceiling of 55: " + numbers.ceiling(55)); // >= 55
        System.out.println("Higher than 50: " + numbers.higher(50)); // > 50
        
        // Subset operations
        System.out.println("HeadSet (< 50): " + numbers.headSet(50));
        System.out.println("TailSet (>= 50): " + numbers.tailSet(50));
        System.out.println("SubSet [30, 70): " + numbers.subSet(30, 70));
        
        // Polling (removal)
        System.out.println("Poll first: " + numbers.pollFirst());
        System.out.println("Poll last: " + numbers.pollLast());
        System.out.println("After polling: " + numbers);
        
        // Custom comparator
        TreeSet<String> words = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        words.addAll(Arrays.asList("apple", "Banana", "cherry", "Date"));
        System.out.println("Case-insensitive TreeSet: " + words);
        
        // Reverse order
        TreeSet<Integer> reverseNumbers = new TreeSet<>(Collections.reverseOrder());
        reverseNumbers.addAll(Arrays.asList(1, 5, 3, 9, 2));
        System.out.println("Reverse order TreeSet: " + reverseNumbers);
    }
    
    /**
     * NavigableSet operations demonstration
     */
    public static void demonstrateNavigableSetOperations() {
        System.out.println("\n=== NAVIGABLESET OPERATIONS ===\n");
        
        NavigableSet<Integer> navSet = new TreeSet<>();
        navSet.addAll(Arrays.asList(10, 20, 30, 40, 50, 60, 70, 80, 90));
        
        System.out.println("NavigableSet: " + navSet);
        
        // Navigation methods
        System.out.println("\nNAVIGATION METHODS:");
        System.out.println("lower(50): " + navSet.lower(50));     // Greatest element < 50
        System.out.println("floor(55): " + navSet.floor(55));     // Greatest element <= 55
        System.out.println("ceiling(55): " + navSet.ceiling(55)); // Smallest element >= 55
        System.out.println("higher(50): " + navSet.higher(50));   // Smallest element > 50
        
        // Descending set
        NavigableSet<Integer> descendingSet = navSet.descendingSet();
        System.out.println("Descending set: " + descendingSet);
        
        // Descending iterator
        System.out.print("Descending iteration: ");
        Iterator<Integer> descIter = navSet.descendingIterator();
        while (descIter.hasNext()) {
            System.out.print(descIter.next() + " ");
        }
        System.out.println();
        
        // Subset views
        System.out.println("\nSUBSET VIEWS:");
        System.out.println("headSet(50, false): " + navSet.headSet(50, false)); // < 50
        System.out.println("headSet(50, true): " + navSet.headSet(50, true));   // <= 50
        System.out.println("tailSet(50, false): " + navSet.tailSet(50, false)); // > 50
        System.out.println("tailSet(50, true): " + navSet.tailSet(50, true));   // >= 50
        System.out.println("subSet(30, false, 70, true): " + 
                          navSet.subSet(30, false, 70, true)); // (30, 70]
        
        // Polling operations
        NavigableSet<Integer> copySet = new TreeSet<>(navSet);
        System.out.println("\nPOLLING OPERATIONS:");
        System.out.println("pollFirst(): " + copySet.pollFirst());
        System.out.println("pollLast(): " + copySet.pollLast());
        System.out.println("After polling: " + copySet);
    }
    
    /**
     * Set mathematical operations
     */
    public static void demonstrateSetMathOperations() {
        System.out.println("\n=== SET MATHEMATICAL OPERATIONS ===\n");
        
        Set<Integer> set1 = new HashSet<>(Arrays.asList(1, 2, 3, 4, 5));
        Set<Integer> set2 = new HashSet<>(Arrays.asList(4, 5, 6, 7, 8));
        
        System.out.println("Set A: " + set1);
        System.out.println("Set B: " + set2);
        
        // Union (A ∪ B)
        Set<Integer> union = new HashSet<>(set1);
        union.addAll(set2);
        System.out.println("Union (A ∪ B): " + union);
        
        // Intersection (A ∩ B)
        Set<Integer> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);
        System.out.println("Intersection (A ∩ B): " + intersection);
        
        // Difference (A - B)
        Set<Integer> difference = new HashSet<>(set1);
        difference.removeAll(set2);
        System.out.println("Difference (A - B): " + difference);
        
        // Symmetric difference ((A - B) ∪ (B - A))
        Set<Integer> symmetricDiff = new HashSet<>(set1);
        Set<Integer> temp = new HashSet<>(set2);
        temp.removeAll(set1); // B - A
        symmetricDiff.removeAll(set2); // A - B
        symmetricDiff.addAll(temp); // (A - B) ∪ (B - A)
        System.out.println("Symmetric Difference: " + symmetricDiff);
        
        // Subset checking
        Set<Integer> subset = new HashSet<>(Arrays.asList(2, 3));
        System.out.println("Is {2, 3} subset of A: " + set1.containsAll(subset));
        
        // Disjoint sets
        Set<Integer> disjointSet = new HashSet<>(Arrays.asList(10, 11, 12));
        System.out.println("Are A and {10, 11, 12} disjoint: " + 
                          Collections.disjoint(set1, disjointSet));
    }
    
    /**
     * Performance analysis of Set implementations
     */
    public static void analyzePerformance() {
        System.out.println("\n=== PERFORMANCE ANALYSIS ===\n");
        
        final int SIZE = 100000;
        
        // Test data
        Integer[] testData = new Integer[SIZE];
        Random random = new Random();
        for (int i = 0; i < SIZE; i++) {
            testData[i] = random.nextInt(SIZE * 2); // Some duplicates
        }
        
        // Test HashSet
        long startTime = System.nanoTime();
        Set<Integer> hashSet = new HashSet<>();
        for (Integer num : testData) {
            hashSet.add(num);
        }
        long hashSetTime = System.nanoTime() - startTime;
        
        // Test LinkedHashSet
        startTime = System.nanoTime();
        Set<Integer> linkedHashSet = new LinkedHashSet<>();
        for (Integer num : testData) {
            linkedHashSet.add(num);
        }
        long linkedHashSetTime = System.nanoTime() - startTime;
        
        // Test TreeSet
        startTime = System.nanoTime();
        Set<Integer> treeSet = new TreeSet<>();
        for (Integer num : testData) {
            treeSet.add(num);
        }
        long treeSetTime = System.nanoTime() - startTime;
        
        System.out.println("Adding " + SIZE + " elements:");
        System.out.println("HashSet: " + hashSetTime / 1_000_000 + " ms");
        System.out.println("LinkedHashSet: " + linkedHashSetTime / 1_000_000 + " ms");
        System.out.println("TreeSet: " + treeSetTime / 1_000_000 + " ms");
        
        // Test lookup performance
        Integer[] lookupData = new Integer[10000];
        for (int i = 0; i < 10000; i++) {
            lookupData[i] = random.nextInt(SIZE * 2);
        }
        
        // HashSet lookup
        startTime = System.nanoTime();
        for (Integer num : lookupData) {
            hashSet.contains(num);
        }
        long hashSetLookup = System.nanoTime() - startTime;
        
        // TreeSet lookup
        startTime = System.nanoTime();
        for (Integer num : lookupData) {
            treeSet.contains(num);
        }
        long treeSetLookup = System.nanoTime() - startTime;
        
        System.out.println("\n10,000 lookup operations:");
        System.out.println("HashSet: " + hashSetLookup / 1_000_000 + " ms");
        System.out.println("TreeSet: " + treeSetLookup / 1_000_000 + " ms");
        
        System.out.println("\nFinal set sizes:");
        System.out.println("HashSet: " + hashSet.size());
        System.out.println("LinkedHashSet: " + linkedHashSet.size());
        System.out.println("TreeSet: " + treeSet.size());
    }
    
    /**
     * Hash collision demonstration
     */
    private static void demonstrateHashCollisions() {
        System.out.println("\nHASH COLLISION EXAMPLE:");
        
        // Objects that intentionally have same hash code
        Set<CollidingObject> collisionSet = new HashSet<>();
        collisionSet.add(new CollidingObject("A", 1));
        collisionSet.add(new CollidingObject("B", 2));
        collisionSet.add(new CollidingObject("C", 3));
        
        System.out.println("Objects with same hash code in set: " + collisionSet);
        System.out.println("All have hash code: " + 
                          collisionSet.iterator().next().hashCode());
    }
    
    /**
     * Best practices and recommendations
     */
    public static void showBestPractices() {
        System.out.println("\n=== BEST PRACTICES ===\n");
        
        System.out.println("CHOOSING THE RIGHT SET:");
        System.out.println("✓ HashSet: General purpose, best performance");
        System.out.println("✓ LinkedHashSet: When you need predictable iteration order");
        System.out.println("✓ TreeSet: When you need sorted elements or range operations");
        
        System.out.println("\nHASHCODE AND EQUALS BEST PRACTICES:");
        System.out.println("✓ Always override both equals() and hashCode() together");
        System.out.println("✓ Use Objects.hash() for hashCode implementation");
        System.out.println("✓ Include all fields used in equals() in hashCode()");
        System.out.println("✓ Make objects used as Set elements immutable");
        
        System.out.println("\nPERFORMANCE CONSIDERATIONS:");
        System.out.println("✓ HashSet: O(1) average, O(n) worst case");
        System.out.println("✓ LinkedHashSet: Slightly slower than HashSet");
        System.out.println("✓ TreeSet: O(log n) guaranteed");
        System.out.println("✓ Consider initial capacity for HashSet/LinkedHashSet");
        
        System.out.println("\nCOMMON PITFALLS:");
        System.out.println("✗ Modifying objects after adding to HashSet/LinkedHashSet");
        System.out.println("✗ Using mutable objects as Set elements");
        System.out.println("✗ Poor hashCode() implementation causing excessive collisions");
        System.out.println("✗ Forgetting that TreeSet doesn't allow null elements");
        
        // Example of immutable Set element
        System.out.println("\nIMMUTABLE SET ELEMENT EXAMPLE:");
        Set<ImmutablePerson> immutableSet = new HashSet<>();
        immutableSet.add(new ImmutablePerson("John", 25));
        immutableSet.add(new ImmutablePerson("Jane", 30));
        System.out.println("Immutable person set: " + immutableSet);
    }
}

// Supporting classes

/**
 * Proper implementation with equals and hashCode
 */
class Person {
    private final String name;
    private final int age;
    
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Person person = (Person) obj;
        return age == person.age && Objects.equals(name, person.name);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }
    
    @Override
    public String toString() {
        return name + "(" + age + ")";
    }
}

/**
 * Bad implementation - only overrides equals
 */
class BadHashPerson {
    private final String name;
    private final int age;
    
    public BadHashPerson(String name, int age) {
        this.name = name;
        this.age = age;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        BadHashPerson person = (BadHashPerson) obj;
        return age == person.age && Objects.equals(name, person.name);
    }
    
    // No hashCode override - uses Object.hashCode()
    
    @Override
    public String toString() {
        return name + "(" + age + ")";
    }
}

/**
 * Objects that intentionally collide on hash
 */
class CollidingObject {
    private final String id;
    private final int value;
    
    public CollidingObject(String id, int value) {
        this.id = id;
        this.value = value;
    }
    
    @Override
    public int hashCode() {
        return 42; // All objects have same hash code
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CollidingObject that = (CollidingObject) obj;
        return value == that.value && Objects.equals(id, that.id);
    }
    
    @Override
    public String toString() {
        return id + ":" + value;
    }
}

/**
 * Immutable person class
 */
final class ImmutablePerson {
    private final String name;
    private final int age;
    
    public ImmutablePerson(String name, int age) {
        this.name = name;
        this.age = age;
    }
    
    public String getName() { return name; }
    public int getAge() { return age; }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ImmutablePerson person = (ImmutablePerson) obj;
        return age == person.age && Objects.equals(name, person.name);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }
    
    @Override
    public String toString() {
        return name + "(" + age + ")";
    }
}

/**
 * LRU-like Set implementation
 */
class LRUSet<E> extends LinkedHashSet<E> {
    private final int maxSize;
    
    public LRUSet(int maxSize) {
        super(16, 0.75f, false); // Access order = false (insertion order)
        this.maxSize = maxSize;
    }
    
    @Override
    protected boolean removeEldestEntry(Map.Entry<E, Object> eldest) {
        return size() > maxSize;
    }
    
    public void access(E element) {
        // Remove and re-add to move to end
        if (remove(element)) {
            add(element);
        }
    }
}