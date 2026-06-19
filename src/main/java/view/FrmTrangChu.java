package view;

import model.NguoiQuanLy;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class FrmTrangChu extends JFrame {

    private final NguoiQuanLy quanLy;

    private JPanel pnlMenu;
    private JPanel pnlCenter;
    private CardLayout cardLayout;

    private JButton btnDashboard, btnPhong, btnKhach, btnDienNuoc, btnHoaDon, btnGiaHan, btnThongKe, btnDangXuat;

    public FrmTrangChu(NguoiQuanLy quanLy) {
        this.quanLy = quanLy;

        // Ép cấu hình giao diện hệ thống đồng bộ để chống mờ chữ, mất chữ trên thanh Menu
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setTitle("🏠 HỆ THỐNG QUẢN LÝ NHÀ TRỌ - " + quanLy.getHoTen());
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
        pnlMenu.setLayout(new GridLayout(12, 1, 8, 8)); // Tăng số hàng để các nút giãn cách đẹp hơn

        JLabel lblHeader = new JLabel("<html><center>🏠 NHÀ TRỌ<br><font size='5'>QUẢN LÝ</font></center></html>", SwingConstants.CENTER);
        lblHeader.setForeground(Color.WHITE);
        lblHeader.setFont(new Font("Arial", Font.BOLD, 20));

        JLabel lblUser = new JLabel("<html><center>👤 " + quanLy.getHoTen() + "<br>" + quanLy.getSdt() + "</center></html>", SwingConstants.CENTER);
        lblUser.setForeground(new Color(189, 195, 199));
        lblUser.setFont(new Font("Arial", Font.PLAIN, 14));

        // Khởi tạo các nút Menu với màu sắc chuẩn, ép hiển thị đặc để không bị mờ chữ
        btnDashboard = createMenuButton("🏠 Dashboard", new Color(52, 152, 219));
        btnPhong      = createMenuButton("🏘️ Quản lý Phòng", new Color(52, 152, 219));
        btnKhach      = createMenuButton("👥 Quản lý Khách Thuê", new Color(52, 152, 219));
        btnDienNuoc   = createMenuButton("⚡ Điện - Nước", new Color(52, 152, 219));
        btnHoaDon     = createMenuButton("🧾 Hóa Đơn & Thanh Toán", new Color(52, 152, 219));
        btnGiaHan     = createMenuButton("📅 Gia Hạn Hợp Đồng", new Color(52, 152, 219));
        btnThongKe    = createMenuButton("📊 Thống Kê & Báo Cáo", new Color(52, 152, 219));
        btnDangXuat   = createMenuButton("🔐 Đăng Xuất", new Color(231, 76, 60));

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
        // ĐÃ SỬA: Ép đặc hiển thị màu nền cho nút trên Menu, dứt điểm lỗi mờ chữ
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

        // ĐÃ SỬA TRIỆT ĐỂ: Loại bỏ hoàn toàn ".getContentPane()".
        // Nạp trực tiếp các thực thể lớp con (đã được chuyển đổi sang JPanel ở các bước trước)
        pnlCenter.add(createDashboardPanel(), "DASHBOARD");

        try {
            pnlCenter.add(new FrmQuanLyPhong(), "PHONG");
        } catch (Exception e) {
            pnlCenter.add(createErrorPanel("Quản Lý Phòng"), "PHONG");
        }

        try {
            pnlCenter.add(new FrmQuanLyKhach(), "KHACH");
        } catch (Exception e) {
            pnlCenter.add(createErrorPanel("Quản Lý Khách"), "KHACH");
        }

        try {
            pnlCenter.add(new FrmQuanLyDienNuoc(), "DIENNUOC");
        } catch (Exception e) {
            pnlCenter.add(createErrorPanel("Quản Lý Điện Nước"), "DIENNUOC");
        }

        try {
            pnlCenter.add(new FrmHoaDon(), "HOADON");
        } catch (Exception e) {
            pnlCenter.add(createErrorPanel("Quản Lý Hóa Đơn"), "HOADON");
        }

        try {
            pnlCenter.add(new FrmGiaHan(), "GIAHAN");
        } catch (Exception e) {
            pnlCenter.add(createErrorPanel("Gia Hạn Hợp Đồng"), "GIAHAN");
        }

        try {
            pnlCenter.add(new FrmThongKe(), "THONGKE");
        } catch (Exception e) {
            pnlCenter.add(createErrorPanel("Thống Kê Báo Cáo"), "THONGKE");
        }

        add(pnlCenter, BorderLayout.CENTER);
        cardLayout.show(pnlCenter, "DASHBOARD");
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 247, 250));

        JLabel lblWelcome = new JLabel("<html><center><h1 style='color:#2980b9;'>Chào mừng trở lại, " + quanLy.getHoTen() + "!</h1>"
                + "<p style='font-size:14px; color:#7f8c8d;'>Hệ thống quản lý nhà trọ đang hoạt động ổn định</p></center></html>", SwingConstants.CENTER);
        lblWelcome.setFont(new Font("Arial", Font.PLAIN, 22));
        lblWelcome.setBorder(BorderFactory.createEmptyBorder(80, 0, 0, 0));

        panel.add(lblWelcome, BorderLayout.CENTER);
        return panel;
    }

    // Giao diện dự phòng hiển thị nếu form con đó chưa kịp đổi từ JFrame sang JPanel
    private JPanel createErrorPanel(String tabName) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel lblErr = new JLabel("<html><center><h2 style='color:#e74c3c;'>Lỗi nạp giao diện " + tabName + "</h2>"
                + "<p>Vui lòng mở file Frm" + tabName.replace(" ", "") + ".java và đổi 'extends JFrame' thành 'extends JPanel'</p></center></html>", SwingConstants.CENTER);
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
                    "Bạn có chắc chắn muốn đăng xuất?",
                    "Đăng xuất", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new FrmLogin().setVisible(true);
            }
        });
    }

    public static void main(String[] args) {
        NguoiQuanLy ql = new NguoiQuanLy(
                "QL001",
                "Trịnh Ngọc Bảo",
                "admin",
                "123456",
                "trinhngocbao1508@gmail.com",
                "0335122471"
        );

        new FrmTrangChu(ql);
    }
}