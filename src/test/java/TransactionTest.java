import org.example.*;
import org.junit.jupiter.api.Test;
import javax.xml.bind.JAXBException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class TransactionTest {

    private final TransactionReader reader = new TransactionReader() {
        @Override
        public List<Transaction> readTransactions(String filePath) throws IOException, JAXBException {
            return List.of();
        }
    };

    @Test
    public void testConvertCurrency_USD_to_EUR() {
        double result = reader.convertCurrency(100.0, "USD", "EUR");
        assertEquals(94.0, result);
    }

    @Test
    public void testConvertCurrency_EUR_to_GBP() {
        double result = new JsonTransaction().convertCurrency(200.0, "EUR", "GBP");
        assertEquals(172.0, result);
    }

    @Test
    public void testConvertCurrency_GBP_to_INR() {
        double result = reader.convertCurrency(150.0, "GBP", "INR");
        assertEquals(15597.0, result);
    }

    @Test
    public void testConvertCurrency_NotFound() {
        double result = reader.convertCurrency(100.0, "XYZ", "ABC");
        assertEquals(0.0, result);
    }

    @Test
    public void testConvertCurrency_ZeroAmount() {
        double result = reader.convertCurrency(0.0, "USD", "EUR");
        assertEquals(0.0, result);
    }

    @Test
    public void testGetFileExtension_Valid() {
        assertEquals("json", Main.getFileExtension("file.json"));
        assertEquals("csv", Main.getFileExtension("file.csv"));
        assertEquals("xml", Main.getFileExtension("file.xml"));
        assertEquals("txt", Main.getFileExtension("file.txt"));
    }

    @Test
    public void testGetFileExtension_InvalidPath() {
        try {
            Main.getFileExtension("noextension");
        } catch (IllegalArgumentException e) {
            assert true;
            return;
        }
        assert false;
    }

    @Test
    public void testGetFileExtension_NoExtension() {
        try {
            Main.getFileExtension("file.");
        } catch (IllegalArgumentException e) {
            assert true;
            return;
        }
        assert false;
    }

    @Test
    public void testGetFileExtension_InvalidExtension() {
        try {
            Main.getFileExtension("file");
        } catch (IllegalArgumentException e) {
            assert true;
            return;
        }
        assert false;
    }

    @Test
    public void testReadWriteTransactionsToCsv() throws IOException {
        String inputFilePath = "C:\\Users\\Checkout\\IdeaProjects\\202_individual\\src\\transaction.csv";
        String outputFilePath = "C:\\Users\\Checkout\\IdeaProjects\\202_individual\\src\\output.csv";
        String TempoutputFilePath = "C:\\Users\\Checkout\\IdeaProjects\\202_individual\\src\\output1.csv";

        // Read transactions from the input CSV file
        CsvTransaction csvTransaction = new CsvTransaction();
        List<Transaction> originalTransactions = csvTransaction.readTransactions(inputFilePath);

        // Write transactions to a new CSV file
        csvTransaction.writeTransactionsToCsv(originalTransactions, TempoutputFilePath);

        // Read transactions from the output CSV file
        List<Transaction> writtenTransactions = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(outputFilePath))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(","); // Assuming CSV format with comma-separated values
                if (parts.length >= 5) {
                    double amount = Double.parseDouble(parts[0].replaceAll("\"", "").trim());
                    String originalCurrency = parts[1].replaceAll("\"", "").trim();
                    String targetCurrency = parts[2].replaceAll("\"", "").trim();
                    double convertedAmount = Double.parseDouble(parts[3].replaceAll("\"", "").trim());
                    String status = parts[4].replaceAll("\"", "").trim();

                    Transaction transaction = new Transaction(amount, originalCurrency, targetCurrency, convertedAmount, status);

                    writtenTransactions.add(transaction);
                } else {
                    System.err.println("Incomplete or malformed line: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();

        }

        // Compare the transactions read from the input and output files
        assertEquals(originalTransactions.size(), writtenTransactions.size());
        for (int i = 0; i < originalTransactions.size(); i++) {

        }

        // Check if the output file exists
        File outputFile = new File(outputFilePath);
        assertTrue(outputFile.exists());
    }

    @Test
    public void testWriteTransactionsToJson() throws IOException {

        String inputFilePath = "C:\\Users\\Checkout\\IdeaProjects\\202_individual\\src\\transaction.json";
        String outputFilePath = "C:\\Users\\Checkout\\IdeaProjects\\202_individual\\src\\output.json";

        // Checking read method
        List<Transaction> transactions = new JsonTransaction().readTransactions(inputFilePath);

        // checking write method
        JsonTransaction.writeTransactionsToJson(transactions, outputFilePath);

        // Checking if output file exists
        File outputFile = new File(outputFilePath);
        assertTrue(outputFile.exists());
    }
    @Test
    public void testWriteTransactionsToXml() throws IOException {

        String inputFilePath = "C:\\Users\\Checkout\\IdeaProjects\\202_individual\\src\\transaction.xml";
        String originalOutputFilePath = "C:\\Users\\Checkout\\IdeaProjects\\202_individual\\src\\output.xml";
        String tempOutputFilePath = "C:\\Users\\Checkout\\IdeaProjects\\202_individual\\src\\sample_output.xml";

        // Reading transactions from the input XML file
        List<Transaction> originalTransactions = new XmlTransaction().readTransactions(inputFilePath);

        // Writing transactions to the sample output XML file
        XmlTransaction.writeTransactionsToXml(originalTransactions, tempOutputFilePath);

        // Reading transactions from the original output XML file
        List<Transaction> writtenTransactions = new XmlTransaction().readTransactions(originalOutputFilePath);

        // Reading transactions from the sample output XML file
        List<Transaction> sampleTransactions = new XmlTransaction().readTransactions(tempOutputFilePath);

        // Comparing transactions from original output file and sample output file
        assertEquals(writtenTransactions.size(), sampleTransactions.size(), "Number of transactions should match");
        for (int i = 0; i < writtenTransactions.size(); i++) {
            Transaction writtenTransaction = writtenTransactions.get(i);
            Transaction sampleTransaction = sampleTransactions.get(i);
            assertEquals(writtenTransaction.getAmount(), sampleTransaction.getAmount(), 0.001);
            assertEquals(writtenTransaction.getOriginalCurrency(), sampleTransaction.getOriginalCurrency());
            assertEquals(writtenTransaction.getTargetCurrency(), sampleTransaction.getTargetCurrency());
        }
        // Check if the original output file exists
        File originalOutputFile = new File(originalOutputFilePath);
        assertTrue(originalOutputFile.exists());
    }
}

















