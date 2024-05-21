package com.example.inzynierka.kontrolery;

import com.example.inzynierka.klasy.Drzewo;
import com.example.inzynierka.klasy.GrafWczytajTxt;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import org.graphstream.ui.fx_viewer.FxViewer;
import org.graphstream.ui.view.Viewer;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController  implements Initializable {
    @FXML
    private Pane graphPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize the graph
        Drzewo drzewo = new Drzewo();
        GrafWczytajTxt grafWczytajTxt = new GrafWczytajTxt();
        grafWczytajTxt.loadGraph("/Dane/drzewo_decyzyjne_1.txt", drzewo);

        drzewo.getGraf().setAttribute("ui.stylesheet", "node { fill-color: red; size:25px; text-size: 12px; text-alignment:center; }" +
                "edge { text-alignment:under; text-background-mode: plain; text-size: 12px; }");
        drzewo.getGraf().getNode(0).setAttribute("xy",0,0);

        // Initialize the viewer
        Viewer viewer = new FxViewer(drzewo.getGraf(), Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
        viewer.disableAutoLayout();
        graphPane.getChildren().add((javafx.scene.Node) viewer.addDefaultView(false));
        // Get the JavaFX view and add it to the Pane


    }
}