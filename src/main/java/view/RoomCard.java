package view;

import dao.PhongTroDAO;
import model.PhongTro;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

public class RoomCard extends JPanel {

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

        JLabel lblTenPhong = new JLabel("Phòng: " + phongTro.getTenPhong());
        lblTenPhong.setFont(titleFont);

        JLabel lblGia = new JLabel("Giá: " + String.format("%,.0f", phongTro.getGiaPhong()) + " ₫");
        lblGia.setFont(normalFont);

        String khach = (phongTro.getTenKhachThue() != null && !phongTro.getTenKhachThue().trim().isEmpty())
                ? phongTro.getTenKhachThue() : "Chưa có khách";

        JLabel lblKhach = new JLabel("Khách: " + khach);
        lblKhach.setFont(normalFont);

        String han = phongTro.getNgayGiaHan() != null
                ? phongTro.getNgayGiaHan().toString() : "Chưa có hạn";

        JLabel lblHan = new JLabel("Hạn hợp đồng: " + han);
        lblHan.setFont(normalFont);

        JLabel lblDiaChi = new JLabel("Địa chỉ: " + phongTro.getDiaChi());
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

        if ("Trống".equalsIgnoreCase(phongTro.getTrangThai())) {
            lblTrangThai.setBackground(new Color(223, 255, 223));
            lblTrangThai.setForeground(new Color(0, 120, 0));
        } else {
            lblTrangThai.setBackground(new Color(255, 230, 230));
            lblTrangThai.setForeground(new Color(180, 0, 0));
        }

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        pnlButtons.setOpaque(false);

        JButton btnSua = createStyledButton("Sửa", new Color(52, 152, 219));
        JButton btnXoa = createStyledButton("Xóa", new Color(231, 76, 60));

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
                "Bạn có chắc chắn muốn xóa phòng " + phongTro.getTenPhong() + "?\nHành động này không thể hoàn tác!",
                "Xác nhận xóa phòng", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (dao.xoaPhong(phongTro.getMaPhong())) {
                    JOptionPane.showMessageDialog(this, "Xóa phòng thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    if (onReloadCallback != null) {
                        onReloadCallback.run();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Không thể xóa phòng này!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi xóa: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
}