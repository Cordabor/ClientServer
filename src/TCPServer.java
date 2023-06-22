import java.io.IOException;
import java.io.InputStream;
import java.io.FileWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class TCPServer {

    //private String ipAddress = "192.168.1.23";
    private String ipAddress = "192.168.1.10";
    private int serverPort = 59830;
    private static BlockingQueue<String> messageQueue = new ArrayBlockingQueue<>(1024);
    private volatile boolean running = true;

    public void start() {
        Thread messageProcessor = new Thread(() -> {
            while (running) {
                try {
                    String message = messageQueue.take();
                    writeToFile(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        messageProcessor.start();

        try {
            ServerSocket serverSocket = new ServerSocket(serverPort, 0, InetAddress.getByName(ipAddress));

            while (running) {
                Socket socket = serverSocket.accept();
                Thread t = new Thread(() -> {
                    try {
                        InputStream input = socket.getInputStream();
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = input.read(buffer)) != -1) {
                            byte[] bufferCopy = Arrays.copyOfRange(buffer, 0, bytesRead);
                            String message = new String(bufferCopy, StandardCharsets.UTF_8);
                            messageQueue.put(message);
                        }
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                t.start();
            }

            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeToFile(String message) {
        try {
            FileWriter writer = new FileWriter("D:\\files\\file1.txt", true);
            writer.write(message + "\n");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopServer() {
        running = false;
    }
}