# Factory Design Patterns

Factory patterns are creational patterns that deal with the problem of creating objects without specifying the exact class of the object that will be created. They encapsulate object creation logic, making the code more flexible, extensible, and decoupled.

---

## 1. Factory Method Pattern

The Factory Method pattern provides an interface for creating objects in a superclass but allows subclasses to alter the type of objects that will be created.

**Core Idea**: Define a method for creating an object, but let subclasses decide which class to instantiate.

Our project uses a variation of this pattern called a **Static Factory Method**.

### Static Factory Method

This is a simple and common implementation where a single factory class contains a `static` method that returns an instance of a class based on the input parameters. It doesn't require subclasses and is often sufficient for many use cases.

Our project's `StorageClientFactory` is a perfect example.

**Structure:**
- **Product Interface (`StorageClient`)**: Defines the common interface for all objects the factory can create.
- **Concrete Products (`AWSS3Client`, `GCSClient`)**: Implementations of the product interface.
- **Factory (`StorageClientFactory`)**: A class with a static method (`create`) that contains the logic to decide which concrete product to instantiate.

```java
// 1. The Product Interface
public interface StorageClient {
    void upload(StorageRequest r);
}

// 2. Concrete Products
public class AWSS3Client implements StorageClient { /* ... */ }
public class GCSClient implements StorageClient { /* ... */ }

// 3. The Static Factory
public class StorageClientFactory {
    public static StorageClient create(String provider) {
        switch (provider) {
            case "AWS":
                return new AWSS3Client();
            case "GCS":
                return new GCSClient();
            default:
                throw new IllegalArgumentException("Unknown provider");
        }
    }
}
```

**Usage:**
The client code is decoupled from the concrete implementations. It only needs to know about the `StorageClient` interface and the `StorageClientFactory`.

```java
StorageClient client = StorageClientFactory.create("AWS");
client.upload(myRequest);
```

---

## 2. Abstract Factory Pattern

The Abstract Factory pattern provides an interface for creating **families of related or dependent objects** without specifying their concrete classes. It is a "factory of factories."

**Core Idea**: Create families of related objects. For example, an `AWSFactory` might create an `AWSS3Client` and an `AWSLogger`, while a `GCSFactory` creates a `GCSClient` and a `GCSLogger`.

Our project's `CloudFactory` demonstrates this pattern.

**Structure:**
- **Abstract Factory Interface (`CloudFactory`)**: Declares a set of methods for creating each of the abstract products.
- **Concrete Factories (`AWSCloudFactory`, `GCSCloudFactory`)**: Implement the abstract factory interface to create a specific family of products.
- **Abstract Product Interfaces (`StorageClient`, `Logger`)**: Define the interfaces for the families of related products.
- **Concrete Products (`AWSS3Client`, `AWSLogger`, etc.)**: The specific implementations of the products.

```java
// 1. Abstract Product Interfaces
public interface StorageClient { /* ... */ }
public interface Logger { /* ... */ }

// 2. Abstract Factory Interface
public interface CloudFactory {
    StorageClient createStorageClient();
    Logger createLogger();
}

// 3. Concrete Factories
public class AWSCloudFactory implements CloudFactory {
    @Override
    public StorageClient createStorageClient() {
        return new AWSS3Client();
    }

    @Override
    public Logger createLogger() {
        return new AWSLogger();
    }
}

public class GCSCloudFactory implements CloudFactory {
    // ... returns GCSClient and GCSLogger
}
```

**Usage:**
The client is configured with a specific factory (`AWSCloudFactory` or `GCSCloudFactory`) and uses it to create the objects it needs. This ensures that the client uses a consistent family of objects (e.g., it won't accidentally use an `AWSS3Client` with a `GCSLogger`).

```java
public class CloudApp {
    private final StorageClient storageClient;
    private final Logger logger;

    // The app is configured with a factory
    public CloudApp(CloudFactory factory) {
        this.storageClient = factory.createStorageClient();
        this.logger = factory.createLogger();
    }

    public void upload(StorageRequest request) {
        storageClient.upload(request);
        logger.info("Upload complete");
    }
}

// Client Code
CloudFactory awsFactory = new AWSCloudFactory();
CloudApp app = new CloudApp(awsFactory);
app.upload(myRequest);
```

---

## Factory Pattern in Airline Management System

### What Problem Does It Solve?
When object creation logic is **complex or varies** based on conditions, scattering `new` operators throughout the code creates tight coupling. If you need to change how objects are created, you must change every `new` call.

### What is Changing? What Varies?
- **The type of object created** varies: Domestic vs International flight, Economy vs Business seat, Credit Card vs UPI payment.
- **The creation logic** varies: Different validation rules, different default values, different dependencies.

### What Should Remain Stable?
- The **interface** of the created object should remain stable.
- The **client code** that uses the object should not change when creation logic changes.

### Who Owns This Responsibility?
- A **Factory class** should own the creation logic, not the client.

### Where is My Code Tightly Coupled?
- In [`Main.java`](src/airline_management_system/Main.java:27), the client directly calls `new Flight.FlightBuilder(...)` — it knows the exact construction process.
- [`UserFactory.java`](src/airline_management_system/factory/UserFactory.java:7) has a switch statement that creates different user types. This is a Factory Method, but it was incomplete (now fixed).

### Implementation Steps for Airline Management System

#### Step 1: `FlightFactory` Interface (Already Defined)
**Current State**: [`FlightFactory.java`](src/airline_management_system/factory/FlightFactory.java:3) defines the interface with `createFlight()` and `displayFlightDetails()`.

**What's Working**:
- Interface defines the contract for all flight factories

**What's Pending**:
- Need to implement concrete factories: `DomesticFlightFactory`, `InternationalFlightFactory`, `CharterFlightFactory`

#### Step 2: Concrete Flight Factories (Already Implemented)
**Current State**: [`DomesticFlightFactory`](src/airline_management_system/factory/flight_factory/DomesticFlightFactory.java:14), [`InternationalFlightFactory`](src/airline_management_system/factory/flight_factory/InternationalFlightFactory.java:14), and [`CharterFlightFactory`](src/airline_management_system/factory/flight_factory/CharterFlightFactory.java) are implemented.

**What's Working**:
- Each factory creates a `Flight` with appropriate default settings
- `DomesticFlightFactory` uses `FlightTypes.DOMESTIC_FLIGHTS`
- `InternationalFlightFactory` uses `FlightTypes.INTERNATIONAL_FLIGHTS`
- `CharterFlightFactory` uses `FlightTypes.CHARTER_FLIGHTS`

**What's Pending**:
- None — these are fully implemented

#### Step 3: `FlightFactoryProvider` (Already Implemented)
**Current State**: [`FlightFactoryProvider`](src/airline_management_system/provider/FlightFactoryProvider.java:13) is a static provider that returns the appropriate factory based on `FlightTypes`.

**What's Working**:
- Static method `getFactory()` takes all required parameters
- Uses switch expression to return the correct factory
- No instance fields — stateless and thread-safe

**What's Pending**:
- None — this is fully implemented

#### Step 4: `BookingFactory` Interface (Already Defined)
**Current State**: [`BookingFactory.java`](src/airline_management_system/factory/BookingFactory.java:7) defines the interface.

**What's Pending**:
- Need to implement concrete factories: `StandardBookingFactory`, `GroupBookingFactory`, `CharterBookingFactory`

#### Step 5: `PaymentFactory` Interface (Already Defined)
**Current State**: [`PaymentFactory.java`](src/airline_management_system/factory/PaymentFactory.java:5) defines the interface.

**What's Pending**:
- Need to implement concrete factories: `CreditCardPaymentFactory`, `UPIPaymentFactory`, `NetBankingPaymentFactory`

#### Step 6: `SeatFactory` Interface (Already Defined)
**Current State**: [`SeatFactory.java`](src/airline_management_system/factory/SeatFactory.java:3) defines the interface.

**What's Pending**:
- Need to implement concrete factories: `EconomySeatFactory`, `BusinessSeatFactory`, `FirstClassSeatFactory`

#### Step 7: `UserFactory` (Already Implemented)
**Current State**: [`UserFactory`](src/airline_management_system/factory/UserFactory.java:9) is a static factory that creates `Person` objects based on `PersonType`.

**What's Working**:
- Private constructor (prevents instantiation)
- Static `createUser()` method takes all three builders as parameters
- Uses switch expression to call the appropriate builder's `build()` method

**What's Pending**:
- None — this is fully implemented

### Summary: Factory Pattern Status

| Factory | Status | Notes |
|---------|--------|-------|
| `FlightFactory` (interface) | ✅ Defined | |
| `DomesticFlightFactory` | ✅ Implemented | |
| `InternationalFlightFactory` | ✅ Implemented | |
| `CharterFlightFactory` | ✅ Implemented | |
| `FlightFactoryProvider` | ✅ Implemented | Static provider, no instance fields |
| `BookingFactory` (interface) | ✅ Defined | Concrete implementations pending |
| `PaymentFactory` (interface) | ✅ Defined | Concrete implementations pending |
| `SeatFactory` (interface) | ✅ Defined | Concrete implementations pending |
| `UserFactory` | ✅ Implemented | Creates Passenger, AirportStaff, or Pilot |

**Pending Work**:
- Implement `StandardBookingFactory`, `GroupBookingFactory`, `CharterBookingFactory`
- Implement `CreditCardPaymentFactory`, `UPIPaymentFactory`, `NetBankingPaymentFactory`
- Implement `EconomySeatFactory`, `BusinessSeatFactory`, `FirstClassSeatFactory`

---

## When to Avoid the Factory Pattern

| Scenario | Why to Avoid |
|----------|-------------|
| **Only one type of object is ever created** | If there's no variation in what you're creating, a Factory adds unnecessary indirection. Use `new` directly. |
| **Object creation is trivial** | If creating an object is just `new ClassName(args)` with no complex logic, a Factory is overkill. |
| **The creation logic is unlikely to change** | Factories shine when creation logic varies or is expected to evolve. If it's stable, don't abstract it. |
| **You need different subclasses based on runtime data that's already available** | Sometimes a simple `if-else` or `switch` at the call site is clearer than creating a whole Factory hierarchy. |
| **Performance is critical and Factory adds overhead** | In hot paths, the extra method call and object creation in a Factory can matter. Profile first. |
| **You're using dependency injection (Spring, etc.)** | Modern DI frameworks already handle object creation and wiring. Adding a Factory on top can create redundant layers. |

### Quick Decision Guide

```
Do you have multiple types of objects to create?
├── NO → Use `new` directly ✅
└── YES → Is the creation logic complex or likely to change?
    ├── NO → Use a simple static factory method ✅
    └── YES → Use Factory Method or Abstract Factory ✅
```
