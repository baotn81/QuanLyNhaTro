package model;

import java.sql.Date;

public class PhongTro {

    private String maPhong;
    private String tenPhong;
    private double giaPhong;
    private String loaiPhong;
    private String trangThai;
    private String diaChi;
    private String moTa;

    private String maKhachThue;
    private String tenKhachThue;
    private String sdtKhachThue;

    private Date ngayBatDau;
    private Date ngayGiaHan;
    private int soThangThue;

    private String ghiChu;

    public PhongTro() {
    }

    public PhongTro(String maPhong, String tenPhong, double giaPhong,
                    String loaiPhong, String trangThai, String diaChi) {
        this.maPhong = maPhong;
        this.tenPhong = tenPhong;
        this.giaPhong = giaPhong;
        this.loaiPhong = loaiPhong;
        this.trangThai = trangThai;
        this.diaChi = diaChi;
    }

    public PhongTro(String maPhong, String tenPhong, double giaPhong, String loaiPhong,
                    String trangThai, String diaChi, String moTa, String maKhachThue,
                    String tenKhachThue, String sdtKhachThue, Date ngayBatDau,
                    Date ngayGiaHan, int soThangThue, String ghiChu) {
        this.maPhong = maPhong;
        this.tenPhong = tenPhong;
        this.giaPhong = giaPhong;
        this.loaiPhong = loaiPhong;
        this.trangThai = trangThai;
        this.diaChi = diaChi;
        this.moTa = moTa;
        this.maKhachThue = maKhachThue;
        this.tenKhachThue = tenKhachThue;
        this.sdtKhachThue = sdtKhachThue;
        this.ngayBatDau = ngayBatDau;
        this.ngayGiaHan = ngayGiaHan;
        this.soThangThue = soThangThue;
        this.ghiChu = ghiChu;
    }

    public String getMaPhong() { return maPhong; }
    public void setMaPhong(String maPhong) { this.maPhong = maPhong; }

    public String getTenPhong() { return tenPhong; }
    public void setTenPhong(String tenPhong) { this.tenPhong = tenPhong; }

    public double getGiaPhong() { return giaPhong; }
    public void setGiaPhong(double giaPhong) { this.giaPhong = giaPhong; }

    public String getLoaiPhong() { return loaiPhong; }
    public void setLoaiPhong(String loaiPhong) { this.loaiPhong = loaiPhong; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }

    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }

    public String getMaKhachThue() { return maKhachThue; }
    public void setMaKhachThue(String maKhachThue) { this.maKhachThue = maKhachThue; }

    public String getTenKhachThue() { return tenKhachThue; }
    public void setTenKhachThue(String tenKhachThue) { this.tenKhachThue = tenKhachThue; }

    public String getSdtKhachThue() { return sdtKhachThue; }
    public void setSdtKhachThue(String sdtKhachThue) { this.sdtKhachThue = sdtKhachThue; }

    public Date getNgayBatDau() { return ngayBatDau; }
    public void setNgayBatDau(Date ngayBatDau) { this.ngayBatDau = ngayBatDau; }

    public Date getNgayGiaHan() { return ngayGiaHan; }
    public void setNgayGiaHan(Date ngayGiaHan) { this.ngayGiaHan = ngayGiaHan; }

    public int getSoThangThue() { return soThangThue; }
    public void setSoThangThue(int soThangThue) { this.soThangThue = soThangThue; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }

    public boolean isPhongTrong() {
        return "Trống".equalsIgnoreCase(this.trangThai);
    }

    public long tinhTienThueConLai() {
        return Math.round(giaPhong * (soThangThue > 0 ? soThangThue : 1));
    }

    @Override
    public String toString() {
        return maPhong + " - " + tenPhong + " (" + trangThai + ")";
    }
}