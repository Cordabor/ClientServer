import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class JavaFXForm extends Application {
    private TCPServer tcpServer;
    private TCPSender tcpSender;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setOnCloseRequest(event -> {
            if (tcpServer != null) {
                tcpServer.stopServer();
            }
            if (tcpSender != null) {
                tcpSender.stopTCPSender();
            }
            Platform.exit();
            System.exit(0);
        });

        tcpServer = new TCPServer();
        tcpSender = new TCPSender();

        Thread serverThread = new Thread(() -> tcpServer.start());
        Thread senderThread = new Thread(() -> tcpSender.startTCPSender());

        serverThread.start();
        senderThread.start();

        primaryStage.setTitle("Программа");
        primaryStage.setScene(new Scene(new StackPane(), 400, 300));
        primaryStage.show();
    }
}