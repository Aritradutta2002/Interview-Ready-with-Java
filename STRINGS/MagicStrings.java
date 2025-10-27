package STRINGS;
import java.util.*;
/**
 * Problem: Magic Strings
 * 
 * Implement a program that processes string operations from standard input (stdin).
 * The program handles the following commands:
 * 
 * 1. x = "abc" - Assign a string literal to variable x
 * 2. x = y[l:r] - Assign substring of y from index l (inclusive) to r (exclusive)
 * 3. x = y+z - Assign concatenation of strings y and z
 * 4. x[l:r] - Print substring of x from l (inclusive) to r (exclusive), where r-l ≤ 10
 * 
 * Data Structures Used:
 * - HashMap: To store variable names and their string values
 * 
 * Time Complexity: O(n*m) where n is number of operations and m is average string length
 * Space Complexity: O(n*m) for storing all variables
 */

public class MagicStrings {
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Map<String, String> variables = new HashMap<>();
        
        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.isEmpty()) continue;
            
            processCommand(line, variables);
        }
        sc.close();
    }
    
    /**
     * Process a single command (assignment or print operation)
     * @param command The command string to process
     * @param variables HashMap storing all variables
     */
    private static void processCommand(String command, Map<String, String> variables) {
        if (command.contains("=")) {
            // Assignment operation
            String[] parts = command.split("=", 2);
            String varName = parts[0].trim();
            String value = parts[1].trim();
            
            if (value.startsWith("\"") && value.endsWith("\"")) {
                // String literal assignment: x = "abc"
                variables.put(varName, value.substring(1, value.length() - 1));
            } else if (value.contains("+")) {
                // Concatenation: x = y+z
                String[] vars = value.split("\\+");
                String var1 = vars[0].trim();
                String var2 = vars[1].trim();
                String result = variables.get(var1) + variables.get(var2);
                variables.put(varName, result);
            } else if (value.contains("[")) {
                // Substring: x = y[l:r]
                int bracketStart = value.indexOf('[');
                String sourceVar = value.substring(0, bracketStart).trim();
                String indices = value.substring(bracketStart + 1, value.length() - 1);
                String[] indexParts = indices.split(":");
                int l = Integer.parseInt(indexParts[0].trim());
                int r = Integer.parseInt(indexParts[1].trim());
                String result = variables.get(sourceVar).substring(l, r);
                variables.put(varName, result);
            }
        } else if (command.contains("[")) {
            // Print operation: x[l:r]
            int bracketStart = command.indexOf('[');
            String varName = command.substring(0, bracketStart).trim();
            String indices = command.substring(bracketStart + 1, command.length() - 1);
            String[] indexParts = indices.split(":");
            int l = Integer.parseInt(indexParts[0].trim());
            int r = Integer.parseInt(indexParts[1].trim());
            String substring = variables.get(varName).substring(l, r);
            System.out.println(substring);
        }
    }
}

