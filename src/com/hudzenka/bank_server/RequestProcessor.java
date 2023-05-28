package com.hudzenka.bank_server;

import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class RequestProcessor implements Runnable {

    private final BlockingQueue<Request> requests = new LinkedBlockingQueue<>(1000);

    public void processRequest(Request request) {
        try {
            boolean success = this.requests.offer(request, 10, TimeUnit.SECONDS);

            if(!success) {
                PrintWriter out = request.getOut();
                out.println("Connection was closed because of timeout in request queue");
                throw new RuntimeException("Connection was closed because of timeout in request queue");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        CommandProcessor commandProcessor = new CommandProcessor();
        try {
            while (true) {
                Request request = this.requests.take();
                String responseMessage = commandProcessor.processCommand(request.getRequestMessage());
                PrintWriter out = request.getOut();
                out.println(responseMessage);
            }

        } catch (InterruptedException e) {
            System.out.println("Server was stopped because of exception");
            throw new RuntimeException(e);
        }
    }
}
