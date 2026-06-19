package model;

public class ThongKe {

    private int tongPhong;
    private int phongDangThue;
    private int phongTrong;
    private int tongKhachHang;
    private double tongDoanhThuThang;
    private double tongDoanhThuNam;
    private int hoaDonChuaThanhToan;
    private double tyLePhongTrong;
    private double[] doanhThu6Thang;

    public ThongKe() {
        this.doanhThu6Thang = new double[6];
    }

    public int getTongPhong() {
        return tongPhong;
    }

    public void setTongPhong(int tongPhong) {
        this.tongPhong = tongPhong;
    }

    public int getPhongDangThue() {
        return phongDangThue;
    }

    public void setPhongDangThue(int phongDangThue) {
        this.phongDangThue = phongDangThue;
    }

    public int getPhongTrong() {
        return phongTrong;
    }

    public void setPhongTrong(int phongTrong) {
        this.phongTrong = phongTrong;
    }

    public int getTongKhachHang() {
        return tongKhachHang;
    }

    public void setTongKhachHang(int tongKhachHang) {
        this.tongKhachHang = tongKhachHang;
    }

    public double getTongDoanhThuThang() {
        return tongDoanhThuThang;
    }

    public void setTongDoanhThuThang(double tongDoanhThuThang) {
        this.tongDoanhThuThang = tongDoanhThuThang;
    }

    public double getTongDoanhThuNam() {
        return tongDoanhThuNam;
    }

    public void setTongDoanhThuNam(double tongDoanhThuNam) {
        this.tongDoanhThuNam = tongDoanhThuNam;
    }

    public int getHoaDonChuaThanhToan() {
        return hoaDonChuaThanhToan;
    }

    public void setHoaDonChuaThanhToan(int hoaDonChuaThanhToan) {
        this.hoaDonChuaThanhToan = hoaDonChuaThanhToan;
    }

    public double getTyLePhongTrong() {
        return tyLePhongTrong;
    }

    public void setTyLePhongTrong(double tyLePhongTrong) {
        this.tyLePhongTrong = tyLePhongTrong;
    }

    public double[] getDoanhThu6Thang() {
        return doanhThu6Thang;
    }

    public void setDoanhThu6Thang(double[] doanhThu6Thang) {
        this.doanhThu6Thang = doanhThu6Thang;
    }

    public String getTyLePhongTrongFormat() {
        return String.format("%.1f", tyLePhongTrong) + "%";
    }

    public String getTongDoanhThuThangFormat() {
        return String.format("%,.0f", tongDoanhThuThang) + " ₫";
    }

    public String getTongDoanhThuNamFormat() {
        return String.format("%,.0f", tongDoanhThuNam) + " ₫";
    }

    @Override
    public String toString() {
        return "ThongKe{" +
                "tongPhong=" + tongPhong +
                ", phongDangThue=" + phongDangThue +
                ", phongTrong=" + phongTrong +
                ", tongKhachHang=" + tongKhachHang +
                ", tongDoanhThuThang=" + tongDoanhThuThang +
                '}';
    }
}