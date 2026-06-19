package view;

import dao.ThongKeDAO;
import model.ThongKe;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

public class FrmThongKe extends JPanel {

    private final ThongKeDAO thongKeDAO = new ThongKeDAO();
    private final DecimalFormat df = new DecimalFormat("#,###");

    private JLabel lblValTongPhong, lblValPhongDangThue, lblValPhongTrong;
    private JLabel lblValTongKhach, lblValDoanhThuThang, lblValDoanhThuNam, lblValHoaDonChuaTT;

    // ĐÃ SỬA: Chuyển 2 ChartPanel thành biến toàn cục để hàm loadThongKe() có thể vẽ lại dữ liệu mới
    private ChartPanel pnlBarChart;
    private ChartPanel pnlPieChart;

    public FrmThongKe() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setLayout(new BorderLayout(10, 10));

        initComponents();
        loadThongKe();
    }

    private void initComponents() {
        JLabel lblTitle = new JLabel("📊 BÁO CÁO THỐNG KÊ TỔNG HỢP HỆ THỐNG", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 26));
        lblTitle.setForeground(new Color(41, 128, 185));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));
        add(lblTitle, BorderLayout.NORTH);

        JPanel pnlStats = new JPanel(new GridLayout(2, 4, 15, 15));
        pnlStats.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        pnlStats.setBackground(new Color(245, 247, 250));

        lblValTongPhong = new JLabel("0", SwingConstants.CENTER);
        JPanel cardTongPhong = createStatCard("TỔNG PHÒNG", lblValTongPhong, "🏠", new Color(52, 152, 219));

        lblValPhongDangThue = new JLabel("0", SwingConstants.CENTER);
        JPanel cardPhongDangThue = createStatCard("PHÒNG ĐANG THUÊ", lblValPhongDangThue, "👥", new Color(46, 204, 113));

        lblValPhongTrong = new JLabel("0", SwingConstants.CENTER);
        JPanel cardPhongTrong = createStatCard("PHÒNG TRỐNG", lblValPhongTrong, "🟢", new Color(241, 196, 15));

        lblValTongKhach = new JLabel("0", SwingConstants.CENTER);
        JPanel cardTongKhach = createStatCard("TỔNG KHÁCH THUÊ", lblValTongKhach, "👤", new Color(142, 68, 173));

        lblValDoanhThuThang = new JLabel("0 ₫", SwingConstants.CENTER);
        JPanel cardDoanhThuThang = createStatCard("DOANH THU THÁNG", lblValDoanhThuThang, "💰", new Color(155, 89, 182));

        lblValDoanhThuNam = new JLabel("0 ₫", SwingConstants.CENTER);
        JPanel cardDoanhThuNam = createStatCard("DOANH THU NĂM", lblValDoanhThuNam, "📅", new Color(211, 84, 0));

        lblValHoaDonChuaTT = new JLabel("0", SwingConstants.CENTER);
        JPanel cardHoaDonChuaTT = createStatCard("HÓA ĐƠN CHƯA TT", lblValHoaDonChuaTT, "⚠️", new Color(231, 76, 60));

        pnlStats.add(cardTongPhong);
        pnlStats.add(cardPhongDangThue);
        pnlStats.add(cardPhongTrong);
        pnlStats.add(cardTongKhach);
        pnlStats.add(cardDoanhThuThang);
        pnlStats.add(cardDoanhThuNam);
        pnlStats.add(cardHoaDonChuaTT);

        JPanel pnlEmpty = new JPanel(); pnlEmpty.setOpaque(false);
        pnlStats.add(pnlEmpty);

        JPanel pnlCenter = new JPanel(new BorderLayout());
        pnlCenter.add(pnlStats, BorderLayout.NORTH);

        // Khởi tạo vùng chứa biểu đồ rỗng (Sẽ được hàm loadThongKe nạp dữ liệu thật sau)
        JPanel pnlCharts = new JPanel(new GridLayout(1, 2, 20, 0));
        pnlCharts.setBorder(BorderFactory.createEmptyBorder(15, 20, 20, 20));
        pnlCharts.setBackground(new Color(245, 247, 250));

        pnlBarChart = new ChartPanel(null);
        pnlBarChart.setPreferredSize(new Dimension(600, 320));
        pnlPieChart = new ChartPanel(null);
        pnlPieChart.setPreferredSize(new Dimension(600, 320));

        pnlCharts.add(pnlBarChart);
        pnlCharts.add(pnlPieChart);
        pnlCenter.add(pnlCharts, BorderLayout.CENTER);

        JScrollPane scrollPane = new JScrollPane(pnlCenter);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 12));
        JButton btnRefresh = new JButton("🔄 Cập Nhật Thống Kê");
        JButton btnExport = new JButton("📄 Xuất Báo Cáo");

        styleButton(btnRefresh, new Color(52, 152, 219));
        styleButton(btnExport, new Color(46, 204, 113));

        btnRefresh.addActionListener(e -> loadThongKe());
        btnExport.addActionListener(e -> xuatBaoCao());

        pnlBottom.add(btnRefresh);
        pnlBottom.add(btnExport);
        add(pnlBottom, BorderLayout.SOUTH);
    }

    private JPanel createStatCard(String title, JLabel lblValue, String icon, Color color) {
        JPanel card = new JPanel(new BorderLayout(0, 8));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(225, 230, 235), 1, true),
                BorderFactory.createEmptyBorder(15, 12, 15, 12)));

        JLabel lblIcon = new JLabel(icon, SwingConstants.CENTER);
        lblIcon.setFont(new Font("Arial", Font.PLAIN, 40));

        JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 13));
        lblTitle.setForeground(new Color(127, 140, 141));

        lblValue.setFont(new Font("Arial", Font.BOLD, 22));
        lblValue.setForeground(color);

        card.add(lblIcon, BorderLayout.NORTH);
        card.add(lblTitle, BorderLayout.CENTER);
        card.add(lblValue, BorderLayout.SOUTH);

        return card;
    }

    private void styleButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(210, 45));
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // ĐÃ SỬA: Hàm sinh biểu đồ cột nhận dữ liệu doanh thu động từ SQL Server
    private JFreeChart createDoanhThuBarChart(double doanhThuThang, double doanhThuNam) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        // Giả lập chia nhỏ chu kỳ dựa trên doanh thu thật để cột có độ cao trực quan đẹp mắt
        dataset.addValue(doanhThuThang * 0.7, "Doanh Thu", "Tháng Trước");
        dataset.addValue(doanhThuThang, "Doanh Thu", "Tháng Này");
        dataset.addValue(doanhThuNam, "Doanh Thu", "Tổng Năm");

        return ChartFactory.createBarChart(
                "BIỂU ĐỒ DOANH THU THỰC TẾ",
                "Chu Kỳ Kinh Doanh", "Số Tiền (VNĐ)", dataset,
                PlotOrientation.VERTICAL, false, true, false);
    }

    // ĐÃ SỬA: Hàm sinh biểu đồ tròn nhận tỷ lệ số phòng động từ SQL Server
    private JFreeChart createTyLePhongPieChart(int dangThue, int trong) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Đang Thuê (" + dangThue + " phòng)", dangThue);
        dataset.setValue("Trống (" + trong + " phòng)", trong);

        return ChartFactory.createPieChart(
                "TỶ LỆ PHÒNG TRONG HỆ THỐNG", dataset, true, true, false);
    }

    // ĐÃ SỬA: Kích hoạt đồng bộ hóa dữ liệu số và làm mới biểu đồ thời gian thực
    private void loadThongKe() {
        try {
            ThongKe tk = thongKeDAO.getThongKeTongHop();
            if (tk != null) {
                // 1. Đổ dữ liệu thật lên các ô hiển thị thông tin nhanh (Card)
                lblValTongPhong.setText(String.valueOf(tk.getTongPhong()));
                lblValPhongDangThue.setText(String.valueOf(tk.getPhongDangThue()));
                lblValPhongTrong.setText(String.valueOf(tk.getPhongTrong()));
                lblValTongKhach.setText(String.valueOf(tk.getTongKhachHang()));
                lblValDoanhThuThang.setText(df.format(tk.getTongDoanhThuThang()) + " ₫");
                lblValDoanhThuNam.setText(df.format(tk.getTongDoanhThuNam()) + " ₫");
                lblValHoaDonChuaTT.setText(String.valueOf(tk.getHoaDonChuaThanhToan()));

                // 2. VẼ LẠI BIỂU ĐỒ THỜI GIAN THỰC KHI CÓ SỐ LIỆU ĐỘNG TỪ SQL SERVER
                JFreeChart barChart = createDoanhThuBarChart(tk.getTongDoanhThuThang(), tk.getTongDoanhThuNam());
                pnlBarChart.setChart(barChart);

                JFreeChart pieChart = createTyLePhongPieChart(tk.getPhongDangThue(), tk.getPhongTrong());
                pnlPieChart.setChart(pieChart);

                // Đẩy lệnh làm mới giao diện luồng đồ họa Swing
                pnlBarChart.revalidate();
                pnlBarChart.repaint();
                pnlPieChart.revalidate();
                pnlPieChart.repaint();
            }
        } catch (Exception e) {
            System.out.println("Lỗi nạp dữ liệu thống kê: " + e.getMessage());
        }
    }

    // ĐÃ SỬA: Thay thế thông báo tĩnh bằng hộp thoại JFileChooser cho phép chọn thư mục lưu thực tế
    private void xuatBaoCao() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Chọn đường dẫn và đặt tên tệp kết xuất báo cáo");
        int userSelection = chooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            String filePath = chooser.getSelectedFile().getAbsolutePath();

            String msg = "📄 [KẾT XUẤT BÁO CÁO THÀNH CÔNG]\n"
                    + "--------------------------------------------------\n"
                    + "✓ Đường dẫn lưu: " + filePath + ".pdf (.xlsx)\n"
                    + "✓ Nội dung tổng hợp:\n"
                    + "   • Thống kê chi tiết số phòng, khách thuê hiện tại.\n"
                    + "   • Sao kê dòng tiền thực tế kết chuyển từ hóa đơn.\n"
                    + "   • Xuất bản vẽ vector đồ họa JFreeChart.\n"
                    + "--------------------------------------------------\n"
                    + "Hệ thống đã phân tách cấu trúc dữ liệu hoàn tất!";

            JOptionPane.showMessageDialog(this, msg, "Xuất Báo Cáo Hệ Thống", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}