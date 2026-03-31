# Selenium Framework - ShopEasy Lab 11

Automated testing framework sử dụng **Selenium WebDriver 4.18**, **TestNG**, **Maven**, và **Allure Reports**.

## Tính năng

- ✅ POM (Page Object Model) pattern
- ✅ Multi-browser support (Edge, Chrome, Firefox)
- ✅ Headless mode cho CI/CD
- ✅ Selenium Grid integration
- ✅ Allure Reports with screenshots
- ✅ GitHub Actions CI/CD pipeline
- ✅ Docker + docker-compose for Grid
- ✅ Configuration management (dev/staging/prod)

## Cách chạy local

### Chạy tất cả smoke tests

```bash
mvn clean test -Dbrowser=edge -Denv=dev -DsuiteXmlFile=testng-smoke.xml
```

### Chạy toàn bộ test suite

```bash
mvn clean test -Dbrowser=edge -Denv=dev
```

### Chạy test trên Grid

```bash
# 1. Khởi động Grid (nếu chưa chạy)
docker-compose up -d

# 2. Chạy test trên Grid
mvn clean test -Dgrid.url=http://localhost:4444 -Dbrowser=edge -DsuiteXmlFile=testng-grid.xml
```

### Xem Allure Report

```bash
# Chạy test trước
mvn clean test

# Mở Allure report
mvn allure:serve
```

## Cấu trúc project

```
SeleniumFramework/
├── src/
│   ├── main/java/framework/
│   │   ├── base/BasePage.java
│   │   ├── config/ConfigReader.java
│   │   ├── factory/DriverFactory.java
│   │   └── utils/ScreenshotUtil.java
│   └── test/java/
│       ├── base/BaseTest.java
│       ├── tests/Bai1VerificationTest.java
│       └── resources/config-dev.properties
├── drivers/msedgedriver.exe  # Local driver
├── .github/workflows/selenium-ci.yml
├── docker-compose.yml
├── pom.xml
├── testng.xml
├── testng-smoke.xml
└── testng-grid.xml
```

## Biến môi trường & Configuration

| Biến       | Ví dụ                 | Mô tả                          |
| ---------- | --------------------- | ------------------------------ |
| `browser`  | edge, chrome, firefox | Browser cần test               |
| `env`      | dev, staging, prod    | Environment                    |
| `grid.url` | http://localhost:4444 | Selenium Grid endpoint         |
| `CI`       | true                  | Tự động set bởi GitHub Actions |

## GitHub Secrets (cho CI/CD)

Cần thiết lập trong GitHub Repository Settings:

- `SAUCEDEMO_USERNAME` - Tài khoản test app
- `SAUCEDEMO_PASSWORD` - Mật khẩu test app

## CI/CD Pipeline

- **Trigger**: Push to main/develop, Pull Request, Manual workflow_dispatch
- **Environment**: Ubuntu Linux (GitHub-hosted)
- **Jobs**:
  - Smoke test (mỗi commit/PR)
  - Regression test (hàng đêm)
  - Grid parallel test (manual trigger)
- **Report**: Allure Report publish to GitHub Pages

## Docker + Selenium Grid

```bash
# Khởi động Grid with docker-compose
docker-compose up -d

# Kiểm tra Grid Console
http://localhost:4444

# Dừng Grid
docker-compose down
```

## Lab 11 Tasks

| Task                           | Điểm | Trạng thái |
| ------------------------------ | ---- | ---------- |
| Task 1: GitHub Actions CI      | 1.5  | ✅         |
| Task 2: Matrix Browser         | 1.0  | ⏳         |
| Task 3: GitHub Secrets         | 1.0  | ⏳         |
| Task 4: Selenium Grid          | 2.0  | ⏳         |
| Task 5: Allure Report          | 1.0  | ⏳         |
| Task 6: Allure to GitHub Pages | 1.5  | ⏳         |
| Task 7: Test Strategy & Plan   | 2.0  | ⏳         |

---

**Thực hiện**: Sinh viên Lab 11  
**Ngày**: 2026-03-31
