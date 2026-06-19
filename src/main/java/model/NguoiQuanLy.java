package model;

import java.sql.Date;

public class NguoiQuanLy {

    private String maQL;
    private String hoTen;
    private String taiKhoan;
    private String matKhau;
    private String email;
    private String sdt;
    private String diaChi;
    private Date ngaySinh;
    private String vaiTro;
    private boolean trangThai;

    public NguoiQuanLy() {
    }

    public NguoiQuanLy(String maQL, String hoTen, String taiKhoan, String matKhau,
                       String email, String sdt) {
        this.maQL = maQL;
        this.hoTen = hoTen;
        this.taiKhoan = taiKhoan;
        this.matKhau = matKhau;
        this.email = email;
        this.sdt = sdt;
    }

    public NguoiQuanLy(String maQL, String hoTen, String taiKhoan, String matKhau,
                       String email, String sdt, String diaChi, Date ngaySinh,
                       String vaiTro, boolean trangThai) {
        this.maQL = maQL;
        this.hoTen = hoTen;
        this.taiKhoan = taiKhoan;
        this.matKhau = matKhau;
        this.email = email;
        this.sdt = sdt;
        this.diaChi = diaChi;
        this.ngaySinh = ngaySinh;
        this.vaiTro = vaiTro;
        this.trangThai = trangThai;
    }

    public String getMaQL() {
        return maQL;
    }

    public void setMaQL(String maQL) {
        this.maQL = maQL;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getTaiKhoan() {
        return taiKhoan;
    }

    public void setTaiKhoan(String taiKhoan) {
        this.taiKhoan = taiKhoan;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public Date getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(Date ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getVaiTro() {
        return vaiTro;
    }

    public void setVaiTro(String vaiTro) {
        this.vaiTro = vaiTro;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }

    @Override
    public String toString() {
        return "NguoiQuanLy{" +
                "maQL='" + maQL + '\'' +
                ", hoTen='" + hoTen + '\'' +
                ", email='" + email + '\'' +
                ", sdt='" + sdt + '\'' +
                '}';
    }
}