package dtm.data;

import org.testng.annotations.DataProvider;

public class DangNhapData {

    @DataProvider(name = "du_lieu_dang_nhap")
    public Object[][] getData() {
        return new Object[][]{
                {"standard_user", "secret_sauce", "THANH_CONG", "Valid standard_user"},
                {"problem_user", "secret_sauce", "THANH_CONG", "Valid problem_user"},
                {"performance_glitch_user", "secret_sauce", "THANH_CONG", "Valid performance_glitch_user"},
                {"error_user", "secret_sauce", "THANH_CONG", "Valid error_user"},

                {"locked_out_user", "secret_sauce", "BI_KHOA", "Locked user"},

                {"no_user_123", "no_pass_123", "SAI_THONG_TIN", "Non-existing user"},

                {"", "secret_sauce", "TRUONG_TRONG", "Empty username"},
                {"standard_user", "", "TRUONG_TRONG", "Empty password"},
                {"", "", "TRUONG_TRONG", "Empty both"},

                {" standard_user", "secret_sauce", "SAI_THONG_TIN", "Leading space"},
                {"standard_user ", "secret_sauce", "SAI_THONG_TIN", "Trailing space"},
                {"sta@ndard", "secret_sauce", "SAI_THONG_TIN", "Special char"},

                {null, "secret_sauce", "TRUONG_TRONG", "Null username"},
                {"standard_user", null, "TRUONG_TRONG", "Null password"},
                {null, null, "TRUONG_TRONG", "Null both"},

                // (tùy chọn) Ép fail để sinh screenshots
                // {"standard_user", "sai_pass", "THANH_CONG", "EP FAIL - tạo screenshot"},
        };
    }
}