package org.example.util;

import javax.swing.*;

public class DialogUtils {

    public static void showInfoDialog(String message) {
        showDialog(message, JOptionPane.INFORMATION_MESSAGE);
    }
    public static void showErrorDialog(String message) {
        showDialog(message, JOptionPane.ERROR_MESSAGE);
    }

    private static void showDialog(String message, int messageType) {
        JOptionPane optionPane = new JOptionPane(message, messageType);
        JDialog dialog = optionPane.createDialog("Error!");
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);
    }
}
