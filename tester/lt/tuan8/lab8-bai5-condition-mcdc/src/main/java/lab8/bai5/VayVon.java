package lab8.bai5;

public
class VayVon {

  public
    static boolean duDieuKienVay(int tuoi, double thuNhap,
                                 boolean coTaiSanBaoLanh, int dienTinDung) {
        boolean dieuKienCoBan = (tuoi >= 22) && (thuNhap >= 10_000_000);
        boolean dieuKienBaoDam = coTaiSanBaoLanh || (dienTinDung >= 700);
        return dieuKienCoBan && dieuKienBaoDam;
    }
}
