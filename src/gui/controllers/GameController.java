package gui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    @FXML
    Label labelJoueur1;

    public void setLabelJoueur1(String labelJoueur1) {
        this.labelJoueur1.setText(labelJoueur1);
    }

    public void setJoueur1(String joueur1) {
        this.joueur1 = joueur1;
    }

    public void setJoueur2(String joueur2) {
        this.joueur2 = joueur2;
    }

    String joueur1, joueur2;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        System.out.println("joueur1" + joueur1);
        System.out.println("avant" +labelJoueur1.getText());
        labelJoueur1.setText(joueur1);
        System.out.println("apres" + labelJoueur1.getText());
    }
}