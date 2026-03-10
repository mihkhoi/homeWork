package lab8.bai3;

import org.testng.Assert;
import org.testng.annotations.Test;

public
class TinhTienNuocBranchCoverageTest {

    @Test(description = "TC1 - N1 True: soM3 <= 0") public void testTinhTienNuoc_SoM3KhongHopLe() {
        double actual = TinhTienNuoc.tinhTienNuoc(0, "dan_cu");
        Assert.assertEquals(
            actual,
            0.0,
            0.01,
            "soM3 <= 0 phai tra ve 0");
    }

    @Test(description = "TC2 - N1 False, N2 True: ho_ngheo") public void testTinhTienNuoc_HoNgheo() {
        double actual = TinhTienNuoc.tinhTienNuoc(5, "ho_ngheo");
        double expected = 5 * 5000;
        Assert.assertEquals(
            actual,
            expected,
            0.01,
            "Khach hang ho_ngheo phai tinh theo don gia 5000");
    }

    @Test(description = "TC3 - N1 False, N2 False, N3 True, N4 True: dan_cu <= 10m3") public void testTinhTienNuoc_DanCu_Bac1() {
        double actual = TinhTienNuoc.tinhTienNuoc(8, "dan_cu");
        double expected = 8 * 7500;
        Assert.assertEquals(
            actual,
            expected,
            0.01,
            "Dan cu <= 10m3 phai tinh theo don gia 7500");
    }

    @Test(description = "TC4 - N1 False, N2 False, N3 True, N4 False, N5 True: dan_cu 11-20m3") public void testTinhTienNuoc_DanCu_Bac2() {
        double actual = TinhTienNuoc.tinhTienNuoc(15, "dan_cu");
        double expected = 15 * 9900;
        Assert.assertEquals(
            actual,
            expected,
            0.01,
            "Dan cu 11-20m3 phai tinh theo don gia 9900");
    }

    @Test(description = "TC5 - N1 False, N2 False, N3 True, N4 False, N5 False: dan_cu > 20m3") public void testTinhTienNuoc_DanCu_Bac3() {
        double actual = TinhTienNuoc.tinhTienNuoc(25, "dan_cu");
        double expected = 25 * 11400;
        Assert.assertEquals(
            actual,
            expected,
            0.01,
            "Dan cu > 20m3 phai tinh theo don gia 11400");
    }

    @Test(description = "TC6 - N1 False, N2 False, N3 False: kinh_doanh") public void testTinhTienNuoc_KinhDoanh() {
        double actual = TinhTienNuoc.tinhTienNuoc(5, "kinh_doanh");
        double expected = 5 * 22000;
        Assert.assertEquals(
            actual,
            expected,
            0.01,
            "Khach hang kinh_doanh phai tinh theo don gia 22000");
    }
}
