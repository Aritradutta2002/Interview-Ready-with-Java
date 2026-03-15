# Java I/O and NIO - Interview Guide

## I/O Stream Hierarchy

```
                     ┌────────────┐
               ┌─────┤ InputStream├─────┐
               │     └────────────┘     │
        ┌──────▼──────┐         ┌───────▼──────┐
        │FileInputStream│       │BufferedInputStream│
        └─────────────┘         └──────────────┘

                     ┌─────────────┐
               ┌─────┤ OutputStream├─────┐
               │     └─────────────┘     │
        ┌──────▼───────┐        ┌────────▼──────────┐
        │FileOutputStream│      │BufferedOutputStream│
        └──────────────┘        └───────────────────┘

                     ┌────────┐
               ┌─────┤ Reader ├─────┐
               │     └────────┘     │
        ┌──────▼───────┐    ┌──────▼───────────┐
        │FileReader     │    │BufferedReader     │
        │InputStreamReader   │StringReader       │
        └──────────────┘    └──────────────────┘

                     ┌────────┐
               ┌─────┤ Writer ├─────┐
               │     └────────┘     │
        ┌──────▼───────┐    ┌──────▼───────────┐
        │FileWriter     │    │BufferedWriter     │
        │OutputStreamWriter  │PrintWriter        │
        └──────────────┘    └──────────────────┘
```

### Byte Streams vs Character Streams

| Feature | Byte Streams | Character Streams |
|---------|-------------|-------------------|
| Base class | InputStream / OutputStream | Reader / Writer |
| Unit | byte (8-bit) | char (16-bit) |
| Use case | Binary data (images, audio) | Text data |
| Encoding | Raw bytes | Handles character encoding |

---

## Reading and Writing Files

### try-with-resources (ALWAYS use this)
```java
// ✅ Auto-closes resources — prevents resource leaks
try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"));
     BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"))) {
    
    String line;
    while ((line = reader.readLine()) != null) {
        writer.write(line);
        writer.newLine();
    }
}  // reader and writer automatically closed here
```

### Reading Text Files
```java
// 1. BufferedReader — line by line (classic)
try (BufferedReader br = new BufferedReader(new FileReader("file.txt"))) {
    String line;
    while ((line = br.readLine()) != null) {
        System.out.println(line);
    }
}

// 2. Files.readAllLines — entire file into List (Java 7+)
List<String> lines = Files.readAllLines(Path.of("file.txt"));

// 3. Files.readString — entire file as String (Java 11+)
String content = Files.readString(Path.of("file.txt"));

// 4. Files.lines — Stream<String> (lazy, for large files)
try (Stream<String> stream = Files.lines(Path.of("file.txt"))) {
    stream.filter(line -> line.contains("error"))
          .forEach(System.out::println);
}

// 5. Scanner
try (Scanner scanner = new Scanner(new File("file.txt"))) {
    while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
    }
}
```

### Writing Text Files
```java
// 1. BufferedWriter
try (BufferedWriter bw = new BufferedWriter(new FileWriter("file.txt"))) {
    bw.write("Hello World");
    bw.newLine();
    bw.write("Second line");
}

// 2. PrintWriter (convenience methods)
try (PrintWriter pw = new PrintWriter(new FileWriter("file.txt"))) {
    pw.println("Hello World");
    pw.printf("Name: %s, Age: %d%n", "John", 30);
}

// 3. Files.writeString (Java 11+)
Files.writeString(Path.of("file.txt"), "Hello World");

// 4. Files.write — write lines
Files.write(Path.of("file.txt"), List.of("Line 1", "Line 2"));

// Append mode
Files.writeString(Path.of("file.txt"), "Appended", StandardOpenOption.APPEND);
```

---

## Serialization (Object I/O)

```java
// Serializable class
class Employee implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String name;
    private transient String password;  // Not serialized
    
    public Employee(String name, String password) {
        this.name = name;
        this.password = password;
    }
}

// Serialize (Object → bytes)
try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("emp.ser"))) {
    oos.writeObject(new Employee("John", "secret"));
}

// Deserialize (bytes → Object)
try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("emp.ser"))) {
    Employee emp = (Employee) ois.readObject();
    // emp.name = "John", emp.password = null (transient)
}
```

---

## NIO (New I/O) — Java 7+

### Path API
```java
// Creating Path objects
Path path = Path.of("src", "main", "java", "App.java");  // Java 11+
Path path = Paths.get("src/main/java/App.java");          // Java 7+
Path path = Path.of("C:\\Users\\john\\file.txt");          // Absolute

// Path operations
path.getFileName();      // "App.java"
path.getParent();        // "src/main/java"
path.getRoot();          // null (relative) or "C:\" (absolute)
path.toAbsolutePath();   // Full absolute path
path.normalize();        // Remove . and ..
path.resolve("sub");     // Append child path
path.relativize(other);  // Relative path between two paths
```

### Files Utility Class
```java
// File operations
boolean exists = Files.exists(path);
boolean isDir = Files.isDirectory(path);
boolean isFile = Files.isRegularFile(path);
long size = Files.size(path);

// Create
Files.createFile(path);
Files.createDirectory(path);
Files.createDirectories(path);  // Creates parent dirs too

// Copy, Move, Delete
Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
Files.move(source, target, StandardCopyOption.ATOMIC_MOVE);
Files.delete(path);           // Throws if not found
Files.deleteIfExists(path);   // Returns false if not found

// List directory contents
try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*.java")) {
    for (Path entry : stream) {
        System.out.println(entry.getFileName());
    }
}

// Walk directory tree
try (Stream<Path> walk = Files.walk(dir)) {
    walk.filter(Files::isRegularFile)
        .filter(p -> p.toString().endsWith(".java"))
        .forEach(System.out::println);
}

// File attributes
FileTime lastModified = Files.getLastModifiedTime(path);
String contentType = Files.probeContentType(path);
```

---

## I/O vs NIO Comparison

| Feature | I/O (java.io) | NIO (java.nio) |
|---------|---------------|-----------------|
| Approach | Stream-oriented | Buffer-oriented |
| Blocking | Blocking | Non-blocking possible |
| Channels | No | Yes (Channel + Buffer) |
| Selectors | No | Yes (multiplexing) |
| File API | File class | Path + Files |
| Best for | Simple file I/O | Scalable network I/O |

---

## Interview Q&A

**Q: What is try-with-resources?**
A: Auto-closes resources that implement `AutoCloseable`. Introduced in Java 7. Replaces try-finally pattern.

**Q: Difference between InputStream and Reader?**
A: InputStream reads raw bytes. Reader reads characters with encoding support.

**Q: What is BufferedReader and why use it?**
A: Wraps a Reader with a buffer for efficient reading. `readLine()` reads entire lines. Much faster than reading char-by-char.

**Q: What is serialVersionUID?**
A: Version identifier for Serializable classes. Ensures compatibility during deserialization. Change it when class structure changes incompatibly.

**Q: What does transient keyword do in serialization?**
A: Marks a field to be excluded from serialization. Deserialized as default value (null, 0, false).

**Q: Difference between Path and File?**
A: `Path` (NIO) is newer, more powerful, and supports more operations. `File` (I/O) is legacy. Prefer `Path` + `Files`.

**Q: How to read a large file efficiently?**
A: Use `Files.lines()` (Stream, lazy) or `BufferedReader`. Avoid `readAllLines()` for very large files (loads entire file into memory).

**Q: What is the difference between Files.copy and FileChannel?**
A: `Files.copy` is simpler for basic copy. `FileChannel` (with `transferTo`) is faster for large files — uses OS-level zero-copy.
