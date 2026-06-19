package view;

import dao.ChuTroDAO;
import model.NguoiQuanLy;
import util.OTPService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class FrmQuenMatKhau extends JFrame {

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
        setTitle("QUÊN MẬT KHẨU");
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
        JLabel lblTitle = new JLabel("KHÔI PHỤC MẬT KHẨU", SwingConstants.CENTER);
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

        btnGuiOTP = new JButton("GỬI MÃ OTP");
        btnGuiOTP.setBackground(new Color(52, 152, 219));
        btnGuiOTP.setForeground(Color.WHITE);
        btnGuiOTP.setFont(new Font("Arial", Font.BOLD, 14));

        btnXacNhanOTP = new JButton("XÁC NHẬN OTP");
        btnXacNhanOTP.setBackground(new Color(46, 204, 113));
        btnXacNhanOTP.setForeground(Color.WHITE);
        btnXacNhanOTP.setEnabled(false);

        btnDoiMatKhau = new JButton("ĐỔI MẬT KHẨU");
        btnDoiMatKhau.setBackground(new Color(155, 89, 182));
        btnDoiMatKhau.setForeground(Color.WHITE);
        btnDoiMatKhau.setEnabled(false);

        btnHuy = new JButton("HỦY");

        formPanel.add(new JLabel("Email hoặc Mã quản lý:"));
        formPanel.add(txtEmailOrMaQL);
        formPanel.add(new JLabel("Mã OTP:"));
        formPanel.add(txtOTP);
        formPanel.add(new JLabel("Mật khẩu mới:"));
        formPanel.add(txtMatKhauMoi);
        formPanel.add(new JLabel("Xác nhận mật khẩu:"));
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
            JOptionPane.showMessageDialog(this, "Vui lòng nhập Email hoặc Mã quản lý!", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        NguoiQuanLy ql = chuTroDAO.timTheoMaHoacEmail(input);
        if (ql == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy tài khoản!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        maQLHienTai = ql.getMaQL();
        otpHienTai = otpService.generateOTP();

        boolean sent = otpService.sendOTP(ql.getEmail(), otpHienTai);

        if (sent) {
            JOptionPane.showMessageDialog(this,
                    "Đã gửi mã OTP đến email: " + ql.getEmail() + "\nVui lòng kiểm tra hộp thư!",
                    "Thành công", JOptionPane.INFORMATION_MESSAGE);

            txtOTP.setEnabled(true);
            btnXacNhanOTP.setEnabled(true);
            btnGuiOTP.setEnabled(false);
        } else {
            JOptionPane.showMessageDialog(this, "Không thể gửi OTP. Vui lòng thử lại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void xacNhanOTP(ActionEvent e) {
        String nhapOTP = txtOTP.getText().trim();
        if (nhapOTP.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã OTP!", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (otpService.verifyOTP(nhapOTP, otpHienTai)) {
            JOptionPane.showMessageDialog(this, "Xác thực OTP thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);

            txtMatKhauMoi.setEnabled(true);
            txtXacNhanMK.setEnabled(true);
            btnDoiMatKhau.setEnabled(true);
            btnXacNhanOTP.setEnabled(false);
        } else {
            JOptionPane.showMessageDialog(this, "Mã OTP không đúng hoặc đã hết hạn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtOTP.setText("");
        }
    }

    private void doiMatKhau(ActionEvent e) {
        String mkMoi = new String(txtMatKhauMoi.getPassword());
        String xacNhan = new String(txtXacNhanMK.getPassword());

        if (mkMoi.isEmpty() || xacNhan.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mật khẩu mới!", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!mkMoi.equals(xacNhan)) {
            JOptionPane.showMessageDialog(this, "Mật khẩu xác nhận không khớp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (mkMoi.length() < 6) {
            JOptionPane.showMessageDialog(this, "Mật khẩu phải ít nhất 6 ký tự!", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean success = chuTroDAO.capNhatMatKhau(maQLHienTai, mkMoi);

        if (success) {
            JOptionPane.showMessageDialog(this,
                    "Đổi mật khẩu thành công!\nVui lòng đăng nhập lại.",
                    "Thành công", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Đổi mật khẩu thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new FrmQuenMatKhau();
    }
}