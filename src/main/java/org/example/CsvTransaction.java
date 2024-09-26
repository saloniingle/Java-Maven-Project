package org.example;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvTransaction extends TransactionReader {
    @Override
    public List<Transaction> readTransactions(String filePath) throws IOException {
        List<Transaction> transactions = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            List<String[]> rows = reader.readAll();
            boolean skipRow = true;
            for (String[] row : rows) {
                if (row.length == 3 && !skipRow) {
                    System.out.println(row[0]);
                    double amount = Double.parseDouble(row[0]);

                    String originalCurrency = row[1];
                    String targetCurrency = row[2];
                    transactions.add(new Transaction(amount, originalCurrency, targetCurrency));
                } else {
                    System.out.println("Invalid CSV format");
                }
                skipRow = false;
            }
        } catch (CsvException e) {
            throw new RuntimeException(e);
        }
        return transactions;
    }
    public static void writeTransactionsToCsv(List<Transaction> transactions, String outputPath) throws IOException {
        try (CSVWriter writer = new CSVWriter(new FileWriter(outputPath))) {
            String[] header = {"Amount", "OriginalCurrency", "TargetCurrency", "ConvertedAmount", "Status"};
            writer.writeNext(header);
            for (Transaction transaction : transactions) {
                writer.writeNext(new String[] {String.valueOf(transaction.getAmount()), transaction.getOriginalCurrency(), transaction.getTargetCurrency(), String.valueOf(transaction.getConvertedAmount()),transaction.getStatus()});
            }
        }
}
}
