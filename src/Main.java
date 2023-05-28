import com.hudzenka.bank_server.RequestProcessor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws IOException {

        ServerSocket server = new ServerSocket(6666);
        RequestProcessor requestProcessor = new RequestProcessor();
        new Thread(requestProcessor).start();

        while (true) {
            Socket clientSocket = server.accept();

        }


    }
}