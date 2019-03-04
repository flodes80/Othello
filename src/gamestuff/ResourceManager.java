package gamestuff;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ResourceManager {


    public static void save(SaveData data, String fileName) throws Exception {
        Element racine = new Element("Othello");

        //On crée un nouveau Document JDOM basé sur la racine que l'on vient de créer
        Document document = new Document(racine);

        Element joueurs = new Element("joueurs");
        racine.addContent(joueurs);

        Attribute joueur_1 = new Attribute("joueur_1", data.getPlayer1());
        joueurs.setAttribute(joueur_1);

        Attribute joueur_2 = new Attribute("joueur_2", data.getPlayer2());
        joueurs.setAttribute(joueur_2);

        Element currentPlayer = new Element("currentPlayer");
        currentPlayer.setText(data.getCurrentPlayer());
        joueurs.addContent(currentPlayer);

        Element isAI = new Element("isAI");
        isAI.setText(String.valueOf(data.isAi()));
        racine.addContent(isAI);

        Element tableau = new Element("tableau");
        racine.addContent(tableau);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Element node = new Element("node" + String.valueOf(i) + String.valueOf(j));
                node.addContent(String.valueOf(data.getBoard()[i][j]));
                tableau.addContent(node);
            }
        }

        //Les deux méthodes qui suivent seront définies plus loin dans l'article
        XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
        sortie.output(document, new FileOutputStream(fileName));
    }

    public static Object load(String fileName) throws Exception {

        Document document = null;
        Element racine;

        SAXBuilder sxb = new SAXBuilder();
        try {
            document = sxb.build(new File(fileName));
        } catch (Exception e) {
        }

        racine = document.getRootElement();

        List<Element> joueurs = racine.getChildren("joueurs");
        String joueur_1 = joueurs.get(0).getAttribute("joueur_1").getValue();
        String joueur_2 = joueurs.get(0).getAttribute("joueur_2").getValue();
        String currentPlayer = joueurs.get(0).getChildText("currentPlayer");
        List<Element> e_isAI = racine.getChildren("isAI");
        Boolean isAI = Boolean.valueOf(e_isAI.get(0).getContent().toString());

        byte[][] board = new byte[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                List<Element> tableau = racine.getChildren("tableau");
                String node = tableau.get(0).getChildText("node" + String.valueOf(i) + String.valueOf(j));
                board[i][j] =  Byte.parseByte(node);
            }
        }

        SaveData data = new SaveData(joueur_1, joueur_2, currentPlayer, board, isAI);
        return data;
    }
}
