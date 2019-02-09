package gui.misc;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.util.Duration;

/** Permet l'animation en fondu du splashScreen **/

public class AnimationGenerator {

    public void applyTranslateAnimationOn(Node node, int duration, double from, double to) {
        TranslateTransition translateTransition
                = new TranslateTransition(Duration.millis(duration), node);
        translateTransition.setFromX(from);
        translateTransition.setToX(to);
        translateTransition.setCycleCount(1);
        translateTransition.setAutoReverse(true);
        translateTransition.play();
    }

    public void applyFadeAnimationOn01(Node node, int duration, double from, double to,int cc, EventHandler<ActionEvent> eventHandler) {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(duration), node);
        fadeTransition.setOnFinished(eventHandler);
        fadeTransition.setFromValue(from);
        fadeTransition.setToValue(to);
        fadeTransition.setAutoReverse(true);
        fadeTransition.setCycleCount(cc);
        fadeTransition.play();
    }
    public void applyFadeAnimationOn02(Node node, int duration, double from, double to,int cc, EventHandler<ActionEvent> eventHandler) {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(duration), node);
        fadeTransition.setOnFinished(eventHandler);
        fadeTransition.setFromValue(from);
        fadeTransition.setToValue(to);
        fadeTransition.setAutoReverse(true);
        fadeTransition.setCycleCount(cc);
        fadeTransition.play();
    }

}
