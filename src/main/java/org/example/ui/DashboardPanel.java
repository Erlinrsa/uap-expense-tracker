package org.example.ui;

import org.example.service.ExpenseManager;
import javax.swing.*;
import java.awt.*;

/**
 * Dashboard Panel - Halaman 1
 * Modul 6: GUI - JPanel, GridLayout, JButton, JLabel
 */
public class DashboardPanel extends JPanel {
    private MainFrame mainFrame;
    private ExpenseManager manager;

    // Stat labels
    private JLabel totalMonthLabel;
    private JLabel totalTransactionLabel;
    private JLabel avgPerDayLabel;
    private JLabel totalTodayLabel;

    // Colors
    private static final Color BG_COLOR = new Color(85, 66, 61);
    private static final Color CARD_BG = new Color(255, 192, 173, 25);
    private static final Color PRIMARY = new Color(255, 192, 173);
    private static final Color TEXT = new Color(255, 243, 236);
    private static final Color ACCENT = new Color(231, 143, 179);

    public DashboardPanel(MainFrame mainFrame, ExpenseManager manager) {
        this.mainFrame = mainFrame;
        this.manager = manager;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(20, 20));
        setBackground(BG_COLOR);
        setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // Header
        JPanel headerPanel = createHeader();
        add(headerPanel, BorderLayout.NORTH);

        // Stats
        JPanel statsPanel = createStatsPanel();
        add(statsPanel, BorderLayout.CENTER);

        // Action buttons
        JPanel actionPanel = createActionPanel();
        add(actionPanel, BorderLayout.SOUTH);

        refreshData();
    }

    private JPanel createHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(255, 192, 173, 30));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY, 2, true),
                BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));

        JLabel titleLabel = new JLabel("💸 Dashboard Keuangan Saya");
        titleLabel.setFont(new Font("Poppins", Font.BOLD, 36));
        titleLabel.setForeground(PRIMARY);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        panel.add(titleLabel);
        return panel;
    }

    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 25, 25));
        panel.setBackground(BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));

        // Create stat cards
        totalMonthLabel = new JLabel();
        totalTransactionLabel = new JLabel();
        avgPerDayLabel = new JLabel();
        totalTodayLabel = new JLabel();

        panel.add(createStatCard("💵", "Total Bulan Ini", totalMonthLabel));
        panel.add(createStatCard("📝", "Total Transaksi", totalTransactionLabel));
        panel.add(createStatCard("📈", "Rata-rata/Hari", avgPerDayLabel));
        panel.add(createStatCard("🎯", "Hari Ini", totalTodayLabel));

        return panel;
    }

    private JPanel createStatCard(String icon, String label, JLabel valueLabel) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY, 2, true),
                BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));

        // Icon
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Value
        valueLabel.setFont(new Font("Poppins", Font.BOLD, 32));
        valueLabel.setForeground(PRIMARY);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Label
        JLabel textLabel = new JLabel(label);
        textLabel.setFont(new Font("Poppins", Font.PLAIN, 14));
        textLabel.setForeground(TEXT);
        textLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Layout
        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        centerPanel.setOpaque(false);
        centerPanel.add(valueLabel);
        centerPanel.add(textLabel);

        card.add(iconLabel, BorderLayout.NORTH);
        card.add(centerPanel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createActionPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 20, 0));
        panel.setBackground(BG_COLOR);

        // Create buttons
        JButton addBtn = createActionButton("➕ Tambah Pengeluaran");
        JButton listBtn = createActionButton("📋 Lihat Semua Data");
        JButton reportBtn = createActionButton("📊 Laporan Detail");

        // Add action listeners
        addBtn.addActionListener(e -> mainFrame.showAddForm());
        listBtn.addActionListener(e -> mainFrame.showPanel(MainFrame.LIST));
        reportBtn.addActionListener(e -> mainFrame.showPanel(MainFrame.REPORT));

        panel.add(addBtn);
        panel.add(reportBtn);
        panel.add(listBtn);

        return panel;
    }

    private JButton createActionButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Poppins", Font.BOLD, 16));
        btn.setBackground(PRIMARY);
        btn.setForeground(new Color(39, 28, 25));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Hover effect
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(ACCENT);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(PRIMARY);
            }
        });

        return btn;
    }

    /**
     * Refresh statistics data
     */
    public void refreshData() {
        double totalMonth = manager.getTotalThisMonth();
        int totalTrans = manager.getTransactionCount();
        double avgDay = manager.getAveragePerDay();
        double totalToday = manager.getTotalToday();

        totalMonthLabel.setText(formatRupiah(totalMonth));
        totalTransactionLabel.setText(String.valueOf(totalTrans));
        avgPerDayLabel.setText(formatRupiah(avgDay));
        totalTodayLabel.setText(formatRupiah(totalToday));
    }

    /**
     * Format angka ke format Rupiah Indonesia
     */
    private String formatRupiah(double amount) {
        return String.format("Rp %,.0f", amount).replace(",", ".");
    }
}