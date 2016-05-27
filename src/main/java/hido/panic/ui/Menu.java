package hido.panic.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Menu extends Application {

    private static final int HEIGHT = 500;
    private static final int WIDTH = 500;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("PanicFiles v0.1");
        primaryStage.setHeight(HEIGHT);
        primaryStage.setWidth(WIDTH);


        primaryStage.setScene(new Scene(new CipherMainPaneWrapper().getPane()));
        primaryStage.setOnCloseRequest(event ->{
//            threadsPool.shutdown();
            System.exit(0);
        });
        try {
            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/panic.png")));
        } catch (Exception e) {
            System.out.println("Can't load the icon.");
        }
        primaryStage.show();
    }


}
