package dao;

import database.ConnectDB;
import model.NguoiQuanLy;

import java.sql.*;
import java.util.ArrayList;

public class ChuTroDAO {

    public NguoiQuanLy dangNhap(String maQL, String matKhau) {

        String sql = "SELECT * FROM NguoiQuanLy WHERE MaQL = ? AND Password = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maQL);
            ps.setString(2, matKhau);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapToNguoiQuanLy(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public NguoiQuanLy timTheoMaHoacEmail(String input) {

        String sql = "SELECT * FROM NguoiQuanLy WHERE MaQL = ? OR Email = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, input);
            ps.setString(2, input);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                NguoiQuanLy ql = mapToNguoiQuanLy(rs);

                System.out.println("========== DEBUG ==========");
                System.out.println("MaQL      : " + ql.getMaQL());
                System.out.println("HoTen     : " + ql.getHoTen());
                System.out.println("Username  : " + ql.getTaiKhoan());
                System.out.println("Password  : " + ql.getMatKhau());
                System.out.println("Email     : " + ql.getEmail());
                System.out.println("SDT       : " + ql.getSdt());
                System.out.println("===========================");

                return ql;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean capNhatMatKhau(String maQL, String matKhauMoi) {

        String sql = "UPDATE NguoiQuanLy SET Password = ? WHERE MaQL = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, matKhauMoi);
            ps.setString(2, maQL);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public ArrayList<NguoiQuanLy> getAll() {

        ArrayList<NguoiQuanLy> list = new ArrayList<>();

        String sql = "SELECT * FROM NguoiQuanLy";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                list.add(mapToNguoiQuanLy(rs));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean themNguoiQuanLy(NguoiQuanLy ql) {

        String sql =
                "INSERT INTO NguoiQuanLy " +
                        "(MaQL, HoTen, Username, Password, Email, SoDienThoai) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, ql.getMaQL());
            ps.setString(2, ql.getHoTen());
            ps.setString(3, ql.getTaiKhoan());
            ps.setString(4, ql.getMatKhau());
            ps.setString(5, ql.getEmail());
            ps.setString(6, ql.getSdt());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    private NguoiQuanLy mapToNguoiQuanLy(ResultSet rs) throws SQLException {

        NguoiQuanLy ql = new NguoiQuanLy();

        ql.setMaQL(rs.getString("MaQL"));
        ql.setHoTen(rs.getString("HoTen"));
        ql.setTaiKhoan(rs.getString("Username"));
        ql.setMatKhau(rs.getString("Password"));
        ql.setEmail(rs.getString("Email"));
        ql.setSdt(rs.getString("SoDienThoai"));

        return ql;
    }

}