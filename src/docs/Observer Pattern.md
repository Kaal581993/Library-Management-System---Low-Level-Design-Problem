# Observer Pattern

## Intent
Define a **one-to-many** dependency so that when one object changes state, all dependents are **notified automatically**.

## The Core Roles
| Role | Responsibility |
|------|---------------|
| **Subject** (Observable) | Maintains a list of observers, notifies on state change |
| **Observer** (interface) | Declares `update()` — how to react to changes |
| **Concrete Observer** | Implements specific reaction logic |

## How It Works
```
Subject ──notify()──► Observer A
                  ──► Observer B
                  ──► Observer C
```
1. Observers **register** with the Subject (`addObserver`)
2. Subject's state changes
3. Subject calls `notifyObservers()` → loops through list, calls `update()` on each
4. Observers can **unregister** at any time (`removeObserver`)

## Code Structure
```java
// Subject
class Stock {
    List<Observer> observers = new ArrayList<>();
    void addObserver(Observer o)    { observers.add(o); }
    void removeObserver(Observer o) { observers.remove(o); }
    void setPrice(double p) {
        this.price = p;
        for (Observer o : observers) o.update(name, price);
    }
}

// Observer
interface Observer {
    void update(String name, double price);
}
```

## Real-World Uses
- **Event listeners** in GUI frameworks (button click → handler)
- **Pub/Sub** messaging systems (Kafka consumers)
- **MVC architecture** (Model changes → View updates)
- **Java's built-in**: `PropertyChangeListener`, `java.util.Observable` (deprecated)
- **Spring**: `ApplicationEvent` + `@EventListener`

## Observer vs. Polling
| Approach | How | Problem |
|----------|-----|---------|
| Polling | Observer keeps checking "did it change?" | Wasteful, tight coupling |
| Observer | Subject pushes changes to observers | Efficient, loose coupling |

## Common Pitfalls
- **Memory leaks**: Forgetting to unregister observers (they stay in the list forever)
- **Order dependency**: Don't assume observers are notified in a specific order
- **Cascading updates**: Observer A's reaction triggers Subject B, which notifies Observer C... can cause infinite loops
- **Thread safety**: In multi-threaded apps, the observer list needs synchronization

## Quick Memory Aid
> "Don't call us, we'll call you." — The Subject notifies Observers, not the other way around.

---

## Observer Pattern in Airline Management System

### What Problem Does It Solve?
When a change in one object (the **Subject**) requires **multiple other objects (Observers)** to be notified, but the Subject shouldn't know about the specific observers. This avoids tight coupling.

**Example**: When a `Flight` status changes from `ON_TIME` to `CANCELLED`:
- Passengers need to be notified
- Booking system needs to update
- Refund system needs to trigger
- Crew needs to be reassigned

If `Flight` directly calls all these systems, it's tightly coupled to them.

### What is Changing? What Varies?
- **The number and type of observers** varies. New notification channels (SMS, push notification) may be added.
- **The reaction to state changes** varies per observer.

### What Should Remain Stable?
- The **Subject's core logic** (flight scheduling) should remain stable.
- The **notification mechanism** (register, notify, remove) should remain stable.

### Who Owns This Responsibility?
- The **Subject** owns the observer list and notification logic.
- Each **Observer** owns its own reaction to changes.

### Where is My Code Tightly Coupled?
- In [`Flight.java`](src/airline_management_system/entity/flights/Flight.java:217), `addObserver()` and `removeObserver()` are **empty** — observers are never actually registered!
- The same issue exists in [`Aircraft`](src/airline_management_system/entity/flights/air_craft/Aircraft.java:89), [`Booking`](src/airline_management_system/entity/bookings/Booking.java:85), and [`Payment`](src/airline_management_system/entity/payment/Payment.java:83).

### Implementation Steps for Airline Management System

#### Step 1: Observer Interfaces (Already Defined)
**Current State**: Four observer interfaces are defined:
- [`FlightStatusObserver`](src/airline_management_system/observer/FlightStatusObserver.java:6)
- [`BookingStatusObserver`](src/airline_management_system/observer/BookingStatusObserver.java:6)
- [`SeatStatusUpdater`](src/airline_management_system/observer/SeatStatusUpdater.java:6)
- [`PaymentStatusUpdater`](src/airline_management_system/observer/PaymentStatusUpdater.java:6)

**What's Working**:
- Each interface defines the callback method for its domain

**What's Pending**:
- None — interfaces are defined

#### Step 2: Concrete Observers (Partially Implemented)
**Current State**: Three concrete observers exist but have empty implementations:
- [`BookingNotificationObServer`](src/airline_management_system/entity/bookings/BookingNotificationObServer.java:6)
- [`PaymentNotifierObserver`](src/airline_management_system/entity/payment/PaymentNotifierObserver.java:5)
- [`PassengerNotificationObserver`](src/airline_management_system/entity/persons/PassengerNotificationObserver.java:10)

**What's Pending**:
- Implement the `onBookingStatusChanged()`, `onPaymentStatusChanged()`, and `onSeatOccupancyStatusChanged()` methods with actual notification logic

#### Step 3: Observer Registration in Subjects (Not Implemented)
**Current State**: `addObserver()` and `removeObserver()` are empty in:
- [`Flight`](src/airline_management_system/entity/flights/Flight.java:217)
- [`Aircraft`](src/airline_management_system/entity/flights/air_craft/Aircraft.java:89)
- [`Booking`](src/airline_management_system/entity/bookings/Booking.java:85)
- [`Payment`](src/airline_management_system/entity/payment/Payment.java:83)

**What's Pending**:
- Implement `addObserver()` to add observers to the list
- Implement `removeObserver()` to remove observers from the list
- Ensure `notifyObservers()` iterates over the actual list

**Example Implementation for Flight**:
```java
private List<FlightStatusObserver> observers = new ArrayList<>();

public void addObserver(FlightStatusObserver observer) {
    if (observer != null && !observers.contains(observer)) {
        observers.add(observer);
    }
}

public void removeObserver(FlightStatusObserver observer) {
    observers.remove(observer);
}
```

#### Step 4: Register Observers in Client Code (Not Implemented)
**What's Pending**:
- Create concrete observers with real logic (e.g., `EmailNotificationObserver`, `SMSNotificationObserver`, `RefundProcessorObserver`)
- Register them with the subjects in the application startup or when events occur

### Summary: Observer Pattern Status

| Component | Status | Notes |
|-----------|--------|-------|
| `FlightStatusObserver` interface | ✅ Defined | |
| `BookingStatusObserver` interface | ✅ Defined | |
| `SeatStatusUpdater` interface | ✅ Defined | |
| `PaymentStatusUpdater` interface | ✅ Defined | |
| `BookingNotificationObServer` | ⚠️ Partial | Empty implementation |
| `PaymentNotifierObserver` | ⚠️ Partial | Empty implementation |
| `PassengerNotificationObserver` | ⚠️ Partial | Empty implementation |
| `Flight.addObserver/removeObserver` | ❌ Not Implemented | Methods are empty |
| `Aircraft.addObserver/removeObserver` | ❌ Not Implemented | Methods are empty |
| `Booking.addObserver/removeObserver` | ❌ Not Implemented | Methods are empty |
| `Payment.addObserver/removeObserver` | ❌ Not Implemented | Methods are empty |

**Pending Work**:
1. Implement `addObserver()` and `removeObserver()` in `Flight`, `Aircraft`, `Booking`, and `Payment`
2. Implement concrete observers with actual notification logic
3. Register observers in client code
