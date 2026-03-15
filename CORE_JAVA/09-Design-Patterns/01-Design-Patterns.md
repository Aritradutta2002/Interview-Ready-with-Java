# Design Patterns - Complete Guide

## Creational Patterns

### 1. Singleton
```java
// Thread-safe Singleton (Double-Checked Locking)
public class Singleton {
    private static volatile Singleton instance;
    
    private Singleton() { }  // Private constructor
    
    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}

// Best practice: Enum Singleton
public enum Singleton {
    INSTANCE;
    
    public void doSomething() { }
}

// Usage
Singleton.INSTANCE.doSomething();

// Alternative: Bill Pugh (Inner Static Class)
public class Singleton {
    private Singleton() { }
    
    private static class Holder {
        static final Singleton INSTANCE = new Singleton();
    }
    
    public static Singleton getInstance() {
        return Holder.INSTANCE;
    }
}
```

---

### 2. Factory Method
```java
// Define interface for creating object, let subclasses decide
interface Product { void use(); }

class ConcreteProductA implements Product {
    public void use() { System.out.println("Product A"); }
}

class ConcreteProductB implements Product {
    public void use() { System.out.println("Product B"); }
}

abstract class Creator {
    public abstract Product createProduct();
    
    public void doSomething() {
        Product product = createProduct();
        product.use();
    }
}

class CreatorA extends Creator {
    public Product createProduct() {
        return new ConcreteProductA();
    }
}

// Usage
Creator creator = new CreatorA();
creator.doSomething();
```

---

### 3. Abstract Factory
```java
// Factory of factories
interface Button { void render(); }
interface Checkbox { void render(); }

// Windows family
class WindowsButton implements Button {
    public void render() { System.out.println("Windows Button"); }
}
class WindowsCheckbox implements Checkbox {
    public void render() { System.out.println("Windows Checkbox"); }
}

// Mac family
class MacButton implements Button {
    public void render() { System.out.println("Mac Button"); }
}
class MacCheckbox implements Checkbox {
    public void render() { System.out.println("Mac Checkbox"); }
}

// Abstract Factory
interface GUIFactory {
    Button createButton();
    Checkbox createCheckbox();
}

class WindowsFactory implements GUIFactory {
    public Button createButton() { return new WindowsButton(); }
    public Checkbox createCheckbox() { return new WindowsCheckbox(); }
}

class MacFactory implements GUIFactory {
    public Button createButton() { return new MacButton(); }
    public Checkbox createCheckbox() { return new MacCheckbox(); }
}

// Usage
GUIFactory factory = new WindowsFactory();
Button button = factory.createButton();
button.render();
```

---

### 4. Builder
```java
// Step-by-step construction of complex objects
public class User {
    private final String firstName;
    private final String lastName;
    private final int age;
    private final String phone;
    private final String address;
    
    private User(Builder builder) {
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.age = builder.age;
        this.phone = builder.phone;
        this.address = builder.address;
    }
    
    public static class Builder {
        private final String firstName;
        private final String lastName;
        private int age;
        private String phone;
        private String address;
        
        public Builder(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }
        
        public Builder age(int age) {
            this.age = age;
            return this;
        }
        
        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }
        
        public Builder address(String address) {
            this.address = address;
            return this;
        }
        
        public User build() {
            return new User(this);
        }
    }
}

// Usage
User user = new User.Builder("John", "Doe")
    .age(30)
    .phone("123-456-7890")
    .address("123 Main St")
    .build();
```

---

### 5. Prototype
```java
// Clone existing objects
interface Prototype<T> {
    T clone();
}

class Person implements Prototype<Person> {
    private String name;
    private int age;
    
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
    
    // Copy constructor
    public Person(Person other) {
        this.name = other.name;
        this.age = other.age;
    }
    
    @Override
    public Person clone() {
        return new Person(this);
    }
}

// Usage
Person original = new Person("John", 30);
Person copy = original.clone();
```

---

## Structural Patterns

### 6. Adapter
```java
// Convert interface to another interface
interface MediaPlayer {
    void play(String filename);
}

class MP3Player implements MediaPlayer {
    public void play(String filename) {
        System.out.println("Playing MP3: " + filename);
    }
}

class VLCPlayer {
    public void playVLC(String filename) {
        System.out.println("Playing VLC: " + filename);
    }
}

// Adapter
class VLCAdapter implements MediaPlayer {
    private VLCPlayer vlcPlayer;
    
    public VLCAdapter(VLCPlayer vlcPlayer) {
        this.vlcPlayer = vlcPlayer;
    }
    
    public void play(String filename) {
        vlcPlayer.playVLC(filename);
    }
}

// Usage
MediaPlayer player = new VLCAdapter(new VLCPlayer());
player.play("movie.vlc");
```

---

### 7. Decorator
```java
// Add behavior dynamically
interface Coffee {
    double cost();
    String description();
}

class SimpleCoffee implements Coffee {
    public double cost() { return 5.0; }
    public String description() { return "Simple Coffee"; }
}

abstract class CoffeeDecorator implements Coffee {
    protected Coffee coffee;
    
    public CoffeeDecorator(Coffee coffee) {
        this.coffee = coffee;
    }
}

class MilkDecorator extends CoffeeDecorator {
    public MilkDecorator(Coffee coffee) {
        super(coffee);
    }
    
    public double cost() { return coffee.cost() + 1.5; }
    public String description() { return coffee.description() + ", Milk"; }
}

class SugarDecorator extends CoffeeDecorator {
    public SugarDecorator(Coffee coffee) {
        super(coffee);
    }
    
    public double cost() { return coffee.cost() + 0.5; }
    public String description() { return coffee.description() + ", Sugar"; }
}

// Usage
Coffee coffee = new SimpleCoffee();
coffee = new MilkDecorator(coffee);
coffee = new SugarDecorator(coffee);
System.out.println(coffee.description() + " $" + coffee.cost());
// Output: Simple Coffee, Milk, Sugar $7.0
```

---

### 8. Proxy
```java
// Placeholder for another object
interface Image {
    void display();
}

class RealImage implements Image {
    private String filename;
    
    public RealImage(String filename) {
        this.filename = filename;
        loadFromDisk();
    }
    
    private void loadFromDisk() {
        System.out.println("Loading " + filename);
    }
    
    public void display() {
        System.out.println("Displaying " + filename);
    }
}

class ProxyImage implements Image {
    private String filename;
    private RealImage realImage;
    
    public ProxyImage(String filename) {
        this.filename = filename;
    }
    
    public void display() {
        if (realImage == null) {
            realImage = new RealImage(filename);  // Lazy loading
        }
        realImage.display();
    }
}

// Usage
Image image = new ProxyImage("photo.jpg");
image.display();  // Loads and displays
image.display();  // Only displays (already loaded)
```

---

### 9. Composite
```java
// Tree structure of objects
interface Component {
    void operation();
}

class Leaf implements Component {
    private String name;
    
    public Leaf(String name) { this.name = name; }
    public void operation() { System.out.println("Leaf: " + name); }
}

class Composite implements Component {
    private List<Component> children = new ArrayList<>();
    private String name;
    
    public Composite(String name) { this.name = name; }
    
    public void add(Component c) { children.add(c); }
    public void remove(Component c) { children.remove(c); }
    
    public void operation() {
        System.out.println("Composite: " + name);
        for (Component c : children) {
            c.operation();
        }
    }
}

// Usage
Composite root = new Composite("Root");
root.add(new Leaf("Leaf 1"));
Composite sub = new Composite("Sub");
sub.add(new Leaf("Leaf 2"));
root.add(sub);
root.operation();
```

---

## Behavioral Patterns

### 10. Observer
```java
// One-to-many dependency
interface Observer {
    void update(String message);
}

interface Subject {
    void attach(Observer o);
    void detach(Observer o);
    void notifyObservers();
}

class NewsAgency implements Subject {
    private List<Observer> observers = new ArrayList<>();
    private String news;
    
    public void attach(Observer o) { observers.add(o); }
    public void detach(Observer o) { observers.remove(o); }
    
    public void setNews(String news) {
        this.news = news;
        notifyObservers();
    }
    
    public void notifyObservers() {
        for (Observer o : observers) {
            o.update(news);
        }
    }
}

class NewsChannel implements Observer {
    private String name;
    
    public NewsChannel(String name) { this.name = name; }
    
    public void update(String news) {
        System.out.println(name + " received: " + news);
    }
}

// Usage
NewsAgency agency = new NewsAgency();
agency.attach(new NewsChannel("CNN"));
agency.attach(new NewsChannel("BBC"));
agency.setNews("Breaking News!");
```

---

### 11. Strategy
```java
// Interchangeable algorithms
interface PaymentStrategy {
    void pay(int amount);
}

class CreditCardPayment implements PaymentStrategy {
    public void pay(int amount) {
        System.out.println("Paid $" + amount + " via Credit Card");
    }
}

class PayPalPayment implements PaymentStrategy {
    public void pay(int amount) {
        System.out.println("Paid $" + amount + " via PayPal");
    }
}

class ShoppingCart {
    private PaymentStrategy strategy;
    
    public void setPaymentStrategy(PaymentStrategy strategy) {
        this.strategy = strategy;
    }
    
    public void checkout(int amount) {
        strategy.pay(amount);
    }
}

// Usage
ShoppingCart cart = new ShoppingCart();
cart.setPaymentStrategy(new CreditCardPayment());
cart.checkout(100);
```

---

### 12. Chain of Responsibility
```java
// Pass request along chain
abstract class Handler {
    protected Handler next;
    
    public Handler setNext(Handler next) {
        this.next = next;
        return next;
    }
    
    public abstract void handle(int request);
}

class AuthHandler extends Handler {
    public void handle(int request) {
        if (request < 10) {
            System.out.println("Auth handled");
        } else if (next != null) {
            next.handle(request);
        }
    }
}

class LogHandler extends Handler {
    public void handle(int request) {
        System.out.println("Log handled");
        if (next != null) next.handle(request);
    }
}

// Usage
Handler auth = new AuthHandler();
Handler log = new LogHandler();
auth.setNext(log);
auth.handle(5);  // Auth handles
auth.handle(15); // Passes to Log
```

---

### 13. Command
```java
// Encapsulate request as object
interface Command {
    void execute();
    void undo();
}

class Light {
    public void on() { System.out.println("Light ON"); }
    public void off() { System.out.println("Light OFF"); }
}

class LightOnCommand implements Command {
    private Light light;
    
    public LightOnCommand(Light light) { this.light = light; }
    public void execute() { light.on(); }
    public void undo() { light.off(); }
}

class RemoteControl {
    private Command command;
    
    public void setCommand(Command command) {
        this.command = command;
    }
    
    public void pressButton() {
        command.execute();
    }
}

// Usage
RemoteControl remote = new RemoteControl();
remote.setCommand(new LightOnCommand(new Light()));
remote.pressButton();
```

---

### 14. Template Method
```java
// Define algorithm skeleton in base class
abstract class DataProcessor {
    // Template method
    public final void process() {
        readData();
        processData();
        writeData();
    }
    
    protected abstract void readData();
    protected abstract void processData();
    
    protected void writeData() {
        System.out.println("Writing data to file");
    }
}

class CSVProcessor extends DataProcessor {
    protected void readData() { System.out.println("Reading CSV"); }
    protected void processData() { System.out.println("Processing CSV"); }
}

class JSONProcessor extends DataProcessor {
    protected void readData() { System.out.println("Reading JSON"); }
    protected void processData() { System.out.println("Processing JSON"); }
}

// Usage
DataProcessor processor = new CSVProcessor();
processor.process();
```

---

## Interview Q&A

**Q: When to use Singleton?**
A: Logger, configuration, connection pool, thread pool. Use enum for thread safety.

**Q: Difference between Factory and Abstract Factory?**
A: Factory creates one type. Abstract Factory creates families of related types.

**Q: When to use Builder?**
A: When object has many optional parameters, immutable object construction.

**Q: Difference between Decorator and Proxy?**
A: Decorator adds behavior. Proxy controls access.

**Q: When to use Strategy pattern?**
A: When you need to switch algorithms at runtime (payment methods, sorting).

**Q: What is the benefit of Observer pattern?**
A: Loose coupling between subject and observers. Subject doesn't know concrete observers.

**Q: When to use Chain of Responsibility?**
A: When multiple handlers may process a request (middleware, validation pipeline).
