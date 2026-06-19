package view;

import org.jfree.chart.*;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import javax.swing.*;
import java.awt.*;

public class PanelThongKe extends JPanel {

    public PanelThongKe() {
        setLayout(new BorderLayout());

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(5000000, "Thu Nhập", "Tháng 4");
        dataset.addValue(7500000, "Thu Nhập", "Tháng 5");
        dataset.addValue(12000000, "Thu Nhập", "Tháng 6"); // Tháng hiện tại

        JFreeChart chart = ChartFactory.createBarChart(
                "BIỂU ĐỒ THU NHẬP",
                "Thời gian",
                "Số tiền (VNĐ)",
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false
        );

        ChartPanel chartPanel = new ChartPanel(chart);
        add(chartPanel, BorderLayout.CENTER);
    }
}