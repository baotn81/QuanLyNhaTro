package view;

import dao.HoaDonDAO;
import dao.KhachHangDAO;
import dao.PhongTroDAO;
import model.HoaDon;
import model.KhachHang;
import model.PhongTro;
import util.XuatHoaDonEmailService;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class FrmHoaDon extends JPanel {

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
        JLabel lblTitle = new JLabel("💰 QUẢN LÝ HÓA ĐƠN THANH TOÁN NHÀ TRỌ", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 26));
        lblTitle.setForeground(new Color(41, 128, 185));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(lblTitle, BorderLayout.NORTH);

        JPanel pnlLeft = new JPanel(new GridLayout(14, 2, 12, 10));
        pnlLeft.setBorder(BorderFactory.createTitledBorder("Nhập Thông Tin Hóa Đơn"));
        pnlLeft.setPreferredSize(new Dimension(460, 0));

        txtMaHD = new JTextField(); txtMaHD.setEditable(false);
        cboKhach = new JComboBox<>();
        cboPhong = new JComboBox<>();
        txtThang = new JTextField();
        txtNam = new JTextField();
        txtDienCu = new JTextField(); txtDienCu.setEditable(false); txtDienCu.setToolTipText("Lấy tự động từ mục Quản Lý Điện Nước");
        txtDienMoi = new JTextField(); txtDienMoi.setEditable(false); txtDienMoi.setToolTipText("Lấy tự động từ mục Quản Lý Điện Nước");
        txtNuocCu = new JTextField(); txtNuocCu.setEditable(false); txtNuocCu.setToolTipText("Lấy tự động từ mục Quản Lý Điện Nước");
        txtNuocMoi = new JTextField(); txtNuocMoi.setEditable(false); txtNuocMoi.setToolTipText("Lấy tự động từ mục Quản Lý Điện Nước");
        txtTienPhong = new JTextField(); txtTienPhong.setEditable(false);
        txtTienDien = new JTextField(); txtTienDien.setEditable(false);
        txtTienNuoc = new JTextField(); txtTienNuoc.setEditable(false);
        txtTongTien = new JTextField(); txtTongTien.setEditable(false);

        pnlLeft.add(new JLabel("Mã Hóa Đơn:")); pnlLeft.add(txtMaHD);
        pnlLeft.add(new JLabel("Khách Thuê:")); pnlLeft.add(cboKhach);
        pnlLeft.add(new JLabel("Phòng:")); pnlLeft.add(cboPhong);
        pnlLeft.add(new JLabel("Tháng:")); pnlLeft.add(txtThang);
        pnlLeft.add(new JLabel("Năm:")); pnlLeft.add(txtNam);
        pnlLeft.add(new JLabel("Điện cũ (kWh):")); pnlLeft.add(txtDienCu);
        pnlLeft.add(new JLabel("Điện mới (kWh):")); pnlLeft.add(txtDienMoi);
        pnlLeft.add(new JLabel("Nước cũ (m³):")); pnlLeft.add(txtNuocCu);
        pnlLeft.add(new JLabel("Nước mới (m³):")); pnlLeft.add(txtNuocMoi);
        pnlLeft.add(new JLabel("Tiền phòng:")); pnlLeft.add(txtTienPhong);
        pnlLeft.add(new JLabel("Tiền điện:")); pnlLeft.add(txtTienDien);
        pnlLeft.add(new JLabel("Tiền nước:")); pnlLeft.add(txtTienNuoc);
        pnlLeft.add(new JLabel("TỔNG TIỀN:")); pnlLeft.add(txtTongTien);

        add(pnlLeft, BorderLayout.WEST);

        JPanel pnlCenter = new JPanel(new BorderLayout());
        model = new DefaultTableModel(new String[]{
                "Mã HĐ", "Khách", "Phòng", "Tháng", "Năm", "T.Phòng",
                "T.Điện", "T.Nước", "Tổng", "Trạng Thái"
        }, 0);

        tblHoaDon = new JTable(model);
        tblHoaDon.setRowHeight(32);
        tblHoaDon.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tblHoaDon.setAutoCreateRowSorter(true);

        pnlCenter.add(new JScrollPane(tblHoaDon), BorderLayout.CENTER);

        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        txtTim = new JTextField(25);
        btnTim = new JButton("🔍 Tìm");
        pnlSearch.add(new JLabel("Tìm kiếm:"));
        pnlSearch.add(txtTim);
        pnlSearch.add(btnTim);

        JPanel pnlTop = new JPanel(new BorderLayout());
        pnlTop.add(new JLabel("📋 DANH SÁCH HÓA ĐƠN", SwingConstants.LEFT), BorderLayout.WEST);
        pnlTop.add(pnlSearch, BorderLayout.EAST);
        pnlCenter.add(pnlTop, BorderLayout.NORTH);

        add(pnlCenter, BorderLayout.CENTER);

        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 12));
        btnThem = new JButton("➕ Thêm");
        btnSua = new JButton("✏ Sửa");
        btnXoa = new JButton("🗑 Xóa");
        btnThanhToan = new JButton("💵 Thanh Toán");
        btnGuiMail = new JButton("📧 Gửi Email");
        btnInHoaDon = new JButton("📄 In PDF");
        btnGiaHan = new JButton("🔄 Gia Hạn");
        btnLamMoi = new JButton("🔄 Làm Mới");

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
        } catch (Exception e) { System.out.println("Lỗi tải khách hàng: " + e.getMessage()); }
    }

    private void loadPhong() {
        cboPhong.removeAllItems();
        try {
            for (PhongTro pt : phongTroDAO.getAllPhongTro()) {
                cboPhong.addItem(pt.getMaPhong());
            }
        } catch (Exception e) { System.out.println("Lỗi tải phòng: " + e.getMessage()); }
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
                        hd.getTrangThai() == 1 ? "Đã Thanh Toán" : "Chưa Thanh Toán"
                });
            }
        } catch (Exception e) { System.out.println("Lỗi tải hóa đơn: " + e.getMessage()); }
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
            // Chưa nhập đủ số tháng năm, bỏ qua
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
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ tháng và năm chốt hóa đơn!");
            return;
        }
        if (txtDienMoi.getText().trim().isEmpty() || txtNuocMoi.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Chưa có chỉ số Điện Nước cho phòng này trong tháng! Vui lòng qua tab Quản Lý Điện Nước để chốt số trước khi lập hóa đơn.");
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
                JOptionPane.showMessageDialog(this, "➕ Chốt và thêm hóa đơn " + hdData.getMaHD() + " thành công!");
                lamMoi();
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi khi thêm hóa đơn xuống Cơ sở dữ liệu!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi định dạng dữ liệu số nhập vào: " + e.getMessage());
        }
    }

    private void suaHoaDon() {
        int row = tblHoaDon.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng hóa đơn trên bảng cần sửa thông tin!");
            return;
        }
        try {
            String maHD = txtMaHD.getText().trim();
            if (maHD.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Mã hóa đơn không được để trống!");
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

            hd.setTrangThai(model.getValueAt(row, 9).toString().equals("Đã Thanh Toán") ? 1 : 0);

            if (hoaDonDAO.suaHoaDon(hd)) {
                JOptionPane.showMessageDialog(this, "✏️ Cập nhật thông tin sửa đổi hóa đơn " + hd.getMaHD() + " thành công!");
                loadHoaDon();
            } else {
                JOptionPane.showMessageDialog(this, "Sửa hóa đơn thất bại trong cơ sở dữ liệu!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi xảy ra khi sửa dữ liệu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void xoaHoaDon() {
        int row = tblHoaDon.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn muốn xóa bỏ trên bảng!");
            return;
        }
        String maHD = model.getValueAt(row, 0).toString();
        int output = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa hóa đơn " + maHD + " không?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (output == JOptionPane.YES_OPTION) {
            if (hoaDonDAO.xoaHoaDon(maHD)) {
                JOptionPane.showMessageDialog(this, "🗑️ Đã xóa thành công hóa đơn khỏi hệ thống!");
                lamMoi();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa hóa đơn thất bại!");
            }
        }
    }

    private void thanhToan() {
        int row = tblHoaDon.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng hóa đơn cần thanh toán trên bảng!");
            return;
        }
        String maHD = model.getValueAt(row, 0).toString();
        if (model.getValueAt(row, 9).toString().equals("Đã Thanh Toán")) {
            JOptionPane.showMessageDialog(this, "Hóa đơn này đã được thanh toán trước đó rồi!");
            return;
        }

        // Lấy thông tin hóa đơn và khách hàng trước khi cập nhật
        String maKhach = model.getValueAt(row, 1).toString();
        String maPhong = model.getValueAt(row, 2).toString();
        int thang = Integer.parseInt(model.getValueAt(row, 3).toString());
        int nam = Integer.parseInt(model.getValueAt(row, 4).toString());
        double tongTien = 0;
        try {
            tongTien = Double.parseDouble(model.getValueAt(row, 8).toString().replace(",", ""));
        } catch (Exception ignored) {}

        String emailKhach = null;
        String tenKhach = "Khách thuê";
        for (KhachHang kh : khachHangDAO.getAllKhachHang()) {
            if (kh.getMaKhach().equals(maKhach)) {
                emailKhach = kh.getEmail();
                tenKhach = kh.getHoTen();
                break;
            }
        }

        // Cập nhật trạng thái thanh toán trong DB
        if (hoaDonDAO.capNhatTrangThai(maHD, 1)) {
            JOptionPane.showMessageDialog(this,
                    "💵 [Thanh Toán Thành Công] Hóa đơn " + maHD + " đã được quyết toán.\nDòng tiền doanh thu thống kê sẽ tự động cập nhật!",
                    "Thành công", JOptionPane.INFORMATION_MESSAGE);
            loadHoaDon();

            // Hỏi có gửi email xác nhận không
            if (emailKhach != null && !emailKhach.trim().isEmpty() && emailKhach.contains("@")) {
                int guiMail = JOptionPane.showConfirmDialog(this,
                        "📧 Gửi email xác nhận thanh toán đến khách thuê?\n"
                        + "Khách: " + tenKhach + "\nEmail: " + emailKhach,
                        "Gửi Email Xác Nhận", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                if (guiMail == JOptionPane.YES_OPTION) {
                    final String fEmail = emailKhach;
                    final String fTenKhach = tenKhach;
                    final String fMaHD = maHD;
                    final String fTenPhong = "Phòng " + maPhong;
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
                                            "📧 Đã gửi email xác nhận thanh toán thành công!\nĐến: " + fEmail,
                                            "Email Đã Gửi", JOptionPane.INFORMATION_MESSAGE);
                                } else {
                                    JOptionPane.showMessageDialog(FrmHoaDon.this,
                                            "⚠ Thanh toán đã ghi nhận nhưng gửi email thất bại.\nVui lòng kiểm tra lại cấu hình SMTP.",
                                            "Lỗi Gửi Email", JOptionPane.WARNING_MESSAGE);
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
            // Nếu khách không có email thì không hỏi, chỉ thông báo thanh toán xong

        } else {
            JOptionPane.showMessageDialog(this, "Thanh toán thất bại! Vui lòng kiểm tra lại kết nối Database.");
        }
    }

    private void guiMail() {
        int row = tblHoaDon.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn của khách thuê cần gửi email!");
            return;
        }

        try {
            String maKhach = model.getValueAt(row, 1).toString();
            String maPhong = model.getValueAt(row, 2).toString();

            String emailKhach = null;
            String tenKhach = "Khách thuê";
            for (KhachHang kh : khachHangDAO.getAllKhachHang()) {
                if (kh.getMaKhach().equals(maKhach)) {
                    emailKhach = kh.getEmail();
                    tenKhach = kh.getHoTen();
                    break;
                }
            }

            if (emailKhach == null || emailKhach.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Khách hàng " + tenKhach + " chưa được cập nhật Email trên hệ thống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validate email format cơ bản trước khi gửi
            if (!emailKhach.contains("@") || !emailKhach.contains(".")) {
                JOptionPane.showMessageDialog(this, "Địa chỉ email của khách hàng \"" + emailKhach + "\" không hợp lệ!", "Lỗi Email", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double soDienCu = txtDienCu.getText().trim().isEmpty() ? 0 : Double.parseDouble(txtDienCu.getText().trim());
            double soDienMoi = txtDienMoi.getText().trim().isEmpty() ? 0 : Double.parseDouble(txtDienMoi.getText().trim());
            double soNuocCu = txtNuocCu.getText().trim().isEmpty() ? 0 : Double.parseDouble(txtNuocCu.getText().trim());
            double soNuocMoi = txtNuocMoi.getText().trim().isEmpty() ? 0 : Double.parseDouble(txtNuocMoi.getText().trim());
            double tongTien = txtTongTien.getText().trim().isEmpty() ? 0 : Double.parseDouble(txtTongTien.getText().trim());

            String diaChiThucTe = "Khu nhà trọ quản lý";
            for (PhongTro p : phongTroDAO.getAllPhongTro()) {
                if (p.getMaPhong().equals(maPhong)) {
                    diaChiThucTe = p.getDiaChi();
                    break;
                }
            }

            PhongTro pt = new PhongTro();
            pt.setMaPhong(maPhong);
            pt.setTenPhong("Phòng " + maPhong);
            pt.setTenKhachThue(tenKhach);
            pt.setDiaChi(diaChiThucTe);

            // Tạo PDF trước (nhanh, không cần background)
            File tempPdf = XuatHoaDonEmailService.createInvoicePDF(pt, soDienMoi, soDienCu, soNuocMoi, soNuocCu, tongTien);
            if (tempPdf == null) {
                JOptionPane.showMessageDialog(this, "Lỗi hệ thống khi kết xuất cấu trúc HTML sang PDF tạm thời!", "Lỗi sinh file", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Gửi email trên background thread để tránh đơ giao diện
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
                                    "📧 Gửi hóa đơn thành công tới khách thuê: " + finalTenKhach
                                            + "\nĐịa chỉ Gmail nhận: " + finalEmail,
                                    "Thành công", JOptionPane.INFORMATION_MESSAGE);
                            finalPdf.delete();
                        } else {
                            JOptionPane.showMessageDialog(FrmHoaDon.this,
                                    "Gửi email thất bại! Vui lòng kiểm tra lại cấu hình tài khoản hoặc mã bí mật APP_PASSWORD.",
                                    "Lỗi kết nối SMTP", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception ex) {
                        setCursor(Cursor.getDefaultCursor());
                        btnGuiMail.setEnabled(true);
                        JOptionPane.showMessageDialog(FrmHoaDon.this,
                                "Lỗi xử lý gửi Email: " + ex.getCause(),
                                "Thông báo lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                }
            };
            worker.execute();

        } catch (Exception ex) {
            this.setCursor(Cursor.getDefaultCursor());
            btnGuiMail.setEnabled(true);
            JOptionPane.showMessageDialog(this, "Lỗi xử lý gửi Email thực tế: " + ex.toString(), "Thông báo lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void inPDF() {
        int row = tblHoaDon.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Chọn một dòng hóa đơn trên bảng để kết xuất file!");
            return;
        }

        try {
            String maKhach = model.getValueAt(row, 1).toString();
            String maPhong = model.getValueAt(row, 2).toString();

            String tenKhach = "Khách thuê";
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

            String diaChiThucTe = "Khu nhà trọ quản lý";
            for (PhongTro p : phongTroDAO.getAllPhongTro()) {
                if (p.getMaPhong().equals(maPhong)) {
                    diaChiThucTe = p.getDiaChi();
                    break;
                }
            }

            PhongTro pt = new PhongTro();
            pt.setMaPhong(maPhong);
            pt.setTenPhong("Phòng " + maPhong);
            pt.setTenKhachThue(tenKhach);
            pt.setDiaChi(diaChiThucTe);

            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Chọn thư mục lưu hóa đơn PDF");
            chooser.setSelectedFile(new File("HoaDon_Phong_" + maPhong + ".pdf"));

            if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                File saveFile = chooser.getSelectedFile();

                File srcPdf = XuatHoaDonEmailService.createInvoicePDF(pt, soDienMoi, soDienCu, soNuocMoi, soNuocCu, tongTien);

                if (srcPdf != null) {
                    if (srcPdf.renameTo(saveFile) || srcPdf.length() > 0) {
                        JOptionPane.showMessageDialog(this, "📄 [In Thành Công] Hóa đơn điện nước phòng " + maPhong + " đã kết xuất thành công tại:\n" + saveFile.getAbsolutePath(), "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Lỗi sao chép tệp tin xuất ra ổ đĩa máy tính!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Lỗi hệ thống khi dịch mã HTML sang định dạng PDF!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi kết xuất in PDF chi tiết: " + ex.toString(), "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void giaHanHopDong() {
        JOptionPane.showMessageDialog(this, "💡 Tính năng Gia Hạn được thực hiện trực tiếp bên Tab Quản Lý Hợp Đồng để đảm bảo toàn vẹn dữ liệu chu kỳ thuê!");
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
                            hd.getTrangThai() == 1 ? "Đã Thanh Toán" : "Chưa Thanh Toán"
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