# TEST PLAN - SPRINT 5: VPBank Installment Payment

**Sprint:** Sprint 5 (2026-04-01 ~ 2026-04-14)  
**Feature:** "Thanh toán trả góp qua VPBank"  
**Owner:** QA1 (Payment domain specialist)  
**Status:** 📋 In Planning  
**Last Updated:** 2026-03-31

---

## 1. SPRINT GOAL & OVERVIEW

### 1.1 Mục tiêu Sprint

Phát triển tính năng **Thanh toán Installment qua VPBank** cho các đơn hàng từ 3 triệu VND trở lên.

**Promotion:**

- Không có lãi suất (0%) cho các kỳ trả góp 3, 6, 12 tháng
- Không có phí phạt nếu thanh toán sớm
- Lãi suất cháy card từ 15%/năm (so với 0% installment)

### 1.2 Phạm vi Sprint (Acceptance Criteria)

```
Được phát triển:
✅ API endpoint: POST /payments/installment
✅ UI: Radio button chọn kỳ hạn (3/6/12 tháng)
✅ Backend logic: Tính toán của hàng, lãi, phí
✅ Integration: VPBank sandbox API
✅ Error handling: Invalid request, transaction failure

Chưa phát triển (Out-of-scope S5):
❌ Auto-retry failed payment (S6)
❌ EMI SMS notification (S6)
❌ Admin dashboard for EMI management (S7)
```

---

## 2. PHÂN TÍCH RỦI RO KINH DOANH (Business Risk Analysis)

### 2.1 Tại sao payment feature có rủi ro cao nhất?

Thanh toán liên quan trực tiếp đến **tiền thật** → **Có thể gây tổn thất lớn** nếu sai:

### 2.2 Rủi ro & Kịch bản Fail Scenario

| Rủi ro                    | Chi tiết                                                        | Tác động                      | Xác suất | Mức độ       |
| ------------------------- | --------------------------------------------------------------- | ----------------------------- | -------- | ------------ |
| **Charge sai tiền**       | Tính toán installment amount sai → user bị charge 5M thay vì 3M | Mất khách hàng, lawsuit       | 20%      | **CRITICAL** |
| **Charge 2 lần**          | Transaction success nhưng gọi API 2 lần → charge 6M             | Mất tiền người dùng           | 15%      | **CRITICAL** |
| **Không giảm số dư**      | Payment thành công nhưng không cập nhật inventory               | Out of stock, conflict order  | 10%      | **CRITICAL** |
| **Lộ thông tin thẻ**      | Card info lưu plaintext hoặc transmit unencrypted               | Bảo mật, compliance violation | 5%       | **CRITICAL** |
| **Installment limit sai** | VPBank limit 50M nhưng hệ thống allow 100M                      | Reject transaction, user chê  | 25%      | **HIGH**     |
| **VPBank API timeout**    | API sandbox hung → redirect page hang                           | Timeout, UX fail              | 40%      | **HIGH**     |
| **Lãi suất tính sai**     | Công thức lãi suất lệch → charge customer extra                 | Unfair, refund request        | 15%      | **HIGH**     |

### 2.3 Test Approach

```
High Risk = High Test Intensity!

Rủi ro Cao  →  Loại Test
────────────────────────────────────────
Charge sai tiền  →  Unit Test (calculation) + API Test + E2E Test
Charge 2 lần     →  Idempotency test + Concurrency test
Lộ thông tin     →  Security test + Code review
VPBank timeout   →  Performance test + Retry logic test
```

---

## 3. TEST SCOPE & STRATEGY

### 3.1 In Scope - Cái gì cần test

| Module             | In Scope                         | Out Scope (Lý do)                  |
| ------------------ | -------------------------------- | ---------------------------------- |
| **API Payment**    | ✅ Validate business logic       | ❌ VPBank side (3rd party)         |
| **UI Installment** | ✅ Display options, calculations | ❌ Browser compatibility (generic) |
| **Database**       | ✅ Data consistency, integrity   | ❌ Backup/recovery (DevOps)        |
| **Error Handling** | ✅ Graceful failure, retry logic | ❌ VPBank sandbox restart          |
| **Integration**    | ✅ Happy path + edge case        | ❌ VPBank server health            |

### 3.2 Test Distribution

```
Unit Test (40%):
  - Calculate installment amount
  - Calculate interest
  - Validate order amount (>= 3M)
  - Validate promotion conditions

API Test (35%):
  - Payment creation (POST /payments/installment)
  - Payment status check (GET /payments/{id})
  - Transaction rollback on error
  - Idempotency (retry same request)

UI Test (15%):
  - Render 3/6/12 month options
  - Amount calculation display
  - Error message handling
  - Form validation

Performance (10%):
  - Payment API response time < 2s
  - Payment page load time < 3s
  - Concurrent payment handling (16 parallel requests)
```

---

## 4. TEST CASE DESIGN (15 Test Cases)

### 4.1 Test Case Mapping

```
TC Priority Matrix:
─────────────────────────────────────
        P1              P2              P3
─────────────────────────────────────
Happy Path    TC-001  TC-002  TC-003   TC-011
              TC-004  TC-005  TC-012
Sad Path      TC-006  TC-007  TC-008   TC-013
              TC-009  TC-010  TC-014
Edge Cases                             TC-015
─────────────────────────────────────
```

### 4.2 Detailed Test Cases

| TC ID      | Tiêu đề                                                  | Loại        | Ưu tiên | Bước thực hiện                                                                              | Kết quả mong đợi                                                                   | Input                 | Ghi chú               |
| ---------- | -------------------------------------------------------- | ----------- | ------- | ------------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------- | --------------------- | --------------------- |
| **TC-001** | ✅ Thanh toán trả góp 3 tháng thành công cho đơn >= 3M   | API         | **P1**  | 1. POST /payments/installment<br/>2. body: {amount:3000000, period:3, method:"installment"} | HTTP 201<br/>status="APPROVED"<br/>installmentAmount=1000000                       | amount=3M, period=3   | **CRITICAL PATH**     |
| **TC-002** | ✅ Thanh toán trả góp 6 tháng với 0% lãi                 | API         | **P1**  | 1. POST payment 6/12m<br/>2. Verify interest = 0                                            | HTTP 201<br/>interest_rate=0%<br/>monthly_payment calc OK                          | amount=6M, period=6   | Promo check           |
| **TC-003** | ✅ Thanh toán trả góp 12 tháng                           | API         | **P1**  | 1. POST payment<br/>2. period=12                                                            | HTTP 201<br/>installments=12                                                       | amount=10M, period=12 | Long term             |
| **TC-004** | ✅ UI hiển thị 3 tùy chọn kỳ hạn                         | UI          | **P1**  | 1. Mở trang checkout<br/>2. Chọn VPBank<br/>3. Kiểm tra radio options                       | 3 radio button hiển thị:<br/>- 3 tháng (0%)<br/>- 6 tháng (0%)<br/>- 12 tháng (0%) | Product >= 3M         | UX verify             |
| **TC-005** | ✅ Tính toán số tiền hàng tháng đúng                     | Unit        | **P1**  | 1. Call calculateMonthlyPayment()<br/>2. amount=6.000.000, period=6                         | monthly = 1.000.000                                                                | Input: 6M, 6m         | Math logic            |
| **TC-006** | ❌ Thanh toán từ chối nếu đơn < 3 triệu                  | API         | **P1**  | 1. POST /payments/installment<br/>2. amount=2500000                                         | HTTP 400<br/>error="ORDER_TOO_SMALL"<br/>message="Min 3M required"                 | amount=2.5M           | Boundary test         |
| **TC-007** | ❌ API reject nếu period không hợp lệ                    | API         | **P2**  | 1. POST payment<br/>2. period=5 (invalid)                                                   | HTTP 400<br/>error="INVALID_PERIOD"                                                | period=5              | Validation            |
| **TC-008** | ❌ Null pointer khi amount missing                       | API         | **P2**  | 1. POST payment<br/>2. {period=3} (no amount)                                               | HTTP 400<br/>error="MISSING_REQUIRED_FIELD"                                        | missing amount        | Error handling        |
| **TC-009** | ❌ Transaction rollback nếu VPBank reject                | API         | **P2**  | 1. POST payment (VPBank sandbox simulate FAIL)<br/>2. Verify DB rollback                    | HTTP 502<br/>order status unchanged<br/>payment record = FAILED                    | VPBank return 400     | Cleanup check         |
| **TC-010** | ❌ Idempotency: Call cùng request 2 lần chỉ charge 1 lần | API         | **P1**  | 1. POST payment (idempotency_key=UUID)<br/>2. POST same request again                       | Lần 1: HTTP 201 (charged)<br/>Lần 2: HTTP 200 (return cached)                      | idempotency_key=same  | Prevent double charge |
| **TC-011** | ✅ Promotion 0% lãi áp dụng đúng                         | API         | **P2**  | 1. POST payment with promotion code<br/>2. Verify interest=0                                | HTTP 201<br/>interest = 0%<br/>No extra fee                                        | code="VPBANK0"        | Business logic        |
| **TC-012** | ✅ Payment history lưu câu trả lời VPBank                | API         | **P2**  | 1. POST payment<br/>2. GET /payments/history                                                | Response chứa:<br/>- transaction_id from VPBank<br/>- timestamp<br/>- status       | -                     | Audit trail           |
| **TC-013** | ❌ Concurrent payment: 5 user checkout cùng lúc          | API         | **P3**  | 1. Spawn 5 threads<br/>2. Each POST /payments<br/>3. Verify all success                     | All 5 return HTTP 201<br/>No race condition<br/>DB consistent                      | 5 concurrent          | Load test             |
| **TC-014** | ❌ User cancel EMI after 1st payment                     | API         | **P3**  | 1. POST payment<br/>2. Call cancel endpoint                                                 | HTTP 200<br/>status="CANCELLED"<br/>Refund triggered                               | -                     | Future feature?       |
| **TC-015** | ✅ VPBank integration smoke test                         | Integration | **P1**  | 1. Hit VPBank sandbox endpoint<br/>2. Verify connection                                     | HTTP 200 or error from VPBank<br/>(not our problem)                                | VPBank URL            | Connectivity          |

---

## 5. ENTRY CRITERIA - Khi nào bắt đầu test?

```
☑️ Checklist trước khi test:

Development:
  ✅ Feature branch created: feature/vpbank-installment
  ✅ Unit tests written by dev (>80% coverage)
  ✅ Code review approved
  ✅ Deployed to staging

Test Environment:
  ✅ VPBank sandbox account: [credentials in GitHub Secrets]
  ✅ Test data account: [VPBANK_TEST_ACCOUNT in secret]
  ✅ Database: Fresh snapshot, > 1000 test orders
  ✅ API endpoint: /payments/installment responding

QA Preparation:
  ✅ Test case reviewed: All 15 TC signed off
  ✅ Test data CSV prepared
  ✅ Selenium scripts ready
  ✅ Allure environment configured

Date/Time: Start no later than T+2 days after code merge
```

---

## 6. EXIT CRITERIA - Khi nào dừng & release?

```
🎯 Definition of "Payment Feature DONE":

Functional:
  ✅ All 15 TC PASS (or waived with risk acknowledge)
  ✅ Zero P1 bug mở
  ✅ Zero P2 bug mở (có thể discuss)

Quality:
  ✅ Unit test coverage >= 85%
  ✅ API response time <= 2s (p95)
  ✅ No database inconsistency

Compliance:
  ✅ PCI DSS compliance check (no card logging)
  ✅ Security review approved
  ✅ Allure report generated & linked in PR

Sign-offs:
  ✅ QA Lead: APPROVED
  ✅ Dev Lead: APPROVED
  ✅ Product Owner: APPROVED

If not met → HOLD RELEASE (không release theo schedule)
```

---

## 7. BLOCKERS & RISKS

### 7.1 Known Blockers (Hiện tại biết)

| Blocker                                    | Impact                     | Workaround                      | Owner    |
| ------------------------------------------ | -------------------------- | ------------------------------- | -------- |
| **VPBank Sandbox không stable**            | Cannot test → TC fail      | Dùng mock server (Postman Echo) | Dev Lead |
| **Test data account not ready**            | Cannot create test payment | Manual setup account trước T+1  | DevOps   |
| **API /payments/installment not deployed** | Cannot test                | Merge PR to staging ASAP        | Dev      |

### 7.2 Potential Risks (Có thể xảy ra)

| Risk                      | Xác suất | Tác động      | Giải pháp                       | Contingency          |
| ------------------------- | -------- | ------------- | ------------------------------- | -------------------- |
| VPBank API go slow        | 40%      | Test timeout  | Increase wait time to 30s       | Reschedule test      |
| Database transaction lock | 20%      | Test hang     | Rollback transaction explicitly | Restart DB           |
| Team absence (leave)      | 10%      | Delay testing | Cross-training QA2 as backup    | Extend test timeline |

---

## 8. RESOURCES & SCHEDULING

### 8.1 QA Resource Allocation

| Resource             | Primary                   | Secondary        | Availability  |
| -------------------- | ------------------------- | ---------------- | ------------- |
| **QA1** (Automation) | Write test code           | Execute API test | Mon-Fri 8-5pm |
| **QA2** (Manual)     | Exploratory test          | UI test          | Mon-Fri 8-5pm |
| **QA Lead**          | Review TC, Approve report | Escalate issues  | As-needed     |

### 8.2 Sprint Schedule

```
Week 1 (Apr 1-5): Development
  Mon: Feature planning, test case design
  Tue-Thu: Dev coding, QA prepare test environment
  Fri: Code review, merge to staging

Week 2 (Apr 8-14): Testing & Verification
  Mon: Smoke test (basic 3TC pass)
  Tue-Wed: Full regression test
  Thu: Bug fix & re-test
  Fri: Final UAT + Release readiness sign-off
```

### 8.3 Effort Estimation

| Activity                   | Estimate | Owner     |
| -------------------------- | -------- | --------- |
| Test case design           | 4h       | QA Lead   |
| Test execution (manual)    | 8h       | QA2       |
| Test automation (Selenium) | 12h      | QA1       |
| Bug fix & re-test          | 8h       | QA1 + QA2 |
| Report generation          | 2h       | QA Lead   |
| **TOTAL**                  | **34h**  | -         |

---

## 9. COMMUNICATION & ESCALATION

### 9.1 Daily Stand-up (10 AM)

```
What did QA complete yesterday?
→ "Wrote TC-001 to TC-005, setup VPBank sandbox"

What is QA doing today?
→ "Execution: TC-001 to TC-010, bug triage"

Any blockers?
→ "VPBank API timeout > 5s, escalating to Dev"
```

### 9.2 Escalation Path

```
Issue Found → QA1 Report → QA Lead → Dev Lead → Product Owner
     (hour 0)   (hour 0.5)   (hour 1)   (hour 2)

P1 Bug: React within 1 hour
P2 Bug: React within 4 hours
P3 Bug: Can defer to next sprint
```

---

## 10. SUCCESS METRICS

### 10.1 Testing Metrics

| Metric            | Target           | Formula                             | Owner   |
| ----------------- | ---------------- | ----------------------------------- | ------- |
| **Pass Rate**     | >= 95%           | Passed TC / Total TC                | QA Lead |
| **Bug Density**   | < 5 per 1000 LOC | Bug Found / Code Lines              | QA1     |
| **Code Coverage** | >= 85%           | (Covered Lines / Total Lines) x 100 | Dev     |
| **Defect Escape** | 0 (P1)           | Bug found after release             | QA Lead |

### 10.2 Speed Metrics

| Phase              | Target               | Owner |
| ------------------ | -------------------- | ----- |
| **Test Execution** | <= 2 hour per TC set | QA1   |
| **Bug Fix Time**   | <= 4 hours (P1)      | Dev   |
| **Re-test Time**   | <= 1 hour per fix    | QA1   |

---

## 11. SIGN-OFF & APPROVAL

### 11.1 Test Plan Review

- [ ] QA Lead: Reviewed & signed off
- [ ] Dev Lead: Reviewed & signed off
- [ ] Product Owner: Reviewed & signed off

### 11.2 Sign-off Workflow

```
Test Plan Drafted (Mar 31)
    ↓
QA Lead Review (Apr 1)
    ↓
Dev Lead Input (Apr 1)
    ↓
PO Approval (Apr 2)
    ↓
Testing Starts (Apr 3)
```

---

## 12. APPENDIX

### 12.1 Test Data

```SQL
-- Sample test order (>= 3M)
INSERT INTO orders VALUES
  (1, 'user@test.com', 3000000, 'pending', '2026-04-03');

-- Sample payment (installment)
INSERT INTO payments VALUES
  (1, 1, 3000000, 'installment', 'PENDING', NULL, '2026-04-03');
```

### 12.2 Test Environment Setup

```bash
# Deploy feature branch to staging
git checkout develop
git pull origin develop
git merge origin/feature/vpbank-installment
git push origin develop

# DB Reset
./scripts/db-reset.sh staging

# Verify VPBank sandbox connection
curl -X GET https://sandbox.vpbank.com.vn/health
```

### 12.3 Pipeline Configuration (Task 6)

```yaml
# .github/workflows/test-payment.yml
name: Payment Feature Tests
on:
  pull_request:
    branches: [develop]
    paths:
      - "src/main/java/payment/**"
      - "src/test/java/tests/PaymentTest.java"

jobs:
  payment-tests:
    runs-on: ubuntu-latest
    if: contains(github.head_ref, 'vpbank') || contains(github.head_ref, 'payment')

    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with: { java-version: "17", cache: "maven" }
      - run: mvn test -DsuiteXmlFile=testng-payment.xml
        env:
          VPBANK_SANDBOX_URL: ${{ secrets.VPBANK_SANDBOX_URL }}
          VPBANK_API_KEY: ${{ secrets.VPBANK_API_KEY }}
```

---

## 13. APPENDIX: TEST CASE TEMPLATES

### Template: Boundary Value Test

```
Test: Payment amount boundary
├─ Input: 2,999,999 → Expected: FAIL (< 3M min)
├─ Input: 3,000,000 → Expected: PASS (= min)
└─ Input: 3,000,001 → Expected: PASS (> min)
```

### Template: State Transition Test

```
Payment State Machine:
PENDING → AUTHORIZED → CAPTURED → SETTLED
   ↓          ↓           ↓
  FAILED    FAILED      FAILED
```

---

## 14. HISTORY

| Version | Date       | Author  | Status  | Notes                         |
| ------- | ---------- | ------- | ------- | ----------------------------- |
| 1.0     | 2026-03-31 | QA Lead | Draft   | Initial version               |
| 1.1     | 2026-04-01 | QA Lead | Final   | After dev QA approval         |
| 2.0     | TBD        | QA1     | Testing | Updated with actual learnings |

---

**END OF TEST PLAN - SPRINT 5**

_Document Classification: Internal Use  
Next Review: 2026-04-08 (Mid-Sprint Check-in)_
