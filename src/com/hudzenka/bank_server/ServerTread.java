package com.hudzenka.bank_server;

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
                if(requestMessage == null || "exit".equals(requestMessage)) {
                    out.println("Session cosed");
                    System.out.println("Session has been closed by client");
                    break;
                }
                Request request = new Request(out, requestMessage);
                processor.processRequest(request);
            }

        } catch (Exception e) {
            System.out.println("Exception during session. Session is closed. Resources are cosed");
            e.printStackTrace();

        }
    }
}
