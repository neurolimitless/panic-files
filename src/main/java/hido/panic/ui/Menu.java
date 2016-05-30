package hido.panic.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Menu extends Application {

    private static final Logger log = LogManager.getLogger("UI logger");

    private static final int HEIGHT = 500;
    private static final int WIDTH = 500;

    @Override
    public void start(Stage primaryStage) throws Exception {

        log.info("Application started.");
        primaryStage.setTitle("PanicFiles v0.1d");
        primaryStage.setHeight(HEIGHT);
        primaryStage.setWidth(WIDTH);

        primaryStage.setScene(new Scene(new CipherMainPaneWrapper().getPane()));
        primaryStage.setOnCloseRequest(event -> {
            log.info("Application closed.");
//            threadsPool.shutdown();
            System.exit(0);
        });
        try {
            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/panic.png")));
        } catch (Exception e) {
            log.warn("Can't load the icon");
        }
        primaryStage.show();
    }


}
