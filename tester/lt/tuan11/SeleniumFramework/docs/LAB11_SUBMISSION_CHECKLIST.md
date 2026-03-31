# LAB 11 SUBMISSION CHECKLIST

**Dùng checklist này để verify toàn bộ Lab 11 trước khi nộp**

---

## 📋 TASK-BY-TASK VERIFICATION

### ✅ TASK 1: GitHub Actions CI - 1.5 điểm

```
File tạo/sửa:
  ☑ .github/workflows/selenium-ci.yml
  ☑ testng-smoke.xml

Yêu cầu:
  ☑ Trigger: push [main,develop], pull_request, workflow_dispatch, schedule
  ☑ Runner: ubuntu-latest (KHÔNG phải windows-latest)
  ☑ Java: 17, temurin, cache maven
  ☑ DriverFactory: detect CI env var tự động set headless
  ☑ Artifacts upload: surefire-reports + screenshots
  ☑ if: always() → upload dù fail hay pass

Kiểm chứng:
  ☑ Push code, Actions tab xanh ✅
  ☑ Log: "[DriverFactory] Chạy Edge HEADLESS (CI mode)"
  ☑ Artifacts: test-results được lưu

Screenshot cần:
  ✓ Actions log xanh (full test pass)
  ✓ Surefire reports upload success
```

---

### ✅ TASK 2: Matrix Strategy - 1.0 điểm

```
File tạo/sửa:
  ☑ .github/workflows/task-2-matrix.yml
  ☑ testng-matrix.xml
  ☑ DriverFactory.java (hỗ trợ Chrome + Firefox)

Yêu cầu:
  ☑ Strategy: matrix.browser: [chrome, firefox]
  ☑ fail-fast: false (Chrome fail không block Firefox)
  ☑ Artifact names: test-results-${{ matrix.browser }}
  ☑ 2 job chạy song song (không tuần tự)

Kiểm chứng:
  ☑ Actions: 2 pipeline visible cùng lúc
  ☑ Timeline: Cả 2 start gần nhau, finish gần nhau
  ☑ Artifacts: test-results-chrome, test-results-firefox

Bảng so sánh (nộp trong report):
  Cấu hình    | Số browser | Thời gian | Ghi chú
  ─────────────────────────────────────────
  Tuần tự     | 1          | X phút    | Serial
  Matrix      | 2          | Y phút    | Parallel
  Speedup: X/Y times

Screenshot cần:
  ✓ Actions: 2 job visible (browser matrix)
  ✓ Timeline: Cả 2 chạy song song
  ✓ 2 artifacts riêng
```

---

### ✅ TASK 3: GitHub Secrets - 1.0 điểm

```
Yêu cầu:
  ☑ GitHub Secrets tạo:
    - SAUCEDEMO_USERNAME = "standard_user"
    - SAUCEDEMO_PASSWORD = "secret_sauce"

  ☑ Code: KHÔNG have hardcode:
    grep -r "secret_sauce" src/ → (empty) ✓
    grep -r "standard_user" src/main/ → (empty) ✓

  ☑ ConfigReader: getUsername(), getPassword()
    - Ưu tiên System.getenv("APP_USERNAME/PASSWORD")
    - Fallback: config file

  ☑ Workflow: Sử dụng ${{ secrets.SAUCEDEMO_USERNAME }}

  ☑ Bảo mật: Log MASK secret → APP_PASSWORD=***

Kiểm chứng:
  ☑ Chạy grep command → output rỗng
  ☑ GitHub Secrets visible (Settings → Secrets)
  ☑ Workflow log: masked secret
  ☑ Test pass dù credential qua env

File cần:
  ☑ .env.example (template, không commit .env)

Screenshot cần:
  ✓ GitHub Secrets page (SAUCEDEMO_* set)
  ✓ Workflow log: APP_PASSWORD=*** (masked)
  ✓ Grep output: (empty)
```

---

### ✅ TASK 4: Selenium Grid - 2.0 điểm

```
File tạo/sửa:
  ☑ docker-compose.yml (1 Hub, 2 Chrome, 1 Firefox)
  ☑ testng-grid.xml (parallel="tests", thread-count=4)
  ☑ DriverFactory.java (createRemoteDriver method)

Phần A: Khởi động Grid (0.5 điểm)
  ☑ docker-compose up -d
  ☑ docker ps → 4 container: hub + chrome + chrome + firefox
  ☑ http://localhost:4444 → Grid UI
  ☑ 3 nodes ready (Capacity: 8 sessions)

Phần B: Kết nối Framework (0.75 điểm)
  ☑ DriverFactory support -Dgrid.url=http://localhost:4444
  ☑ createRemoteDriver(browser, gridUrl)
  ☑ RemoteWebDriver session ID printed
  ☑ mvn test -Dgrid.url=... -Dbrowser=chrome
  ☑ Lúc test chạy, Grid Console hiển thị active sessions

Phần C: Hiệu suất (0.75 điểm)
  ☑ Prepare 4 test methods (hoặc 4 <test> sections)

  ☑ Test TUẦN TỰ: mvn test (disable parallel)
    → Record time: T1 = 15.2s (example)

  ☑ Test SONG SONG: mvn test -Dgrid.url=...
    → Record time: T2 = 4.6s (example)

  ☑ Tính speedup: T1/T2 = 15.2/4.6 = 3.3x

  ☑ Bảng comparison (nộp):
    | Cấu hình | Thread | Thời gian | Speedup |
    |---------|--------|----------|---------|
    | Local   | 1      | 15.2s    | 1.0x    |
    | Grid    | 4      | 4.6s     | 3.3x    |

Kiểm chứng:
  ☑ docker-compose.yml syntax đúng
  ☑ 4 container running: docker ps
  ☑ Grid UI: http://localhost:4444 ← 3 nodes visible
  ☑ Chạy test trên Grid thành công
  ☑ Grid Console show active sessions lúc test
  ☑ Speedup >= 2x (phải!)

Screenshot cần:
  ✓ Grid Console: 3 nodes đã đăng ký
  ✓ Grid Console: Active sessions lúc test chạy
  ✓ Bảng comparison timing
```

---

### ✅ TASK 5: Allure Reports - 1.0 điểm

```
File tạo/sửa:
  ☑ pom.xml: thêm allure-testng, allure-maven
  ☑ src/.../listeners/ScreenshotOnFailureListener.java

Yêu cầu:
  ☑ @Feature, @Story, @Severity, @Description trên @Test
  ☑ Allure.step() bên trong mỗi test method
  ☑ ScreenshotOnFailureListener: chụp khi fail → attach
  ☑ mvn clean test generate target/allure-results/
  ☑ mvn allure:serve → open report tự động

Kiểm chứng:
  ☑ pom.xml: allure-testng version 2.26.0+
  ☑ pom.xml: allure-maven plugin
  ☑ Compile không error: mvn compile
  ☑ Test execute: mvn clean test
  ☑ target/allure-results/ được generate
  ☑ mvn allure:serve mở browser tại http://localhost:4040
  ☑ Report hiển: Overview, Features breakdown, Step-by-step

Allure Report phải show:
  ☑ Pie chart: Total, Passed, Failed, Skipped
  ☑ Features: Nhóm test theo @Feature
  ☑ Test detail: Bấm vào → Hiển thị steps + severity
  ☑ Screenshot on failure (nếu có fail test)

Screenshot cần:
  ✓ Allure Overview page (pie chart, pass/fail % )
  ✓ Features page (phân loại theo module)
  ✓ 1 test detail: Steps hiển thị
  ✓ Severity breakdown
```

---

### ✅ TASK 6: Allure → GitHub Pages - 1.5 điểm

```
File tạo/sửa:
  ☑ .github/workflows/task-6-full-pipeline.yml
  ☑ GitHub Pages enabled

Yêu cầu:
  ☑ Workflow:
    - Jobs: test (matrix: Chrome+Firefox) + publish-report
    - Test: Generate allure-results/
    - Publish: Merge results → generate report → deploy

  ☑ GitHub Pages:
    Settings → Pages → Source: Deploy from a branch
    Branch: gh-pages, Folder: /

  ☑ URL live:
    https://<username>.github.io/<repo>/reports/

  ☑ Badge trong README.md:
    [![Allure Report](badge-url)](report-url)

Kiểm chứng:
  ☑ Workflow tạo được, syntax YAML đúng
  ☑ Push code → Actions: test + publish chạy
  ☑ publish-report phải chạy AFTER test job
  ☑ GitHub Pages branch (gh-pages) auto-create
  ☑ URL live accessible (không 404)
  ☑ Allure report hiển thị trên web (same as local)

Trigger workflow:
  ☑ push origin main
  ☑ Chờ 2-3 phút
  ☑ Xem GitHub Pages URL

Screenshot cần:
  ✓ GitHub Pages URL live (not 404)
  ✓ Allure Report trên web (overview, features)
  ✓ Badge trong README.md (tùy chọn)
```

---

### ✅ TASK 7: Test Strategy & Test Plan - 2.0 điểm

```
File tạo:
  ☑ docs/TEST_STRATEGY.md
  ☑ docs/TEST_PLAN_SPRINT_5.md

TEST STRATEGY (1.0 điểm) - >= 600 từ, 5 section bắt buộc:

  1. Phạm vi kiểm thử:
     ☑ IN SCOPE: 5 module (tài khoản, tìm kiếm, giỏ hàng, thanh toán, đơn hàng)
     ☑ OUT OF SCOPE: 2 module (admin, mobile, ...) + lý do

  2. Phân loại test & tỷ lệ:
     ☑ Distribution: 60% Unit, 28% API, 8% UI, 3% Performance, 1% Security
     ☑ Lý do chọn tỷ lệ đó (Test Pyramid)

  3. Công cụ & Framework:
     ☑ Liệt kê: Selenium, TestNG, Maven, Allure, Docker, GitHub Actions
     ☑ Giải thích tại sao chọn mỗi cái

  4. Entry/Exit Criteria:
     ☑ Entry: Code compile OK, test data ready, environment stable
     ☑ Exit: Pass rate >= 95%, 0 P1 bug, coverage >= 80%

  5. Rủi ro kiểm thử:
     ☑ >= 4 rủi ro kỹ thuật hoặc business
     ☑ Mỗi rủi ro: Description → Xác suất → Tác động → Giảm thiểu

  Bonus: Bảng lịch trình, quản lý bug, tools, communication

TEST PLAN SPRINT 5 (1.0 điểm) - Feature: VPBank Installment Payment

  1. Phân tích rủi ro kinh doanh:
     ☑ >= 5 kịch bản fail liên quan thanh toán
     ☑ Tác động, xác suất, mức độ

  2. 15 Test Cases:
     ☑ Bảng với cột: TC-ID | Title | Type | Priority | Steps | Expected
     ☑ >= 5 P1 (happy path + critical)
     ☑ >= 5 P2 (sad path, error handling)
     ☑ >= 5 P3 (edge case, boundary)

  3. Entry/Exit Criteria:
     ☑ Entry: Dev done, code merged, VPBank sandbox ready
     ☑ Exit: 95% pass, 0 P1, coverage >= 85%, PO approve

  4. Blockers & Risks:
     ☑ VPBank sandbox not stable → workaround
     ☑ Test data missing → rescue plan

  5. Resources & Scheduling:
     ☑ Phân công: QA1 (automation), QA2 (manual)
     ☑ Sprint schedule (W1: dev, W2: test)
     ☑ Effort estimate (tổng giờ)

  6. Pipeline Configuration:
     ☑ Workflow YAML: trigger trên feature/vpbank-payment branch

Kiểm chứng:
  ☑ TEST_STRATEGY.md >= 600 từ
  ☑ TEST_PLAN_SPRINT_5.md >= 1000 từ
  ☑ Đủ 5 section bắt buộc mỗi file
  ☑ 15 TC format đúng (ID, Title, Type, Priority, Expected)
  ☑ Rủi ro phân tích rõ ràng
  ☑ Criteria đo được (không write mông lung)

Screenshot/Ghi chú:
  ✓ docs/ folder structure
  ✓ Word count (validate file size)
  ✓ TC table format (clean, professional)
```

---

## 🎯 FINAL SUBMISSION STEPS

### 1. Verify tất cả files tồn tại

```bash
SeleniumFramework/
├── .github/workflows/
│   ├── selenium-ci.yml             ✅ T1
│   ├── task-2-matrix.yml           ✅ T2
│   ├── task-3-secrets.yml          ✅ T3
│   ├── task-5-allure.yml           ✅ T5
│   └── task-6-full-pipeline.yml    ✅ T6
├── docs/
│   ├── TEST_STRATEGY.md            ✅ T7
│   ├── TEST_PLAN_SPRINT_5.md       ✅ T7
│   └── LAB11_COMPLETE_GUIDE.md     (bonus)
├── src/main/java/framework/
│   ├── factory/DriverFactory.java  ✅ T2,T4
│   └── config/ConfigReader.java    ✅ T3
├── src/test/java/framework/
│   └── listeners/ScreenshotOnFailureListener.java  ✅ T5
├── docker-compose.yml              ✅ T4
├── pom.xml                         ✅ T5
├── testng-smoke.xml                ✅ T1
├── testng-matrix.xml               ✅ T2
├── testng-grid.xml                 ✅ T4
├── .env.example                    ✅ T3
└── README.md                       ✅ (updated)
```

### 2. Validate code syntax

```bash
# Compile
mvn clean compile                    # ✅ No error

# Test locally
mvn clean test -Dbrowser=edge -Denv=dev -DsuiteXmlFile=testng-smoke.xml
# ✅ Test pass

# Allure
mvn allure:serve                     # ✅ Report open
```

### 3. Test workflows locally (tùy chọn)

```bash
# Sử dụng act (local GitHub Actions runner)
# act -j run-tests

# Hoặc trigger thực tế:
git add .
git commit -m "Lab 11: Complete CI/CD + Selenium Grid + Test Strategy"
git push origin main
# → Check GitHub Actions tabs
```

### 4. Gather screenshots

Chuẩn bị 10-15 screenshot cho báo cáo:

**Task 1-2:** Actions tab (pipeline chạy)
**Task 2:** Matrix 2 jobs parallel
**Task 3:** GitHub Secrets + masked log
**Task 4:** Grid Console UI + timing table
**Task 5:** Allure Report overview + steps
**Task 6:** GitHub Pages URL live
**Task 7:** TEST_PLAN table 15 TC

### 5. Nộp bài

Repository GitHub:

```
✅ Public repo (hoặc shared with instructor)
✅ main branch có toàn bộ code + workflows + docs
✅ gh-pages branch auto-created (for Allure report)
✅ Commit message rõ ràng
✅ README.md updated với hướng dẫn
```

Document báo cáo:

```
✅ Screenshot từng task
✅ Giải thích kết quả
✅ Bảng so sánh timing (Task 2, 4)
✅ Tổng điểm: 10/10 📝
```

---

## ⚠️ COMMON MISTAKES TO AVOID

```
❌ MISTAKE 1: Chạy trên ubuntu nhưng quên --headless
   → Test fail: "cannot open display :0"
   ✓ FIX: Bật --headless=new, --no-sandbox, --disable-dev-shm-usage

❌ MISTAKE 2: Hardcode credential trong code
   → Fail security check
   ✓ FIX: Dùng GitHub Secrets + System.getenv()

❌ MISTAKE 3: Windows runner (runs-on: windows-latest)
   → Không match requirement (need Linux)
   ✓ FIX: Change to ubuntu-latest

❌ MISTAKE 4: Docker not running
   → docker-compose up fail
   ✓ FIX: Start Docker Desktop

❌ MISTAKE 5: Allure report phát generate
   → mvn allure:serve hang
   ✓ FIX: Clean target/, rerun clean test

❌ MISTAKE 6: TEST_STRATEGY/PLAN quá ngắn
   → Không đủ requirement (600+ từ)
   ✓ FIX: Viết chi tiết, phân tích sâu

❌ MISTAKE 7: Quên tạo testng-*.xml files
   → Test suite execut from default testng.xml
   ✓ FIX: Create testng-smoke.xml, testng-matrix.xml, testng-grid.xml
```

---

## 📞 SUPPORT

**Nếu gặp issue, check:**

1. Đọc error message kỹ (thường chỉ rõ nguyên nhân)
2. Check Docker logs: `docker logs <container-name>`
3. Check Maven error: `mvn clean test -e` (chi tiết)
4. Xem workflow log trên GitHub Actions
5. Kiểm tra kết nối network (firewall?)

**Nếu vẫn đóc:**

- Liên hệ instructor
- Khởi động lại Docker
- `git clean -fd` (xóa artifact rác)
- Pull code mới nhất

---

## 📊 ĐIỂM SỐ BREAKDOWN

```
Task 1: 1.5/1.5
Task 2: 1.0/1.0
Task 3: 1.0/1.0
Task 4: 2.0/2.0
Task 5: 1.0/1.0
Task 6: 1.5/1.5
Task 7: 2.0/2.0
────────────────
TỔNG: 10.0/10.0 ✅
```

**Bonus điểm (tùy chọn):**

- [ ] +0.5: Security test (OWASP scanner)
- [ ] +0.5: Performance test (JMeter)
- [ ] +0.5: Documentation clarity + professionalism

---

**Generated: 2026-03-31  
Status: Ready for Submission ✅**
