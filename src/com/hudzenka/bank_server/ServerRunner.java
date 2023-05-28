package com.hudzenka.bank_server;

import com.hudzenka.bank_server.processor.RequestProcessor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerRunner {
    public static void main(String[] args) throws IOException {

        try (ServerSocket server = new ServerSocket(3333)) {
            RequestProcessor requestProcessor = new RequestProcessor();
            new Thread(requestProcessor).start();

            while (true) {
                Socket clientSocket = server.accept();
                new Thread(new ServerTread(clientSocket, requestProcessor)).start();
            }
        }

    }
}