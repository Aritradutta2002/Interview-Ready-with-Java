# Sorting Theory Guide (CP Standard)

Core algorithms and properties:

- Comparison-based lower bound: any comparison sort requires Omega(n log n) time in worst case.
- Stability: Bubble (stable), Insertion (stable), Merge (stable), TimSort (stable), Counting (stable with cumulative), Radix (stable), QuickSort (unstable), HeapSort (unstable).
- In-place: QuickSort, HeapSort (O(1) extra); MergeSort (O(n) extra typically); Insertion/Bubble (O(1)).
- When to use:
  - HeapSort: need worst-case O(n log n) and O(1) space
  - QuickSort: fastest average + good cache; randomized pivot
  - MergeSort: stable, external sorting, linked list sort
  - Counting/Radix/Bucket: when key range/base is small (break O(n log n))

Patterns and techniques:

1) Partition schemes (Lomuto/Hoare) and 3-way partition (Dutch national flag) for duplicates.
2) Quickselect for order statistics (median, kth element) – average O(n).
3) Bucketing with Pigeonhole principle (Maximum Gap) – gaps lie between buckets.
4) TimSort (Java/Python built-in): run detection + galloping merge; leverage partially ordered data.
5) External sorting: chunk sort + k-way merge using heap for data larger than memory.

Cheat-sheet (time/space):
- Insertion: O(n^2)/O(1) – small or nearly-sorted arrays.
- Merge: O(n log n)/O(n) – stable; good for linked lists/external.
- Quick: O(n log n) avg, O(n^2) worst/O(log n) stack – great cache.
- Heap: O(n log n)/O(1) – worst-case guarantee.
- Counting: O(n+k)/O(k) – integers within small range.
- Radix (LSD base 10/256): O((n+d)*b) – depends on digits/base; needs stable counting sort.
- Bucket: O(n) average – uniform distribution assumption.

Tricks:
- Use comparator for custom order (e.g., Largest Number problem with ab vs ba).
- Coordinate compression + counting for frequencies and order statistics.
- Partial sorting: keep min/max heap of size k (top-k problems).

References to implementations in this folder:
- QuickSort.java, MergeSort.java, HeapSort.java, CountingSort.java, RadixSort.java
- Problems: LargestNumber.java, KthLargestElement.java, WiggleSortII.java, MaximumGap.java, TopKFrequentElements.java
