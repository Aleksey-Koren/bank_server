package com.hudzenka.bank_server;

import com.hudzenka.bank_server.model.Request;
import com.hudzenka.bank_server.processor.RequestProcessor;

import java.io.*;
import java.net.Socket;

public class ServerTread implements Runnable {

    private final Socket clientSocket;
    private final RequestProcessor processor;

    public ServerTread(Socket clientSocket, RequestProcessor processor) {
        this.clientSocket = clientSocket;
        this.processor = processor;
    }

    @Override
    public void run() {
        try (Socket clientSocket = this.clientSocket;
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

            while (true) {
                String requestMessage = in.readLine();

                if ("exit".equals(requestMessage)) {
                    System.out.println("Exit command received. Session is closed");
                    break;
                }

                Request request = new Request(out, requestMessage);
                processor.processRequest(request);
            }
        } catch (Exception e) {
            System.out.println("Exception during session. Session is closed");
            e.printStackTrace();
        }
    }
}
