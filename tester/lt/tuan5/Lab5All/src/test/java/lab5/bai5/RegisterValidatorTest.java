
package lab5.bai5;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

public
class RegisterValidatorTest {

  private
    RegisterValidator v;

    @Before public void setup() {
        v = new RegisterValidator();
        v.seedExisting("ABC123", "old@email.com");
    }

    @Test public void ok_register() {
        String err = v.validate(
            "KH0012",
            "Nguyễn Văn A",
            "a1@email.com",
            "0123456789",
            "HCM",
            "12345678",
            "12345678",
            LocalDate.of(2000, 1, 1),
            true);
        assertNull(err);
    }

    @Test public void fail_duplicate_customerId() {
        String err = v.validate(
            "ABC123",
            "Nguyễn Văn A",
            "new@email.com",
            "0123456789",
            "HCM",
            "12345678",
            "12345678",
            null,
            true);
        assertEquals("Mã KH đã tồn tại", err);
    }

    @Test public void fail_phone_format() {
        String err = v.validate(
            "KH0012",
            "Nguyễn Văn A",
            "new@email.com",
            "12345",
            "HCM",
            "12345678",
            "12345678",
            null,
            true);
        assertEquals("SĐT 10-12 số, bắt đầu 0", err);
    }

    @Test public void fail_under18() {
        LocalDate dob = LocalDate.now().minusYears(17);
        String err = v.validate(
            "KH0012",
            "Nguyễn Văn A",
            "new@email.com",
            "0123456789",
            "HCM",
            "12345678",
            "12345678",
            dob,
            true);
        assertEquals("Chưa đủ 18 tuổi", err);
    }
}
