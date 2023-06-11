package com.hudzenka.bank_server.processor;

import com.hudzenka.bank_server.model.Request;

import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class RequestProcessor implements Runnable {

    private final BlockingQueue<Request> requestsQueue = new LinkedBlockingQueue<>(1000);

    public void processRequest(Request request) {
        try {
            boolean success = this.requestsQueue.offer(request, 10, TimeUnit.SECONDS);
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
        DataAccessProcessor dataAccessProcessor = new DataAccessProcessor();
        CommandProcessor commandProcessor = new CommandProcessor(dataAccessProcessor);

        try {
            while (true) {

                Request requestFromTheQueue = this.requestsQueue.take();
                String commandFromTheRequest = requestFromTheQueue.getCommand();
                String responseMessage = commandProcessor.processCommand(commandFromTheRequest);
                PrintWriter out = requestFromTheQueue.getOut();
                out.println(responseMessage);
            }

        } catch (Exception e) {
            System.out.println("Exception due request processing");
            e.printStackTrace();
        }
    }
}
