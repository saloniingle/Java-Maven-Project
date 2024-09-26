package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class JsonTransaction extends TransactionReader {
    public static final ObjectMapper objectMapper = new ObjectMapper();

    static class JSONFileTransaction
    {
        private Transaction[] transactions;
//        public JSONFileTransaction(){}
//        public JSONFileTransaction(Transaction[] transactions)
        {
            this.transactions = transactions;
        }

        public Transaction[] getTransactions() {
            return transactions;
        }

        public void setTransactions(Transaction[] transactions) {
            this.transactions = transactions;
        }
    }
    @Override
    public List<Transaction> readTransactions(String filePath) throws IOException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JSONFileTransaction transactions = objectMapper.readValue(new File(filePath), JsonTransaction.JSONFileTransaction.class);
        return Arrays.asList(transactions.getTransactions());
    }
    public static void writeTransactionsToJson(List<Transaction> transactions, String outputPath) throws IOException {
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(outputPath), transactions);
    }


}
