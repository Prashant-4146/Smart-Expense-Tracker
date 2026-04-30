package com.tracker.ui.custom;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class RoundedPanel extends JPanel {
    private int cornerRadius = 20;
    private int shadowSize = 5;

    public RoundedPanel() {
        super();
        setOpaque(false);
        setBackground(Theme.CARD_BG);
    }

    public RoundedPanel(int radius) {
        super();
        setOpaque(false);
        this.cornerRadius = radius;
        setBackground(Theme.CARD_BG);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        // Draw shadow
        g2.setColor(Theme.SHADOW_COLOR);
        g2.fillRoundRect(shadowSize, shadowSize, width - shadowSize * 2, height - shadowSize * 2, cornerRadius, cornerRadius);

        // Draw panel background
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, width - shadowSize, height - shadowSize, cornerRadius, cornerRadius);

        g2.dispose();
    }
}
