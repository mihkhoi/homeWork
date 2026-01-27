package lab5.bai5;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public
class RegisterValidator {

  private
    static final Pattern CUSTOMER_ID = Pattern.compile("^[a-zA-Z0-9]{6,10}$");
  private
    static final Pattern EMAIL = Pattern.compile("^[\\w.+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
  private
    static final Pattern PHONE = Pattern.compile("^0\\d{9,11}$");

    // dùng cho UNIT TEST (seedExisting)
  private
    final Set<String> existingCustomerIds = new HashSet<>();
  private
    final Set<String> existingEmails = new HashSet<>();

    // TEST dùng để nạp dữ liệu trùng
  public
    void seedExisting(String customerId, String email) {
        if (customerId != null)
            existingCustomerIds.add(customerId.trim());
        if (email != null)
            existingEmails.add(email.trim().toLowerCase());
    }

    // TEST đang gọi method này
  public
    String validate(
        String customerId, String fullName, String email,
        String phone, String address,
        String password, String confirmPassword,
        LocalDate dob, boolean acceptedTerms) {
        if (isBlank(customerId))
            return "Mã khách hàng là bắt buộc";
        if (!CUSTOMER_ID.matcher(customerId).matches())
            return "Mã KH 6-10 ký tự, chỉ chữ và số";
        if (existingCustomerIds.contains(customerId))
            return "Mã khách hàng đã tồn tại";

        if (isBlank(fullName))
            return "Họ và tên là bắt buộc";
        if (fullName.length() < 5 || fullName.length() > 50)
            return "Họ tên 5-50 ký tự";

        if (isBlank(email))
            return "Email là bắt buộc";
        String emailLower = email.trim().toLowerCase();
        if (!EMAIL.matcher(emailLower).matches())
            return "Email không hợp lệ";
        if (existingEmails.contains(emailLower))
            return "Email đã tồn tại";

        if (isBlank(phone))
            return "Số điện thoại là bắt buộc";
        if (!PHONE.matcher(phone.trim()).matches())
            return "SĐT 10-12 số, bắt đầu bằng 0";

        if (isBlank(address))
            return "Địa chỉ là bắt buộc";
        if (address.length() > 255)
            return "Địa chỉ tối đa 255 ký tự";

        if (isBlank(password))
            return "Mật khẩu là bắt buộc";
        if (password.length() < 8)
            return "Mật khẩu tối thiểu 8 ký tự";

        if (isBlank(confirmPassword))
            return "Xác nhận mật khẩu là bắt buộc";
        if (!confirmPassword.equals(password))
            return "Xác nhận mật khẩu không khớp";

        if (dob != null) {
            int years = Period.between(dob, LocalDate.now()).getYears();
            if (years < 18)
                return "Nếu nhập ngày sinh thì phải đủ 18 tuổi";
        }

        if (!acceptedTerms)
            return "Bạn phải đồng ý điều khoản dịch vụ";
        return null;
    }

  private
    boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
