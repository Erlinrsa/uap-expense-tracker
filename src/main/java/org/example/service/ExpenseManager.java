package org.example.service;

import org.example.model.Expense;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Manager untuk business logic dan CRUD operations
 * Modul 1: Program Correctness
 * Modul 2: Refactoring - Extract methods, clean code
 * Modul 4: Java API - ArrayList, Comparator, HashMap, Stream
 */
public class ExpenseManager {
    private List<Expense> expenses;
    private int nextId;

    public ExpenseManager() {
        expenses = new ArrayList<>();
        loadData();
    }

    /**
     * Load data from file
     */
    private void loadData() {
        expenses = FileHandler.loadFromCSV();
        nextId = expenses.isEmpty() ? 1 : expenses.stream()
                .mapToInt(Expense::getId)
                .max()
                .orElse(0) + 1;
    }

    /**
     * Save data to file
     */
    private void saveData() {
        FileHandler.saveToCSV(expenses);
    }

    // ==================== CRUD OPERATIONS ====================

    /**
     * CREATE - Add new expense
     */
    public boolean addExpense(String description, String category, double amount,
                              LocalDate date, String notes) {
        try {
            // Validation
            if (description == null || description.trim().isEmpty()) {
                throw new IllegalArgumentException("Deskripsi tidak boleh kosong");
            }
            if (category == null || category.trim().isEmpty()) {
                throw new IllegalArgumentException("Kategori harus dipilih");
            }
            if (amount <= 0) {
                throw new IllegalArgumentException("Jumlah harus lebih dari 0");
            }
            if (date == null) {
                throw new IllegalArgumentException("Tanggal tidak boleh kosong");
            }

            Expense expense = new Expense(nextId++, description, category, amount, date, notes);
            expenses.add(expense);
            saveData();
            return true;
        } catch (Exception e) {
            System.err.println("Error adding expense: " + e.getMessage());
            return false;
        }
    }

    /**
     * READ - Get all expenses
     */
    public List<Expense> getAllExpenses() {
        return new ArrayList<>(expenses);
    }

    /**
     * READ - Get expense by ID
     */
    public Expense getExpenseById(int id) {
        return expenses.stream()
                .filter(e -> e.getId() == id)
                .findFirst()
                .orElse(null);
    }

    /**
     * UPDATE - Update existing expense
     */
    public boolean updateExpense(int id, String description, String category,
                                 double amount, LocalDate date, String notes) {
        try {
            Expense expense = getExpenseById(id);
            if (expense == null) {
                throw new IllegalArgumentException("Data tidak ditemukan");
            }

            // Validation
            if (description == null || description.trim().isEmpty()) {
                throw new IllegalArgumentException("Deskripsi tidak boleh kosong");
            }
            if (amount <= 0) {
                throw new IllegalArgumentException("Jumlah harus lebih dari 0");
            }

            expense.setDescription(description);
            expense.setCategory(category);
            expense.setAmount(amount);
            expense.setDate(date);
            expense.setNotes(notes);

            saveData();
            return true;
        } catch (Exception e) {
            System.err.println("Error updating expense: " + e.getMessage());
            return false;
        }
    }

    /**
     * DELETE - Delete expense
     */
    public boolean deleteExpense(int id) {
        try {
            boolean removed = expenses.removeIf(e -> e.getId() == id);
            if (removed) {
                saveData();
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("Error deleting expense: " + e.getMessage());
            return false;
        }
    }

    // ==================== SEARCH & FILTER ====================

    /**
     * Search expenses by keyword
     */
    public List<Expense> searchExpenses(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllExpenses();
        }

        String searchTerm = keyword.toLowerCase();
        return expenses.stream()
                .filter(e -> e.getDescription().toLowerCase().contains(searchTerm) ||
                        e.getCategory().toLowerCase().contains(searchTerm) ||
                        e.getFormattedDate().contains(searchTerm))
                .collect(Collectors.toList());
    }

    /**
     * Filter by category
     */
    public List<Expense> filterByCategory(String category) {
        if (category == null || category.equals("Semua")) {
            return getAllExpenses();
        }

        return expenses.stream()
                .filter(e -> e.getCategory().equals(category))
                .collect(Collectors.toList());
    }

    /**
     * Filter by date range
     */
    public List<Expense> filterByDateRange(LocalDate startDate, LocalDate endDate) {
        return expenses.stream()
                .filter(e -> !e.getDate().isBefore(startDate) && !e.getDate().isAfter(endDate))
                .collect(Collectors.toList());
    }

    // ==================== SORTING ====================

    /**
     * Sort by date (newest first)
     */
    public List<Expense> sortByDateDesc() {
        List<Expense> sorted = new ArrayList<>(expenses);
        sorted.sort(Comparator.comparing(Expense::getDate).reversed());
        return sorted;
    }

    /**
     * Sort by amount (highest first)
     */
    public List<Expense> sortByAmountDesc() {
        List<Expense> sorted = new ArrayList<>(expenses);
        sorted.sort(Comparator.comparing(Expense::getAmount).reversed());
        return sorted;
    }

    // ==================== STATISTICS ====================

    /**
     * Get total for today
     */
    public double getTotalToday() {
        LocalDate today = LocalDate.now();
        return expenses.stream()
                .filter(e -> e.getDate().equals(today))
                .mapToDouble(Expense::getAmount)
                .sum();
    }

    /**
     * Get total for current month
     */
    public double getTotalThisMonth() {
        LocalDate now = LocalDate.now();
        return expenses.stream()
                .filter(e -> e.getDate().getMonth() == now.getMonth() &&
                        e.getDate().getYear() == now.getYear())
                .mapToDouble(Expense::getAmount)
                .sum();
    }

    /**
     * Get total for current week
     */
    public double getTotalThisWeek() {
        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.minusDays(6);
        return filterByDateRange(weekStart, today).stream()
                .mapToDouble(Expense::getAmount)
                .sum();
    }

    /**
     * Get transaction count
     */
    public int getTransactionCount() {
        return expenses.size();
    }

    /**
     * Get average per day for current month
     */
    public double getAveragePerDay() {
        double total = getTotalThisMonth();
        int daysInMonth = LocalDate.now().lengthOfMonth();
        return total / daysInMonth;
    }

    /**
     * Get expenses grouped by category
     */
    public Map<String, Double> getTotalByCategory() {
        Map<String, Double> categoryTotals = new HashMap<>();

        for (Expense expense : expenses) {
            String category = expense.getCategory();
            categoryTotals.put(category,
                    categoryTotals.getOrDefault(category, 0.0) + expense.getAmount());
        }

        return categoryTotals;
    }

    /**
     * Get most expensive category
     */
    public String getMostExpensiveCategory() {
        Map<String, Double> totals = getTotalByCategory();
        return totals.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("N/A");
    }
}