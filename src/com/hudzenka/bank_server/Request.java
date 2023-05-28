package com.hudzenka.bank_server;

import java.io.PrintWriter;

public class Request {

    private final String requestMessage;
    private final PrintWriter out;


    public Request (PrintWriter out, String requestMessage) {
        this.requestMessage = requestMessage;
        this.out = out;
    }

    public PrintWriter getOut() {
        return out;
    }

    public String getRequestMessage() {
        return requestMessage;
    }

}
