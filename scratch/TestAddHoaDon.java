package scratch;

import dao.HoaDonDAO;
import model.HoaDon;
import java.sql.Date;

public class TestAddHoaDon {
    public static void main(String[] args) {
        // Initialize the connection class static block if needed
        try {
            Class.forName("database.ConnectDB");
        } catch (Exception e) {
            e.printStackTrace();
        }

        HoaDonDAO dao = new HoaDonDAO();
        HoaDon hd = new HoaDon();
        hd.setMaHD("TEST_HD_999");
        hd.setMaKhach("KH001");
        hd.setMaPhong("P01");
        hd.setThang(6);
        hd.setNam(2026);
        hd.setSoDienCu(100);
        hd.setSoDienMoi(200);
        hd.setSoNuocCu(10);
        hd.setSoNuocMoi(20);
        hd.setTienPhong(2000000);
        hd.setTienDien(350000);
        hd.setTienNuoc(180000);
        hd.setTongTien(2530000);
        hd.setTrangThai(0);
        hd.setNgayLap(new Date(System.currentTimeMillis()));

        System.out.println("Trying to insert test invoice...");
        boolean success = dao.themHoaDon(hd);
        System.out.println("Result of themHoaDon: " + success);
        
        // Clean up if it succeeded
        if (success) {
            System.out.println("Cleaning up test invoice...");
            dao.xoaHoaDon("TEST_HD_999");
        }
    }
}
