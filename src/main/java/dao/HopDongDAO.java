package dao;

import database.ConnectDB;
import model.HopDong;

import java.sql.*;
import java.util.ArrayList;

public class HopDongDAO {

    public ArrayList<HopDong> getAllHopDong() {
        ArrayList<HopDong> list = new ArrayList<>();
        String sql = "SELECT * FROM HopDong ORDER BY NgayHetHan";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapToHopDong(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean themHopDong(HopDong hd) {
        String sql = "INSERT INTO HopDong(MaHopDong, MaKhach, MaPhong, NgayBatDau, NgayHetHan, SoThang, TienCoc, TrangThai, GhiChu) " +
                "VALUES(?,?,?,?,?,?,?,?,?)";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, hd.getMaHopDong());
            ps.setString(2, hd.getMaKhach());
            ps.setString(3, hd.getMaPhong());
            ps.setDate(4, hd.getNgayBatDau());
            ps.setDate(5, hd.getNgayHetHan());
            ps.setInt(6, hd.getSoThang());
            ps.setDouble(7, hd.getTienCoc());
            ps.setString(8, hd.getTrangThai());
            ps.setString(9, hd.getGhiChu());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean giaHanHopDong(String maHopDong, int soThangThem, String ghiChuMoi) {

        String sql = "UPDATE HopDong " +
                "SET NgayHetHan = DATEADD(MONTH, ?, NgayHetHan), " +
                "SoThang = SoThang + ?, " +
                "TrangThai = N'Còn Hiệu Lực', " +
                "GhiChu = ? " +
                "WHERE MaHopDong = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, soThangThem);
            ps.setInt(2, soThangThem);
            ps.setString(3, ghiChuMoi);
            ps.setString(4, maHopDong);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Lỗi gia hạn hợp đồng (3 tham số): " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean giaHanHopDong(String maKhach, int soThangThem) {
        String sql = "UPDATE HopDong " +
                "SET NgayHetHan = DATEADD(MONTH, ?, NgayHetHan), " +
                "SoThang = SoThang + ?, " +
                "TrangThai = N'Còn Hiệu Lực', " +
                "GhiChu = CONCAT(ISNULL(GhiChu,''), ' - Gia hạn +', ?, ' tháng') " +
                "WHERE MaKhach = ? AND TrangThai = N'Còn Hiệu Lực'";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, soThangThem);
            ps.setInt(2, soThangThem);
            ps.setInt(3, soThangThem);
            ps.setString(4, maKhach);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public HopDong timTheoMaKhach(String maKhach) {
        String sql = "SELECT * FROM HopDong WHERE MaKhach = ? AND TrangThai = N'Còn Hiệu Lực'";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maKhach);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapToHopDong(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean xoaHopDong(String maHopDong) {
        String sql = "DELETE FROM HopDong WHERE MaHopDong = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maHopDong);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private HopDong mapToHopDong(ResultSet rs) throws SQLException {
        HopDong hd = new HopDong();
        hd.setMaHopDong(rs.getString("MaHopDong"));
        hd.setMaKhach(rs.getString("MaKhach"));
        hd.setMaPhong(rs.getString("MaPhong"));
        hd.setNgayBatDau(rs.getDate("NgayBatDau"));
        hd.setNgayHetHan(rs.getDate("NgayHetHan"));
        hd.setSoThang(rs.getInt("SoThang"));
        hd.setTienCoc(rs.getDouble("TienCoc"));
        hd.setTrangThai(rs.getString("TrangThai"));
        hd.setGhiChu(rs.getString("GhiChu"));
        return hd;
    }
}