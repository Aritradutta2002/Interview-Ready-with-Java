# Segment Tree - Complete Study Guide

## 📚 Folder Structure (Organized)

```
SEGMENT_TREE/
├── README.md                           # Complete overview
├── STUDY_GUIDE.md                      # This file
│
├── basics/                             # Learn these first
│   ├── SegmentTreeBasic.java          # Start here! Basic implementation
│   ├── SegmentTreeTemplate.java       # Clean template for interviews
│   ├── LazyPropagation.java           # For range updates
│   └── BinaryIndexedTree.java         # Simpler alternative (Fenwick Tree)
│
├── problems/                           # Practice problems
│   ├── RangeSumQueryMutable.java      # LC 307 - First problem to solve
│   ├── RangeMinimumQuery.java         # Different merge operation
│   ├── RangeSumQuery2D.java           # LC 308 - 2D extension
│   ├── CountSmallerAfterSelf.java     # LC 315 - Coordinate compression
│   └── ReversePairs.java              # LC 493 - Similar to above
│
└── advanced/                           # Advanced applications
    ├── RangeModule.java               # LC 715 - Interval management
    └── MyCalendar.java                # LC 729, 731, 732 - Booking system
```

---

## 🎯 Learning Path (4 Weeks)

### Week 1: Fundamentals (Must Master)

#### Day 1-2: Basic Segment Tree
1. **Read**: `basics/SegmentTreeBasic.java`
   - Understand tree structure
   - Learn build, query, update operations
   - Practice: Implement from scratch

2. **Key Concepts**:
   ```
   - Tree stored in array: tree[4*n]
   - Node i has children: 2*i+1 and 2*i+2
   - Build: O(n), Query: O(log n), Update: O(log n)
   ```

#### Day 3-4: Binary Indexed Tree
1. **Read**: `basics/BinaryIndexedTree.java`
   - Simpler alternative to Segment Tree
   - Only for prefix sum queries
   - Less space, easier to code

2. **When to use BIT vs Segment Tree**:
   - BIT: Only sum queries, simpler code
   - Segment Tree: Any query type, more flexible

#### Day 5-7: First Problem
1. **Solve**: `problems/RangeSumQueryMutable.java` (LC 307)
   - Implement using Segment Tree
   - Implement using BIT
   - Compare both approaches

---

### Week 2: Intermediate Techniques

#### Day 1-3: Lazy Propagation
1. **Read**: `basics/LazyPropagation.java`
   - For range updates (not just point updates)
   - Understand lazy array concept
   - Practice push-down operation

2. **Key Pattern**:
   ```java
   void push(int node) {
       if (lazy[node] != 0) {
           tree[node] += lazy[node] * (end - start + 1);
           if (start != end) {
               lazy[2*node+1] += lazy[node];
               lazy[2*node+2] += lazy[node];
           }
           lazy[node] = 0;
       }
   }
   ```

#### Day 4-5: Different Merge Operations
1. **Solve**: `problems/RangeMinimumQuery.java`
   - Change merge from sum to min
   - Understand how to adapt template

#### Day 6-7: 2D Extension
1. **Solve**: `problems/RangeSumQuery2D.java` (LC 308)
   - 2D Binary Indexed Tree
   - More complex but same principles

---

### Week 3: Advanced Patterns

#### Day 1-4: Coordinate Compression
1. **Solve**: `problems/CountSmallerAfterSelf.java` (LC 315)
   - **Very Important for FAANG!**
   - Learn coordinate compression technique
   - Combine with Segment Tree/BIT

2. **Pattern**:
   ```java
   // Step 1: Compress coordinates
   Arrays.sort(sorted);
   Map<Integer, Integer> map = new HashMap<>();
   for (int i = 0; i < sorted.length; i++) {
       map.put(sorted[i], i);
   }
   
   // Step 2: Use compressed values in tree
   ```

#### Day 5-7: Similar Problem
1. **Solve**: `problems/ReversePairs.java` (LC 493)
   - Similar to CountSmallerAfterSelf
   - Practice the pattern again

---

### Week 4: Real-World Applications

#### Day 1-3: Interval Management
1. **Solve**: `advanced/RangeModule.java` (LC 715)
   - Track ranges dynamically
   - Add, remove, query ranges
   - Can use TreeMap or Segment Tree

#### Day 4-7: Booking System
1. **Solve**: `advanced/MyCalendar.java` (LC 729, 731, 732)
   - Three variations of calendar booking
   - Handle overlapping intervals
   - Count k-bookings

---

## 🔑 Key Patterns to Master

### Pattern 1: Basic Range Query
```java
class SegmentTree {
    int[] tree;
    
    void build(int[] arr, int node, int start, int end) {
        if (start == end) {
            tree[node] = arr[start];
            return;
        }
        int mid = (start + end) / 2;
        build(arr, 2*node+1, start, mid);
        build(arr, 2*node+2, mid+1, end);
        tree[node] = tree[2*node+1] + tree[2*node+2]; // Merge
    }
}
```

### Pattern 2: Coordinate Compression
```java
// When values are large but count is small
int[] sorted = nums.clone();
Arrays.sort(sorted);
Map<Integer, Integer> map = new HashMap<>();
int rank = 0;
for (int num : sorted) {
    if (!map.containsKey(num)) {
        map.put(num, rank++);
    }
}
```

### Pattern 3: Lazy Propagation
```java
// For range updates
void updateRange(int node, int start, int end, int l, int r, int val) {
    push(node, start, end); // Push down lazy values
    
    if (l <= start && end <= r) {
        lazy[node] += val;
        push(node, start, end);
        return;
    }
    // ... recurse to children
}
```

---

## 💡 Interview Tips

### When to Use Segment Tree
✅ **Use when**:
- Need range queries (sum, min, max, etc.)
- Array is mutable (updates needed)
- Both queries and updates are frequent

❌ **Don't use when**:
- Array is static → Use prefix sum or sparse table
- Only need prefix sum → Use BIT (simpler)
- Updates are rare → Recalculate on demand

### Common Interview Questions
1. **"Why not just use a loop for range query?"**
   - Answer: Loop is O(n), Segment Tree is O(log n)
   - For many queries, Segment Tree is much faster

2. **"What's the space complexity?"**
   - Answer: O(4n) = O(n)
   - Explain: Need 4n to handle all possible nodes

3. **"Can you do range updates?"**
   - Answer: Yes, with lazy propagation
   - Explain: Delay updates until needed

### Coding Interview Strategy
1. **Clarify requirements**:
   - Type of query? (sum, min, max)
   - Update frequency?
   - Array size?

2. **Choose approach**:
   - Small array → Simple loop
   - Only sum queries → BIT
   - Complex queries → Segment Tree

3. **Start with template**:
   - Use `SegmentTreeTemplate.java`
   - Modify merge operation as needed

---

## 📊 Problem Difficulty & Priority

### Must Solve (⭐⭐⭐)
1. **RangeSumQueryMutable** (LC 307) - Foundation
2. **CountSmallerAfterSelf** (LC 315) - Very common in FAANG
3. **RangeModule** (LC 715) - Real-world application

### Should Solve (⭐⭐)
4. **ReversePairs** (LC 493) - Similar to #2
5. **RangeSumQuery2D** (LC 308) - 2D extension
6. **MyCalendar** (LC 729, 731, 732) - Practical problem

### Good to Know (⭐)
7. **RangeMinimumQuery** - Different merge operation

---

## 🎓 FAANG Interview Frequency

| Company | Frequency | Common Problems |
|---------|-----------|-----------------|
| **Google** | Very High | Count Smaller, Range Module, Calendar |
| **Amazon** | High | Range Sum Query, Count Smaller |
| **Facebook** | Medium | Range queries, BIT problems |
| **Microsoft** | Medium | Basic range queries |
| **Apple** | Low-Medium | Occasional range problems |

---

## 🚀 Quick Reference

### Time Complexities
| Operation | Segment Tree | BIT | Prefix Sum |
|-----------|-------------|-----|------------|
| Build | O(n) | O(n) | O(n) |
| Query | O(log n) | O(log n) | O(1) |
| Update | O(log n) | O(log n) | O(n) |

### Space Complexity
- Segment Tree: O(4n) = O(n)
- BIT: O(n)
- Prefix Sum: O(n)

### When to Use What
- **Static array + sum** → Prefix sum
- **Dynamic + sum only** → BIT
- **Dynamic + any query** → Segment Tree
- **Static + min/max** → Sparse Table

---

## ✅ Checklist

### Week 1
- [ ] Understand Segment Tree structure
- [ ] Implement basic build, query, update
- [ ] Learn Binary Indexed Tree
- [ ] Solve RangeSumQueryMutable

### Week 2
- [ ] Master lazy propagation
- [ ] Solve RangeMinimumQuery
- [ ] Solve RangeSumQuery2D

### Week 3
- [ ] Learn coordinate compression
- [ ] Solve CountSmallerAfterSelf
- [ ] Solve ReversePairs

### Week 4
- [ ] Solve RangeModule
- [ ] Solve MyCalendar series
- [ ] Review all problems

---

## 🎯 Final Tips

1. **Start simple**: Master basic Segment Tree before lazy propagation
2. **Use BIT when possible**: Simpler code, fewer bugs
3. **Practice templates**: Have a clean template ready for interviews
4. **Understand trade-offs**: Know when to use what
5. **Coordinate compression**: Very common in FAANG interviews

**Good luck with your preparation! 🚀**
