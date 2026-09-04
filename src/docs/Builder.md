# Builder Design Pattern

The Builder pattern is a creational design pattern that lets you construct complex objects step by step. It is particularly useful when an object requires many parameters (some optional, some required) to be created. The pattern separates the construction of a complex object from its representation, so the same construction process can create different representations.

Using a builder helps solve two common problems:
- **Telescoping Constructors**: A long list of constructors with different parameter combinations, which is hard to read and maintain.
- **JavaBeans Anti-Pattern**: Creating an object with a no-argument constructor and then setting fields with setters. This can leave the object in an inconsistent state partway through its construction and is not thread-safe.

A Builder pattern typically involves:
- A `Builder` class (often a static nested class) that has fields corresponding to the object's fields.
- Fluent "setter" methods in the `Builder` that return `this` to allow for method chaining.
- A `build()` method in the `Builder` that creates and returns the final, often immutable, object.

---
### What Problem Does It Solve?
- When a class has many constructor parameters (especially optional ones), the constructor becomes unwieldy. You end up with:

- Telescoping constructors: Many overloaded constructors
- Invalid states: Objects created with inconsistent data
- Hard to read: new Flight(aircraft, seatType, seatStatus, vendor, source, dest, depTime, arrTime, flightType) — what does each parameter mean?
### What is Changing? What Varies?
- The combination of fields used to create an object varies. Some flights need all fields, others need fewer.
- The order and presence of parameters changes based on the use case.
### What Should Remain Stable?
- The object's structure (what fields it has) should remain stable.
- The validation rules (required vs optional fields) should remain stable.
### Who Owns This Responsibility?
- The class being built should own the construction logic, not the client code.


---

## Types of Builder Pattern Implementation

### 1. Classic (Separate Builder)

The builder is a separate class from the object it builds. This is common but requires the object to have a public constructor or for the builder to be in the same package to access a package-private constructor.

### 2. Nested Static Builder (Most Common)

The `Builder` is a `public static` nested class within the object it builds. This is the most popular and idiomatic approach in Java. Because it is a nested class, it can access the private constructor of the enclosing class, allowing the final object to be truly immutable.

Our project's `StorageRequest` uses this pattern.

```java
public class StorageRequest {
    // Final fields make the object immutable
    private final String bucket;
    private final String key;
    private final String operation;
    private final String contentType;
    private final long sizeBytes;

    // Private constructor accepts the builder
    private StorageRequest(Builder builder) {
        this.bucket = builder.bucket;
        this.key = builder.key;
        this.operation = builder.operation;
        this.contentType = builder.contentType;
        this.sizeBytes = builder.sizeBytes;
    }

    // Public static nested Builder class
    public static class Builder {
        // Required fields
        private final String bucket;
        private final String key;
        private final String operation;

        // Optional fields with default values
        private String contentType = "application/octet-stream";
        private long sizeBytes = 0;

        public Builder(String bucket, String key, String operation) {
            this.bucket = bucket;
            this.key = key;
            this.operation = operation;
        }

        // Fluent methods for optional parameters
        public Builder contentType(String contentType) {
            this.contentType = contentType;
            return this; // Return this for chaining
        }

        public Builder sizeBytes(long sizeBytes) {
            this.sizeBytes = sizeBytes;
            return this;
        }

        // The final build() method
        public StorageRequest build() {
            // Can perform validation here
            if (bucket == null || key == null) {
                throw new IllegalStateException("Bucket and key cannot be null");
            }
            // Calls the private constructor
            return new StorageRequest(this);
        }
    }
}
```

**Usage:**

```java
StorageRequest request = new StorageRequest.Builder("my-bucket", "file.txt", "upload")
    .contentType("text/plain")
    .sizeBytes(1024)
    .build();
```

### 3. Builder with Director (Advanced)

In this variation, a `Director` class encapsulates a specific, common way to construct an object. The client uses the `Director` to build an object, abstracting away the specific steps. This is useful for creating standard configurations of an object.

```java
// The same Builder as before...

public class StorageRequestDirector {
    public StorageRequest buildDefaultUploadRequest(String bucket, String key) {
        return new StorageRequest.Builder(bucket, key, "upload")
            .contentType("application/octet-stream")
            .build();
    }

    public StorageRequest buildImageUploadRequest(String bucket, String key, long size) {
        return new StorageRequest.Builder(bucket, key, "upload")
            .contentType("image/jpeg")
            .sizeBytes(size)
            .build();
    }
}
```

---

## Builder Pattern in Airline Management System

### What Problem Does It Solve?
When a class has **many constructor parameters** (especially optional ones), the constructor becomes unwieldy. You end up with:
- **Telescoping constructors**: Many overloaded constructors
- **Invalid states**: Objects created with inconsistent data
- **Hard to read**: `new Flight(aircraft, seatType, seatStatus, vendor, source, dest, depTime, arrTime, flightType)` — what does each parameter mean?

### What is Changing? What Varies?
- The **combination of fields** used to create an object varies. Some flights need all fields, others need fewer.
- The **order and presence** of parameters changes based on the use case.

### What Should Remain Stable?
- The **object's structure** (what fields it has) should remain stable.
- The **validation rules** (required vs optional fields) should remain stable.

### Who Owns This Responsibility?
- The **class being built** should own the construction logic, not the client code.

### Where is My Code Tightly Coupled?
- In [`Main.java`](src/airline_management_system/Main.java:27), the client must know all parameters and their order to create a `Flight`.
- In [`Aircraft.java`](src/airline_management_system/entity/flights/air_craft/Aircraft.java:21), two constructors with overlapping parameters create confusion.

### Implementation Steps for Airline Management System

#### Step 1: Builder for `Aircraft` (Already Implemented)
**Current State**: [`Aircraft`](src/airline_management_system/entity/flights/air_craft/Aircraft.java:12) has a `private` constructor that takes `AircraftBuilder`. The builder is a static nested class with fluent setters.

**What's Working**:
- `AircraftBuilder` is `static` — can be accessed as `new Aircraft.AircraftBuilder(...)`
- Constructor is `protected` — allows `Flight` (subclass in different package) to call `super(builder)`
- Builder has fluent setters for optional fields
- `build()` method creates the `Aircraft` instance

**What's Pending**:
- None — this is fully implemented

#### Step 2: Builder for `Flight` (Already Implemented)
**Current State**: [`Flight`](src/airline_management_system/entity/flights/Flight.java:14) extends `Aircraft` and has a `FlightBuilder` that creates an `AircraftBuilder` internally and passes it to `super()`.

**What's Working**:
- `FlightBuilder` collects all flight-specific fields
- In `build()`, it creates an `Aircraft.AircraftBuilder` from its fields
- Passes the `AircraftBuilder` to `super()` — this is the correct pattern for inheritance with builders

**What's Pending**:
- None — this is fully implemented

#### Step 3: Builder for `Person` (Already Implemented)
**Current State**: [`Person`](src/airline_management_system/entity/persons/Person.java:9) has a package-private constructor that takes `PersonBuilder`.

**What's Working**:
- `PersonBuilder` is `static`
- Constructor is package-private — allows subclasses (`Passenger`, `AirportStaff`, `Pilot`) in the same package to call `super(builder)`
- `build()` method validates required fields before creating the object

**What's Pending**:
- None — this is fully implemented

#### Step 4: Builder for `Passenger` (Already Implemented)
**Current State**: [`Passenger`](src/airline_management_system/entity/persons/Passenger.java:11) extends `Person` and has a `PassengerBuilder` that extends `Person.PersonBuilder`.

**What's Working**:
- `PassengerBuilder` is `static` and extends `Person.PersonBuilder`
- Inherits all Person fields (`f_Name`, `l_Name`, `passport_id`, etc.)
- Adds `Passenger`-specific field: `seatStatus`
- `build()` calls `super.build()` for validation, then creates `Passenger`

**What's Pending**:
- None — this is fully implemented

#### Step 5: Builder for `AirportStaff` (Already Implemented)
**Current State**: [`AirportStaff`](src/airline_management_system/entity/persons/AirportStaff.java:6) extends `Person` and has a static `AirportStaffBuilder` that extends `Person.PersonBuilder`.

**What's Working**:
- `AirportStaffBuilder` is `static` and extends `Person.PersonBuilder`
- Adds `AirportStaff`-specific field: `staffType`
- `build()` calls `super.build()` for validation, then creates `AirportStaff`

**What's Pending**:
- None — this is fully implemented

#### Step 6: Builder for `Pilot` (Already Implemented)
**Current State**: [`Pilot`](src/airline_management_system/entity/persons/Pilot.java:6) extends `Person` and has a static `PilotBuilder` that extends `Person.PersonBuilder`.

**What's Working**:
- `PilotBuilder` is `static` and extends `Person.PersonBuilder`
- Adds `Pilot`-specific field: `experience`
- `build()` calls `super.build()` for validation, then creates `Pilot`

**What's Pending**:
- None — this is fully implemented

#### Step 7: Builder for `Booking` (Already Implemented)
**Current State**: [`Booking`](src/airline_management_system/entity/bookings/Booking.java:14) has a `BookingBuilder` with validation logic.

**What's Working**:
- `BookingBuilder` validates that flight is not cancelled, payment is successful, etc.
- `build()` throws `IllegalStateException` if validation fails

**What's Pending**:
- None — this is fully implemented

#### Step 8: Builder for `Payment` (Already Implemented)
**Current State**: [`Payment`](src/airline_management_system/entity/payment/Payment.java:11) has a `PaymentBuilder` with validation logic.

**What's Working**:
- `PaymentBuilder` validates payment status and flight status
- `build()` throws `IllegalStateException` if validation fails

**What's Pending**:
- None — this is fully implemented

### Summary: Builder Pattern Status

| Class | Status | Notes |
|-------|--------|-------|
| `Aircraft` | ✅ Implemented | Static builder, protected constructor for subclass access |
| `Flight` | ✅ Implemented | Creates AircraftBuilder internally, passes to super() |
| `Person` | ✅ Implemented | Package-private constructor, static builder |
| `Passenger` | ✅ Implemented | Extends PersonBuilder, adds seatStatus |
| `AirportStaff` | ✅ Implemented | Extends PersonBuilder, adds staffType |
| `Pilot` | ✅ Implemented | Extends PersonBuilder, adds experience |
| `Booking` | ✅ Implemented | Validates business rules before building |
| `Payment` | ✅ Implemented | Validates payment status before building |

**All Builder pattern implementations are complete. No pending work.**

---

## When to Avoid the Builder Pattern

| Scenario | Why to Avoid |
|----------|-------------|
| **Simple objects with ≤ 3 required parameters** | The Builder adds unnecessary complexity. A simple constructor is clearer and more concise. |
| **All parameters are always required** | A Builder doesn't add value when there are no optional parameters. Use a regular constructor instead. |
| **Immutable objects with few fields** | If the object has only 2-3 fields and is always created with all values, a constructor is simpler. |
| **Performance-critical code** | The Builder creates an extra object (the builder itself) before creating the final object. In tight loops, this overhead matters. |
| **Objects that are frequently mutated after creation** | The Builder pattern shines for creating immutable objects. If the object is mutable and frequently changed, the Builder's immutability guarantee is wasted. |
| **When you need inheritance with builders** | Builder pattern with inheritance is complex. If you have a deep class hierarchy, consider whether Builder is the right choice for all levels. |

### Quick Decision Guide

```
Does your class have > 4 constructor parameters?
├── YES → Are some parameters optional?
│   ├── YES → Use Builder ✅
│   └── NO → Consider telescoping constructors or a parameter object
└── NO → Use a simple constructor ✅
```
