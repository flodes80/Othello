package gui.controllers;

import gamestuff.Game;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController{

    private Stage mainStage, gameStage;
    private SplashScreenController splashScreenController;
    private MenuPrincipalController menuPrincipalController;
    private SousMenuController sousMenuController;
    private GameController gameController;
    private final String title = "Othello";
    private Game game;

    public MainController(Stage stage){
        this.mainStage = stage;
        //Lancement et initialisation du mainStage (Menu)
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/interfaces/SplashScreen.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        splashScreenController = loader.getController();
        Scene scene = new Scene(root, 1270, 710);
        mainStage.setScene(scene);
        mainStage.setTitle(title);
        mainStage.getIcons().add(new Image("img/icon.png"));
        mainStage.setResizable(false);
        mainStage.show();
        menuPrincipalController = splashScreenController.getMenuPrincipalController();
        menuPrincipalController.setMainController(this);
    }

    public void launchGame(Game game){
        this.game = game;
        mainStage.hide();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/interfaces/GameInterface.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        gameController = fxmlLoader.getController();
        gameStage = new Stage();
        gameStage.setResizable(false);
        Scene gameScene = new Scene(root, 1270, 737, true, SceneAntialiasing.BALANCED);
        gameStage.setScene(gameScene);
        gameStage.setTitle(title);
        gameStage.getIcons().add(new Image("img/icon.png"));

        //Afficher le mainStage lorsque l'on quitte une partie
        gameStage.setOnCloseRequest(event -> mainStage.show());
        gameStage.show();
        gameController.setMainController(this);
        game.setGameController(gameController);
    }

    public Game getGame() {
        return game;
    }

    public Stage getGameStage() {
        return gameStage;
    }

    public void setSousMenuController(SousMenuController sousMenuController) {
        sousMenuController.setMainController(this);
        this.sousMenuController = sousMenuController;
    }

}
