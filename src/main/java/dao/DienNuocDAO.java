package dao;

import database.ConnectDB;
import model.DienNuoc;
import java.sql.*;
import java.util.ArrayList;

public class DienNuocDAO {

    public ArrayList<DienNuoc> getAllDienNuoc() {
        ArrayList<DienNuoc> list = new ArrayList<>();
        String sql = "SELECT * FROM DienNuoc ORDER BY NgayGhi DESC";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapToDienNuoc(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean themDienNuoc(DienNuoc dn) {
        String sql = "INSERT INTO DienNuoc(MaPhong, Thang, Nam, ChiSoDienCu, ChiSoDienMoi, " +
                "ChiSoNuocCu, ChiSoNuocMoi, TienDien, TienNuoc, TongTien, GhiChu) " +
                "VALUES(?,?,?,?,?,?,?,?,?,?,?)";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, dn.getMaPhong());
            ps.setInt(2, dn.getThang());
            ps.setInt(3, dn.getNam());
            ps.setInt(4, dn.getChiSoDienCu());
            ps.setInt(5, dn.getChiSoDienMoi());
            ps.setInt(6, dn.getChiSoNuocCu());
            ps.setInt(7, dn.getChiSoNuocMoi());
            ps.setDouble(8, dn.getTienDien());
            ps.setDouble(9, dn.getTienNuoc());
            ps.setDouble(10, dn.getTongTien());
            ps.setString(11, dn.getGhiChu());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean suaDienNuoc(DienNuoc dn) {
        String sql = "UPDATE DienNuoc SET MaPhong=?, Thang=?, Nam=?, ChiSoDienCu=?, ChiSoDienMoi=?, " +
                "ChiSoNuocCu=?, ChiSoNuocMoi=?, TienDien=?, TienNuoc=?, TongTien=?, GhiChu=? " +
                "WHERE Id=?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, dn.getMaPhong());
            ps.setInt(2, dn.getThang());
            ps.setInt(3, dn.getNam());
            ps.setInt(4, dn.getChiSoDienCu());
            ps.setInt(5, dn.getChiSoDienMoi());
            ps.setInt(6, dn.getChiSoNuocCu());
            ps.setInt(7, dn.getChiSoNuocMoi());
            ps.setDouble(8, dn.getTienDien());
            ps.setDouble(9, dn.getTienNuoc());
            ps.setDouble(10, dn.getTongTien());
            ps.setString(11, dn.getGhiChu());
            ps.setInt(12, dn.getId());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoaDienNuoc(int id) {
        String sql = "DELETE FROM DienNuoc WHERE Id = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private DienNuoc mapToDienNuoc(ResultSet rs) throws SQLException {
        DienNuoc dn = new DienNuoc();
        dn.setId(rs.getInt("Id"));
        dn.setMaPhong(rs.getString("MaPhong"));
        dn.setThang(rs.getInt("Thang"));
        dn.setNam(rs.getInt("Nam"));
        dn.setChiSoDienCu(rs.getInt("ChiSoDienCu"));
        dn.setChiSoDienMoi(rs.getInt("ChiSoDienMoi"));
        dn.setChiSoNuocCu(rs.getInt("ChiSoNuocCu"));
        dn.setChiSoNuocMoi(rs.getInt("ChiSoNuocMoi"));
        dn.setTienDien(rs.getDouble("TienDien"));
        dn.setTienNuoc(rs.getDouble("TienNuoc"));
        dn.setTongTien(rs.getDouble("TongTien"));
        dn.setNgayGhi(rs.getDate("NgayGhi"));
        dn.setGhiChu(rs.getString("GhiChu"));
        return dn;
    }

    public DienNuoc getDienNuocByPhongAndThang(String maPhong, int thang, int nam) {
        String sql = "SELECT * FROM DienNuoc WHERE MaPhong = ? AND Thang = ? AND Nam = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
             
            ps.setString(1, maPhong);
            ps.setInt(2, thang);
            ps.setInt(3, nam);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapToDienNuoc(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}