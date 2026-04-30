package com.tracker;

import com.tracker.ui.LoginFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Set modern Look and Feel for Swing
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Run UI on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}
