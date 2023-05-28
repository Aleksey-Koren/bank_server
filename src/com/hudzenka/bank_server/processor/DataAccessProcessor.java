package com.hudzenka.bank_server.processor;

import com.hudzenka.bank_server.exception.DataAccessException;
import com.hudzenka.bank_server.exception.FileParsingException;
import com.hudzenka.bank_server.model.Account;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataAccessProcessor {

    private final String PATH_TO_DATAFILE = "D:\\\\bank.txt";
    private final String FILE_DELIMITER = ";";

    public Map<Long, Account> readData() {
        Map<Long, Account> accounts = null;
        try {
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

    public void writeData(Map<Long, Account> accounts) {
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
}
