package model;

import java.sql.Date;

public class KhachHang {

    private String maKhach;
    private String hoTen;
    private String sdt;
    private String cmnd;
    private String email;
    private String diaChi;
    private String maPhongDangThue;
    private Date ngayBatDauThue;
    private Date ngayHetHan;
    private String trangThai;
    private String ghiChu;

    public KhachHang() {
    }

    public KhachHang(String maKhach, String hoTen, String sdt, String cmnd,
                     String email, String diaChi) {
        this.maKhach = maKhach;
        this.hoTen = hoTen;
        this.sdt = sdt;
        this.cmnd = cmnd;
        this.email = email;
        this.diaChi = diaChi;
    }

    public KhachHang(String maKhach, String hoTen, String sdt, String cmnd, String email,
                     String diaChi, String maPhongDangThue, Date ngayBatDauThue,
                     Date ngayHetHan, String trangThai, String ghiChu) {
        this.maKhach = maKhach;
        this.hoTen = hoTen;
        this.sdt = sdt;
        this.cmnd = cmnd;
        this.email = email;
        this.diaChi = diaChi;
        this.maPhongDangThue = maPhongDangThue;
        this.ngayBatDauThue = ngayBatDauThue;
        this.ngayHetHan = ngayHetHan;
        this.trangThai = trangThai;
        this.ghiChu = ghiChu;
    }

    public String getMaKhach() {
        return maKhach;
    }

    public void setMaKhach(String maKhach) {
        this.maKhach = maKhach;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getCmnd() {
        return cmnd;
    }

    public void setCmnd(String cmnd) {
        this.cmnd = cmnd;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getMaPhongDangThue() {
        return maPhongDangThue;
    }

    public void setMaPhongDangThue(String maPhongDangThue) {
        this.maPhongDangThue = maPhongDangThue;
    }

    public Date getNgayBatDauThue() {
        return ngayBatDauThue;
    }

    public void setNgayBatDauThue(Date ngayBatDauThue) {
        this.ngayBatDauThue = ngayBatDauThue;
    }

    public Date getNgayHetHan() {
        return ngayHetHan;
    }

    public void setNgayHetHan(Date ngayHetHan) {
        this.ngayHetHan = ngayHetHan;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    // ==================== METHOD HỖ TRỢ ====================
    public boolean dangThuePhong() {
        return maPhongDangThue != null && !maPhongDangThue.trim().isEmpty();
    }

    @Override
    public String toString() {
        return maKhach + " - " + hoTen;
    }
}