package lab8.bai61;

import org.testng.Assert;
import org.testng.annotations.Test;

public
class PhoneValidatorTest {

    @Test(description = "Valid - so bat dau bang 09 va du 10 chu so") public void testValid_09Prefix() {
        Assert.assertTrue(
            PhoneValidator.isValid("0912345678"),
            "0912345678 phai la so dien thoai hop le");
    }

    @Test(description = "Valid - so dang +84 va se duoc chuan hoa ve 0xxxxxxxxx") public void testValid_Plus84Format() {
        Assert.assertTrue(
            PhoneValidator.isValid("+84912345678"),
            "+84912345678 phai la so dien thoai hop le");
    }

    @Test(description = "Valid - cho phep khoang trang truoc khi chuan hoa") public void testValid_WithSpaces() {
        Assert.assertTrue(
            PhoneValidator.isValid("+84 912 345 678"),
            "So co khoang trang hop le sau chuan hoa phai PASS");
    }

    @Test(description = "Invalid - null") public void testInvalid_Null() {
        Assert.assertFalse(
            PhoneValidator.isValid(null),
            "null khong phai so dien thoai hop le");
    }

    @Test(description = "Invalid - rong") public void testInvalid_Empty() {
        Assert.assertFalse(
            PhoneValidator.isValid(""),
            "Chuoi rong khong phai so dien thoai hop le");
    }

    @Test(description = "Invalid - chi co khoang trang") public void testInvalid_OnlySpaces() {
        Assert.assertFalse(
            PhoneValidator.isValid("    "),
            "Chuoi chi co khoang trang khong hop le");
    }

    @Test(description = "Invalid - chua ky tu dac biet khong duoc phep") public void testInvalid_SpecialCharacter() {
        Assert.assertFalse(
            PhoneValidator.isValid("09123-45678"),
            "Dau '-' khong nam trong tap ky tu duoc phep");
    }

    @Test(description = "Invalid - sai prefix sau chuan hoa") public void testInvalid_WrongPrefix() {
        Assert.assertFalse(
            PhoneValidator.isValid("0212345678"),
            "0212345678 sai dau prefix hop le");
    }

    @Test(description = "Invalid - do dai ngan hon 10 so") public void testInvalid_ShortLength() {
        Assert.assertFalse(
            PhoneValidator.isValid("091234567"),
            "So ngan hon 10 chu so khong hop le");
    }

    @Test(description = "Invalid - do dai lon hon 10 so") public void testInvalid_LongLength() {
        Assert.assertFalse(
            PhoneValidator.isValid("09123456789"),
            "So dai hon 10 chu so khong hop le");
    }

    @Test(description = "Boundary - tuoi tho +84 nhung sau chuan hoa van sai do dai") public void testInvalid_Plus84WrongLength() {
        Assert.assertFalse(
            PhoneValidator.isValid("+849123456789"),
            "Sau chuan hoa ma khong du 10 chu so thi khong hop le");
    }

    @Test(description = "Boundary - prefix 03 hop le") public void testValid_03Prefix() {
        Assert.assertTrue(
            PhoneValidator.isValid("0312345678"),
            "0312345678 phai hop le");
    }

    @Test(description = "Boundary - prefix 07 hop le") public void testValid_07Prefix() {
        Assert.assertTrue(
            PhoneValidator.isValid("0712345678"),
            "0712345678 phai hop le");
    }
}
