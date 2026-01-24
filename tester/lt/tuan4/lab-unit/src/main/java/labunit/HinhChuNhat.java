package labunit;

public class HinhChuNhat {
    private final Diem trenTrai;  // top-left
    private final Diem duoiPhai;  // bottom-right

    public HinhChuNhat(Diem trenTrai, Diem duoiPhai) {
        // top-left: x nhỏ hơn, y lớn hơn
        if (trenTrai.x >= duoiPhai.x || trenTrai.y <= duoiPhai.y) {
            throw new IllegalArgumentException("Invalid Data");
        }
        this.trenTrai = trenTrai;
        this.duoiPhai = duoiPhai;
    }

    public double dienTich() {
        double w = duoiPhai.x - trenTrai.x;
        double h = trenTrai.y - duoiPhai.y;
        return w * h;
    }

    // Chọn quy ước: chạm cạnh vẫn tính là giao nhau
    public boolean giaoNhau(HinhChuNhat other) {
        boolean separated =
                this.duoiPhai.x < other.trenTrai.x ||   // bên trái
                other.duoiPhai.x < this.trenTrai.x ||   // bên phải
                this.duoiPhai.y > other.trenTrai.y ||   // phía trên (y nhỏ hơn là thấp hơn)
                other.duoiPhai.y > this.trenTrai.y;     // phía dưới
        return !separated;
    }

    public Diem getTrenTrai() { return trenTrai; }
    public Diem getDuoiPhai() { return duoiPhai; }
}
