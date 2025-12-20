package org.example.ui;

import org.example.model.Expense;
import org.example.service.ExpenseManager;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Form Panel - Halaman 3
 * Modul 6: GUI - JTextField, JComboBox, Input Validation
 * Modul 1: Exception Handling - try-catch
 * FIXED: ComboBox dropdown color issue
 */
public class FormPanel extends JPanel {
    private MainFrame mainFrame;
    private ExpenseManager manager;

    private JTextField descField;
    private JComboBox<String> categoryCombo;
    private JTextField amountField;
    private JTextField dateField;
    private JTextField notesField;

    private JButton saveBtn;
    private JButton cancelBtn;
    private JLabel titleLabel;

    private boolean isEditMode = false;
    private int editingId = -1;

    // Colors - ORIGINAL
    private static final Color BG_COLOR = new Color(85, 66, 61);
    private static final Color PRIMARY = new Color(255, 192, 173);
    private static final Color TEXT = new Color(255, 243, 236);
    private static final Color ACCENT = new Color(231, 143, 179);

    private static final String[] CATEGORIES = {
            "-- Pilih Kategori --",
            "Makanan & Minuman", "Transport & Bensin",
            "Pendidikan & Buku", "Hiburan & Hobi", "Kesehatan",
            "Fashion & Pakaian", "Teknologi & Gadget", "Kebutuhan Rumah", "Lainnya"
    };

    public FormPanel(MainFrame mainFrame, ExpenseManager manager) {
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

        // Form
        add(createFormPanel(), BorderLayout.CENTER);

        // Buttons
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel createHeader() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(255, 192, 173, 30));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY, 2, true),
                BorderFactory.createEmptyBorder(25, 30, 25, 30)
        ));

        titleLabel = new JLabel("➕ Tambah Pengeluaran Baru");
        titleLabel.setFont(new Font("Poppins", Font.BOLD, 28));
        titleLabel.setForeground(PRIMARY);

        panel.add(titleLabel);
        return panel;
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(85, 66, 61, 100));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY, 2, true),
                BorderFactory.createEmptyBorder(35, 40, 35, 40)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Description
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(createLabel("📝 Deskripsi Pengeluaran *"), gbc);

        gbc.gridy = 1;
        descField = createTextField("Contoh: Makan siang di kantin kampus");
        panel.add(descField, gbc);

        // Category
        gbc.gridy = 2;
        panel.add(createLabel("🏷Kategori *"), gbc);

        gbc.gridy = 3;
        categoryCombo = new JComboBox<>(CATEGORIES);
        categoryCombo.setFont(new Font("Poppins", Font.PLAIN, 14));
        categoryCombo.setBackground(new Color(255, 243, 236, 25));
        categoryCombo.setForeground(new Color(39, 28, 25)); // Text hitam
        categoryCombo.setPreferredSize(new Dimension(0, 45));
        categoryCombo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY, 2),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));

        // FIXED: Custom renderer untuk dropdown - perbaiki warna hitam!
        categoryCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                          int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                // Fix warna dropdown
                if (isSelected) {
                    label.setBackground(ACCENT); // Pink saat dipilih
                    label.setForeground(new Color(39, 28, 25)); // Dark text
                } else {
                    // Background cream untuk item yang tidak dipilih (BUKAN HITAM!)
                    label.setBackground(new Color(255, 243, 236)); // Cream
                    label.setForeground(new Color(39, 28, 25)); // Dark text
                }

                label.setOpaque(true);
                label.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
                label.setFont(new Font("Poppins", Font.PLAIN, 13));

                return label;
            }
        });

        // PENTING: Override warna popup list secara langsung
        try {
            Object child = categoryCombo.getAccessibleContext().getAccessibleChild(0);
            if (child instanceof javax.swing.plaf.basic.ComboPopup) {
                JList<?> popupList = ((javax.swing.plaf.basic.ComboPopup) child).getList();
                popupList.setBackground(new Color(255, 243, 236)); // Cream background
                popupList.setForeground(new Color(39, 28, 25)); // Dark text
                popupList.setSelectionBackground(ACCENT); // Pink saat dipilih
                popupList.setSelectionForeground(new Color(39, 28, 25)); // Dark text saat dipilih
            }
        } catch (Exception ex) {
            // Ignore if can't access popup
        }

        panel.add(categoryCombo, gbc);

        // Amount
        gbc.gridy = 4;
        panel.add(createLabel("💰 Jumlah (Rupiah) *"), gbc);

        gbc.gridy = 5;
        amountField = createTextField(": 50000");
        panel.add(amountField, gbc);

        // Date
        gbc.gridy = 6;
        panel.add(createLabel("📅 Tanggal *"), gbc);

        gbc.gridy = 7;
        dateField = createTextField("DD/MM/YYYY");
        dateField.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        panel.add(dateField, gbc);

        // Notes
        gbc.gridy = 8;
        panel.add(createLabel("📌 Catatan Tambahan (Opsional)"), gbc);

        gbc.gridy = 9;
        notesField = createTextField("Tambahkan keterangan");
        panel.add(notesField, gbc);

        return panel;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Poppins", Font.BOLD, 15));
        label.setForeground(PRIMARY);
        return label;
    }

    private JTextField createTextField(String placeholder) {
        JTextField field = new JTextField(30);
        field.setFont(new Font("Poppins", Font.PLAIN, 14));
        field.setBackground(new Color(255, 243, 236, 25));
        field.setForeground(TEXT);
        field.setCaretColor(TEXT);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY, 2),
                BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));

        // Placeholder effect
        field.setText(placeholder);
        field.setForeground(new Color(255, 243, 236, 100));
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(TEXT);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(new Color(255, 243, 236, 100));
                }
            }
        });

        return field;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        panel.setBackground(BG_COLOR);

        saveBtn = createButton("💾 Simpan Data", PRIMARY);
        cancelBtn = createButton("❌ Batal", new Color(149, 86, 161));

        saveBtn.addActionListener(e -> saveExpense());
        cancelBtn.addActionListener(e -> {
            clearForm();
            mainFrame.showPanel(MainFrame.DASHBOARD);
        });

        panel.add(saveBtn);
        panel.add(cancelBtn);

        return panel;
    }

    private JButton createButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Poppins", Font.BOLD, 16));
        btn.setBackground(bgColor);
        btn.setForeground(new Color(39, 28, 25));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(15, 40, 15, 40));
        return btn;
    }

    /**
     * Set form to ADD mode
     */
    public void setAddMode() {
        isEditMode = false;
        editingId = -1;
        titleLabel.setText("➕ Tambah Pengeluaran Baru");
        saveBtn.setText("💾 Simpan Data");
        clearForm();
    }

    /**
     * Set form to EDIT mode
     */
    public void setEditMode(int expenseId) {
        isEditMode = true;
        editingId = expenseId;
        titleLabel.setText("✏️ Edit Pengeluaran");
        saveBtn.setText("💾 Update Data");

        // Load data
        Expense expense = manager.getExpenseById(expenseId);
        if (expense != null) {
            descField.setText(expense.getDescription());
            descField.setForeground(TEXT);

            categoryCombo.setSelectedItem(expense.getCategory());

            amountField.setText(String.valueOf((int)expense.getAmount()));
            amountField.setForeground(TEXT);

            dateField.setText(expense.getFormattedDate());
            dateField.setForeground(TEXT);

            notesField.setText(expense.getNotes());
            notesField.setForeground(TEXT);
        }
    }

    /**
     * Save expense with validation
     * Exception Handling: try-catch untuk parse data
     */
    private void saveExpense() {
        try {
            // Get values
            String desc = descField.getText().trim();
            String category = (String) categoryCombo.getSelectedItem();
            String amountText = amountField.getText().trim();
            String dateText = dateField.getText().trim();
            String notes = notesField.getText().trim();

            // Validation
            if (desc.isEmpty() || desc.equals("Contoh: Makan siang di kantin kampus")) {
                throw new IllegalArgumentException("Deskripsi tidak boleh kosong!");
            }

            if (category == null || category.equals("-- Pilih Kategori --")) {
                throw new IllegalArgumentException("Silakan pilih kategori!");
            }

            if (amountText.isEmpty() || amountText.equals("Masukkan nominal tanpa titik. Contoh: 50000")) {
                throw new IllegalArgumentException("Jumlah tidak boleh kosong!");
            }

            // Parse amount (Exception handling)
            double amount;
            try {
                amount = Double.parseDouble(amountText);
                if (amount <= 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Jumlah harus berupa angka positif!");
            }

            // Parse date (Exception handling)
            LocalDate date;
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                date = LocalDate.parse(dateText, formatter);
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Format tanggal salah! Gunakan DD/MM/YYYY");
            }

            // Clean notes
            if (notes.equals("Tambahkan keterangan jika diperlukan...")) {
                notes = "";
            }

            // Save or update
            boolean success;
            if (isEditMode) {
                success = manager.updateExpense(editingId, desc, category, amount, date, notes);
            } else {
                success = manager.addExpense(desc, category, amount, date, notes);
            }

            if (success) {
                JOptionPane.showMessageDialog(this,
                        isEditMode ? "Data berhasil diupdate!" : "Data berhasil disimpan!",
                        "Sukses",
                        JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                mainFrame.showPanel(MainFrame.DASHBOARD);
            } else {
                throw new Exception("Gagal menyimpan data!");
            }

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Validasi Error",
                    JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Terjadi kesalahan: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Clear all form fields
     */
    private void clearForm() {
        descField.setText("Contoh: Makan siang di kantin kampus");
        descField.setForeground(new Color(255, 243, 236, 100));

        categoryCombo.setSelectedIndex(0);

        amountField.setText(": 50000");
        amountField.setForeground(new Color(255, 243, 236, 100));

        dateField.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        dateField.setForeground(TEXT);

        notesField.setText("Tambahkan keterangan");
        notesField.setForeground(new Color(255, 243, 236, 100));
    }
}