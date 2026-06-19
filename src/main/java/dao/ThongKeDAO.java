package dao;

import database.ConnectDB;
import model.ThongKe;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ThongKeDAO {

    public ThongKe getThongKeTongHop() {
        ThongKe tk = new ThongKe();

        try (Connection conn = ConnectDB.getConnection()) {

            tk.setTongPhong(getTongPhong(conn));

            // ĐÃ SỬA: Đồng bộ chuỗi N'Đang Thuê' viết hoa chuẩn dữ liệu SQL Server
            tk.setPhongDangThue(getPhongTheoTrangThai(conn, "Đang Thuê"));

            tk.setPhongTrong(tk.getTongPhong() - tk.getPhongDangThue());

            tk.setTongKhachHang(getTongKhachHang(conn));

            tk.setTongDoanhThuThang(getDoanhThuThangHienTai(conn));

            tk.setTongDoanhThuNam(getDoanhThuNamHienTai(conn));

            tk.setHoaDonChuaThanhToan(getHoaDonChuaThanhToan(conn));

            tk.setTyLePhongTrong(tk.getTongPhong() > 0 ?
                    (double) tk.getPhongTrong() / tk.getTongPhong() * 100 : 0);

            tk.setDoanhThu6Thang(getDoanhThu6Thang(conn));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return tk;
    }

    private int getTongPhong(Connection conn) throws SQLException {
        String sql = "SELECT COUNT(*) FROM PhongTro";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    private int getPhongTheoTrangThai(Connection conn, String trangThai) throws SQLException {
        String sql = "SELECT COUNT(*) FROM PhongTro WHERE TrangThai = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, trangThai);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    // Đếm khách trên bảng KhachHang (nguồn dữ liệu chuẩn của ứng dụng, đồng bộ với KhachHangDAO)
    private int getTongKhachHang(Connection conn) throws SQLException {
        String sql = "SELECT COUNT(*) FROM KhachHang";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    // ĐÃ SỬA: Đổi kiểm tra TrangThai thành kiểu số (1 = Đã thanh toán)
    private double getDoanhThuThangHienTai(Connection conn) throws SQLException {
        String sql = "SELECT ISNULL(SUM(TongTien), 0) FROM HoaDon " +
                "WHERE Thang = MONTH(GETDATE()) " +
                "AND Nam = YEAR(GETDATE()) " +
                "AND TrangThai = 1";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getDouble(1) : 0;
        }
    }

    // ĐÃ SỬA: Đổi kiểm tra TrangThai thành kiểu số (1 = Đã thanh toán)
    private double getDoanhThuNamHienTai(Connection conn) throws SQLException {
        String sql = "SELECT ISNULL(SUM(TongTien), 0) FROM HoaDon " +
                "WHERE Nam = YEAR(GETDATE()) " +
                "AND TrangThai = 1";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getDouble(1) : 0;
        }
    }

    // ĐÃ SỬA: Đổi kiểm tra TrangThai thành kiểu số (0 = Chưa thanh toán)
    private int getHoaDonChuaThanhToan(Connection conn) throws SQLException {
        String sql = "SELECT COUNT(*) FROM HoaDon WHERE TrangThai = 0";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    // ĐÃ SỬA: Tối ưu thuật toán quét doanh thu 6 tháng chính xác, tự động bù số 0 nếu tháng đó trống dữ liệu
    private double[] getDoanhThu6Thang(Connection conn) throws SQLException {
        double[] data = new double[6];

        Calendar cal = Calendar.getInstance();
        int currentThang = cal.get(Calendar.MONTH) + 1;
        int currentNam = cal.get(Calendar.YEAR);

        String sql = "SELECT ISNULL(SUM(TongTien), 0) FROM HoaDon WHERE Thang = ? AND Nam = ? AND TrangThai = 1";

        // Vòng lặp tính toán lùi dần chuẩn xác 6 tháng từ quá khứ đến hiện tại (vị trí index từ 0 đến 5)
        for (int i = 0; i < 6; i++) {
            int targetThang = currentThang - (5 - i);
            int targetNam = currentNam;

            if (targetThang <= 0) {
                targetThang += 12;
                targetNam -= 1;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, targetThang);
                ps.setInt(2, targetNam);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        data[i] = rs.getDouble(1);
                    } else {
                        data[i] = 0;
                    }
                }
            }
        }
        return data;
    }

    public List<Object[]> getTopKhachThueDoanhThu() {
        return new ArrayList<>();
    }
}