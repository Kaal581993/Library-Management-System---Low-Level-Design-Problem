# State Design Pattern

## Intent
Allow an object to **alter its behavior** when its internal state changes. The object will appear to change its class.

## The Core Roles
| Role | Responsibility |
|------|---------------|
| **Context** | The object whose behavior varies based on its state. It delegates state-specific behavior to a State object. |
| **State** (interface) | Declares methods for handling actions that depend on state. |
| **Concrete State** | Implements behavior specific to a particular state. May also handle transitions to other states. |

## How It Works
```
Context (Booking)
    │
    ├── State: PendingBookingState
    │       ├── confirm() → transitions to ConfirmedBookingState
    │       └── cancel() → transitions to CancelledBookingState
    │
    ├── State: ConfirmedBookingState
    │       ├── confirm() → throws exception (already confirmed)
    │       └── cancel() → transitions to CancelledBookingState
    │
    └── State: CancelledBookingState
            ├── confirm() → throws exception (cannot confirm cancelled)
            └── cancel() → throws exception (already cancelled)
```

1. The Context holds a reference to the current State
2. When an action is requested, the Context delegates to the current State
3. The State may change the Context's state (transition)
4. The client interacts with the Context, not the State directly

## Code Structure
```java
// State interface
public interface BookingState {
    void confirm(Booking booking);
    void cancel(Booking booking);
    BookingStatus getStatus();
}

// Concrete State
public class PendingBookingState implements BookingState {
    @Override
    public void confirm(Booking booking) {
        booking.setState(new ConfirmedBookingState());
        booking.setBookingStatus(BookingStatus.CONFIRMED);
    }
    
    @Override
    public void cancel(Booking booking) {
        booking.setState(new CancelledBookingState());
        booking.setBookingStatus(BookingStatus.CANCELLED);
    }
    
    @Override
    public BookingStatus getStatus() {
        return BookingStatus.PENDING;
    }
}

// Context
public class Booking {
    private BookingState state;
    
    public void confirm() {
        state.confirm(this);
    }
    
    public void cancel() {
        state.cancel(this);
    }
    
    public void setState(BookingState state) {
        this.state = state;
    }
}
```

## State vs. Strategy
| | State | Strategy |
|--|-------|----------|
| Who decides transitions | The State itself decides when to transition | The Client picks the Strategy |
| Intent | Manage state-dependent behavior | Swap algorithms |
| Awareness | States often know about other states | Strategies don't know about each other |
| Transitions | States trigger transitions | No transitions between strategies |

## Real-World Uses
- **Vending machines**: Different states (idle, selecting, dispensing, out-of-stock) with different behaviors
- **Order processing**: Pending → Confirmed → Shipped → Delivered
- **Game development**: Character states (idle, walking, running, jumping)
- **Traffic lights**: Red → Green → Yellow → Red
- **Document editing**: Draft → Review → Approved → Published

## Common Pitfalls
- **State explosion**: If there are many states and transitions, the number of State classes can grow large.
- **States sharing too much code**: If states are very similar, consider whether State pattern is the right choice.
- **Transitions scattered across states**: If transition logic is spread across many State classes, it can be hard to understand the overall flow.

## Quick Memory Aid
> "The object changes its behavior by changing its state — like a person acting differently when happy vs. angry."

---

## State Pattern in Airline Management System

### What Problem Does It Solve?
When an object's behavior changes based on its internal state, and you want to avoid large `if-else` or `switch` statements scattered throughout the code.

**Example**: A `Booking` has states: `PENDING` → `CONFIRMED` → `CANCELLED` or `EXPIRED`. Each state has different behavior:
- `PENDING`: can be confirmed or cancelled
- `CONFIRMED`: can be cancelled (with refund)
- `CANCELLED`: no further actions allowed

### What is Changing? What Varies?
- **The state** of the object varies, and each state has different allowed transitions and behaviors.

### What Should Remain Stable?
- The **state interface** should remain stable.
- The **context** (Booking) should not change when new states are added.

### Who Owns This Responsibility?
- Each **State** owns its specific behavior and transition rules.
- The **Context** delegates to the current state.

### Where is My Code Tightly Coupled?
- Currently, `BookingStatus` is an enum, and state transitions are handled with `if-else` or direct assignment in `setBookingStatus()`.

### Implementation Steps for Airline Management System

#### Step 1: `BookingState` Pattern

**Current State**: `Booking` uses `BookingStatus` enum and `setBookingStatus()` directly.

**What's Pending**:
1. Create `BookingState` interface with methods: `confirm()`, `cancel()`, `expire()`, `getStatus()`
2. Create `PendingBookingState`, `ConfirmedBookingState`, `CancelledBookingState`, `ExpiredBookingState`
3. Each state implements allowed transitions (e.g., `CancelledBookingState.confirm()` throws exception)
4. Refactor `Booking` to delegate state behavior to the current `BookingState`

**Example Implementation**:
```java
public interface BookingState {
    void confirm(Booking booking);
    void cancel(Booking booking);
    void expire(Booking booking);
    BookingStatus getStatus();
}

public class PendingBookingState implements BookingState {
    @Override
    public void confirm(Booking booking) {
        booking.setState(new ConfirmedBookingState());
        booking.setBookingStatus(BookingStatus.CONFIRMED);
    }
    
    @Override
    public void cancel(Booking booking) {
        booking.setState(new CancelledBookingState());
        booking.setBookingStatus(BookingStatus.CANCELLED);
    }
    
    @Override
    public void expire(Booking booking) {
        booking.setState(new ExpiredBookingState());
        booking.setBookingStatus(BookingStatus.EXPIRED);
    }
    
    @Override
    public BookingStatus getStatus() {
        return BookingStatus.PENDING;
    }
}
```

#### Step 2: `FlightState` Pattern

**What's Pending**:
1. Create `FlightState` interface with methods: `delay()`, `cancel()`, `resume()`, `getStatus()`
2. Create `OnTimeFlightState`, `DelayedFlightState`, `CancelledFlightState`
3. Refactor `Flight` to use state pattern instead of enum + `if-else`

**Example Implementation**:
```java
public interface FlightState {
    void delay(Flight flight);
    void cancel(Flight flight);
    void resume(Flight flight);
    Flight_Status getStatus();
}

public class OnTimeFlightState implements FlightState {
    @Override
    public void delay(Flight flight) {
        flight.setState(new DelayedFlightState());
        flight.setFlightStatus(Flight_Status.DELAYED);
    }
    
    @Override
    public void cancel(Flight flight) {
        flight.setState(new CancelledFlightState());
        flight.setFlightStatus(Flight_Status.CANCELLED);
    }
    
    @Override
    public void resume(Flight flight) {
        // Already on time, no action needed
    }
    
    @Override
    public Flight_Status getStatus() {
        return Flight_Status.ON_TIME;
    }
}
```

### Summary: State Pattern Status

| Component | Status | Notes |
|-----------|--------|-------|
| `BookingState` interface | ❌ Not Created | Replaces enum-based state transitions in `Booking` |
| `PendingBookingState` | ❌ Not Created | |
| `ConfirmedBookingState` | ❌ Not Created | |
| `CancelledBookingState` | ❌ Not Created | |
| `ExpiredBookingState` | ❌ Not Created | |
| `FlightState` interface | ❌ Not Created | Replaces enum-based status in `Flight` |
| `OnTimeFlightState` | ❌ Not Created | |
| `DelayedFlightState` | ❌ Not Created | |
| `CancelledFlightState` | ❌ Not Created | |

**Pending Work**:
1. Implement `BookingState` pattern to replace enum-based state transitions
2. Implement `FlightState` pattern to replace enum-based flight status

---

## When to Avoid the State Pattern

| Scenario | Why to Avoid |
|----------|-------------|
| **There are only 2-3 states with simple transitions** | A simple `enum` + `switch` is more readable and maintainable. State pattern adds unnecessary complexity. |
| **States don't have significantly different behavior** | If all states behave almost the same, the State pattern's benefit is minimal. |
| **State transitions are rare** | If state changes are infrequent, the overhead of maintaining State objects isn't justified. |
| **The state machine is simple and unlikely to change** | If the state transitions are fixed and stable, a simple enum or constants are sufficient. |
| **You need to persist state as a simple value** | If the state is stored as a database column (e.g., `VARCHAR`), using State objects adds complexity to persistence. |
| **Performance is critical** | The State pattern adds an extra level of indirection. In hot paths, this can matter. |
| **States are tightly coupled to the context** | If states need to access many internal details of the context, they become tightly coupled, defeating the pattern's purpose. |

### Quick Decision Guide

```
Does the object have > 3 states with different behavior per state?
├── NO → Use enum + switch ✅
└── YES → Do states have complex transitions and behaviors?
    ├── NO → Use enum with methods ✅
    └── YES → Use State pattern ✅