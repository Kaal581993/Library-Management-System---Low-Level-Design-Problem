# Singleton Design Pattern

The Singleton pattern is a creational design pattern that ensures a class has only one instance and provides a single, global point of access to it. It is commonly used for objects that need to coordinate actions across a system, such as a configuration manager, a logger, or a connection pool.

The key principles of a Singleton are:
- **Single Instance**: The class can be instantiated only once.
- **Global Access**: A globally accessible method provides access to the single instance.
- **Private Constructor**: The constructor is marked as `private` to prevent external instantiation with the `new` keyword.

---

## Types of Singleton Implementation

There are several ways to implement the Singleton pattern, each with its own trade-offs regarding thread safety, performance, and lazy initialization.

### 1. Eager Initialization

The instance is created at the time of class loading. It's the simplest method and is inherently thread-safe.

**Use Case**: Good if the object is not resource-intensive or is always needed.

```java
public class EagerSingleton {
    private static final EagerSingleton INSTANCE = new EagerSingleton();

    private EagerSingleton() {}

    public static EagerSingleton getInstance() {
        return INSTANCE;
    }
}
```

### 2. Lazy Initialization

The instance is created only when the `getInstance()` method is called for the first time. This saves resources but is **not thread-safe** in its basic form.

```java
public class LazySingleton {
    private static LazySingleton instance;

    private LazySingleton() {}

    public static LazySingleton getInstance() {
        if (instance == null) {
            instance = new LazySingleton();
        }
        return instance;
    }
}
```

### 3. Thread-Safe Lazy Initialization (Synchronized Method)

This approach makes the `getInstance()` method `synchronized`, ensuring that only one thread can execute it at a time. It is thread-safe but can cause performance bottlenecks as the method is synchronized every time it's called.

```java
public class ThreadSafeSingleton {
    private static ThreadSafeSingleton instance;

    private ThreadSafeSingleton() {}

    public static synchronized ThreadSafeSingleton getInstance() {
        if (instance == null) {
            instance = new ThreadSafeSingleton();
        }
        return instance;
    }
}
```

### 4. Double-Checked Locking

This pattern attempts to reduce the performance overhead of the synchronized method. It synchronizes only the first time the instance is created. The `volatile` keyword is crucial here to prevent issues related to how compilers and CPUs can reorder instructions.

```java
public class DoubleCheckedSingleton {
    private static volatile DoubleCheckedSingleton instance;

    private DoubleCheckedSingleton() {}

    public static DoubleCheckedSingleton getInstance() {
        if (instance == null) { // First check (no lock)
            synchronized (DoubleCheckedSingleton.class) {
                if (instance == null) { // Second check (with lock)
                    instance = new DoubleCheckedSingleton();
                }
            }
        }
        return instance;
    }
}
```

### 5. Bill Pugh Singleton (Initialization-on-demand Holder)

**This is the preferred approach for most use cases.** It relies on a private static inner class to hold the singleton instance. The JVM guarantees that the inner class is loaded (and the instance created) only when `getInstance()` is called. This provides lazy initialization and is inherently thread-safe without any `synchronized` keywords.

Our project's `ConfigManager` uses this pattern.

```java
public class ConfigManager {
    private ConfigManager() {}

    // Private static inner class that holds the instance
    private static class SingletonHelper {
        private static final ConfigManager INSTANCE = new ConfigManager();
    }

    public static ConfigManager getInstance() {
        return SingletonHelper.INSTANCE;
    }
}
```

---

## Comparison of Singleton Implementations

| Feature | Eager Initialization | Lazy Initialization | Thread-Safe (Synchronized) | Double-Checked Locking | Bill Pugh (Holder) | Enum Singleton |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **Initialization** | Eager | Lazy | Lazy | Lazy | Lazy | Eager |
| **Thread-Safe** | Yes | No | Yes | Yes | Yes | Yes |
| **Performance** | High | High (no lock) | Low (always locks) | High (locks once) | High (no lock) | High |
| **Reflection Safe**| No | No | No | No | No | Yes |
| **Serialization**| Requires `readResolve()` | Requires `readResolve()` | Requires `readResolve()` | Requires `readResolve()` | Requires `readResolve()` | Yes (out of the box) |
| **Best For** | Simple, always-needed objects | Single-threaded apps (rare) | Quick thread-safety | High-performance needs | **Most general cases** | Ultimate safety |

---

## Singleton Pattern Recommendations for Your Project

Based on your project structure, here are some recommendations for applying the Singleton pattern:

### 1. `IdGenerator.java`

- **Current State**: This class uses `static` methods and a `static` map to manage ID generation. While functional, it doesn't prevent developers from creating instances of `IdGenerator`, which could be confusing.
- **Recommendation**: Convert `IdGenerator` into a proper Singleton using the **Bill Pugh (Initialization-on-demand Holder)** pattern.
- **Why?**:
    - It formally enforces the single-instance rule, which is critical for a class designed to issue unique IDs across the application.
    - The Bill Pugh pattern is the ideal choice here. It's thread-safe by default, offers lazy initialization (so it's only created when first needed), and does so without the performance cost of `synchronized` blocks.
- **Suggested Approach**:
    1. Make the `IdGenerator` constructor `private`.
    2. Create a `private static class IdGeneratorHolder` inside `IdGenerator`.
    3. Initialize the singleton instance within the holder class: `private static final IdGenerator INSTANCE = new IdGenerator();`.
    4. Create a public `getInstance()` method that returns `IdGeneratorHolder.INSTANCE`.
    5. Make the `generateId` method non-static, so it must be called on the single instance (e.g., `IdGenerator.getInstance().generateId(...)`).

### 2. Future Service or Manager Classes

- **Scenario**: As your application grows, you will likely create classes to manage application-wide resources or state, such as a `BookingService` (to handle all flight bookings) or a `FlightManager` (to track all active flights).
- **Recommendation**: These classes are prime candidates for the Singleton pattern.
- **Why?**: You need a single, central point of authority for managing collections of objects like bookings or flights. This prevents data inconsistencies and race conditions that could occur if multiple managers were operating independently (e.g., two services trying to book the same seat).
- **Approach**: The **Bill Pugh** pattern remains an excellent and recommended choice for these scenarios due to its simplicity, thread-safety, and performance.

---

## Singleton Pattern in Airline Management System

### What Problem Does It Solve?
When you need **exactly one instance** of a class to coordinate actions across the system. Multiple instances could cause:
- **Data inconsistency**: Multiple `IdGenerator` instances generating duplicate IDs
- **Resource waste**: Multiple `BookingManager` instances holding separate booking lists
- **Race conditions**: Concurrent access to shared resources without a single point of control

### What is Changing? What Varies?
- Nothing varies — the Singleton is **always the same instance**.

### What Should Remain Stable?
- The **single instance** must remain stable throughout the application lifecycle.
- The **global access point** (`getInstance()`) must remain stable.

### Who Owns This Responsibility?
- The **Singleton class itself** owns the instance creation and access.

### Where is My Code Tightly Coupled?
- Currently, [`IdGenerator.generateId()`](src/airline_management_system/entity/util/IdGenerator.java:80) is `static`, allowing calls without the Singleton instance. This breaks the pattern.

### Implementation Steps for Airline Management System

#### Step 1: `IdGenerator` (Already Implemented — with Issues)
**Current State**: [`IdGenerator`](src/airline_management_system/entity/util/IdGenerator.java:8) uses the Bill Pugh Singleton pattern.

**What's Working**:
- Private static inner class `SingletonHelper` holds the instance
- `getInstance()` returns `SingletonHelper.INSTANCE`
- Thread-safe and lazy-initialized

**What's Pending**:
- `generateId()` method is `static` — should be an instance method to enforce Singleton usage
- The `counters` map is an instance field (good), but the static method undermines the pattern

**Fix Required**:
```java
// BEFORE:
public synchronized static String generateId(IdType type) { ... }

// AFTER:
public synchronized String generateId(IdType type) { ... } // remove 'static'
```

#### Step 2: `BookingManager` (Already Implemented)
**Current State**: [`BookingManager`](src/airline_management_system/entity/util/BookingManager.java:8) uses the Bill Pugh Singleton pattern.

**What's Working**:
- Private static inner class `Holder` holds the instance
- `getInstance()` returns `Holder.INSTANCE`
- Private constructor

**What's Pending**:
- None — this is fully implemented

#### Step 3: `FlightScheduleManager` (Already Implemented)
**Current State**: [`FlightScheduleManager`](src/airline_management_system/entity/util/FlightScheduleManager.java:3) uses the Bill Pugh Singleton pattern.

**What's Working**:
- Private static inner class `FlightScheduler` holds the instance
- `getInstance()` returns `FlightScheduler.INSTANCE`
- Private constructor

**What's Pending**:
- None — this is fully implemented

#### Step 4: `PaymentProcessor` (Already Implemented)
**Current State**: [`PaymentProcessor`](src/airline_management_system/entity/util/PaymentProcessor.java:3) uses the Bill Pugh Singleton pattern.

**What's Working**:
- Private static inner class `PaymentProcessHelper` holds the instance
- `getInstance()` returns `PaymentProcessHelper.INSTANCE`
- Private constructor

**What's Pending**:
- None — this is fully implemented

#### Step 5: `AirlineManagementSystem` (Already Implemented)
**Current State**: [`AirlineManagementSystem`](src/airline_management_system/entity/util/AirlineManagementSystem.java:3) uses the Bill Pugh Singleton pattern.

**What's Working**:
- Private static inner class `AMSHelper` holds the instance
- `getInstance()` returns `AMSHelper.INSTANCE`
- Private constructor

**What's Pending**:
- None — this is fully implemented

### Summary: Singleton Pattern Status

| Class | Status | Notes |
|-------|--------|-------|
| `IdGenerator` | ⚠️ Partial | Bill Pugh implemented, but `generateId()` is static — should be instance method |
| `BookingManager` | ✅ Implemented | Bill Pugh Singleton |
| `FlightScheduleManager` | ✅ Implemented | Bill Pugh Singleton |
| `PaymentProcessor` | ✅ Implemented | Bill Pugh Singleton |
| `AirlineManagementSystem` | ✅ Implemented | Bill Pugh Singleton |

**Pending Work**:
- Make `IdGenerator.generateId()` an instance method (remove `static` keyword)

---

## When to Avoid the Singleton Pattern

| Scenario | Why to Avoid |
|----------|-------------|
| **You need multiple instances** | If different parts of your system need independent instances (e.g., separate database connections), Singleton prevents this. |
| **Unit testing is a priority** | Singletons are hard to mock because they enforce a single instance globally. This makes testing difficult — you can't replace the Singleton with a test double. |
| **The object holds mutable global state** | Global mutable state is a source of bugs. If the Singleton's state changes, every part of the system sees the change, which can cause unexpected side effects. |
| **You're using dependency injection** | Modern frameworks (Spring, Dagger) manage object lifecycles. Using Singleton manually can conflict with the framework's management. |
| **The object is resource-intensive and not always needed** | A Singleton is created once and lives for the application's lifetime. If it's heavy (e.g., a large cache), consider lazy initialization or a pool instead. |
| **You need to serialize/deserialize the object** | Singletons don't serialize well — deserialization creates a new instance, breaking the Singleton contract. You need `readResolve()` to fix this. |
| **Multi-tenant or multi-user systems** | A single instance shared across all users can cause data leakage between tenants. Each tenant may need its own instance. |

### Quick Decision Guide

```
Do you need exactly one instance for the entire application?
├── NO → Don't use Singleton ✅
└── YES → Is the object stateless or read-only?
    ├── YES → Singleton is safe ✅
    └── NO → Can you use dependency injection instead?
        ├── YES → Use DI (preferred) ✅
        └── NO → Use Singleton with caution ⚠️
```
