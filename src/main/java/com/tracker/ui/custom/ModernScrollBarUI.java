package com.tracker.ui.custom;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

public class ModernScrollBarUI extends BasicScrollBarUI {
    private final int THUMB_SIZE = 8;

    @Override
    protected JButton createDecreaseButton(int orientation) {
        return createZeroButton();
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return createZeroButton();
    }

    private JButton createZeroButton() {
        JButton button = new JButton();
        Dimension zeroDim = new Dimension(0, 0);
        button.setPreferredSize(zeroDim);
        button.setMinimumSize(zeroDim);
        button.setMaximumSize(zeroDim);
        return button;
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        g.setColor(Theme.BG_COLOR);
        g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Theme.BORDER_COLOR);
        
        int w = thumbBounds.width;
        int h = thumbBounds.height;
        
        if (scrollbar.getOrientation() == java.awt.Adjustable.VERTICAL) {
            g2.fillRoundRect(thumbBounds.x + (w - THUMB_SIZE) / 2, thumbBounds.y, THUMB_SIZE, h, THUMB_SIZE, THUMB_SIZE);
        } else {
            g2.fillRoundRect(thumbBounds.x, thumbBounds.y + (h - THUMB_SIZE) / 2, w, THUMB_SIZE, THUMB_SIZE, THUMB_SIZE);
        }
        
        g2.dispose();
    }
}
