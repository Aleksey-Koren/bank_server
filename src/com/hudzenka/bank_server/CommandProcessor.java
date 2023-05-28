package com.hudzenka.bank_server;

public class CommandProcessor {

    private final String PATH_TO_TEXTFILE = "D:\\\\bank.txt";

    public String processCommand(String commandMessage) {
        String[] command = commandMessage.split(" ");
        String commandName = command[0];

        if ("deposit".equals(commandName)) {
            return deposit(command);
        } else {
            return "unsupported command: " + commandName;
        }
    }

    private String deposit(String[] command) {
        return "Command passed successfully";
    }
}
