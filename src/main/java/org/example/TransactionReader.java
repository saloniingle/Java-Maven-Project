package org.example;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.dataformat.xml.*;


import javax.xml.bind.JAXBException;



public abstract class TransactionReader {
    private static final Map<String, Double> exchangeRates = new HashMap<>();

    static {
        // Exchange rates initialization
        exchangeRates.put("USDEUR", 0.94);
        exchangeRates.put("EURGBP", 0.86);
        exchangeRates.put("GBPINR", 103.98);
        exchangeRates.put("AUDCAD", 0.89);
        exchangeRates.put("CADUSD", 0.73);
        exchangeRates.put("CHFAUD", 1.69);
        exchangeRates.put("USDCHF", 0.91);
        exchangeRates.put("JPYUSD", 0.0065);
    }
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final XmlMapper xmlMapper = new XmlMapper();
    private static final List<Transaction> transactions = new ArrayList<>();
    public abstract List<Transaction> readTransactions(String filePath) throws IOException, JAXBException;

    public double convertCurrency(double amount, String fromCurrency, String toCurrency) {
        double result = 0.0;
        String key = (fromCurrency + toCurrency).toUpperCase();
        if (exchangeRates.containsKey(key)) {
            double exchangeRate = exchangeRates.get(key);
            result = amount * exchangeRate;
        }
        return result;
    }

    protected boolean isValidCurrency(String currencyCode) {
        return exchangeRates.keySet().stream()
                .anyMatch(key -> key.startsWith(currencyCode.toUpperCase()));
    }
    private boolean containsNumeric(String str) {
        for (char c : str.toCharArray()) {
            if (Character.isDigit(c)) {
                return true;
            }
        }
        return false;
    }
    public void processTransaction(Transaction transaction) {
        if (containsNumeric(transaction.getOriginalCurrency())) {
            System.out.println("Currency code cannot contain numeric characters");
            transaction.setStatus("Invalid original currency should not contain numbers");
            transaction.setConvertedAmount(0);
            return;
        }
        if (containsNumeric(transaction.getTargetCurrency())) {
            System.out.println("Currency code cannot contain numeric characters");
            transaction.setStatus("Invalid target currency should not contain numbers");
            transaction.setConvertedAmount(0);
            return;
        }
        if (!isValidCurrency(transaction.getOriginalCurrency()) ) {
            System.out.println("Invalid currency code in transaction");
            transaction.setStatus("Invalid currency code in original Currency code");
            transaction.setConvertedAmount(0);
            return;
        }
        if (!isValidCurrency(transaction.getTargetCurrency())) {
            System.out.println("Invalid currency code in target currency");
            transaction.setStatus("Invalid currency code in target currency code");
            transaction.setConvertedAmount(0);
            return;
        }

        double convertedAmount = convertCurrency(transaction.getAmount(), transaction.getOriginalCurrency(), transaction.getTargetCurrency());
        if (convertedAmount == 0.0) {
            System.out.println("Conversion issue in transaction");
            transaction.setConvertedAmount(0);
            if(transaction.getAmount()== 0) {
                transaction.setStatus("Success");
            }
            else {
                transaction.setStatus("Failure");
            }
            return;
        }
        transaction.setConvertedAmount(convertedAmount);
        transaction.setStatus("Success");
        System.out.printf("%.2f %s converted to %.2f %s%n", transaction.getAmount(), transaction.getOriginalCurrency(), convertedAmount, transaction.getTargetCurrency());
    }

}
