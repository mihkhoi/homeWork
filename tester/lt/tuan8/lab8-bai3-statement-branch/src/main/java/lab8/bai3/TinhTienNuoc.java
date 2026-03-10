package lab8.bai3;

public
class TinhTienNuoc {

  public
    static double tinhTienNuoc(int soM3, String loaiKhachHang) {
        if (soM3 <= 0) {
            return 0;
        }

        double donGia;

        if (loaiKhachHang.equals("ho_ngheo")) {
            donGia = 5000;
        } else if (loaiKhachHang.equals("dan_cu")) {
            if (soM3 <= 10) {
                donGia = 7500;
            } else if (soM3 <= 20) {
                donGia = 9900;
            } else {
                donGia = 11400;
            }
        } else {
            donGia = 22000;
        }

        return soM3 * donGia;
    }
}
