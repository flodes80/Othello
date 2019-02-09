package gamestuff;

import gui.controllers.MainController;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception{
        MainController.launchMainStage(stage);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
