package model;

import java.sql.Date;

public class HopDong {

    private String maHopDong;
    private String maKhach;
    private String maPhong;
    private Date ngayBatDau;
    private Date ngayHetHan;
    private int soThang;
    private double tienCoc;
    private String trangThai;
    private String ghiChu;

    public HopDong() {}

    public HopDong(String maHopDong, String maKhach, String maPhong, Date ngayBatDau,
                   Date ngayHetHan, int soThang, double tienCoc, String trangThai, String ghiChu) {
        this.maHopDong = maHopDong;
        this.maKhach = maKhach;
        this.maPhong = maPhong;
        this.ngayBatDau = ngayBatDau;
        this.ngayHetHan = ngayHetHan;
        this.soThang = soThang;
        this.tienCoc = tienCoc;
        this.trangThai = trangThai;
        this.ghiChu = ghiChu;
    }
    public String getMaHopDong() { return maHopDong; }
    public void setMaHopDong(String maHopDong) { this.maHopDong = maHopDong; }

    public String getMaKhach() { return maKhach; }
    public void setMaKhach(String maKhach) { this.maKhach = maKhach; }

    public String getMaPhong() { return maPhong; }
    public void setMaPhong(String maPhong) { this.maPhong = maPhong; }

    public Date getNgayBatDau() { return ngayBatDau; }
    public void setNgayBatDau(Date ngayBatDau) { this.ngayBatDau = ngayBatDau; }

    public Date getNgayHetHan() { return ngayHetHan; }
    public void setNgayHetHan(Date ngayHetHan) { this.ngayHetHan = ngayHetHan; }

    public int getSoThang() { return soThang; }
    public void setSoThang(int soThang) { this.soThang = soThang; }

    public double getTienCoc() { return tienCoc; }
    public void setTienCoc(double tienCoc) { this.tienCoc = tienCoc; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }

    public boolean isHetHan() {
        return "Đã hết hạn".equalsIgnoreCase(trangThai);
    }
}