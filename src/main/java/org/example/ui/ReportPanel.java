package org.example.ui;

import org.example.model.Expense;
import org.example.service.ExpenseManager;
import org.example.service.FileHandler;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

/**
 * Report Panel - Halaman 4
 * Modul 6: GUI - JProgressBar, Charts
 * Modul 4: HashMap untuk aggregation
 */
public class ReportPanel extends JPanel {
    private MainFrame mainFrame;
    private ExpenseManager manager;

    private JLabel totalLabel;
    private JLabel transactionLabel;
    private JLabel topCategoryLabel;
    private JLabel avgLabel;
    private JPanel categoryPanel;

    private String currentPeriod = "MONTH"; // DAILY, WEEKLY, MONTH

    // Colors
    private static final Color BG_COLOR = new Color(85, 66, 61);
    private static final Color PRIMARY = new Color(255, 192, 173);
    private static final Color TEXT = new Color(255, 243, 236);
    private static final Color ACCENT = new Color(231, 143, 179);
    private static final Color TERTIARY = new Color(149, 86, 161);

    public ReportPanel(MainFrame mainFrame, ExpenseManager manager) {
        this.mainFrame = mainFrame;
        this.manager = manager;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(20, 20));
        setBackground(BG_COLOR);
        setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // Header
        add(createHeader(), BorderLayout.NORTH);

        // Content
        JPanel contentPanel = new JPanel(new BorderLayout(20, 20));
        contentPanel.setBackground(BG_COLOR);

        // Stats summary
        contentPanel.add(createSummaryPanel(), BorderLayout.NORTH);

        // Category breakdown
        JScrollPane scrollPane = new JScrollPane(createCategoryPanel());
        scrollPane.setBorder(BorderFactory.createLineBorder(PRIMARY, 2));
        scrollPane.getViewport().setBackground(BG_COLOR);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);

        // Bottom buttons
        add(createBottomPanel(), BorderLayout.SOUTH);
    }

    private JPanel createHeader() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(BG_COLOR);

        // Title
        JLabel titleLabel = new JLabel("📈 Laporan Pengeluaran");
        titleLabel.setFont(new Font("Poppins", Font.BOLD, 28));
        titleLabel.setForeground(PRIMARY);

        // Period tabs
        JPanel tabPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        tabPanel.setBackground(BG_COLOR);

        JButton dailyBtn = createTabButton("📅 Harian", "DAILY");
        JButton weeklyBtn = createTabButton("📊 Mingguan", "WEEKLY");
        JButton monthlyBtn = createTabButton("📈 Bulanan", "MONTH");

        // Set active
        monthlyBtn.setBackground(ACCENT);

        dailyBtn.addActionListener(e -> {
            currentPeriod = "DAILY";
            setActiveTab(dailyBtn, weeklyBtn, monthlyBtn);
            refreshData();
        });

        weeklyBtn.addActionListener(e -> {
            currentPeriod = "WEEKLY";
            setActiveTab(weeklyBtn, dailyBtn, monthlyBtn);
            refreshData();
        });

        monthlyBtn.addActionListener(e -> {
            currentPeriod = "MONTH";
            setActiveTab(monthlyBtn, dailyBtn, weeklyBtn);
            refreshData();
        });

        tabPanel.add(dailyBtn);
        tabPanel.add(weeklyBtn);
        tabPanel.add(monthlyBtn);

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(BG_COLOR);
        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(tabPanel, BorderLayout.CENTER);

        panel.add(topPanel);
        return panel;
    }

    private JButton createTabButton(String text, String period) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Poppins", Font.BOLD, 14));
        btn.setBackground(new Color(255, 192, 173, 25));
        btn.setForeground(TEXT);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY, 2),
                BorderFactory.createEmptyBorder(12, 25, 12, 25)
        ));
        return btn;
    }

    private void setActiveTab(JButton active, JButton... others) {
        active.setBackground(ACCENT);
        active.setForeground(new Color(39, 28, 25));
        for (JButton btn : others) {
            btn.setBackground(new Color(255, 192, 173, 25));
            btn.setForeground(TEXT);
        }
    }

    private JPanel createSummaryPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 20, 0));
        panel.setBackground(BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        totalLabel = new JLabel();
        transactionLabel = new JLabel();
        topCategoryLabel = new JLabel();
        avgLabel = new JLabel();

        panel.add(createSummaryCard("💵 Total Pengeluaran", totalLabel));
        panel.add(createSummaryCard("📝 Jumlah Transaksi", transactionLabel));
        panel.add(createSummaryCard("🎯 Kategori Terbanyak", topCategoryLabel));
        panel.add(createSummaryCard("📊 Rata-rata/Hari", avgLabel));

        return panel;
    }

    private JPanel createSummaryCard(String title, JLabel valueLabel) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(new Color(255, 192, 173, 40));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY, 2, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Poppins", Font.PLAIN, 13));
        titleLabel.setForeground(PRIMARY);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        valueLabel.setFont(new Font("Poppins", Font.BOLD, 24));
        valueLabel.setForeground(ACCENT);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createCategoryPanel() {
        categoryPanel = new JPanel();
        categoryPanel.setLayout(new BoxLayout(categoryPanel, BoxLayout.Y_AXIS));
        categoryPanel.setBackground(BG_COLOR);
        categoryPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        return categoryPanel;
    }

    private void updateCategoryBreakdown(Map<String, Double> categoryTotals, double grandTotal) {
        categoryPanel.removeAll();

        // Title
        JLabel titleLabel = new JLabel("📊 Breakdown per Kategori");
        titleLabel.setFont(new Font("Poppins", Font.BOLD, 20));
        titleLabel.setForeground(PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
        categoryPanel.add(titleLabel);

        // Sort by amount
        List<Map.Entry<String, Double>> sortedCategories = new ArrayList<>(categoryTotals.entrySet());
        sortedCategories.sort(Map.Entry.<String, Double>comparingByValue().reversed());

        // Add category items
        for (Map.Entry<String, Double> entry : sortedCategories) {
            String category = entry.getKey();
            double amount = entry.getValue();
            double percentage = (amount / grandTotal) * 100;

            JPanel itemPanel = createCategoryItem(category, amount, percentage);
            itemPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            categoryPanel.add(itemPanel);
            categoryPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        }

        categoryPanel.revalidate();
        categoryPanel.repaint();
    }

    private JPanel createCategoryItem(String category, double amount, double percentage) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(255, 243, 236, 15));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 192, 173, 50), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel nameLabel = new JLabel(getCategoryIcon(category) + " " + category);
        nameLabel.setFont(new Font("Poppins", Font.BOLD, 15));
        nameLabel.setForeground(TEXT);

        JLabel amountLabel = new JLabel(String.format("Rp %,.0f (%.0f%%)", amount, percentage));
        amountLabel.setFont(new Font("Poppins", Font.BOLD, 15));
        amountLabel.setForeground(ACCENT);

        headerPanel.add(nameLabel, BorderLayout.WEST);
        headerPanel.add(amountLabel, BorderLayout.EAST);

        // Progress bar
        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setValue((int) percentage);
        progressBar.setStringPainted(false);
        progressBar.setBackground(new Color(231, 143, 179, 50));
        progressBar.setForeground(ACCENT);
        progressBar.setBorderPainted(false);
        progressBar.setPreferredSize(new Dimension(0, 15));

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(progressBar, BorderLayout.CENTER);

        return panel;
    }

    private String getCategoryIcon(String category) {
        Map<String, String> icons = new HashMap<>();
        icons.put("Makanan & Minuman", "🍔");
        icons.put("Transport & Bensin", "🚗");
        icons.put("Pendidikan & Buku", "📚");
        icons.put("Hiburan & Hobi", "🎮");
        icons.put("Kesehatan", "💊");
        icons.put("Fashion & Pakaian", "👕");
        icons.put("Teknologi & Gadget", "📱");
        icons.put("Kebutuhan Rumah", "🏠");
        icons.put("Lainnya", "💡");
        return icons.getOrDefault(category, "📦");
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        panel.setBackground(BG_COLOR);

        JButton txtBtn = createButton("📄 Export ke TXT", PRIMARY);
        JButton csvBtn = createButton("📊 Export ke CSV", TERTIARY);
        JButton backBtn = createButton("⬅️ Kembali", new Color(149, 86, 161));

        txtBtn.addActionListener(e -> exportToTXT());
        csvBtn.addActionListener(e -> exportToCSV());
        backBtn.addActionListener(e -> mainFrame.showPanel(MainFrame.DASHBOARD));

        panel.add(txtBtn);
        panel.add(csvBtn);
        panel.add(backBtn);

        return panel;
    }

    private JButton createButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Poppins", Font.BOLD, 13));
        btn.setBackground(bgColor);
        btn.setForeground(new Color(39, 28, 25));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        return btn;
    }

    /**
     * Refresh report data based on current period
     */
    public void refreshData() {
        List<Expense> expenses = getExpensesByPeriod();

        // Calculate stats
        double total = expenses.stream().mapToDouble(Expense::getAmount).sum();
        int transactionCount = expenses.size();

        Map<String, Double> categoryTotals = new HashMap<>();
        for (Expense expense : expenses) {
            String category = expense.getCategory();
            categoryTotals.put(category,
                    categoryTotals.getOrDefault(category, 0.0) + expense.getAmount());
        }

        String topCategory = categoryTotals.isEmpty() ? "N/A" :
                categoryTotals.entrySet().stream()
                        .max(Map.Entry.comparingByValue())
                        .get().getKey();

        double avgPerDay = currentPeriod.equals("MONTH") ?
                total / LocalDate.now().lengthOfMonth() : total / getDaysInPeriod();

        // Update labels
        totalLabel.setText(String.format("Rp %.0fk", total / 1000));
        transactionLabel.setText(String.valueOf(transactionCount));
        topCategoryLabel.setText(topCategory);
        avgLabel.setText(String.format("Rp %.0fk", avgPerDay / 1000));

        // Update category breakdown
        if (total > 0) {
            updateCategoryBreakdown(categoryTotals, total);
        }
    }

    private List<Expense> getExpensesByPeriod() {
        LocalDate today = LocalDate.now();
        LocalDate start;

        switch (currentPeriod) {
            case "DAILY":
                return manager.filterByDateRange(today, today);
            case "WEEKLY":
                start = today.minusDays(6);
                return manager.filterByDateRange(start, today);
            case "MONTH":
            default:
                start = today.withDayOfMonth(1);
                return manager.filterByDateRange(start, today);
        }
    }

    private int getDaysInPeriod() {
        switch (currentPeriod) {
            case "DAILY": return 1;
            case "WEEKLY": return 7;
            case "MONTH": return LocalDate.now().lengthOfMonth();
            default: return 1;
        }
    }

    private void exportToTXT() {
        StringBuilder content = new StringBuilder();
        content.append(repeatChar('=', 60)).append("\n");
        content.append("       LAPORAN PENGELUARAN - ").append(getPeriodName()).append("\n");
        content.append(repeatChar('=', 60)).append("\n\n");

        List<Expense> expenses = getExpensesByPeriod();
        double total = expenses.stream().mapToDouble(Expense::getAmount).sum();

        content.append("Total Pengeluaran: ").append(String.format("Rp %,.0f", total)).append("\n");
        content.append("Jumlah Transaksi: ").append(expenses.size()).append("\n\n");

        content.append("Detail Transaksi:\n");
        content.append(repeatChar('-', 60)).append("\n");

        for (Expense expense : expenses) {
            content.append(expense.getFormattedDate()).append(" | ");
            content.append(expense.getCategory()).append(" | ");
            content.append(expense.getDescription()).append(" | ");
            content.append(expense.getFormattedAmount()).append("\n");
        }

        String filename = "laporan_" + currentPeriod.toLowerCase() + "_" +
                LocalDate.now().toString() + ".txt";

        if (FileHandler.exportToTXT(content.toString(), filename)) {
            JOptionPane.showMessageDialog(this,
                    "Laporan berhasil diekspor ke: " + filename);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Gagal mengekspor laporan!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exportToCSV() {
        JOptionPane.showMessageDialog(this,
                "Data CSV sudah tersimpan di: data/expenses.csv");
    }

    private String getPeriodName() {
        switch (currentPeriod) {
            case "DAILY": return "HARIAN";
            case "WEEKLY": return "MINGGUAN";
            case "MONTH": return "BULANAN";
            default: return "UNKNOWN";
        }
    }

    /**
     * Helper method untuk repeat character (Java 8 compatible)
     */
    private String repeatChar(char ch, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(ch);
        }
        return sb.toString();
    }
}