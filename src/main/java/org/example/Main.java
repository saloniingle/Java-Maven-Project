package org.example;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String filePath = args[0];
        String outputFilePath = args[1];
        String fileExtension = getFileExtension(filePath);
        TransactionReader transactionReader;
        switch (fileExtension) {
            case "json":
                transactionReader = new JsonTransaction();
                break;

            case "csv":
                transactionReader = new CsvTransaction();
                break;

            case "xml":
                transactionReader = new XmlTransaction();
                break;

            default:
                throw new IllegalArgumentException("Unsupported file format: " + fileExtension);
        }

        try {
            List<Transaction> transactions = transactionReader.readTransactions(filePath);

            // Process transactions
            for (Transaction transaction : transactions) {
                transactionReader.processTransaction(transaction);
            }
            writeTransactionsToFile(transactions, outputFilePath, fileExtension);
        } catch (IOException | JAXBException e) {
            e.printStackTrace();
        }
    }

    private static void writeTransactionsToFile(List<Transaction> transactions, String outputPath, String fileExtension) throws IOException, JAXBException {
        switch (fileExtension) {
            case "json":
                JsonTransaction.writeTransactionsToJson(transactions, outputPath);
                break;
            case "csv":
                CsvTransaction.writeTransactionsToCsv(transactions, outputPath);
                break;
            case "xml":
                XmlTransaction.writeTransactionsToXml(transactions, outputPath);
                break;
            default:
                throw new IllegalArgumentException("Unsupported file format: " + fileExtension);
        }
    }


    public static String getFileExtension(String filePath) {
        int lastIndex = filePath.lastIndexOf('.');
        if (lastIndex == -1 || lastIndex == filePath.length() - 1) {
            throw new IllegalArgumentException("Invalid file path: " + filePath);
        }
        return filePath.substring(lastIndex + 1).toLowerCase();
    }
}
