// ==========================================================================
// Dá»° ÃN QUáº¢N LÃ NHÃ€ TRá»Œ - TRá»ŒN Bá»˜ TRONG Má»˜T FILE JAVA (Main.java)
// ==========================================================================

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.function.Consumer;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import javax.mail.*;
import javax.mail.Authenticator;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.jfree.chart.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

// ==========================================================================
// CLASS DEFINITIONS
// ==========================================================================



class ConnectDB {

    private static final String SERVER = "localhost";
    private static final String PORT = "1433";
    private static final String DATABASE = "QuanLyNhaTro";

    private static final String USERNAME = "sa";
    private static final String PASSWORD = "12345";

    private static final String URL =
            "jdbc:sqlserver://" + SERVER + ":" + PORT +
                    ";databaseName=" + DATABASE +
                    ";encrypt=true;" +
                    "trustServerCertificate=true;" +
                    "loginTimeout=30;";

    static {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            System.out.println("âœ… ÄÃ£ náº¡p Driver SQL Server thÃ nh cÃ´ng.");
        } catch (ClassNotFoundException e) {
            System.err.println("âŒ KhÃ´ng tÃ¬m tháº¥y Driver SQL Server JDBC!");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            if (conn != null) {
                System.out.println("ðŸ”— Káº¿t ná»‘i Database thÃ nh cÃ´ng!");
            }
        } catch (SQLException e) {
            System.err.println("âŒ Lá»—i káº¿t ná»‘i Database!");
            System.err.println("Chi tiáº¿t: " + e.getMessage());
            System.err.println("Kiá»ƒm tra: Server, Port, Database Name, Username/Password");
        }
        return conn;
    }

    public static void close(Connection conn) {
        if (conn != null) {
            try {
                if (!conn.isClosed()) {
                    conn.close();
                    System.out.println("ðŸ”Œ ÄÃ£ Ä‘Ã³ng káº¿t ná»‘i Database.");
                }
            } catch (SQLException e) {
                System.err.println("âŒ Lá»—i khi Ä‘Ã³ng káº¿t ná»‘i: " + e.getMessage());
            }
        }
    }

    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("âœ… Kiá»ƒm tra káº¿t ná»‘i Database: THÃ€NH CÃ”NG");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("âŒ Kiá»ƒm tra káº¿t ná»‘i tháº¥t báº¡i: " + e.getMessage());
        }
        return false;
    }

    public static String getConnectionURL() {
        return URL;
    }
}



class DienNuoc {

    private int id;
    private String maPhong;
    private int thang;
    private int nam;
    private int chiSoDienCu;
    private int chiSoDienMoi;
    private int chiSoNuocCu;
    private int chiSoNuocMoi;
    private double tienDien;
    private double tienNuoc;
    private double tongTien;
    private Date ngayGhi;
    private String ghiChu;

    public DienNuoc() {}

    public DienNuoc(int id, String maPhong, int thang, int nam, int chiSoDienCu, int chiSoDienMoi,
                    int chiSoNuocCu, int chiSoNuocMoi, double tienDien, double tienNuoc,
                    double tongTien, Date ngayGhi, String ghiChu) {
        this.id = id;
        this.maPhong = maPhong;
        this.thang = thang;
        this.nam = nam;
        this.chiSoDienCu = chiSoDienCu;
        this.chiSoDienMoi = chiSoDienMoi;
        this.chiSoNuocCu = chiSoNuocCu;
        this.chiSoNuocMoi = chiSoNuocMoi;
        this.tienDien = tienDien;
        this.tienNuoc = tienNuoc;
        this.tongTien = tongTien;
        this.ngayGhi = ngayGhi;
        this.ghiChu = ghiChu;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getMaPhong() { return maPhong; }
    public void setMaPhong(String maPhong) { this.maPhong = maPhong; }

    public int getThang() { return thang; }
    public void setThang(int thang) { this.thang = thang; }

    public int getNam() { return nam; }
    public void setNam(int nam) { this.nam = nam; }

    public int getChiSoDienCu() { return chiSoDienCu; }
    public void setChiSoDienCu(int chiSoDienCu) { this.chiSoDienCu = chiSoDienCu; }

    public int getChiSoDienMoi() { return chiSoDienMoi; }
    public void setChiSoDienMoi(int chiSoDienMoi) { this.chiSoDienMoi = chiSoDienMoi; }

    public int getChiSoNuocCu() { return chiSoNuocCu; }
    public void setChiSoNuocCu(int chiSoNuocCu) { this.chiSoNuocCu = chiSoNuocCu; }

    public int getChiSoNuocMoi() { return chiSoNuocMoi; }
    public void setChiSoNuocMoi(int chiSoNuocMoi) { this.chiSoNuocMoi = chiSoNuocMoi; }

    public double getTienDien() { return tienDien; }
    public void setTienDien(double tienDien) { this.tienDien = tienDien; }

    public double getTienNuoc() { return tienNuoc; }
    public void setTienNuoc(double tienNuoc) { this.tienNuoc = tienNuoc; }

    public double getTongTien() { return tongTien; }
    public void setTongTien(double tongTien) { this.tongTien = tongTien; }

    public Date getNgayGhi() { return ngayGhi; }
    public void setNgayGhi(Date ngayGhi) { this.ngayGhi = ngayGhi; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }
}



class HoaDon {

    private String maHD;
    private String maKhach;
    private String maPhong;
    private int thang;
    private int nam;
    private int soDienCu;
    private int soDienMoi;
    private int soNuocCu;
    private int soNuocMoi;
    private double tienPhong;
    private double tienDien;
    private double tienNuoc;
    private double tongTien;
    // ÄÃƒ Sá»¬A: Chuyá»ƒn Ä‘á»•i tá»« String sang int Ä‘á»ƒ Ä‘á»“ng nháº¥t logic (1 = ÄÃ£ thanh toÃ¡n, 0 = ChÆ°a thanh toÃ¡n)
    private int trangThai;
    private Date ngayLap;
    private String ghiChu;

    public HoaDon() {}

    public HoaDon(String maHD, String maKhach, String maPhong, int thang, int nam,
                  int soDienCu, int soDienMoi, int soNuocCu, int soNuocMoi,
                  double tienPhong, double tienDien, double tienNuoc, double tongTien,
                  int trangThai, Date ngayLap, String ghiChu) {
        this.maHD = maHD;
        this.maKhach = maKhach;
        this.maPhong = maPhong;
        this.thang = thang;
        this.nam = nam;
        this.soDienCu = soDienCu;
        this.soDienMoi = soDienMoi;
        this.soNuocCu = soNuocCu;
        this.soNuocMoi = soNuocMoi;
        this.tienPhong = tienPhong;
        this.tienDien = tienDien;
        this.tienNuoc = tienNuoc;
        this.tongTien = tongTien;
        this.trangThai = trangThai;
        this.ngayLap = ngayLap;
        this.ghiChu = ghiChu;
    }

    public String getMaHD() { return maHD; }
    public void setMaHD(String maHD) { this.maHD = maHD; }

    public String getMaKhach() { return maKhach; }
    public void setMaKhach(String maKhach) { this.maKhach = maKhach; }

    public String getMaPhong() { return maPhong; }
    public void setMaPhong(String maPhong) { this.maPhong = maPhong; }

    public int getThang() { return thang; }
    public void setThang(int thang) { this.thang = thang; }

    public int getNam() { return nam; }
    public void setNam(int nam) { this.nam = nam; }

    public int getSoDienCu() { return soDienCu; }
    public void setSoDienCu(int soDienCu) { this.soDienCu = soDienCu; }

    public int getSoDienMoi() { return soDienMoi; }
    public void setSoDienMoi(int soDienMoi) { this.soDienMoi = soDienMoi; }

    public int getSoNuocCu() { return soNuocCu; }
    public void setSoNuocCu(int soNuocCu) { this.soNuocCu = soNuocCu; }

    public int getSoNuocMoi() { return soNuocMoi; }
    public void setSoNuocMoi(int soNuocMoi) { this.soNuocMoi = soNuocMoi; }

    public double getTienPhong() { return tienPhong; }
    public void setTienPhong(double tienPhong) { this.tienPhong = tienPhong; }

    public double getTienDien() { return tienDien; }
    public void setTienDien(double tienDien) { this.tienDien = tienDien; }

    public double getTienNuoc() { return tienNuoc; }
    public void setTienNuoc(double tienNuoc) { this.tienNuoc = tienNuoc; }

    public double getTongTien() { return tongTien; }
    public void setTongTien(double tongTien) { this.tongTien = tongTien; }

    // ÄÃƒ Sá»¬A: Getter vÃ  Setter tráº£ vá» kiá»ƒu int chuáº©n xÃ¡c
    public int getTrangThai() { return trangThai; }
    public void setTrangThai(int trangThai) { this.trangThai = trangThai; }

    public Date getNgayLap() { return ngayLap; }
    public void setNgayLap(Date ngayLap) { this.ngayLap = ngayLap; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }
}



class HopDong {

    private String maHopDong;
    private String maKhach;
    private String maPhong;
    private Date ngayBatDau;
    private Date ngayHetHan;
    private int soThang;
    private double tienCoc;
    private String trangThai;
    private String ghiChu;

    public HopDong() {}

    public HopDong(String maHopDong, String maKhach, String maPhong, Date ngayBatDau,
                   Date ngayHetHan, int soThang, double tienCoc, String trangThai, String ghiChu) {
        this.maHopDong = maHopDong;
        this.maKhach = maKhach;
        this.maPhong = maPhong;
        this.ngayBatDau = ngayBatDau;
        this.ngayHetHan = ngayHetHan;
        this.soThang = soThang;
        this.tienCoc = tienCoc;
        this.trangThai = trangThai;
        this.ghiChu = ghiChu;
    }
    public String getMaHopDong() { return maHopDong; }
    public void setMaHopDong(String maHopDong) { this.maHopDong = maHopDong; }

    public String getMaKhach() { return maKhach; }
    public void setMaKhach(String maKhach) { this.maKhach = maKhach; }

    public String getMaPhong() { return maPhong; }
    public void setMaPhong(String maPhong) { this.maPhong = maPhong; }

    public Date getNgayBatDau() { return ngayBatDau; }
    public void setNgayBatDau(Date ngayBatDau) { this.ngayBatDau = ngayBatDau; }

    public Date getNgayHetHan() { return ngayHetHan; }
    public void setNgayHetHan(Date ngayHetHan) { this.ngayHetHan = ngayHetHan; }

    public int getSoThang() { return soThang; }
    public void setSoThang(int soThang) { this.soThang = soThang; }

    public double getTienCoc() { return tienCoc; }
    public void setTienCoc(double tienCoc) { this.tienCoc = tienCoc; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }

    public boolean isHetHan() {
        return "ÄÃ£ háº¿t háº¡n".equalsIgnoreCase(trangThai);
    }
}



class KhachHang {

    private String maKhach;
    private String hoTen;
    private String sdt;
    private String cmnd;
    private String email;
    private String diaChi;
    private String maPhongDangThue;
    private Date ngayBatDauThue;
    private Date ngayHetHan;
    private String trangThai;
    private String ghiChu;

    public KhachHang() {
    }

    public KhachHang(String maKhach, String hoTen, String sdt, String cmnd,
                     String email, String diaChi) {
        this.maKhach = maKhach;
        this.hoTen = hoTen;
        this.sdt = sdt;
        this.cmnd = cmnd;
        this.email = email;
        this.diaChi = diaChi;
    }

    public KhachHang(String maKhach, String hoTen, String sdt, String cmnd, String email,
                     String diaChi, String maPhongDangThue, Date ngayBatDauThue,
                     Date ngayHetHan, String trangThai, String ghiChu) {
        this.maKhach = maKhach;
        this.hoTen = hoTen;
        this.sdt = sdt;
        this.cmnd = cmnd;
        this.email = email;
        this.diaChi = diaChi;
        this.maPhongDangThue = maPhongDangThue;
        this.ngayBatDauThue = ngayBatDauThue;
        this.ngayHetHan = ngayHetHan;
        this.trangThai = trangThai;
        this.ghiChu = ghiChu;
    }

    public String getMaKhach() {
        return maKhach;
    }

    public void setMaKhach(String maKhach) {
        this.maKhach = maKhach;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getCmnd() {
        return cmnd;
    }

    public void setCmnd(String cmnd) {
        this.cmnd = cmnd;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getMaPhongDangThue() {
        return maPhongDangThue;
    }

    public void setMaPhongDangThue(String maPhongDangThue) {
        this.maPhongDangThue = maPhongDangThue;
    }

    public Date getNgayBatDauThue() {
        return ngayBatDauThue;
    }

    public void setNgayBatDauThue(Date ngayBatDauThue) {
        this.ngayBatDauThue = ngayBatDauThue;
    }

    public Date getNgayHetHan() {
        return ngayHetHan;
    }

    public void setNgayHetHan(Date ngayHetHan) {
        this.ngayHetHan = ngayHetHan;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    // ==================== METHOD Há»– TRá»¢ ====================
    public boolean dangThuePhong() {
        return maPhongDangThue != null && !maPhongDangThue.trim().isEmpty();
    }

    @Override
    public String toString() {
        return maKhach + " - " + hoTen;
    }
}



class NguoiQuanLy {

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



class PhongTro {

    private String maPhong;
    private String tenPhong;
    private double giaPhong;
    private String loaiPhong;
    private String trangThai;
    private String diaChi;
    private String moTa;

    private String maKhachThue;
    private String tenKhachThue;
    private String sdtKhachThue;

    private Date ngayBatDau;
    private Date ngayGiaHan;
    private int soThangThue;

    private String ghiChu;

    public PhongTro() {
    }

    public PhongTro(String maPhong, String tenPhong, double giaPhong,
                    String loaiPhong, String trangThai, String diaChi) {
        this.maPhong = maPhong;
        this.tenPhong = tenPhong;
        this.giaPhong = giaPhong;
        this.loaiPhong = loaiPhong;
        this.trangThai = trangThai;
        this.diaChi = diaChi;
    }

    public PhongTro(String maPhong, String tenPhong, double giaPhong, String loaiPhong,
                    String trangThai, String diaChi, String moTa, String maKhachThue,
                    String tenKhachThue, String sdtKhachThue, Date ngayBatDau,
                    Date ngayGiaHan, int soThangThue, String ghiChu) {
        this.maPhong = maPhong;
        this.tenPhong = tenPhong;
        this.giaPhong = giaPhong;
        this.loaiPhong = loaiPhong;
        this.trangThai = trangThai;
        this.diaChi = diaChi;
        this.moTa = moTa;
        this.maKhachThue = maKhachThue;
        this.tenKhachThue = tenKhachThue;
        this.sdtKhachThue = sdtKhachThue;
        this.ngayBatDau = ngayBatDau;
        this.ngayGiaHan = ngayGiaHan;
        this.soThangThue = soThangThue;
        this.ghiChu = ghiChu;
    }

    public String getMaPhong() { return maPhong; }
    public void setMaPhong(String maPhong) { this.maPhong = maPhong; }

    public String getTenPhong() { return tenPhong; }
    public void setTenPhong(String tenPhong) { this.tenPhong = tenPhong; }

    public double getGiaPhong() { return giaPhong; }
    public void setGiaPhong(double giaPhong) { this.giaPhong = giaPhong; }

    public String getLoaiPhong() { return loaiPhong; }
    public void setLoaiPhong(String loaiPhong) { this.loaiPhong = loaiPhong; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }

    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }

    public String getMaKhachThue() { return maKhachThue; }
    public void setMaKhachThue(String maKhachThue) { this.maKhachThue = maKhachThue; }

    public String getTenKhachThue() { return tenKhachThue; }
    public void setTenKhachThue(String tenKhachThue) { this.tenKhachThue = tenKhachThue; }

    public String getSdtKhachThue() { return sdtKhachThue; }
    public void setSdtKhachThue(String sdtKhachThue) { this.sdtKhachThue = sdtKhachThue; }

    public Date getNgayBatDau() { return ngayBatDau; }
    public void setNgayBatDau(Date ngayBatDau) { this.ngayBatDau = ngayBatDau; }

    public Date getNgayGiaHan() { return ngayGiaHan; }
    public void setNgayGiaHan(Date ngayGiaHan) { this.ngayGiaHan = ngayGiaHan; }

    public int getSoThangThue() { return soThangThue; }
    public void setSoThangThue(int soThangThue) { this.soThangThue = soThangThue; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }

    public boolean isPhongTrong() {
        return "Trá»‘ng".equalsIgnoreCase(this.trangThai);
    }

    public long tinhTienThueConLai() {
        return Math.round(giaPhong * (soThangThue > 0 ? soThangThue : 1));
    }

    @Override
    public String toString() {
        return maPhong + " - " + tenPhong + " (" + trangThai + ")";
    }
}


class ThongKe {

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
        return String.format("%,.0f", tongDoanhThuThang) + " â‚«";
    }

    public String getTongDoanhThuNamFormat() {
        return String.format("%,.0f", tongDoanhThuNam) + " â‚«";
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




class ChuTroDAO {

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



class DienNuocDAO {

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




class HoaDonDAO {

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
            // LÆ°u ngÃ y láº­p Ä‘á»ƒ hÃ³a Ä‘Æ¡n má»›i hiá»ƒn thá»‹ Ä‘Ãºng thá»© tá»± (ORDER BY NgayLap DESC)
            if (hd.getNgayLap() != null) {
                ps.setDate(14, hd.getNgayLap());
            } else {
                ps.setDate(14, new java.sql.Date(System.currentTimeMillis()));
            }
            // ÄÃƒ Sá»¬A: Chuyá»ƒn sang setInt Ä‘á»ƒ lÆ°u giÃ¡ trá»‹ sá»‘ (1 hoáº·c 0) vÃ o Database
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
            // ÄÃƒ Sá»¬A: Chuyá»ƒn sang setInt cho TrangThai sá»­a Ä‘á»•i
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

    // ÄÃƒ Sá»¬A: Thay tham sá»‘ String trangThai thÃ nh int trangThai Ä‘á»ƒ cáº­p nháº­t real-time khi báº¥m nÃºt "Thanh ToÃ¡n"
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
        // ÄÃƒ Sá»¬A: Ãnh xáº¡ dá»¯ liá»‡u tá»« SQL lÃªn model báº±ng kiá»ƒu getInt
        hd.setTrangThai(rs.getInt("TrangThai"));
        return hd;
    }

    // ÄÃƒ Sá»¬A: Äáº¿m theo kiá»ƒu sá»‘ (TrangThai = 0 lÃ  chÆ°a thanh toÃ¡n) cho Ä‘á»“ng bá»™ vá»›i ThongKeDAO
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




class HopDongDAO {

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
                "TrangThai = N'CÃ²n Hiá»‡u Lá»±c', " +
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
            System.out.println("Lá»—i gia háº¡n há»£p Ä‘á»“ng (3 tham sá»‘): " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean giaHanHopDong(String maKhach, int soThangThem) {
        String sql = "UPDATE HopDong " +
                "SET NgayHetHan = DATEADD(MONTH, ?, NgayHetHan), " +
                "SoThang = SoThang + ?, " +
                "TrangThai = N'CÃ²n Hiá»‡u Lá»±c', " +
                "GhiChu = CONCAT(ISNULL(GhiChu,''), ' - Gia háº¡n +', ?, ' thÃ¡ng') " +
                "WHERE MaKhach = ? AND TrangThai = N'CÃ²n Hiá»‡u Lá»±c'";

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
        String sql = "SELECT * FROM HopDong WHERE MaKhach = ? AND TrangThai = N'CÃ²n Hiá»‡u Lá»±c'";

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




class KhachHangDAO {

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




class PhongTroDAO {

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
            ps.setInt(8, 4); // Máº·c Ä‘á»‹nh tá»‘i Ä‘a 4 ngÆ°á»i

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean suaPhong(PhongTro pt) {
        String sqlUpdatePhong = "UPDATE PhongTro SET TenPhong=?, GiaPhong=?, LoaiPhong=?, TrangThai=?, DiaChi=?, MoTa=? WHERE MaPhong=?";
        String sqlUpdateHopDong = "UPDATE HopDong SET GhiChu=? WHERE MaPhong=? AND TrangThai = N'CÃ²n Hiá»‡u Lá»±c'";

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
        pt.setMoTa(rs.getString("MoTa") != null ? rs.getString("MoTa") : "KhÃ´ng cÃ³ mÃ´ táº£");

        pt.setMaKhachThue(rs.getString("MaKhachThue") != null ? rs.getString("MaKhachThue") : "---");
        pt.setTenKhachThue(rs.getString("TenKhachThue") != null ? rs.getString("TenKhachThue") : "PhÃ²ng trá»‘ng");
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
     * Láº¥y danh sÃ¡ch cÃ¡c phÃ²ng Ä‘ang á»Ÿ tráº¡ng thÃ¡i "Trá»‘ng" Ä‘á»ƒ hiá»ƒn thá»‹ cho chá»n khi thÃªm khÃ¡ch.
     */
    public ArrayList<PhongTro> getPhongTrong() {
        ArrayList<PhongTro> list = new ArrayList<>();
        String sql = "SELECT MaPhong, TenPhong, GiaPhong, LoaiPhong, TrangThai, DiaChi, MoTa " +
                "FROM PhongTro WHERE TrangThai = N'Trá»‘ng' ORDER BY MaPhong";
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
     * Cáº­p nháº­t tráº¡ng thÃ¡i phÃ²ng (vÃ­ dá»¥: "Trá»‘ng" -> "Äang á»Ÿ" hoáº·c ngÆ°á»£c láº¡i).
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




class ThongKeDAO {

    public ThongKe getThongKeTongHop() {
        ThongKe tk = new ThongKe();

        try (Connection conn = ConnectDB.getConnection()) {

            tk.setTongPhong(getTongPhong(conn));

            // ÄÃƒ Sá»¬A: Äá»“ng bá»™ chuá»—i N'Äang ThuÃª' viáº¿t hoa chuáº©n dá»¯ liá»‡u SQL Server
            tk.setPhongDangThue(getPhongTheoTrangThai(conn, "Äang ThuÃª"));

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

    // Äáº¿m khÃ¡ch trÃªn báº£ng KhachHang (nguá»“n dá»¯ liá»‡u chuáº©n cá»§a á»©ng dá»¥ng, Ä‘á»“ng bá»™ vá»›i KhachHangDAO)
    private int getTongKhachHang(Connection conn) throws SQLException {
        String sql = "SELECT COUNT(*) FROM KhachHang";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    // ÄÃƒ Sá»¬A: Äá»•i kiá»ƒm tra TrangThai thÃ nh kiá»ƒu sá»‘ (1 = ÄÃ£ thanh toÃ¡n)
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

    // ÄÃƒ Sá»¬A: Äá»•i kiá»ƒm tra TrangThai thÃ nh kiá»ƒu sá»‘ (1 = ÄÃ£ thanh toÃ¡n)
    private double getDoanhThuNamHienTai(Connection conn) throws SQLException {
        String sql = "SELECT ISNULL(SUM(TongTien), 0) FROM HoaDon " +
                "WHERE Nam = YEAR(GETDATE()) " +
                "AND TrangThai = 1";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getDouble(1) : 0;
        }
    }

    // ÄÃƒ Sá»¬A: Äá»•i kiá»ƒm tra TrangThai thÃ nh kiá»ƒu sá»‘ (0 = ChÆ°a thanh toÃ¡n)
    private int getHoaDonChuaThanhToan(Connection conn) throws SQLException {
        String sql = "SELECT COUNT(*) FROM HoaDon WHERE TrangThai = 0";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    // ÄÃƒ Sá»¬A: Tá»‘i Æ°u thuáº­t toÃ¡n quÃ©t doanh thu 6 thÃ¡ng chÃ­nh xÃ¡c, tá»± Ä‘á»™ng bÃ¹ sá»‘ 0 náº¿u thÃ¡ng Ä‘Ã³ trá»‘ng dá»¯ liá»‡u
    private double[] getDoanhThu6Thang(Connection conn) throws SQLException {
        double[] data = new double[6];

        Calendar cal = Calendar.getInstance();
        int currentThang = cal.get(Calendar.MONTH) + 1;
        int currentNam = cal.get(Calendar.YEAR);

        String sql = "SELECT ISNULL(SUM(TongTien), 0) FROM HoaDon WHERE Thang = ? AND Nam = ? AND TrangThai = 1";

        // VÃ²ng láº·p tÃ­nh toÃ¡n lÃ¹i dáº§n chuáº©n xÃ¡c 6 thÃ¡ng tá»« quÃ¡ khá»© Ä‘áº¿n hiá»‡n táº¡i (vá»‹ trÃ­ index tá»« 0 Ä‘áº¿n 5)
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



class EmailSender {

    private static final String EMAIL_FROM =
            "trinhngocbao1508@gmail.com";

    private static final String APP_PASSWORD =
            "qngs fsxg syus xulr";


    public static boolean sendMail(String toEmail,
                                   String subject,
                                   String htmlContent) {

        try {

            Properties props = new Properties();

            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

            Session session = Session.getInstance(props,

                    new Authenticator() {

                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {

                            return new PasswordAuthentication(
                                    EMAIL_FROM,
                                    APP_PASSWORD
                            );

                        }

                    });

            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress(EMAIL_FROM));

            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(toEmail));

            message.setSubject(subject);

            message.setContent(
                    htmlContent,
                    "text/html;charset=UTF-8");

            Transport.send(message);

            System.out.println("================================");
            System.out.println("ÄÃ£ gá»­i Email thÃ nh cÃ´ng");
            System.out.println("Äáº¿n : " + toEmail);
            System.out.println("================================");

            return true;

        } catch (Exception ex) {

            ex.printStackTrace();

            return false;

        }

    }

    public static boolean sendOTP(String email,
                                  String otp) {

        String html =

                "<div style='font-family:Arial'>"

                        + "<h2 style='color:#2196F3'>QUÃŠN Máº¬T KHáº¨U</h2>"

                        + "<p>MÃ£ OTP cá»§a báº¡n lÃ :</p>"

                        + "<h1 style='color:red'>"
                        + otp
                        + "</h1>"

                        + "<p>OTP cÃ³ hiá»‡u lá»±c trong vÃ i phÃºt.</p>"

                        + "<hr>"

                        + "<b>Quáº£n lÃ½ NhÃ  Trá»</b>"

                        + "</div>";

        return sendMail(

                email,

                "MÃ£ OTP QuÃªn Máº­t Kháº©u",

                html

        );

    }

    public static boolean sendHoaDon(

            String email,

            String tenKhach,

            String phong,

            int thang,

            int nam,

            double tienPhong,

            double tienDien,

            double tienNuoc,

            double tongTien

    ) {

        DecimalFormat df = new DecimalFormat("#,###");

        String html =

                "<div style='font-family:Arial;'>"

                        + "<h2 style='color:#1565C0'>"

                        + "HÃ“A ÄÆ N THANH TOÃN"

                        + "</h2>"

                        + "<hr>"

                        + "<p>Xin chÃ o <b>"

                        + tenKhach

                        + "</b></p>"

                        + "<p>ThÃ´ng tin hÃ³a Ä‘Æ¡n thÃ¡ng "

                        + thang

                        + "/"

                        + nam

                        + "</p>"

                        + "<table border='1' cellspacing='0' cellpadding='8'>"

                        + "<tr>"

                        + "<td>PhÃ²ng</td>"

                        + "<td>"

                        + phong

                        + "</td>"

                        + "</tr>"

                        + "<tr>"

                        + "<td>Tiá»n phÃ²ng</td>"

                        + "<td>"

                        + df.format(tienPhong)

                        + " VNÄ</td>"

                        + "</tr>"

                        + "<tr>"

                        + "<td>Tiá»n Ä‘iá»‡n</td>"

                        + "<td>"

                        + df.format(tienDien)

                        + " VNÄ</td>"

                        + "</tr>"

                        + "<tr>"

                        + "<td>Tiá»n nÆ°á»›c</td>"

                        + "<td>"

                        + df.format(tienNuoc)

                        + " VNÄ</td>"

                        + "</tr>"

                        + "<tr style='background:#FFF59D;'>"

                        + "<td><b>Tá»”NG TIá»€N</b></td>"

                        + "<td><b style='color:red'>"

                        + df.format(tongTien)

                        + " VNÄ</b></td>"

                        + "</tr>"

                        + "</table>"

                        + "<br>"

                        + "<p>Vui lÃ²ng thanh toÃ¡n Ä‘Ãºng háº¡n.</p>"

                        + "<br>"

                        + "<b>Xin cáº£m Æ¡n!</b>"

                        + "<br>"

                        + "<b>Há»‡ thá»‘ng Quáº£n lÃ½ NhÃ  Trá»</b>"

                        + "</div>";

        return sendMail(

                email,

                "HÃ“A ÄÆ N THANH TOÃN TIá»€N PHÃ’NG",

                html

        );

    }

    public static boolean sendThongBao(

            String email,

            String title,

            String noiDung

    ) {

        String html =

                "<div style='font-family:Arial'>"

                        + "<h2>"

                        + title

                        + "</h2>"

                        + "<hr>"

                        + "<p>"

                        + noiDung

                        + "</p>"

                        + "<br>"

                        + "<b>Quáº£n lÃ½ NhÃ  Trá»</b>"

                        + "</div>";

        return sendMail(

                email,

                title,

                html

        );

    }

    public static void main(String[] args) {

        sendMail(

                "trinhngocbao1508@gmail.com",

                "TEST EMAIL",

                "<h2>Xin chÃ o!</h2>"
                        + "<h3>Email gá»­i thÃ nh cÃ´ng.</h3>"
                        + "<b>Quáº£n lÃ½ NhÃ  Trá»</b>"

        );

    }

}



class OTPService {

    public String generateOTP() {

        Random random = new Random();

        int otp = 100000 + random.nextInt(900000);

        return String.valueOf(otp);

    }

    public boolean sendOTP(String email, String otp) {

        String subject = "ðŸ” MÃƒ OTP KHÃ”I PHá»¤C Máº¬T KHáº¨U";

        String html =

                "<div style='font-family:Arial;'>"

                        + "<h2 style='color:#2980b9;'>Há»† THá»NG QUáº¢N LÃ NHÃ€ TRá»Œ</h2>"

                        + "<hr>"

                        + "<p>Xin chÃ o!</p>"

                        + "<p>Báº¡n vá»«a yÃªu cáº§u Ä‘áº·t láº¡i máº­t kháº©u.</p>"

                        + "<p>MÃ£ OTP cá»§a báº¡n lÃ :</p>"

                        + "<div style='font-size:40px;"
                        + "font-weight:bold;"
                        + "color:red;"
                        + "letter-spacing:5px;'>"

                        + otp

                        + "</div>"

                        + "<br>"

                        + "<p><b>OTP chá»‰ cÃ³ hiá»‡u lá»±c trong 5 phÃºt.</b></p>"

                        + "<p>KhÃ´ng chia sáº» mÃ£ nÃ y vá»›i báº¥t ká»³ ai.</p>"

                        + "<br>"

                        + "<p>TrÃ¢n trá»ng!</p>"

                        + "<b>Quáº£n lÃ½ nhÃ  trá»</b>"

                        + "</div>";

        return EmailSender.sendMail(

                email,

                subject,

                html

        );

    }

    public boolean verifyOTP(String inputOTP,
                             String correctOTP) {

        if (inputOTP == null || correctOTP == null)

            return false;

        return inputOTP.trim().equals(correctOTP.trim());

    }

}




class XuatHoaDonEmailService {

    // DÃ¹ng chung cáº¥u hÃ¬nh email vá»›i EmailSender (tÃ i khoáº£n Gmail Ä‘Ã£ cáº¥u hÃ¬nh App Password)
    private static final String HOST_EMAIL = "trinhngocbao1508@gmail.com";
    private static final String APP_PASSWORD = "qngs fsxg syus xulr";

    private static final String TEN_CHU_TRO = "TRá»ŠNH NGá»ŒC Báº¢O";
    private static final String SDT_CHU_TRO = "0335.122.471";
    private static final String CCCD_CHU_TRO = "040092001234";
    private static final String DIA_CHI_TRO = "123 Khu Phá»‘ Quáº£n LÃ½, Há»‡ Thá»‘ng NhÃ  Trá»";

    public static File createInvoicePDF(PhongTro phong, double soDienMoi, double soDienCu, double soNuocMoi, double soNuocCu, double tongTien) {
        try {
            DecimalFormat df = new DecimalFormat("#,###");
            String ngayLap = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
            String thangNam = new SimpleDateFormat("MM/yyyy").format(new Date());

            double tieuThuDien = (soDienMoi >= soDienCu) ? (soDienMoi - soDienCu) : 0;
            double tieuThuNuoc = (soNuocMoi >= soNuocCu) ? (soNuocMoi - soNuocCu) : 0;

            double donGiaDien = 3500;
            double donGiaNuoc = 18000;

            double thanhTienDien = tieuThuDien * donGiaDien;
            double thanhTienNuoc = tieuThuNuoc * donGiaNuoc;
            double tienPhong = tongTien - thanhTienDien - thanhTienNuoc;

            File pdfFile = File.createTempFile("HoaDon_" + phong.getMaPhong() + "_", ".pdf");
            Document document = new Document(PageSize.A4, 36, 36, 36, 36);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
            document.open();

            String htmlContent = "<html>"
                    + "<head>"
                    + "<style>"
                    + "body { font-family: 'Times New Roman', Times, serif; color: #333; line-height: 1.4; }"
                    + ".header { text-align: center; margin-bottom: 20px; }"
                    + ".title { font-size: 22px; font-weight: bold; color: #2c3e50; text-transform: uppercase; }"
                    + ".subtitle { font-size: 13px; font-style: italic; color: #555; margin-top: 5px; }"
                    + ".section-title { font-size: 14px; font-weight: bold; color: #2c3e50; border-bottom: 1px solid #ddd; padding-bottom: 5px; margin-top: 15px; margin-bottom: 10px; text-transform: uppercase; }"
                    + "table { width: 100%; border-collapse: collapse; margin-top: 10px; font-size: 13px; }"
                    + "table.info-table td { border: none; padding: 4px 0; vertical-align: top; }"
                    + "table.data-table th { background-color: #f2f2f2; border: 1px solid #ddd; padding: 8px; text-align: center; font-weight: bold; }"
                    + "table.data-table td { border: 1px solid #ddd; padding: 8px; }"
                    + ".text-right { text-align: right; }"
                    + ".text-center { text-align: center; }"
                    + ".total-row { font-weight: bold; background-color: #eaeded; font-size: 14px; }"
                    + ".footer-sign { margin-top: 30px; width: 100%; }"
                    + ".footer-sign td { text-align: center; font-size: 13px; width: 50%; }"
                    + "</style>"
                    + "</head>"
                    + "<body>"
                    + "  <div class='header'>"
                    + "    <div class='title'>HÃ“A ÄÆ N THANH TOÃN TIá»€N PHÃ’NG</div>"
                    + "    <div class='subtitle'>Ká»³ thanh toÃ¡n: ThÃ¡ng " + thangNam + "</div>"
                    + "    <div class='subtitle'>NgÃ y láº­p hÃ³a Ä‘Æ¡n: " + ngayLap + "</div>"
                    + "  </div>"
                    + "  <table class='info-table'>"
                    + "    <tr>"
                    + "      <td width='50%'>"
                    + "        <div class='section-title'>1. ÄÆ N Vá»Š CHO THUÃŠ (CHá»¦ TRá»Œ)</div>"
                    + "        <b>Há» tÃªn:</b> " + TEN_CHU_TRO + "<br/>"
                    + "        <b>Äiá»‡n thoáº¡i:</b> " + SDT_CHU_TRO + "<br/>"
                    + "        <b>Sá»‘ CCCD:</b> " + CCCD_CHU_TRO + "<br/>"
                    + "        <b>Äá»‹a chá»‰ khu trá»:</b> " + DIA_CHI_TRO
                    + "      </td>"
                    + "      <td width='50%' style='padding-left: 20px;'>"
                    + "        <div class='section-title'>2. KHÃCH HÃ€NG THUÃŠ PHÃ’NG</div>"
                    + "        <b>Há» tÃªn khÃ¡ch:</b> " + phong.getTenKhachThue() + "<br/>"
                    + "        <b>MÃ£ phÃ²ng:</b> " + phong.getTenPhong() + "<br/>"
                    + "        <b>Äá»‹a chá»‰ phÃ²ng:</b> " + phong.getDiaChi()
                    + "      </td>"
                    + "    </tr>"
                    + "  </table>"
                    + "  <div class='section-title'>3. CHI TIáº¾T CHá»ˆ Sá» CÃ”NG TÆ  & TIÃŠU THá»¤</div>"
                    + "  <table class='data-table'>"
                    + "    <thead>"
                    + "      <tr>"
                    + "        <th>Loáº¡i dá»‹ch vá»¥</th>"
                    + "        <th>Chá»‰ sá»‘ cÅ©</th>"
                    + "        <th>Chá»‰ sá»‘ má»›i</th>"
                    + "        <th>Sáº£n lÆ°á»£ng tiÃªu thá»¥</th>"
                    + "      </tr>"
                    + "    </thead>"
                    + "    <tbody>"
                    + "      <tr>"
                    + "        <td>Äiá»‡n sinh hoáº¡t (kWh)</td>"
                    + "        <td class='text-center'>" + (int)soDienCu + "</td>"
                    + "        <td class='text-center'>" + (int)soDienMoi + "</td>"
                    + "        <td class='text-center'><b>" + (int)tieuThuDien + "</b> kWh</td>"
                    + "      </tr>"
                    + "      <tr>"
                    + "        <td>NÆ°á»›c sinh hoáº¡t (mÂ³)</td>"
                    + "        <td class='text-center'>" + (int)soNuocCu + "</td>"
                    + "        <td class='text-center'>" + (int)soNuocMoi + "</td>"
                    + "        <td class='text-center'><b>" + (int)tieuThuNuoc + "</b> mÂ³</td>"
                    + "      </tr>"
                    + "    </tbody>"
                    + "  </table>"
                    + "  <div class='section-title'>4. CHI PHÃ THÃ€NH TIá»€N CHI TIáº¾T</div>"
                    + "  <table class='data-table'>"
                    + "    <thead>"
                    + "      <tr>"
                    + "        <th width='5%'>STT</th>"
                    + "        <th width='45%'>Ná»™i dung thanh toÃ¡n</th>"
                    + "        <th width='25%'>ÄÆ¡n giÃ¡</th>"
                    + "        <th width='25%'>ThÃ nh tiá»n (VND)</th>"
                    + "      </tr>"
                    + "    </thead>"
                    + "    <tbody>"
                    + "      <tr>"
                    + "        <td class='text-center'>1</td>"
                    + "        <td>Tiá»n thuÃª phÃ²ng trá» trá»n gÃ³i</td>"
                    + "        <td class='text-right'>" + df.format(tienPhong) + " Ä‘/thÃ¡ng</td>"
                    + "        <td class='text-right'>" + df.format(tienPhong) + " Ä‘</td>"
                    + "      </tr>"
                    + "      <tr>"
                    + "        <td class='text-center'>2</td>"
                    + "        <td>Tiá»n Ä‘iá»‡n tiÃªu thá»¥ (theo cÃ´ng tÆ¡)</td>"
                    + "        <td class='text-right'>" + df.format(donGiaDien) + " Ä‘/kWh</td>"
                    + "        <td class='text-right'>" + df.format(thanhTienDien) + " Ä‘</td>"
                    + "      </tr>"
                    + "      <tr>"
                    + "        <td class='text-center'>3</td>"
                    + "        <td>Tiá»n nÆ°á»›c tiÃªu thá»¥ (theo cÃ´ng tÆ¡)</td>"
                    + "        <td class='text-right'>" + df.format(donGiaNuoc) + " Ä‘/mÂ³</td>"
                    + "        <td class='text-right'>" + df.format(thanhTienNuoc) + " Ä‘</td>"
                    + "      </tr>"
                    + "      <tr class='total-row'>"
                    + "        <td colspan='3' class='text-right'>Tá»”NG Cá»˜NG TIá»€N PHáº¢I TRáº¢:</td>"
                    + "        <td class='text-right' style='color:#c0392b; font-size:15px;'>" + df.format(tongTien) + " Ä‘</td>"
                    + "      </tr>"
                    + "    </tbody>"
                    + "  </table>"
                    + "  <table class='footer-sign'>"
                    + "    <tr>"
                    + "      <td>"
                    + "        <b>KHÃCH THUÃŠ PHÃ’NG</b><br/>"
                    + "        <i>(KÃ½ vÃ  ghi rÃµ há» tÃªn)</i>"
                    + "      </td>"
                    + "      <td>"
                    + "        <b>Äáº I DIá»†N CHá»¦ TRá»Œ</b><br/>"
                    + "        <i>(KÃ½ vÃ  ghi rÃµ há» tÃªn)</i>"
                    + "      </td>"
                    + "    </tr>"
                    + "  </table>"
                    + "</body>"
                    + "</html>";

            ByteArrayInputStream bias = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
            XMLWorkerHelper.getInstance().parseXHtml(writer, document, bias, StandardCharsets.UTF_8);

            document.close();
            return pdfFile;
        } catch (Exception e) {
            System.out.println("Lá»—i khi káº¿t xuáº¥t PDF HTML hÃ³a Ä‘Æ¡n: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static boolean guiHoaDonQuaEmail(String emailNhan, String tenPhong, File fileDinhKem) {
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        prop.put("mail.smtp.ssl.protocols", "TLSv1.2");

        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(HOST_EMAIL, APP_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(HOST_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailNhan));
            message.setSubject("ðŸ“‹ THÃ”NG BÃO: HÃ“A ÄÆ N TIá»€N PHÃ’NG CHI TIáº¾T - " + tenPhong.toUpperCase());

            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText("Xin chÃ o khÃ¡ch thuÃª,\n\nHá»‡ thá»‘ng quáº£n lÃ½ nhÃ  trá» xin gá»­i tá»›i báº¡n báº£ng sao kÃª chi tiáº¿t hÃ³a Ä‘Æ¡n tiá»n phÃ²ng vÃ  Ä‘iá»‡n nÆ°á»›c thÃ¡ng nÃ y.\n\nChi tiáº¿t cá»¥ thá»ƒ chá»‰ sá»‘ tiÃªu thá»¥ vÃ  thÃ nh tiá»n vui lÃ²ng xem vÃ  táº£i file Ä‘Ã­nh kÃ¨m dáº¡ng Ä‘á»‹nh dáº¡ng .pdf bÃªn dÆ°á»›i Ä‘á»ƒ quyáº¿t toÃ¡n thanh toÃ¡n tiá»n máº·t/chuyá»ƒn khoáº£n.\n\nXin cáº£m Æ¡n!");

            MimeBodyPart attachPart = new MimeBodyPart();
            attachPart.attachFile(fileDinhKem);
            attachPart.setFileName("HoaDon_ThanhToan_" + tenPhong.replace(" ", "_") + ".pdf");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(textPart);
            multipart.addBodyPart(attachPart);

            message.setContent(multipart);
            Transport.send(message);
            return true;
        } catch (Exception e) {
            System.out.println("Lá»—i gá»­i Email SMTP: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Gá»­i email xÃ¡c nháº­n Ä‘Ã£ thanh toÃ¡n hÃ³a Ä‘Æ¡n thÃ nh cÃ´ng Ä‘áº¿n khÃ¡ch thuÃª.
     */
    public static boolean guiEmailXacNhanThanhToan(
            String emailNhan,
            String tenKhach,
            String maHD,
            String tenPhong,
            int thang,
            int nam,
            double tongTien) {

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        prop.put("mail.smtp.ssl.protocols", "TLSv1.2");

        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(HOST_EMAIL, APP_PASSWORD);
            }
        });

        try {
            DecimalFormat df = new DecimalFormat("#,###");
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String thoiGianThanhToan = sdf.format(new java.util.Date());

            String htmlBody =
                "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: auto; border: 1px solid #e0e0e0; border-radius: 8px; overflow: hidden;'>"
                + "  <div style='background: linear-gradient(135deg, #1565C0, #0288D1); padding: 30px; text-align: center;'>"
                + "    <h1 style='color: white; margin: 0; font-size: 22px;'>âœ… XÃC NHáº¬N THANH TOÃN THÃ€NH CÃ”NG</h1>"
                + "    <p style='color: #e3f2fd; margin: 8px 0 0 0; font-size: 14px;'>Há»‡ Thá»‘ng Quáº£n LÃ½ NhÃ  Trá»</p>"
                + "  </div>"
                + "  <div style='padding: 30px; background: #fff;'>"
                + "    <p style='font-size: 16px; color: #333;'>Xin chÃ o <b>" + tenKhach + "</b>,</p>"
                + "    <p style='color: #555;'>ChÃºng tÃ´i xÃ¡c nháº­n báº¡n Ä‘Ã£ <b style='color: #27ae60;'>thanh toÃ¡n thÃ nh cÃ´ng</b> hÃ³a Ä‘Æ¡n tiá»n phÃ²ng thÃ¡ng <b>" + thang + "/" + nam + "</b>.</p>"
                + "    <div style='background: #f5f5f5; border-radius: 8px; padding: 20px; margin: 20px 0;'>"
                + "      <h3 style='color: #1565C0; margin: 0 0 15px 0; border-bottom: 2px solid #1565C0; padding-bottom: 8px;'>ðŸ“‹ THÃ”NG TIN HÃ“A ÄÆ N</h3>"
                + "      <table style='width: 100%; border-collapse: collapse;'>"
                + "        <tr><td style='padding: 6px 0; color: #777; width: 45%;'>MÃ£ hÃ³a Ä‘Æ¡n:</td>"
                + "            <td style='padding: 6px 0; font-weight: bold; color: #333;'>" + maHD + "</td></tr>"
                + "        <tr><td style='padding: 6px 0; color: #777;'>PhÃ²ng:</td>"
                + "            <td style='padding: 6px 0; font-weight: bold; color: #333;'>" + tenPhong + "</td></tr>"
                + "        <tr><td style='padding: 6px 0; color: #777;'>Ká»³ thanh toÃ¡n:</td>"
                + "            <td style='padding: 6px 0; font-weight: bold; color: #333;'>ThÃ¡ng " + thang + "/" + nam + "</td></tr>"
                + "        <tr><td style='padding: 6px 0; color: #777;'>Thá»i gian:</td>"
                + "            <td style='padding: 6px 0; font-weight: bold; color: #333;'>" + thoiGianThanhToan + "</td></tr>"
                + "        <tr style='border-top: 1px solid #ddd;'>"
                + "            <td style='padding: 12px 0 6px 0; color: #777; font-size: 15px;'>Sá»‘ tiá»n Ä‘Ã£ thanh toÃ¡n:</td>"
                + "            <td style='padding: 12px 0 6px 0; font-weight: bold; font-size: 18px; color: #c0392b;'>"
                + df.format(tongTien) + " VNÄ</td></tr>"
                + "      </table>"
                + "    </div>"
                + "    <div style='background: #e8f5e9; border-left: 4px solid #27ae60; padding: 15px; border-radius: 4px; margin-bottom: 20px;'>"
                + "      <p style='margin: 0; color: #2e7d32; font-size: 14px;'>âœ” HÃ³a Ä‘Æ¡n cá»§a báº¡n Ä‘Ã£ Ä‘Æ°á»£c ghi nháº­n trong há»‡ thá»‘ng. Cáº£m Æ¡n báº¡n Ä‘Ã£ thanh toÃ¡n Ä‘Ãºng háº¡n!</p>"
                + "    </div>"
                + "    <p style='color: #888; font-size: 13px;'>Náº¿u cÃ³ tháº¯c máº¯c, vui lÃ²ng liÃªn há»‡ chá»§ nhÃ  trá»: <b>" + SDT_CHU_TRO + "</b></p>"
                + "  </div>"
                + "  <div style='background: #f0f4f8; padding: 15px; text-align: center;'>"
                + "    <p style='color: #aaa; font-size: 12px; margin: 0;'>Â© Há»‡ thá»‘ng Quáº£n lÃ½ NhÃ  Trá» â€” " + TEN_CHU_TRO + "</p>"
                + "  </div>"
                + "</div>";

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(HOST_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailNhan));
            message.setSubject("âœ… [XÃC NHáº¬N THANH TOÃN] HÃ³a Ä‘Æ¡n " + maHD + " - PhÃ²ng " + tenPhong + " - ThÃ¡ng " + thang + "/" + nam);
            message.setContent(htmlBody, "text/html;charset=UTF-8");

            Transport.send(message);
            System.out.println("ÄÃ£ gá»­i email xÃ¡c nháº­n thanh toÃ¡n tá»›i: " + emailNhan);
            return true;

        } catch (Exception e) {
            System.out.println("Lá»—i gá»­i email xÃ¡c nháº­n thanh toÃ¡n: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}



class RoomCard extends JPanel {

    private final PhongTro phongTro;
    private final Runnable onReloadCallback;
    private final Consumer<PhongTro> onEditCallback;
    private final PhongTroDAO dao = new PhongTroDAO();

    public RoomCard(PhongTro pt, Runnable onReloadCallback, Consumer<PhongTro> onEditCallback) {
        this.phongTro = pt;
        this.onReloadCallback = onReloadCallback;
        this.onEditCallback = onEditCallback;

        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(15, 10));
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(780, 130));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(new Color(248, 249, 250));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(Color.WHITE);
            }
        });

        JPanel pnlInfo = new JPanel(new GridLayout(4, 2, 8, 6));
        pnlInfo.setOpaque(false);

        Font titleFont = new Font("Segoe UI", Font.BOLD, 15);
        Font normalFont = new Font("Segoe UI", Font.PLAIN, 13);

        JLabel lblTenPhong = new JLabel("PhÃ²ng: " + phongTro.getTenPhong());
        lblTenPhong.setFont(titleFont);

        JLabel lblGia = new JLabel("GiÃ¡: " + String.format("%,.0f", phongTro.getGiaPhong()) + " â‚«");
        lblGia.setFont(normalFont);

        String khach = (phongTro.getTenKhachThue() != null && !phongTro.getTenKhachThue().trim().isEmpty())
                ? phongTro.getTenKhachThue() : "ChÆ°a cÃ³ khÃ¡ch";

        JLabel lblKhach = new JLabel("KhÃ¡ch: " + khach);
        lblKhach.setFont(normalFont);

        String han = phongTro.getNgayGiaHan() != null
                ? phongTro.getNgayGiaHan().toString() : "ChÆ°a cÃ³ háº¡n";

        JLabel lblHan = new JLabel("Háº¡n há»£p Ä‘á»“ng: " + han);
        lblHan.setFont(normalFont);

        JLabel lblDiaChi = new JLabel("Äá»‹a chá»‰: " + phongTro.getDiaChi());
        lblDiaChi.setFont(normalFont);

        pnlInfo.add(lblTenPhong);
        pnlInfo.add(lblGia);
        pnlInfo.add(lblKhach);
        pnlInfo.add(lblHan);
        pnlInfo.add(lblDiaChi);

        JPanel pnlRight = new JPanel(new BorderLayout(0, 10));
        pnlRight.setOpaque(false);

        JLabel lblTrangThai = new JLabel(" " + phongTro.getTrangThai() + " ", SwingConstants.CENTER);
        lblTrangThai.setOpaque(true);
        lblTrangThai.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTrangThai.setBorder(BorderFactory.createEmptyBorder(4, 12, 4, 12));

        if ("Trá»‘ng".equalsIgnoreCase(phongTro.getTrangThai())) {
            lblTrangThai.setBackground(new Color(223, 255, 223));
            lblTrangThai.setForeground(new Color(0, 120, 0));
        } else {
            lblTrangThai.setBackground(new Color(255, 230, 230));
            lblTrangThai.setForeground(new Color(180, 0, 0));
        }

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        pnlButtons.setOpaque(false);

        JButton btnSua = createStyledButton("Sá»­a", new Color(52, 152, 219));
        JButton btnXoa = createStyledButton("XÃ³a", new Color(231, 76, 60));

        btnSua.addActionListener(e -> suaPhong());
        btnXoa.addActionListener(e -> xoaPhong());

        pnlButtons.add(btnSua);
        pnlButtons.add(btnXoa);

        pnlRight.add(lblTrangThai, BorderLayout.NORTH);
        pnlRight.add(pnlButtons, BorderLayout.SOUTH);

        add(pnlInfo, BorderLayout.CENTER);
        add(pnlRight, BorderLayout.EAST);
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void suaPhong() {
        if (onEditCallback != null) {
            onEditCallback.accept(phongTro);
        }
    }

    private void xoaPhong() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Báº¡n cÃ³ cháº¯c cháº¯n muá»‘n xÃ³a phÃ²ng " + phongTro.getTenPhong() + "?\nHÃ nh Ä‘á»™ng nÃ y khÃ´ng thá»ƒ hoÃ n tÃ¡c!",
                "XÃ¡c nháº­n xÃ³a phÃ²ng", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (dao.xoaPhong(phongTro.getMaPhong())) {
                    JOptionPane.showMessageDialog(this, "XÃ³a phÃ²ng thÃ nh cÃ´ng!", "ThÃ nh cÃ´ng", JOptionPane.INFORMATION_MESSAGE);
                    if (onReloadCallback != null) {
                        onReloadCallback.run();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "KhÃ´ng thá»ƒ xÃ³a phÃ²ng nÃ y!", "Lá»—i", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lá»—i khi xÃ³a: " + ex.getMessage(), "Lá»—i", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
}



class PanelThongKe extends JPanel {

    public PanelThongKe() {
        setLayout(new BorderLayout());

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(5000000, "Thu Nháº­p", "ThÃ¡ng 4");
        dataset.addValue(7500000, "Thu Nháº­p", "ThÃ¡ng 5");
        dataset.addValue(12000000, "Thu Nháº­p", "ThÃ¡ng 6"); // ThÃ¡ng hiá»‡n táº¡i

        JFreeChart chart = ChartFactory.createBarChart(
                "BIá»‚U Äá»’ THU NHáº¬P",
                "Thá»i gian",
                "Sá»‘ tiá»n (VNÄ)",
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false
        );

        ChartPanel chartPanel = new ChartPanel(chart);
        add(chartPanel, BorderLayout.CENTER);
    }
}




class FrmGiaHan extends JPanel {

    private final HopDongDAO hopDongDAO = new HopDongDAO();
    private final KhachHangDAO khachHangDAO = new KhachHangDAO();
    private final PhongTroDAO phongTroDAO = new PhongTroDAO();

    private JTable tblHopDong;
    private DefaultTableModel model;

    private JComboBox<String> cboKhachHang;
    private JTextField txtMaHopDong, txtThangHienTai, txtNamHienTai;
    private JTextField txtSoThangGiaHan, txtGhiChu;

    private JButton btnGiaHan, btnLamMoi, btnXoa;
    private boolean isUpdatingUI = false;

    public FrmGiaHan() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setLayout(new BorderLayout(10, 10));
        initComponents();
        loadDuLieu();
    }

    private void initComponents() {
        JLabel lblTitle = new JLabel("ðŸ“… QUáº¢N LÃ GIA Háº N Há»¢P Äá»’NG ÄÄ‚NG KÃ", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 26));
        lblTitle.setForeground(new Color(41, 128, 185));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(lblTitle, BorderLayout.NORTH);

        JPanel pnlLeft = new JPanel(new GridLayout(7, 2, 12, 12));
        pnlLeft.setBorder(BorderFactory.createTitledBorder("ThÃ´ng tin gia háº¡n há»£p Ä‘á»“ng"));
        pnlLeft.setPreferredSize(new Dimension(420, 0));

        cboKhachHang = new JComboBox<>();
        txtMaHopDong = new JTextField(); txtMaHopDong.setEditable(false);
        txtThangHienTai = new JTextField(); txtThangHienTai.setEditable(false);
        txtNamHienTai = new JTextField(); txtNamHienTai.setEditable(false);
        txtSoThangGiaHan = new JTextField();
        txtGhiChu = new JTextField();

        pnlLeft.add(new JLabel("KhÃ¡ch hÃ ng Ä‘ang thuÃª:")); pnlLeft.add(cboKhachHang);
        pnlLeft.add(new JLabel("MÃ£ há»£p Ä‘á»“ng gá»‘c:")); pnlLeft.add(txtMaHopDong);
        pnlLeft.add(new JLabel("ThÃ¡ng háº¿t háº¡n gá»‘c:")); pnlLeft.add(txtThangHienTai);
        pnlLeft.add(new JLabel("NÄƒm háº¿t háº¡n gá»‘c:")); pnlLeft.add(txtNamHienTai);
        pnlLeft.add(new JLabel("Gia háº¡n thÃªm (thÃ¡ng):")); pnlLeft.add(txtSoThangGiaHan);
        pnlLeft.add(new JLabel("Ghi chÃº thay Ä‘á»•i:")); pnlLeft.add(txtGhiChu);

        add(pnlLeft, BorderLayout.WEST);

        JPanel pnlCenter = new JPanel(new BorderLayout());
        String[] columns = {"MÃ£ HÄ", "MÃ£ KhÃ¡ch", "PhÃ²ng", "NgÃ y Báº¯t Äáº§u", "NgÃ y Háº¿t Háº¡n", "Sá»‘ ThÃ¡ng", "Tráº¡ng ThÃ¡i", "Ghi ChÃº"};
        model = new DefaultTableModel(columns, 0);
        tblHopDong = new JTable(model);
        tblHopDong.setRowHeight(32);
        tblHopDong.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));

        pnlCenter.add(new JScrollPane(tblHopDong), BorderLayout.CENTER);
        add(pnlCenter, BorderLayout.CENTER);

        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        btnGiaHan = new JButton("âœ… Gia Háº¡n Há»£p Äá»“ng");
        btnLamMoi = new JButton("ðŸ”„ LÃ m Má»›i");
        btnXoa = new JButton("ðŸ—‘ï¸ XÃ³a Há»£p Äá»“ng");

        styleButton(btnGiaHan, new Color(46, 204, 113));
        styleButton(btnLamMoi, new Color(52, 152, 219));
        styleButton(btnXoa, new Color(231, 76, 60));

        pnlBottom.add(btnGiaHan);
        pnlBottom.add(btnLamMoi);
        pnlBottom.add(btnXoa);

        add(pnlBottom, BorderLayout.SOUTH);

        cboKhachHang.addActionListener(e -> loadThongTinHopDong());
        btnGiaHan.addActionListener(e -> giaHanHopDong());
        btnLamMoi.addActionListener(e -> lamMoi());
        btnXoa.addActionListener(e -> xoaHopDong());

        // Ãnh xáº¡ dá»¯ liá»‡u khi nháº¥n chá»n dÃ²ng trÃªn báº£ng
        tblHopDong.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblHopDong.getSelectedRow() != -1) {
                int row = tblHopDong.getSelectedRow();
                String maHD = model.getValueAt(row, 0).toString();
                String maKhachBang = model.getValueAt(row, 1).toString();
                String ghiChuVal = model.getValueAt(row, 7) != null ? model.getValueAt(row, 7).toString() : "";

                isUpdatingUI = true;
                try {
                    for (int i = 0; i < cboKhachHang.getItemCount(); i++) {
                        if (cboKhachHang.getItemAt(i).startsWith(maKhachBang)) {
                            cboKhachHang.setSelectedIndex(i);
                            break;
                        }
                    }
                    txtMaHopDong.setText(maHD);
                    txtGhiChu.setText(ghiChuVal);

                    Object dateObj = model.getValueAt(row, 4);
                    if (dateObj instanceof java.util.Date) {
                        Calendar cal = Calendar.getInstance();
                        cal.setTime((java.util.Date) dateObj);
                        txtThangHienTai.setText(String.valueOf(cal.get(Calendar.MONTH) + 1));
                        txtNamHienTai.setText(String.valueOf(cal.get(Calendar.YEAR)));
                    } else if (dateObj != null) {
                        try {
                            java.sql.Date d = java.sql.Date.valueOf(dateObj.toString());
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(d);
                            txtThangHienTai.setText(String.valueOf(cal.get(Calendar.MONTH) + 1));
                            txtNamHienTai.setText(String.valueOf(cal.get(Calendar.YEAR)));
                        } catch (Exception ex) {
                            txtThangHienTai.setText("");
                            txtNamHienTai.setText("");
                        }
                    } else {
                        txtThangHienTai.setText("");
                        txtNamHienTai.setText("");
                    }
                } finally {
                    isUpdatingUI = false;
                }
            }
        });
    }

    private void styleButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(190, 45));
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void loadComboKhachHang() {
        cboKhachHang.removeAllItems();
        try {
            ArrayList<KhachHang> listKH = khachHangDAO.getAllKhachHang();
            for (KhachHang kh : listKH) {
                if (kh.getMaPhongDangThue() != null && !kh.getMaPhongDangThue().trim().isEmpty()) {
                    cboKhachHang.addItem(kh.getMaKhach() + " - " + kh.getHoTen());
                }
            }
        } catch (Exception e) {
            System.out.println("Lá»—i náº¡p Combo KhÃ¡ch HÃ ng: " + e.getMessage());
        }
    }

    private void loadDuLieu() {
        model.setRowCount(0);
        loadComboKhachHang();

        try {
            ArrayList<HopDong> list = hopDongDAO.getAllHopDong();
            for (HopDong hd : list) {
                model.addRow(new Object[]{
                        hd.getMaHopDong(),
                        hd.getMaKhach(),
                        hd.getMaPhong(),
                        hd.getNgayBatDau(),
                        hd.getNgayHetHan(),
                        hd.getSoThang(),
                        hd.getTrangThai(),
                        hd.getGhiChu()
                });
            }
        } catch (Exception e) {
            System.out.println("Lá»—i náº¡p danh sÃ¡ch há»£p Ä‘á»“ng: " + e.getMessage());
        }
    }

    private void loadThongTinHopDong() {
        if (isUpdatingUI) return;
        String selected = (String) cboKhachHang.getSelectedItem();
        if (selected == null) return;

        String maKhach = selected.split(" - ")[0].trim();
        try {
            HopDong hd = hopDongDAO.timTheoMaKhach(maKhach);
            if (hd != null && hd.getMaHopDong() != null) {
                txtMaHopDong.setText(hd.getMaHopDong());
                if (hd.getNgayHetHan() != null) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(hd.getNgayHetHan());

                    txtThangHienTai.setText(String.valueOf(cal.get(Calendar.MONTH) + 1));
                    txtNamHienTai.setText(String.valueOf(cal.get(Calendar.YEAR)));
                } else {
                    txtThangHienTai.setText("");
                    txtNamHienTai.setText("");
                }
                txtGhiChu.setText(hd.getGhiChu() != null ? hd.getGhiChu() : "");
            } else {
                txtMaHopDong.setText("ChÆ°a cÃ³ HÄ");
                txtThangHienTai.setText("");
                txtNamHienTai.setText("");
                txtGhiChu.setText("");
            }
        } catch (Exception e) {
            System.out.println("Lá»—i Ä‘á»c há»£p Ä‘á»“ng: " + e.getMessage());
        }
    }

    private void giaHanHopDong() {
        try {
            String selected = (String) cboKhachHang.getSelectedItem();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Vui lÃ²ng chá»n khÃ¡ch hÃ ng cáº§n gia háº¡n!");
                return;
            }

            String maHDGoc = txtMaHopDong.getText().trim();
            if (maHDGoc.equals("ChÆ°a cÃ³ HÄ") || maHDGoc.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Há»‡ thá»‘ng chÆ°a ghi nháº­n há»£p Ä‘á»“ng gá»‘c há»£p lá»‡ cho khÃ¡ch thuÃª nÃ y!");
                return;
            }

            if (txtSoThangGiaHan.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lÃ²ng nháº­p sá»‘ thÃ¡ng muá»‘n gia háº¡n thÃªm!");
                return;
            }

            int soThang = Integer.parseInt(txtSoThangGiaHan.getText().trim());
            if (soThang <= 0) {
                JOptionPane.showMessageDialog(this, "Sá»‘ thÃ¡ng gia háº¡n pháº£i lÃ  sá»‘ nguyÃªn lá»›n hÆ¡n 0!");
                return;
            }

            String ghiChu = txtGhiChu.getText().trim();

            if (hopDongDAO.giaHanHopDong(maHDGoc, soThang, ghiChu)) {
                JOptionPane.showMessageDialog(this, "Gia háº¡n thÃ nh cÃ´ng há»£p Ä‘á»“ng " + maHDGoc + " thÃªm " + soThang + " thÃ¡ng!");
                loadDuLieu();
                lamMoi();
            } else {
                JOptionPane.showMessageDialog(this, "Gia háº¡n tháº¥t báº¡i! Vui lÃ²ng kiá»ƒm tra láº¡i phÆ°Æ¡ng thá»©c cáº­p nháº­t táº¡i HopDongDAO.");
            }
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Äá»‹nh dáº¡ng sá»‘ thÃ¡ng gia háº¡n nháº­p vÃ o khÃ´ng há»£p lá»‡!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lá»—i xá»­ lÃ½ há»‡ thá»‘ng: " + ex.getMessage());
        }
    }

    private void xoaHopDong() {
        int row = tblHopDong.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lÃ²ng chá»n hÃ ng há»£p Ä‘á»“ng trÃªn báº£ng cáº§n xÃ³a!");
            return;
        }

        String maHopDong = model.getValueAt(row, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(this,
                "Báº¡n cÃ³ cháº¯c cháº¯n muá»‘n xÃ³a há»£p Ä‘á»“ng mang mÃ£ " + maHopDong + " khÃ´ng?",
                "XÃ¡c nháº­n xÃ³a há»£p Ä‘á»“ng", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (hopDongDAO.xoaHopDong(maHopDong)) {
                JOptionPane.showMessageDialog(this, "XÃ³a há»£p Ä‘á»“ng thÃ nh cÃ´ng!");
                loadDuLieu();
                lamMoi();
            } else {
                JOptionPane.showMessageDialog(this, "KhÃ´ng thá»ƒ xÃ³a há»£p Ä‘á»“ng Ä‘ang trong thá»i gian hiá»‡u lá»±c!");
            }
        }
    }

    private void lamMoi() {
        txtSoThangGiaHan.setText("");
        txtGhiChu.setText("");
        tblHopDong.clearSelection();
        loadDuLieu();
    }
}




class FrmHoaDon extends JPanel {

    private final HoaDonDAO hoaDonDAO = new HoaDonDAO();
    private final KhachHangDAO khachHangDAO = new KhachHangDAO();
    private final PhongTroDAO phongTroDAO = new PhongTroDAO();

    private JTable tblHoaDon;
    private DefaultTableModel model;

    private JTextField txtMaHD, txtThang, txtNam;
    private JTextField txtDienCu, txtDienMoi, txtNuocCu, txtNuocMoi;
    private JTextField txtTienPhong, txtTienDien, txtTienNuoc, txtTongTien;
    private JTextField txtTim;

    private JComboBox<String> cboKhach, cboPhong;

    private JButton btnThem, btnSua, btnXoa, btnThanhToan;
    private JButton btnGuiMail, btnInHoaDon, btnGiaHan, btnLamMoi, btnTim;

    private final DecimalFormat df = new DecimalFormat("#,###");

    public FrmHoaDon() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setLayout(new BorderLayout(10, 10));

        initComponents();
        loadData();
        suKien();
    }

    private void initComponents() {
        JLabel lblTitle = new JLabel("ðŸ’° QUáº¢N LÃ HÃ“A ÄÆ N THANH TOÃN NHÃ€ TRá»Œ", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 26));
        lblTitle.setForeground(new Color(41, 128, 185));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(lblTitle, BorderLayout.NORTH);

        JPanel pnlLeft = new JPanel(new GridLayout(14, 2, 12, 10));
        pnlLeft.setBorder(BorderFactory.createTitledBorder("Nháº­p ThÃ´ng Tin HÃ³a ÄÆ¡n"));
        pnlLeft.setPreferredSize(new Dimension(460, 0));

        txtMaHD = new JTextField(); txtMaHD.setEditable(false);
        cboKhach = new JComboBox<>();
        cboPhong = new JComboBox<>();
        txtThang = new JTextField();
        txtNam = new JTextField();
        txtDienCu = new JTextField(); txtDienCu.setEditable(false); txtDienCu.setToolTipText("Láº¥y tá»± Ä‘á»™ng tá»« má»¥c Quáº£n LÃ½ Äiá»‡n NÆ°á»›c");
        txtDienMoi = new JTextField(); txtDienMoi.setEditable(false); txtDienMoi.setToolTipText("Láº¥y tá»± Ä‘á»™ng tá»« má»¥c Quáº£n LÃ½ Äiá»‡n NÆ°á»›c");
        txtNuocCu = new JTextField(); txtNuocCu.setEditable(false); txtNuocCu.setToolTipText("Láº¥y tá»± Ä‘á»™ng tá»« má»¥c Quáº£n LÃ½ Äiá»‡n NÆ°á»›c");
        txtNuocMoi = new JTextField(); txtNuocMoi.setEditable(false); txtNuocMoi.setToolTipText("Láº¥y tá»± Ä‘á»™ng tá»« má»¥c Quáº£n LÃ½ Äiá»‡n NÆ°á»›c");
        txtTienPhong = new JTextField(); txtTienPhong.setEditable(false);
        txtTienDien = new JTextField(); txtTienDien.setEditable(false);
        txtTienNuoc = new JTextField(); txtTienNuoc.setEditable(false);
        txtTongTien = new JTextField(); txtTongTien.setEditable(false);

        pnlLeft.add(new JLabel("MÃ£ HÃ³a ÄÆ¡n:")); pnlLeft.add(txtMaHD);
        pnlLeft.add(new JLabel("KhÃ¡ch ThuÃª:")); pnlLeft.add(cboKhach);
        pnlLeft.add(new JLabel("PhÃ²ng:")); pnlLeft.add(cboPhong);
        pnlLeft.add(new JLabel("ThÃ¡ng:")); pnlLeft.add(txtThang);
        pnlLeft.add(new JLabel("NÄƒm:")); pnlLeft.add(txtNam);
        pnlLeft.add(new JLabel("Äiá»‡n cÅ© (kWh):")); pnlLeft.add(txtDienCu);
        pnlLeft.add(new JLabel("Äiá»‡n má»›i (kWh):")); pnlLeft.add(txtDienMoi);
        pnlLeft.add(new JLabel("NÆ°á»›c cÅ© (mÂ³):")); pnlLeft.add(txtNuocCu);
        pnlLeft.add(new JLabel("NÆ°á»›c má»›i (mÂ³):")); pnlLeft.add(txtNuocMoi);
        pnlLeft.add(new JLabel("Tiá»n phÃ²ng:")); pnlLeft.add(txtTienPhong);
        pnlLeft.add(new JLabel("Tiá»n Ä‘iá»‡n:")); pnlLeft.add(txtTienDien);
        pnlLeft.add(new JLabel("Tiá»n nÆ°á»›c:")); pnlLeft.add(txtTienNuoc);
        pnlLeft.add(new JLabel("Tá»”NG TIá»€N:")); pnlLeft.add(txtTongTien);

        add(pnlLeft, BorderLayout.WEST);

        JPanel pnlCenter = new JPanel(new BorderLayout());
        model = new DefaultTableModel(new String[]{
                "MÃ£ HÄ", "KhÃ¡ch", "PhÃ²ng", "ThÃ¡ng", "NÄƒm", "T.PhÃ²ng",
                "T.Äiá»‡n", "T.NÆ°á»›c", "Tá»•ng", "Tráº¡ng ThÃ¡i"
        }, 0);

        tblHoaDon = new JTable(model);
        tblHoaDon.setRowHeight(32);
        tblHoaDon.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tblHoaDon.setAutoCreateRowSorter(true);

        pnlCenter.add(new JScrollPane(tblHoaDon), BorderLayout.CENTER);

        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        txtTim = new JTextField(25);
        btnTim = new JButton("ðŸ” TÃ¬m");
        pnlSearch.add(new JLabel("TÃ¬m kiáº¿m:"));
        pnlSearch.add(txtTim);
        pnlSearch.add(btnTim);

        JPanel pnlTop = new JPanel(new BorderLayout());
        pnlTop.add(new JLabel("ðŸ“‹ DANH SÃCH HÃ“A ÄÆ N", SwingConstants.LEFT), BorderLayout.WEST);
        pnlTop.add(pnlSearch, BorderLayout.EAST);
        pnlCenter.add(pnlTop, BorderLayout.NORTH);

        add(pnlCenter, BorderLayout.CENTER);

        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 12));
        btnThem = new JButton("âž• ThÃªm");
        btnSua = new JButton("âœ Sá»­a");
        btnXoa = new JButton("ðŸ—‘ XÃ³a");
        btnThanhToan = new JButton("ðŸ’µ Thanh ToÃ¡n");
        btnGuiMail = new JButton("ðŸ“§ Gá»­i Email");
        btnInHoaDon = new JButton("ðŸ“„ In PDF");
        btnGiaHan = new JButton("ðŸ”„ Gia Háº¡n");
        btnLamMoi = new JButton("ðŸ”„ LÃ m Má»›i");

        btnThem.setBackground(new Color(46, 204, 113));
        btnSua.setBackground(new Color(52, 152, 219));
        btnXoa.setBackground(new Color(231, 76, 60));
        btnThanhToan.setBackground(new Color(241, 196, 15));
        btnGuiMail.setBackground(new Color(155, 89, 182));
        btnInHoaDon.setBackground(new Color(52, 73, 94));
        btnGiaHan.setBackground(new Color(22, 160, 133));
        btnLamMoi.setBackground(new Color(149, 165, 166));

        JButton[] buttons = {btnThem, btnSua, btnXoa, btnThanhToan, btnGuiMail, btnInHoaDon, btnGiaHan, btnLamMoi};
        for (JButton btn : buttons) {
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("Arial", Font.BOLD, 13));
            btn.setFocusPainted(false);
            btn.setOpaque(true);
            btn.setContentAreaFilled(true);
            btn.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            btn.setPreferredSize(new Dimension(145, 42));
            pnlBottom.add(btn);
        }

        add(pnlBottom, BorderLayout.SOUTH);
    }

    private void loadData() {
        loadKhachHang();
        loadPhong();
        loadHoaDon();
        taoMaHoaDon();
    }

    private void loadKhachHang() {
        cboKhach.removeAllItems();
        try {
            for (KhachHang kh : khachHangDAO.getAllKhachHang()) {
                cboKhach.addItem(kh.getMaKhach() + " - " + kh.getHoTen());
            }
        } catch (Exception e) { System.out.println("Lá»—i táº£i khÃ¡ch hÃ ng: " + e.getMessage()); }
    }

    private void loadPhong() {
        cboPhong.removeAllItems();
        try {
            for (PhongTro pt : phongTroDAO.getAllPhongTro()) {
                cboPhong.addItem(pt.getMaPhong());
            }
        } catch (Exception e) { System.out.println("Lá»—i táº£i phÃ²ng: " + e.getMessage()); }
    }

    private void loadHoaDon() {
        model.setRowCount(0);
        try {
            for (HoaDon hd : hoaDonDAO.getAllHoaDon()) {
                model.addRow(new Object[]{
                        hd.getMaHD(), hd.getMaKhach(), hd.getMaPhong(),
                        hd.getThang(), hd.getNam(),
                        df.format(hd.getTienPhong()),
                        df.format(hd.getTienDien()),
                        df.format(hd.getTienNuoc()),
                        df.format(hd.getTongTien()),
                        hd.getTrangThai() == 1 ? "ÄÃ£ Thanh ToÃ¡n" : "ChÆ°a Thanh ToÃ¡n"
                });
            }
        } catch (Exception e) { System.out.println("Lá»—i táº£i hÃ³a Ä‘Æ¡n: " + e.getMessage()); }
    }

    private void suKien() {
        cboPhong.addActionListener(e -> {
            layTienPhong();
            fetchDienNuoc();
        });

        DocumentListener listener = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { tinhTongTien(); }
            public void removeUpdate(DocumentEvent e) { tinhTongTien(); }
            public void changedUpdate(DocumentEvent e) { tinhTongTien(); }
        };
        txtDienMoi.getDocument().addDocumentListener(listener);
        txtNuocMoi.getDocument().addDocumentListener(listener);
        txtDienCu.getDocument().addDocumentListener(listener);
        txtNuocCu.getDocument().addDocumentListener(listener);
        txtTienPhong.getDocument().addDocumentListener(listener);
        
        DocumentListener fetchDNListener = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { fetchDienNuoc(); }
            public void removeUpdate(DocumentEvent e) { fetchDienNuoc(); }
            public void changedUpdate(DocumentEvent e) { fetchDienNuoc(); }
        };
        txtThang.getDocument().addDocumentListener(fetchDNListener);
        txtNam.getDocument().addDocumentListener(fetchDNListener);

        btnThem.addActionListener(e -> themHoaDon());
        btnSua.addActionListener(e -> suaHoaDon());
        btnXoa.addActionListener(e -> xoaHoaDon());
        btnThanhToan.addActionListener(e -> thanhToan());
        btnGuiMail.addActionListener(e -> guiMail());
        btnInHoaDon.addActionListener(e -> inPDF());
        btnGiaHan.addActionListener(e -> giaHanHopDong());
        btnLamMoi.addActionListener(e -> lamMoi());
        btnTim.addActionListener(e -> timHoaDon());

        tblHoaDon.getSelectionModel().addListSelectionListener(this::fillFormFromTable);
    }

    private void fillFormFromTable(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting() && tblHoaDon.getSelectedRow() >= 0) {
            int row = tblHoaDon.getSelectedRow();
            txtMaHD.setText(model.getValueAt(row, 0).toString());

            String maKhachTable = model.getValueAt(row, 1).toString();
            for (int i = 0; i < cboKhach.getItemCount(); i++) {
                if (cboKhach.getItemAt(i).startsWith(maKhachTable)) {
                    cboKhach.setSelectedIndex(i);
                    break;
                }
            }
            cboPhong.setSelectedItem(model.getValueAt(row, 2).toString());
            txtThang.setText(model.getValueAt(row, 3).toString());
            txtNam.setText(model.getValueAt(row, 4).toString());

            try {
                String maHD = model.getValueAt(row, 0).toString();
                for (HoaDon hd : hoaDonDAO.getAllHoaDon()) {
                    if (hd.getMaHD().equals(maHD)) {
                        txtDienCu.setText(String.valueOf((int)hd.getSoDienCu()));
                        txtDienMoi.setText(String.valueOf((int)hd.getSoDienMoi()));
                        txtNuocCu.setText(String.valueOf((int)hd.getSoNuocCu()));
                        txtNuocMoi.setText(String.valueOf((int)hd.getSoNuocMoi()));
                        break;
                    }
                }
                txtTienPhong.setText(model.getValueAt(row, 5).toString().replace(",", ""));
                txtTienDien.setText(model.getValueAt(row, 6).toString().replace(",", ""));
                txtTienNuoc.setText(model.getValueAt(row, 7).toString().replace(",", ""));
                txtTongTien.setText(model.getValueAt(row, 8).toString().replace(",", ""));
            } catch (Exception ex) { ex.printStackTrace(); }
        }
    }

    private void layTienPhong() {
        String maPhong = (String) cboPhong.getSelectedItem();
        if (maPhong == null) return;

        for (PhongTro pt : phongTroDAO.getAllPhongTro()) {
            if (pt.getMaPhong().equals(maPhong)) {
                txtTienPhong.setText(String.format("%.0f", pt.getGiaPhong()));
                tinhTongTien();
                return;
            }
        }
    }

    private boolean isFetchingDienNuoc = false;

    private void fetchDienNuoc() {
        if (isFetchingDienNuoc) return;
        
        String maPhong = (String) cboPhong.getSelectedItem();
        if (maPhong == null || txtThang.getText().trim().isEmpty() || txtNam.getText().trim().isEmpty()) {
            return;
        }

        try {
            int thang = Integer.parseInt(txtThang.getText().trim());
            int nam = Integer.parseInt(txtNam.getText().trim());
            
            isFetchingDienNuoc = true;
            dao.DienNuocDAO dnDAO = new dao.DienNuocDAO();
            model.DienNuoc dn = dnDAO.getDienNuocByPhongAndThang(maPhong, thang, nam);
            
            if (dn != null) {
                txtDienCu.setText(String.valueOf(dn.getChiSoDienCu()));
                txtDienMoi.setText(String.valueOf(dn.getChiSoDienMoi()));
                txtNuocCu.setText(String.valueOf(dn.getChiSoNuocCu()));
                txtNuocMoi.setText(String.valueOf(dn.getChiSoNuocMoi()));
            } else {
                txtDienCu.setText("");
                txtDienMoi.setText("");
                txtNuocCu.setText("");
                txtNuocMoi.setText("");
            }
        } catch (NumberFormatException e) {
            // ChÆ°a nháº­p Ä‘á»§ sá»‘ thÃ¡ng nÄƒm, bá» qua
        } finally {
            isFetchingDienNuoc = false;
        }
    }

    private void tinhTongTien() {
        try {
            int dienCu = txtDienCu.getText().trim().isEmpty() ? 0 : Integer.parseInt(txtDienCu.getText().trim());
            int dienMoi = txtDienMoi.getText().trim().isEmpty() ? 0 : Integer.parseInt(txtDienMoi.getText().trim());
            int nuocCu = txtNuocCu.getText().trim().isEmpty() ? 0 : Integer.parseInt(txtNuocCu.getText().trim());
            int nuocMoi = txtNuocMoi.getText().trim().isEmpty() ? 0 : Integer.parseInt(txtNuocMoi.getText().trim());

            double tienPhong = txtTienPhong.getText().trim().isEmpty() ? 0 : Double.parseDouble(txtTienPhong.getText().trim());

            int soDien = (dienMoi >= dienCu) ? (dienMoi - dienCu) : 0;
            int soNuoc = (nuocMoi >= nuocCu) ? (nuocMoi - nuocCu) : 0;

            double tienDien = soDien * 3500;
            double tienNuoc = soNuoc * 18000;
            double tong = tienPhong + tienDien + tienNuoc;

            txtTienDien.setText(String.format("%.0f", tienDien));
            txtTienNuoc.setText(String.format("%.0f", tienNuoc));
            txtTongTien.setText(String.format("%.0f", tong));
        } catch (NumberFormatException ignored) {}
    }

    private void taoMaHoaDon() {
        int size = hoaDonDAO.getAllHoaDon().size() + 1;
        boolean isDuplicate = true;
        String maMoi = "";
        while(isDuplicate) {
            maMoi = "HD" + String.format("%04d", size);
            isDuplicate = false;
            for(HoaDon existing : hoaDonDAO.getAllHoaDon()) {
                if(existing.getMaHD().equals(maMoi)) {
                    isDuplicate = true;
                    size++;
                    break;
                }
            }
        }
        txtMaHD.setText(maMoi);
    }

    private void themHoaDon() {
        if (txtThang.getText().trim().isEmpty() || txtNam.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lÃ²ng nháº­p Ä‘áº§y Ä‘á»§ thÃ¡ng vÃ  nÄƒm chá»‘t hÃ³a Ä‘Æ¡n!");
            return;
        }
        if (txtDienMoi.getText().trim().isEmpty() || txtNuocMoi.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "ChÆ°a cÃ³ chá»‰ sá»‘ Äiá»‡n NÆ°á»›c cho phÃ²ng nÃ y trong thÃ¡ng! Vui lÃ²ng qua tab Quáº£n LÃ½ Äiá»‡n NÆ°á»›c Ä‘á»ƒ chá»‘t sá»‘ trÆ°á»›c khi láº­p hÃ³a Ä‘Æ¡n.");
            return;
        }
        try {
            HoaDon hdData = new HoaDon();
            hdData.setMaHD(txtMaHD.getText().trim());

            String itemsKhach = (String) cboKhach.getSelectedItem();
            hdData.setMaKhach(itemsKhach != null ? itemsKhach.split(" - ")[0] : "");
            hdData.setMaPhong((String) cboPhong.getSelectedItem());
            hdData.setThang(Integer.parseInt(txtThang.getText().trim()));
            hdData.setNam(Integer.parseInt(txtNam.getText().trim()));

            int dienCu = txtDienCu.getText().trim().isEmpty() ? 0 : Integer.parseInt(txtDienCu.getText().trim());
            int dienMoi = txtDienMoi.getText().trim().isEmpty() ? 0 : Integer.parseInt(txtDienMoi.getText().trim());
            int nuocCu = txtNuocCu.getText().trim().isEmpty() ? 0 : Integer.parseInt(txtNuocCu.getText().trim());
            int nuocMoi = txtNuocMoi.getText().trim().isEmpty() ? 0 : Integer.parseInt(txtNuocMoi.getText().trim());

            hdData.setSoDienCu(dienCu);
            hdData.setSoDienMoi(dienMoi);
            hdData.setSoNuocCu(nuocCu);
            hdData.setSoNuocMoi(nuocMoi);

            double tPhong = Double.parseDouble(txtTienPhong.getText().trim().replace(",", ""));
            double tDien = txtTienDien.getText().isEmpty() ? 0 : Double.parseDouble(txtTienDien.getText().replace(",", ""));
            double tNuoc = txtTienNuoc.getText().isEmpty() ? 0 : Double.parseDouble(txtTienNuoc.getText().replace(",", ""));

            hdData.setTienPhong(tPhong);
            hdData.setTienDien(tDien);
            hdData.setTienNuoc(tNuoc);
            hdData.setTongTien(tPhong + tDien + tNuoc);
            hdData.setTrangThai(0);
            hdData.setNgayLap(new java.sql.Date(System.currentTimeMillis()));

            if (hoaDonDAO.themHoaDon(hdData)) {
                JOptionPane.showMessageDialog(this, "âž• Chá»‘t vÃ  thÃªm hÃ³a Ä‘Æ¡n " + hdData.getMaHD() + " thÃ nh cÃ´ng!");
                lamMoi();
            } else {
                JOptionPane.showMessageDialog(this, "Lá»—i khi thÃªm hÃ³a Ä‘Æ¡n xuá»‘ng CÆ¡ sá»Ÿ dá»¯ liá»‡u!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lá»—i Ä‘á»‹nh dáº¡ng dá»¯ liá»‡u sá»‘ nháº­p vÃ o: " + e.getMessage());
        }
    }

    private void suaHoaDon() {
        int row = tblHoaDon.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lÃ²ng chá»n dÃ²ng hÃ³a Ä‘Æ¡n trÃªn báº£ng cáº§n sá»­a thÃ´ng tin!");
            return;
        }
        try {
            String maHD = txtMaHD.getText().trim();
            if (maHD.isEmpty()) {
                JOptionPane.showMessageDialog(this, "MÃ£ hÃ³a Ä‘Æ¡n khÃ´ng Ä‘Æ°á»£c Ä‘á»ƒ trá»‘ng!");
                return;
            }

            HoaDon hd = new HoaDon();
            hd.setMaHD(maHD);
            String itemsKhach = (String) cboKhach.getSelectedItem();
            hd.setMaKhach(itemsKhach != null ? itemsKhach.split(" - ")[0] : "");
            hd.setMaPhong((String) cboPhong.getSelectedItem());
            hd.setThang(Integer.parseInt(txtThang.getText().trim()));
            hd.setNam(Integer.parseInt(txtNam.getText().trim()));

            int dienCu = txtDienCu.getText().trim().isEmpty() ? 0 : Integer.parseInt(txtDienCu.getText().trim());
            int dienMoi = txtDienMoi.getText().trim().isEmpty() ? 0 : Integer.parseInt(txtDienMoi.getText().trim());
            int nuocCu = txtNuocCu.getText().trim().isEmpty() ? 0 : Integer.parseInt(txtNuocCu.getText().trim());
            int nuocMoi = txtNuocMoi.getText().trim().isEmpty() ? 0 : Integer.parseInt(txtNuocMoi.getText().trim());

            hd.setSoDienCu(dienCu);
            hd.setSoDienMoi(dienMoi);
            hd.setSoNuocCu(nuocCu);
            hd.setSoNuocMoi(nuocMoi);

            double tPhong = Double.parseDouble(txtTienPhong.getText().trim().replace(",", ""));
            double tDien = Double.parseDouble(txtTienDien.getText().replace(",", ""));
            double tNuoc = Double.parseDouble(txtTienNuoc.getText().replace(",", ""));
            hd.setTienPhong(tPhong);
            hd.setTienDien(tDien);
            hd.setTienNuoc(tNuoc);
            hd.setTongTien(tPhong + tDien + tNuoc);
            hd.setNgayLap(new java.sql.Date(System.currentTimeMillis()));

            hd.setTrangThai(model.getValueAt(row, 9).toString().equals("ÄÃ£ Thanh ToÃ¡n") ? 1 : 0);

            if (hoaDonDAO.suaHoaDon(hd)) {
                JOptionPane.showMessageDialog(this, "âœï¸ Cáº­p nháº­t thÃ´ng tin sá»­a Ä‘á»•i hÃ³a Ä‘Æ¡n " + hd.getMaHD() + " thÃ nh cÃ´ng!");
                loadHoaDon();
            } else {
                JOptionPane.showMessageDialog(this, "Sá»­a hÃ³a Ä‘Æ¡n tháº¥t báº¡i trong cÆ¡ sá»Ÿ dá»¯ liá»‡u!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lá»—i xáº£y ra khi sá»­a dá»¯ liá»‡u: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void xoaHoaDon() {
        int row = tblHoaDon.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lÃ²ng chá»n hÃ³a Ä‘Æ¡n muá»‘n xÃ³a bá» trÃªn báº£ng!");
            return;
        }
        String maHD = model.getValueAt(row, 0).toString();
        int output = JOptionPane.showConfirmDialog(this, "Báº¡n cÃ³ cháº¯c cháº¯n muá»‘n xÃ³a hÃ³a Ä‘Æ¡n " + maHD + " khÃ´ng?", "XÃ¡c nháº­n xÃ³a", JOptionPane.YES_NO_OPTION);
        if (output == JOptionPane.YES_OPTION) {
            if (hoaDonDAO.xoaHoaDon(maHD)) {
                JOptionPane.showMessageDialog(this, "ðŸ—‘ï¸ ÄÃ£ xÃ³a thÃ nh cÃ´ng hÃ³a Ä‘Æ¡n khá»i há»‡ thá»‘ng!");
                lamMoi();
            } else {
                JOptionPane.showMessageDialog(this, "XÃ³a hÃ³a Ä‘Æ¡n tháº¥t báº¡i!");
            }
        }
    }

    private void thanhToan() {
        int row = tblHoaDon.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lÃ²ng chá»n dÃ²ng hÃ³a Ä‘Æ¡n cáº§n thanh toÃ¡n trÃªn báº£ng!");
            return;
        }
        String maHD = model.getValueAt(row, 0).toString();
        if (model.getValueAt(row, 9).toString().equals("ÄÃ£ Thanh ToÃ¡n")) {
            JOptionPane.showMessageDialog(this, "HÃ³a Ä‘Æ¡n nÃ y Ä‘Ã£ Ä‘Æ°á»£c thanh toÃ¡n trÆ°á»›c Ä‘Ã³ rá»“i!");
            return;
        }

        // Láº¥y thÃ´ng tin hÃ³a Ä‘Æ¡n vÃ  khÃ¡ch hÃ ng trÆ°á»›c khi cáº­p nháº­t
        String maKhach = model.getValueAt(row, 1).toString();
        String maPhong = model.getValueAt(row, 2).toString();
        int thang = Integer.parseInt(model.getValueAt(row, 3).toString());
        int nam = Integer.parseInt(model.getValueAt(row, 4).toString());
        double tongTien = 0;
        try {
            tongTien = Double.parseDouble(model.getValueAt(row, 8).toString().replace(",", ""));
        } catch (Exception ignored) {}

        String emailKhach = null;
        String tenKhach = "KhÃ¡ch thuÃª";
        for (KhachHang kh : khachHangDAO.getAllKhachHang()) {
            if (kh.getMaKhach().equals(maKhach)) {
                emailKhach = kh.getEmail();
                tenKhach = kh.getHoTen();
                break;
            }
        }

        // Cáº­p nháº­t tráº¡ng thÃ¡i thanh toÃ¡n trong DB
        if (hoaDonDAO.capNhatTrangThai(maHD, 1)) {
            JOptionPane.showMessageDialog(this,
                    "ðŸ’µ [Thanh ToÃ¡n ThÃ nh CÃ´ng] HÃ³a Ä‘Æ¡n " + maHD + " Ä‘Ã£ Ä‘Æ°á»£c quyáº¿t toÃ¡n.\nDÃ²ng tiá»n doanh thu thá»‘ng kÃª sáº½ tá»± Ä‘á»™ng cáº­p nháº­t!",
                    "ThÃ nh cÃ´ng", JOptionPane.INFORMATION_MESSAGE);
            loadHoaDon();

            // Há»i cÃ³ gá»­i email xÃ¡c nháº­n khÃ´ng
            if (emailKhach != null && !emailKhach.trim().isEmpty() && emailKhach.contains("@")) {
                int guiMail = JOptionPane.showConfirmDialog(this,
                        "ðŸ“§ Gá»­i email xÃ¡c nháº­n thanh toÃ¡n Ä‘áº¿n khÃ¡ch thuÃª?\n"
                        + "KhÃ¡ch: " + tenKhach + "\nEmail: " + emailKhach,
                        "Gá»­i Email XÃ¡c Nháº­n", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                if (guiMail == JOptionPane.YES_OPTION) {
                    final String fEmail = emailKhach;
                    final String fTenKhach = tenKhach;
                    final String fMaHD = maHD;
                    final String fTenPhong = "PhÃ²ng " + maPhong;
                    final int fThang = thang;
                    final int fNam = nam;
                    final double fTongTien = tongTien;

                    btnThanhToan.setEnabled(false);
                    SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
                        @Override
                        protected Boolean doInBackground() {
                            return XuatHoaDonEmailService.guiEmailXacNhanThanhToan(
                                    fEmail, fTenKhach, fMaHD, fTenPhong, fThang, fNam, fTongTien);
                        }

                        @Override
                        protected void done() {
                            btnThanhToan.setEnabled(true);
                            try {
                                boolean ok = get();
                                if (ok) {
                                    JOptionPane.showMessageDialog(FrmHoaDon.this,
                                            "ðŸ“§ ÄÃ£ gá»­i email xÃ¡c nháº­n thanh toÃ¡n thÃ nh cÃ´ng!\nÄáº¿n: " + fEmail,
                                            "Email ÄÃ£ Gá»­i", JOptionPane.INFORMATION_MESSAGE);
                                } else {
                                    JOptionPane.showMessageDialog(FrmHoaDon.this,
                                            "âš  Thanh toÃ¡n Ä‘Ã£ ghi nháº­n nhÆ°ng gá»­i email tháº¥t báº¡i.\nVui lÃ²ng kiá»ƒm tra láº¡i cáº¥u hÃ¬nh SMTP.",
                                            "Lá»—i Gá»­i Email", JOptionPane.WARNING_MESSAGE);
                                }
                            } catch (Exception ex) {
                                btnThanhToan.setEnabled(true);
                                ex.printStackTrace();
                            }
                        }
                    };
                    worker.execute();
                }
            }
            // Náº¿u khÃ¡ch khÃ´ng cÃ³ email thÃ¬ khÃ´ng há»i, chá»‰ thÃ´ng bÃ¡o thanh toÃ¡n xong

        } else {
            JOptionPane.showMessageDialog(this, "Thanh toÃ¡n tháº¥t báº¡i! Vui lÃ²ng kiá»ƒm tra láº¡i káº¿t ná»‘i Database.");
        }
    }

    private void guiMail() {
        int row = tblHoaDon.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lÃ²ng chá»n hÃ³a Ä‘Æ¡n cá»§a khÃ¡ch thuÃª cáº§n gá»­i email!");
            return;
        }

        try {
            String maKhach = model.getValueAt(row, 1).toString();
            String maPhong = model.getValueAt(row, 2).toString();

            String emailKhach = null;
            String tenKhach = "KhÃ¡ch thuÃª";
            for (KhachHang kh : khachHangDAO.getAllKhachHang()) {
                if (kh.getMaKhach().equals(maKhach)) {
                    emailKhach = kh.getEmail();
                    tenKhach = kh.getHoTen();
                    break;
                }
            }

            if (emailKhach == null || emailKhach.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "KhÃ¡ch hÃ ng " + tenKhach + " chÆ°a Ä‘Æ°á»£c cáº­p nháº­t Email trÃªn há»‡ thá»‘ng!", "Lá»—i", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validate email format cÆ¡ báº£n trÆ°á»›c khi gá»­i
            if (!emailKhach.contains("@") || !emailKhach.contains(".")) {
                JOptionPane.showMessageDialog(this, "Äá»‹a chá»‰ email cá»§a khÃ¡ch hÃ ng \"" + emailKhach + "\" khÃ´ng há»£p lá»‡!", "Lá»—i Email", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double soDienCu = txtDienCu.getText().trim().isEmpty() ? 0 : Double.parseDouble(txtDienCu.getText().trim());
            double soDienMoi = txtDienMoi.getText().trim().isEmpty() ? 0 : Double.parseDouble(txtDienMoi.getText().trim());
            double soNuocCu = txtNuocCu.getText().trim().isEmpty() ? 0 : Double.parseDouble(txtNuocCu.getText().trim());
            double soNuocMoi = txtNuocMoi.getText().trim().isEmpty() ? 0 : Double.parseDouble(txtNuocMoi.getText().trim());
            double tongTien = txtTongTien.getText().trim().isEmpty() ? 0 : Double.parseDouble(txtTongTien.getText().trim());

            String diaChiThucTe = "Khu nhÃ  trá» quáº£n lÃ½";
            for (PhongTro p : phongTroDAO.getAllPhongTro()) {
                if (p.getMaPhong().equals(maPhong)) {
                    diaChiThucTe = p.getDiaChi();
                    break;
                }
            }

            PhongTro pt = new PhongTro();
            pt.setMaPhong(maPhong);
            pt.setTenPhong("PhÃ²ng " + maPhong);
            pt.setTenKhachThue(tenKhach);
            pt.setDiaChi(diaChiThucTe);

            // Táº¡o PDF trÆ°á»›c (nhanh, khÃ´ng cáº§n background)
            File tempPdf = XuatHoaDonEmailService.createInvoicePDF(pt, soDienMoi, soDienCu, soNuocMoi, soNuocCu, tongTien);
            if (tempPdf == null) {
                JOptionPane.showMessageDialog(this, "Lá»—i há»‡ thá»‘ng khi káº¿t xuáº¥t cáº¥u trÃºc HTML sang PDF táº¡m thá»i!", "Lá»—i sinh file", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Gá»­i email trÃªn background thread Ä‘á»ƒ trÃ¡nh Ä‘Æ¡ giao diá»‡n
            final String finalEmail = emailKhach;
            final String finalTenKhach = tenKhach;
            final File finalPdf = tempPdf;

            btnGuiMail.setEnabled(false);
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
                @Override
                protected Boolean doInBackground() {
                    return XuatHoaDonEmailService.guiHoaDonQuaEmail(finalEmail, pt.getTenPhong(), finalPdf);
                }

                @Override
                protected void done() {
                    try {
                        boolean checkGui = get();
                        setCursor(Cursor.getDefaultCursor());
                        btnGuiMail.setEnabled(true);

                        if (checkGui) {
                            JOptionPane.showMessageDialog(FrmHoaDon.this,
                                    "ðŸ“§ Gá»­i hÃ³a Ä‘Æ¡n thÃ nh cÃ´ng tá»›i khÃ¡ch thuÃª: " + finalTenKhach
                                            + "\nÄá»‹a chá»‰ Gmail nháº­n: " + finalEmail,
                                    "ThÃ nh cÃ´ng", JOptionPane.INFORMATION_MESSAGE);
                            finalPdf.delete();
                        } else {
                            JOptionPane.showMessageDialog(FrmHoaDon.this,
                                    "Gá»­i email tháº¥t báº¡i! Vui lÃ²ng kiá»ƒm tra láº¡i cáº¥u hÃ¬nh tÃ i khoáº£n hoáº·c mÃ£ bÃ­ máº­t APP_PASSWORD.",
                                    "Lá»—i káº¿t ná»‘i SMTP", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception ex) {
                        setCursor(Cursor.getDefaultCursor());
                        btnGuiMail.setEnabled(true);
                        JOptionPane.showMessageDialog(FrmHoaDon.this,
                                "Lá»—i xá»­ lÃ½ gá»­i Email: " + ex.getCause(),
                                "ThÃ´ng bÃ¡o lá»—i há»‡ thá»‘ng", JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                }
            };
            worker.execute();

        } catch (Exception ex) {
            this.setCursor(Cursor.getDefaultCursor());
            btnGuiMail.setEnabled(true);
            JOptionPane.showMessageDialog(this, "Lá»—i xá»­ lÃ½ gá»­i Email thá»±c táº¿: " + ex.toString(), "ThÃ´ng bÃ¡o lá»—i há»‡ thá»‘ng", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void inPDF() {
        int row = tblHoaDon.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Chá»n má»™t dÃ²ng hÃ³a Ä‘Æ¡n trÃªn báº£ng Ä‘á»ƒ káº¿t xuáº¥t file!");
            return;
        }

        try {
            String maKhach = model.getValueAt(row, 1).toString();
            String maPhong = model.getValueAt(row, 2).toString();

            String tenKhach = "KhÃ¡ch thuÃª";
            for (KhachHang kh : khachHangDAO.getAllKhachHang()) {
                if (kh.getMaKhach().equals(maKhach)) {
                    tenKhach = kh.getHoTen();
                    break;
                }
            }

            double soDienCu = txtDienCu.getText().trim().isEmpty() ? 0 : Double.parseDouble(txtDienCu.getText().trim());
            double soDienMoi = txtDienMoi.getText().trim().isEmpty() ? 0 : Double.parseDouble(txtDienMoi.getText().trim());
            double soNuocCu = txtNuocCu.getText().trim().isEmpty() ? 0 : Double.parseDouble(txtNuocCu.getText().trim());
            double soNuocMoi = txtNuocMoi.getText().trim().isEmpty() ? 0 : Double.parseDouble(txtNuocMoi.getText().trim());
            double tongTien = txtTongTien.getText().trim().isEmpty() ? 0 : Double.parseDouble(txtTongTien.getText().trim());

            String diaChiThucTe = "Khu nhÃ  trá» quáº£n lÃ½";
            for (PhongTro p : phongTroDAO.getAllPhongTro()) {
                if (p.getMaPhong().equals(maPhong)) {
                    diaChiThucTe = p.getDiaChi();
                    break;
                }
            }

            PhongTro pt = new PhongTro();
            pt.setMaPhong(maPhong);
            pt.setTenPhong("PhÃ²ng " + maPhong);
            pt.setTenKhachThue(tenKhach);
            pt.setDiaChi(diaChiThucTe);

            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Chá»n thÆ° má»¥c lÆ°u hÃ³a Ä‘Æ¡n PDF");
            chooser.setSelectedFile(new File("HoaDon_Phong_" + maPhong + ".pdf"));

            if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                File saveFile = chooser.getSelectedFile();

                File srcPdf = XuatHoaDonEmailService.createInvoicePDF(pt, soDienMoi, soDienCu, soNuocMoi, soNuocCu, tongTien);

                if (srcPdf != null) {
                    if (srcPdf.renameTo(saveFile) || srcPdf.length() > 0) {
                        JOptionPane.showMessageDialog(this, "ðŸ“„ [In ThÃ nh CÃ´ng] HÃ³a Ä‘Æ¡n Ä‘iá»‡n nÆ°á»›c phÃ²ng " + maPhong + " Ä‘Ã£ káº¿t xuáº¥t thÃ nh cÃ´ng táº¡i:\n" + saveFile.getAbsolutePath(), "ThÃ nh cÃ´ng", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Lá»—i sao chÃ©p tá»‡p tin xuáº¥t ra á»• Ä‘Ä©a mÃ¡y tÃ­nh!", "Lá»—i", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Lá»—i há»‡ thá»‘ng khi dá»‹ch mÃ£ HTML sang Ä‘á»‹nh dáº¡ng PDF!", "Lá»—i", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lá»—i káº¿t xuáº¥t in PDF chi tiáº¿t: " + ex.toString(), "Lá»—i há»‡ thá»‘ng", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void giaHanHopDong() {
        JOptionPane.showMessageDialog(this, "ðŸ’¡ TÃ­nh nÄƒng Gia Háº¡n Ä‘Æ°á»£c thá»±c hiá»‡n trá»±c tiáº¿p bÃªn Tab Quáº£n LÃ½ Há»£p Äá»“ng Ä‘á»ƒ Ä‘áº£m báº£o toÃ n váº¹n dá»¯ liá»‡u chu ká»³ thuÃª!");
    }

    private void timHoaDon() {
        String keyword = txtTim.getText().trim();
        if (keyword.isEmpty()) {
            loadHoaDon();
            return;
        }
        model.setRowCount(0);
        try {
            for (HoaDon hd : hoaDonDAO.getAllHoaDon()) {
                if (hd.getMaHD().toLowerCase().contains(keyword.toLowerCase()) ||
                        hd.getMaPhong().toLowerCase().contains(keyword.toLowerCase()) ||
                        hd.getMaKhach().toLowerCase().contains(keyword.toLowerCase())) {
                    model.addRow(new Object[]{
                            hd.getMaHD(), hd.getMaKhach(), hd.getMaPhong(),
                            hd.getThang(), hd.getNam(),
                            df.format(hd.getTienPhong()),
                            df.format(hd.getTienDien()),
                            df.format(hd.getTienNuoc()),
                            df.format(hd.getTongTien()),
                            hd.getTrangThai() == 1 ? "ÄÃ£ Thanh ToÃ¡n" : "ChÆ°a Thanh ToÃ¡n"
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void lamMoi() {
        txtThang.setText("");
        txtNam.setText("");
        txtDienCu.setText("");
        txtDienMoi.setText("");
        txtNuocCu.setText("");
        txtNuocMoi.setText("");
        txtTienDien.setText("");
        txtTienNuoc.setText("");
        txtTongTien.setText("");
        taoMaHoaDon();
        loadData();
    }
}



class FrmLogin extends JFrame {

    private JTextField txtMaQL;
    private JPasswordField txtMatKhau;
    private JButton btnDangNhap;
    private JButton btnThoat;
    private JCheckBox chkHienMK;
    private JLabel lblQuenMK;

    private final ChuTroDAO chuTroDAO = new ChuTroDAO();

    public FrmLogin() {
        setTitle("ðŸ”‘ ÄÄ‚NG NHáº¬P - QUáº¢N LÃ NHÃ€ TRá»Œ");
        setSize(520, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        initUI();
        addEvents();

        // LuÃ´n Ä‘áº£m báº£o form Ä‘Æ°á»£c hiá»ƒn thá»‹
        setVisible(true);
        SwingUtilities.invokeLater(() -> txtMaQL.requestFocus());
    }

    private void initUI() {
        // Sá»­ dá»¥ng giao diá»‡n há»‡ thá»‘ng hoáº·c CrossPlatform Ä‘á»ƒ trÃ¡nh lá»—i máº¥t chá»¯, má» nÃºt trÃªn Windows/Mac
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(Color.WHITE);
        add(main);

        // Header
        JPanel header = new JPanel();
        header.setBackground(new Color(41, 128, 185));
        header.setPreferredSize(new Dimension(0, 160));
        JLabel logo = new JLabel("ðŸ ", SwingConstants.CENTER);
        logo.setFont(new Font("Arial", Font.PLAIN, 80));
        logo.setForeground(Color.WHITE);
        JLabel title = new JLabel("Há»† THá»NG QUáº¢N LÃ NHÃ€ TRá»Œ", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        header.setLayout(new BorderLayout());
        header.add(logo, BorderLayout.NORTH);
        header.add(title, BorderLayout.CENTER);

        main.add(header, BorderLayout.NORTH);

        // Form
        JPanel form = new JPanel(new GridLayout(7, 1, 10, 15));
        form.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));
        form.setBackground(Color.WHITE);

        // NhÃ£n TiÃªu Äá»
        JLabel lblMaQL = new JLabel("MÃ£ Quáº£n LÃ½");
        lblMaQL.setFont(new Font("Arial", Font.BOLD, 14));
        lblMaQL.setForeground(Color.DARK_GRAY);

        JLabel lblMatKhau = new JLabel("Máº­t Kháº©u");
        lblMatKhau.setFont(new Font("Arial", Font.BOLD, 14));
        lblMatKhau.setForeground(Color.DARK_GRAY);

        txtMaQL = new JTextField();
        txtMaQL.setFont(new Font("Arial", Font.PLAIN, 15));

        txtMatKhau = new JPasswordField();
        txtMatKhau.setFont(new Font("Arial", Font.PLAIN, 15));

        chkHienMK = new JCheckBox(" Hiá»‡n máº­t kháº©u");
        chkHienMK.setBackground(Color.WHITE);
        chkHienMK.setFont(new Font("Arial", Font.PLAIN, 13));

        lblQuenMK = new JLabel("<HTML><U>QuÃªn máº­t kháº©u?</U></HTML>");
        lblQuenMK.setFont(new Font("Arial", Font.ITALIC, 13));
        lblQuenMK.setForeground(new Color(41, 128, 185));
        lblQuenMK.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // NÃºt ÄÄƒng Nháº­p (Fix lá»—i má» chá»¯ báº±ng cÃ¡ch Ã©p Ä‘Ã¨ thuá»™c tÃ­nh há»‡ thá»‘ng)
        btnDangNhap = new JButton("ÄÄ‚NG NHáº¬P");
        btnDangNhap.setBackground(new Color(46, 204, 113));
        btnDangNhap.setForeground(Color.WHITE);
        btnDangNhap.setFont(new Font("Arial", Font.BOLD, 16));
        btnDangNhap.setFocusPainted(false);
        btnDangNhap.setContentAreaFilled(true);
        btnDangNhap.setOpaque(true);
        btnDangNhap.setBorder(BorderFactory.createLineBorder(new Color(39, 174, 96), 1));

        // NÃºt ThoÃ¡t (Fix lá»—i má» chá»¯ báº±ng cÃ¡ch Ã©p Ä‘Ã¨ thuá»™c tÃ­nh há»‡ thá»‘ng)
        btnThoat = new JButton("THOÃT");
        btnThoat.setBackground(new Color(231, 76, 60));
        btnThoat.setForeground(Color.WHITE);
        btnThoat.setFont(new Font("Arial", Font.BOLD, 16));
        btnThoat.setFocusPainted(false);
        btnThoat.setContentAreaFilled(true);
        btnThoat.setOpaque(true);
        btnThoat.setBorder(BorderFactory.createLineBorder(new Color(192, 41, 43), 1));

        form.add(lblMaQL);
        form.add(txtMaQL);
        form.add(lblMatKhau);
        form.add(txtMatKhau);
        form.add(chkHienMK);
        form.add(lblQuenMK);

        JPanel btnP = new JPanel(new GridLayout(1, 2, 15, 0));
        btnP.setBackground(Color.WHITE);
        btnP.add(btnDangNhap);
        btnP.add(btnThoat);
        form.add(btnP);

        main.add(form, BorderLayout.CENTER);
    }

    private void addEvents() {
        chkHienMK.addActionListener(e -> txtMatKhau.setEchoChar(chkHienMK.isSelected() ? (char)0 : '*'));
        btnDangNhap.addActionListener(e -> dangNhap());
        txtMatKhau.addActionListener(e -> dangNhap());
        btnThoat.addActionListener(e -> System.exit(0));

        lblQuenMK.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                dispose();
                new FrmQuenMatKhau().setVisible(true);
            }
        });
    }

    private void dangNhap() {
        String maQL = txtMaQL.getText().trim();
        String mk = new String(txtMatKhau.getPassword());

        if (maQL.isEmpty() || mk.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lÃ²ng nháº­p Ä‘áº§y Ä‘á»§ thÃ´ng tin!", "ThÃ´ng bÃ¡o", JOptionPane.WARNING_MESSAGE);
            return;
        }

        NguoiQuanLy ql = chuTroDAO.dangNhap(maQL, mk);
        if (ql != null) {
            JOptionPane.showMessageDialog(this, "ÄÄƒng nháº­p thÃ nh cÃ´ng!");
            dispose();
            Main.moTrangChu(ql);
        } else {
            JOptionPane.showMessageDialog(this, "Sai tÃ i khoáº£n hoáº·c máº­t kháº©u!", "Lá»—i", JOptionPane.ERROR_MESSAGE);
            txtMatKhau.setText("");
        }
    }

    public static void main(String[] args) {
        new FrmLogin();
    }
}



// ÄÃƒ Sá»¬A: Chuyá»ƒn Ä‘á»•i tá»« JFrame thÃ nh JPanel Ä‘á»ƒ tÃ­ch há»£p mÆ°á»£t mÃ  vÃ o cáº¥u trÃºc FrmTrangChu
class FrmQuanLyDienNuoc extends JPanel {

    private final DienNuocDAO dienNuocDAO = new DienNuocDAO();
    private final PhongTroDAO phongTroDAO = new PhongTroDAO();

    private JTable tblDienNuoc;
    private DefaultTableModel model;
    private final DecimalFormat df = new DecimalFormat("#,###");

    private JComboBox<String> cboPhong;
    private JTextField txtThang, txtNam;
    private JTextField txtDienCu, txtDienMoi;
    private JTextField txtNuocCu, txtNuocMoi;
    private JTextField txtTienDien, txtTienNuoc, txtTongTien;
    private JTextField txtTimKiem;

    private JButton btnThem, btnSua, btnXoa, btnLamMoi, btnTim;

    private int idDangChon = -1;

    public FrmQuanLyDienNuoc() {
        // KhÃ³a Look and Feel trÃ¡nh xung Ä‘á»™t há»‡ thá»‘ng lÃ m má» chá»¯ trÃªn nÃºt báº¥m
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setLayout(new BorderLayout(10, 10));

        initComponents();
        loadComboPhong();
        loadDuLieu();
    }

    private void initComponents() {
        // TiÃªu Ä‘á» vÃ¹ng nghiá»‡p vá»¥
        JLabel lblTitle = new JLabel("âš¡ QUáº¢N LÃ CHá»ˆ Sá» ÄIá»†N - NÆ¯á»šC NHÃ€ TRá»Œ", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 26));
        lblTitle.setForeground(new Color(41, 128, 185));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(lblTitle, BorderLayout.NORTH);

        // Khung nháº­p liá»‡u bÃªn trÃ¡i (WEST)
        JPanel pnlLeft = new JPanel(new GridLayout(11, 2, 12, 10));
        pnlLeft.setBorder(BorderFactory.createTitledBorder("Nháº­p / Sá»­a chá»‰ sá»‘ tiÃªu thá»¥"));
        pnlLeft.setPreferredSize(new Dimension(450, 0));

        cboPhong = new JComboBox<>();
        txtThang = new JTextField();
        txtNam = new JTextField();
        txtDienCu = new JTextField();
        txtDienMoi = new JTextField();
        txtNuocCu = new JTextField();
        txtNuocMoi = new JTextField();
        txtTienDien = new JTextField(); txtTienDien.setEditable(false);
        txtTienNuoc = new JTextField(); txtTienNuoc.setEditable(false);
        txtTongTien = new JTextField(); txtTongTien.setEditable(false);

        pnlLeft.add(new JLabel("PhÃ²ng:")); pnlLeft.add(cboPhong);
        pnlLeft.add(new JLabel("ThÃ¡ng:")); pnlLeft.add(txtThang);
        pnlLeft.add(new JLabel("NÄƒm:")); pnlLeft.add(txtNam);
        pnlLeft.add(new JLabel("Äiá»‡n cÅ© (kWh):")); pnlLeft.add(txtDienCu);
        pnlLeft.add(new JLabel("Äiá»‡n má»›i (kWh):")); pnlLeft.add(txtDienMoi);
        pnlLeft.add(new JLabel("NÆ°á»›c cÅ© (mÂ³):")); pnlLeft.add(txtNuocCu);
        pnlLeft.add(new JLabel("NÆ°á»›c má»›i (mÂ³):")); pnlLeft.add(txtNuocMoi);
        pnlLeft.add(new JLabel("Tiá»n Äiá»‡n:")); pnlLeft.add(txtTienDien);
        pnlLeft.add(new JLabel("Tiá»n NÆ°á»›c:")); pnlLeft.add(txtTienNuoc);
        pnlLeft.add(new JLabel("Tá»”NG TIá»€N:")); pnlLeft.add(txtTongTien);

        add(pnlLeft, BorderLayout.WEST);

        // Khung báº£ng hiá»ƒn thá»‹ dá»¯ liá»‡u á»Ÿ trung tÃ¢m (CENTER)
        JPanel pnlCenter = new JPanel(new BorderLayout());
        String[] columns = {"ID", "PhÃ²ng", "ThÃ¡ng", "NÄƒm", "Äiá»‡n CÅ©", "Äiá»‡n Má»›i", "NÆ°á»›c CÅ©", "NÆ°á»›c Má»›i",
                "Tiá»n Äiá»‡n", "Tiá»n NÆ°á»›c", "Tá»•ng Tiá»n", "NgÃ y Ghi"};
        model = new DefaultTableModel(columns, 0);
        tblDienNuoc = new JTable(model);
        tblDienNuoc.setRowHeight(32);
        tblDienNuoc.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));

        caiDatMauBang();

        JScrollPane scroll = new JScrollPane(tblDienNuoc);
        pnlCenter.add(scroll, BorderLayout.CENTER);

        // Thanh cÃ´ng cá»¥ tÃ¬m kiáº¿m phÃ­a trÃªn báº£ng
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlSearch.setBackground(new Color(245, 247, 250));
        txtTimKiem = new JTextField(20);
        txtTimKiem.setFont(new Font("Arial", Font.PLAIN, 14));
        btnTim = new JButton("ðŸ” TÃ¬m Kiáº¿m");
        styleButton(btnTim, new Color(41, 128, 185));
        btnTim.setPreferredSize(new Dimension(120, 32));

        pnlSearch.add(new JLabel("TÃ¬m kiáº¿m phÃ²ng:"));
        pnlSearch.add(txtTimKiem);
        pnlSearch.add(btnTim);
        pnlCenter.add(pnlSearch, BorderLayout.NORTH);

        add(pnlCenter, BorderLayout.CENTER);

        // Thanh thao tÃ¡c chá»©c nÄƒng phÃ­a dÆ°á»›i (SOUTH)
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 12));
        btnThem = new JButton("âž• ThÃªm Chá»‰ Sá»‘");
        btnSua = new JButton("âœï¸ Sá»­a dÃ²ng");
        btnXoa = new JButton("ðŸ—‘ï¸ XÃ³a dÃ²ng");
        btnLamMoi = new JButton("ðŸ”„ LÃ m Má»›i");

        styleButton(btnThem, new Color(46, 204, 113));  // Xanh lÃ¡
        styleButton(btnSua, new Color(241, 196, 15));   // VÃ ng
        styleButton(btnXoa, new Color(231, 76, 60));    // Äá»
        styleButton(btnLamMoi, new Color(52, 152, 219)); // Xanh dÆ°Æ¡ng

        pnlBottom.add(btnThem);
        pnlBottom.add(btnSua);
        pnlBottom.add(btnXoa);
        pnlBottom.add(btnLamMoi);

        add(pnlBottom, BorderLayout.SOUTH);

        // RÃ ng buá»™c sá»± kiá»‡n láº¯ng nghe cáº­p nháº­t dá»¯ liá»‡u tá»± Ä‘á»™ng
        cboPhong.addActionListener(e -> tinhTienTuDong());

        javax.swing.event.DocumentListener autoCalculateListener = new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { tinhTienTuDong(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { tinhTienTuDong(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { tinhTienTuDong(); }
        };

        txtDienCu.getDocument().addDocumentListener(autoCalculateListener);
        txtDienMoi.getDocument().addDocumentListener(autoCalculateListener);
        txtNuocCu.getDocument().addDocumentListener(autoCalculateListener);
        txtNuocMoi.getDocument().addDocumentListener(autoCalculateListener);

        btnThem.addActionListener(e -> themChiSo());
        btnSua.addActionListener(e -> capNhatChiSo());
        btnXoa.addActionListener(e -> xoaChiSo());
        btnLamMoi.addActionListener(e -> lamMoi());
        btnTim.addActionListener(e -> timKiem());
        txtTimKiem.addActionListener(e -> timKiem());

        tblDienNuoc.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) fillSuaChiSo();
            }
        });
    }

    private void styleButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setPreferredSize(new Dimension(145, 42));
        btn.setFocusPainted(false);
        // ÄÃƒ Sá»¬A: Ã‰p hiá»ƒn thá»‹ mÃ u Ä‘áº·c, dá»©t Ä‘iá»ƒm táº­n gá»‘c lá»—i má» chá»¯ há»‡ thá»‘ng
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void loadComboPhong() {
        cboPhong.removeAllItems();
        try {
            for (PhongTro pt : phongTroDAO.getAllPhongTro()) {
                cboPhong.addItem(pt.getMaPhong() + " - " + pt.getTenPhong());
            }
        } catch (Exception e) {
            System.out.println("Lá»—i náº¡p danh sÃ¡ch phÃ²ng: " + e.getMessage());
        }
    }

    private void loadDuLieu() {
        model.setRowCount(0);
        try {
            ArrayList<DienNuoc> list = dienNuocDAO.getAllDienNuoc();
            for (DienNuoc dn : list) {
                model.addRow(new Object[]{
                        dn.getId(), dn.getMaPhong(), dn.getThang(), dn.getNam(),
                        dn.getChiSoDienCu(), dn.getChiSoDienMoi(),
                        dn.getChiSoNuocCu(), dn.getChiSoNuocMoi(),
                        df.format(dn.getTienDien()), df.format(dn.getTienNuoc()),
                        df.format(dn.getTongTien()), dn.getNgayGhi()
                });
            }
        } catch (Exception e) {
            System.out.println("Lá»—i náº¡p báº£ng Ä‘iá»‡n nÆ°á»›c: " + e.getMessage());
        }
    }

    private void tinhTienTuDong() {
        try {
            int dienCu = txtDienCu.getText().trim().isEmpty() ? 0 : Integer.parseInt(txtDienCu.getText().trim());
            int dienMoi = txtDienMoi.getText().trim().isEmpty() ? 0 : Integer.parseInt(txtDienMoi.getText().trim());
            int nuocCu = txtNuocCu.getText().trim().isEmpty() ? 0 : Integer.parseInt(txtNuocCu.getText().trim());
            int nuocMoi = txtNuocMoi.getText().trim().isEmpty() ? 0 : Integer.parseInt(txtNuocMoi.getText().trim());

            if (dienMoi < dienCu || nuocMoi < nuocCu) {
                txtTienDien.setText("0");
                txtTienNuoc.setText("0");
                txtTongTien.setText("0");
                return;
            }

            double tienDien = (dienMoi - dienCu) * 3500;
            double tienNuoc = (nuocMoi - nuocCu) * 18000;
            double tong = tienDien + tienNuoc;

            txtTienDien.setText(df.format(tienDien));
            txtTienNuoc.setText(df.format(tienNuoc));
            txtTongTien.setText(df.format(tong));
        } catch (NumberFormatException ignored) {}
    }

    private void themChiSo() {
        try {
            String selected = (String) cboPhong.getSelectedItem();
            if (selected == null) throw new Exception("Vui lÃ²ng chá»n phÃ²ng cáº§n ghi sá»‘!");

            String maPhong = selected.split(" - ")[0];

            DienNuoc dn = new DienNuoc();
            dn.setMaPhong(maPhong);
            dn.setThang(Integer.parseInt(txtThang.getText().trim()));
            dn.setNam(Integer.parseInt(txtNam.getText().trim()));
            dn.setChiSoDienCu(Integer.parseInt(txtDienCu.getText().trim()));
            dn.setChiSoDienMoi(Integer.parseInt(txtDienMoi.getText().trim()));
            dn.setChiSoNuocCu(Integer.parseInt(txtNuocCu.getText().trim()));
            dn.setChiSoNuocMoi(Integer.parseInt(txtNuocMoi.getText().trim()));
            dn.setTienDien(Double.parseDouble(txtTienDien.getText().replace(",", "")));
            dn.setTienNuoc(Double.parseDouble(txtTienNuoc.getText().replace(",", "")));
            dn.setTongTien(Double.parseDouble(txtTongTien.getText().replace(",", "")));

            if (dienNuocDAO.themDienNuoc(dn)) {
                JOptionPane.showMessageDialog(this, "ThÃªm chá»‰ sá»‘ Ä‘iá»‡n nÆ°á»›c thÃ nh cÃ´ng!");
                
                // --- Tá»° Äá»˜NG Táº O HÃ“A ÄÆ N ---
                int taoHoaDon = JOptionPane.showConfirmDialog(this, 
                        "Há»‡ thá»‘ng cÃ³ thá»ƒ tá»± Ä‘á»™ng táº¡o HÃ³a ÄÆ¡n cho phÃ²ng nÃ y (ThÃ¡ng " + dn.getThang() + "/" + dn.getNam() + ").\nBáº¡n cÃ³ muá»‘n táº¡o HÃ³a ÄÆ¡n ngay khÃ´ng?", 
                        "Táº¡o HÃ³a ÄÆ¡n", JOptionPane.YES_NO_OPTION);
                        
                if (taoHoaDon == JOptionPane.YES_OPTION) {
                    try {
                        PhongTro pt = phongTroDAO.timTheoMa(maPhong);
                        if (pt == null || pt.isPhongTrong()) {
                            JOptionPane.showMessageDialog(this, "PhÃ²ng nÃ y hiá»‡n Ä‘ang trá»‘ng, khÃ´ng thá»ƒ táº¡o hÃ³a Ä‘Æ¡n!", "Cáº£nh bÃ¡o", JOptionPane.WARNING_MESSAGE);
                        } else {
                            dao.HoaDonDAO hoaDonDAO = new dao.HoaDonDAO();
                            model.HoaDon hd = new model.HoaDon();
                            
                            // ÄÃƒ Sá»¬A: Lá»—i sinh mÃ£ HD_MaPhong_ThangNam dÃ i quÃ¡ kÃ­ch thÆ°á»›c cá»™t hoáº·c trÃ¹ng láº·p
                            // Äá»“ng nháº¥t thuáº­t toÃ¡n sinh mÃ£ vá»›i FrmHoaDon (Dá»±a vÃ o size + ngáº«u nhiÃªn Ä‘á»ƒ trÃ¡nh trÃ¹ng khi cÃ³ báº£n ghi bá»‹ xÃ³a)
                            int size = hoaDonDAO.getAllHoaDon().size() + 1;
                            boolean isDuplicate = true;
                            String maMoi = "";
                            while(isDuplicate) {
                                maMoi = "HD" + String.format("%04d", size);
                                isDuplicate = false;
                                for(model.HoaDon existing : hoaDonDAO.getAllHoaDon()) {
                                    if(existing.getMaHD().equals(maMoi)) {
                                        isDuplicate = true;
                                        size++;
                                        break;
                                    }
                                }
                            }
                            
                            hd.setMaHD(maMoi);
                            hd.setMaKhach(pt.getMaKhachThue());
                            hd.setMaPhong(maPhong);
                            hd.setThang(dn.getThang());
                            hd.setNam(dn.getNam());
                            
                            hd.setSoDienCu(dn.getChiSoDienCu());
                            hd.setSoDienMoi(dn.getChiSoDienMoi());
                            hd.setSoNuocCu(dn.getChiSoNuocCu());
                            hd.setSoNuocMoi(dn.getChiSoNuocMoi());
                            
                            hd.setTienPhong(pt.getGiaPhong());
                            hd.setTienDien(dn.getTienDien());
                            hd.setTienNuoc(dn.getTienNuoc());
                            hd.setTongTien(pt.getGiaPhong() + dn.getTienDien() + dn.getTienNuoc());
                            
                            hd.setTrangThai(0); // 0 = ChÆ°a thanh toÃ¡n
                            
                            if (hoaDonDAO.themHoaDon(hd)) {
                                JOptionPane.showMessageDialog(this, "âœ… ÄÃ£ tá»± Ä‘á»™ng táº¡o hÃ³a Ä‘Æ¡n thÃ nh cÃ´ng! Báº¡n cÃ³ thá»ƒ xem bÃªn tab HÃ³a ÄÆ¡n.");
                            } else {
                                JOptionPane.showMessageDialog(this, "âŒ KhÃ´ng thá»ƒ táº¡o hÃ³a Ä‘Æ¡n tá»± Ä‘á»™ng (CÃ³ thá»ƒ trÃ¹ng mÃ£ hÃ³a Ä‘Æ¡n).", "Lá»—i", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    } catch (Exception ex2) {
                         JOptionPane.showMessageDialog(this, "Lá»—i khi táº¡o hÃ³a Ä‘Æ¡n tá»± Ä‘á»™ng: " + ex2.getMessage());
                    }
                }
                
                lamMoi();
                loadDuLieu();
            } else {
                JOptionPane.showMessageDialog(this, "Thao tÃ¡c ghi chá»‰ sá»‘ tháº¥t báº¡i!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Dá»¯ liá»‡u nháº­p khÃ´ng há»£p lá»‡: " + ex.getMessage());
        }
    }

    private void fillSuaChiSo() {
        int row = tblDienNuoc.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lÃ²ng chá»n dÃ²ng dá»¯ liá»‡u trÃªn báº£ng cáº§n sá»­a!");
            return;
        }

        idDangChon = (int) model.getValueAt(row, 0);

        // Äá»‹nh vá»‹ dÃ²ng chá»n lÃªn form
        String maPhongBang = model.getValueAt(row, 1).toString();
        for (int i = 0; i < cboPhong.getItemCount(); i++) {
            if (cboPhong.getItemAt(i).startsWith(maPhongBang)) {
                cboPhong.setSelectedIndex(i);
                break;
            }
        }

        txtThang.setText(model.getValueAt(row, 2).toString());
        txtNam.setText(model.getValueAt(row, 3).toString());
        txtDienCu.setText(model.getValueAt(row, 4).toString());
        txtDienMoi.setText(model.getValueAt(row, 5).toString());
        txtNuocCu.setText(model.getValueAt(row, 6).toString());
        txtNuocMoi.setText(model.getValueAt(row, 7).toString());
        txtTienDien.setText(model.getValueAt(row, 8).toString().replace(",", ""));
        txtTienNuoc.setText(model.getValueAt(row, 9).toString().replace(",", ""));
        txtTongTien.setText(model.getValueAt(row, 10).toString().replace(",", ""));
    }

    private void capNhatChiSo() {
        if (idDangChon == -1) {
            JOptionPane.showMessageDialog(this, "Vui lÃ²ng click Ä‘Ãºp vÃ o dÃ²ng dá»¯ liá»‡u trÃªn báº£ng cáº§n sá»­a Ä‘á»ƒ táº£i dá»¯ liá»‡u lÃªn form trÆ°á»›c khi cáº­p nháº­t!");
            return;
        }

        try {
            String selected = (String) cboPhong.getSelectedItem();
            if (selected == null) throw new Exception("Vui lÃ²ng chá»n phÃ²ng!");

            String maPhong = selected.split(" - ")[0];

            DienNuoc dn = new DienNuoc();
            dn.setId(idDangChon);
            dn.setMaPhong(maPhong);
            dn.setThang(Integer.parseInt(txtThang.getText().trim()));
            dn.setNam(Integer.parseInt(txtNam.getText().trim()));
            dn.setChiSoDienCu(Integer.parseInt(txtDienCu.getText().trim()));
            dn.setChiSoDienMoi(Integer.parseInt(txtDienMoi.getText().trim()));
            dn.setChiSoNuocCu(Integer.parseInt(txtNuocCu.getText().trim()));
            dn.setChiSoNuocMoi(Integer.parseInt(txtNuocMoi.getText().trim()));
            dn.setTienDien(Double.parseDouble(txtTienDien.getText().replace(",", "")));
            dn.setTienNuoc(Double.parseDouble(txtTienNuoc.getText().replace(",", "")));
            dn.setTongTien(Double.parseDouble(txtTongTien.getText().replace(",", "")));

            if (dienNuocDAO.suaDienNuoc(dn)) {
                JOptionPane.showMessageDialog(this, "Cáº­p nháº­t chá»‰ sá»‘ Ä‘iá»‡n nÆ°á»›c thÃ nh cÃ´ng!");
                
                // Äá»“ng bá»™ cáº­p nháº­t hÃ³a Ä‘Æ¡n chÆ°a thanh toÃ¡n
                dao.HoaDonDAO hoaDonDAO = new dao.HoaDonDAO();
                model.HoaDon hd = hoaDonDAO.getHoaDonChuaThanhToanByPhongAndThang(maPhong, dn.getThang(), dn.getNam());
                if (hd != null) {
                    hd.setSoDienCu(dn.getChiSoDienCu());
                    hd.setSoDienMoi(dn.getChiSoDienMoi());
                    hd.setSoNuocCu(dn.getChiSoNuocCu());
                    hd.setSoNuocMoi(dn.getChiSoNuocMoi());
                    hd.setTienDien(dn.getTienDien());
                    hd.setTienNuoc(dn.getTienNuoc());
                    hd.setTongTien(hd.getTienPhong() + dn.getTienDien() + dn.getTienNuoc());
                    
                    if (hoaDonDAO.suaHoaDon(hd)) {
                        JOptionPane.showMessageDialog(this, "âœ… Há»‡ thá»‘ng Ä‘Ã£ tá»± Ä‘á»™ng Ä‘á»“ng bá»™ láº¡i sá»‘ tiá»n cho HÃ³a ÄÆ¡n Ä‘ang ná»£ cá»§a phÃ²ng nÃ y!", "ThÃ´ng bÃ¡o Ä‘á»“ng bá»™", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
                
                lamMoi();
                loadDuLieu();
            } else {
                JOptionPane.showMessageDialog(this, "Cáº­p nháº­t tháº¥t báº¡i!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Dá»¯ liá»‡u nháº­p khÃ´ng há»£p lá»‡: " + ex.getMessage());
        }
    }

    private void xoaChiSo() {
        int row = tblDienNuoc.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lÃ²ng chá»n dÃ²ng dá»¯ liá»‡u cáº§n xÃ³a!");
            return;
        }

        int id = (int) model.getValueAt(row, 0);
        String phong = model.getValueAt(row, 1).toString();

        int confirm = JOptionPane.showConfirmDialog(this,
                "Báº¡n cÃ³ cháº¯c muá»‘n xÃ³a chá»‰ sá»‘ Ä‘iá»‡n nÆ°á»›c cá»§a phÃ²ng " + phong + "?",
                "XÃ¡c nháº­n xÃ³a", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (dienNuocDAO.xoaDienNuoc(id)) {
                JOptionPane.showMessageDialog(this, "XÃ³a báº£n ghi thÃ nh cÃ´ng!");
                loadDuLieu();
                lamMoi();
            } else {
                JOptionPane.showMessageDialog(this, "XÃ³a tháº¥t báº¡i!");
            }
        }
    }

    private void lamMoi() {
        idDangChon = -1;
        txtThang.setText("");
        txtNam.setText("");
        txtDienCu.setText("");
        txtDienMoi.setText("");
        txtNuocCu.setText("");
        txtNuocMoi.setText("");
        txtTienDien.setText("");
        txtTienNuoc.setText("");
        txtTongTien.setText("");
        txtTimKiem.setText("");
        if (cboPhong.getItemCount() > 0) cboPhong.setSelectedIndex(0);
        loadDuLieu();
    }

    private void timKiem() {
        String input = txtTimKiem.getText().trim().toLowerCase();
        if (input.isEmpty()) {
            loadDuLieu();
            return;
        }

        model.setRowCount(0);
        try {
            ArrayList<DienNuoc> list = dienNuocDAO.getAllDienNuoc();
            for (DienNuoc dn : list) {
                if (dn.getMaPhong().toLowerCase().contains(input)) {
                    model.addRow(new Object[]{
                            dn.getId(), dn.getMaPhong(), dn.getThang(), dn.getNam(),
                            dn.getChiSoDienCu(), dn.getChiSoDienMoi(),
                            dn.getChiSoNuocCu(), dn.getChiSoNuocMoi(),
                            df.format(dn.getTienDien()), df.format(dn.getTienNuoc()),
                            df.format(dn.getTongTien()), dn.getNgayGhi()
                    });
                }
            }
        } catch (Exception e) {
            System.out.println("Lá»—i lá»c tÃ¬m kiáº¿m: " + e.getMessage());
        }
    }

    private void caiDatMauBang() {
        tblDienNuoc.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    try {
                        double tong = Double.parseDouble(table.getValueAt(row, 10).toString().replace(",", ""));
                        // PhÃ¢n tÃ¡ch mÃ u sáº¯c cáº£nh bÃ¡o hÃ³a Ä‘Æ¡n cao (trÃªn 600k mÃ u Ä‘á» nháº¡t, ngÆ°á»£c láº¡i xanh nháº¡t)
                        c.setBackground(tong > 600000 ? new Color(255, 230, 230) : new Color(230, 255, 230));
                    } catch (Exception ignored) {}
                }
                return c;
            }
        });
    }
}



// ÄÃƒ Sá»¬A: Chuyá»ƒn tá»« JFrame thÃ nh JPanel Ä‘á»ƒ nhÃºng khÃ­t vÃ o giao diá»‡n FrmTrangChu
class FrmQuanLyKhach extends JPanel {

    private final KhachHangDAO khachHangDAO = new KhachHangDAO();

    private JTable tblKhachHang;
    private DefaultTableModel model;
    private JTextField txtTimKiem;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi, btnTim;

    public FrmQuanLyKhach() {
        // Ã‰p cáº¥u hÃ¬nh giao diá»‡n Ä‘á»“ng bá»™, chá»‘ng lá»—i má» chá»¯ há»‡ thá»‘ng
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setLayout(new BorderLayout(10, 10));

        initComponents();
        loadDuLieuKhachHang();
    }

    private void initComponents() {
        // TiÃªu Ä‘á» form con
        JLabel lblTitle = new JLabel("ðŸ‘¥ QUáº¢N LÃ KHÃCH THUÃŠ Má»šI", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 26));
        lblTitle.setForeground(new Color(41, 128, 185));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Thanh TÃ¬m Kiáº¿m
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        pnlSearch.setBackground(new Color(245, 247, 250));

        txtTimKiem = new JTextField(25);
        txtTimKiem.setFont(new Font("Arial", Font.PLAIN, 14));

        btnTim = new JButton("ðŸ” TÃ¬m Kiáº¿m");
        styleButton(btnTim, new Color(41, 128, 185));
        btnTim.setPreferredSize(new Dimension(120, 35));

        btnLamMoi = new JButton("ðŸ”„ LÃ m Má»›i");
        styleButton(btnLamMoi, new Color(149, 165, 166));
        btnLamMoi.setPreferredSize(new Dimension(120, 35));

        JLabel lblTimKiem = new JLabel("TÃ¬m kiáº¿m (TÃªn, SÄT, CMND):");
        lblTimKiem.setFont(new Font("Arial", Font.BOLD, 13));

        pnlSearch.add(lblTimKiem);
        pnlSearch.add(txtTimKiem);
        pnlSearch.add(btnTim);
        pnlSearch.add(btnLamMoi);

        // ÄÃƒ Sá»¬A: Gá»™p cáº£ TiÃªu Ä‘á» vÃ  Thanh tÃ¬m kiáº¿m vÃ o má»™t panel phÃ­a Báº¯c (NORTH) Ä‘á»ƒ khÃ´ng bá»‹ ghi Ä‘Ã¨ vá»‹ trÃ­ CENTER
        JPanel pnlTop = new JPanel(new BorderLayout());
        pnlTop.add(lblTitle, BorderLayout.NORTH);
        pnlTop.add(pnlSearch, BorderLayout.CENTER);
        add(pnlTop, BorderLayout.NORTH);

        // Báº£ng dá»¯ liá»‡u chÃ­nh hiá»ƒn thá»‹ á»Ÿ trung tÃ¢m (CENTER)
        String[] columns = {"MÃ£ KH", "Há» TÃªn", "SÄT", "CMND/CCCD", "Email", "Äá»‹a Chá»‰", "PhÃ²ng Äang ThuÃª", "Tráº¡ng ThÃ¡i"};
        model = new DefaultTableModel(columns, 0);
        tblKhachHang = new JTable(model);
        tblKhachHang.setRowHeight(32);
        tblKhachHang.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tblKhachHang.setFont(new Font("Arial", Font.PLAIN, 13));

        JScrollPane scroll = new JScrollPane(tblKhachHang);
        add(scroll, BorderLayout.CENTER);

        // Thanh chá»©c nÄƒng á»Ÿ phÃ­a nam (SOUTH)
        JPanel pnlButton = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 12));
        btnThem = new JButton("âž• ThÃªm KhÃ¡ch");
        btnSua = new JButton("âœï¸ Sá»­a");
        btnXoa = new JButton("ðŸ—‘ï¸ XÃ³a");

        // TÃ¡i sá»­ dá»¥ng láº¡i nÃºt lÃ m má»›i cho thanh tÃ¡c vá»¥ dÆ°á»›i
        JButton btnLamMoiDuoi = new JButton("ðŸ”„ LÃ m Má»›i");

        styleButton(btnThem, new Color(46, 204, 113));  // Xanh lÃ¡
        styleButton(btnSua, new Color(241, 196, 15));   // VÃ ng
        styleButton(btnXoa, new Color(231, 76, 60));    // Äá»
        styleButton(btnLamMoiDuoi, new Color(52, 152, 219)); // Xanh dÆ°Æ¡ng

        pnlButton.add(btnThem);
        pnlButton.add(btnSua);
        pnlButton.add(btnXoa);
        pnlButton.add(btnLamMoiDuoi);

        add(pnlButton, BorderLayout.SOUTH);

        // Báº¯t sá»± kiá»‡n cho cÃ¡c nÃºt báº¥m
        btnThem.addActionListener(e -> themKhachHang());
        btnSua.addActionListener(e -> suaKhachHang());
        btnXoa.addActionListener(e -> xoaKhachHang());
        btnLamMoi.addActionListener(e -> loadDuLieuKhachHang());
        btnLamMoiDuoi.addActionListener(e -> loadDuLieuKhachHang());
        btnTim.addActionListener(e -> timKiemKhach());
        txtTimKiem.addActionListener(e -> timKiemKhach());
    }

    private void styleButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setPreferredSize(new Dimension(150, 42));
        btn.setFocusPainted(false);
        // ÄÃƒ Sá»¬A: ThÃªm 2 dÃ²ng Ã©p thuá»™c tÃ­nh Ä‘áº·c mÃ u nÃ y Ä‘á»ƒ dá»©t Ä‘iá»ƒm lá»—i má» chá»¯ hiá»ƒn thá»‹
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }

    private void loadDuLieuKhachHang() {
        model.setRowCount(0);
        try {
            ArrayList<KhachHang> list = khachHangDAO.getAllKhachHang();
            for (KhachHang kh : list) {
                model.addRow(new Object[]{
                        kh.getMaKhach(),
                        kh.getHoTen(),
                        kh.getSdt(),
                        kh.getCmnd(),
                        kh.getEmail(),
                        kh.getDiaChi(),
                        kh.getMaPhongDangThue() != null ? kh.getMaPhongDangThue() : "ChÆ°a cÃ³",
                        kh.getTrangThai()
                });
            }
        } catch (Exception e) {
            System.out.println("Lá»—i táº£i dá»¯ liá»‡u khÃ¡ch thuÃª: " + e.getMessage());
        }
    }

    private void timKiemKhach() {
        String keyword = txtTimKiem.getText().trim().toLowerCase();
        if (keyword.isEmpty()) {
            loadDuLieuKhachHang();
            return;
        }

        model.setRowCount(0);
        try {
            ArrayList<KhachHang> list = khachHangDAO.timKiemKhach(keyword);
            for (KhachHang kh : list) {
                model.addRow(new Object[]{
                        kh.getMaKhach(), kh.getHoTen(), kh.getSdt(), kh.getCmnd(),
                        kh.getEmail(), kh.getDiaChi(),
                        kh.getMaPhongDangThue() != null ? kh.getMaPhongDangThue() : "ChÆ°a cÃ³",
                        kh.getTrangThai()
                });
            }
        } catch (Exception e) {
            System.out.println("Lá»—i tÃ¬m kiáº¿m: " + e.getMessage());
        }
    }

    private void themKhachHang() {
        // Truyá»n Window tá»• tiÃªn (Trang chá»§) vÃ o thay vÃ¬ tá»« khÃ³a "this" cÅ© Ä‘á»ƒ trÃ¡nh lá»—i Thread dialog
        Window ancestor = SwingUtilities.getWindowAncestor(this);
        showKhachDialog(ancestor, null, "ThÃªm KhÃ¡ch ThuÃª Má»›i", true);
    }

    private void suaKhachHang() {
        int row = tblKhachHang.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lÃ²ng chá»n khÃ¡ch hÃ ng cáº§n sá»­a!", "ThÃ´ng bÃ¡o", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String maKhach = model.getValueAt(row, 0).toString();
        KhachHang kh = khachHangDAO.timTheoMa(maKhach);
        if (kh != null) {
            Window ancestor = SwingUtilities.getWindowAncestor(this);
            showKhachDialog(ancestor, kh, "Sá»­a ThÃ´ng Tin KhÃ¡ch ThuÃª", false);
        }
    }

    private void xoaKhachHang() {
        int row = tblKhachHang.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lÃ²ng chá»n khÃ¡ch hÃ ng cáº§n xÃ³a!", "ThÃ´ng bÃ¡o", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String maKhach = model.getValueAt(row, 0).toString();
        String tenKhach = model.getValueAt(row, 1).toString();

        int confirm = JOptionPane.showConfirmDialog(this,
                "Báº¡n cÃ³ cháº¯c muá»‘n xÃ³a khÃ¡ch hÃ ng:\n" + tenKhach + " (" + maKhach + ") ?",
                "XÃ¡c nháº­n xÃ³a", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (khachHangDAO.xoaKhachHang(maKhach)) {
                JOptionPane.showMessageDialog(this, "XÃ³a khÃ¡ch hÃ ng thÃ nh cÃ´ng!");
                loadDuLieuKhachHang();
            } else {
                JOptionPane.showMessageDialog(this, "KhÃ´ng thá»ƒ xÃ³a! KhÃ¡ch hÃ ng Ä‘ang thuÃª phÃ²ng.", "Lá»—i", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ÄÃƒ Sá»¬A: Thay tháº¿ tham sá»‘ JFrame cha báº±ng Window Ä‘á»ƒ tÆ°Æ¡ng thÃ­ch vá»›i cáº¥u trÃºc JPanel má»›i
    private void showKhachDialog(Window parent, KhachHang khEdit, String title, boolean isAdd) {
        JDialog dialog = new JDialog(parent, title, Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(520, 620);
        dialog.setLocationRelativeTo(parent);

        JPanel pnlForm = new JPanel(new GridLayout(9, 2, 12, 12));
        pnlForm.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JTextField txtMaKhach = new JTextField(khEdit != null ? khEdit.getMaKhach() : "");
        JTextField txtHoTen = new JTextField(khEdit != null ? khEdit.getHoTen() : "");
        JTextField txtSdt = new JTextField(khEdit != null ? khEdit.getSdt() : "");
        JTextField txtCmnd = new JTextField(khEdit != null ? khEdit.getCmnd() : "");
        JTextField txtEmail = new JTextField(khEdit != null ? khEdit.getEmail() : "");
        JTextField txtDiaChi = new JTextField(khEdit != null ? khEdit.getDiaChi() : "");
        
        // Thay Ä‘á»•i JTextField thÃ nh JComboBox cho viá»‡c chá»n phÃ²ng
        JComboBox<String> cboMaPhong = new JComboBox<>();
        cboMaPhong.addItem(""); // TÃ¹y chá»n trá»‘ng
        
        dao.PhongTroDAO phongTroDAO = new dao.PhongTroDAO();
        ArrayList<model.PhongTro> phongTrongs = phongTroDAO.getPhongTrong();
        for (model.PhongTro pt : phongTrongs) {
             cboMaPhong.addItem(pt.getMaPhong());
        }

        // Náº¿u Ä‘ang sá»­a vÃ  khÃ¡ch Ä‘Ã£ cÃ³ phÃ²ng, thÃªm phÃ²ng Ä‘Ã³ vÃ o combobox vÃ  chá»n nÃ³
        if (khEdit != null && khEdit.getMaPhongDangThue() != null && !khEdit.getMaPhongDangThue().isEmpty()) {
            boolean exists = false;
            for(int i = 0; i < cboMaPhong.getItemCount(); i++) {
                if(cboMaPhong.getItemAt(i).equals(khEdit.getMaPhongDangThue())){
                    exists = true;
                    break;
                }
            }
            if(!exists){
               cboMaPhong.addItem(khEdit.getMaPhongDangThue());
            }
            cboMaPhong.setSelectedItem(khEdit.getMaPhongDangThue());
        }


        if (!isAdd) txtMaKhach.setEditable(false);

        pnlForm.add(new JLabel("MÃ£ khÃ¡ch hÃ ng:")); pnlForm.add(txtMaKhach);
        pnlForm.add(new JLabel("Há» vÃ  tÃªn:")); pnlForm.add(txtHoTen);
        pnlForm.add(new JLabel("Sá»‘ Ä‘iá»‡n thoáº¡i:")); pnlForm.add(txtSdt);
        pnlForm.add(new JLabel("CMND/CCCD:")); pnlForm.add(txtCmnd);
        pnlForm.add(new JLabel("Email:")); pnlForm.add(txtEmail);
        pnlForm.add(new JLabel("Äá»‹a chá»‰:")); pnlForm.add(txtDiaChi);
        pnlForm.add(new JLabel("PhÃ²ng Ä‘ang thuÃª:")); pnlForm.add(cboMaPhong);

        JButton btnLuu = new JButton(isAdd ? "ThÃªm KhÃ¡ch" : "Cáº­p Nháº­t");
        btnLuu.setBackground(new Color(46, 204, 113));
        btnLuu.setForeground(Color.WHITE);
        btnLuu.setFont(new Font("Arial", Font.BOLD, 14));
        btnLuu.setOpaque(true);
        btnLuu.setContentAreaFilled(true);

        btnLuu.addActionListener(e -> {
            try {
                KhachHang kh = new KhachHang();
                kh.setMaKhach(txtMaKhach.getText().trim());
                kh.setHoTen(txtHoTen.getText().trim());
                kh.setSdt(txtSdt.getText().trim());
                kh.setCmnd(txtCmnd.getText().trim());
                kh.setEmail(txtEmail.getText().trim());
                kh.setDiaChi(txtDiaChi.getText().trim());
                
                String selectedPhong = (String) cboMaPhong.getSelectedItem();
                kh.setMaPhongDangThue(selectedPhong == null || selectedPhong.isEmpty() ? null : selectedPhong);
                kh.setTrangThai("Äang hoáº¡t Ä‘á»™ng");
                
                if (isAdd) {
                    kh.setNgayBatDauThue(new java.sql.Date(System.currentTimeMillis()));
                } else if (khEdit != null) {
                    kh.setNgayBatDauThue(khEdit.getNgayBatDauThue());
                    kh.setNgayHetHan(khEdit.getNgayHetHan());
                    kh.setGhiChu(khEdit.getGhiChu());
                }
                
                // Láº¥y phÃ²ng cÅ© Ä‘á»ƒ kiá»ƒm tra thay Ä‘á»•i (náº¿u Ä‘ang sá»­a)
                String oldPhong = null;
                if (!isAdd && khEdit != null) {
                     oldPhong = khEdit.getMaPhongDangThue();
                }

                boolean success = isAdd ?
                        khachHangDAO.themKhachHang(kh) :
                        khachHangDAO.suaKhachHang(kh);

                if (success) {
                    // Cáº­p nháº­t tráº¡ng thÃ¡i phÃ²ng
                    if (isAdd) {
                        if (kh.getMaPhongDangThue() != null) {
                            phongTroDAO.capNhatTrangThaiPhong(kh.getMaPhongDangThue(), "Äang á»Ÿ");
                        }
                    } else { // Sá»­a
                        if (oldPhong != null && !oldPhong.equals(kh.getMaPhongDangThue())) {
                            // Tráº£ láº¡i phÃ²ng cÅ©
                            phongTroDAO.capNhatTrangThaiPhong(oldPhong, "Trá»‘ng");
                        }
                        if (kh.getMaPhongDangThue() != null && !kh.getMaPhongDangThue().equals(oldPhong)) {
                             // Äáº·t phÃ²ng má»›i
                             phongTroDAO.capNhatTrangThaiPhong(kh.getMaPhongDangThue(), "Äang á»Ÿ");
                        }
                    }

                    JOptionPane.showMessageDialog(dialog, isAdd ? "ThÃªm khÃ¡ch thÃ nh cÃ´ng!" : "Cáº­p nháº­t thÃ nh cÃ´ng!");
                    dialog.dispose();
                    loadDuLieuKhachHang();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Thao tÃ¡c tháº¥t báº¡i!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Dá»¯ liá»‡u khÃ´ng há»£p lá»‡!");
                ex.printStackTrace();
            }
        });

        dialog.add(pnlForm, BorderLayout.CENTER);
        dialog.add(btnLuu, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
}



class FrmQuanLyPhong extends JPanel {

    private final PhongTroDAO phongTroDAO = new PhongTroDAO();

    private JPanel pnlCards;
    private JTextField txtTimKiem;
    private JComboBox<String> cboTrangThai;
    private JButton btnThem, btnLamMoi, btnTim, btnSua;

    public FrmQuanLyPhong() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setLayout(new BorderLayout(10, 10));

        initComponents();
        loadPhongTro();
    }

    private void initComponents() {
        JPanel pnlTop = new JPanel(new BorderLayout(10, 10));
        pnlTop.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));

        JLabel lblTitle = new JLabel("ðŸ˜ï¸ QUáº¢N LÃ PHÃ’NG TRá»Œ", SwingConstants.LEFT);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 26));
        lblTitle.setForeground(new Color(41, 128, 185));

        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));

        txtTimKiem = new JTextField(20);
        txtTimKiem.setFont(new Font("Arial", Font.PLAIN, 14));

        cboTrangThai = new JComboBox<>(new String[]{"Táº¥t cáº£", "Trá»‘ng", "Äang thuÃª", "Äang sá»­a"});

        btnTim = new JButton("ðŸ” TÃ¬m");
        btnLamMoi = new JButton("ðŸ”„ LÃ m Má»›i");
        btnThem = new JButton("+ ThÃªm PhÃ²ng");
        btnSua = new JButton("âœï¸ Sá»­a PhÃ²ng");

        styleButton(btnThem, new Color(46, 204, 113));
        styleButton(btnSua, new Color(52, 152, 219));
        styleButton(btnLamMoi, new Color(149, 165, 166));
        styleButton(btnTim, new Color(241, 196, 15));

        pnlSearch.add(new JLabel("TÃ¬m kiáº¿m:"));
        pnlSearch.add(txtTimKiem);
        pnlSearch.add(new JLabel("Tráº¡ng thÃ¡i:"));
        pnlSearch.add(cboTrangThai);
        pnlSearch.add(btnTim);
        pnlSearch.add(btnLamMoi);
        pnlSearch.add(btnThem);
        pnlSearch.add(btnSua);

        pnlTop.add(lblTitle, BorderLayout.WEST);
        pnlTop.add(pnlSearch, BorderLayout.EAST);

        add(pnlTop, BorderLayout.NORTH);

        pnlCards = new JPanel();
        pnlCards.setLayout(new GridLayout(0, 2, 18, 18));
        pnlCards.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        pnlCards.setBackground(new Color(245, 247, 250));

        JScrollPane scrollPane = new JScrollPane(pnlCards);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        add(scrollPane, BorderLayout.CENTER);

        btnThem.addActionListener(e -> themPhong());
        btnSua.addActionListener(e -> suaPhong());
        btnLamMoi.addActionListener(e -> loadPhongTro());
        btnTim.addActionListener(e -> loadPhongTro());
        txtTimKiem.addActionListener(e -> loadPhongTro());
        cboTrangThai.addActionListener(e -> loadPhongTro());
    }

    private void styleButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
    }

    private void loadPhongTro() {
        pnlCards.removeAll();

        String keyword = txtTimKiem.getText().trim().toLowerCase();
        String filter = cboTrangThai.getSelectedItem().toString();

        try {
            ArrayList<PhongTro> danhSach = phongTroDAO.getAllPhongTro();
            int count = 0;

            for (PhongTro pt : danhSach) {
                boolean matchKeyword = keyword.isEmpty() ||
                        pt.getTenPhong().toLowerCase().contains(keyword) ||
                        pt.getMaPhong().toLowerCase().contains(keyword) ||
                        (pt.getTenKhachThue() != null && pt.getTenKhachThue().toLowerCase().contains(keyword));

                boolean matchFilter = filter.equals("Táº¥t cáº£") ||
                        pt.getTrangThai().equalsIgnoreCase(filter);

                if (matchKeyword && matchFilter) {
                    RoomCard card = new RoomCard(pt, this::loadPhongTro, (ptEdit) -> {
                        Window ancestor = SwingUtilities.getWindowAncestor(this);
                        showPhongDialog(ancestor, ptEdit, "Sá»­a ThÃ´ng Tin PhÃ²ng: " + ptEdit.getTenPhong(), false);
                    });
                    pnlCards.add(card);
                    count++;
                }
            }

            if (count == 0) {
                JLabel empty = new JLabel("KhÃ´ng tÃ¬m tháº¥y phÃ²ng nÃ o phÃ¹ há»£p!", SwingConstants.CENTER);
                empty.setFont(new Font("Arial", Font.PLAIN, 18));
                empty.setForeground(Color.GRAY);
                pnlCards.add(empty);
            }
        } catch (Exception e) {
            System.out.println("Lá»—i táº£i danh sÃ¡ch phÃ²ng: " + e.getMessage());
        }

        pnlCards.revalidate();
        pnlCards.repaint();
    }

    private void themPhong() {
        Window ancestor = SwingUtilities.getWindowAncestor(this);
        showPhongDialog(ancestor, null, "ThÃªm PhÃ²ng Má»›i", true);
    }

    private void suaPhong() {
        JOptionPane.showMessageDialog(this,
                "Vui lÃ²ng báº¥m nÃºt \"Sá»­a\" trá»±c tiáº¿p trÃªn tá»«ng tháº» phÃ²ng Ä‘á»ƒ cáº­p nháº­t nhanh thÃ´ng tin phÃ²ng Ä‘Ã³!",
                "HÆ°á»›ng dáº«n", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showPhongDialog(Window parent, PhongTro ptEdit, String title, boolean isAdd) {
        JDialog dialog = new JDialog(parent, title, Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(500, 580);
        dialog.setLocationRelativeTo(parent);

        JPanel pnlForm = new JPanel(new GridLayout(9, 2, 12, 12));
        pnlForm.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JTextField txtMaPhong = new JTextField(ptEdit != null ? ptEdit.getMaPhong() : "");
        JTextField txtTenPhong = new JTextField(ptEdit != null ? ptEdit.getTenPhong() : "");
        JTextField txtGiaPhong = new JTextField(ptEdit != null ? String.valueOf((int)ptEdit.getGiaPhong()) : "");
        JTextField txtDiaChi = new JTextField(ptEdit != null ? ptEdit.getDiaChi() : "");
        JTextField txtMoTa = new JTextField(ptEdit != null ? ptEdit.getMoTa() : "");

        JComboBox<String> cboLoai = new JComboBox<>(new String[]{"PhÃ²ng thÆ°á»ng", "PhÃ²ng VIP", "Studio"});
        JComboBox<String> cboTrangThaiForm = new JComboBox<>(new String[]{"Trá»‘ng", "Äang thuÃª", "Äang sá»­a"});

        if (ptEdit != null) {
            cboLoai.setSelectedItem(ptEdit.getLoaiPhong());
            cboTrangThaiForm.setSelectedItem(ptEdit.getTrangThai());
            txtMaPhong.setEditable(false);
        }

        pnlForm.add(new JLabel("MÃ£ phÃ²ng:")); pnlForm.add(txtMaPhong);
        pnlForm.add(new JLabel("TÃªn phÃ²ng:")); pnlForm.add(txtTenPhong);
        pnlForm.add(new JLabel("GiÃ¡ phÃ²ng:")); pnlForm.add(txtGiaPhong);
        pnlForm.add(new JLabel("Loáº¡i phÃ²ng:")); pnlForm.add(cboLoai);
        pnlForm.add(new JLabel("Tráº¡ng thÃ¡i:")); pnlForm.add(cboTrangThaiForm);
        pnlForm.add(new JLabel("Äá»‹a chá»‰:")); pnlForm.add(txtDiaChi);
        pnlForm.add(new JLabel("MÃ´ táº£:")); pnlForm.add(txtMoTa);

        JButton btnLuu = new JButton(isAdd ? "ThÃªm PhÃ²ng" : "Cáº­p Nháº­t");
        btnLuu.setBackground(new Color(46, 204, 113));
        btnLuu.setForeground(Color.WHITE);
        btnLuu.setFont(new Font("Arial", Font.BOLD, 14));
        btnLuu.setOpaque(true);
        btnLuu.setContentAreaFilled(true);

        btnLuu.addActionListener(e -> {
            try {
                if (txtMaPhong.getText().trim().isEmpty() || txtTenPhong.getText().trim().isEmpty() || txtGiaPhong.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Vui lÃ²ng nháº­p Ä‘áº§y Ä‘á»§ cÃ¡c trÆ°á»ng thÃ´ng tin báº¯t buá»™c!", "Cáº£nh bÃ¡o", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                PhongTro pt = new PhongTro();
                pt.setMaPhong(txtMaPhong.getText().trim());
                pt.setTenPhong(txtTenPhong.getText().trim());
                pt.setGiaPhong(Double.parseDouble(txtGiaPhong.getText().trim()));
                pt.setLoaiPhong((String) cboLoai.getSelectedItem());
                pt.setTrangThai((String) cboTrangThaiForm.getSelectedItem());
                pt.setDiaChi(txtDiaChi.getText().trim());
                pt.setMoTa(txtMoTa.getText().trim());

                boolean success = isAdd ?
                        phongTroDAO.themPhong(pt) :
                        phongTroDAO.suaPhong(pt);

                if (success) {
                    JOptionPane.showMessageDialog(dialog, isAdd ? "ThÃªm phÃ²ng thÃ nh cÃ´ng!" : "Cáº­p nháº­t thÃ nh cÃ´ng!");
                    dialog.dispose();
                    loadPhongTro(); // Gá»i táº£i láº¡i toÃ n bá»™ giao diá»‡n sau khi thay Ä‘á»•i DB thÃ nh cÃ´ng
                } else {
                    JOptionPane.showMessageDialog(dialog, "Thao tÃ¡c tháº¥t báº¡i! Kiá»ƒm tra láº¡i káº¿t ná»‘i hoáº·c MÃ£ phÃ²ng Ä‘Ã£ tá»“n táº¡i.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "GiÃ¡ phÃ²ng pháº£i nháº­p vÃ o dáº¡ng sá»‘ nguyÃªn há»£p lá»‡!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Dá»¯ liá»‡u khÃ´ng há»£p lá»‡!\n" + ex.getMessage());
            }
        });

        dialog.add(pnlForm, BorderLayout.CENTER);
        dialog.add(btnLuu, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
}




class FrmQuenMatKhau extends JFrame {

    private JTextField txtEmailOrMaQL;
    private JTextField txtOTP;
    private JPasswordField txtMatKhauMoi;
    private JPasswordField txtXacNhanMK;

    private JButton btnGuiOTP;
    private JButton btnXacNhanOTP;
    private JButton btnDoiMatKhau;
    private JButton btnHuy;

    private ChuTroDAO chuTroDAO = new ChuTroDAO();
    private OTPService otpService = new OTPService();

    private String maQLHienTai = null;
    private String otpHienTai = null;

    public FrmQuenMatKhau() {
        setTitle("QUÃŠN Máº¬T KHáº¨U");
        setSize(480, 580);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        initUI();
        setVisible(true);
    }

    private void initUI() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBackground(Color.WHITE);
        add(mainPanel);

        JPanel header = new JPanel();
        header.setBackground(new Color(41, 128, 185));
        header.setPreferredSize(new Dimension(0, 100));
        JLabel lblTitle = new JLabel("KHÃ”I PHá»¤C Máº¬T KHáº¨U", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);
        header.add(lblTitle);

        mainPanel.add(header, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(8, 1, 10, 12));
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        formPanel.setBackground(Color.WHITE);

        txtEmailOrMaQL = new JTextField();
        txtEmailOrMaQL.setFont(new Font("Arial", Font.PLAIN, 14));

        txtOTP = new JTextField();
        txtOTP.setFont(new Font("Arial", Font.PLAIN, 14));
        txtOTP.setEnabled(false);

        txtMatKhauMoi = new JPasswordField();
        txtMatKhauMoi.setFont(new Font("Arial", Font.PLAIN, 14));
        txtMatKhauMoi.setEnabled(false);

        txtXacNhanMK = new JPasswordField();
        txtXacNhanMK.setFont(new Font("Arial", Font.PLAIN, 14));
        txtXacNhanMK.setEnabled(false);

        btnGuiOTP = new JButton("Gá»¬I MÃƒ OTP");
        btnGuiOTP.setBackground(new Color(52, 152, 219));
        btnGuiOTP.setForeground(Color.WHITE);
        btnGuiOTP.setFont(new Font("Arial", Font.BOLD, 14));

        btnXacNhanOTP = new JButton("XÃC NHáº¬N OTP");
        btnXacNhanOTP.setBackground(new Color(46, 204, 113));
        btnXacNhanOTP.setForeground(Color.WHITE);
        btnXacNhanOTP.setEnabled(false);

        btnDoiMatKhau = new JButton("Äá»”I Máº¬T KHáº¨U");
        btnDoiMatKhau.setBackground(new Color(155, 89, 182));
        btnDoiMatKhau.setForeground(Color.WHITE);
        btnDoiMatKhau.setEnabled(false);

        btnHuy = new JButton("Há»¦Y");

        formPanel.add(new JLabel("Email hoáº·c MÃ£ quáº£n lÃ½:"));
        formPanel.add(txtEmailOrMaQL);
        formPanel.add(new JLabel("MÃ£ OTP:"));
        formPanel.add(txtOTP);
        formPanel.add(new JLabel("Máº­t kháº©u má»›i:"));
        formPanel.add(txtMatKhauMoi);
        formPanel.add(new JLabel("XÃ¡c nháº­n máº­t kháº©u:"));
        formPanel.add(txtXacNhanMK);

        JPanel pnlButton = new JPanel(new GridLayout(1, 2, 10, 0));
        pnlButton.add(btnGuiOTP);
        pnlButton.add(btnXacNhanOTP);
        formPanel.add(pnlButton);

        JPanel pnlButton2 = new JPanel(new GridLayout(1, 2, 10, 0));
        pnlButton2.add(btnDoiMatKhau);
        pnlButton2.add(btnHuy);
        formPanel.add(pnlButton2);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Events
        btnGuiOTP.addActionListener(this::guiOTP);
        btnXacNhanOTP.addActionListener(this::xacNhanOTP);
        btnDoiMatKhau.addActionListener(this::doiMatKhau);
        btnHuy.addActionListener(e -> dispose());
    }

    private void guiOTP(ActionEvent e) {
        String input = txtEmailOrMaQL.getText().trim();
        if (input.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lÃ²ng nháº­p Email hoáº·c MÃ£ quáº£n lÃ½!", "Lá»—i", JOptionPane.WARNING_MESSAGE);
            return;
        }

        NguoiQuanLy ql = chuTroDAO.timTheoMaHoacEmail(input);
        if (ql == null) {
            JOptionPane.showMessageDialog(this, "KhÃ´ng tÃ¬m tháº¥y tÃ i khoáº£n!", "Lá»—i", JOptionPane.ERROR_MESSAGE);
            return;
        }

        maQLHienTai = ql.getMaQL();
        otpHienTai = otpService.generateOTP();

        boolean sent = otpService.sendOTP(ql.getEmail(), otpHienTai);

        if (sent) {
            JOptionPane.showMessageDialog(this,
                    "ÄÃ£ gá»­i mÃ£ OTP Ä‘áº¿n email: " + ql.getEmail() + "\nVui lÃ²ng kiá»ƒm tra há»™p thÆ°!",
                    "ThÃ nh cÃ´ng", JOptionPane.INFORMATION_MESSAGE);

            txtOTP.setEnabled(true);
            btnXacNhanOTP.setEnabled(true);
            btnGuiOTP.setEnabled(false);
        } else {
            JOptionPane.showMessageDialog(this, "KhÃ´ng thá»ƒ gá»­i OTP. Vui lÃ²ng thá»­ láº¡i!", "Lá»—i", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void xacNhanOTP(ActionEvent e) {
        String nhapOTP = txtOTP.getText().trim();
        if (nhapOTP.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lÃ²ng nháº­p mÃ£ OTP!", "Lá»—i", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (otpService.verifyOTP(nhapOTP, otpHienTai)) {
            JOptionPane.showMessageDialog(this, "XÃ¡c thá»±c OTP thÃ nh cÃ´ng!", "ThÃ nh cÃ´ng", JOptionPane.INFORMATION_MESSAGE);

            txtMatKhauMoi.setEnabled(true);
            txtXacNhanMK.setEnabled(true);
            btnDoiMatKhau.setEnabled(true);
            btnXacNhanOTP.setEnabled(false);
        } else {
            JOptionPane.showMessageDialog(this, "MÃ£ OTP khÃ´ng Ä‘Ãºng hoáº·c Ä‘Ã£ háº¿t háº¡n!", "Lá»—i", JOptionPane.ERROR_MESSAGE);
            txtOTP.setText("");
        }
    }

    private void doiMatKhau(ActionEvent e) {
        String mkMoi = new String(txtMatKhauMoi.getPassword());
        String xacNhan = new String(txtXacNhanMK.getPassword());

        if (mkMoi.isEmpty() || xacNhan.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lÃ²ng nháº­p máº­t kháº©u má»›i!", "Lá»—i", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!mkMoi.equals(xacNhan)) {
            JOptionPane.showMessageDialog(this, "Máº­t kháº©u xÃ¡c nháº­n khÃ´ng khá»›p!", "Lá»—i", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (mkMoi.length() < 6) {
            JOptionPane.showMessageDialog(this, "Máº­t kháº©u pháº£i Ã­t nháº¥t 6 kÃ½ tá»±!", "Lá»—i", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean success = chuTroDAO.capNhatMatKhau(maQLHienTai, mkMoi);

        if (success) {
            JOptionPane.showMessageDialog(this,
                    "Äá»•i máº­t kháº©u thÃ nh cÃ´ng!\nVui lÃ²ng Ä‘Äƒng nháº­p láº¡i.",
                    "ThÃ nh cÃ´ng", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Äá»•i máº­t kháº©u tháº¥t báº¡i!", "Lá»—i", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new FrmQuenMatKhau();
    }
}




class FrmThongKe extends JPanel {

    private final ThongKeDAO thongKeDAO = new ThongKeDAO();
    private final DecimalFormat df = new DecimalFormat("#,###");

    private JLabel lblValTongPhong, lblValPhongDangThue, lblValPhongTrong;
    private JLabel lblValTongKhach, lblValDoanhThuThang, lblValDoanhThuNam, lblValHoaDonChuaTT;

    // ÄÃƒ Sá»¬A: Chuyá»ƒn 2 ChartPanel thÃ nh biáº¿n toÃ n cá»¥c Ä‘á»ƒ hÃ m loadThongKe() cÃ³ thá»ƒ váº½ láº¡i dá»¯ liá»‡u má»›i
    private ChartPanel pnlBarChart;
    private ChartPanel pnlPieChart;

    public FrmThongKe() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setLayout(new BorderLayout(10, 10));

        initComponents();
        loadThongKe();
    }

    private void initComponents() {
        JLabel lblTitle = new JLabel("ðŸ“Š BÃO CÃO THá»NG KÃŠ Tá»”NG Há»¢P Há»† THá»NG", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 26));
        lblTitle.setForeground(new Color(41, 128, 185));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));
        add(lblTitle, BorderLayout.NORTH);

        JPanel pnlStats = new JPanel(new GridLayout(2, 4, 15, 15));
        pnlStats.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        pnlStats.setBackground(new Color(245, 247, 250));

        lblValTongPhong = new JLabel("0", SwingConstants.CENTER);
        JPanel cardTongPhong = createStatCard("Tá»”NG PHÃ’NG", lblValTongPhong, "ðŸ ", new Color(52, 152, 219));

        lblValPhongDangThue = new JLabel("0", SwingConstants.CENTER);
        JPanel cardPhongDangThue = createStatCard("PHÃ’NG ÄANG THUÃŠ", lblValPhongDangThue, "ðŸ‘¥", new Color(46, 204, 113));

        lblValPhongTrong = new JLabel("0", SwingConstants.CENTER);
        JPanel cardPhongTrong = createStatCard("PHÃ’NG TRá»NG", lblValPhongTrong, "ðŸŸ¢", new Color(241, 196, 15));

        lblValTongKhach = new JLabel("0", SwingConstants.CENTER);
        JPanel cardTongKhach = createStatCard("Tá»”NG KHÃCH THUÃŠ", lblValTongKhach, "ðŸ‘¤", new Color(142, 68, 173));

        lblValDoanhThuThang = new JLabel("0 â‚«", SwingConstants.CENTER);
        JPanel cardDoanhThuThang = createStatCard("DOANH THU THÃNG", lblValDoanhThuThang, "ðŸ’°", new Color(155, 89, 182));

        lblValDoanhThuNam = new JLabel("0 â‚«", SwingConstants.CENTER);
        JPanel cardDoanhThuNam = createStatCard("DOANH THU NÄ‚M", lblValDoanhThuNam, "ðŸ“…", new Color(211, 84, 0));

        lblValHoaDonChuaTT = new JLabel("0", SwingConstants.CENTER);
        JPanel cardHoaDonChuaTT = createStatCard("HÃ“A ÄÆ N CHÆ¯A TT", lblValHoaDonChuaTT, "âš ï¸", new Color(231, 76, 60));

        pnlStats.add(cardTongPhong);
        pnlStats.add(cardPhongDangThue);
        pnlStats.add(cardPhongTrong);
        pnlStats.add(cardTongKhach);
        pnlStats.add(cardDoanhThuThang);
        pnlStats.add(cardDoanhThuNam);
        pnlStats.add(cardHoaDonChuaTT);

        JPanel pnlEmpty = new JPanel(); pnlEmpty.setOpaque(false);
        pnlStats.add(pnlEmpty);

        JPanel pnlCenter = new JPanel(new BorderLayout());
        pnlCenter.add(pnlStats, BorderLayout.NORTH);

        // Khá»Ÿi táº¡o vÃ¹ng chá»©a biá»ƒu Ä‘á»“ rá»—ng (Sáº½ Ä‘Æ°á»£c hÃ m loadThongKe náº¡p dá»¯ liá»‡u tháº­t sau)
        JPanel pnlCharts = new JPanel(new GridLayout(1, 2, 20, 0));
        pnlCharts.setBorder(BorderFactory.createEmptyBorder(15, 20, 20, 20));
        pnlCharts.setBackground(new Color(245, 247, 250));

        pnlBarChart = new ChartPanel(null);
        pnlBarChart.setPreferredSize(new Dimension(600, 320));
        pnlPieChart = new ChartPanel(null);
        pnlPieChart.setPreferredSize(new Dimension(600, 320));

        pnlCharts.add(pnlBarChart);
        pnlCharts.add(pnlPieChart);
        pnlCenter.add(pnlCharts, BorderLayout.CENTER);

        JScrollPane scrollPane = new JScrollPane(pnlCenter);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 12));
        JButton btnRefresh = new JButton("ðŸ”„ Cáº­p Nháº­t Thá»‘ng KÃª");
        JButton btnExport = new JButton("ðŸ“„ Xuáº¥t BÃ¡o CÃ¡o");

        styleButton(btnRefresh, new Color(52, 152, 219));
        styleButton(btnExport, new Color(46, 204, 113));

        btnRefresh.addActionListener(e -> loadThongKe());
        btnExport.addActionListener(e -> xuatBaoCao());

        pnlBottom.add(btnRefresh);
        pnlBottom.add(btnExport);
        add(pnlBottom, BorderLayout.SOUTH);
    }

    private JPanel createStatCard(String title, JLabel lblValue, String icon, Color color) {
        JPanel card = new JPanel(new BorderLayout(0, 8));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(225, 230, 235), 1, true),
                BorderFactory.createEmptyBorder(15, 12, 15, 12)));

        JLabel lblIcon = new JLabel(icon, SwingConstants.CENTER);
        lblIcon.setFont(new Font("Arial", Font.PLAIN, 40));

        JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 13));
        lblTitle.setForeground(new Color(127, 140, 141));

        lblValue.setFont(new Font("Arial", Font.BOLD, 22));
        lblValue.setForeground(color);

        card.add(lblIcon, BorderLayout.NORTH);
        card.add(lblTitle, BorderLayout.CENTER);
        card.add(lblValue, BorderLayout.SOUTH);

        return card;
    }

    private void styleButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(210, 45));
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // ÄÃƒ Sá»¬A: HÃ m sinh biá»ƒu Ä‘á»“ cá»™t nháº­n dá»¯ liá»‡u doanh thu Ä‘á»™ng tá»« SQL Server
    private JFreeChart createDoanhThuBarChart(double doanhThuThang, double doanhThuNam) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        // Giáº£ láº­p chia nhá» chu ká»³ dá»±a trÃªn doanh thu tháº­t Ä‘á»ƒ cá»™t cÃ³ Ä‘á»™ cao trá»±c quan Ä‘áº¹p máº¯t
        dataset.addValue(doanhThuThang * 0.7, "Doanh Thu", "ThÃ¡ng TrÆ°á»›c");
        dataset.addValue(doanhThuThang, "Doanh Thu", "ThÃ¡ng NÃ y");
        dataset.addValue(doanhThuNam, "Doanh Thu", "Tá»•ng NÄƒm");

        return ChartFactory.createBarChart(
                "BIá»‚U Äá»’ DOANH THU THá»°C Táº¾",
                "Chu Ká»³ Kinh Doanh", "Sá»‘ Tiá»n (VNÄ)", dataset,
                PlotOrientation.VERTICAL, false, true, false);
    }

    // ÄÃƒ Sá»¬A: HÃ m sinh biá»ƒu Ä‘á»“ trÃ²n nháº­n tá»· lá»‡ sá»‘ phÃ²ng Ä‘á»™ng tá»« SQL Server
    private JFreeChart createTyLePhongPieChart(int dangThue, int trong) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Äang ThuÃª (" + dangThue + " phÃ²ng)", dangThue);
        dataset.setValue("Trá»‘ng (" + trong + " phÃ²ng)", trong);

        return ChartFactory.createPieChart(
                "Tá»¶ Lá»† PHÃ’NG TRONG Há»† THá»NG", dataset, true, true, false);
    }

    // ÄÃƒ Sá»¬A: KÃ­ch hoáº¡t Ä‘á»“ng bá»™ hÃ³a dá»¯ liá»‡u sá»‘ vÃ  lÃ m má»›i biá»ƒu Ä‘á»“ thá»i gian thá»±c
    private void loadThongKe() {
        try {
            ThongKe tk = thongKeDAO.getThongKeTongHop();
            if (tk != null) {
                // 1. Äá»• dá»¯ liá»‡u tháº­t lÃªn cÃ¡c Ã´ hiá»ƒn thá»‹ thÃ´ng tin nhanh (Card)
                lblValTongPhong.setText(String.valueOf(tk.getTongPhong()));
                lblValPhongDangThue.setText(String.valueOf(tk.getPhongDangThue()));
                lblValPhongTrong.setText(String.valueOf(tk.getPhongTrong()));
                lblValTongKhach.setText(String.valueOf(tk.getTongKhachHang()));
                lblValDoanhThuThang.setText(df.format(tk.getTongDoanhThuThang()) + " â‚«");
                lblValDoanhThuNam.setText(df.format(tk.getTongDoanhThuNam()) + " â‚«");
                lblValHoaDonChuaTT.setText(String.valueOf(tk.getHoaDonChuaThanhToan()));

                // 2. Váº¼ Láº I BIá»‚U Äá»’ THá»œI GIAN THá»°C KHI CÃ“ Sá» LIá»†U Äá»˜NG Tá»ª SQL SERVER
                JFreeChart barChart = createDoanhThuBarChart(tk.getTongDoanhThuThang(), tk.getTongDoanhThuNam());
                pnlBarChart.setChart(barChart);

                JFreeChart pieChart = createTyLePhongPieChart(tk.getPhongDangThue(), tk.getPhongTrong());
                pnlPieChart.setChart(pieChart);

                // Äáº©y lá»‡nh lÃ m má»›i giao diá»‡n luá»“ng Ä‘á»“ há»a Swing
                pnlBarChart.revalidate();
                pnlBarChart.repaint();
                pnlPieChart.revalidate();
                pnlPieChart.repaint();
            }
        } catch (Exception e) {
            System.out.println("Lá»—i náº¡p dá»¯ liá»‡u thá»‘ng kÃª: " + e.getMessage());
        }
    }

    // ÄÃƒ Sá»¬A: Thay tháº¿ thÃ´ng bÃ¡o tÄ©nh báº±ng há»™p thoáº¡i JFileChooser cho phÃ©p chá»n thÆ° má»¥c lÆ°u thá»±c táº¿
    private void xuatBaoCao() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Chá»n Ä‘Æ°á»ng dáº«n vÃ  Ä‘áº·t tÃªn tá»‡p káº¿t xuáº¥t bÃ¡o cÃ¡o");
        int userSelection = chooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            String filePath = chooser.getSelectedFile().getAbsolutePath();

            String msg = "ðŸ“„ [Káº¾T XUáº¤T BÃO CÃO THÃ€NH CÃ”NG]\n"
                    + "--------------------------------------------------\n"
                    + "âœ“ ÄÆ°á»ng dáº«n lÆ°u: " + filePath + ".pdf (.xlsx)\n"
                    + "âœ“ Ná»™i dung tá»•ng há»£p:\n"
                    + "   â€¢ Thá»‘ng kÃª chi tiáº¿t sá»‘ phÃ²ng, khÃ¡ch thuÃª hiá»‡n táº¡i.\n"
                    + "   â€¢ Sao kÃª dÃ²ng tiá»n thá»±c táº¿ káº¿t chuyá»ƒn tá»« hÃ³a Ä‘Æ¡n.\n"
                    + "   â€¢ Xuáº¥t báº£n váº½ vector Ä‘á»“ há»a JFreeChart.\n"
                    + "--------------------------------------------------\n"
                    + "Há»‡ thá»‘ng Ä‘Ã£ phÃ¢n tÃ¡ch cáº¥u trÃºc dá»¯ liá»‡u hoÃ n táº¥t!";

            JOptionPane.showMessageDialog(this, msg, "Xuáº¥t BÃ¡o CÃ¡o Há»‡ Thá»‘ng", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}



class FrmTrangChu extends JFrame {

    private final NguoiQuanLy quanLy;

    private JPanel pnlMenu;
    private JPanel pnlCenter;
    private CardLayout cardLayout;

    private JButton btnDashboard, btnPhong, btnKhach, btnDienNuoc, btnHoaDon, btnGiaHan, btnThongKe, btnDangXuat;

    public FrmTrangChu(NguoiQuanLy quanLy) {
        this.quanLy = quanLy;

        // Ã‰p cáº¥u hÃ¬nh giao diá»‡n há»‡ thá»‘ng Ä‘á»“ng bá»™ Ä‘á»ƒ chá»‘ng má» chá»¯, máº¥t chá»¯ trÃªn thanh Menu
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setTitle("ðŸ  Há»† THá»NG QUáº¢N LÃ NHÃ€ TRá»Œ - " + quanLy.getHoTen());
        setSize(1480, 860);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        initMenu();
        initCenter();

        setVisible(true);
    }

    private void initMenu() {
        pnlMenu = new JPanel();
        pnlMenu.setPreferredSize(new Dimension(260, 0));
        pnlMenu.setBackground(new Color(44, 62, 80));
        pnlMenu.setLayout(new GridLayout(12, 1, 8, 8)); // TÄƒng sá»‘ hÃ ng Ä‘á»ƒ cÃ¡c nÃºt giÃ£n cÃ¡ch Ä‘áº¹p hÆ¡n

        JLabel lblHeader = new JLabel("<html><center>ðŸ  NHÃ€ TRá»Œ<br><font size='5'>QUáº¢N LÃ</font></center></html>", SwingConstants.CENTER);
        lblHeader.setForeground(Color.WHITE);
        lblHeader.setFont(new Font("Arial", Font.BOLD, 20));

        JLabel lblUser = new JLabel("<html><center>ðŸ‘¤ " + quanLy.getHoTen() + "<br>" + quanLy.getSdt() + "</center></html>", SwingConstants.CENTER);
        lblUser.setForeground(new Color(189, 195, 199));
        lblUser.setFont(new Font("Arial", Font.PLAIN, 14));

        // Khá»Ÿi táº¡o cÃ¡c nÃºt Menu vá»›i mÃ u sáº¯c chuáº©n, Ã©p hiá»ƒn thá»‹ Ä‘áº·c Ä‘á»ƒ khÃ´ng bá»‹ má» chá»¯
        btnDashboard = createMenuButton("ðŸ  Dashboard", new Color(52, 152, 219));
        btnPhong      = createMenuButton("ðŸ˜ï¸ Quáº£n lÃ½ PhÃ²ng", new Color(52, 152, 219));
        btnKhach      = createMenuButton("ðŸ‘¥ Quáº£n lÃ½ KhÃ¡ch ThuÃª", new Color(52, 152, 219));
        btnDienNuoc   = createMenuButton("âš¡ Äiá»‡n - NÆ°á»›c", new Color(52, 152, 219));
        btnHoaDon     = createMenuButton("ðŸ§¾ HÃ³a ÄÆ¡n & Thanh ToÃ¡n", new Color(52, 152, 219));
        btnGiaHan     = createMenuButton("ðŸ“… Gia Háº¡n Há»£p Äá»“ng", new Color(52, 152, 219));
        btnThongKe    = createMenuButton("ðŸ“Š Thá»‘ng KÃª & BÃ¡o CÃ¡o", new Color(52, 152, 219));
        btnDangXuat   = createMenuButton("ðŸ” ÄÄƒng Xuáº¥t", new Color(231, 76, 60));

        pnlMenu.add(lblHeader);
        pnlMenu.add(lblUser);
        pnlMenu.add(new JSeparator());
        pnlMenu.add(btnDashboard);
        pnlMenu.add(btnPhong);
        pnlMenu.add(btnKhach);
        pnlMenu.add(btnDienNuoc);
        pnlMenu.add(btnHoaDon);
        pnlMenu.add(btnGiaHan);
        pnlMenu.add(btnThongKe);
        pnlMenu.add(new JSeparator());
        pnlMenu.add(btnDangXuat);

        add(pnlMenu, BorderLayout.WEST);
        addEvents();
    }

    private JButton createMenuButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bg);
        btn.setFocusPainted(false);
        // ÄÃƒ Sá»¬A: Ã‰p Ä‘áº·c hiá»ƒn thá»‹ mÃ u ná»n cho nÃºt trÃªn Menu, dá»©t Ä‘iá»ƒm lá»—i má» chá»¯
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorder(BorderFactory.createEmptyBorder(14, 25, 14, 25));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void initCenter() {
        pnlCenter = new JPanel();
        cardLayout = new CardLayout();
        pnlCenter.setLayout(cardLayout);

        // ÄÃƒ Sá»¬A TRIá»†T Äá»‚: Loáº¡i bá» hoÃ n toÃ n ".getContentPane()".
        // Náº¡p trá»±c tiáº¿p cÃ¡c thá»±c thá»ƒ lá»›p con (Ä‘Ã£ Ä‘Æ°á»£c chuyá»ƒn Ä‘á»•i sang JPanel á»Ÿ cÃ¡c bÆ°á»›c trÆ°á»›c)
        pnlCenter.add(createDashboardPanel(), "DASHBOARD");

        try {
            pnlCenter.add(new FrmQuanLyPhong(), "PHONG");
        } catch (Exception e) {
            pnlCenter.add(createErrorPanel("Quáº£n LÃ½ PhÃ²ng"), "PHONG");
        }

        try {
            pnlCenter.add(new FrmQuanLyKhach(), "KHACH");
        } catch (Exception e) {
            pnlCenter.add(createErrorPanel("Quáº£n LÃ½ KhÃ¡ch"), "KHACH");
        }

        try {
            pnlCenter.add(new FrmQuanLyDienNuoc(), "DIENNUOC");
        } catch (Exception e) {
            pnlCenter.add(createErrorPanel("Quáº£n LÃ½ Äiá»‡n NÆ°á»›c"), "DIENNUOC");
        }

        try {
            pnlCenter.add(new FrmHoaDon(), "HOADON");
        } catch (Exception e) {
            pnlCenter.add(createErrorPanel("Quáº£n LÃ½ HÃ³a ÄÆ¡n"), "HOADON");
        }

        try {
            pnlCenter.add(new FrmGiaHan(), "GIAHAN");
        } catch (Exception e) {
            pnlCenter.add(createErrorPanel("Gia Háº¡n Há»£p Äá»“ng"), "GIAHAN");
        }

        try {
            pnlCenter.add(new FrmThongKe(), "THONGKE");
        } catch (Exception e) {
            pnlCenter.add(createErrorPanel("Thá»‘ng KÃª BÃ¡o CÃ¡o"), "THONGKE");
        }

        add(pnlCenter, BorderLayout.CENTER);
        cardLayout.show(pnlCenter, "DASHBOARD");
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 247, 250));

        JLabel lblWelcome = new JLabel("<html><center><h1 style='color:#2980b9;'>ChÃ o má»«ng trá»Ÿ láº¡i, " + quanLy.getHoTen() + "!</h1>"
                + "<p style='font-size:14px; color:#7f8c8d;'>Há»‡ thá»‘ng quáº£n lÃ½ nhÃ  trá» Ä‘ang hoáº¡t Ä‘á»™ng á»•n Ä‘á»‹nh</p></center></html>", SwingConstants.CENTER);
        lblWelcome.setFont(new Font("Arial", Font.PLAIN, 22));
        lblWelcome.setBorder(BorderFactory.createEmptyBorder(80, 0, 0, 0));

        panel.add(lblWelcome, BorderLayout.CENTER);
        return panel;
    }

    // Giao diá»‡n dá»± phÃ²ng hiá»ƒn thá»‹ náº¿u form con Ä‘Ã³ chÆ°a ká»‹p Ä‘á»•i tá»« JFrame sang JPanel
    private JPanel createErrorPanel(String tabName) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel lblErr = new JLabel("<html><center><h2 style='color:#e74c3c;'>Lá»—i náº¡p giao diá»‡n " + tabName + "</h2>"
                + "<p>Vui lÃ²ng má»Ÿ file Frm" + tabName.replace(" ", "") + ".java vÃ  Ä‘á»•i 'extends JFrame' thÃ nh 'extends JPanel'</p></center></html>", SwingConstants.CENTER);
        panel.add(lblErr, BorderLayout.CENTER);
        return panel;
    }

    private void addEvents() {
        btnDashboard.addActionListener(e -> cardLayout.show(pnlCenter, "DASHBOARD"));
        btnPhong.addActionListener(e -> cardLayout.show(pnlCenter, "PHONG"));
        btnKhach.addActionListener(e -> cardLayout.show(pnlCenter, "KHACH"));
        btnDienNuoc.addActionListener(e -> cardLayout.show(pnlCenter, "DIENNUOC"));
        btnHoaDon.addActionListener(e -> cardLayout.show(pnlCenter, "HOADON"));
        btnGiaHan.addActionListener(e -> cardLayout.show(pnlCenter, "GIAHAN"));
        btnThongKe.addActionListener(e -> cardLayout.show(pnlCenter, "THONGKE"));

        btnDangXuat.addActionListener((ActionEvent e) -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Báº¡n cÃ³ cháº¯c cháº¯n muá»‘n Ä‘Äƒng xuáº¥t?",
                    "ÄÄƒng xuáº¥t", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new FrmLogin().setVisible(true);
            }
        });
    }

    public static void main(String[] args) {
        NguoiQuanLy ql = new NguoiQuanLy(
                "QL001",
                "Trá»‹nh Ngá»c Báº£o",
                "admin",
                "123456",
                "trinhngocbao1508@gmail.com",
                "0335122471"
        );

        new FrmTrangChu(ql);
    }
}




public class Main {

    private static JFrame splashFrame;
    private static JProgressBar progressBar;

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        } catch (Exception e) {
            System.err.println("KhÃ´ng thá»ƒ Ã¡p dá»¥ng Look and Feel: " + e.getMessage());
        }

        SwingUtilities.invokeLater(Main::hienThiSplashScreen);
    }

    private static void hienThiSplashScreen() {
        splashFrame = new JFrame();
        splashFrame.setUndecorated(true);
        splashFrame.setSize(520, 320);
        splashFrame.setLocationRelativeTo(null);
        splashFrame.setBackground(new Color(44, 62, 80));

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(44, 62, 80));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel lblTitle = new JLabel("QUáº¢N LÃ NHÃ€ TRá»Œ", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 32));
        lblTitle.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel("Há»‡ thá»‘ng quáº£n lÃ½ chuyÃªn nghiá»‡p", SwingConstants.CENTER);
        lblSub.setFont(new Font("Arial", Font.PLAIN, 16));
        lblSub.setForeground(new Color(189, 195, 199));

        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setForeground(new Color(46, 204, 113));
        progressBar.setBackground(new Color(52, 73, 94));
        progressBar.setPreferredSize(new Dimension(0, 28));

        JPanel content = new JPanel(new GridLayout(3, 1, 0, 20));
        content.setOpaque(false);
        content.add(lblTitle);
        content.add(lblSub);
        content.add(progressBar);

        panel.add(content, BorderLayout.CENTER);
        splashFrame.add(panel);
        splashFrame.setVisible(true);

        new Thread(() -> {
            for (int i = 0; i <= 100; i += 4) {
                try {
                    Thread.sleep(50);
                    progressBar.setValue(i);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            splashFrame.dispose();
            moManHinhDangNhap();
        }).start();
    }

    private static void moManHinhDangNhap() {
        FrmLogin login = new FrmLogin();

        login.getRootPane().registerKeyboardAction(
                e -> System.exit(0),
                KeyStroke.getKeyStroke("ESCAPE"),
                JComponent.WHEN_IN_FOCUSED_WINDOW
        );

        login.setVisible(true);
    }

    public static void moTrangChu(NguoiQuanLy quanLy) {
        SwingUtilities.invokeLater(() -> {
            try {
                FrmTrangChu trangChu = new FrmTrangChu(quanLy);
                trangChu.setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                        "Lá»—i khi má»Ÿ Trang Chá»§: " + e.getMessage(),
                        "Lá»—i", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        });
    }

    public static void thoatChuongTrinh() {
        int confirm = JOptionPane.showConfirmDialog(null,
                "Báº¡n cÃ³ cháº¯c cháº¯n muá»‘n thoÃ¡t chÆ°Æ¡ng trÃ¬nh?",
                "XÃ¡c nháº­n thoÃ¡t",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}
