package labunit;

public class HocVien {
    public final String maSo;
    public final String hoTen;
    public final String queQuan;
    public final double d1, d2, d3;

    public HocVien(String maSo, String hoTen, String queQuan,
                   double d1, double d2, double d3) {
        this.maSo = maSo;
        this.hoTen = hoTen;
        this.queQuan = queQuan;
        this.d1 = d1;
        this.d2 = d2;
        this.d3 = d3;
    }

    public double diemTrungBinh() {
        return (d1 + d2 + d3) / 3.0;
    }

    // ĐÚNG THEO ĐỀ: TB >= 8.0 và KHÔNG MÔN NÀO < 5  => mỗi môn >= 5
    public boolean duDieuKienHocBong() {
        return diemTrungBinh() >= 8.0
                && d1 >= 5.0
                && d2 >= 5.0
                && d3 >= 5.0;
    }
}
