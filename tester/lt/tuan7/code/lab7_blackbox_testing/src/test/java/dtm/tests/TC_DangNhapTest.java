package dtm.tests;

import dtm.base.BaseTest;
import dtm.data.DangNhapData;
import dtm.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TC_DangNhapTest extends BaseTest {

    @Test(dataProvider = "du_lieu_dang_nhap", dataProviderClass = DangNhapData.class)
    public void kiemThuDangNhap(String username, String password, String ketQuaMongDoi, String moTa) {
        getDriver().get("https://www.saucedemo.com/");

        LoginPage lp = new LoginPage(getDriver());
        lp.dangNhap(username, password);

        switch (ketQuaMongDoi) {
            case "THANH_CONG":
                Assert.assertTrue(lp.isDangOTrangSanPham(), "FAIL: " + moTa);
                break;

            case "BI_KHOA":
                Assert.assertFalse(lp.isDangOTrangSanPham(), "FAIL: " + moTa);
                Assert.assertNotNull(lp.layThongBaoLoi(), "Không có error: " + moTa);
                Assert.assertTrue(lp.layThongBaoLoi().toLowerCase().contains("locked out"), "Sai msg: " + moTa);
                break;

            case "TRUONG_TRONG":
                Assert.assertFalse(lp.isDangOTrangSanPham(), "FAIL: " + moTa);
                Assert.assertNotNull(lp.layThongBaoLoi(), "Không có error: " + moTa);
                Assert.assertTrue(lp.layThongBaoLoi().toLowerCase().contains("required"), "Sai msg: " + moTa);
                break;

            case "SAI_THONG_TIN":
                Assert.assertFalse(lp.isDangOTrangSanPham(), "FAIL: " + moTa);
                Assert.assertNotNull(lp.layThongBaoLoi(), "Không có error: " + moTa);
                Assert.assertTrue(lp.layThongBaoLoi().toLowerCase().contains("do not match"), "Sai msg: " + moTa);
                break;

            default:
                Assert.fail("Sai ketQuaMongDoi: " + ketQuaMongDoi + " | " + moTa);
        }
    }
}