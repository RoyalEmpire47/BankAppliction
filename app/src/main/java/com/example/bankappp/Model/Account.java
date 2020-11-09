package com.example.bankappp.Model;

import java.util.ArrayList;
import java.util.Locale;

public class Account {

    private String accountName;
    private String accountNo;
    private double accountBalance;
    private ArrayList<Transaction> transactions;
    private long dbID;

    public Account (String accountName, String accountNo, double accountBalance) {
        this.accountName = accountName;
        this.accountNo = accountNo;
        this.accountBalance = accountBalance;
        transactions = new ArrayList<>();
    }

    public Account (String accountName, String accountNo, double accountBalance, long dbID) {
        this(accountName, accountNo, accountBalance);
        this.dbID = dbID;
    }

    /**
     * Getters for the account name, number and balance
     */
    public String getAccountName() {
        return accountName;
    }
    public String getAccountNo() {
        return accountNo;
    }
    public double getAccountBalance() {
        return accountBalance;
    }

    public void setDbID(long dbID) { this.dbID = dbID; }

    public void setAccountBalance(double accountBalance) { this.accountBalance = accountBalance; }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public void addPaymentTransaction (String payee, double amount) {
        accountBalance -= amount;

        int paymentCount = 0;

        for (int i = 0; i < transactions.size(); i++) {
            if (transactions.get(i).getTransactionType() == Transaction.TRANSACTION_TYPE.PAYMENT)  {
                paymentCount++;
            }
        }

        Transaction payment = new Transaction("T" + (transactions.size() + 1) + "-P" + (paymentCount+1), payee, amount);
        transactions.add(payment);
    }

    public void addDepositTransaction(double amount) {
        accountBalance += amount;

        int depositsCount = 0;

        for (int i = 0; i < transactions.size(); i++) {
            if (transactions.get(i).getTransactionType() == Transaction.TRANSACTION_TYPE.DEPOSIT)  {
                depositsCount++;
            }
        }

        Transaction deposit = new Transaction("T" + (transactions.size() + 1) + "-D" + (depositsCount+1), amount);
        transactions.add(deposit);
    }

    public String toString() {
        return (accountName + " (RS." + String.format(Locale.getDefault(), "%.2f",accountBalance) + ")");
    }

    public String toTransactionString() { return (accountName + " (" + accountNo + ")"); }

    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }
}
