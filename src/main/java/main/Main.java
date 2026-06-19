package main;

import model.NguoiQuanLy;
import view.FrmLogin;
import view.FrmTrangChu;

import javax.swing.*;
import java.awt.*;

public class Main {

    private static JFrame splashFrame;
    private static JProgressBar progressBar;

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        } catch (Exception e) {
            System.err.println("Không thể áp dụng Look and Feel: " + e.getMessage());
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

        JLabel lblTitle = new JLabel("QUẢN LÝ NHÀ TRỌ", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 32));
        lblTitle.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel("Hệ thống quản lý chuyên nghiệp", SwingConstants.CENTER);
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
                        "Lỗi khi mở Trang Chủ: " + e.getMessage(),
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        });
    }

    public static void thoatChuongTrinh() {
        int confirm = JOptionPane.showConfirmDialog(null,
                "Bạn có chắc chắn muốn thoát chương trình?",
                "Xác nhận thoát",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}