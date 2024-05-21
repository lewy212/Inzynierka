package com.example.inzynierka.kontrolery;

import com.example.inzynierka.klasy.Drzewo;
import com.example.inzynierka.klasy.GrafWczytajTxt;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.graphstream.ui.fx_viewer.FxViewer;
import org.graphstream.ui.view.Viewer;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class DrzewoController {
    @FXML
    private BorderPane borderPaneGlowny;
    @FXML
    private Button zapiszPng;

    public void initialize()
    {
        Drzewo drzewo = new Drzewo();
        GrafWczytajTxt grafWczytajTxt = new GrafWczytajTxt();
        grafWczytajTxt.loadGraph("/Dane/drzewo_decyzyjne_1.txt", drzewo);

        drzewo.getGraf().setAttribute("ui.stylesheet", "node { fill-color: red; size:25px; text-size: 12px; text-alignment:center; }" +
                "edge { text-alignment:under; text-background-mode: plain; text-size: 12px; }");

        System.out.println(drzewo.getGraf().getNodeCount());
        String tekst = "";

        for (int i = 0; i < drzewo.getGraf().getNodeCount(); i++) {
            tekst = tekst + drzewo.getGraf().getNode(i).getId() + " ";
        }
        drzewo.getGraf().getNode(0).setAttribute("xy", 0, 0);
        System.out.println(tekst);

        Viewer viewer = new FxViewer(drzewo.getGraf(), Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
        Parent graphView = (Parent) viewer.addDefaultView(true);

        borderPaneGlowny.setCenter(graphView);
        zapiszPng.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Zapisz zdjecie");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG Files", "*.png"));
            Stage stage = (Stage) borderPaneGlowny.getScene().getWindow();
            File file = fileChooser.showSaveDialog(stage);
            if (file != null) {
                try {
                    WritableImage snapshot = borderPaneGlowny.snapshot(null, null);
                    ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", file);
                    System.out.println("Zdjecie zapisane w " + file.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });
    }

}