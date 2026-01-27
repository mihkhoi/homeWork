package lab5.bai5;

import lab5.bai5.db.CustomerRepo;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDate;

public
class RegisterService {
  private
    final RegisterValidator validator = new RegisterValidator();
  private
    final CustomerRepo repo = new CustomerRepo();

  public
    String register(
        String customerId, String fullName, String email, String phone, String address,
        String password, String confirmPassword, LocalDate dob, String gender,
        boolean acceptedTerms) {
        try {
            // ĐỔI validateBasic -> validate (đúng với RegisterValidator hiện tại)
            String err = validator.validate(
                customerId, fullName, email, phone, address,
                password, confirmPassword, dob, acceptedTerms);
            if (err != null)
                return err;

            // Unique theo đề: mã KH và email không trùng (check thật bằng DB)
            if (repo.existsCustomerId(customerId))
                return "Mã khách hàng đã tồn tại";
            if (repo.existsEmail(email))
                return "Email đã tồn tại";

            CustomerRepo.Customer c = new CustomerRepo.Customer();
            c.customerId = customerId;
            c.fullName = fullName;
            c.email = email;
            c.phone = phone;
            c.address = address;
            c.passwordHash = sha256(password); // demo học tập
            c.dob = (dob == null) ? null : dob.toString();
            c.gender = (gender == null || gender.trim().isEmpty()) ? null : gender;

            repo.insert(c);
            return null;
        } catch (Exception e) {
            return "Lỗi DB: " + e.getMessage();
        }
    }

  private
    static String sha256(String s) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(s.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : hash)
            sb.append(String.format("%02x", b));
        return sb.toString();
    }
}
