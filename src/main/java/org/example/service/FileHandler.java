package org.example.service;

import org.example.model.Expense;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handler untuk operasi file CSV
 * Modul 5: File Handling - BufferedReader/Writer, Exception Handling
 */
public class FileHandler {
    private static final String FILE_PATH = "data/expenses.csv";
    private static final String HEADER = "id,description,category,amount,date,notes";

    /**
     * Load data dari CSV file
     * Exception Handling: IOException, FileNotFoundException
     */
    public static List<Expense> loadFromCSV() {
        List<Expense> expenses = new ArrayList<>();
        File file = new File(FILE_PATH);

        // Create file if not exists
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
                // Write header
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    writer.write(HEADER);
                    writer.newLine();
                }
            } catch (IOException e) {
                System.err.println("Error creating file: " + e.getMessage());
                return expenses;
            }
        }

        // Read data
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // Skip header

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                try {
                    Expense expense = Expense.fromCSV(line);
                    expenses.add(expense);
                } catch (Exception e) {
                    System.err.println("Error parsing line: " + line);
                    System.err.println("Error: " + e.getMessage());
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + FILE_PATH);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }

        return expenses;
    }

    /**
     * Save data ke CSV file
     * Exception Handling: IOException
     */
    public static boolean saveToCSV(List<Expense> expenses) {
        File file = new File(FILE_PATH);

        try {
            file.getParentFile().mkdirs();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                // Write header
                writer.write(HEADER);
                writer.newLine();

                // Write data
                for (Expense expense : expenses) {
                    writer.write(expense.toCSV());
                    writer.newLine();
                }
            }

            return true;
        } catch (IOException e) {
            System.err.println("Error saving file: " + e.getMessage());
            return false;
        }
    }

    /**
     * Export report to TXT file
     */
    public static boolean exportToTXT(String content, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(content);
            return true;
        } catch (IOException e) {
            System.err.println("Error exporting to TXT: " + e.getMessage());
            return false;
        }
    }
}