package view;

import dao.KhachHangDAO;
import model.KhachHang;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

// ĐÃ SỬA: Chuyển từ JFrame thành JPanel để nhúng khít vào giao diện FrmTrangChu
public class FrmQuanLyKhach extends JPanel {

    private final KhachHangDAO khachHangDAO = new KhachHangDAO();

    private JTable tblKhachHang;
    private DefaultTableModel model;
    private JTextField txtTimKiem;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi, btnTim;

    public FrmQuanLyKhach() {
        // Ép cấu hình giao diện đồng bộ, chống lỗi mờ chữ hệ thống
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
        // Tiêu đề form con
        JLabel lblTitle = new JLabel("👥 QUẢN LÝ KHÁCH THUÊ MỚI", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 26));
        lblTitle.setForeground(new Color(41, 128, 185));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Thanh Tìm Kiếm
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        pnlSearch.setBackground(new Color(245, 247, 250));

        txtTimKiem = new JTextField(25);
        txtTimKiem.setFont(new Font("Arial", Font.PLAIN, 14));

        btnTim = new JButton("🔍 Tìm Kiếm");
        styleButton(btnTim, new Color(41, 128, 185));
        btnTim.setPreferredSize(new Dimension(120, 35));

        btnLamMoi = new JButton("🔄 Làm Mới");
        styleButton(btnLamMoi, new Color(149, 165, 166));
        btnLamMoi.setPreferredSize(new Dimension(120, 35));

        JLabel lblTimKiem = new JLabel("Tìm kiếm (Tên, SĐT, CMND):");
        lblTimKiem.setFont(new Font("Arial", Font.BOLD, 13));

        pnlSearch.add(lblTimKiem);
        pnlSearch.add(txtTimKiem);
        pnlSearch.add(btnTim);
        pnlSearch.add(btnLamMoi);

        // ĐÃ SỬA: Gộp cả Tiêu đề và Thanh tìm kiếm vào một panel phía Bắc (NORTH) để không bị ghi đè vị trí CENTER
        JPanel pnlTop = new JPanel(new BorderLayout());
        pnlTop.add(lblTitle, BorderLayout.NORTH);
        pnlTop.add(pnlSearch, BorderLayout.CENTER);
        add(pnlTop, BorderLayout.NORTH);

        // Bảng dữ liệu chính hiển thị ở trung tâm (CENTER)
        String[] columns = {"Mã KH", "Họ Tên", "SĐT", "CMND/CCCD", "Email", "Địa Chỉ", "Phòng Đang Thuê", "Trạng Thái"};
        model = new DefaultTableModel(columns, 0);
        tblKhachHang = new JTable(model);
        tblKhachHang.setRowHeight(32);
        tblKhachHang.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tblKhachHang.setFont(new Font("Arial", Font.PLAIN, 13));

        JScrollPane scroll = new JScrollPane(tblKhachHang);
        add(scroll, BorderLayout.CENTER);

        // Thanh chức năng ở phía nam (SOUTH)
        JPanel pnlButton = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 12));
        btnThem = new JButton("➕ Thêm Khách");
        btnSua = new JButton("✏️ Sửa");
        btnXoa = new JButton("🗑️ Xóa");

        // Tái sử dụng lại nút làm mới cho thanh tác vụ dưới
        JButton btnLamMoiDuoi = new JButton("🔄 Làm Mới");

        styleButton(btnThem, new Color(46, 204, 113));  // Xanh lá
        styleButton(btnSua, new Color(241, 196, 15));   // Vàng
        styleButton(btnXoa, new Color(231, 76, 60));    // Đỏ
        styleButton(btnLamMoiDuoi, new Color(52, 152, 219)); // Xanh dương

        pnlButton.add(btnThem);
        pnlButton.add(btnSua);
        pnlButton.add(btnXoa);
        pnlButton.add(btnLamMoiDuoi);

        add(pnlButton, BorderLayout.SOUTH);

        // Bắt sự kiện cho các nút bấm
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
        // ĐÃ SỬA: Thêm 2 dòng ép thuộc tính đặc màu này để dứt điểm lỗi mờ chữ hiển thị
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
                        kh.getMaPhongDangThue() != null ? kh.getMaPhongDangThue() : "Chưa có",
                        kh.getTrangThai()
                });
            }
        } catch (Exception e) {
            System.out.println("Lỗi tải dữ liệu khách thuê: " + e.getMessage());
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
                        kh.getMaPhongDangThue() != null ? kh.getMaPhongDangThue() : "Chưa có",
                        kh.getTrangThai()
                });
            }
        } catch (Exception e) {
            System.out.println("Lỗi tìm kiếm: " + e.getMessage());
        }
    }

    private void themKhachHang() {
        // Truyền Window tổ tiên (Trang chủ) vào thay vì từ khóa "this" cũ để tránh lỗi Thread dialog
        Window ancestor = SwingUtilities.getWindowAncestor(this);
        showKhachDialog(ancestor, null, "Thêm Khách Thuê Mới", true);
    }

    private void suaKhachHang() {
        int row = tblKhachHang.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String maKhach = model.getValueAt(row, 0).toString();
        KhachHang kh = khachHangDAO.timTheoMa(maKhach);
        if (kh != null) {
            Window ancestor = SwingUtilities.getWindowAncestor(this);
            showKhachDialog(ancestor, kh, "Sửa Thông Tin Khách Thuê", false);
        }
    }

    private void xoaKhachHang() {
        int row = tblKhachHang.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String maKhach = model.getValueAt(row, 0).toString();
        String tenKhach = model.getValueAt(row, 1).toString();

        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa khách hàng:\n" + tenKhach + " (" + maKhach + ") ?",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (khachHangDAO.xoaKhachHang(maKhach)) {
                JOptionPane.showMessageDialog(this, "Xóa khách hàng thành công!");
                loadDuLieuKhachHang();
            } else {
                JOptionPane.showMessageDialog(this, "Không thể xóa! Khách hàng đang thuê phòng.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ĐÃ SỬA: Thay thế tham số JFrame cha bằng Window để tương thích với cấu trúc JPanel mới
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
        
        // Thay đổi JTextField thành JComboBox cho việc chọn phòng
        JComboBox<String> cboMaPhong = new JComboBox<>();
        cboMaPhong.addItem(""); // Tùy chọn trống
        
        dao.PhongTroDAO phongTroDAO = new dao.PhongTroDAO();
        ArrayList<model.PhongTro> phongTrongs = phongTroDAO.getPhongTrong();
        for (model.PhongTro pt : phongTrongs) {
             cboMaPhong.addItem(pt.getMaPhong());
        }

        // Nếu đang sửa và khách đã có phòng, thêm phòng đó vào combobox và chọn nó
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

        pnlForm.add(new JLabel("Mã khách hàng:")); pnlForm.add(txtMaKhach);
        pnlForm.add(new JLabel("Họ và tên:")); pnlForm.add(txtHoTen);
        pnlForm.add(new JLabel("Số điện thoại:")); pnlForm.add(txtSdt);
        pnlForm.add(new JLabel("CMND/CCCD:")); pnlForm.add(txtCmnd);
        pnlForm.add(new JLabel("Email:")); pnlForm.add(txtEmail);
        pnlForm.add(new JLabel("Địa chỉ:")); pnlForm.add(txtDiaChi);
        pnlForm.add(new JLabel("Phòng đang thuê:")); pnlForm.add(cboMaPhong);

        JButton btnLuu = new JButton(isAdd ? "Thêm Khách" : "Cập Nhật");
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
                kh.setTrangThai("Đang hoạt động");
                
                if (isAdd) {
                    kh.setNgayBatDauThue(new java.sql.Date(System.currentTimeMillis()));
                } else if (khEdit != null) {
                    kh.setNgayBatDauThue(khEdit.getNgayBatDauThue());
                    kh.setNgayHetHan(khEdit.getNgayHetHan());
                    kh.setGhiChu(khEdit.getGhiChu());
                }
                
                // Lấy phòng cũ để kiểm tra thay đổi (nếu đang sửa)
                String oldPhong = null;
                if (!isAdd && khEdit != null) {
                     oldPhong = khEdit.getMaPhongDangThue();
                }

                boolean success = isAdd ?
                        khachHangDAO.themKhachHang(kh) :
                        khachHangDAO.suaKhachHang(kh);

                if (success) {
                    // Cập nhật trạng thái phòng
                    if (isAdd) {
                        if (kh.getMaPhongDangThue() != null) {
                            phongTroDAO.capNhatTrangThaiPhong(kh.getMaPhongDangThue(), "Đang ở");
                        }
                    } else { // Sửa
                        if (oldPhong != null && !oldPhong.equals(kh.getMaPhongDangThue())) {
                            // Trả lại phòng cũ
                            phongTroDAO.capNhatTrangThaiPhong(oldPhong, "Trống");
                        }
                        if (kh.getMaPhongDangThue() != null && !kh.getMaPhongDangThue().equals(oldPhong)) {
                             // Đặt phòng mới
                             phongTroDAO.capNhatTrangThaiPhong(kh.getMaPhongDangThue(), "Đang ở");
                        }
                    }

                    JOptionPane.showMessageDialog(dialog, isAdd ? "Thêm khách thành công!" : "Cập nhật thành công!");
                    dialog.dispose();
                    loadDuLieuKhachHang();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Thao tác thất bại!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Dữ liệu không hợp lệ!");
                ex.printStackTrace();
            }
        });

        dialog.add(pnlForm, BorderLayout.CENTER);
        dialog.add(btnLuu, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
}