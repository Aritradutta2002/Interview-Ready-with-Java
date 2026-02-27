# SEARCHING - Binary Search Mastery

## 📂 Structure
```
SEARCHING/
├── theory/          # Binary search basics
└── problems/        # Interview problems
```

## 🎯 Critical FAANG Problems

###  **⭐ Must-Do Binary Search**

| Problem | LeetCode | Difficulty | Companies | Pattern |
|---------|----------|------------|-----------|---------|
| **Search in Rotated Sorted Array** | #33 | Medium | FB, AMZN, MSFT | Modified BS |
| **Find Peak Element** | #162 | Medium | GOOG, FB, AMZN | BS on Unsorted |
| **Search 2D Matrix** | #74 | Medium | AMZN, MSFT, GOOG | 2D → 1D BS |
| **Find Minimum in Rotated Array** | #153 | Medium | FB, AMZN | Modified BS |
| **First and Last Position** | #34 | Medium | FB, GOOG, AMZN | BS Twice |

### 🔥 **BS on Answer Space** (Critical Pattern!)

| Problem | LeetCode | Difficulty | Type |
|---------|----------|------------|------|
| **Koko Eating Bananas** | #875 | Medium | Speed BS |
| **Capacity To Ship Packages** | #1011 | Medium | Capacity BS |
| **Split Array Largest Sum** | #410 | Hard | Min-Max BS |
| **Minimum Time to Complete Trips** | #2187 | Medium | Time BS |
| **Magnetic Force Between Balls** | #1552 | Medium | Distance BS |

### 💪 **CP Level**

| Problem | LeetCode | Difficulty | Technique |
|---------|----------|------------|-----------|
| Median of Two Sorted Arrays | #4 | Hard | Partition BS |
| Kth Smallest in Sorted Matrix | #378 | Medium | Multi-array BS |
| Find K Closest Elements | #658 | Medium | BS + Two Pointers |
| Count of Smaller Numbers | #315 | Hard | BS + BIT/Merge Sort |

---

## 📚 Binary Search Patterns

### **Pattern 1: Classic Binary Search**
```java
int binarySearch(int[] arr, int target) {
    int left = 0, right = arr.length - 1;
    
    while (left <= right) {
        int mid = left + (right - left) / 2;
        
        if (arr[mid] == target) return mid;
        else if (arr[mid] < target) left = mid + 1;
        else right = mid - 1;
    }
    
    return -1; // Not found
}
```

**Problems:** #704, #374, #278

---

### **Pattern 2: Find First/Last Position**
```java
// Find FIRST position where condition is true
int findFirst(int[] arr, int target) {
    int left = 0, right = arr.length - 1;
    int result = -1;
    
    while (left <= right) {
        int mid = left + (right - left) / 2;
        
        if (arr[mid] == target) {
            result = mid;
            right = mid - 1; // Keep searching left
        } else if (arr[mid] < target) {
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }
    
    return result;
}

// Find LAST: change to left = mid + 1
```

**Problems:** #34 ⭐, #35, #69, #744

---

### **Pattern 3: Rotated Array**
```java
int searchRotated(int[] nums, int target) {
    int left = 0, right = nums.length - 1;
    
    while (left <= right) {
        int mid = left + (right - left) / 2;
        
        if (nums[mid] == target) return mid;
        
        // Which half is sorted?
        if (nums[left] <= nums[mid]) {
            // Left half sorted
            if (target >= nums[left] && target < nums[mid]) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        } else {
            // Right half sorted
            if (target > nums[mid] && target <= nums[right]) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
    }
    
    return -1;
}
```

**Problems:** #33 ⭐⭐⭐, #81, #153 ⭐⭐⭐

---

### **Pattern 4: BS on Answer Space** ⚡ CRITICAL!

**When to use:** Min/Max optimization problems
- **"Minimize the maximum"**
- **"Maximize the minimum"**
- Can verify answer in O(n)

**Template:**
```java
int binarySearchOnAnswer(int[] arr, int target) {
    int left = MIN_POSSIBLE, right = MAX_POSSIBLE;
    int result = -1;
    
    while (left <= right) {
        int mid = left + (right - left) / 2;
        
        if (isPossible(arr, mid, target)) {
            result = mid;
            // Minimize: right = mid - 1
            // Maximize: left = mid + 1
        } else {
            // Adjust search space
        }
    }
    
    return result;
}

boolean isPossible(int[] arr, int capacity, int target) {
    // Check if this capacity works
    return true/false;
}
```

**Classic Problems:**
1. **Koko Eating Bananas** (#875) ⭐⭐⭐
   - Find min speed to eat all bananas in H hours
   - BS on speed [1, max(piles)]

2. **Capacity To Ship Packages** (#1011) ⭐⭐⭐
   - Find min capacity to ship in D days
   - BS on capacity [max(weights), sum(weights)]

3. **Split Array Largest Sum** (#410) ⭐⭐⭐
   - Split array into m subarrays, minimize largest sum
   - BS on sum [max(nums), sum(nums)]

4. **Minimum Time to Complete Trips** (#2187)
   - BS on time

---

### **Pattern 5: 2D Matrix Search**
```java
boolean searchMatrix(int[][] matrix, int target) {
    int m = matrix.length, n = matrix[0].length;
    int left = 0, right = m * n - 1;
    
    while (left <= right) {
        int mid = left + (right - left) / 2;
        int midVal = matrix[mid / n][mid % n]; // Key: Convert 1D to 2D
        
        if (midVal == target) return true;
        else if (midVal < target) left = mid + 1;
        else right = mid - 1;
    }
    
    return false;
}
```

**Problems:** #74 ⭐⭐, #240 ⭐

---

### **Pattern 6: Peak Finding**
```java
int findPeakElement(int[] nums) {
    int left = 0, right = nums.length - 1;
    
    while (left < right) {
        int mid = left + (right - left) / 2;
        
        if (nums[mid] > nums[mid + 1]) {
            // Peak is on left (including mid)
            right = mid;
        } else {
            // Peak is on right
            left = mid + 1;
        }
    }
    
    return left;
}
```

**Problems:** #162 ⭐⭐, #852, #1095

---

## 🎓 Study Plan

### Week 1: Master Basics
- [ ] Binary Search (#704)
- [ ] First Bad Version (#278)
- [ ] Search Insert Position (#35)
- [ ] Find First and Last (#34) ⭐

### Week 2: Rotated Arrays
- [ ] Search in Rotated Array (#33) ⭐⭐⭐
- [ ] Find Minimum in Rotated (#153) ⭐⭐
- [ ] Search in Rotated II (#81)

### Week 3: BS on Answer (CRITICAL!)
- [ ] Koko Eating Bananas (#875) ⭐⭐⭐
- [ ] Capacity To Ship Packages (#1011) ⭐⭐⭐
- [ ] Split Array Largest Sum (#410) ⭐⭐⭐

### Week 4: Advanced
- [ ] Search 2D Matrix (#74) ⭐
- [ ] Find Peak Element (#162) ⭐
- [ ] Median of Two Sorted Arrays (#4) ⭐⭐⭐

---

## 💡 Interview Tips

### 1. **Binary Search Template Choice**

**Left <= Right (Most Common):**
```java
while (left <= right) {
    // For exact match
    if (found) return mid;
    // Update: left = mid + 1 OR right = mid - 1
}
```

**Left < Right (For range):**
```java
while (left < right) {
    // For finding boundary
    // Update: left = mid + 1 OR right = mid
}
```

### 2. **Common Mistakes**
❌ Integer overflow: `mid = (left + right) / 2`  
✅ Use: `mid = left + (right - left) / 2`

❌ Infinite loop: `left = mid` with `while (left < right)`  
✅ Use: `left = mid + 1` or change condition

❌ Off-by-one errors in bounds  
❌ Not handling duplicates in rotated array

### 3. **When to Use BS**
✅ **Use BS when:**
- Array is sorted (or rotated sorted)
- Answer space is monotonic
- Can verify answer in O(n) or better
- Need O(log n) solution

❌ **Don't use BS when:**
- Array is completely unsorted
- Need to find all occurrences
- Linear scan is simpler

### 4. **BS on Answer Checklist**
1. ✅ Can I binary search the answer?
2. ✅ What are min/max possible values?
3. ✅ Can I write `isPossible(mid)` function?
4. ✅ Is it minimize max or maximize min?
5. ✅ Which direction to move?

---

## 🔥 Company-Specific

### **Facebook**
1. Search in Rotated Array (#33)
2. Find Peak Element (#162)
3. First and Last Position (#34)

### **Amazon**
1. Koko Eating Bananas (#875)
2. Capacity To Ship Packages (#1011)
3. Search 2D Matrix (#74)

### **Google**
1. Median of Two Sorted Arrays (#4)
2. Split Array Largest Sum (#410)
3. Find K Closest Elements (#658)

### **Microsoft**
1. Search in Rotated Array (#33)
2. Find Minimum in Rotated (#153)
3. Kth Smallest in Matrix (#378)

---

## 📝 Quick Templates

### Template 1: Find Exact
```java
while (left <= right) {
    int mid = left + (right - left) / 2;
    if (arr[mid] == target) return mid;
    if (arr[mid] < target) left = mid + 1;
    else right = mid - 1;
}
```

### Template 2: Find First True
```java
while (left < right) {
    int mid = left + (right - left) / 2;
    if (condition(mid)) right = mid;
    else left = mid + 1;
}
return left;
```

### Template 3: BS on Answer
```java
int left = MIN, right = MAX;
while (left < right) {
    int mid = left + (right - left) / 2;
    if (isPossible(mid)) right = mid; // Minimize
    else left = mid + 1;
}
return left;
```

---

## 🚀 Practice Strategy

1. **Master Template 1** - Do 5-10 easy problems
2. **Learn Rotated Array** - Critical pattern!
3. **Master BS on Answer** - Game changer!
4. **Practice 2D problems** - Good bonus
5. **Time yourself** - 20-25 mins per problem

**Must-Do: 15 problems**  
**Time: 2-3 weeks**

---

**The key insight: Binary Search isn't just for sorted arrays - it's for ANY monotonic space!** 🎯
