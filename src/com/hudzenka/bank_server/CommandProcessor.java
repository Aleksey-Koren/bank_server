package com.hudzenka.bank_server;

import com.hudzenka.bank_server.exception.DataAccessException;
import com.hudzenka.bank_server.exception.FileParsingException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandProcessor {

    private final String PATH_TO_DATAFILE = "D:\\\\bank.txt";
    private final String FILE_DELIMITER = ";";

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
            accounts = readData();
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
            writeData(accounts);
        } catch (Exception e) {
            System.out.println("File reading error");
            e.printStackTrace();
        }

        return "deposit -- success. Current balance = " + retrieveStringFromLovestUnits(balance);
    }

    private Map<Long, Account> readData() {
        Map<Long, Account> accounts = null;
        try (FileReader fileReader = new FileReader(PATH_TO_DATAFILE)) {
            Path path = Paths.get(PATH_TO_DATAFILE);
            List<String> lines = Files.readAllLines(path);
            accounts = mapFileLinesToObjects(lines);
            return accounts;
        } catch (Exception e) {
            System.out.println("Error during reading data file");
            throw new DataAccessException(e);
        }
    }

    private Map<Long, Account> mapFileLinesToObjects(List<String> lines) {

            Map<Long, Account> accounts = new HashMap<>();
            for (String line : lines) {
                String[] split = line.split(FILE_DELIMITER);
                if(split.length != 5) {
                    throw new FileParsingException("Corrupted line in datafile");
                }
                Account account = new Account();
                account.setAccountNumber(Long.parseLong(split[0]));
                account.setFirstName(split[1]);
                account.setLastName(split[2]);
                account.setPesel(split[3]);
                account.setBalance(Long.parseLong(split[4]));
                accounts.put(account.getAccountNumber(), account);
            }
            return accounts;
    }

    private void writeData(Map<Long, Account> accounts) {
        try {
            List<String> lines = mapToLines(accounts);
            Path pathToFile = Paths.get(PATH_TO_DATAFILE);
            Files.deleteIfExists(pathToFile);
            Files.createFile(pathToFile);
            for(String line : lines) {
                Files.writeString(pathToFile, line + System.lineSeparator(), StandardOpenOption.APPEND);
            }
        } catch (IOException e) {
            System.out.println("Error during writing data file");
            throw new DataAccessException(e);
        }
    }

    private List<String> mapToLines(Map<Long, Account> accounts) {
        List<String> result = new ArrayList<>();
        for (Account account : accounts.values()) {
            String accountNumber = Long.toString(account.getAccountNumber());
            String balance = Long.toString(account.getBalance());
            String accountAsString = accountNumber + FILE_DELIMITER
                    + account.getFirstName() + FILE_DELIMITER
                    + account.getLastName() + FILE_DELIMITER
                    + account.getPesel() + FILE_DELIMITER
                    + balance;
            result.add(accountAsString);
        }

        return result;
    }

    private long convertToLowestUnits(String value) {
        double doubleValue = Double.parseDouble(value);
        long valueInLowestUnits = (long) doubleValue * 100;
        return valueInLowestUnits;
    }

    private String retrieveStringFromLovestUnits(long amountInLowestUnits) {
        double doubleValue = amountInLowestUnits / 100.00;
        String stringValue = Double.toString(doubleValue);
        return stringValue;
    }
}
