package lab8.bai3;

import org.testng.Assert;
import org.testng.annotations.Test;

public
class XepLoaiBranchCoverageTest {

    @Test(description = "TC1 - N1 True: diem khong hop le") public void testXepLoai_InvalidScore() {
        String actual = XepLoai.xepLoai(11, false);
        Assert.assertEquals(
            actual,
            "Diem khong hop le",
            "Diem ngoai khoang 0..10 phai tra ve 'Diem khong hop le'");
    }

    @Test(description = "TC2 - N1 False, N2 True: xep loai Gioi") public void testXepLoai_Gioi() {
        String actual = XepLoai.xepLoai(9, false);
        Assert.assertEquals(
            actual,
            "Gioi",
            "Diem 9 phai duoc xep loai Gioi");
    }

    @Test(description = "TC3 - N1 False, N2 False, N3 True: xep loai Kha") public void testXepLoai_Kha() {
        String actual = XepLoai.xepLoai(7, false);
        Assert.assertEquals(
            actual,
            "Kha",
            "Diem 7 phai duoc xep loai Kha");
    }

    @Test(description = "TC4 - N1 False, N2 False, N3 False, N4 True: xep loai Trung Binh") public void testXepLoai_TrungBinh() {
        String actual = XepLoai.xepLoai(6, false);
        Assert.assertEquals(
            actual,
            "Trung Binh",
            "Diem 6 phai duoc xep loai Trung Binh");
    }

    @Test(description = "TC5 - N1 False, N2 False, N3 False, N4 False, N5 True: Thi lai") public void testXepLoai_ThiLai() {
        String actual = XepLoai.xepLoai(4, true);
        Assert.assertEquals(
            actual,
            "Thi lai",
            "Diem thap va coThiLai=true phai tra ve 'Thi lai'");
    }

    @Test(description = "TC6 - N1 False, N2 False, N3 False, N4 False, N5 False: Yeu - Hoc lai") public void testXepLoai_YeuHocLai() {
        String actual = XepLoai.xepLoai(4, false);
        Assert.assertEquals(
            actual,
            "Yeu - Hoc lai",
            "Diem thap va coThiLai=false phai tra ve 'Yeu - Hoc lai'");
    }
}
