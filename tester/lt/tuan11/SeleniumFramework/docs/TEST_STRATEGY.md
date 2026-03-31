# TEST STRATEGY - ShopEasy E-Commerce Platform

**Dự án:** ShopEasy - Ứng dụng mua sắm online  
**Phiên bản:** 1.0  
**Ngày tạo:** 2026-03-31  
**Người soạn:** QA Lead  
**Phạm vi:** Toàn dự án  
**Hiệu lực:** Tùy theo chu kỳ phát triển

---

## 1. PHẠM VI KIỂM THỬ (Test Scope)

### 1.1 IN SCOPE (Nằm trong phạm vi kiểm thử)

| Thứ tự | Module                      | Mô tả                                               | Lý do                                          |
| ------ | --------------------------- | --------------------------------------------------- | ---------------------------------------------- |
| 1      | **Tài khoản & Xác thực**    | Register, Login, Logout, Reset password             | Cốt lõi hệ thống, ảnh hưởng toàn bộ người dùng |
| 2      | **Tìm kiếm & Lọc sản phẩm** | Search by keyword, filter by category/price/rating  | Tính năng chính giúp người dùng tìm sản phẩm   |
| 3      | **Giỏ hàng & Checkout**     | Add to cart, update quantity, remove item, checkout | Process thanh toán, đảm bảo không mất dữ liệu  |
| 4      | **Thanh toán**              | Payment gateway, promotions, tax calculation        | Liên quan đến tiền, **CỰC KỲ QUAN TRỌNG**      |
| 5      | **Quản lý đơn hàng**        | View orders, track shipment, cancel order           | Trải nghiệm người dùng, hỗ trợ khách hàng      |

### 1.2 OUT OF SCOPE (Không nằm trong phạm vi hiện tại)

| Thứ tự | Module                           | Lý do loại bỏ                                                | Target Release      |
| ------ | -------------------------------- | ------------------------------------------------------------ | ------------------- |
| 1      | **Admin Dashboard**              | Backend team sẽ test riêng. Sẽ tích hợp ở Sprint 8           | Sprint 8 (2026-Q3)  |
| 2      | **Mobile App**                   | Hiện tại chỉ test Web. App iOS/Android sẽ có QA team riêng   | Sprint 10 (2026-Q4) |
| 3      | **Performance & Load Testing**   | Không có QA performance chuyên trách. Sẽ tập hợp trong UAT   | Pre-Release         |
| 4      | **Security Penetration Testing** | Cần consultant security bên ngoài, không phải trách nhiệm QA | Post-Release        |

**Lý do:** OUT OF SCOPE do:

- Không có resource (Admin Dashboard test bởi backend dev)
- Nằm ngoài phạm vi tech stack hiện tại (Mobile app)
- Cần expertise khác hoặc content specialist (Performance, Security)

---

## 2. PHÂN LOẠI TEST & TỶ LỆ (Test Types & Distribution)

### 2.1 Chiến lược test

Dựa trên **Test Pyramid** của Mike Cohn:

```
        △ E2E (10%)
       / \  UI Integration Tests
      / E \ 12% (System-level)
     /    \
    /______\  API Tests - 28%
   /        \ (Service-level)
  /  Unit Test\
 /   60%      \ Unit & Component
/______________\
```

| Loại Test       | %   | Công cụ                | Lý do chọn                              |
| --------------- | --- | ---------------------- | --------------------------------------- |
| **Unit Test**   | 60% | JUnit 5                | Nhanh, rẻ, bao phủ chi tiết logic       |
| **API Test**    | 28% | REST Assured / Postman | Test backend, không phụ thuộc UI, nhanh |
| **UI/E2E Test** | 8%  | Selenium + TestNG      | Verify user journey thực tế             |
| **Performance** | 3%  | JMeter (future)        | Đảm bảo response time < 2s              |
| **Security**    | 1%  | OWASP ZAP (future)     | Scan SQL injection, XSS                 |

### 2.2 Giải thích tỷ lệ

**Tại sao chọn 60% Unit, 28% API, 8% UI, 3% Performance, 1% Security?**

1. **60% Unit Test:**
   - E-commerce cần logic tính toán chính xác (discount, tax, shipping fee)
   - Regression test nhanh khi refactor code
   - Chi phí maintenance thấp nhất

2. **28% API Test:**
   - Backend API không phụ thuộc UI framework
   - Có thể chạy song song trên nhiều client (web, mobile, partner)
   - Phát hiện lỗi sớm trong development cycle

3. **8% UI/E2E Test:**
   - Chỉ test 3-4 critical user journey (signup, search, checkout)
   - Tránh test quá nhiều UI (brittle test)
   - Bảo vệ trước regression lớn

4. **3% Performance, 1% Security:**
   - Sprint đầu focus vào functionality
   - Performance & Security test sẽ tăng sau khi codebase stable

---

## 3. TÍNH NĂNG & CÔNG CỤ (Tools & Framework)

### 3.1 Công cụ kiểm thử

| Công cụ        | Phiên bản | Mục đích       | Lý do chọn                               |
| -------------- | --------- | -------------- | ---------------------------------------- |
| Selenium       | 4.18      | UI Automation  | Phổ biến, hỗ trợ cross-browser, free     |
| TestNG         | 7.9       | Test Framework | Powerful assertion, parallel execution   |
| REST Assured   | 5.4       | API Testing    | DSL cho REST, dễ viết                    |
| Maven          | 3.9       | Build Tool     | Standard Java project management         |
| Allure         | 2.26      | Reporting      | Report đẹp, easy integration             |
| Docker         | Latest    | Selenium Grid  | Chạy test trên container, CI/CD friendly |
| GitHub Actions | N/A       | CI/CD          | Native với GitHub, free for public repo  |

### 3.2 Framework Architecture

```
SeleniumFramework/
├── src/
│   ├── main/java/framework/
│   │   ├── base/
│   │   │   └── BasePage.java (POM pattern)
│   │   ├── config/
│   │   │   └── ConfigReader.java (Manage environment)
│   │   ├── factory/
│   │   │   └── DriverFactory.java (Multi-browser support)
│   │   └── utils/
│   │       └── ScreenshotUtil.java
│   └── test/java/
│       ├── pages/
│       │   ├── LoginPage.java
│       │   ├── ProductPage.java
│       │   └── CheckoutPage.java
│       └── tests/
│           ├── LoginTest.java
│           ├── SearchTest.java
│           └── PaymentTest.java
├── .github/workflows/ (CI/CD pipelines)
└── docker-compose.yml (Selenium Grid)
```

---

## 4. MÔI TRƯỜNG KIỂM THỬ (Test Environments)

| Môi trường     | URL                   | Đặc điểm                                | Khi dùng                                  |
| -------------- | --------------------- | --------------------------------------- | ----------------------------------------- |
| **Dev**        | http://localhost:8080 | Mới nhất code, hay bị lỗi               | Smoke test ngay khi dev xong              |
| **Staging**    | staging.shopeasy.vn   | Mirror production, production-like data | Regression test, UAT                      |
| **Production** | shopeasy.vn           | Thực tế, real users                     | Synthetic monitoring (không test tự động) |

### 4.1 Chuẩn bị test data

- **Dev:** Generate fake data dùng Faker (không cần production data)
- **Staging:** Snapshot của production data, reset hàng tuần
- **Production:** Chỉ dùng đối với synthetic monitoring (read-only)

---

## 5. ENTRY & EXIT CRITERIA (Điều kiện bắt đầu & kết thúc)

### 5.1 Entry Criteria - Khi nào bắt đầu test?

```
✓ Code được commit và merge vào branch develop
✓ Build thành công trên CI/CD (no compilation errors)
✓ Dev team đã tạo test data cần thiết
✓ Test case được review và approve bởi QA Lead
✓ Environment (staging) ổn định
```

Nếu một điều kiện không đủ → **KHÔNG BẮT ĐẦU TEST**

### 5.2 Exit Criteria - Khi nào dừng test?

```
✓ Regression test pass rate >= 95%
✓ Zero (0) bug Priority 1 (P1) mở
✓ Zero (0) bug Priority 2 (P2) chặn release
✓ Code coverage >= 80% (Unit test coverage)
✓ Allure Report được publish thành công
✓ PO approve chức năng đáp ứng requirement
```

**Exit = Sản phẩm an toàn để release lên production**

---

## 6. RỦI RO KIỂM THỬ (Test Risks)

### 6.1 Rủi ro kỹ thuật

| Rủi ro                                           | Xác suất    | Tác động                | Kế hoạch giảm thiểu                                               | Người chịu trách nhiệm |
| ------------------------------------------------ | ----------- | ----------------------- | ----------------------------------------------------------------- | ---------------------- |
| Staging data xóa mất (VD: payment partner reset) | Cao (50%)   | Không test được payment | • Test trước khi partial reset • Backup data script               | DevOps + QA            |
| API payment gateway không ổn định                | Trung (30%) | Timeout payment test    | • Redundant payment gateway (Plan B) • Retry logic                | Backend Lead           |
| Selenium Grid network lag trên CI                | Trung (40%) | Element not found error | • Explicit wait (20s) • Retry failed test • Local driver fallback | QA Automation          |
| Browser version mismatch                         | Thấp (10%)  | Compatibility issue     | • WebDriverManager auto match • Keep multiple driver versions     | DevOps                 |

### 6.2 Rủi ro nghiệp vụ

| Rủi ro                       | Mô tả                                               | Tác động                               | Cách xử lý                                                               |
| ---------------------------- | --------------------------------------------------- | -------------------------------------- | ------------------------------------------------------------------------ |
| **Mất tiền người dùng**      | Bug trong tính toán discount → charge sai tiền      | **CRITICAL** - Mất khách hàng, lawsuit | • Test discount logic cực kỳ cẩn thận • E2E test payment • Manual review |
| **Bị hack thanh toán**       | SQL injection trong payment page                    | **CRITICAL** - Mất dữ liệu thẻ         | • Security test • Code review • PEN test (hire consultant)               |
| **Server down ngày release** | Performance không test → crash khi user tăng sudden | **HIGH** - Down time revenue loss      | • Load test trước release • Capacity planning                            |

---

## 7. LỊCH TRÌNH KIỂM THỬ (Test Schedule)

### 7.1 Test Execution Schedule

```
Git Event          Trigger              Test Suite              Môi trường   Timeout
─────────────────────────────────────────────────────────────────────────────────
Push Code    →    GitHub Actions    →  Smoke Test (5 min)     Dev         15 min
             →    (CI)              →  Unit Test (10 min)     Dev         20 min
             →                      →  API Test (15 min)      Staging     25 min

Open PR      →    GitHub Actions    →  Smoke Test (5 min)     Dev         15 min
             →    (PR Check)        →  + Code Review
             →                      →  + Security Scan

Nightly      →    Cron: 2 AM        →  Full Regression        Staging     60 min
(2 AM UTC)   →    (Scheduled)       →  (all API + UI)
             →    Mon-Fri only      →  + Performance Test

Pre-Release  →    Manual Trigger    →  UAT Smoke (all browser)Production   90 min
(Wed before  →    (QA Lead)         →  + Security PEN test    + Edge cases
release)     →                      →  + Accessibility
```

### 7.2 Phân bổ công việc

| Sprint    | Tuần | Task                      | Owner    | Estimate |
| --------- | ---- | ------------------------- | -------- | -------- |
| S5        | W1   | Viết test case thanh toán | QA1      | 8h       |
| S5        | W1   | Setup Selenium Grid       | QA2      | 6h       |
| S5        | W2   | Code automation test      | QA1, QA2 | 16h      |
| S5        | W2   | Regression test           | QA1      | 8h       |
| S5        | W2   | Bug fix & re-test         | QA1      | 4h       |
| **Total** |      |                           |          | **42h**  |

---

## 8. ĐỊNH NGHĨA SEVERITY & PRIORITY

### 8.1 Severity Levels (Tính chất lỗi)

| Level             | Mô tả                                      | Ví dụ              | Fix timeline          |
| ----------------- | ------------------------------------------ | ------------------ | --------------------- |
| **P1 - Critical** | Treo ứng dụng, mất dữ liệu, lỗi thanh toán | Crash khi checkout | Trong 24h             |
| **P2 - High**     | Tính năng không hoạt động hoàn toàn        | Login fail         | Trong 3 ngày          |
| **P3 - Medium**   | Tính năng hoạt động nhưng sai logic        | Discount sai 5%    | Trong 1 sprint        |
| **P4 - Low**      | UI/UX minor issue                          | Font không đến     | Backlog (next sprint) |

### 8.2 Release Decision Matrix

```
P1 Bugs Mở?  |  P2 Bugs Mở?  |  Pass Rate  |  Decision
─────────────┼───────────────┼─────────────┼────────────
Có (>0)      |  N/A          |  N/A        |  🔴 HOLD RELEASE
Không        |  Có (>2)      |  N/A        |  🟡 DISCUSS
Không        |  Không        |  <90%       |  🟡 DISCUSS
Không        |  Không        |  >=90%      |  🟢 OK TO RELEASE
```

---

## 9. QUẢN LÝ BUG (Bug Management)

### 9.1 Quy trình báo cáo bug

```
Found Bug → File Jira → Assign to Dev → Dev Fix → QA Verify → Close
   (QA)      (P1-P4)    (Sprint)        (3d)      (1d)        (Done)
```

### 9.2 Jira Workflow

- **New** → **In Progress** (Dev assigned) → **In Review** (Dev done) → **QA Test** (QA verify) → **Done**
- Nếu fail verification → back to **In Progress** + comment

---

## 10. REPORT & COMMUNICATION

### 10.1 Daily Reports

- **Passed:** ✅ X test cases
- **Failed:** ❌ Y test cases (P1/P2/P3 breakdown)
- **Blocked:** 🚫 Z test cases (lý do)
- **Coverage:** XX% code coverage

### 10.2 Stakeholders & Frequency

| Người nhận    | Nội dung                | Tần suất         |
| ------------- | ----------------------- | ---------------- |
| Dev Lead      | Test status + new bugs  | Daily (5 PM)     |
| QA Lead       | Test summary, risks     | Daily end-of-day |
| Product Owner | Release readiness       | Before release   |
| C-Level       | ROI, test effectiveness | Weekly/Monthly   |

---

## 11. TOOLS & INFRASTRUCTURE

### 11.1 CI/CD Pipeline

- **GitHub Actions** - Free, native with GitHub
- **Docker** - Selenium Grid container
- **Allure Reports** - Beautiful test reports
- **GitHub Pages** - Host Allure reports

### 11.2 Monitoring & Metrics

```
Metrics to Track:

1. Pass Rate Trend (should be ↑)
2. Bug Escape (should be ↓)
3. Test Execution Time (should be ↓ after optimization)
4. Code Coverage % (should be ↑ towards 80%+)
5. Time to Fix (should be ↓)
```

---

## 12. APPROVAL & HISTORY

| Người        | Vai trò       | Ký duyệt | Ngày       |
| ------------ | ------------- | -------- | ---------- |
| Nguyễn Văn A | QA Lead       | ✓        | 2026-03-31 |
| Trần Thị B   | QA Manager    | ✓        | 2026-03-31 |
| Lê Văn C     | Dev Lead      | ✓        | 2026-04-01 |
| Phạm Thị D   | Product Owner | ✓        | 2026-04-01 |

---

## 13. REVISION HISTORY

| Version | Ngày       | Tác giả | Thay đổi                 |
| ------- | ---------- | ------- | ------------------------ |
| 1.0     | 2026-03-31 | QA Lead | Initial version          |
| 1.1     | TBD        | TBD     | Add mobile test strategy |

---

**END OF TEST STRATEGY DOCUMENT**

**Best Practice Note:** Tài liệu này được review và update mỗi sprint hoặc khi có thay đổi lớn về scope/tool/risk.
