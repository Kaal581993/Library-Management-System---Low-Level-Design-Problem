# Observer Design Pattern

## Intent
Define a **one-to-many dependency** between objects so that when one object changes state, all its dependents are notified and updated automatically.

## The Core Roles
| Role | Responsibility |
|------|---------------|
| **Subject** (Observable) | Maintains a list of observers, provides methods to add/remove observers, and notifies them of state changes |
| **Observer** (interface) | Defines the update method that gets called when the subject changes |
| **Concrete Observer** | Implements the Observer interface, defines the specific reaction to the subject's change |

## How It Works
```
Subject (Flight)
    │
    ├── Observer 1: EmailNotifier
    ├── Observer 2: SMSNotifier
    ├── Observer 3: RefundProcessor
    └── Observer 4: SeatMapUpdater

When Flight status changes:
    → notifyObservers() is called
    → Each observer's onFlightStatusChanged() is called
    → Each observer reacts independently
```

1. Observers **register** themselves with the Subject
2. When the Subject's state changes, it calls `notifyObservers()`
3. Each Observer's `update()` method is called with the new state
4. Observers react independently — the Subject doesn't know what they do

## Code Structure
```java
// Observer interface
public interface FlightStatusObserver {
    void onFlightStatusChanged(Flight flight, Flight_Status oldStatus, Flight_Status newStatus);
}

// Subject
public class Flight {
    private List<FlightStatusObserver> observers = new ArrayList<>();
    
    public void addObserver(FlightStatusObserver observer) {
        observers.add(observer);
    }
    
    public void removeObserver(FlightStatusObserver observer) {
        observers.remove(observer);
    }
    
    public void setFlightStatus(Flight_Status newStatus) {
        Flight_Status old = this.flightStatus;
        this.flightStatus = newStatus;
        notifyObservers(old, newStatus);
    }
    
    private void notifyObservers(Flight_Status oldStatus, Flight_Status newStatus) {
        for (FlightStatusObserver observer : observers) {
            observer.onFlightStatusChanged(this, oldStatus, newStatus);
        }
    }
}

// Concrete Observer
public class EmailNotifier implements FlightStatusObserver {
    @Override
    public void onFlightStatusChanged(Flight flight, Flight_Status oldStatus, Flight_Status newStatus) {
        // Send email to passenger
        System.out.println("Email sent: Flight " + flight.getFlightId() + 
            " status changed from " + oldStatus + " to " + newStatus);
    }
}
```

## Real-World Uses
- **Event handling in GUI**: Button click → multiple listeners notified
- **Java's `PropertyChangeListener`**: Used in Swing for data binding
- **Message queues**: Kafka, RabbitMQ — producers publish, consumers observe
- **Social media feeds**: When a user posts, all followers are notified
- **Stock market apps**: When stock price changes, all watching users are notified

## Observer vs. Publish-Subscribe
| | Observer | Pub-Sub |
|--|----------|---------|
| Coupling | Tight (Subject knows Observers) | Loose (via message broker) |
| Communication | Direct method calls | Via event bus/queue |
| Scale | Single process | Distributed systems |

## Common Pitfalls
- **Memory leaks**: Observers that are never removed can cause memory leaks. Always provide `removeObserver()`.
- **Order of notification**: The order in which observers are notified can matter. Document the expected order.
- **Cascading notifications**: An observer's update might trigger another notification, causing infinite loops. Be careful.
- **Thread safety**: If the Subject is accessed by multiple threads, the observer list must be thread-safe.

## Quick Memory Aid
> "Subscribe to updates — when something changes, everyone who cares gets notified."

---

## Observer Pattern in Airline Management System

### What Problem Does It Solve?
When a change in one object (the **Subject**) needs to notify **multiple other objects** (Observers) without the Subject knowing who those observers are. This decouples the Subject from its dependents.

**Example**: When a flight is cancelled:
- Passengers need to be notified (email/SMS)
- Booking status needs to update
- Payment refund needs to trigger
- Seat availability needs to update

Without Observer, `Flight` would need direct references to `EmailService`, `SMSNotifier`, `RefundProcessor`, etc. — tight coupling.

### What is Changing? What Varies?
- **The number and type of observers** varies. New notification channels can be added without changing the Subject.

### What Should Remain Stable?
- The **Subject's core logic** should not change when observers are added/removed.
- The **Observer interface** should remain stable.

### Who Owns This Responsibility?
- The **Subject** (Flight, Booking, Payment, Aircraft) owns the observer list and notification logic.
- Each **Observer** implements the interface and reacts to changes.

### Where is My Code Tightly Coupled?
- Currently, `addObserver()` and `removeObserver()` methods are **empty** in all entities. The observer lists exist but are never populated or used.

### Implementation Steps for Airline Management System

#### Step 1: Wire up `addObserver()` / `removeObserver()` in entities

**Current State**: These methods are empty stubs in `Flight`, `Booking`, `Payment`, and `Aircraft`.

**What's Pending**:
```java
// In Flight.java:
public void addObserver(FlightStatusObserver observer) {
    observers.add(observer);  // FILL THIS IN
}
public void removeObserver(FlightStatusObserver observer) {
    observers.remove(observer);  // FILL THIS IN
}
```

#### Step 2: Create concrete observers with actual logic

**What's Pending**:
- `EmailNotifier` — sends email when flight status changes
- `SMSNotifier` — sends SMS when booking status changes
- `RefundProcessor` — triggers refund when payment fails
- `SeatMapUpdater` — updates seat map when seat status changes

**Example Implementation**:
```java
public class EmailNotifier implements FlightStatusObserver {
    @Override
    public void onFlightStatusChanged(Flight flight, Flight_Status oldStatus, Flight_Status newStatus) {
        // Actual email sending logic
        System.out.println("Email sent to passenger: Flight " + 
            flight.getFlightId() + " is now " + newStatus);
    }
}
```

#### Step 3: Register observers in client code

**What's Pending**:
```java
Flight flight = ...;
flight.addObserver(new EmailNotifier());
flight.addObserver(new SMSNotifier());

// When status changes, all observers are notified automatically
flight.setFlightStatus(Flight_Status.DELAYED);
```

### Summary: Observer Pattern Status

| Component | Status | Notes |
|-----------|--------|-------|
| `BookingStatusObserver` interface | ✅ Defined | |
| `FlightStatusObserver` interface | ✅ Defined | |
| `PaymentStatusUpdater` interface | ✅ Defined | |
| `SeatStatusUpdater` interface | ✅ Defined | |
| `BookingNotificationObServer` | ⚠️ Empty | Needs actual notification logic |
| `PassengerNotificationObserver` | ⚠️ Empty | Needs actual notification logic |
| `PaymentNotifierObserver` | ⚠️ Empty | Needs actual notification logic |
| `addObserver()` / `removeObserver()` | ⚠️ Empty | Methods are stubs, need implementation |

**Pending Work**:
1. Wire up `addObserver()` / `removeObserver()` in all entities
2. Create concrete observers with actual notification logic
3. Register observers in client code

---

## When to Avoid the Observer Pattern

| Scenario | Why to Avoid |
|----------|-------------|
| **There's only one observer** | If only one object cares about the change, a direct method call is simpler. Observer adds unnecessary indirection. |
| **The Subject and Observer are tightly coupled by design** | If they're in the same module and always change together, Observer's decoupling benefit is wasted. |
| **Notifications are rare and unpredictable** | If state changes are infrequent, the overhead of maintaining observer lists isn't justified. |
| **You need guaranteed delivery** | Observer pattern doesn't guarantee delivery. If an observer throws an exception, other observers may not be notified. Use a message queue for guaranteed delivery. |
| **Order of notification matters critically** | If observers must be notified in a specific order, the simple list-based approach may not suffice. |
| **Memory leaks are a concern** | If observers are not properly removed, they can cause memory leaks. In long-running applications, this is a real risk. |
| **The subject has no meaningful state changes** | If the object doesn't have events that other objects care about, Observer is unnecessary. |

### Quick Decision Guide

```
Does a change in one object affect multiple other objects?
├── NO → Use direct method calls ✅
└── YES → Are the affected objects known at compile time?
    ├── YES → Consider direct calls or callbacks ✅
    └── NO → Use Observer ✅