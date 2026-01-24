package labunit;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Bai5_HocVienTests {

    @Test
    void scholarship_true() {
        var hv = new HocVien("1", "A", "HN", 8, 8, 8);
        assertTrue(hv.duDieuKienHocBong());
    }

    @Test
    void scholarship_false_avgBelow8() {
        var hv = new HocVien("2", "B", "HCM", 7, 8, 8);
        assertFalse(hv.duDieuKienHocBong());
    }

    @Test
    void scholarship_false_anyBelow5() {
        var hv = new HocVien("3", "C", "DN", 10, 10, 4.9);
        assertFalse(hv.duDieuKienHocBong());
    }

    @Test
    void scholarship_borderCases() {
        var ok = new HocVien("4", "D", "CT", 8, 8, 5);
        assertFalse(ok.duDieuKienHocBong()); // Change expected to false
        System.out.println("ok: " + ok + ", duDieuKienHocBong: " + ok.duDieuKienHocBong());

        var fail = new HocVien("5", "E", "CT", 8, 8, 4.999);
        assertFalse(fail.duDieuKienHocBong());
        System.out.println("fail: " + fail + ", duDieuKienHocBong: " + fail.duDieuKienHocBong());
    }
}
