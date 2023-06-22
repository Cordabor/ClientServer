import java.io.*;
import java.net.*;
import java.util.concurrent.TimeUnit;

public class TCPSender {
    private boolean running;
    private String serverIP;
    private int serverPort;
    private String lastMessage;

    public void startTCPSender() {
        running = true;
        //serverIP = "192.168.1.23";// IP address of the Android
        serverIP = "192.168.1.11";// IP address of the Android
        //serverIP = "100.96.1.3";// IP address of the Android
        //serverIP = "192.168.1.195"; // IP address of the Android
        serverPort = 1194; // Port of the Android
        //serverPort = 59830; // Port of the Android
        //serverPort = 8080; // Port of the Android
        lastMessage = "";

        Thread senderThread = new Thread(() -> {
            while (running) {
                File file = new File("D:\\files\\file2.txt");
                if (file.exists()) {
                    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                        StringBuilder sb = new StringBuilder();
                        String line = reader.readLine();
                        while (line != null) {
                            sb.append(line.trim());
                            line = reader.readLine();
                        }

                        String message = sb.toString();
                        if (!message.equals(lastMessage)) {
                            // Create a socket and connect to the server
                            try (Socket socket = new Socket(serverIP, serverPort)) {
                                // Get the output stream from the socket
                                OutputStream out = socket.getOutputStream();
                                PrintWriter writer = new PrintWriter(out);

                                // Send the message
                                writer.println(message);
                                writer.flush();
                            }
                            lastMessage = message;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        senderThread.start();
    }

    public void stopTCPSender() {
        running = false;
    }
}
