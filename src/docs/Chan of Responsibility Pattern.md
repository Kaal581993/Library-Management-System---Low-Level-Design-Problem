# Chain of Responsibility Pattern

## Intent
Pass a request along a **chain of handlers**. Each handler either **processes** it or **passes** it to the next.

## The Core Roles
| Role | Responsibility |
|------|---------------|
| **Handler** (abstract) | Defines `handle()`, holds `next` reference |
| **Concrete Handler** | Decides if it can process, or passes along |
| **Client** | Sends request to the first handler in the chain |

## How It Works
```
Request → Handler A → Handler B → Handler C → (end)
            │              │            │
         can handle?   can handle?  can handle?
         no → pass     no → pass    yes → process
```
1. Client sends request to the **first** handler
2. Each handler checks: "Can I handle this?"
3. If yes → process it. If no → pass to `next`
4. If no handler can process → request falls off the chain

## Code Structure
```java
abstract class SupportHandler {
    private SupportHandler next;

    SupportHandler setNext(SupportHandler next) {
        this.next = next;
        return next;  // fluent chaining
    }

    void handle(Ticket t) {
        if (canHandle(t)) process(t);
        else if (next != null) next.handle(t);
    }

    abstract boolean canHandle(Ticket t);
    abstract void process(Ticket t);
}
```

## Real-World Uses
- **Servlet Filters**: `doFilter()` → next filter → servlet
- **Middleware**: Express.js `app.use()`, Spring `HandlerInterceptor`
- **Logging frameworks**: DEBUG → INFO → WARN → ERROR level handlers
- **Exception handling**: catch blocks in Java (`try { } catch A { } catch B { }`)
- **Approval workflows**: Employee → Manager → Director → VP

## Chain of Responsibility vs. If-Else
| Approach | Adding new handler | Problem |
|----------|-------------------|---------|
| If-else | Modify existing code | Violates OCP, one giant class |
| Chain | Add new handler class, link it in | Existing handlers untouched |

## Two Variants
| Variant | Behavior |
|---------|----------|
| **Pure** | Exactly one handler processes the request (others skip) |
| **Pipeline** | Every handler processes + passes along (e.g., filters, middleware) |

## Common Pitfalls
- **Unhandled requests**: If no handler matches, request is silently dropped. Always add a fallback at the end.
- **Order matters**: The chain order determines priority. Wrong order = wrong handler processes.
- **Performance**: Very long chains can be slow for every request.

## Quick Memory Aid
> "Pass the buck until someone handles it."

---

## Chain of Responsibility Pattern in Airline Management System

### What Problem Does It Solve?
When a request must pass through a **sequence of handlers**, and you don't know which handler will process it. Each handler decides:
- Process the request and stop
- Pass to the next handler

**Example**: Flight search validation:
1. Is source valid? → Yes, pass to next
2. Is destination valid? → Yes, pass to next
3. Is date valid? → Yes, pass to next
4. Are seats available? → Yes, return results

### What is Changing? What Varies?
- **The validation chain** varies. Different searches may need different validation steps.
- **The handler** that processes the request varies.

### What Should Remain Stable?
- The **chain structure** (each handler has a reference to the next) should remain stable.
- The **request interface** should remain stable.

### Who Owns This Responsibility?
- Each **Handler** owns its specific validation logic.
- The **Client** assembles the chain.

### Where is My Code Tightly Coupled?
- Currently, there's no `FlightSearch` class in the code. The problem statement mentions it but it's not implemented.
- Validation logic would likely be scattered if implemented directly in the search method.

### Implementation Steps for Airline Management System

#### Step 1: Create Handler Interface (Not Implemented)
**What's Pending**:
```java
public interface FlightSearchHandler {
    void setNextHandler(FlightSearchHandler nextHandler);
    boolean handle(FlightSearchRequest request);
}

public class FlightSearchRequest {
    private Locations source;
    private Locations destination;
    private Date date;
    private int passengers;
    // getters and setters
}
```

#### Step 2: Create Concrete Handlers (Not Implemented)
**What's Pending**:
- `SourceValidationHandler` — validates source location
- `DestinationValidationHandler` — validates destination (not same as source)
- `DateValidationHandler` — validates date is not in the past
- `AvailabilityCheckHandler` — checks seat availability (terminal handler)

**Example Implementation**:
```java
public class SourceValidationHandler implements FlightSearchHandler {
    private FlightSearchHandler nextHandler;
    public void setNextHandler(FlightSearchHandler nextHandler) { this.nextHandler = nextHandler; }
    public boolean handle(FlightSearchRequest request) {
        if (request.getSource() == null) {
            System.out.println("Error: Source location is required");
            return false;
        }
        System.out.println("Source validated: " + request.getSource());
        return nextHandler != null && nextHandler.handle(request);
    }
}
```

#### Step 3: Assemble the Chain (Not Implemented)
**What's Pending**:
```java
// Client assembles the chain
FlightSearchHandler sourceHandler = new SourceValidationHandler();
FlightSearchHandler destinationHandler = new DestinationValidationHandler();
FlightSearchHandler dateHandler = new DateValidationHandler();
FlightSearchHandler availabilityHandler = new AvailabilityCheckHandler();

// Link them: source → destination → date → availability
sourceHandler.setNextHandler(destinationHandler);
destinationHandler.setNextHandler(dateHandler);
dateHandler.setNextHandler(availabilityHandler);

// Process request
boolean isValid = sourceHandler.handle(request);
```

### Summary: Chain of Responsibility Pattern Status

| Component | Status | Notes |
|-----------|--------|-------|
| `FlightSearchHandler` interface | ❌ Not Created | |
| `FlightSearchRequest` class | ❌ Not Created | |
| `SourceValidationHandler` | ❌ Not Created | |
| `DestinationValidationHandler` | ❌ Not Created | |
| `DateValidationHandler` | ❌ Not Created | |
| `AvailabilityCheckHandler` | ❌ Not Created | |

**Pending Work**:
1. Create `FlightSearchHandler` interface and `FlightSearchRequest` class
2. Implement all concrete handlers
3. Assemble the chain in client code

---

## When to Avoid the Chain of Responsibility Pattern

| Scenario | Why to Avoid |
|----------|-------------|
| **There's only one handler** | If only one handler ever processes the request, a chain is unnecessary overhead. Use a single handler directly. |
| **The chain is always the same and never changes** | If the chain structure is fixed and never varies, hardcoding the chain in a single class is simpler. |
| **You need guaranteed processing by a specific handler** | In a pure chain, requests can fall off the end unhandled. If you need guaranteed processing, use a different pattern. |
| **Order doesn't matter** | If handlers are independent and order doesn't affect the outcome, a chain adds unnecessary coupling between handlers. |
| **Performance is critical with long chains** | Each request traverses the entire chain. With many handlers, this can be slow. Consider parallel processing or a different approach. |
| **Debugging is difficult** | Tracing which handler processed a request can be hard in long chains. Add logging or use a different pattern for critical flows. |
| **Handlers need to communicate with each other** | Chain of Responsibility assumes handlers are independent. If they need to share state or coordinate, the pattern breaks down. |

### Quick Decision Guide

```
Does the request need to pass through multiple handlers?
├── NO → Use a single handler ✅
└── YES → Is the chain structure fixed?
    ├── YES → Consider a single class with multiple methods ✅
    └── NO → Use Chain of Responsibility ✅
```
