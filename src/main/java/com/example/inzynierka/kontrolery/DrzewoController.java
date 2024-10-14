package com.example.inzynierka.kontrolery;

import com.example.inzynierka.klasy.Drzewo;
import com.example.inzynierka.klasy.GrafWczytajTxt;
import com.example.inzynierka.klasy.Wierzcholek;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.graphstream.ui.fx_viewer.FxViewer;
import org.graphstream.ui.view.Viewer;
import java.util.List;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class DrzewoController {
    @FXML
    private BorderPane borderPaneGlowny;
    @FXML
    private Button zapiszPng;
    @FXML
    private Button zapiszJpg;

    @FXML
    private Button pokazTabele;

    @FXML
    private TableView<Wierzcholek> tableView; // Zmieniamy typ na Wierzcholek
    @FXML
    private TableColumn<Wierzcholek, String> nodeColumn;
    @FXML
    private TableColumn<Wierzcholek, String> parentColumn;
    @FXML
    private TableColumn<Wierzcholek, String> valueColumn;

    public void initialize()
    {
        Drzewo drzewo = new Drzewo();
        GrafWczytajTxt grafWczytajTxt = new GrafWczytajTxt();
        grafWczytajTxt.loadGraph("/Dane/drzewo_decyzyjne_1.txt", drzewo);

        drzewo.getGraf().setAttribute("ui.stylesheet", "node { fill-color: red; size:25px; text-size: 12px; text-alignment:center; }" +
                "edge { text-alignment:under; text-background-mode: plain; text-size: 12px; }");

        System.out.println(drzewo.getGraf().getNodeCount());
        String tekst = "";
        // Ustawienia kolumn tabeli

        nodeColumn.setCellValueFactory(new PropertyValueFactory<>("label")); // Zmieniamy na label wierzchołka
        parentColumn.setCellValueFactory(new PropertyValueFactory<>("rodzicId")); // Zmieniamy na rodzica
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("wartosc")); // Zmieniamy na wartość wierzchołka (jeśli to jest atrybut)

        parentColumn.setCellFactory(new Callback<TableColumn<Wierzcholek, String>, TableCell<Wierzcholek, String>>() {
            @Override
            public TableCell<Wierzcholek, String> call(TableColumn<Wierzcholek, String> param) {
                return new TableCell<Wierzcholek, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText(usunKropki(item)); // Użycie metody do usunięcia kropek
                        }
                    }
                };
            }
        });

        ObservableList<Wierzcholek> daneDrzewa = FXCollections.observableArrayList();
        List<Wierzcholek> listaWierzcholkow = drzewo.getListaWierzcholkow();
        for (Wierzcholek wierzcholek : listaWierzcholkow) {
            daneDrzewa.add(wierzcholek); // Dodajemy wierzchołki do listy
            System.out.println(wierzcholek.getLabel()+" TO JEST LABEL");
        }

        tableView.setItems(daneDrzewa); // Ustawiamy dane w tabeli
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
                    WritableImage snapshot = borderPaneGlowny.getCenter().snapshot(null, null);
                    ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", file);
                    System.out.println("Zdjecie zapisane w " + file.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        zapiszJpg.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Zapisz zdjęcie");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPG Files", "*.jpg"));
            Stage stage = (Stage) borderPaneGlowny.getScene().getWindow();
            File file = fileChooser.showSaveDialog(stage);
            if (file != null) {
                try {
                    Image image= borderPaneGlowny.getCenter().snapshot(null, null);
                    BufferedImage bufImageARGB = SwingFXUtils.fromFXImage(image, null);
                    BufferedImage bufImageRGB = new BufferedImage(bufImageARGB.getWidth(), bufImageARGB.getHeight(), BufferedImage.OPAQUE);

                    Graphics2D graphics = bufImageRGB.createGraphics();
                    graphics.drawImage(bufImageARGB, 0, 0, null);
                    ImageIO.write(bufImageRGB, "jpg", file);
                    System.out.println("Zdjęcie zapisane w " + file.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        pokazTabele.setOnAction(event -> {
            tableView.setVisible(!tableView.isVisible());
        });

    }
    // Metoda do usuwania kropek z końca tekstu
    private String usunKropki(String nazwa) {
        while (nazwa.endsWith(".")) {
            nazwa = nazwa.substring(0, nazwa.length() - 1);
        }
        return nazwa;
    }
}