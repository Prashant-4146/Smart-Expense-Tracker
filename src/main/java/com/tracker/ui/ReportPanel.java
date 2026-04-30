package com.tracker.ui;

import com.tracker.service.ExpenseTrackerService;
import com.tracker.ui.custom.RoundedPanel;
import com.tracker.ui.custom.Theme;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Map;

public class ReportPanel extends RoundedPanel {
    private ExpenseTrackerService service;
    private JPanel chartContainer;

    public ReportPanel(ExpenseTrackerService service) {
        super(20);
        this.service = service;
        setLayout(new BorderLayout(0, 15));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Expense Reports");
        titleLabel.setFont(Theme.FONT_HEADING);
        titleLabel.setForeground(Theme.TEXT_PRIMARY);
        add(titleLabel, BorderLayout.NORTH);

        chartContainer = new JPanel(new GridLayout(2, 1, 0, 20));
        chartContainer.setOpaque(false);
        add(chartContainer, BorderLayout.CENTER);

        refreshCharts();
    }

    public void refreshCharts() {
        chartContainer.removeAll();

        Map<String, Double> expensesByCategory = service.getExpensesByCategory();

        // 1. Pie Chart
        DefaultPieDataset pieDataset = new DefaultPieDataset();
        for (Map.Entry<String, Double> entry : expensesByCategory.entrySet()) {
            pieDataset.setValue(entry.getKey(), entry.getValue());
        }

        JFreeChart pieChart = ChartFactory.createPieChart(
                "", // No title here, we have a panel title
                pieDataset,
                true, true, false
        );
        
        // Theme Pie Chart
        pieChart.setBackgroundPaint(Theme.CARD_BG);
        pieChart.getLegend().setBackgroundPaint(Theme.CARD_BG);
        pieChart.getLegend().setItemPaint(Theme.TEXT_SECONDARY);
        pieChart.getLegend().setBorder(0, 0, 0, 0);

        PiePlot piePlot = (PiePlot) pieChart.getPlot();
        piePlot.setBackgroundPaint(Theme.CARD_BG);
        piePlot.setOutlinePaint(null); // Remove border
        piePlot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0} (₹{1})"));
        piePlot.setLabelBackgroundPaint(Theme.BG_COLOR);
        piePlot.setLabelPaint(Theme.TEXT_PRIMARY);
        piePlot.setLabelOutlinePaint(null);
        piePlot.setLabelShadowPaint(null);
        piePlot.setShadowPaint(null);

        ChartPanel pieChartPanel = new ChartPanel(pieChart);
        pieChartPanel.setOpaque(false);

        // 2. Bar Chart
        DefaultCategoryDataset barDataset = new DefaultCategoryDataset();
        for (Map.Entry<String, Double> entry : expensesByCategory.entrySet()) {
            barDataset.addValue(entry.getValue(), "Expenses", entry.getKey());
        }

        JFreeChart barChart = ChartFactory.createBarChart(
                "",
                "",
                "Amount (₹)",
                barDataset,
                PlotOrientation.VERTICAL,
                false, true, false
        );

        // Theme Bar Chart
        barChart.setBackgroundPaint(Theme.CARD_BG);
        
        CategoryPlot barPlot = barChart.getCategoryPlot();
        barPlot.setBackgroundPaint(Theme.CARD_BG);
        barPlot.setOutlinePaint(null);
        barPlot.setRangeGridlinePaint(Theme.BORDER_COLOR);
        
        barPlot.getDomainAxis().setTickLabelPaint(Theme.TEXT_SECONDARY);
        barPlot.getDomainAxis().setLabelPaint(Theme.TEXT_SECONDARY);
        barPlot.getDomainAxis().setAxisLinePaint(Theme.BORDER_COLOR);
        
        barPlot.getRangeAxis().setTickLabelPaint(Theme.TEXT_SECONDARY);
        barPlot.getRangeAxis().setLabelPaint(Theme.TEXT_SECONDARY);
        barPlot.getRangeAxis().setAxisLinePaint(Theme.BORDER_COLOR);

        BarRenderer renderer = (BarRenderer) barPlot.getRenderer();
        renderer.setBarPainter(new StandardBarPainter()); // Flat style, no gradient
        renderer.setSeriesPaint(0, Theme.PRIMARY);
        renderer.setDrawBarOutline(false);
        renderer.setShadowVisible(false);

        ChartPanel barChartPanel = new ChartPanel(barChart);
        barChartPanel.setOpaque(false);

        chartContainer.add(pieChartPanel);
        chartContainer.add(barChartPanel);

        chartContainer.revalidate();
        chartContainer.repaint();
    }
}
