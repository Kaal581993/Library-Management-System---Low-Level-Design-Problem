# State Pattern

## Intent
Allow an object to **alter its behavior when its internal state changes**. The object will appear to change its class.

## The Core Roles
| Role | Responsibility |
|------|---------------|
| **State** (interface) | Declares operations that differ by state |
| **Concrete State** | Implements behavior for one state; triggers transitions via `context.setState(...)` |
| **Context** | Holds current State; delegates all requests to it |

## How It Works
```
Client calls: doc.submit()
                  ↓
           Document (Context)
                  ↓  delegates to
           currentState.submit(this)
                  ↓
       [DraftState / ReviewState / PublishedState]
       each handles it differently AND can switch the state
```

## State Transition Diagram
```
        submit()         approve()
  [Draft] ────────→ [Review] ────────→ [Published]
```

## Code Structure
```java
interface DocumentState {
    void edit(Document doc);
    void submit(Document doc);
    void approve(Document doc);
}

class Document {                      // Context
    private DocumentState state = new DraftState();
    void setState(DocumentState s) { state = s; }
    void submit() { state.submit(this); }   // delegates
}

class DraftState implements DocumentState {
    public void submit(Document doc) {
        System.out.println("Submitted for review.");
        doc.setState(new ReviewState());     // transition!
    }
    public void approve(Document doc) {
        System.out.println("Can't approve — not submitted yet.");
    }
}
```

## State vs. Strategy
| | State | Strategy |
|--|-------|----------|
| **Who switches?** | State switches itself (via Context) | Client switches explicitly |
| **States know each other?** | Yes — transitions reference next state | No — strategies are independent |
| **Purpose** | Model lifecycle / finite state machine | Swap algorithm at runtime |

## Real-World Uses
- **Order lifecycle**: PLACED → CONFIRMED → SHIPPED → DELIVERED
- **TCP connection**: LISTEN → ESTABLISHED → CLOSED
- **Game character**: IDLE → RUNNING → JUMPING → DEAD

## Quick Memory Aid
> "The context is the machine. Each state is a mode. Switching the mode changes everything the machine does."

---

## State Pattern in Airline Management System

### What Problem Does It Solve?
When an object's **behavior changes based on its internal state**, and you have many `if-else` or `switch` statements checking the state. This leads to:
- **Rigid code**: Adding a new state requires modifying all existing state checks
- **Error-prone**: Forgetting to handle a state transition
- **Scattered logic**: State-specific code is spread across multiple methods

**Example**: `Flight` status transitions:
- `ON_TIME` → can go to `DELAYED` or `CANCELLED`
- `DELAYED` → can go to `ON_TIME` or `CANCELLED`
- `CANCELLED` → cannot go to any other state (terminal state)

Currently, [`Flight_Status`](src/airline_management_system/entity/flights/Flight_Status.java:3) is just an enum. The transition logic is implicit and scattered.

### What is Changing? What Varies?
- **The state** of the object changes over time.
- **The behavior** associated with each state varies.

### What Should Remain Stable?
- The **state transition rules** should remain stable and enforceable.
- The **interface** of the state should remain stable.

### Who Owns This Responsibility?
- Each **State object** owns its own behavior and valid transitions.
- The **Context** (e.g., `Flight`) delegates to the current state.

### Where is My Code Tightly Coupled?
- In [`Flight.java`](src/airline_management_system/entity/flights/Flight.java:231), `setFlightStatus()` directly sets the enum value without validating transitions.
- The `calculateTax()` method (line 148) uses `if-else` on `Seat_Type` — this could be a State pattern too.

### Implementation Steps for Airline Management System

#### Step 1: State Pattern for `Flight_Status` (Not Implemented)
**Current State**: `Flight_Status` is an enum with values `ON_TIME`, `DELAYED`, `CANCELLED`.

**What's Pending**:
- Create `FlightState` interface with methods: `onTime()`, `delay()`, `cancel()`
- Create concrete states: `OnTimeState`, `DelayedState`, `CancelledState`
- Create `FlightContext` class that wraps `Flight` and delegates to current state
- Each state defines valid transitions (e.g., `CancelledState` cannot transition to `OnTimeState`)

**Example Implementation**:
```java
// State interface
public interface FlightState {
    void onTime(FlightContext context);
    void delay(FlightContext context);
    void cancel(FlightContext context);
    Flight_Status getStatus();
}

// Concrete state
public class OnTimeState implements FlightState {
    @Override
    public void delay(FlightContext context) {
        System.out.println("Flight delayed");
        context.setState(new DelayedState());
    }
    @Override
    public void cancel(FlightContext context) {
        System.out.println("Flight cancelled");
        context.setState(new CancelledState());
    }
    @Override
    public Flight_Status getStatus() { return Flight_Status.ON_TIME; }
}

// Context
public class FlightContext {
    private FlightState currentState;
    private Flight flight;
    public FlightContext(Flight flight) {
        this.flight = flight;
        this.currentState = new OnTimeState();
    }
    public void setState(FlightState newState) { this.currentState = newState; }
    public void delay() { currentState.delay(this); }
    public void cancel() { currentState.cancel(this); }
}
```

#### Step 2: State Pattern for `BookingStatus` (Not Implemented)
**Current State**: `BookingStatus` is an enum with values `CONFIRMED`, `CANCELLED`, `PENDING`, `EXPIRED`.

**What's Pending**:
- Create `BookingState` interface with methods: `confirm()`, `cancel()`, `expire()`
- Create concrete states: `PendingState`, `ConfirmedState`, `CancelledState`, `ExpiredState`
- Enforce valid transitions (e.g., `Pending` → `Confirmed` or `Cancelled`, but not `Pending` → `Expired` directly)

#### Step 3: State Pattern for `PaymentStatus` (Not Implemented)
**Current State**: `PaymentStatus` is an enum with values `SUCCESSFUL`, `PENDING`, `FAILED`.

**What's Pending**:
- Create `PaymentState` interface with methods: `process()`, `succeed()`, `fail()`
- Create concrete states: `PendingState`, `SuccessfulState`, `FailedState`
- Model the payment lifecycle with proper transitions

### Summary: State Pattern Status

| Component | Status | Notes |
|-----------|--------|-------|
| `Flight_Status` enum | ✅ Defined | State pattern not yet implemented |
| `BookingStatus` enum | ✅ Defined | State pattern not yet implemented |
| `PaymentStatus` enum | ✅ Defined | State pattern not yet implemented |
| `FlightState` interface | ❌ Not Created | |
| `BookingState` interface | ❌ Not Created | |
| `PaymentState` interface | ❌ Not Created | |
| Concrete state classes | ❌ Not Created | |
| Context classes | ❌ Not Created | |

**Pending Work**:
1. Implement State pattern for `Flight_Status`
2. Implement State pattern for `BookingStatus`
3. Implement State pattern for `PaymentStatus`
