package org.example.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Model class untuk merepresentasikan data pengeluaran
 * Modul 1: Program Correctness - Validasi data
 * Modul 4: Java API - LocalDate, DateTimeFormatter
 */
public class Expense {
    private int id;
    private String description;
    private String category;
    private double amount;
    private LocalDate date;
    private String notes;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Constructor lengkap
     */
    public Expense(int id, String description, String category, double amount, LocalDate date, String notes) {
        this.id = id;
        this.description = description;
        this.category = category;
        this.amount = amount;
        this.date = date;
        this.notes = notes;
    }

    /**
     * Constructor tanpa notes
     */
    public Expense(int id, String description, String category, double amount, LocalDate date) {
        this(id, description, category, amount, date, "");
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getNotes() {
        return notes;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * Format tanggal untuk display
     */
    public String getFormattedDate() {
        return date.format(DATE_FORMATTER);
    }

    /**
     * Format amount untuk display (Rupiah)
     */
    public String getFormattedAmount() {
        return String.format("Rp %,.0f", amount);
    }

    /**
     * Convert ke CSV format
     */
    public String toCSV() {
        return String.join(",",
                String.valueOf(id),
                description,
                category,
                String.valueOf(amount),
                date.toString(),
                notes
        );
    }

    /**
     * Parse dari CSV string
     */
    public static Expense fromCSV(String csvLine) {
        String[] parts = csvLine.split(",", -1);
        return new Expense(
                Integer.parseInt(parts[0]),
                parts[1],
                parts[2],
                Double.parseDouble(parts[3]),
                LocalDate.parse(parts[4]),
                parts.length > 5 ? parts[5] : ""
        );
    }

    @Override
    public String toString() {
        return String.format("[%d] %s - %s: %s (%s)",
                id, getFormattedDate(), category, getFormattedAmount(), description);
    }
}