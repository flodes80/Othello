package gui.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController{

    private static Stage mainStage, gameStage;
    private static boolean mainStageInitialized, gameStageInitialized;
    private final static String title = "Othello";

    private static void initializeMainStage(Stage stage){
        mainStage = stage;
        FXMLLoader loader = new FXMLLoader(MainController.class.getClass().getResource("/gui/interfaces/SplashScreen.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root);
        mainStage.setScene(scene);
        mainStage.setTitle(title);
        mainStageInitialized = true;
    }

    private static void initializeGameStage(){
        FXMLLoader fxmlLoader = new FXMLLoader(MainController.class.getResource("/gui/interfaces/GameInterface.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        gameStage = new Stage();
        Scene gameScene = new Scene(root);
        gameStage.setScene(gameScene);
        gameStage.setTitle(title);
        //Afficher le mainStage lorsque l'on quitte une partie
        gameStage.setOnCloseRequest(event -> {
            launchMainStage(null);
        });
        gameStageInitialized = true;
    }

    public static void launchMainStage(Stage stage){
        //Initialisation des deux stages (main + game)
        if(!mainStageInitialized)
            initializeMainStage(stage);
        if(!gameStageInitialized)
            initializeGameStage();

        //Lancement mainStage
        if(gameStage.isShowing())
            gameStage.hide();
        mainStage.show();
    }

    public static void launchGameStage(){
        mainStage.hide();
        gameStage.show();
    }
}
