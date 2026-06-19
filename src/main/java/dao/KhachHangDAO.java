package dao;

import database.ConnectDB;
import model.KhachHang;

import java.sql.*;
import java.util.ArrayList;

public class KhachHangDAO {

    public ArrayList<KhachHang> getAllKhachHang() {

        ArrayList<KhachHang> list = new ArrayList<>();

        String sql = "SELECT * FROM KhachHang ORDER BY MaKhach";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSetToKhachHang(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean themKhachHang(KhachHang kh) {

        String sql = "INSERT INTO KhachHang(MaKhach,HoTen,SoDienThoai,CMND,Email,DiaChi,"
                + "MaPhongDangThue,NgayBatDauThue,NgayHetHan,TrangThai,GhiChu)"
                + " VALUES(?,?,?,?,?,?,?,?,?,?,?)";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, kh.getMaKhach());
            ps.setString(2, kh.getHoTen());
            ps.setString(3, kh.getSdt());
            ps.setString(4, kh.getCmnd());
            ps.setString(5, kh.getEmail());
            ps.setString(6, kh.getDiaChi());
            ps.setString(7, kh.getMaPhongDangThue());
            
            if (kh.getNgayBatDauThue() != null) {
                ps.setDate(8, kh.getNgayBatDauThue());
            } else {
                ps.setNull(8, java.sql.Types.DATE);
            }
            
            if (kh.getNgayHetHan() != null) {
                ps.setDate(9, kh.getNgayHetHan());
            } else {
                ps.setNull(9, java.sql.Types.DATE);
            }
            
            ps.setString(10, kh.getTrangThai());
            
            if (kh.getGhiChu() != null) {
                ps.setString(11, kh.getGhiChu());
            } else {
                ps.setNull(11, java.sql.Types.NVARCHAR);
            }

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean suaKhachHang(KhachHang kh) {

        String sql = "UPDATE KhachHang SET "
                + "HoTen=?,"
                + "SoDienThoai=?,"
                + "CMND=?,"
                + "Email=?,"
                + "DiaChi=?,"
                + "MaPhongDangThue=?,"
                + "NgayBatDauThue=?,"
                + "NgayHetHan=?,"
                + "TrangThai=?,"
                + "GhiChu=? "
                + "WHERE MaKhach=?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, kh.getHoTen());
            ps.setString(2, kh.getSdt());
            ps.setString(3, kh.getCmnd());
            ps.setString(4, kh.getEmail());
            ps.setString(5, kh.getDiaChi());
            ps.setString(6, kh.getMaPhongDangThue());
            
            if (kh.getNgayBatDauThue() != null) {
                ps.setDate(7, kh.getNgayBatDauThue());
            } else {
                ps.setNull(7, java.sql.Types.DATE);
            }
            
            if (kh.getNgayHetHan() != null) {
                ps.setDate(8, kh.getNgayHetHan());
            } else {
                ps.setNull(8, java.sql.Types.DATE);
            }
            
            ps.setString(9, kh.getTrangThai());
            
            if (kh.getGhiChu() != null) {
                ps.setString(10, kh.getGhiChu());
            } else {
                ps.setNull(10, java.sql.Types.NVARCHAR);
            }
            
            ps.setString(11, kh.getMaKhach());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean xoaKhachHang(String maKhach) {

        String sql = "DELETE FROM KhachHang WHERE MaKhach=?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maKhach);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public KhachHang timTheoMa(String maKhach) {

        String sql = "SELECT * FROM KhachHang WHERE MaKhach=?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maKhach);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToKhachHang(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public KhachHang timTheoEmail(String email) {

        String sql = "SELECT * FROM KhachHang WHERE Email=?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToKhachHang(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean emailTonTai(String email) {

        String sql = "SELECT MaKhach FROM KhachHang WHERE Email=?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);

            return ps.executeQuery().next();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public KhachHang timTheoPhong(String maPhong) {

        String sql = "SELECT * FROM KhachHang WHERE MaPhongDangThue=?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maPhong);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToKhachHang(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public ArrayList<KhachHang> timKiemKhach(String keyword) {

        ArrayList<KhachHang> list = new ArrayList<>();

        String sql = "SELECT * FROM KhachHang "
                + "WHERE MaKhach LIKE ? "
                + "OR HoTen LIKE ? "
                + "OR SoDienThoai LIKE ? "
                + "OR Email LIKE ? "
                + "OR CMND LIKE ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            String s = "%" + keyword + "%";

            ps.setString(1, s);
            ps.setString(2, s);
            ps.setString(3, s);
            ps.setString(4, s);
            ps.setString(5, s);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapResultSetToKhachHang(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public int demKhachDangThue() {

        String sql = "SELECT COUNT(*) FROM KhachHang WHERE MaPhongDangThue IS NOT NULL";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
    public int tongKhachHang() {

        String sql = "SELECT COUNT(*) FROM KhachHang";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    private KhachHang mapResultSetToKhachHang(ResultSet rs) throws SQLException {

        KhachHang kh = new KhachHang();

        kh.setMaKhach(rs.getString("MaKhach"));
        kh.setHoTen(rs.getString("HoTen"));
        kh.setSdt(rs.getString("SoDienThoai"));
        kh.setCmnd(rs.getString("CMND"));
        kh.setEmail(rs.getString("Email"));
        kh.setDiaChi(rs.getString("DiaChi"));
        kh.setMaPhongDangThue(rs.getString("MaPhongDangThue"));
        kh.setNgayBatDauThue(rs.getDate("NgayBatDauThue"));
        kh.setNgayHetHan(rs.getDate("NgayHetHan"));
        kh.setTrangThai(rs.getString("TrangThai"));
        kh.setGhiChu(rs.getString("GhiChu"));

        return kh;
    }

}