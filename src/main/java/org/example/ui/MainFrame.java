package org.example.ui;

import org.example.service.ExpenseManager;
import javax.swing.*;
import java.awt.*;

/**
 * Main Frame dengan CardLayout untuk navigation
 * Modul 6: GUI - JFrame, CardLayout
 */
public class MainFrame extends JFrame {
    private ExpenseManager manager;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    // Panel references
    private DashboardPanel dashboardPanel;
    private ListPanel listPanel;
    private FormPanel formPanel;
    private ReportPanel reportPanel;

    // Card names
    public static final String DASHBOARD = "Dashboard";
    public static final String LIST = "List";
    public static final String FORM = "Form";
    public static final String REPORT = "Report";

    public MainFrame() {
        manager = new ExpenseManager();
        initComponents();
    }

    private void initComponents() {
        setTitle("💸 Aplikasi Pencatatan Pengeluaran");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 900);
        setLocationRelativeTo(null);

        // Setup CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Initialize panels
        dashboardPanel = new DashboardPanel(this, manager);
        listPanel = new ListPanel(this, manager);
        formPanel = new FormPanel(this, manager);
        reportPanel = new ReportPanel(this, manager);

        // Add panels to CardLayout
        mainPanel.add(dashboardPanel, DASHBOARD);
        mainPanel.add(listPanel, LIST);
        mainPanel.add(formPanel, FORM);
        mainPanel.add(reportPanel, REPORT);

        add(mainPanel);

        // Set color scheme
        getContentPane().setBackground(new Color(85, 66, 61));
    }

    /**
     * Navigate to specific panel
     */
    public void showPanel(String panelName) {
        // Refresh panels before showing
        if (panelName.equals(DASHBOARD)) {
            dashboardPanel.refreshData();
        } else if (panelName.equals(LIST)) {
            listPanel.refreshData();
        } else if (panelName.equals(REPORT)) {
            reportPanel.refreshData();
        }

        cardLayout.show(mainPanel, panelName);
    }

    /**
     * Show form for adding new expense
     */
    public void showAddForm() {
        formPanel.setAddMode();
        showPanel(FORM);
    }

    /**
     * Show form for editing expense
     */
    public void showEditForm(int expenseId) {
        formPanel.setEditMode(expenseId);
        showPanel(FORM);
    }

    public ExpenseManager getManager() {
        return manager;
    }
}