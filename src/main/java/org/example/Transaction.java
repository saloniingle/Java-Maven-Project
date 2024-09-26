package org.example;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Transaction {
    private double Amount;
    private String OriginalCurrency;
    private String TargetCurrency;
    private double ConvertedAmount;
    private String Status;

    public Transaction() {}

    public Transaction(double amount, String originalCurrency, String targetCurrency, double convertedAmount, String status) {
    }

    @JsonProperty("ConvertedAmount")
    public double getConvertedAmount() {
        return ConvertedAmount;
    }

    public void setConvertedAmount(double convertedAmount) {
        ConvertedAmount = convertedAmount;
    }
    @JsonProperty("Status")
    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public Transaction(double Amount, String OriginalCurrency, String TargetCurrency) {
        this.Amount = Amount;
        this.OriginalCurrency = OriginalCurrency;
        this.TargetCurrency = TargetCurrency;
        this.ConvertedAmount = 0;
        this.Status =  "";

    }
    @JsonProperty("Amount")
    public double getAmount() {
        return Amount;
    }

    public void setAmount(double amount) {
        this.Amount = amount;
    }
    @JsonProperty("OriginalCurrency")
    public String getOriginalCurrency() {
        return OriginalCurrency;
    }

    public void setOriginalCurrency(String originalCurrency) {
        this.OriginalCurrency = originalCurrency;
    }
    @JsonProperty("TargetCurrency")
    public String getTargetCurrency() {
        return TargetCurrency;
    }

    public void setTargetCurrency(String targetCurrency) {
        this.TargetCurrency = targetCurrency;
    }


}
