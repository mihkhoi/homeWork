package lab8.bai3;

public
class XepLoai {

  public
    static String xepLoai(int diemTB, boolean coThiLai) {
        if (diemTB < 0 || diemTB > 10) {
            return "Diem khong hop le";
        }

        if (diemTB >= 8.5) {
            return "Gioi";
        } else if (diemTB >= 7.0) {
            return "Kha";
        } else if (diemTB >= 5.5) {
            return "Trung Binh";
        } else {
            if (coThiLai) {
                return "Thi lai";
            }
            return "Yeu - Hoc lai";
        }
    }
}
