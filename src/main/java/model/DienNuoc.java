package model;

import java.sql.Date;

public class DienNuoc {

    private int id;
    private String maPhong;
    private int thang;
    private int nam;
    private int chiSoDienCu;
    private int chiSoDienMoi;
    private int chiSoNuocCu;
    private int chiSoNuocMoi;
    private double tienDien;
    private double tienNuoc;
    private double tongTien;
    private Date ngayGhi;
    private String ghiChu;

    public DienNuoc() {}

    public DienNuoc(int id, String maPhong, int thang, int nam, int chiSoDienCu, int chiSoDienMoi,
                    int chiSoNuocCu, int chiSoNuocMoi, double tienDien, double tienNuoc,
                    double tongTien, Date ngayGhi, String ghiChu) {
        this.id = id;
        this.maPhong = maPhong;
        this.thang = thang;
        this.nam = nam;
        this.chiSoDienCu = chiSoDienCu;
        this.chiSoDienMoi = chiSoDienMoi;
        this.chiSoNuocCu = chiSoNuocCu;
        this.chiSoNuocMoi = chiSoNuocMoi;
        this.tienDien = tienDien;
        this.tienNuoc = tienNuoc;
        this.tongTien = tongTien;
        this.ngayGhi = ngayGhi;
        this.ghiChu = ghiChu;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getMaPhong() { return maPhong; }
    public void setMaPhong(String maPhong) { this.maPhong = maPhong; }

    public int getThang() { return thang; }
    public void setThang(int thang) { this.thang = thang; }

    public int getNam() { return nam; }
    public void setNam(int nam) { this.nam = nam; }

    public int getChiSoDienCu() { return chiSoDienCu; }
    public void setChiSoDienCu(int chiSoDienCu) { this.chiSoDienCu = chiSoDienCu; }

    public int getChiSoDienMoi() { return chiSoDienMoi; }
    public void setChiSoDienMoi(int chiSoDienMoi) { this.chiSoDienMoi = chiSoDienMoi; }

    public int getChiSoNuocCu() { return chiSoNuocCu; }
    public void setChiSoNuocCu(int chiSoNuocCu) { this.chiSoNuocCu = chiSoNuocCu; }

    public int getChiSoNuocMoi() { return chiSoNuocMoi; }
    public void setChiSoNuocMoi(int chiSoNuocMoi) { this.chiSoNuocMoi = chiSoNuocMoi; }

    public double getTienDien() { return tienDien; }
    public void setTienDien(double tienDien) { this.tienDien = tienDien; }

    public double getTienNuoc() { return tienNuoc; }
    public void setTienNuoc(double tienNuoc) { this.tienNuoc = tienNuoc; }

    public double getTongTien() { return tongTien; }
    public void setTongTien(double tongTien) { this.tongTien = tongTien; }

    public Date getNgayGhi() { return ngayGhi; }
    public void setNgayGhi(Date ngayGhi) { this.ngayGhi = ngayGhi; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }
}