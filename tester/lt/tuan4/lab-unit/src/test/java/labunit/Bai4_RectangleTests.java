package labunit;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Bai4_RectangleTests {

    @Test
    void area_correct() {
        var r = new HinhChuNhat(new Diem(0, 10), new Diem(5, 0));
        assertEquals(50.0, r.dienTich(), 1e-9);
    }

    @Test
    void invalidRectangle_throw() {
        assertThrows(IllegalArgumentException.class,
                () -> new HinhChuNhat(new Diem(5, 10), new Diem(0, 0)));
    }

    @Test
    void intersect_true_overlap() {
        var a = new HinhChuNhat(new Diem(0, 10), new Diem(5, 0));
        var b = new HinhChuNhat(new Diem(4, 6), new Diem(8, 2));
        assertTrue(a.giaoNhau(b));
    }

    @Test
    void intersect_false_disjoint() {
        var a = new HinhChuNhat(new Diem(0, 10), new Diem(5, 0));
        var b = new HinhChuNhat(new Diem(6, 6), new Diem(8, 2));
        assertFalse(a.giaoNhau(b));
    }

    @Test
    void intersect_touchEdge_true() {
        var a = new HinhChuNhat(new Diem(0, 10), new Diem(5, 0));
        var b = new HinhChuNhat(new Diem(5, 6), new Diem(8, 2)); // chạm cạnh x=5
        assertTrue(a.giaoNhau(b));
    }
}
