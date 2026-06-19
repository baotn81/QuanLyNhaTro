package dao;

import database.ConnectDB;
import model.PhongTro;

import java.sql.*;
import java.util.ArrayList;

public class PhongTroDAO {

    public ArrayList<PhongTro> getAllPhongTro() {
        ArrayList<PhongTro> list = new ArrayList<>();
        String sql = "SELECT p.MaPhong, p.TenPhong, p.GiaPhong, p.LoaiPhong, p.TrangThai, p.DiaChi, p.MoTa, " +
                "       k.MaKhach AS MaKhachThue, k.HoTen AS TenKhachThue, k.SoDienThoai AS SdtKhachThue, " +
                "       h.NgayBatDau, h.NgayGiaHan, h.SoThang AS SoThangThue, h.GhiChu " +
                "FROM PhongTro p " +
                "LEFT JOIN KhachHang k ON p.MaPhong = k.MaPhongDangThue " +
                "LEFT JOIN HopDong h ON p.MaPhong = h.MaPhong AND k.MaKhach = h.MaKhach " +
                "ORDER BY p.MaPhong";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSetToPhongTro(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public boolean themPhong(PhongTro pt) {
        String sql = "INSERT INTO PhongTro(MaPhong, TenPhong, GiaPhong, LoaiPhong, TrangThai, " +
                "DiaChi, MoTa, SoNguoiToiDa) VALUES(?,?,?,?,?,?,?,?)";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, pt.getMaPhong());
            ps.setString(2, pt.getTenPhong());
            ps.setDouble(3, pt.getGiaPhong());
            ps.setString(4, pt.getLoaiPhong());
            ps.setString(5, pt.getTrangThai());
            ps.setString(6, pt.getDiaChi());
            ps.setString(7, pt.getMoTa());
            ps.setInt(8, 4); // Mặc định tối đa 4 người

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean suaPhong(PhongTro pt) {
        String sqlUpdatePhong = "UPDATE PhongTro SET TenPhong=?, GiaPhong=?, LoaiPhong=?, TrangThai=?, DiaChi=?, MoTa=? WHERE MaPhong=?";
        String sqlUpdateHopDong = "UPDATE HopDong SET GhiChu=? WHERE MaPhong=? AND TrangThai = N'Còn Hiệu Lực'";

        Connection con = null;
        try {
            con = ConnectDB.getConnection();
            con.setAutoCommit(false);

            try (PreparedStatement psP = con.prepareStatement(sqlUpdatePhong)) {
                psP.setString(1, pt.getTenPhong());
                psP.setDouble(2, pt.getGiaPhong());
                psP.setString(3, pt.getLoaiPhong());
                psP.setString(4, pt.getTrangThai());
                psP.setString(5, pt.getDiaChi());
                psP.setString(6, pt.getMoTa());
                psP.setString(7, pt.getMaPhong());
                psP.executeUpdate();
            }

            try (PreparedStatement psH = con.prepareStatement(sqlUpdateHopDong)) {
                psH.setString(1, pt.getGhiChu());
                psH.setString(2, pt.getMaPhong());
                psH.executeUpdate();
            }

            con.commit();
            return true;
        } catch (Exception e) {
            if (con != null) {
                try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (con != null) {
                try { con.setAutoCommit(true); con.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }

    public boolean xoaPhong(String maPhong) {
        String sql = "DELETE FROM PhongTro WHERE MaPhong = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maPhong);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public PhongTro timTheoMa(String maPhong) {
        String sql = "SELECT p.MaPhong, p.TenPhong, p.GiaPhong, p.LoaiPhong, p.TrangThai, p.DiaChi, p.MoTa, " +
                "       k.MaKhach AS MaKhachThue, k.HoTen AS TenKhachThue, k.SoDienThoai AS SdtKhachThue, " +
                "       h.NgayBatDau, h.NgayGiaHan, h.SoThang AS SoThangThue, h.GhiChu " +
                "FROM PhongTro p " +
                "LEFT JOIN KhachHang k ON p.MaPhong = k.MaPhongDangThue " +
                "LEFT JOIN HopDong h ON p.MaPhong = h.MaPhong AND k.MaKhach = h.MaKhach " +
                "WHERE p.MaPhong = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maPhong);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPhongTro(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public ArrayList<PhongTro> timKiemPhong(String keyword) {
        ArrayList<PhongTro> list = new ArrayList<>();
        String sql = "SELECT p.MaPhong, p.TenPhong, p.GiaPhong, p.LoaiPhong, p.TrangThai, p.DiaChi, p.MoTa, " +
                "       k.MaKhach AS MaKhachThue, k.HoTen AS TenKhachThue, k.SoDienThoai AS SdtKhachThue, " +
                "       h.NgayBatDau, h.NgayGiaHan, h.SoThang AS SoThangThue, h.GhiChu " +
                "FROM PhongTro p " +
                "LEFT JOIN KhachHang k ON p.MaPhong = k.MaPhongDangThue " +
                "LEFT JOIN HopDong h ON p.MaPhong = h.MaPhong AND k.MaKhach = h.MaKhach " +
                "WHERE p.MaPhong LIKE ? OR p.TenPhong LIKE ? OR p.DiaChi LIKE ? OR k.HoTen LIKE ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            String search = "%" + keyword + "%";
            ps.setString(1, search);
            ps.setString(2, search);
            ps.setString(3, search);
            ps.setString(4, search);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToPhongTro(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    private PhongTro mapResultSetToPhongTro(ResultSet rs) throws SQLException {
        PhongTro pt = new PhongTro();

        pt.setMaPhong(rs.getString("MaPhong"));
        pt.setTenPhong(rs.getString("TenPhong"));
        pt.setGiaPhong(rs.getDouble("GiaPhong"));
        pt.setLoaiPhong(rs.getString("LoaiPhong"));
        pt.setTrangThai(rs.getString("TrangThai"));
        pt.setDiaChi(rs.getString("DiaChi"));
        pt.setMoTa(rs.getString("MoTa") != null ? rs.getString("MoTa") : "Không có mô tả");

        pt.setMaKhachThue(rs.getString("MaKhachThue") != null ? rs.getString("MaKhachThue") : "---");
        pt.setTenKhachThue(rs.getString("TenKhachThue") != null ? rs.getString("TenKhachThue") : "Phòng trống");
        pt.setSdtKhachThue(rs.getString("SdtKhachThue") != null ? rs.getString("SdtKhachThue") : "---");

        pt.setNgayBatDau(rs.getDate("NgayBatDau"));
        pt.setNgayGiaHan(rs.getDate("NgayGiaHan"));
        pt.setSoThangThue(rs.getInt("SoThangThue"));
        pt.setGhiChu(rs.getString("GhiChu") != null ? rs.getString("GhiChu") : "---");

        return pt;
    }

    public int demPhongTheoTrangThai(String trangThai) {
        String sql = "SELECT COUNT(*) FROM PhongTro WHERE TrangThai = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, trangThai);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int tongSoPhong() {
        String sql = "SELECT COUNT(*) FROM PhongTro";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Lấy danh sách các phòng đang ở trạng thái "Trống" để hiển thị cho chọn khi thêm khách.
     */
    public ArrayList<PhongTro> getPhongTrong() {
        ArrayList<PhongTro> list = new ArrayList<>();
        String sql = "SELECT MaPhong, TenPhong, GiaPhong, LoaiPhong, TrangThai, DiaChi, MoTa " +
                "FROM PhongTro WHERE TrangThai = N'Trống' ORDER BY MaPhong";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                PhongTro pt = new PhongTro();
                pt.setMaPhong(rs.getString("MaPhong"));
                pt.setTenPhong(rs.getString("TenPhong"));
                pt.setGiaPhong(rs.getDouble("GiaPhong"));
                pt.setLoaiPhong(rs.getString("LoaiPhong"));
                pt.setTrangThai(rs.getString("TrangThai"));
                pt.setDiaChi(rs.getString("DiaChi"));
                pt.setMoTa(rs.getString("MoTa") != null ? rs.getString("MoTa") : "");
                list.add(pt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Cập nhật trạng thái phòng (ví dụ: "Trống" -> "Đang ở" hoặc ngược lại).
     */
    public boolean capNhatTrangThaiPhong(String maPhong, String trangThai) {
        String sql = "UPDATE PhongTro SET TrangThai = ? WHERE MaPhong = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, trangThai);
            ps.setString(2, maPhong);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}