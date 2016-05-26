package hido.panic.ui;

import hido.panic.Main;
import hido.panic.cipher.Cipher;
import hido.panic.cipher.CipherFactory;
import hido.panic.cipher.CipherMode;
import hido.panic.cipher.CipherType;
import hido.panic.file.FileProcessor;
import hido.panic.file.ThreadsPool;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class Menu extends Application {

    private static final int HEIGHT = 500;
    private static final int WIDTH = 500;
    private static List<String> paths;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("PanicFiles v0.1");
        primaryStage.setHeight(HEIGHT);
        primaryStage.setWidth(WIDTH);
        TextField key = new TextField("Key");
        TextField initVector = new TextField("Init Vector");
        ComboBox<String> algorithm = new ComboBox<>();

        algorithm.setValue("AES_CFB");
        algorithm.getItems().add("AES_CFB");

        TextArea files = new TextArea();
        Button encryptNow = new Button("Encrypt now");
        Button settings = new Button("Settings");
        Button decryptNow = new Button("Decrypt now");
        Button createScript = new Button("Create script");
        Button hideToTray = new Button("Hide to tray");
        Button loadList = new Button("Load list");
        FlowPane flowPane = new FlowPane();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select list");
        fileChooser.setInitialDirectory(new File("/"));
        flowPane.getChildren().add(key);
        flowPane.getChildren().add(initVector);
        flowPane.getChildren().add(loadList);
        loadList.setOnAction(event -> {
            paths = getPathsFromFile(fileChooser.showOpenDialog(primaryStage));
            files.setText(getFilesWithSizes(paths));
            for (String path : paths) {
                System.out.println(path);
            }
        });
        files.setEditable(false);
        flowPane.getChildren().add(algorithm);
        flowPane.getChildren().add(files);
        flowPane.getChildren().add(encryptNow);
        flowPane.getChildren().add(decryptNow);

        flowPane.getChildren().add(createScript);
        createScript.setDisable(true);
        flowPane.getChildren().add(hideToTray);
        hideToTray.setDisable(true);
        flowPane.getChildren().add(settings);
        settings.setDisable(true);

        primaryStage.setScene(new Scene(flowPane));
        ThreadsPool threadsPool = new ThreadsPool();
        encryptNow.setOnAction(event -> {
            CipherType cipherType = Main.parseCipherType(algorithm.getValue());
            String keyValue = key.getText();
            String initVectorValue = initVector.getText();
            Cipher cipher = CipherFactory.factory(cipherType, keyValue, initVectorValue);
            cipher.setCipherMode(CipherMode.ENCRYPTION);
            threadsPool.execute(paths, cipher);
            threadsPool.shutdown();
        });
        decryptNow.setOnAction(event -> {
            CipherType cipherType = Main.parseCipherType(algorithm.getValue());
            String keyValue = key.getText();
            String initVectorValue = initVector.getText();
            Cipher cipher = CipherFactory.factory(cipherType, keyValue, initVectorValue);
            cipher.setCipherMode(CipherMode.DECRYPTION);
            threadsPool.execute(paths, cipher);
            threadsPool.shutdown();
        });
        primaryStage.show();
    }

    private static List<String> getPathsFromFile(File file) {
        return FileProcessor.getFilesPathsFromFile(file.getAbsolutePath());
    }

    private static String getFilesWithSizes(List<String> paths) {
        StringBuilder builder = new StringBuilder();
        int totalSize = 0;
        for (String s : paths) {
            File file1 = new File(s);
            builder.append(s).append(" ").append(((int) file1.length()) / 1024).append(" kb.\n");
            totalSize += ((int) file1.length()) / 1024;
        }
        builder.append("\n Total size: ").append(totalSize).append(" kb.");
        return builder.toString();
    }
}
