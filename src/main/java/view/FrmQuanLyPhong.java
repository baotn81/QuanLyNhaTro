package view;

import dao.PhongTroDAO;
import model.PhongTro;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class FrmQuanLyPhong extends JPanel {

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

        JLabel lblTitle = new JLabel("🏘️ QUẢN LÝ PHÒNG TRỌ", SwingConstants.LEFT);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 26));
        lblTitle.setForeground(new Color(41, 128, 185));

        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));

        txtTimKiem = new JTextField(20);
        txtTimKiem.setFont(new Font("Arial", Font.PLAIN, 14));

        cboTrangThai = new JComboBox<>(new String[]{"Tất cả", "Trống", "Đang thuê", "Đang sửa"});

        btnTim = new JButton("🔍 Tìm");
        btnLamMoi = new JButton("🔄 Làm Mới");
        btnThem = new JButton("+ Thêm Phòng");
        btnSua = new JButton("✏️ Sửa Phòng");

        styleButton(btnThem, new Color(46, 204, 113));
        styleButton(btnSua, new Color(52, 152, 219));
        styleButton(btnLamMoi, new Color(149, 165, 166));
        styleButton(btnTim, new Color(241, 196, 15));

        pnlSearch.add(new JLabel("Tìm kiếm:"));
        pnlSearch.add(txtTimKiem);
        pnlSearch.add(new JLabel("Trạng thái:"));
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

                boolean matchFilter = filter.equals("Tất cả") ||
                        pt.getTrangThai().equalsIgnoreCase(filter);

                if (matchKeyword && matchFilter) {
                    RoomCard card = new RoomCard(pt, this::loadPhongTro, (ptEdit) -> {
                        Window ancestor = SwingUtilities.getWindowAncestor(this);
                        showPhongDialog(ancestor, ptEdit, "Sửa Thông Tin Phòng: " + ptEdit.getTenPhong(), false);
                    });
                    pnlCards.add(card);
                    count++;
                }
            }

            if (count == 0) {
                JLabel empty = new JLabel("Không tìm thấy phòng nào phù hợp!", SwingConstants.CENTER);
                empty.setFont(new Font("Arial", Font.PLAIN, 18));
                empty.setForeground(Color.GRAY);
                pnlCards.add(empty);
            }
        } catch (Exception e) {
            System.out.println("Lỗi tải danh sách phòng: " + e.getMessage());
        }

        pnlCards.revalidate();
        pnlCards.repaint();
    }

    private void themPhong() {
        Window ancestor = SwingUtilities.getWindowAncestor(this);
        showPhongDialog(ancestor, null, "Thêm Phòng Mới", true);
    }

    private void suaPhong() {
        JOptionPane.showMessageDialog(this,
                "Vui lòng bấm nút \"Sửa\" trực tiếp trên từng thẻ phòng để cập nhật nhanh thông tin phòng đó!",
                "Hướng dẫn", JOptionPane.INFORMATION_MESSAGE);
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

        JComboBox<String> cboLoai = new JComboBox<>(new String[]{"Phòng thường", "Phòng VIP", "Studio"});
        JComboBox<String> cboTrangThaiForm = new JComboBox<>(new String[]{"Trống", "Đang thuê", "Đang sửa"});

        if (ptEdit != null) {
            cboLoai.setSelectedItem(ptEdit.getLoaiPhong());
            cboTrangThaiForm.setSelectedItem(ptEdit.getTrangThai());
            txtMaPhong.setEditable(false);
        }

        pnlForm.add(new JLabel("Mã phòng:")); pnlForm.add(txtMaPhong);
        pnlForm.add(new JLabel("Tên phòng:")); pnlForm.add(txtTenPhong);
        pnlForm.add(new JLabel("Giá phòng:")); pnlForm.add(txtGiaPhong);
        pnlForm.add(new JLabel("Loại phòng:")); pnlForm.add(cboLoai);
        pnlForm.add(new JLabel("Trạng thái:")); pnlForm.add(cboTrangThaiForm);
        pnlForm.add(new JLabel("Địa chỉ:")); pnlForm.add(txtDiaChi);
        pnlForm.add(new JLabel("Mô tả:")); pnlForm.add(txtMoTa);

        JButton btnLuu = new JButton(isAdd ? "Thêm Phòng" : "Cập Nhật");
        btnLuu.setBackground(new Color(46, 204, 113));
        btnLuu.setForeground(Color.WHITE);
        btnLuu.setFont(new Font("Arial", Font.BOLD, 14));
        btnLuu.setOpaque(true);
        btnLuu.setContentAreaFilled(true);

        btnLuu.addActionListener(e -> {
            try {
                if (txtMaPhong.getText().trim().isEmpty() || txtTenPhong.getText().trim().isEmpty() || txtGiaPhong.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Vui lòng nhập đầy đủ các trường thông tin bắt buộc!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
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
                    JOptionPane.showMessageDialog(dialog, isAdd ? "Thêm phòng thành công!" : "Cập nhật thành công!");
                    dialog.dispose();
                    loadPhongTro(); // Gọi tải lại toàn bộ giao diện sau khi thay đổi DB thành công
                } else {
                    JOptionPane.showMessageDialog(dialog, "Thao tác thất bại! Kiểm tra lại kết nối hoặc Mã phòng đã tồn tại.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Giá phòng phải nhập vào dạng số nguyên hợp lệ!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Dữ liệu không hợp lệ!\n" + ex.getMessage());
            }
        });

        dialog.add(pnlForm, BorderLayout.CENTER);
        dialog.add(btnLuu, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
}