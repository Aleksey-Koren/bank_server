package com.hudzenka.bank_server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerRunner {
    public static void main(String[] args) throws IOException {

        try (ServerSocket server = new ServerSocket(6666)) {
            RequestProcessor requestProcessor = new RequestProcessor();
            new Thread(requestProcessor).start();

            while (true) {
                Socket clientSocket = server.accept();
                new Thread(new ServerTread(clientSocket, requestProcessor)).start();
            }
        }

    }
}