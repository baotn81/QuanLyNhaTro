package view;

import dao.HopDongDAO;
import dao.KhachHangDAO;
import dao.PhongTroDAO;
import model.HopDong;
import model.KhachHang;
import model.PhongTro;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Calendar;


public class FrmGiaHan extends JPanel {

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
        JLabel lblTitle = new JLabel("📅 QUẢN LÝ GIA HẠN HỢP ĐỒNG ĐĂNG KÝ", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 26));
        lblTitle.setForeground(new Color(41, 128, 185));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(lblTitle, BorderLayout.NORTH);

        JPanel pnlLeft = new JPanel(new GridLayout(7, 2, 12, 12));
        pnlLeft.setBorder(BorderFactory.createTitledBorder("Thông tin gia hạn hợp đồng"));
        pnlLeft.setPreferredSize(new Dimension(420, 0));

        cboKhachHang = new JComboBox<>();
        txtMaHopDong = new JTextField(); txtMaHopDong.setEditable(false);
        txtThangHienTai = new JTextField(); txtThangHienTai.setEditable(false);
        txtNamHienTai = new JTextField(); txtNamHienTai.setEditable(false);
        txtSoThangGiaHan = new JTextField();
        txtGhiChu = new JTextField();

        pnlLeft.add(new JLabel("Khách hàng đang thuê:")); pnlLeft.add(cboKhachHang);
        pnlLeft.add(new JLabel("Mã hợp đồng gốc:")); pnlLeft.add(txtMaHopDong);
        pnlLeft.add(new JLabel("Tháng hết hạn gốc:")); pnlLeft.add(txtThangHienTai);
        pnlLeft.add(new JLabel("Năm hết hạn gốc:")); pnlLeft.add(txtNamHienTai);
        pnlLeft.add(new JLabel("Gia hạn thêm (tháng):")); pnlLeft.add(txtSoThangGiaHan);
        pnlLeft.add(new JLabel("Ghi chú thay đổi:")); pnlLeft.add(txtGhiChu);

        add(pnlLeft, BorderLayout.WEST);

        JPanel pnlCenter = new JPanel(new BorderLayout());
        String[] columns = {"Mã HĐ", "Mã Khách", "Phòng", "Ngày Bắt Đầu", "Ngày Hết Hạn", "Số Tháng", "Trạng Thái", "Ghi Chú"};
        model = new DefaultTableModel(columns, 0);
        tblHopDong = new JTable(model);
        tblHopDong.setRowHeight(32);
        tblHopDong.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));

        pnlCenter.add(new JScrollPane(tblHopDong), BorderLayout.CENTER);
        add(pnlCenter, BorderLayout.CENTER);

        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        btnGiaHan = new JButton("✅ Gia Hạn Hợp Đồng");
        btnLamMoi = new JButton("🔄 Làm Mới");
        btnXoa = new JButton("🗑️ Xóa Hợp Đồng");

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

        // Ánh xạ dữ liệu khi nhấn chọn dòng trên bảng
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
            System.out.println("Lỗi nạp Combo Khách Hàng: " + e.getMessage());
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
            System.out.println("Lỗi nạp danh sách hợp đồng: " + e.getMessage());
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
                txtMaHopDong.setText("Chưa có HĐ");
                txtThangHienTai.setText("");
                txtNamHienTai.setText("");
                txtGhiChu.setText("");
            }
        } catch (Exception e) {
            System.out.println("Lỗi đọc hợp đồng: " + e.getMessage());
        }
    }

    private void giaHanHopDong() {
        try {
            String selected = (String) cboKhachHang.getSelectedItem();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần gia hạn!");
                return;
            }

            String maHDGoc = txtMaHopDong.getText().trim();
            if (maHDGoc.equals("Chưa có HĐ") || maHDGoc.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Hệ thống chưa ghi nhận hợp đồng gốc hợp lệ cho khách thuê này!");
                return;
            }

            if (txtSoThangGiaHan.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập số tháng muốn gia hạn thêm!");
                return;
            }

            int soThang = Integer.parseInt(txtSoThangGiaHan.getText().trim());
            if (soThang <= 0) {
                JOptionPane.showMessageDialog(this, "Số tháng gia hạn phải là số nguyên lớn hơn 0!");
                return;
            }

            String ghiChu = txtGhiChu.getText().trim();

            if (hopDongDAO.giaHanHopDong(maHDGoc, soThang, ghiChu)) {
                JOptionPane.showMessageDialog(this, "Gia hạn thành công hợp đồng " + maHDGoc + " thêm " + soThang + " tháng!");
                loadDuLieu();
                lamMoi();
            } else {
                JOptionPane.showMessageDialog(this, "Gia hạn thất bại! Vui lòng kiểm tra lại phương thức cập nhật tại HopDongDAO.");
            }
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Định dạng số tháng gia hạn nhập vào không hợp lệ!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi xử lý hệ thống: " + ex.getMessage());
        }
    }

    private void xoaHopDong() {
        int row = tblHopDong.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hàng hợp đồng trên bảng cần xóa!");
            return;
        }

        String maHopDong = model.getValueAt(row, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn xóa hợp đồng mang mã " + maHopDong + " không?",
                "Xác nhận xóa hợp đồng", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (hopDongDAO.xoaHopDong(maHopDong)) {
                JOptionPane.showMessageDialog(this, "Xóa hợp đồng thành công!");
                loadDuLieu();
                lamMoi();
            } else {
                JOptionPane.showMessageDialog(this, "Không thể xóa hợp đồng đang trong thời gian hiệu lực!");
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