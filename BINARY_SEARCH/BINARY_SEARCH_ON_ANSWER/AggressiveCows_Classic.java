package BINARY_SEARCH.BINARY_SEARCH_ON_ANSWER;

/*
 * AGGRESSIVE COWS (Classic Problem)
 * Difficulty: Medium-Hard
 * Pattern: Binary Search on Answer (Distance Maximization)
 * Source: SPOJ, CodeChef, various platforms
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * PROBLEM STATEMENT:
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Farmer John has built a new long barn with N stalls. The stalls are located 
 * along a straight line at positions x1, x2, ..., xN.
 * 
 * His C cows don't like this barn layout and become aggressive towards each other 
 * once put into a stall. To prevent the cows from hurting each other, John wants 
 * to assign the cows to the stalls, such that the MINIMUM DISTANCE between any 
 * two of them is as LARGE as possible.
 * 
 * What is the largest minimum distance?
 * 
 * EXAMPLE 1:
 * Input: 
 * stalls = [1, 2, 4, 8, 9]
 * cows = 3
 * 
 * Output: 3
 * Explanation: Place cows at positions 1, 4, and 8
 * - Distance between cow 1 and cow 2: |4-1| = 3
 * - Distance between cow 2 and cow 3: |8-4| = 4
 * - Minimum distance = 3
 * 
 * If we try distance 4:
 * - Place at 1, then next must be at >=5, so place at 8
 * - Can only place 2 cows, not enough!
 * 
 * EXAMPLE 2:
 * Input:
 * stalls = [1, 2, 8, 4, 9]
 * cows = 3
 * 
 * Output: 3
 * After sorting: [1, 2, 4, 8, 9]
 * Place at 1, 4, 8 → min distance = 3
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * UNDERSTANDING THE PROBLEM:
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * GOAL: Maximize the minimum distance between cows
 * 
 * VISUAL:
 * Stalls: 1  2     4        8  9
 *         ●  ●     ●        ●  ●
 * 
 * Option 1: Place at 1, 2, 4
 *           🐄  🐄     🐄        
 * Min distance = 1 (between 1 and 2)
 * 
 * Option 2: Place at 1, 4, 8
 *           🐄        🐄        🐄
 * Min distance = 3 (between 1 and 4)
 * 
 * Option 2 is better! Can we do better than 3? NO!
 * 
 * WHY BINARY SEARCH?
 * ------------------
 * We're looking for: "Maximum possible value of minimum distance"
 * 
 * Key Observation (MONOTONICITY):
 * - If we can place C cows with min distance D
 * - Then we can definitely place C cows with min distance D-1
 * - But maybe NOT with min distance D+1
 * 
 * Visual:
 * Distance:  0  1  2  3  4  5  6  7  8
 * Can place: ✓  ✓  ✓  ✓  ✗  ✗  ✗  ✗  ✗
 *                      ↑
 *              Maximum valid distance (our answer!)
 * 
 * ANSWER SPACE:
 * -------------
 * Minimum distance: 1 (cows can be adjacent)
 * Maximum distance: max(stalls) - min(stalls) (all cows at extremes)
 * 
 * We binary search on: [1, max - min]
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * ALGORITHM:
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * STEP 1: Sort the stalls (important!)
 * 
 * STEP 2: Define search space [1, max-min]
 * 
 * STEP 3: Binary search on distance:
 *   - Try mid distance
 *   - Can we place C cows with min distance = mid?
 *   - If YES: Try larger distance (maximize)
 *   - If NO: Need smaller distance
 * 
 * STEP 4: Validation (Greedy Approach):
 * Place first cow at first stall
 * For remaining cows:
 *   - Place at first available stall that is >= distance away
 *   - If we can place all C cows → distance is valid
 * 
 * DETAILED EXAMPLE:
 * -----------------
 * stalls = [1, 2, 4, 8, 9], cows = 3
 * After sorting: [1, 2, 4, 8, 9]
 * 
 * Search space: [1, 8] (9-1=8)
 * 
 * Iteration 1: mid = 4
 *   Can we place 3 cows with min distance 4?
 *   - Cow 1 at position 1
 *   - Next cow at position >= 1+4 = 5, so place at 8
 *   - Next cow at position >= 8+4 = 12, no stall available
 *   - Only placed 2 cows ✗
 *   Distance 4 too large, try smaller
 *   right = 3
 * 
 * Iteration 2: mid = 2
 *   Can we place 3 cows with min distance 2?
 *   - Cow 1 at position 1
 *   - Next at >= 3, so place at 4
 *   - Next at >= 6, so place at 8
 *   - Placed 3 cows! ✓
 *   Distance 2 works, try larger
 *   left = 3
 * 
 * Iteration 3: mid = 3
 *   Can we place 3 cows with min distance 3?
 *   - Cow 1 at position 1
 *   - Next at >= 4, place at 4
 *   - Next at >= 7, place at 8
 *   - Placed 3 cows! ✓
 *   Distance 3 works, try larger
 *   left = 4
 * 
 * Iteration 4: left=4, right=3, STOP
 * 
 * Answer: 3
 * 
 * TIME COMPLEXITY: O(n log n + n * log(max-min))
 * SPACE COMPLEXITY: O(1) or O(log n) for sorting
 */

import java.util.*;

public class AggressiveCows_Classic {
    
    public int aggressiveCows(int[] stalls, int cows) {
        // Step 1: Sort stalls (CRITICAL!)
        Arrays.sort(stalls);
        
        int n = stalls.length;
        
        // Step 2: Define search space
        int left = 1;                          // Minimum distance
        int right = stalls[n-1] - stalls[0];  // Maximum distance
        int result = 0;
        
        // Step 3: Binary search on distance
        while (left <= right) {
            int mid = left + (right - left) / 2;
            
            // Check if we can place cows with min distance = mid
            if (canPlaceCows(stalls, cows, mid)) {
                result = mid;      // This distance works, save it
                left = mid + 1;    // Try larger distance (maximize)
            } else {
                right = mid - 1;   // Distance too large, try smaller
            }
        }
        
        return result;
    }
    
    // Validation: Can we place 'cows' with minimum distance 'minDist'?
    private boolean canPlaceCows(int[] stalls, int cows, int minDist) {
        int cowsPlaced = 1;  // Place first cow at first stall
        int lastPosition = stalls[0];
        
        // Try to place remaining cows
        for (int i = 1; i < stalls.length; i++) {
            // If current stall is far enough from last cow
            if (stalls[i] - lastPosition >= minDist) {
                cowsPlaced++;
                lastPosition = stalls[i];
                
                // If all cows placed, we're done!
                if (cowsPlaced == cows) {
                    return true;
                }
            }
        }
        
        return cowsPlaced >= cows;
    }
    
    public static void main(String[] args) {
        AggressiveCows_Classic solution = new AggressiveCows_Classic();
        
        // Test Case 1
        int[] stalls1 = {1, 2, 4, 8, 9};
        int cows1 = 3;
        System.out.println("Test Case 1:");
        System.out.println("Stalls: " + Arrays.toString(stalls1));
        System.out.println("Cows: " + cows1);
        System.out.println("Maximum minimum distance: " + solution.aggressiveCows(stalls1, cows1));
        System.out.println();
        
        // Test Case 2
        int[] stalls2 = {1, 2, 8, 4, 9};
        int cows2 = 3;
        System.out.println("Test Case 2 (Unsorted):");
        System.out.println("Stalls: " + Arrays.toString(stalls2));
        System.out.println("Cows: " + cows2);
        System.out.println("Maximum minimum distance: " + solution.aggressiveCows(stalls2, cows2));
        System.out.println();
        
        // Test Case 3
        int[] stalls3 = {1, 2, 4, 5, 8, 9};
        int cows3 = 3;
        System.out.println("Test Case 3:");
        System.out.println("Stalls: " + Arrays.toString(stalls3));
        System.out.println("Cows: " + cows3);
        System.out.println("Maximum minimum distance: " + solution.aggressiveCows(stalls3, cows3));
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * KEY INSIGHTS:
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * 1. MAXIMIZE MINIMUM vs MINIMIZE MAXIMUM:
 *    This is "maximize minimum" problem
 *    → If distance D works, try D+1 (search right)
 *    → If distance D doesn't work, try D-1 (search left)
 * 
 * 2. WHY SORTING IS CRITICAL:
 *    Greedy placement only works on sorted positions
 *    Without sorting, we can't efficiently determine next valid position
 * 
 * 3. GREEDY VALIDATION:
 *    Always place cow at first available valid position
 *    This maximizes chances of placing remaining cows
 *    Proof: Moving a cow further right can only make it harder
 * 
 * 4. SEARCH DIRECTION:
 *    Maximize minimum: left = mid + 1 when valid
 *    Minimize maximum: right = mid - 1 when valid
 *    (Opposite of Koko Eating Bananas!)
 * 
 * 5. SIMILAR PROBLEMS:
 *    - Magnetic Force Between Balls (Leetcode 1552) - Almost identical!
 *    - Minimize Max Distance to Gas Station (Leetcode 774)
 *    - Book Allocation Problem
 * 
 * 6. INTERVIEW TIPS:
 *    - Draw a number line to visualize
 *    - Clearly state the monotonic property
 *    - Explain greedy validation strategy
 *    - Don't forget to sort!
 *    - Test with edge cases (cows=2, all stalls same distance)
 * 
 * 7. COMMON MISTAKES:
 *    ❌ Forgetting to sort stalls
 *    ❌ Wrong search direction (left vs right)
 *    ❌ Not initializing result properly
 *    ❌ Off-by-one in validation (>= vs >)
 * 
 * 8. EDGE CASES:
 *    - cows = 2 (just place at extremes)
 *    - cows = stalls.length (distance = 0 or 1)
 *    - All stalls equidistant
 *    - Two stalls only
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 */

/*
 * COMPARISON: Maximize Minimum vs Minimize Maximum
 * ==================================================
 * 
 * MAXIMIZE MINIMUM (Aggressive Cows):
 * -----------------------------------
 * if (canPlace(mid)) {
 *     result = mid;
 *     left = mid + 1;   // Try larger
 * } else {
 *     right = mid - 1;  // Need smaller
 * }
 * 
 * MINIMIZE MAXIMUM (Koko Bananas):
 * ---------------------------------
 * if (canFinish(mid)) {
 *     result = mid;
 *     right = mid - 1;  // Try smaller
 * } else {
 *     left = mid + 1;   // Need larger
 * }
 * 
 * Remember this difference - it's crucial!
 */

