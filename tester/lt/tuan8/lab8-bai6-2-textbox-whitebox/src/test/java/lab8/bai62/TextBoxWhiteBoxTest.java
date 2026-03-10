package lab8.bai62;

import org.testng.Assert;
import org.testng.annotations.Test;

public
class TextBoxWhiteBoxTest extends BaseTest {

    @Test(description = "Valid input - hien thi output sau khi submit") public void testValidInput_ShouldDisplayOutput() {
        TextBoxPage page = new TextBoxPage(driver);

        page.fillAndSubmit(
            "Nguyen Van A",
            "vana@example.com",
            "123 Le Loi, Q1",
            "456 Tran Hung Dao, Q5");

        Assert.assertTrue(
            page.isOutputDisplayed(),
            "Khi du lieu hop le thi output phai hien thi");

        String output = page.getOutputText();
        Assert.assertTrue(
            output.contains("Nguyen Van A"),
            "Output phai chua ho ten da nhap");
        Assert.assertTrue(
            output.contains("vana@example.com"),
            "Output phai chua email da nhap");
    }

    @Test(description = "Boundary - de trong tat ca field") public void testEmptyFields_ShouldNotDisplayMeaningfulOutput() {
        TextBoxPage page = new TextBoxPage(driver);

        page.fillAndSubmit("", "", "", "");

        Assert.assertFalse(
            page.isOutputDisplayed(),
            "Khi de trong tat ca field thi khong nen hien thi output");
    }

    @Test(description = "Boundary - name chi co khoang trang") public void testWhitespaceName_ShouldKeepWhitespaceInput() {
        TextBoxPage page = new TextBoxPage(driver);

        page.fillForm("   ", "user@example.com", "HN", "DN");

        Assert.assertEquals(
            page.getNameValue(),
            "   ",
            "Field name phai nhan dung input khoang trang de phuc vu phan tich bien");
    }

    @Test(description = "Boundary - email sai dinh dang") public void testInvalidEmail_ShouldFailBrowserValidation() {
        TextBoxPage page = new TextBoxPage(driver);

        page.fillAndSubmit(
            "Nguyen Van B",
            "invalid-email",
            "123 Current Address",
            "456 Permanent Address");

        Assert.assertFalse(
            page.isEmailFieldValidByBrowser(),
            "Email sai dinh dang phai bi browser validation danh dau la invalid");

        Assert.assertFalse(
            page.isOutputDisplayed(),
            "Khi email sai dinh dang thi output khong nen hien thi");
    }

    @Test(description = "Boundary - name chua ky tu dac biet") public void testSpecialCharactersInName_ShouldAcceptText() {
        TextBoxPage page = new TextBoxPage(driver);

        page.fillAndSubmit(
            "@@@ ### Nguyen",
            "special@example.com",
            "So 1, Duong ABC",
            "So 2, Duong XYZ");

        Assert.assertTrue(
            page.isOutputDisplayed(),
            "Name la text nen ky tu dac biet van duoc submit");

        Assert.assertTrue(
            page.getOutputText().contains("@@@ ### Nguyen"),
            "Output phai giu nguyen name co ky tu dac biet");
    }
}
