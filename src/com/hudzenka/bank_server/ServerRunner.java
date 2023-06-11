package com.hudzenka.bank_server;

import com.hudzenka.bank_server.processor.RequestProcessor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerRunner {
    public static void main(String[] args) throws IOException {

        try (ServerSocket server = new ServerSocket(3333)) {

            RequestProcessor requestProcessor = new RequestProcessor();
            Thread requestProcessorThread = new Thread(requestProcessor);
            requestProcessorThread.start();

            while (true) {
                Socket clientSocket = server.accept();
                ServerTread serverTread = new ServerTread(clientSocket, requestProcessor);
                Thread thread = new Thread(serverTread);
                thread.start();
            }
        }

    }
}