package hido.panic.ui;

import javafx.application.Application;
import javafx.stage.Stage;

public class Menu extends Application{

    private static final int HEIGHT = 500;
    private static final int WIDTH = 500;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("PanicFiles v0.1");
        primaryStage.setHeight(HEIGHT);
        primaryStage.setWidth(WIDTH);

        primaryStage.show();
    }
}
