# Trading API - Test Documentation

A comprehensive guide for testing the Trading API with detailed endpoints, SQL queries, and edge case scenarios.
Download zip file and Import Project as existing maven project or Clone the reposiory 
and run the Service and setUp the h2 console.

## Design Decisions

1. **Layered Architecture**
   Used Controller → Service → Repository structure for clean separation of concerns.

2. **Separate Tables (Orders, Portfolio, Holdings)**
   Orders store history, Holdings store current state, Portfolio represents the trader.

3. **Holdings as Snapshot**
   Holdings stores the latest stock quantity for quick access.

4. **Business Logic in Service Layer**
   Keeps controllers simple and centralizes logic in services.

5. **DTO Usage**
   Prevents exposing internal entities and keeps API flexible.

6. **Transaction Management**
   Used `@Transactional` to ensure atomic operations and data consistency.

7. **Concurrency Handling**
   Applied pessimistic locking during stock updates to prevent overselling.

8. **Read vs Write Separation**
   Locking is used only for write operations, not for read/validation.

9. **Validation**
   Used annotations to ensure valid input and avoid runtime errors.

10. **Separate Overlap Service**
    Kept overlap logic independent as it is pure business computation.

11. **Service Interface & Implementation (OOP)**
    Used interfaces and implementations to follow abstraction and loose coupling.

## H2 Console Access

Access the in-memory database console on browser for real-time verification.

**URL:**
```
http://localhost:8080/h2-console
```
Use this cred's for connection

**Connection Details:**
- **JDBC URL:** `jdbc:h2:mem:testdb`
- **Username:** `sa`
- **Password:** (leave empty)

---

## Testing Workflow

### Quick Start

1. Start the API server on `http://localhost:8080`
2. Use Postman, cURL, or any REST client
3. Follow the test cases in order (1-10)
4. Verify results using H2 Console at `/h2-console`
5. Check database state with provided SQL queries
## Base URL

```
http://localhost:8080
```

---

## API Endpoints

### 1. Place Order (BUY)

Place a buy order for a stock.

**Endpoint:** `POST /orders`

**Request Body:**
```json
{
  "traderId": "T001",
  "stock": "AAPL",
  "sector": "TECH",
  "quantity": 50,
  "side": "BUY"
}
```

---

### 2. Place Multiple Orders (Limit Test)

Test the pending orders limit. The API should allow maximum 3 pending orders.

**Endpoint:** `POST /orders`

**Request Body:**
```json
{
  "traderId": "T001",
  "stock": "TSLA",
  "sector": "TECH",
  "quantity": 10,
  "side": "BUY"
}
```

**Note:** Run 4 times → 4th request should fail with error:
```
"Max 3 pending orders allowed"
```

---

### 3. Fill Order

Mark an order as filled.

**Endpoint:** `POST /orders/{id}/fill`

**Parameters:**
- `id` (path parameter): Order ID

---

### 4. Get Portfolio

Retrieve the portfolio for a specific trader.

**Endpoint:** `GET /portfolio/{traderId}`

**Parameters:**
- `traderId` (path parameter): Trader ID (e.g., T001)

---

### 5. Add Stock Directly

Add stock holdings directly to a trader's portfolio.

**Endpoint:** `POST /portfolio/{traderId}/add`

**Query Parameters:**
- `stock`: Stock symbol (e.g., TSLA)
- `sector`: Sector name (e.g., TECH)
- `quantity`: Number of shares

**Example:**
```
POST /portfolio/T001/add?stock=TSLA&sector=TECH&quantity=30
```

---

### 6. Valid Sell Order

Place a sell order for stocks the trader owns.

**Endpoint:** `POST /orders`

**Request Body:**
```json
{
  "traderId": "T001",
  "stock": "AAPL",
  "sector": "TECH",
  "quantity": 20,
  "side": "SELL"
}
```

**Then fill the order:**
```
POST /orders/{id}/fill
```

---

### 7. Invalid Sell (Insufficient Shares)

Attempt to sell more shares than the trader owns.

**Endpoint:** `POST /orders`

**Request Body:**
```json
{
  "traderId": "T001",
  "stock": "AAPL",
  "sector": "TECH",
  "quantity": 999,
  "side": "SELL"
}
```

**Expected Response:**
```
"Insufficient shares"
```

---

### 8. Cancel Order

Cancel a pending order.

**Endpoint:** `POST /orders/{id}/cancel`

**Parameters:**
- `id` (path parameter): Order ID

---

### 9. Overlap Data Setup

Prepare test data for overlap analysis.

**Endpoints:**
```
POST /portfolio/T001/add?stock=AAPL&sector=TECH&quantity=50
POST /portfolio/T001/add?stock=TSLA&sector=TECH&quantity=30
POST /portfolio/T001/add?stock=NVDA&sector=TECH&quantity=20
```

---

### 10. Portfolio Overlap Analysis

Get overlap analysis for a trader's portfolio.

**Endpoint:** `GET /portfolio/{traderId}/overlap`

**Parameters:**
- `traderId` (path parameter): Trader ID (e.g., T001)

---

## Database Verification

### View All Orders

```sql
SELECT * FROM orders;
```

### View Portfolio

```sql
SELECT * FROM portfolio;
```

### View Holdings

```sql
SELECT * FROM holdings;
```

### Check Specific Trader Portfolio

```sql
SELECT * FROM portfolio WHERE trader_id = 'T001';
```

### Check Holdings for Trader

```sql
SELECT h.*
FROM holdings h
JOIN portfolio p ON h.portfolio_id = p.id
WHERE p.trader_id = 'T001';
```

### Check Pending Orders Count

```sql
SELECT COUNT(*)
FROM orders
WHERE trader_id = 'T001' AND status = 'PENDING';
```

### Check Filled Orders

```sql
SELECT * FROM orders WHERE status = 'FILLED';
```

---



## Edge Case Tests

Test the following edge cases to ensure API robustness:

1. **Fill same order twice** → Should fail
2. **Cancel filled order** → Should fail
3. **Cancel cancelled order** → Should fail
4. **Sell without owning stock** → Should fail
5. **Empty portfolio overlap** → Should return LOW risk

---



### Example cURL Commands

**Place a buy order:**
```bash
curl -X POST http://localhost:8080/orders \
  -H "Content-Type: application/json" \
  -d '{
    "traderId": "T001",
    "stock": "AAPL",
    "sector": "TECH",
    "quantity": 50,
    "side": "BUY"
  }'
```

**Get portfolio:**
```bash
curl http://localhost:8080/portfolio/T001
```

**Add stock directly:**
```bash
curl -X POST "http://localhost:8080/portfolio/T001/add?stock=TSLA&sector=TECH&quantity=30"
```

---
