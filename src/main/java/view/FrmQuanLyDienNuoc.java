package view;

import dao.DienNuocDAO;
import dao.PhongTroDAO;
import model.DienNuoc;
import model.PhongTro;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;

// ĐÃ SỬA: Chuyển đổi từ JFrame thành JPanel để tích hợp mượt mà vào cấu trúc FrmTrangChu
public class FrmQuanLyDienNuoc extends JPanel {

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
        // Khóa Look and Feel tránh xung đột hệ thống làm mờ chữ trên nút bấm
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
        // Tiêu đề vùng nghiệp vụ
        JLabel lblTitle = new JLabel("⚡ QUẢN LÝ CHỈ SỐ ĐIỆN - NƯỚC NHÀ TRỌ", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 26));
        lblTitle.setForeground(new Color(41, 128, 185));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(lblTitle, BorderLayout.NORTH);

        // Khung nhập liệu bên trái (WEST)
        JPanel pnlLeft = new JPanel(new GridLayout(11, 2, 12, 10));
        pnlLeft.setBorder(BorderFactory.createTitledBorder("Nhập / Sửa chỉ số tiêu thụ"));
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

        pnlLeft.add(new JLabel("Phòng:")); pnlLeft.add(cboPhong);
        pnlLeft.add(new JLabel("Tháng:")); pnlLeft.add(txtThang);
        pnlLeft.add(new JLabel("Năm:")); pnlLeft.add(txtNam);
        pnlLeft.add(new JLabel("Điện cũ (kWh):")); pnlLeft.add(txtDienCu);
        pnlLeft.add(new JLabel("Điện mới (kWh):")); pnlLeft.add(txtDienMoi);
        pnlLeft.add(new JLabel("Nước cũ (m³):")); pnlLeft.add(txtNuocCu);
        pnlLeft.add(new JLabel("Nước mới (m³):")); pnlLeft.add(txtNuocMoi);
        pnlLeft.add(new JLabel("Tiền Điện:")); pnlLeft.add(txtTienDien);
        pnlLeft.add(new JLabel("Tiền Nước:")); pnlLeft.add(txtTienNuoc);
        pnlLeft.add(new JLabel("TỔNG TIỀN:")); pnlLeft.add(txtTongTien);

        add(pnlLeft, BorderLayout.WEST);

        // Khung bảng hiển thị dữ liệu ở trung tâm (CENTER)
        JPanel pnlCenter = new JPanel(new BorderLayout());
        String[] columns = {"ID", "Phòng", "Tháng", "Năm", "Điện Cũ", "Điện Mới", "Nước Cũ", "Nước Mới",
                "Tiền Điện", "Tiền Nước", "Tổng Tiền", "Ngày Ghi"};
        model = new DefaultTableModel(columns, 0);
        tblDienNuoc = new JTable(model);
        tblDienNuoc.setRowHeight(32);
        tblDienNuoc.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));

        caiDatMauBang();

        JScrollPane scroll = new JScrollPane(tblDienNuoc);
        pnlCenter.add(scroll, BorderLayout.CENTER);

        // Thanh công cụ tìm kiếm phía trên bảng
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlSearch.setBackground(new Color(245, 247, 250));
        txtTimKiem = new JTextField(20);
        txtTimKiem.setFont(new Font("Arial", Font.PLAIN, 14));
        btnTim = new JButton("🔍 Tìm Kiếm");
        styleButton(btnTim, new Color(41, 128, 185));
        btnTim.setPreferredSize(new Dimension(120, 32));

        pnlSearch.add(new JLabel("Tìm kiếm phòng:"));
        pnlSearch.add(txtTimKiem);
        pnlSearch.add(btnTim);
        pnlCenter.add(pnlSearch, BorderLayout.NORTH);

        add(pnlCenter, BorderLayout.CENTER);

        // Thanh thao tác chức năng phía dưới (SOUTH)
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 12));
        btnThem = new JButton("➕ Thêm Chỉ Số");
        btnSua = new JButton("✏️ Sửa dòng");
        btnXoa = new JButton("🗑️ Xóa dòng");
        btnLamMoi = new JButton("🔄 Làm Mới");

        styleButton(btnThem, new Color(46, 204, 113));  // Xanh lá
        styleButton(btnSua, new Color(241, 196, 15));   // Vàng
        styleButton(btnXoa, new Color(231, 76, 60));    // Đỏ
        styleButton(btnLamMoi, new Color(52, 152, 219)); // Xanh dương

        pnlBottom.add(btnThem);
        pnlBottom.add(btnSua);
        pnlBottom.add(btnXoa);
        pnlBottom.add(btnLamMoi);

        add(pnlBottom, BorderLayout.SOUTH);

        // Ràng buộc sự kiện lắng nghe cập nhật dữ liệu tự động
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
        // ĐÃ SỬA: Ép hiển thị màu đặc, dứt điểm tận gốc lỗi mờ chữ hệ thống
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
            System.out.println("Lỗi nạp danh sách phòng: " + e.getMessage());
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
            System.out.println("Lỗi nạp bảng điện nước: " + e.getMessage());
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
            if (selected == null) throw new Exception("Vui lòng chọn phòng cần ghi số!");

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
                JOptionPane.showMessageDialog(this, "Thêm chỉ số điện nước thành công!");
                
                // --- TỰ ĐỘNG TẠO HÓA ĐƠN ---
                int taoHoaDon = JOptionPane.showConfirmDialog(this, 
                        "Hệ thống có thể tự động tạo Hóa Đơn cho phòng này (Tháng " + dn.getThang() + "/" + dn.getNam() + ").\nBạn có muốn tạo Hóa Đơn ngay không?", 
                        "Tạo Hóa Đơn", JOptionPane.YES_NO_OPTION);
                        
                if (taoHoaDon == JOptionPane.YES_OPTION) {
                    try {
                        PhongTro pt = phongTroDAO.timTheoMa(maPhong);
                        if (pt == null || pt.isPhongTrong()) {
                            JOptionPane.showMessageDialog(this, "Phòng này hiện đang trống, không thể tạo hóa đơn!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                        } else {
                            dao.HoaDonDAO hoaDonDAO = new dao.HoaDonDAO();
                            model.HoaDon hd = new model.HoaDon();
                            
                            // ĐÃ SỬA: Lỗi sinh mã HD_MaPhong_ThangNam dài quá kích thước cột hoặc trùng lặp
                            // Đồng nhất thuật toán sinh mã với FrmHoaDon (Dựa vào size + ngẫu nhiên để tránh trùng khi có bản ghi bị xóa)
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
                            
                            hd.setTrangThai(0); // 0 = Chưa thanh toán
                            
                            if (hoaDonDAO.themHoaDon(hd)) {
                                JOptionPane.showMessageDialog(this, "✅ Đã tự động tạo hóa đơn thành công! Bạn có thể xem bên tab Hóa Đơn.");
                            } else {
                                JOptionPane.showMessageDialog(this, "❌ Không thể tạo hóa đơn tự động (Có thể trùng mã hóa đơn).", "Lỗi", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    } catch (Exception ex2) {
                         JOptionPane.showMessageDialog(this, "Lỗi khi tạo hóa đơn tự động: " + ex2.getMessage());
                    }
                }
                
                lamMoi();
                loadDuLieu();
            } else {
                JOptionPane.showMessageDialog(this, "Thao tác ghi chỉ số thất bại!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Dữ liệu nhập không hợp lệ: " + ex.getMessage());
        }
    }

    private void fillSuaChiSo() {
        int row = tblDienNuoc.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng dữ liệu trên bảng cần sửa!");
            return;
        }

        idDangChon = (int) model.getValueAt(row, 0);

        // Định vị dòng chọn lên form
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
            JOptionPane.showMessageDialog(this, "Vui lòng click đúp vào dòng dữ liệu trên bảng cần sửa để tải dữ liệu lên form trước khi cập nhật!");
            return;
        }

        try {
            String selected = (String) cboPhong.getSelectedItem();
            if (selected == null) throw new Exception("Vui lòng chọn phòng!");

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
                JOptionPane.showMessageDialog(this, "Cập nhật chỉ số điện nước thành công!");
                
                // Đồng bộ cập nhật hóa đơn chưa thanh toán
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
                        JOptionPane.showMessageDialog(this, "✅ Hệ thống đã tự động đồng bộ lại số tiền cho Hóa Đơn đang nợ của phòng này!", "Thông báo đồng bộ", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
                
                lamMoi();
                loadDuLieu();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Dữ liệu nhập không hợp lệ: " + ex.getMessage());
        }
    }

    private void xoaChiSo() {
        int row = tblDienNuoc.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng dữ liệu cần xóa!");
            return;
        }

        int id = (int) model.getValueAt(row, 0);
        String phong = model.getValueAt(row, 1).toString();

        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa chỉ số điện nước của phòng " + phong + "?",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (dienNuocDAO.xoaDienNuoc(id)) {
                JOptionPane.showMessageDialog(this, "Xóa bản ghi thành công!");
                loadDuLieu();
                lamMoi();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại!");
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
            System.out.println("Lỗi lọc tìm kiếm: " + e.getMessage());
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
                        // Phân tách màu sắc cảnh báo hóa đơn cao (trên 600k màu đỏ nhạt, ngược lại xanh nhạt)
                        c.setBackground(tong > 600000 ? new Color(255, 230, 230) : new Color(230, 255, 230));
                    } catch (Exception ignored) {}
                }
                return c;
            }
        });
    }
}