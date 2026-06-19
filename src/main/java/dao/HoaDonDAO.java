package dao;

import database.ConnectDB;
import model.HoaDon;

import java.sql.*;
import java.util.ArrayList;

public class HoaDonDAO {

    public ArrayList<HoaDon> getAllHoaDon() {
        ArrayList<HoaDon> list = new ArrayList<>();
        String sql = "SELECT * FROM HoaDon ORDER BY NgayLap DESC";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapToHoaDon(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean themHoaDon(HoaDon hd) {
        String sql = "INSERT INTO HoaDon(MaHD, MaKhach, MaPhong, Thang, Nam, SoDienCu, SoDienMoi, " +
                "SoNuocCu, SoNuocMoi, TienPhong, TienDien, TienNuoc, TongTien, NgayLap, TrangThai) " +
                "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, hd.getMaHD());
            ps.setString(2, hd.getMaKhach());
            ps.setString(3, hd.getMaPhong());
            ps.setInt(4, hd.getThang());
            ps.setInt(5, hd.getNam());
            ps.setInt(6, hd.getSoDienCu());
            ps.setInt(7, hd.getSoDienMoi());
            ps.setInt(8, hd.getSoNuocCu());
            ps.setInt(9, hd.getSoNuocMoi());
            ps.setDouble(10, hd.getTienPhong());
            ps.setDouble(11, hd.getTienDien());
            ps.setDouble(12, hd.getTienNuoc());
            ps.setDouble(13, hd.getTongTien());
            // Lưu ngày lập để hóa đơn mới hiển thị đúng thứ tự (ORDER BY NgayLap DESC)
            if (hd.getNgayLap() != null) {
                ps.setDate(14, hd.getNgayLap());
            } else {
                ps.setDate(14, new java.sql.Date(System.currentTimeMillis()));
            }
            // ĐÃ SỬA: Chuyển sang setInt để lưu giá trị số (1 hoặc 0) vào Database
            ps.setInt(15, hd.getTrangThai());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean suaHoaDon(HoaDon hd) {
        String sql = "UPDATE HoaDon SET MaKhach=?, MaPhong=?, Thang=?, Nam=?, " +
                "SoDienCu=?, SoDienMoi=?, SoNuocCu=?, SoNuocMoi=?, " +
                "TienPhong=?, TienDien=?, TienNuoc=?, TongTien=?, TrangThai=? " +
                "WHERE MaHD=?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, hd.getMaKhach());
            ps.setString(2, hd.getMaPhong());
            ps.setInt(3, hd.getThang());
            ps.setInt(4, hd.getNam());
            ps.setInt(5, hd.getSoDienCu());
            ps.setInt(6, hd.getSoDienMoi());
            ps.setInt(7, hd.getSoNuocCu());
            ps.setInt(8, hd.getSoNuocMoi());
            ps.setDouble(9, hd.getTienPhong());
            ps.setDouble(10, hd.getTienDien());
            ps.setDouble(11, hd.getTienNuoc());
            ps.setDouble(12, hd.getTongTien());
            // ĐÃ SỬA: Chuyển sang setInt cho TrangThai sửa đổi
            ps.setInt(13, hd.getTrangThai());
            ps.setString(14, hd.getMaHD());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoaHoaDon(String maHD) {
        String sql = "DELETE FROM HoaDon WHERE MaHD = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maHD);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ĐÃ SỬA: Thay tham số String trangThai thành int trangThai để cập nhật real-time khi bấm nút "Thanh Toán"
    public boolean capNhatTrangThai(String maHD, int trangThai) {
        String sql = "UPDATE HoaDon SET TrangThai = ? WHERE MaHD = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, trangThai);
            ps.setString(2, maHD);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private HoaDon mapToHoaDon(ResultSet rs) throws SQLException {
        HoaDon hd = new HoaDon();
        hd.setMaHD(rs.getString("MaHD"));
        hd.setMaKhach(rs.getString("MaKhach"));
        hd.setMaPhong(rs.getString("MaPhong"));
        hd.setThang(rs.getInt("Thang"));
        hd.setNam(rs.getInt("Nam"));
        hd.setSoDienCu(rs.getInt("SoDienCu"));
        hd.setSoDienMoi(rs.getInt("SoDienMoi"));
        hd.setSoNuocCu(rs.getInt("SoNuocCu"));
        hd.setSoNuocMoi(rs.getInt("SoNuocMoi"));
        hd.setTienPhong(rs.getDouble("TienPhong"));
        hd.setTienDien(rs.getDouble("TienDien"));
        hd.setTienNuoc(rs.getDouble("TienNuoc"));
        hd.setTongTien(rs.getDouble("TongTien"));
        hd.setNgayLap(rs.getDate("NgayLap"));
        // ĐÃ SỬA: Ánh xạ dữ liệu từ SQL lên model bằng kiểu getInt
        hd.setTrangThai(rs.getInt("TrangThai"));
        return hd;
    }

    // ĐÃ SỬA: Đếm theo kiểu số (TrangThai = 0 là chưa thanh toán) cho đồng bộ với ThongKeDAO
    public int demHoaDonChuaThanhToan() {
        String sql = "SELECT COUNT(*) FROM HoaDon WHERE TrangThai = 0";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public double tinhTongDoanhThuThangHienTai() {
        String sql = "SELECT ISNULL(SUM(TongTien), 0) FROM HoaDon " +
                "WHERE Thang = MONTH(GETDATE()) AND Nam = YEAR(GETDATE()) " +
                "AND TrangThai = 1";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getDouble(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public HoaDon getHoaDonChuaThanhToanByPhongAndThang(String maPhong, int thang, int nam) {
        String sql = "SELECT * FROM HoaDon WHERE MaPhong = ? AND Thang = ? AND Nam = ? AND TrangThai = 0";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
             
            ps.setString(1, maPhong);
            ps.setInt(2, thang);
            ps.setInt(3, nam);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapToHoaDon(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}