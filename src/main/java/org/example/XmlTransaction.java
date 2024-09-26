package org.example;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XmlTransaction extends TransactionReader {
    @Override
    public List<Transaction> readTransactions(String filePath) throws IOException {
        List<Transaction> transactions = new ArrayList<>();
        try {
            File inputFile = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("transaction");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    Transaction transaction = new Transaction();
                    transaction.setAmount(Double.parseDouble(element.getElementsByTagName("Amount").item(0).getTextContent()));
                    transaction.setOriginalCurrency(element.getElementsByTagName("OriginalCurrency").item(0).getTextContent());
                    transaction.setTargetCurrency(element.getElementsByTagName("TargetCurrency").item(0).getTextContent());
                    transactions.add(transaction);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transactions;
    }
public static void writeTransactionsToXml(List<Transaction> transactions, String filePath) throws IOException {
    try {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.newDocument();


        Element rootElement = doc.createElement("transactions");
        doc.appendChild(rootElement);


        for (Transaction transaction : transactions) {
            Element transactionElement = doc.createElement("transaction");
            rootElement.appendChild(transactionElement);

            Element amountElement = doc.createElement("Amount");
            amountElement.appendChild(doc.createTextNode(String.valueOf(transaction.getAmount())));
            transactionElement.appendChild(amountElement);

            Element originalCurrencyElement = doc.createElement("OriginalCurrency");
            originalCurrencyElement.appendChild(doc.createTextNode(transaction.getOriginalCurrency()));
            transactionElement.appendChild(originalCurrencyElement);

            Element targetCurrencyElement = doc.createElement("TargetCurrency");
            targetCurrencyElement.appendChild(doc.createTextNode(transaction.getTargetCurrency()));
            transactionElement.appendChild(targetCurrencyElement);

            Element convertedAmountElement = doc.createElement("ConvertedAmount");
            convertedAmountElement.appendChild(doc.createTextNode(String.valueOf(transaction.getConvertedAmount())));
            transactionElement.appendChild(convertedAmountElement);

            Element statusElement = doc.createElement("Status");
            statusElement.appendChild(doc.createTextNode(transaction.getStatus()));
            transactionElement.appendChild(statusElement);
        }
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes"); // Set indentation
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4"); // Set indentation amount
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(filePath));
        transformer.transform(source, result);
    } catch (ParserConfigurationException | TransformerException e) {
        e.printStackTrace();
    }
}
}