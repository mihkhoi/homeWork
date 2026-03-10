package lab8.bai7;

import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

@Feature("Order Processor") public class OrderProcessorTest {

  private
    final OrderProcessor orderProcessor = new OrderProcessor();

  private
    List<Item> items(double... prices) {
        List<Item> items = new ArrayList<>();
        for (int i = 0; i < prices.length; i++) {
            items.add(new Item("Item-" + (i + 1), prices[i]));
        }
        return items;
    }

    @Attachment(value = "Test data", type = "text/plain") public String attachData(String content) {
        return content;
    }

    // =========================
    // BASIS PATH TESTS
    // =========================

    @Test(description = "Basis Path P1 - gio hang null")
        @Story("P1 - Empty cart")
            @Severity(SeverityLevel.CRITICAL)
                @Description("Kiểm thử path D1=True khi items = null, hệ thống phải ném ngoại lệ 'Gio hang trong'") public void testBasisPath_P1_NullItems() {
        attachData("items=null, coupon=null, member=REGULAR, payment=CARD");
        Assert.assertThrows(
            IllegalArgumentException.class,
            ()->orderProcessor.calculateTotal(null, null, "REGULAR", "CARD"));
    }

    @Test(description = "Basis Path P2 - khong coupon, khong member, tong >= 500000")
        @Story("P2 - No coupon no shipping")
            @Severity(SeverityLevel.CRITICAL)
                @Description("Kiểm thử path không coupon, không giảm thành viên, total không cộng ship") public void testBasisPath_P2_NoCoupon_NoMember_NoShipping() {
        double actual = orderProcessor.calculateTotal(
            items(200000, 300000),
            null,
            "REGULAR",
            "CARD");

        attachData("subtotal=500000, expected=500000");
        Assert.assertEquals(
            actual,
            500000.0,
            0.01,
            "Tong tien 500000 thi khong duoc cong phi ship");
    }

    @Test(description = "Basis Path P3 - khong coupon, khong member, tong < 500000, online")
        @Story("P3 - Online shipping")
            @Severity(SeverityLevel.NORMAL)
                @Description("Kiểm thử path cộng phí ship online 30000 khi total < 500000") public void testBasisPath_P3_NoCoupon_OnlineShipping() {
        double actual = orderProcessor.calculateTotal(
            items(100000),
            null,
            "REGULAR",
            "CARD");

        attachData("subtotal=100000, expected=130000");
        Assert.assertEquals(
            actual,
            130000.0,
            0.01,
            "Thanh toan online va total < 500000 phai cong 30000 phi ship");
    }

    @Test(description = "Basis Path P4 - khong coupon, khong member, tong < 500000, COD")
        @Story("P4 - COD shipping")
            @Severity(SeverityLevel.NORMAL)
                @Description("Kiểm thử path cộng phí ship COD 20000 khi total < 500000") public void testBasisPath_P4_NoCoupon_CODShipping() {
        double actual = orderProcessor.calculateTotal(
            items(100000),
            null,
            "REGULAR",
            "COD");

        attachData("subtotal=100000, expected=120000");
        Assert.assertEquals(
            actual,
            120000.0,
            0.01,
            "Thanh toan COD va total < 500000 phai cong 20000 phi ship");
    }

    @Test(description = "Basis Path P5 - khong coupon, GOLD, COD")
        @Story("P5 - GOLD member")
            @Severity(SeverityLevel.NORMAL)
                @Description("Kiểm thử path member GOLD được giảm 5% rồi cộng ship COD") public void testBasisPath_P5_GoldMember_COD() {
        double actual = orderProcessor.calculateTotal(
            items(100000),
            null,
            "GOLD",
            "COD");

        // subtotal=100000, memberDiscount=5000, total=95000, +20000 = 115000
        attachData("subtotal=100000, member=GOLD, expected=115000");
        Assert.assertEquals(
            actual,
            115000.0,
            0.01,
            "GOLD phai duoc giam 5% truoc khi cong ship COD");
    }

    @Test(description = "Basis Path P6 - khong coupon, PLATINUM, online")
        @Story("P6 - PLATINUM member")
            @Severity(SeverityLevel.NORMAL)
                @Description("Kiểm thử path member PLATINUM được giảm 10% rồi cộng ship online") public void testBasisPath_P6_PlatinumMember_Online() {
        double actual = orderProcessor.calculateTotal(
            items(100000),
            null,
            "PLATINUM",
            "CARD");

        // subtotal=100000, memberDiscount=10000, total=90000, +30000 = 120000
        attachData("subtotal=100000, member=PLATINUM, expected=120000");
        Assert.assertEquals(
            actual,
            120000.0,
            0.01,
            "PLATINUM phai duoc giam 10% truoc khi cong ship online");
    }

    @Test(description = "Basis Path P7 - coupon SALE10, online")
        @Story("P7 - Coupon SALE10")
            @Severity(SeverityLevel.CRITICAL)
                @Description("Kiểm thử path coupon SALE10 giảm 10% rồi cộng phí ship online") public void testBasisPath_P7_CouponSale10() {
        double actual = orderProcessor.calculateTotal(
            items(100000),
            "SALE10",
            "REGULAR",
            "CARD");

        // subtotal=100000, discount=10000, total=90000, +30000 = 120000
        attachData("subtotal=100000, coupon=SALE10, expected=120000");
        Assert.assertEquals(
            actual,
            120000.0,
            0.01,
            "SALE10 phai giam 10% truoc khi cong ship");
    }

    @Test(description = "Basis Path P8 - coupon SALE20, GOLD, COD")
        @Story("P8 - Coupon SALE20 with GOLD")
            @Severity(SeverityLevel.CRITICAL)
                @Description("Kiểm thử path coupon SALE20 kết hợp member GOLD và ship COD") public void testBasisPath_P8_CouponSale20_Gold_COD() {
        double actual = orderProcessor.calculateTotal(
            items(100000),
            "SALE20",
            "GOLD",
            "COD");

        // subtotal=100000
        // discount=20000
        // memberDiscount=(100000-20000)*5%=4000
        // total=76000
        // +20000 COD = 96000
        attachData("subtotal=100000, coupon=SALE20, member=GOLD, expected=96000");
        Assert.assertEquals(
            actual,
            96000.0,
            0.01,
            "SALE20 + GOLD + COD phai tinh ra 96000");
    }

    @Test(description = "Basis Path P9 - coupon khong hop le")
        @Story("P9 - Invalid coupon")
            @Severity(SeverityLevel.CRITICAL)
                @Description("Kiểm thử path ném ngoại lệ khi mã giảm giá không hợp lệ") public void testBasisPath_P9_InvalidCoupon() {
        attachData("subtotal=100000, coupon=BADCODE");
        Assert.assertThrows(
            IllegalArgumentException.class,
            ()->orderProcessor.calculateTotal(
                items(100000),
                "BADCODE",
                "REGULAR",
                "CARD"));
    }

    // =========================
    // MC/DC + COVERAGE BOOSTER
    // =========================

    @Test(description = "MC/DC coupon - A=False: couponCode = null")
        @Story("MCDC - Coupon presence")
            @Severity(SeverityLevel.NORMAL)
                @Description("Chứng minh điều kiện A: couponCode != null ảnh hưởng độc lập bằng trường hợp coupon null") public void testMCDC_Coupon_A_NullCoupon() {
        double actual = orderProcessor.calculateTotal(
            items(100000),
            null,
            "REGULAR",
            "CARD");

        Assert.assertEquals(
            actual,
            130000.0,
            0.01,
            "Coupon null thi khong ap dung giam gia, chi cong phi ship online");
    }

    @Test(description = "MC/DC coupon - B=False: couponCode rong")
        @Story("MCDC - Coupon empty")
            @Severity(SeverityLevel.NORMAL)
                @Description("Chứng minh điều kiện B: !couponCode.isEmpty() ảnh hưởng độc lập bằng trường hợp coupon rỗng") public void testMCDC_Coupon_B_EmptyCoupon() {
        double actual = orderProcessor.calculateTotal(
            items(100000),
            "",
            "REGULAR",
            "CARD");

        Assert.assertEquals(
            actual,
            130000.0,
            0.01,
            "Coupon rong thi khong ap dung giam gia");
    }

    @Test(description = "MC/DC coupon - D3=True: coupon SALE10")
        @Story("MCDC - Coupon SALE10 branch")
            @Severity(SeverityLevel.NORMAL)
                @Description("Kiểm tra nhánh D3=True khi coupon là SALE10") public void testMCDC_Coupon_D3_Sale10() {
        double actual = orderProcessor.calculateTotal(
            items(100000),
            "SALE10",
            "REGULAR",
            "CARD");

        Assert.assertEquals(
            actual,
            120000.0,
            0.01,
            "Coupon SALE10 phai di vao nhanh D3=True");
    }

    @Test(description = "MC/DC coupon - D3=False, D4=True: coupon SALE20")
        @Story("MCDC - Coupon SALE20 branch")
            @Severity(SeverityLevel.NORMAL)
                @Description("Kiểm tra nhánh D3=False và D4=True khi coupon là SALE20") public void testMCDC_Coupon_D4_Sale20() {
        double actual = orderProcessor.calculateTotal(
            items(100000),
            "SALE20",
            "REGULAR",
            "CARD");

        // subtotal=100000, discount=20000, total=80000, +30000 = 110000
        Assert.assertEquals(
            actual,
            110000.0,
            0.01,
            "Coupon SALE20 phai di vao nhanh D4=True");
    }

    @Test(description = "Coverage booster - D1 second operand true: items empty")
        @Story("Coverage Booster - Empty list")
            @Severity(SeverityLevel.MINOR)
                @Description("Bổ sung test cho nhánh items.isEmpty() = true để tăng branch coverage JaCoCo") public void testCoverageBoost_EmptyList() {
        Assert.assertThrows(
            IllegalArgumentException.class,
            ()->orderProcessor.calculateTotal(
                List.of(),
                null,
                "REGULAR",
                "CARD"));
    }
}
