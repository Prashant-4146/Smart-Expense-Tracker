package com.tracker.ui;

import com.tracker.model.Credit;
import com.tracker.model.Expense;
import com.tracker.service.ExpenseTrackerService;
import com.tracker.ui.custom.RoundedPanel;
import com.tracker.ui.custom.Theme;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Comparator;
import java.util.List;

public class ReportPanel extends RoundedPanel {
    private ExpenseTrackerService service;
    private JPanel chartContainer;

    public ReportPanel(ExpenseTrackerService service) {
        super(20);
        this.service = service;
        setLayout(new BorderLayout(0, 15));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Financial Trend (Credits vs Expenses)");
        titleLabel.setFont(Theme.FONT_HEADING);
        titleLabel.setForeground(Theme.TEXT_PRIMARY);
        add(titleLabel, BorderLayout.NORTH);

        chartContainer = new JPanel(new BorderLayout());
        chartContainer.setOpaque(false);
        add(chartContainer, BorderLayout.CENTER);

        refreshCharts();
    }

    public void refreshCharts() {
        chartContainer.removeAll();

        List<Expense> expenses = service.getAllExpenses();
        // Sort expenses chronologically
        expenses.sort(Comparator.comparing(Expense::getDate).thenComparing(Expense::getId));

        Credit credit = service.getCredit();
        double initialCredits = (credit != null) ? credit.getTotalAmount() : 0.0;

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        // Initial state
        dataset.addValue(initialCredits, "Remaining Credits", "Start");
        dataset.addValue(0.0, "Cumulative Expenses", "Start");

        double cumulativeExpense = 0.0;
        int step = 1;

        for (Expense exp : expenses) {
            cumulativeExpense += exp.getAmount();
            double remainingCredit = initialCredits - cumulativeExpense;
            
            // X-axis label: date + sequence to keep it unique if same date
            String xLabel = exp.getDate().toString() + " (#" + step + ")";
            
            dataset.addValue(remainingCredit, "Remaining Credits", xLabel);
            dataset.addValue(cumulativeExpense, "Cumulative Expenses", xLabel);
            step++;
        }

        JFreeChart lineChart = ChartFactory.createLineChart(
                "", // No title here since we have a panel title
                "Timeline (Date / Sequence)", // X-Axis Label
                "Amount (₹)", // Y-Axis Label
                dataset,
                PlotOrientation.VERTICAL,
                true, // Include legend
                true, // Tooltips
                false // URLs
        );

        // Theme Line Chart
        lineChart.setBackgroundPaint(Theme.CARD_BG);
        lineChart.getLegend().setBackgroundPaint(Theme.CARD_BG);
        lineChart.getLegend().setItemPaint(Theme.TEXT_SECONDARY);
        lineChart.getLegend().setBorder(0, 0, 0, 0);

        CategoryPlot plot = lineChart.getCategoryPlot();
        plot.setBackgroundPaint(Theme.CARD_BG);
        plot.setOutlinePaint(null);
        plot.setRangeGridlinePaint(Theme.BORDER_COLOR);
        plot.setDomainGridlinePaint(Theme.BORDER_COLOR);

        plot.getDomainAxis().setTickLabelPaint(Theme.TEXT_SECONDARY);
        plot.getDomainAxis().setLabelPaint(Theme.TEXT_SECONDARY);
        plot.getDomainAxis().setAxisLinePaint(Theme.BORDER_COLOR);

        plot.getRangeAxis().setTickLabelPaint(Theme.TEXT_SECONDARY);
        plot.getRangeAxis().setLabelPaint(Theme.TEXT_SECONDARY);
        plot.getRangeAxis().setAxisLinePaint(Theme.BORDER_COLOR);

        LineAndShapeRenderer renderer = new LineAndShapeRenderer();
        // Credits Line (Green/Success)
        renderer.setSeriesPaint(0, Theme.SUCCESS);
        renderer.setSeriesStroke(0, new BasicStroke(3.0f));
        renderer.setSeriesShapesVisible(0, true);
        
        // Expenses Line (Red/Danger)
        renderer.setSeriesPaint(1, Theme.DANGER);
        renderer.setSeriesStroke(1, new BasicStroke(3.0f));
        renderer.setSeriesShapesVisible(1, true);

        plot.setRenderer(renderer);

        ChartPanel chartPanel = new ChartPanel(lineChart);
        chartPanel.setOpaque(false);
        chartContainer.add(chartPanel, BorderLayout.CENTER);

        chartContainer.revalidate();
        chartContainer.repaint();
    }
}
