# Collections Framework - Maps

## Map Hierarchy

```
                    Map
            ┌────────┼────────┐
            ↓        ↓        ↓
        HashMap  LinkedHashMap TreeMap
            ↓
    ConcurrentHashMap
```

---

## HashMap Deep Dive (MOST IMPORTANT)

### Internal Structure (Java 8+)
```
┌─────────────────────────────────────────────────────┐
│                    HashMap                          │
├─────────────────────────────────────────────────────┤
│ Node<K,V>[] table;  // Array of buckets            │
│ int size;           // Number of entries            │
│ int threshold;      // Capacity * loadFactor        │
│ final float loadFactor;  // Default 0.75            │
└─────────────────────────────────────────────────────┘

Bucket structure:
┌──────┐    ┌──────┐    ┌──────┐
│ Node │ →  │ Node │ →  │ Node │ → null  (Linked List)
└──────┘    └──────┘    └──────┘
    ↓ (when > 8 nodes)
┌──────┐
│TreeNode│ (Red-Black Tree for O(log n))
└──────┘
```

### How put() Works
```java
public V put(K key, V value) {
    // 1. Calculate hash
    int hash = hash(key);  // hash = key.hashCode() ^ (key.hashCode() >>> 16)
    
    // 2. Calculate bucket index
    int index = (n - 1) & hash;  // n = table length
    
    // 3. If bucket empty, create node
    if (table[index] == null) {
        table[index] = new Node<>(hash, key, value, null);
    }
    // 4. If bucket not empty, traverse chain
    else {
        Node<K,V> node = table[index];
        while (node != null) {
            // Check if key exists (hash + equals)
            if (node.hash == hash && 
                (node.key == key || key.equals(node.key))) {
                return node.setValue(value);  // Update existing
            }
            node = node.next;
        }
        // Add new node at head
        table[index] = new Node<>(hash, key, value, table[index]);
    }
    
    // 5. Check if resize needed
    if (++size > threshold) resize();
}
```

### Hash Function
```java
static final int hash(Object key) {
    int h;
    // XOR with upper 16 bits to spread entropy
    return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}
```

### Why XOR with >>> 16?
- Spreads high bits to lower bits
- Reduces collisions when table is small
- Better distribution

### Rehashing/Resize
```java
void resize() {
    // 1. Double the capacity
    int newCap = oldCap << 1;  // 2x
    
    // 2. Create new array
    Node<K,V>[] newTab = new Node[newCap];
    
    // 3. Rehash all entries
    for (Node<K,V> node : table) {
        while (node != null) {
            // Recalculate index: (newCap - 1) & hash
            // Either same index or index + oldCap
        }
    }
}
```

### Treeification (Java 8+)
```java
// When chain length > 8 and table size >= 64
// Convert linked list to Red-Black Tree
if (binCount >= TREEIFY_THRESHOLD - 1) {
    treeifyBin(tab, hash);
}
// Tree back to linked list when nodes < 6
```

### Load Factor
- Default: 0.75
- Threshold = capacity × loadFactor
- When size > threshold → resize
- Trade-off: Memory vs Collision

---

## HashMap Key Points

| Aspect | Detail |
|--------|--------|
| Initial Capacity | 16 |
| Load Factor | 0.75 |
| Resize | 2x capacity |
| Null Keys | 1 null key allowed |
| Null Values | Multiple null values allowed |
| Ordering | No guarantee |
| Thread Safety | Not thread-safe |

---

## LinkedHashMap

### Internal Structure
```java
// Extends HashMap