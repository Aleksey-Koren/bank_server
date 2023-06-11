package com.hudzenka.bank_server.model;

import java.io.PrintWriter;

public class Request {

    private final String command;
    private final PrintWriter out;


    public Request (PrintWriter out, String command) {
        this.command = command;
        this.out = out;
    }

    public PrintWriter getOut() {
        return out;
    }

    public String getCommand() {
        return command;
    }

}
