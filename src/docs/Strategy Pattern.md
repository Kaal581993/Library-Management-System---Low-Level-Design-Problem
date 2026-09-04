# Strategy Pattern

## Intent
Define a **family of algorithms**, encapsulate each one, and make them **interchangeable** at runtime.

## The Core Roles
| Role | Responsibility |
|------|---------------|
| **Strategy** (interface) | Declares the algorithm method signature |
| **Concrete Strategy** | Implements a specific algorithm |
| **Context** | Holds a Strategy reference, delegates work to it |

## How It Works
```
Context ──delegates──► Strategy (interface)
                           ├── ConcreteStrategyA
                           ├── ConcreteStrategyB
                           └── ConcreteStrategyC
```
1. Context has a `setStrategy()` method
2. Client picks a strategy and injects it into the Context
3. Context calls `strategy.execute()` — doesn't know which concrete class it is

## Code Structure
```java
interface PaymentStrategy {
    void pay(double amount);
}

class CreditCardPayment implements PaymentStrategy { ... }
class PayPalPayment implements PaymentStrategy { ... }

class ShoppingCart {
    private PaymentStrategy strategy;
    void setPaymentStrategy(PaymentStrategy s) { this.strategy = s; }
    void checkout(double total) { strategy.pay(total); }
}
```

## Strategy vs. If-Else
| Approach | Adding new behavior | Problem |
|----------|-------------------|---------|
| If-else/switch | Modify existing code | Violates OCP, grows endlessly |
| Strategy | Add new class | Existing code untouched |

## Real-World Uses
- **Sorting algorithms**: Choose quicksort vs mergesort at runtime
- **Compression**: ZIP, GZIP, LZ4 — same interface, different algorithms
- **Validation**: Different validation rules per country/region
- **Java's built-in**: `Comparator<T>` is a Strategy! `Collections.sort(list, comparator)`
- **Spring**: `AuthenticationStrategy`, `ResourceLoader`

## Strategy vs. State Pattern
| | Strategy | State |
|--|----------|-------|
| Who decides | Client picks the strategy | Object transitions itself |
| Intent | Swap algorithms | Manage state-dependent behavior |
| Awareness | Strategies don't know about each other | States often trigger transitions |

## Quick Memory Aid
> "Same job, different ways to do it. Pick one at runtime."

---

## Strategy Pattern in Airline Management System

### What Problem Does It Solve?
When you have **multiple algorithms** for a specific task, and you want to switch between them at runtime without changing the client code.

**Example**: Pricing strategies:
- Economy class: base price + 5% tax
- Business class: base price + 18% tax
- Dynamic pricing: base price × demand factor

If you use `if-else` or `switch` in the pricing code, adding a new strategy requires modifying existing code (violates Open/Closed Principle).

### What is Changing? What Varies?
- **The algorithm** for a specific task varies (pricing, payment, search, refund).
- **The strategy selection** varies based on context.

### What Should Remain Stable?
- The **interface** for the strategy should remain stable.
- The **client** that uses the strategy should not change when a new strategy is added.

### Who Owns This Responsibility?
- Each **Strategy** owns its specific algorithm.
- The **Context** uses the strategy but doesn't know which one it is.

### Where is My Code Tightly Coupled?
- In [`Flight.calculateTax()`](src/airline_management_system/entity/flights/Flight.java:148), there's an `if-else` on `Seat_Type` to calculate tax. This is hardcoded logic that varies by seat type.
- Payment processing logic would likely be scattered if implemented directly.

### Implementation Steps for Airline Management System

#### Step 1: Pricing Strategy (Not Implemented)
**Current State**: [`Flight.calculateTax()`](src/airline_management_system/entity/flights/Flight.java:148) uses `if-else` on `Seat_Type`.

**What's Pending**:
- Create `PricingStrategy` interface with `calculateTax(double basePrice)` method
- Create concrete strategies: `EconomyPricingStrategy`, `BusinessPricingStrategy`, `FirstClassPricingStrategy`
- Create `PricingStrategyFactory` to select the right strategy based on `Seat_Type`

**Example Implementation**:
```java
public interface PricingStrategy {
    double calculateTax(double basePrice);
    String getStrategyName();
}

public class EconomyPricingStrategy implements PricingStrategy {
    private static final double TAX_RATE = 0.05;
    public double calculateTax(double basePrice) { return basePrice * TAX_RATE; }
    public String getStrategyName() { return "Economy Pricing (5% tax)"; }
}

public class BusinessPricingStrategy implements PricingStrategy {
    private static final double TAX_RATE = 0.18;
    public double calculateTax(double basePrice) { return basePrice * TAX_RATE; }
    public String getStrategyName() { return "Business Pricing (18% tax)"; }
}

public class PricingStrategyFactory {
    public static PricingStrategy getStrategy(Seat_Type seatType) {
        return switch (seatType) {
            case ECONOMY -> new EconomyPricingStrategy();
            case BUSINESS, FIRST_CLASS, PREMIUM_ECONOMY -> new BusinessPricingStrategy();
        };
    }
}
```

#### Step 2: Payment Strategy (Not Implemented)
**What's Pending**:
- Create `PaymentStrategy` interface with `processPayment(double amount, Flight flight)` method
- Create concrete strategies: `CreditCardStrategy`, `UPIStrategy`, `NetBankingStrategy`, `WalletStrategy`

#### Step 3: Refund Strategy (Not Implemented)
**What's Pending**:
- Create `RefundStrategy` interface with `calculateRefundAmount(double paidAmount, Booking booking)` method
- Create concrete strategies: `FullRefundStrategy`, `PartialRefundStrategy`, `NoRefundStrategy`, `VoucherRefundStrategy`

#### Step 4: Search Strategy (Not Implemented)
**What's Pending**:
- Create `SearchStrategy` interface with `search(FlightSearchRequest request)` method
- Create concrete strategies: `SearchByDateStrategy`, `SearchByPriceStrategy`, `SearchByDurationStrategy`, `SearchByAirlineStrategy`

#### Step 5: Seat Selection Strategy (Not Implemented)
**What's Pending**:
- Create `SeatSelectionStrategy` interface with `selectSeat(Flight flight, Passenger passenger)` method
- Create concrete strategies: `AutoAssignStrategy`, `WindowPreferenceStrategy`, `AislePreferenceStrategy`, `ExtraLegroomStrategy`

### Summary: Strategy Pattern Status

| Component | Status | Notes |
|-----------|--------|-------|
| `PricingStrategy` interface | ❌ Not Created | Replaces `if-else` in `calculateTax()` |
| `PaymentStrategy` interface | ❌ Not Created | For different payment methods |
| `RefundStrategy` interface | ❌ Not Created | For different refund policies |
| `SearchStrategy` interface | ❌ Not Created | For different search criteria |
| `SeatSelectionStrategy` interface | ❌ Not Created | For different seat preferences |
| Concrete strategies | ❌ Not Created | None exist yet |

**Pending Work**:
1. Implement `PricingStrategy` to replace hardcoded tax calculation in `Flight`
2. Implement `PaymentStrategy` for different payment methods
3. Implement `RefundStrategy` for different refund policies
4. Implement `SearchStrategy` for different search criteria
5. Implement `SeatSelectionStrategy` for different seat preferences

---

## When to Avoid the Strategy Pattern

| Scenario | Why to Avoid |
|----------|-------------|
| **There's only one algorithm** | If there's no variation in behavior, a Strategy adds unnecessary complexity. Use a simple method instead. |
| **The algorithm is unlikely to change** | If the algorithm is stable and won't have new variations, the Strategy's flexibility is wasted. |
| **The strategies are trivial** | If each strategy is just 1-2 lines of code, the overhead of creating separate classes isn't worth it. |
| **The client should decide the algorithm** | Strategy is ideal when the **Context** should be agnostic to which algorithm runs. If the client needs to know the details, Strategy may not be the right fit. |
| **You have only 2 strategies and they rarely change** | A simple `if-else` or `switch` is more readable and maintainable for a small, stable set of options. |
| **Performance is critical and strategies have high overhead** | If strategies involve heavy object creation or the indirection matters in hot paths, consider inlining. |

### Quick Decision Guide

```
Do you have multiple algorithms for the same task?
├── NO → Use a simple method ✅
└── YES → Do algorithms change at runtime?
    ├── NO → Use inheritance or enum ✅
    └── YES → Use Strategy ✅
```
