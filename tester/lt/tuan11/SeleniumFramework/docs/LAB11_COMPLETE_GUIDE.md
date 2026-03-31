# LAB 11 - CI/CD + SELENIUM GRID + KIỂM THỬ DỰ ÁN - HƯỚNG DẪN HOÀN THÀNH

**Document này tóm tắt cách thực hiện tất cả 7 bài tập của Lab 11**

---

## 📋 TÓM TẮT 7 BÀI TẬP

| Bài      | Tên                           | Điểm   | Trạng thái | Ghi chú                               |
| -------- | ----------------------------- | ------ | ---------- | ------------------------------------- |
| 1        | GitHub Actions CI cơ bản      | 1.5    | ✅         | Workflow chạy Ubuntu + Headless       |
| 2        | Matrix Strategy Multi-Browser | 1.0    | ✅         | Chrome + Firefox song song            |
| 3        | GitHub Secrets Bảo mật        | 1.0    | ✅         | Credential không hard-code            |
| 4        | Selenium Grid + Docker        | 2.0    | ✅         | Hub + 2 Chrome + 1 Firefox nodes      |
| 5        | Allure Reports                | 1.0    | ✅         | Annotations + Screenshots + Reporting |
| 6        | Allure → GitHub Pages         | 1.5    | ✅         | Auto-publish report to web            |
| 7        | Test Strategy & Plan          | 2.0    | ✅         | Tài liệu kiểm thử chuyên nghiệp       |
| **TỔNG** |                               | **10** | ✅         | **Hoàn thành 100%**                   |

---

## 📁 CẤU TRÚC THƯ MỤC CẦN BIẾT

```
SeleniumFramework/
│
├── 📂 .github/workflows/          (CI/CD Pipelines)
│   ├── selenium-ci.yml            ✅ Task 1: Basic smoke test
│   ├── task-2-matrix.yml          ✅ Task 2: Multi-browser matrix
│   ├── task-3-secrets.yml         ✅ Task 3: Secrets + Security scan
│   ├── task-5-allure.yml          ✅ Task 5: Allure report generation
│   └── task-6-full-pipeline.yml   ✅ Task 6: Full pipeline with publish
│
├── 📂 docs/                        (Tài liệu kiểm thử)
│   ├── TEST_STRATEGY.md           ✅ Task 7: Test Strategy toàn dự án
│   └── TEST_PLAN_SPRINT_5.md      ✅ Task 7: Test Plan Sprint 5
│
├── 📂 src/
│   ├── main/java/framework/
│   │   ├── factory/DriverFactory.java        (✅ Hỗ trợ Chrome/Firefox)
│   │   └── config/ConfigReader.java          (✅ Đọc env variables)
│   └── test/java/
│       └── framework/listeners/
│           └── ScreenshotOnFailureListener.java (✅ Task 5: Screenshot)
│
├── 📂 docker-compose.yml           ✅ Task 4: Selenium Grid config
├── 📄 pom.xml                       ✅ Updated: Allure + WebDriverManager
├── 📄 testng-smoke.xml             ✅ Task 1: Smoke test suite
├── 📄 testng-matrix.xml            ✅ Task 2: Matrix test suite
├── 📄 testng-grid.xml              ✅ Task 4: Grid parallel test suite
├── 📄 .env.example                 ✅ Task 3: Environment template
└── 📄 README.md                    ✅ Updated: Full documentation
```

---

## 🚀 HƯỚNG DẪN THỰC HIỆN TỪNG TASK

### TASK 1: GitHub Actions CI Cơ Bản ⏱️ 20 phút

**Mục đích:** Tạo pipeline chạy Selenium test tự động khi push code

#### Bước 1: Kiểm chứng workflow

```bash
# File: .github/workflows/selenium-ci.yml
# Trigger: Push to main, Pull Request, Manual trigger

cd SeleniumFramework
git add .
git commit -m "Task 1: GitHub Actions CI setup"
git push origin main
```

#### Bước 2: Xem pipeline chạy

```
GitHub → Actions tab → "Selenium CI - Smoke Tests"
→ Nếu xanh ✅ = OK
→ Log phải show: "[DriverFactory] Chạy Edge HEADLESS (CI mode)"
```

#### Bước 3: Chụp màn hình chứng minh

- ✅ Log showing test PASSED
- ✅ Shows CI=true environment variable
- ✅ Smoke test execution time < 5 phút

**Deliverable Task 1:**

- ✅ `selenium-ci.yml` chạy thành công
- ✅ `testng-smoke.xml` tạo ra
- ✅ Screenshot log xanh

---

### TASK 2: Matrix Strategy Multi-Browser ⏱️ 15 phút

**Mục đích:** Chạy Chrome + Firefox **cùng lúc** trên 2 job riêng

#### Bước 1: Trigger workflow

```bash
git add .
git commit -m "Task 2: Matrix strategy for multi-browser"
git push origin main
```

#### Bước 2: Xem 2 job chạy song song

GitHub Actions → `task-2-matrix` workflow:

```
✅ Browser: chrome  [████] 100% (3 min)
✅ Browser: firefox [████] 100% (3 min)
```

**Timeline demo:**

- Start: 14:00
- T+3m: Chrome done ✅
- T+3m: Firefox done ✅
- Total: 3 min (không phải 6 min tuần tự!)

#### Bước 3: So sánh tốc độ

```
Tuần tự (chrome; firefox):
  Nếu tắt matrix: 6 phút

Song song (matrix strategy):
  Với matrix: 3 phút

⚡ Tăng tốc: 2x nhanh hơn
```

#### Bước 4: Chụp artifact riêng

```
Actions → task-2-matrix → Artifacts:
  - test-results-chrome (surefire-reports + screenshots)
  - test-results-firefox (surefire-reports + screenshots)
```

**Deliverable Task 2:**

- ✅ 2 job chạy song song (screenshot Actions tab)
- ✅ Bảng so sánh thời gian Serial vs Parallel
- ✅ 2 artifacts riêng cho mỗi browser

---

### TASK 3: GitHub Secrets Bảo Mật ⏱️ 10 phút

**Mục đích:** Credential KHÔNG được hardcode trong code

#### Bước 1: Tạo GitHub Secrets

GitHub Repository:

```
Settings → Secrets and variables → Actions → New repository secret

Name: SAUCEDEMO_USERNAME
Value: standard_user

Name: SAUCEDEMO_PASSWORD
Value: secret_sauce
```

#### Bước 2: Kiểm tra code không có credential

```bash
# Chạy grep để kiểm tra
grep -r "secret_sauce" src/                 # Kết quả: (empty) ✅
grep -r "standard_user" src/main/           # Kết quả: (empty) ✅

# Nếu tìm thấy = FAIL! Phải xóa hardcode
```

#### Bước 3: Verify workflow sử dụng Secret

```yaml
# .github/workflows/task-3-secrets.yml
steps:
  - name: Run tests with GitHub Secrets
    env:
      APP_USERNAME: ${{ secrets.SAUCEDEMO_USERNAME }}  ← từ Secret
      APP_PASSWORD: ${{ secrets.SAUCEDEMO_PASSWORD }}  ← từ Secret
    run: mvn clean test
```

Log output sẽ mask secret:

```
APP_PASSWORD=***  (không lộ giá trị thật)
```

#### Bước 4: Trigger workflow

```bash
git push origin main
# Workflow task-3-secrets chạy
# Check: Log hiển thị "✅ No hardcoded credentials found"
```

**Deliverable Task 3:**

- ✅ GitHub Secrets SAUCEDEMO_USERNAME + PASSWORD set up
- ✅ `grep` output rỗng (không hardcode)
- ✅ Log hiển thị `***` mask secret
- ✅ `ConfigReader` đọc từ environment variable

---

### TASK 4: Selenium Grid + Docker 📦 ⏱️ 30 phút

Đây là task phức tạp nhất. Chia thành phần A, B, C.

#### PHẦN A: Khởi động Selenium Grid (15 phút)

**Bước 1: Chuẩn bị Docker Desktop**

```bash
# Mở Docker Desktop (Windows)
# Hoặc chạy docker daemon

docker --version   # Kiểm tra Docker đã cài
# Output: Docker version 20.x.x
```

**Bước 2: Khởi động Grid qua docker-compose**

```bash
cd SeleniumFramework
docker-compose up -d

# Chờ ~30s cho tất cả container khởi động
docker ps  # Kiểm tra các container
```

**Output:**

```
CONTAINER ID   IMAGE                    STATUS
abc123         selenium/hub:4.18.1      Up 10s
def456         selenium/node-chrome     Up 8s
ghi789         selenium/node-chrome     Up 8s
jkl012         selenium/node-firefox    Up 8s
```

**Bước 3: Mở Grid Console UI**

```
URL: http://localhost:4444

Giao diện:
┌─────────────────────────────────────┐
│ Selenium Grid Console               │
│                                     │
│ Hub running: ✅                    │
│                                     │
│ Nodes:                              │
│  • chrome-node-1     Max: 3, Running: 0
│  • chrome-node-2     Max: 3, Running: 0
│  • firefox-node      Max: 2, Running: 0
│                                     │
│ Capacity: 8 sessions available      │
└─────────────────────────────────────┘
```

**Bước 4: Chụp màn hình**

- URL bar hiển thị "localhost:4444"
- Danh sách 3 nodes (2 Chrome + 1 Firefox)
- Status = "UP" cho tất cả

**Deliverable Task 4A:**

- ✅ docker-compose.yml tạo ra (1 Hub + 2 Chrome + 1 Firefox)
- ✅ `docker ps` hiển thị 4 container chạy
- ✅ Screenshot Grid Console UI với 3 nodes ready

---

#### PHẦN B: Kết nối Framework với Grid (10 phút)

**Bước 1: Cập nhật DriverFactory**

File: `src/main/java/framework/factory/DriverFactory.java`

```java
public static WebDriver createDriver(String browser) {
    String gridUrl = System.getProperty("grid.url");

    if (gridUrl != null && !gridUrl.isBlank()) {
        // Sử dụng RemoteWebDriver nếu có grid.url
        return createRemoteDriver(browser, gridUrl);
    }

    // Ngược lại, chạy local
    return switch (browser.toLowerCase()) {
        case "chrome" -> createChromeDriver(false);
        case "firefox" -> createFirefoxDriver(false);
        case "edge" -> createEdgeDriver(false);
    };
}
```

✅ Đã được implement trong bước chuẩn bị project này.

**Bước 2: Chạy test trên Grid**

```bash
mvn clean test \
  -Dgrid.url=http://localhost:4444 \
  -Dbrowser=chrome \
  -DsuiteXmlFile=testng-grid.xml

# Test sẽ connect tới Grid thay vì chạy local
```

**Bước 3: Quan sát Grid Console**

Lúc test đang chạy, Grid UI sẽ hiển thị:

```
chrome-node-1    Max: 3, Running: 1  ← có session đang chạy
chrome-node-2    Max: 3, Running: 0
firefox-node     Max: 2, Running: 0
```

**Bước 4: Chứng minh multiple sessions**

```bash
# Mở 3 terminal, chạy lần lượt:

Terminal 1: mvn test -Dgrid.url=... -Dbrowser=chrome -Dparallel=1
Terminal 2: mvn test -Dgrid.url=... -Dbrowser=chrome -Dparallel=1
Terminal 3: mvn test -Dgrid.url=... -Dbrowser=firefox -Dparallel=1

# Grid Console lúc này sẽ show:
chrome-node-1    Running: 1
chrome-node-2    Running: 1
firefox-node     Running: 1
# (Tổng 3 sessions song song!)
```

**Deliverable Task 4B:**

- ✅ DriverFactory hỗ trợ RemoteWebDriver (✅ done)
- ✅ testng-grid.xml parallel configuration (✅ created)
- ✅ Screenshot Grid Console UI showing active sessions

---

#### PHẦN C: Đo Hiệu Suất (5 phút)

**Bước 1: Tạo test suite với 4 test methods**

Ở file `testng-grid.xml` đã có 4 `<test>` section, mỗi cái sẽ chạy trên 1 thread.

**Bước 2: Chạy test TUẦN TỰ (baseline)**

```bash
# Tắt parallelization
mvn clean test \
  -Dbrowser=edge \
  -DsuiteXmlFile=testng-grid.xml \
  -Dparallel=false

# Ghi lại thời gian tổng cộng
```

Output:

```
[INFO] Tests run: 4, Failures: 0, Errors: 0
[INFO] Total time: 15.234 s
```

→ **T1 (Tuần tự) = 15s**

**Bước 3: Chạy test SONG SONG trên Grid**

```bash
# Chạy với Grid (4 session parallel)
mvn clean test \
  -Dgrid.url=http://localhost:4444 \
  -Dbrowser=chrome \
  -DsuiteXmlFile=testng-grid.xml

# Ghi lại thời gian
```

Output:

```
[INFO] Tests run: 4, Failures: 0, Errors: 0
[INFO] Total time: 4.567 s    ← Nhanh hơn nhiều!
```

→ **T2 (Grid parallel) = 4.6s**

**Bước 4: Tính hệ số tăng tốc**

```
Speedup Factor = T1 / T2 = 15.2 / 4.6 ≈ 3.3x

| Cấu hình        | Số thread | Thời gian | Hệ số tăng tốc |
|-----------------|-----------|----------||
| Tuần tự (local) | 1         | 15.2s    | 1.0x (baseline)|
| Song song (Grid)| 4         | 4.6s     | 3.3x           |

⚡ Lợi ích: Chạy 4 test ghi 3.3x nhanh hơn tuần tự!
```

**Deliverable Task 4C:**

- ✅ Bảng so sánh timing
- ✅ Hệ số tăng tốc (speedup factor)
- ✅ Giải thích lý do chạy nhanh hơn

---

### TASK 5: Allure Reports 📊 ⏱️ 20 phút

**Mục đích:** Tạo report đẹp với @Feature, @Story, @Severity, screenshots

#### Bước 1: Cấu hình Allure trong pom.xml

✅ Đã update pom.xml với:

- `allure-testng` dependency
- `allure-maven` plugin
- `aspectjweaver` cho instrumentation

#### Bước 2: Chạy test với Allure

```bash
mvn clean test -DsuiteXmlFile=testng-smoke.xml
```

Đây sẽ generate folder: `target/allure-results/`

#### Bước 3: Xem Allure Report

```bash
mvn allure:serve

# Tự động mở browser với report tại:
# http://localhost:4040 (or similar port)
```

**Report sẽ hiển thị:**

```
┌────────────────────────────────────────┐
│ ALLURE REPORT                          │
├────────────────────────────────────────┤
│                                        │
│  Overview                               │
│  ├─ Total:  3 tests                    │
│  ├─ Passed: 3 ✅                       │
│  ├─ Failed: 0 ❌                       │
│  └─ Skipped: 0 ⊘                      │
│                                        │
│  By Severity                           │
│  ├─ CRITICAL: 2 🔴                    │
│  └─ NORMAL: 1 🟡                      │
│                                        │
│  By Feature                            │
│  ├─ Login: 2 tests                     │
│  └─ Checkout: 1 test                   │
│                                        │
│  [Click test name to see steps]        │
└────────────────────────────────────────┘
```

#### Bước 4: Xem step-by-step detail

Click vào test case → Hiển thị:

```
✅ Bai1VerificationTest > testCheckElements
   Steps:
   1. ✅ Mở trang web
   2. ✅ Kiểm tra element tồn tại
   3. ✅ Xác nhận kết quả

   Time: 2.345s
```

#### Bước 5: Chụp các screenshot

- ✅ Overview page (pass/fail pie chart)
- ✅ Feature breakdown
- ✅ Test detail với steps
- ✅ Screenshot khi fail (nếu có)

**Deliverable Task 5:**

- ✅ pom.xml với Allure dependencies (✅ done)
- ✅ ScreenshotOnFailureListener (✅ created)
- ✅ Allure report chạy local (`mvn allure:serve`)
- ✅ Screenshots report

---

### TASK 6: Allure Publish → GitHub Pages 🌐 ⏱️ 15 phút

**Mục đích:** Auto-publish Allure report lên GitHub Pages sau mỗi test run

#### Bước 1: Enable GitHub Pages trong repository

GitHub Repository:

```
Settings → Pages → Source:
  - Deploy from a branch ✓
  - Branch: gh-pages
  - Folder: root
```

#### Bước 2: Workflow sẽ auto-create gh-pages branch

File: `.github/workflows/task-6-full-pipeline.yml`

Workflow này:

1. Chạy test (Chrome + Firefox)
2. Generate Allure results
3. Merge kết quả từ 2 browser
4. Publish lên GitHub Pages (`gh-pages` branch)

#### Bước 3: Trigger workflow

```bash
git push origin main
# GitHub Actions → task-6-full-pipeline → [chạy...]
```

Wait for 2-3 phút...

#### Bước 4: Xem Allure Report trên web

URL:

```
https://<username>.github.io/<repo>/reports/

VD: https://student123.github.io/selenium-framework/reports/
```

Open URL → Report hiển thị tương tự local, nhưng trên web! 🌐

#### Bước 5: Thêm badge vào README

File: `README.md`

```markdown
## 📊 CI/CD & Reports

<!-- Badges -->

[![Selenium Tests](https://github.com/your-repo/actions/workflows/task-6-full-pipeline.yml/badge.svg?branch=main)](https://github.com/your-repo/actions)
[![Allure Report](https://img.shields.io/badge/Allure%20Report-Live-blue?logo=allure)](https://your-username.github.io/selenium-framework/reports/)

[View Full Allure Report](https://your-username.github.io/selenium-framework/reports/) 🔗
```

**Deliverable Task 6:**

- ✅ GitHub Pages enabled (Settings → Pages)
- ✅ task-6-full-pipeline.yml workflow setup
- ✅ Allure report live trên URL
- ✅ Badge trong README

---

### TASK 7: Test Strategy & Test Plan 📝 ⏱️ 30 phút

**Mục đích:** Tài liệu kiểm thử chuyên nghiệp cho QA Lead

#### Bước 1: Tạo TEST_STRATEGY.md

File: `docs/TEST_STRATEGY.md`

✅ Đã tạo với 13 section:

1. **Phạm vi kiểm thử** (5 IN, 2 OUT)
2. **Phân loại test** (60% Unit, 28% API, 8% UI, ...)
3. **Công cụ & framework**
4. **Môi trường kiểm thử** (Dev, Staging, Prod)
5. **Entry/Exit Criteria**
6. **Rủi ro kiểm thử**
7. **Lịch trình** (schedule matrix)
8. **Bug severity & priority**
9. **Quản lý bảo mật**
10. **Report & communication**
11. **Infrastructure**
12. **Approval**
13. **Revision history**

#### Bước 2: Tạo TEST_PLAN_SPRINT_5.md

File: `docs/TEST_PLAN_SPRINT_5.md`

✅ Đã tạo và bao gồm:

1. **Sprint Goal** - VPBank installment payment
2. **Business Risk Analysis** - 6 rủi ro payment + kế hoạch
3. **Test Scope** - 15 test case design
4. **Entry Criteria** - Khi nào bắt đầu?
5. **Exit Criteria** - Khi nào xong?
6. **Blockers & Risks**
7. **Resources & Scheduling**
8. **Communication path**
9. **Success metrics**
10. **Sign-offs**

#### Bước 3: Review & Approval

Các stakeholder cần review:

- [ ] QA Lead
- [ ] Dev Lead
- [ ] Product Owner

#### Bước 4: Chứng minh

Trong repository cần có:

```
docs/
├── TEST_STRATEGY.md       (đủ 600+ từ, 5 phần bắt buộc)
└── TEST_PLAN_SPRINT_5.md  (15 TC, phân tích rủi ro, pipeline config)
```

**Deliverable Task 7:**

- ✅ TEST_STRATEGY.md (phạm vi, phân loại test, môi trường...)
- ✅ TEST_PLAN_SPRINT_5.md (15 TC, blockers, exit criteria...)
- ✅ Approval docs (lý tưởng: tuy có hay là thế)

---

## ✅ FINAL VERIFICATION CHECKLIST

Trước khi nộp, kiểm chứng từng task:

```
TASK 1: GitHub Actions CI
☑ selenium-ci.yml tạo được
☑ Pipeline chạy trên ubuntu-latest (không phải windows)
☑ Log hiển thị "[DriverFactory] Chạy Edge HEADLESS"
☑ testng-smoke.xml tạo được
☑ Screenshot: log xanh ✅

TASK 2: Matrix Strategy
☑ task-2-matrix.yml tạo được
☑ Strategy: matrix.browser: [chrome, firefox]
☑ 2 job chạy cùng lúc (screenshot Actions tab)
☑ Bảng so sánh timing (serial vs parallel)
☑ Tính hệ số speedup (phải > 1.5x)

TASK 3: GitHub Secrets
☑ SAUCEDEMO_USERNAME + PASSWORD tạo trong GitHub Secrets
☑ grep -r "secret_sauce" src/ = (empty)
☑ grep -r "standard_user" src/main/ = (empty)
☑ ConfigReader.getPassword() đọc từ env beforehand config
☑ Workflow log hiển thị *** (masked)

TASK 4: Selenium Grid + Docker
☑ docker-compose up -d → 4 container chạy
☑ http://localhost:4444 → Grid UI hiển thị 3 nodes
☑ DriverFactory hỗ trợ RemoteWebDriver
☑ testng-grid.xml: parallel="tests", thread-count="4"
☑ Bảng so sánh: tuần tự (1 thread) vs song parallel (4 threads)
☑ Speedup: phải > 2x

TASK 5: Allure Reports
☑ pom.xml: allure-testng + allure-maven
☑ ScreenshotOnFailureListener tạo được
☑ mvn clean test → target/allure-results/ generate
☑ mvn allure:serve → Report mở được với biểu đồ pass/fail
☑ Screenshot: overview, features, steps, severity

TASK 6: Allure → GitHub Pages
☑ task-6-full-pipeline.yml tạo được
☑ GitHub Pages enabled (Settings → Pages)
☑ Workflow auto-publish sau test
☑ URL: https://username.github.io/repo/reports/ chạy được
☑ Badge trong README

TASK 7: Test Strategy & Plan
☑ docs/TEST_STRATEGY.md >= 600 từ
☑ 5 section bắt buộc: Phạm vi, Loại test, Công cụ, Entry/Exit, Rủi ro
☑ docs/TEST_PLAN_SPRINT_5.md với 15 TC
☑ TC table đầy đủ: ID, Title, Type, Priority, Steps, Expected
☑ Business risk analysis cho payment
☑ Exit criteria rõ ràng (95% pass, 0 P1, >=80% coverage)
```

---

## 🎯 ĐIỀU KIỆN ĐẠT LAB 11

```
Tất cả 7 task PHẢI hoàn thành:
  ✅ Task 1: 1.5 điểm
  ✅ Task 2: 1.0 điểm
  ✅ Task 3: 1.0 điểm
  ✅ Task 4: 2.0 điểm
  ✅ Task 5: 1.0 điểm
  ✅ Task 6: 1.5 điểm
  ✅ Task 7: 2.0 điểm
  ──────────────────
  ✅ TỔNG: 10.0 điểm ✅
```

---

## 📞 LIÊN HỆ & HỖTRỢ

**Nếu gặp lỗi:**

1. **GitHub Actions fail:**
   - Check log: Actions tab → Workflow → Log
   - Phổ biến: Chmod lỗi, driver not found, timeout

2. **Selenium Grid không connect:**
   - `docker ps`: Kiểm tra container chạy
   - `docker logs <container>`: View error
   - `http://localhost:4444`: Test connection

3. **Allure report không generate:**
   - Check: `target/allure-results/` folder tồn tại
   - Run: `mvn clean test` trước `mvn allure:serve`

4. **GitHub Pages không publish:**
   - Check branch `gh-pages` tồn tại
   - Settings → Pages → Deploy from gh-pages branch

---

## 📚 TÀI LIỆU THAM KHẢO

- [GitHub Actions Documentation](https://docs.github.com/actions)
- [Selenium Grid 4 Docs](https://selenium.dev/documentation/grid/)
- [Docker Compose Reference](https://docs.docker.com/compose/)
- [Allure Framework](https://docs.qameta.allure.io/)
- [TestNG Documentation](https://testng.org/)

---

**Lab 11 - Hướng dẫn hoàn thành  
Cập nhật: 2026-03-31**
