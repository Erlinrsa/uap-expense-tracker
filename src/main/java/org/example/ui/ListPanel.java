package org.example.ui;

import org.example.model.Expense;
import org.example.service.ExpenseManager;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

/**
 * List Panel - Halaman 2
 * Modul 6: GUI - JTable, JTextField, JButton
 * Modul 4: Sorting & Filtering
 */
public class ListPanel extends JPanel {
    private MainFrame mainFrame;
    private ExpenseManager manager;

    private JTextField searchField;
    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<String> categoryFilter;

    // Colors
    private static final Color BG_COLOR = new Color(85, 66, 61);
    private static final Color PRIMARY = new Color(255, 192, 173);
    private static final Color TEXT = new Color(255, 243, 236);
    private static final Color ACCENT = new Color(231, 143, 179);

    private static final String[] CATEGORIES = {
            "Semua", "Makanan & Minuman", "Transport & Bensin",
            "Pendidikan & Buku", "Hiburan & Hobi", "Kesehatan",
            "Fashion & Pakaian", "Teknologi & Gadget", "Kebutuhan Rumah", "Lainnya"
    };

    public ListPanel(MainFrame mainFrame, ExpenseManager manager) {
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

        // Table
        add(createTablePanel(), BorderLayout.CENTER);

        // Bottom buttons
        add(createBottomPanel(), BorderLayout.SOUTH);
    }

    private JPanel createHeader() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(BG_COLOR);

        // Title
        JLabel titleLabel = new JLabel("📋 Semua Transaksi");
        titleLabel.setFont(new Font("Poppins", Font.BOLD, 28));
        titleLabel.setForeground(PRIMARY);

        // Search bar
        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
        searchPanel.setBackground(BG_COLOR);

        searchField = new JTextField();
        searchField.setFont(new Font("Poppins", Font.PLAIN, 14));
        searchField.setBackground(new Color(255, 243, 236, 25));
        searchField.setForeground(TEXT);
        searchField.setCaretColor(TEXT);
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY, 2),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        JButton searchBtn = createButton("🔍 Cari", PRIMARY);
        searchBtn.addActionListener(e -> performSearch());

        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchBtn, BorderLayout.EAST);

        // Filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        filterPanel.setBackground(BG_COLOR);

        JLabel filterLabel = new JLabel("Kategori:");
        filterLabel.setForeground(TEXT);
        filterLabel.setFont(new Font("Poppins", Font.PLAIN, 14));

        categoryFilter = new JComboBox<>(CATEGORIES);
        categoryFilter.setFont(new Font("Poppins", Font.PLAIN, 13));
        categoryFilter.setForeground(new Color(39, 28, 25)); // ← TEXT HITAM
        categoryFilter.setPreferredSize(new Dimension(200, 40)); // ← TINGGI 40px
        categoryFilter.setBackground(new Color(255, 243, 236, 25));
        categoryFilter.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY, 2),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        categoryFilter.addActionListener(e -> performFilter());

        JButton sortDateBtn = createButton("📅 Terbaru", ACCENT);
        JButton sortAmountBtn = createButton("💰 Terbesar", ACCENT);

        sortDateBtn.addActionListener(e -> sortByDate());
        sortAmountBtn.addActionListener(e -> sortByAmount());

        filterPanel.add(filterLabel);
        filterPanel.add(categoryFilter);
        filterPanel.add(sortDateBtn);
        filterPanel.add(sortAmountBtn);

        // Layout
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(BG_COLOR);
        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(searchPanel, BorderLayout.CENTER);
        topPanel.add(filterPanel, BorderLayout.SOUTH);

        panel.add(topPanel);
        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_COLOR);

        // Table model
        String[] columns = {"ID", "Deskripsi", "Kategori", "Tanggal", "Jumlah", "Aksi"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Only action column
            }
        };

        table = new JTable(tableModel);
        table.setFont(new Font("Poppins", Font.PLAIN, 13));
        table.setRowHeight(45);
        table.setBackground(new Color(255, 243, 236, 15));
        table.setForeground(TEXT);
        table.setGridColor(PRIMARY);
        table.setSelectionBackground(new Color(255, 192, 173, 50));
        table.setSelectionForeground(TEXT);

        // Header style
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Poppins", Font.BOLD, 14));
        header.setBackground(new Color(255, 192, 173, 80));
        header.setForeground(new Color(39, 28, 25));

        // Hide ID column
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);

        // Action column with buttons
        table.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(PRIMARY, 2));
        scrollPane.getViewport().setBackground(BG_COLOR);

        panel.add(scrollPane);
        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        panel.setBackground(BG_COLOR);

        JButton backBtn = createButton("⬅️ Kembali ke Dashboard", new Color(149, 86, 161));
        backBtn.addActionListener(e -> mainFrame.showPanel(MainFrame.DASHBOARD));

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
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return btn;
    }

    /**
     * Refresh table data
     */
    public void refreshData() {
        loadTableData(manager.sortByDateDesc());
    }

    private void loadTableData(List<Expense> expenses) {
        tableModel.setRowCount(0);

        for (Expense expense : expenses) {
            Object[] row = {
                    expense.getId(),
                    expense.getDescription(),
                    expense.getCategory(),
                    expense.getFormattedDate(),
                    expense.getFormattedAmount(),
                    "action" // Placeholder for action buttons
            };
            tableModel.addRow(row);
        }
    }

    private void performSearch() {
        String keyword = searchField.getText();
        List<Expense> results = manager.searchExpenses(keyword);
        loadTableData(results);
    }

    private void performFilter() {
        String category = (String) categoryFilter.getSelectedItem();
        List<Expense> results = manager.filterByCategory(category);
        loadTableData(results);
    }

    private void sortByDate() {
        List<Expense> sorted = manager.sortByDateDesc();
        loadTableData(sorted);
    }

    private void sortByAmount() {
        List<Expense> sorted = manager.sortByAmountDesc();
        loadTableData(sorted);
    }

    // Button Renderer for action column
    class ButtonRenderer extends JPanel implements TableCellRenderer {
        public ButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            removeAll();

            JButton editBtn = new JButton("✏️");
            JButton deleteBtn = new JButton("🗑️");

            editBtn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
            deleteBtn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));

            add(editBtn);
            add(deleteBtn);

            return this;
        }
    }

    // Button Editor for action column
    class ButtonEditor extends DefaultCellEditor {
        private JPanel panel;
        private JButton editBtn;
        private JButton deleteBtn;
        private int currentRow;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);

            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));

            editBtn = new JButton("✏️");
            deleteBtn = new JButton("🗑️");

            editBtn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
            deleteBtn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));

            editBtn.addActionListener(e -> editExpense());
            deleteBtn.addActionListener(e -> deleteExpense());

            panel.add(editBtn);
            panel.add(deleteBtn);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            currentRow = row;
            return panel;
        }

        private void editExpense() {
            int id = (int) tableModel.getValueAt(currentRow, 0);
            mainFrame.showEditForm(id);
            fireEditingStopped();
        }

        private void deleteExpense() {
            int confirm = JOptionPane.showConfirmDialog(
                    ListPanel.this,
                    "Yakin ingin menghapus data ini?",
                    "Konfirmasi Hapus",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                int id = (int) tableModel.getValueAt(currentRow, 0);
                if (manager.deleteExpense(id)) {
                    JOptionPane.showMessageDialog(ListPanel.this,
                            "Data berhasil dihapus!");
                    refreshData();
                } else {
                    JOptionPane.showMessageDialog(ListPanel.this,
                            "Gagal menghapus data!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            fireEditingStopped();
        }
    }
}