package model;

import java.sql.Date;

public class HoaDon {

    private String maHD;
    private String maKhach;
    private String maPhong;
    private int thang;
    private int nam;
    private int soDienCu;
    private int soDienMoi;
    private int soNuocCu;
    private int soNuocMoi;
    private double tienPhong;
    private double tienDien;
    private double tienNuoc;
    private double tongTien;
    // ĐÃ SỬA: Chuyển đổi từ String sang int để đồng nhất logic (1 = Đã thanh toán, 0 = Chưa thanh toán)
    private int trangThai;
    private Date ngayLap;
    private String ghiChu;

    public HoaDon() {}

    public HoaDon(String maHD, String maKhach, String maPhong, int thang, int nam,
                  int soDienCu, int soDienMoi, int soNuocCu, int soNuocMoi,
                  double tienPhong, double tienDien, double tienNuoc, double tongTien,
                  int trangThai, Date ngayLap, String ghiChu) {
        this.maHD = maHD;
        this.maKhach = maKhach;
        this.maPhong = maPhong;
        this.thang = thang;
        this.nam = nam;
        this.soDienCu = soDienCu;
        this.soDienMoi = soDienMoi;
        this.soNuocCu = soNuocCu;
        this.soNuocMoi = soNuocMoi;
        this.tienPhong = tienPhong;
        this.tienDien = tienDien;
        this.tienNuoc = tienNuoc;
        this.tongTien = tongTien;
        this.trangThai = trangThai;
        this.ngayLap = ngayLap;
        this.ghiChu = ghiChu;
    }

    public String getMaHD() { return maHD; }
    public void setMaHD(String maHD) { this.maHD = maHD; }

    public String getMaKhach() { return maKhach; }
    public void setMaKhach(String maKhach) { this.maKhach = maKhach; }

    public String getMaPhong() { return maPhong; }
    public void setMaPhong(String maPhong) { this.maPhong = maPhong; }

    public int getThang() { return thang; }
    public void setThang(int thang) { this.thang = thang; }

    public int getNam() { return nam; }
    public void setNam(int nam) { this.nam = nam; }

    public int getSoDienCu() { return soDienCu; }
    public void setSoDienCu(int soDienCu) { this.soDienCu = soDienCu; }

    public int getSoDienMoi() { return soDienMoi; }
    public void setSoDienMoi(int soDienMoi) { this.soDienMoi = soDienMoi; }

    public int getSoNuocCu() { return soNuocCu; }
    public void setSoNuocCu(int soNuocCu) { this.soNuocCu = soNuocCu; }

    public int getSoNuocMoi() { return soNuocMoi; }
    public void setSoNuocMoi(int soNuocMoi) { this.soNuocMoi = soNuocMoi; }

    public double getTienPhong() { return tienPhong; }
    public void setTienPhong(double tienPhong) { this.tienPhong = tienPhong; }

    public double getTienDien() { return tienDien; }
    public void setTienDien(double tienDien) { this.tienDien = tienDien; }

    public double getTienNuoc() { return tienNuoc; }
    public void setTienNuoc(double tienNuoc) { this.tienNuoc = tienNuoc; }

    public double getTongTien() { return tongTien; }
    public void setTongTien(double tongTien) { this.tongTien = tongTien; }

    // ĐÃ SỬA: Getter và Setter trả về kiểu int chuẩn xác
    public int getTrangThai() { return trangThai; }
    public void setTrangThai(int trangThai) { this.trangThai = trangThai; }

    public Date getNgayLap() { return ngayLap; }
    public void setNgayLap(Date ngayLap) { this.ngayLap = ngayLap; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }
}