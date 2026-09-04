# Decorator Pattern

## Intent
Attach **additional responsibilities** to an object **dynamically**, without modifying the class. A flexible alternative to subclassing.

## The Core Roles
| Role | Responsibility |
|------|---------------|
| **Component** (interface) | Defines the base operation |
| **Concrete Component** | The base object being decorated |
| **Decorator** (abstract) | Wraps a Component, delegates to it |
| **Concrete Decorator** | Adds behavior before/after delegating |

## How It Works
```
Client sees → Coffee interface
               ↑
WhipCreamDecorator (wraps →)
    SugarDecorator (wraps →)
        MilkDecorator (wraps →)
            SimpleCoffee        ← the actual base object
```
Each decorator:
1. Holds a reference to the **wrapped** component
2. Implements the **same interface**
3. **Delegates** to wrapped + adds its own behavior

## Code Structure
```java
interface Coffee {
    String getDescription();
    double getCost();
}

class SimpleCoffee implements Coffee { ... }

abstract class CoffeeDecorator implements Coffee {
    protected final Coffee wrapped;
    CoffeeDecorator(Coffee c) { this.wrapped = c; }
}

class MilkDecorator extends CoffeeDecorator {
    String getDescription() { return wrapped.getDescription() + " + milk"; }
    double getCost()        { return wrapped.getCost() + 0.50; }
}

// Usage: stack them!
Coffee order = new WhipCreamDecorator(
                   new SugarDecorator(
                       new MilkDecorator(
                           new SimpleCoffee())));
```

## Decorator vs. Subclassing
| | Subclassing | Decorator |
|--|-------------|-----------|
| When | Compile-time | Runtime |
| Combinations | 2^N classes | N decorator classes |
| Flexibility | Fixed per class | Mix and match per object |

## Real-World Uses
- **Java I/O Streams**: `new BufferedReader(new InputStreamReader(new FileInputStream("f.txt")))`
- **Spring**: `@Transactional`, `@Cacheable` (AOP decorates methods)
- **Collections**: `Collections.unmodifiableList()`, `Collections.synchronizedList()`
- **Logging/Metrics**: Wrap service calls with timing/logging decorators

## Common Pitfalls
- **Too many small classes**: Each decorator is a new class. Can be hard to navigate.
- **Order matters**: `MilkDecorator(SugarDecorator(coffee))` ≠ `SugarDecorator(MilkDecorator(coffee))` if order affects behavior.
- **Identity**: `decoratedObj != originalObj` — they're different objects. `instanceof` checks break.

## Quick Memory Aid
> "Wrap it like a gift — each layer adds something, but it's still a gift."

---

## Decorator Pattern in Airline Management System

### What Problem Does It Solve?
When you need to **add responsibilities to objects dynamically** without affecting other objects of the same class. Inheritance is static — you can't add/remove features at runtime.

**Example**: A `Ticket` can have:
- Base ticket (flight only)
- + Meal
- + Extra Baggage
- + Insurance
- + Lounge Access

With inheritance, you'd need `BaseTicket`, `MealTicket`, `MealBaggageTicket`, `MealBaggageInsuranceTicket` — a class explosion.

### What is Changing? What Varies?
- **The features/add-ons** attached to an object vary per customer.
- **The combination** of features varies.

### What Should Remain Stable?
- The **core interface** (`Ticket`, `Seat`, `PriceCalculator`) should remain stable.
- The **base object** should remain stable.

### Who Owns This Responsibility?
- The **Decorator** wraps the component and adds behavior.
- The **Client** decides which decorators to apply.

### Where is My Code Tightly Coupled?
- Currently, [`Seat.java`](src/airline_management_system/decorator/Seat.java:3) is just an empty interface. There's no decorator implementation.
- [`PriceCalculator.java`](src/airline_management_system/decorator/PriceCalculator.java:3) is also empty.

### Implementation Steps for Airline Management System

#### Step 1: `PriceCalculator` Decorator (Not Implemented)
**Current State**: [`PriceCalculator.java`](src/airline_management_system/decorator/PriceCalculator.java:3) is an empty interface.

**What's Pending**:
- Create `PriceCalculator` interface with `calculatePrice()` and `getDescription()` methods
- Create `BasePriceCalculator` (concrete component)
- Create `PriceDecorator` (abstract decorator)
- Create concrete decorators: `TaxDecorator`, `DiscountDecorator`, `PremiumSeatDecorator`

**Example Implementation**:
```java
// Component interface
public interface PriceCalculator {
    double calculatePrice();
    String getDescription();
}

// Concrete component
public class BasePriceCalculator implements PriceCalculator {
    private double basePrice;
    public BasePriceCalculator(double basePrice) { this.basePrice = basePrice; }
    public double calculatePrice() { return basePrice; }
    public String getDescription() { return "Base Price: $" + basePrice; }
}

// Decorator base
public abstract class PriceDecorator implements PriceCalculator {
    protected PriceCalculator wrappedCalculator;
    public PriceDecorator(PriceCalculator wrappedCalculator) { this.wrappedCalculator = wrappedCalculator; }
}

// Concrete decorator
public class TaxDecorator extends PriceDecorator {
    private double taxRate;
    public TaxDecorator(PriceCalculator calculator, double taxRate) {
        super(calculator); this.taxRate = taxRate;
    }
    public double calculatePrice() { return wrappedCalculator.calculatePrice() * (1 + taxRate); }
    public String getDescription() { return wrappedCalculator.getDescription() + " + Tax"; }
}
```

#### Step 2: `Seat` Decorator (Not Implemented)
**Current State**: [`Seat.java`](src/airline_management_system/decorator/Seat.java:3) is an empty interface.

**What's Pending**:
- Create `Seat` interface with `getPrice()` and `getFeatures()` methods
- Create `BaseSeat` (concrete component)
- Create `SeatDecorator` (abstract decorator)
- Create concrete decorators: `ExtraLegroomDecorator`, `MealIncludedDecorator`, `PriorityBoardingDecorator`

#### Step 3: `Ticket` Decorator (Not Implemented)
**Current State**: [`Ticket.java`](src/airline_management_system/decorator/Ticket.java:3) is an empty interface.

**What's Pending**:
- Create `Ticket` interface
- Create `BaseTicket` (concrete component)
- Create `TicketDecorator` (abstract decorator)
- Create concrete decorators: `InsuranceDecorator`, `LoungeAccessDecorator`, `ExtraBaggageDecorator`

#### Step 4: `BookingNotification` Decorator (Not Implemented)
**Current State**: [`BookingNotification.java`](src/airline_management_system/decorator/BookingNotification.java:3) is an empty interface.

**What's Pending**:
- Create `BookingNotification` interface
- Create decorators for different notification channels: `EmailDecorator`, `SMSDecorator`, `PushNotificationDecorator`

### Summary: Decorator Pattern Status

| Component | Status | Notes |
|-----------|--------|-------|
| `PriceCalculator` interface | ⚠️ Defined (empty) | Needs methods and implementations |
| `Seat` interface | ⚠️ Defined (empty) | Needs methods and implementations |
| `Ticket` interface | ⚠️ Defined (empty) | Needs methods and implementations |
| `BookingNotification` interface | ⚠️ Defined (empty) | Needs methods and implementations |
| Concrete decorators | ❌ Not Implemented | None exist yet |

**Pending Work**:
1. Implement `PriceCalculator` decorator hierarchy
2. Implement `Seat` decorator hierarchy
3. Implement `Ticket` decorator hierarchy
4. Implement `BookingNotification` decorator hierarchy

---

## When to Avoid the Decorator Pattern

| Scenario | Why to Avoid |
|----------|-------------|
| **The number of combinations is small and fixed** | If you only have 2-3 fixed combinations, inheritance is simpler. Decorator shines when combinations are numerous and dynamic. |
| **Order of decoration doesn't matter** | If decorators are independent and order doesn't affect behavior, you might be over-engineering. |
| **You need to identify the "real" type of the object** | `decoratedObj instanceof BaseTicket` returns `false` because it's a decorator wrapper. This breaks type checking. |
| **Identity matters** | `decoratedObj != originalObj` — they're different objects. If you need object identity (e.g., in collections as keys), Decorator breaks this. |
| **Performance is critical** | Each decorator adds another layer of indirection. In hot paths with many decorators, this stack of wrappers can impact performance. |
| **The component interface is unstable** | If the base interface changes frequently, every decorator must be updated. This creates maintenance burden. |
| **You're just adding one optional feature** | If there's only one optional feature, a simple boolean flag or subclass is clearer than a full Decorator hierarchy. |

### Quick Decision Guide

```
Do you need to add features dynamically at runtime?
├── NO → Use inheritance or simple flags ✅
└── YES → Are there many possible feature combinations?
    ├── NO (≤ 3 fixed combos) → Use subclasses ✅
    └── YES → Use Decorator ✅
```
