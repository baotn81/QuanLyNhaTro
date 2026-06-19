package view;

import dao.ChuTroDAO;
import main.Main;
import model.NguoiQuanLy;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class FrmLogin extends JFrame {

    private JTextField txtMaQL;
    private JPasswordField txtMatKhau;
    private JButton btnDangNhap;
    private JButton btnThoat;
    private JCheckBox chkHienMK;
    private JLabel lblQuenMK;

    private final ChuTroDAO chuTroDAO = new ChuTroDAO();

    public FrmLogin() {
        setTitle("🔑 ĐĂNG NHẬP - QUẢN LÝ NHÀ TRỌ");
        setSize(520, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        initUI();
        addEvents();

        // Luôn đảm bảo form được hiển thị
        setVisible(true);
        SwingUtilities.invokeLater(() -> txtMaQL.requestFocus());
    }

    private void initUI() {
        // Sử dụng giao diện hệ thống hoặc CrossPlatform để tránh lỗi mất chữ, mờ nút trên Windows/Mac
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
        JLabel logo = new JLabel("🏠", SwingConstants.CENTER);
        logo.setFont(new Font("Arial", Font.PLAIN, 80));
        logo.setForeground(Color.WHITE);
        JLabel title = new JLabel("HỆ THỐNG QUẢN LÝ NHÀ TRỌ", SwingConstants.CENTER);
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

        // Nhãn Tiêu Đề
        JLabel lblMaQL = new JLabel("Mã Quản Lý");
        lblMaQL.setFont(new Font("Arial", Font.BOLD, 14));
        lblMaQL.setForeground(Color.DARK_GRAY);

        JLabel lblMatKhau = new JLabel("Mật Khẩu");
        lblMatKhau.setFont(new Font("Arial", Font.BOLD, 14));
        lblMatKhau.setForeground(Color.DARK_GRAY);

        txtMaQL = new JTextField();
        txtMaQL.setFont(new Font("Arial", Font.PLAIN, 15));

        txtMatKhau = new JPasswordField();
        txtMatKhau.setFont(new Font("Arial", Font.PLAIN, 15));

        chkHienMK = new JCheckBox(" Hiện mật khẩu");
        chkHienMK.setBackground(Color.WHITE);
        chkHienMK.setFont(new Font("Arial", Font.PLAIN, 13));

        lblQuenMK = new JLabel("<HTML><U>Quên mật khẩu?</U></HTML>");
        lblQuenMK.setFont(new Font("Arial", Font.ITALIC, 13));
        lblQuenMK.setForeground(new Color(41, 128, 185));
        lblQuenMK.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Nút Đăng Nhập (Fix lỗi mờ chữ bằng cách ép đè thuộc tính hệ thống)
        btnDangNhap = new JButton("ĐĂNG NHẬP");
        btnDangNhap.setBackground(new Color(46, 204, 113));
        btnDangNhap.setForeground(Color.WHITE);
        btnDangNhap.setFont(new Font("Arial", Font.BOLD, 16));
        btnDangNhap.setFocusPainted(false);
        btnDangNhap.setContentAreaFilled(true);
        btnDangNhap.setOpaque(true);
        btnDangNhap.setBorder(BorderFactory.createLineBorder(new Color(39, 174, 96), 1));

        // Nút Thoát (Fix lỗi mờ chữ bằng cách ép đè thuộc tính hệ thống)
        btnThoat = new JButton("THOÁT");
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
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        NguoiQuanLy ql = chuTroDAO.dangNhap(maQL, mk);
        if (ql != null) {
            JOptionPane.showMessageDialog(this, "Đăng nhập thành công!");
            dispose();
            Main.moTrangChu(ql);
        } else {
            JOptionPane.showMessageDialog(this, "Sai tài khoản hoặc mật khẩu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtMatKhau.setText("");
        }
    }

    public static void main(String[] args) {
        new FrmLogin();
    }
}