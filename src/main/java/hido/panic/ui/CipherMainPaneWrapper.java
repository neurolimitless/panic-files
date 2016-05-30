package hido.panic.ui;

import hido.panic.cipher.Cipher;
import hido.panic.cipher.CipherFactory;
import hido.panic.cipher.CipherMode;
import hido.panic.cipher.CipherType;
import hido.panic.file.FileProcessor;
import hido.panic.file.ThreadsPool;
import hido.panic.ui.console.UiConsole;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.List;


public class CipherMainPaneWrapper {

    private static final Logger log = LogManager.getLogger("Cipher logger");

    private ThreadsPool threadsPool;
    private List<String> paths;

    private FlowPane flowPane;

    private TextField keyField;
    private TextField initVectorField;
    private TextArea filesTextArea;
    private ComboBox<String> algorithmsBox;

    private Button encryptNowButton;
    private Button decryptNowButton;
    private Button settingsButton;
    private Button createScriptButton;
    private Button hideToTrayButton;
    private Button loadListButton;
    private Button clearLogAreaButton;


    public CipherMainPaneWrapper() {
        initNodes();
        initMainPane();
    }

    private void initNodes() {
        keyField = new TextField("Key");
        initVectorField = new TextField("Init Vector");

        filesTextArea = new TextArea();
        filesTextArea.setEditable(false);


        algorithmsBox = new ComboBox<>();
        algorithmsBox.setValue("AES_CFB");
        algorithmsBox.getItems().add("AES_CFB");

        threadsPool = new ThreadsPool();

        encryptNowButton = new Button("Encrypt now");
        encryptNowButton.setOnAction(event -> {
            launchCipher(CipherMode.ENCRYPTION);
        });

        decryptNowButton = new Button("Decrypt now");
        decryptNowButton.setOnAction(event -> {
            launchCipher(CipherMode.DECRYPTION);
        });

        settingsButton = new Button("Settings");
        settingsButton.setDisable(true);

        createScriptButton = new Button("Create script");
        createScriptButton.setDisable(true);

        hideToTrayButton = new Button("Hide to tray");
        hideToTrayButton.setDisable(true);

        clearLogAreaButton = new Button("Clear");
        clearLogAreaButton.setOnAction(event -> {
            UiConsole.clearConsole();
        });

        loadListButton = new Button("Load list");
        loadListButton.setOnAction(event -> {
            File list = buildFileChooser().showOpenDialog(null);
            if (list != null) {
                paths = getPathsFromFile(list);
                filesTextArea.setText(getFilesWithSizes(paths));
            }
        });
    }

    private void initMainPane() {
        flowPane = new FlowPane();

        flowPane.getChildren().add(keyField);
        flowPane.getChildren().add(initVectorField);

        flowPane.getChildren().add(loadListButton);

        flowPane.getChildren().add(algorithmsBox);
        flowPane.getChildren().add(filesTextArea);
        flowPane.getChildren().add(encryptNowButton);
        flowPane.getChildren().add(decryptNowButton);

        flowPane.getChildren().add(createScriptButton);
        flowPane.getChildren().add(hideToTrayButton);
        flowPane.getChildren().add(clearLogAreaButton);
        flowPane.getChildren().add(settingsButton);
        flowPane.getChildren().add(UiConsole.getConsole());

    }

    private Cipher createCipher(String keyValue, String initVectorValue, String cipherTypeValue, CipherMode cipherMode) {
        CipherType cipherType = CipherType.parseCipherType(cipherTypeValue);
        Cipher cipher = CipherFactory.factory(cipherType, keyValue, initVectorValue);
        cipher.setCipherMode(cipherMode);
        log.info("Cipher created: " + keyValue + ":" + initVectorValue + " > " + cipherTypeValue + " in " + cipherMode);
        return cipher;
    }

    private void launchCipher(CipherMode cipherMode) {
        Cipher cipher = createCipher(
                keyField.getText(),
                initVectorField.getText(),
                algorithmsBox.getValue(),
                cipherMode
        );
        threadsPool.execute(paths, cipher);
    }


    private FileChooser buildFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select list");
        fileChooser.setInitialDirectory(new File("/"));
        return fileChooser;
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

    public FlowPane getPane() {
        return flowPane;
    }
}
