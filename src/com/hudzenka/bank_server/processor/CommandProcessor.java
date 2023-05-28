package com.hudzenka.bank_server.processor;

import com.hudzenka.bank_server.model.Account;

import java.util.Map;

public class CommandProcessor {

    private final DataAccessProcessor dataAccessProcessor;

    public CommandProcessor() {
        dataAccessProcessor = new DataAccessProcessor();
    }
    public String processCommand(String commandMessage) {
        String[] command = commandMessage.split(" ");
        String commandName = command[0];

        switch (commandName) {
            case "deposit":
                return deposit(command);

            default: return "unsupported command: " + commandName;
        }


    }

    private String deposit(String[] command) {
        long accountNumber;
        long depositAmount;
        try {
            accountNumber = Long.parseLong(command[1]);
            depositAmount = convertToLowestUnits(command[2]);
            if (accountNumber <= 0 || depositAmount <= 0) {
                return "request rejected -- invalid parameters. Values have to be positive";
            }
        } catch (Exception e) {
            System.out.println("Invalid parameters");
            e.printStackTrace();
            return "request rejected -- invalid parameters";
        }


        Map<Long, Account> accounts = null;

        try {
            accounts = dataAccessProcessor.readData();
        } catch (Exception e) {
            System.out.println("File reading error");
            e.printStackTrace();
            return "request rejected -- data reading error";
        }

        Account account = accounts.get(accountNumber);
        if (account == null) {
            return "account with number" + accountNumber +  "hasn't been found";
        }

        long balance = account.getBalance();
        balance += depositAmount;
        account.setBalance(balance);

        try {
            dataAccessProcessor.writeData(accounts);
        } catch (Exception e) {
            System.out.println("File reading error");
            e.printStackTrace();
        }

        return "deposit -- success. Current balance = " + retrieveStringFromLowestUnits(balance);
    }

    private long convertToLowestUnits(String value) {
        double doubleValue = Double.parseDouble(value);
        long valueInLowestUnits = (long) (doubleValue * 100);
        return valueInLowestUnits;
    }

    private String retrieveStringFromLowestUnits(long amountInLowestUnits) {
        double doubleValue = amountInLowestUnits / 100.00;
        String stringValue = Double.toString(doubleValue);
        return stringValue;
    }
}
