package lab8.bai5;

import org.testng.Assert;
import org.testng.annotations.Test;

public
class VayVonMCDCTest {

    @Test(description = "MC/DC - Base case: du dieu kien co ban, khong co tai san nhung diem tin dung >= 700") public void testMCDC_BaseCase_DuocVay() {
        boolean actual = VayVon.duDieuKienVay(22, 10_000_000, false, 700);

        Assert.assertTrue(
            actual,
            "Base case hop le phai duoc vay");
    }

    @Test(description = "MC/DC - Chung minh tuoi anh huong doc lap: tuoi < 22") public void testMCDC_TuoiDocLap_ThapHon22() {
        boolean actual = VayVon.duDieuKienVay(21, 10_000_000, false, 700);

        Assert.assertFalse(
            actual,
            "Khi tuoi < 22 thi khong du dieu kien vay");
    }

    @Test(description = "MC/DC - Chung minh thu nhap anh huong doc lap: thu nhap < 10 trieu") public void testMCDC_ThuNhapDocLap_ThapHon10Tr() {
        boolean actual = VayVon.duDieuKienVay(22, 9_000_000, false, 700);

        Assert.assertFalse(
            actual,
            "Khi thu nhap < 10 trieu thi khong du dieu kien vay");
    }

    @Test(description = "MC/DC - Chung minh tai san bao lanh anh huong doc lap: co tai san, diem tin dung < 700") public void testMCDC_TaiSanBaoLanhDocLap_CoTaiSan() {
        boolean actual = VayVon.duDieuKienVay(22, 10_000_000, true, 650);

        Assert.assertTrue(
            actual,
            "Co tai san bao lanh hop le thi van duoc vay du diem tin dung < 700");
    }

    @Test(description = "MC/DC - Chung minh diem tin dung anh huong doc lap: khong co tai san, diem tin dung < 700") public void testMCDC_DiemTinDungDocLap_Duoi700() {
        boolean actual = VayVon.duDieuKienVay(22, 10_000_000, false, 650);

        Assert.assertFalse(
            actual,
            "Khong co tai san va diem tin dung < 700 thi khong du dieu kien vay");
    }
}
