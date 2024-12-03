package com.example.inzynierka.kontrolery;

import com.example.inzynierka.MainApplication;
import com.example.inzynierka.klasy.Drzewo;
import com.example.inzynierka.klasy.Json.GrafWczytajJson;
import com.example.inzynierka.klasy.Krawedz;
import com.example.inzynierka.klasy.Wierzcholek;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.stream.ProxyPipe;
import org.graphstream.ui.fx_viewer.FxViewer;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerListener;
import org.graphstream.ui.view.ViewerPipe;

import java.util.List;
import javax.imageio.ImageIO;
import javax.xml.bind.JAXBException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.prefs.Preferences;

public class DrzewoController {
    @FXML
    private BorderPane borderPaneGlowny;

    @FXML
    private VBox checkBoxMenu;
    @FXML
    private Button zapiszPng;
    @FXML
    private Button zapiszJpg;
    @FXML
    private VBox tableMenu;
    @FXML
    private Button pokazTabele;
    @FXML
    private Button pokazCheckBoxy;
    @FXML
    private TableView<Wierzcholek> tableView; // Zmieniamy typ na Wierzcholek
    @FXML
    private TableColumn<Wierzcholek, String> nodeColumn;
    @FXML
    private TableColumn<Wierzcholek, String> parentColumn;
    @FXML
    private TableColumn<Wierzcholek, String> valueColumn;

    @FXML private CheckBox checkBoxName;
    @FXML private CheckBox checkBoxClass;
    @FXML private CheckBox checkBoxAttempts;
    @FXML private CheckBox checkBoxCondition;

    private Wierzcholek poprzednioWybranyWierzcholek = null;
    private String shapeWezlow, shapeLisci,liczbaPx,kolorWezlow,kolorLisci,obramowaniePx,kolorObramowania;
    public void initialize() throws IOException, JAXBException {
        Drzewo drzewo = new Drzewo();
        readSetting();
        System.out.println("TAKI SHAPE: "+ shapeWezlow);

//        GrafWczytajTxt grafWczytajTxt = new GrafWczytajTxt();
//        grafWczytajTxt.loadGraph("/Dane/drzewo_decyzyjne_1.txt", drzewo);
        GrafWczytajJson grafWczytajJson = new GrafWczytajJson();
        grafWczytajJson.loadGraph("/Dane/drzewo_decyzyjne_json.json",drzewo);
//        GrafWczytajXml grafWczytajXml = new GrafWczytajXml();
//        grafWczytajXml.loadGraph("/Dane/decisionTree.xml",drzewo);
        drzewo.getGraf().setAttribute("ui.stylesheet"," node { shape: " + shapeWezlow + ";text-alignment:center; text-offset: 4px, -4px; size-mode: fit; size: 100px; text-color: white; fill-color: "+kolorWezlow+"; }");
//        drzewo.getGraf().setAttribute("ui.stylesheet", "node { fill-color: red; shape: box; size:40px; text-size: 6px; text-alignment:center; }" +
//                "edge { text-alignment:under; text-background-mode: plain; text-size: 12px; }");
        ustawLiscie(drzewo);
        System.out.println(drzewo.getGraf().getNodeCount());
        String tekst = "";
        zmienWyswietlenie(drzewo);
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
        // Dodajemy listener kliknięcia wiersza tabeli
        tableView.setRowFactory(tv -> {
            TableRow<Wierzcholek> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    Wierzcholek wybranyWierzcholek = row.getItem();
                    String nodeId = wybranyWierzcholek.getId();
                    double wierzcholekColumnStart = nodeColumn.getWidth();
                    double rodzicColumnStart = parentColumn.getWidth() + wierzcholekColumnStart;
                    double wartoscColumnStart = valueColumn.getWidth() + rodzicColumnStart;

                    if (event.getX() < wierzcholekColumnStart) {
                        zmienWierzcholek2(drzewo, nodeId);
                    }  else if (event.getX() >= rodzicColumnStart && event.getX() < wartoscColumnStart) {
                        if(wybranyWierzcholek.getRodzicId()!=null)
                            zmienWartoscKrawedzi(drzewo,wybranyWierzcholek);


                    }
                }
                if(event.getClickCount() == 1 &&!row.isEmpty() )
                {
                    Wierzcholek wybranyWierzcholek = row.getItem();
                    String nodeId = wybranyWierzcholek.getId();
                    zmienKolorWybranychElementow(drzewo,wybranyWierzcholek);
                    poprzednioWybranyWierzcholek=wybranyWierzcholek;
                }
            });
            return row;
        });





        for (int i = 0; i < drzewo.getGraf().getNodeCount(); i++) {
            tekst = tekst + drzewo.getGraf().getNode(i).getId() + " ";
        }
        drzewo.getGraf().getNode(0).setAttribute("xy", 0, 0);

        Viewer viewer = new FxViewer(drzewo.getGraf(), Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
        Parent graphView = (Parent) viewer.addDefaultView(true);

        // Przetwarzanie kliknięć węzłów
        ViewerPipe pipe = viewer.newViewerPipe();
        ProxyPipe pipe2 = viewer.newViewerPipe();
        pipe2.addAttributeSink(drzewo.getGraf());
        pipe.addViewerListener(new ViewerListener() {
            @Override
            public void viewClosed(String viewName) {
                // Możesz dodać kod do działania po zamknięciu widoku
            }

            @Override
            public void buttonPushed(String id) {
                Platform.runLater(() -> {

                    if (drzewo.getGraf().getEdge(id) != null) {
                        // Jeśli istnieje krawędź o danym ID
                        System.out.println("Kliknięto krawędź: " + id);
                    } else if (drzewo.getGraf().getNode(id) != null) {
                        // Jeśli istnieje węzeł o danym ID
                        System.out.println("Kliknięto węzeł: " + id);


                        zmienWierzcholek2(drzewo, id);
                    }
                });
            }

            @Override
            public void buttonReleased(String nodeId) {

            }

            @Override
            public void mouseOver(String nodeId) {

            }

            @Override
            public void mouseLeft(String nodeId) {

            }
        });

        // Wątek do przetwarzania zdarzeń
        new Thread(() -> {
            while (true) {
                pipe.pump();
                pipe2.pump();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

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
        pokazCheckBoxy.setOnAction(event -> {
            checkBoxMenu.setVisible(!checkBoxMenu.isVisible());
            tableMenu.setVisible(false);
            System.out.println("klikam");
        });
        pokazTabele.setOnAction(event -> {
            tableMenu.setVisible(!tableMenu.isVisible());
            checkBoxMenu.setVisible(false);
        });
        checkBoxName.setOnAction(e -> zmienWyswietlenie(drzewo));
        checkBoxAttempts.setOnAction(e -> zmienWyswietlenie(drzewo));
        checkBoxClass.setOnAction(e -> zmienWyswietlenie(drzewo));
        checkBoxCondition.setOnAction(e -> zmienWyswietlenie(drzewo));

    }
    // Metoda do usuwania kropek z końca tekstu
    private void zmienWierzcholek2(Drzewo drzewo, String nodeId) {
        Node originalNode = drzewo.getGraf().getNode(nodeId);
        Wierzcholek wierzcholek = null;
        for(Wierzcholek wierzcholek2 : drzewo.getListaWierzcholkow())
        {
            if (wierzcholek2.getId().equals(nodeId))
            {
                wierzcholek=wierzcholek2;
            }
        }
        if (originalNode != null) {
            Dialog<Void> dialog = new Dialog<>();
            dialog.setTitle("Zmiana danych węzła");
            dialog.setHeaderText("Zmiana danych węzła");
            
            VBox vbox = new VBox();
            vbox.setSpacing(10);
            
            TextField field1 = new TextField();
            field1.setPromptText("Wprowadź nową nazwę węzła");
            field1.setText(nodeId);  

            TextField field2 = new TextField();
            String[] parts = wierzcholek.getFullLabel().split(" (?=\\S)");
            field2.setText(parts[1]+" "+parts[2]);
            if(wierzcholek.getDzieciId().size()>0)
            {
                vbox.getChildren().addAll(new Label("Nazwa węzła:"), field1, new Label("Podaj warunek: "), field2);
            }
            else
            {
                vbox.getChildren().addAll(new Label("Nazwa węzła:"), field1, new Label("Podaj klasę i liczbę prób: "), field2);
            }



            dialog.getDialogPane().setContent(vbox);

            ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

            Wierzcholek finalWierzcholek = wierzcholek;
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == okButtonType) {
                    String newId = field1.getText().trim();
                    String newLabel = field2.getText().trim();
                    // Sprawdzamy czy nowy ID nie jest pusty i czy nie istnieje już w grafie
                    if(finalWierzcholek.getId().equals(newId))
                    {
                        finalWierzcholek.setFullLabel(usunKropki(finalWierzcholek.getId())+" "+field2.getText().trim());

                    }
                    else
                    {
                        if (!newId.isEmpty() && drzewo.getGraf().getNode(newId) == null) {

                            // Zmieniamy nazwę węzła
                            System.out.println("Zmieniono ID węzła na: " + newId);

                            // Utwórz nowy węzeł z nowym ID
                            Node newNode = drzewo.getGraf().addNode(newId);
                            Wierzcholek nowyWierzcholek = new Wierzcholek(newId, newId,newId +" " +field2.getText().trim());

                            // Skopiuj atrybuty ze starego węzła
                            originalNode.attributeKeys().forEach(key -> {
                                if (!"ui.label".equals(key)) {
                                    Object value = originalNode.getAttribute(key);
                                    newNode.setAttribute(key, value);
                                }
                            });
                            newNode.setAttribute("ui.label", newId);

                            // Przenieś krawędzie
                            originalNode.edges().forEach(edge -> {
                                if (Objects.equals(edge.getSourceNode(), originalNode)) {
                                    // Krawędź wychodząca
                                    Krawedz krawedz = new Krawedz(newId + edge.getTargetNode().getId(), newId,
                                            edge.getTargetNode().getId(), edge.getAttribute("ui.label").toString());
                                    drzewo.dodajKrawedz(krawedz);
                                } else {
                                    // Krawędź przychodząca
                                    String sourceId = edge.getSourceNode().getId();
                                    Krawedz krawedz = new Krawedz(sourceId + newId, sourceId, newId, edge.getAttribute("ui.label").toString());
                                    drzewo.dodajKrawedz(krawedz);
                                }
                            });

                            drzewo.edytujWierzcholek(nowyWierzcholek, nodeId);

                            // Usuwamy oryginalny węzeł
                            drzewo.getGraf().removeNode(nodeId);
                            tableView.refresh();
                        } else {
                            System.out.println("Węzeł o ID " + newId + " już istnieje lub ID jest puste.");
                        }
                    }
                    zmienWyswietlenie(drzewo);
                }
                return null;
            });

            // Wyświetlamy dialog
            dialog.showAndWait();
        } else {
            System.out.println("Nie znaleziono węzła o ID: " + nodeId);
        }
    }

    private void zmienWartoscKrawedzi(Drzewo drzewo, Wierzcholek wierzcholek)
    {
        TextInputDialog dialog = new TextInputDialog(wierzcholek.getWartosc());
        dialog.setTitle("Zmiana wartosci krawedzi");
        dialog.setHeaderText("Zmień wartosc krawedzi");
        dialog.setContentText("Wprowadź nową wartosc dla krawedzi:");

        // Wyświetlamy dialog i czekamy na wynik
        dialog.showAndWait().ifPresent(newValue -> {
            if (!newValue.trim().isEmpty()) {
                drzewo.getGraf().getEdge(wierzcholek.getRodzicId()+wierzcholek.getId()).setAttribute("ui.label",newValue);
                wierzcholek.setWartosc(newValue);
                tableView.refresh();
            }
        });
    }
    private void zmienKolorWybranychElementow(Drzewo drzewo,Wierzcholek wierzcholek)
    {
        if(poprzednioWybranyWierzcholek!=null)
        {
            drzewo.getGraf().getNode(poprzednioWybranyWierzcholek.getId()).setAttribute("ui.style","fill-color: red;");
            drzewo.getGraf().getEdge(poprzednioWybranyWierzcholek.getRodzicId()+poprzednioWybranyWierzcholek.getId()).setAttribute("ui.style","fill-color: black;");
        }
        drzewo.getGraf().getNode(wierzcholek.getId()).setAttribute("ui.style","fill-color: green;");
        Edge edge = drzewo.getGraf().getEdge(wierzcholek.getRodzicId()+wierzcholek.getId());
        if(edge!=null)
        {
            edge.setAttribute("ui.style","fill-color: green;");
        }
    }
    private String usunKropki(String nazwa) {
        while (nazwa.endsWith(".")) {
            nazwa = nazwa.substring(0, nazwa.length() - 1);
        }
        return nazwa;
    }
    private void zmienWyswietlenie(Drzewo drzewo)
    {
        Graph graph = drzewo.getGraf();
        List<Wierzcholek> listaWierzcholkow  = drzewo.getListaWierzcholkow();
        for(Node node : drzewo.getGraf())
        {
            String nazwa ="";
            if(checkBoxName.isSelected())
            {
                for(Wierzcholek wierzcholek: listaWierzcholkow)
                {
                    if(wierzcholek.getId().equals(node.getId()))
                    {
                        String aktualnaNazwa = wierzcholek.getFullLabel();
                        String[] parts = aktualnaNazwa.split(" (?=\\S)");
                        nazwa = parts[0]+" ";
                        break;
                    }
                }
            }
            if(checkBoxClass.isSelected())
            {
                for(Wierzcholek wierzcholek: listaWierzcholkow)
                {
                    if(wierzcholek.getDzieciId().size()==0)
                    {
                        if(wierzcholek.getId().equals(node.getId()))
                        {
                            String aktualnaNazwa = wierzcholek.getFullLabel();
                            String[] parts = aktualnaNazwa.split(" (?=\\S)");
                            nazwa += parts[1]+" ";
                            break;
                        }
                    }
                }
            }
            if(checkBoxCondition.isSelected())
            {
                for(Wierzcholek wierzcholek: listaWierzcholkow)
                {
                    if(wierzcholek.getDzieciId().size()>0)
                    {
                        if(wierzcholek.getId().equals(node.getId()))
                        {
                            String aktualnaNazwa = wierzcholek.getFullLabel();
                            String[] parts = aktualnaNazwa.split(" (?=\\S)");
                            nazwa += parts[1]+" "+parts[2];
                            break;
                        }
                    }
                }
            }
            if(checkBoxAttempts.isSelected())
            {
                for(Wierzcholek wierzcholek: listaWierzcholkow)
                {
                    if(wierzcholek.getDzieciId().size()==0)
                    {
                        if(wierzcholek.getId().equals(node.getId()))
                        {
                            String aktualnaNazwa = wierzcholek.getFullLabel();
                            String[] parts = aktualnaNazwa.split(" (?=\\S)");
                            nazwa += parts[2];
                            drzewo.getGraf().getNode(wierzcholek.getId()).setAttribute("ui.style","padding: 20px;" );
                            break;
                        }
                    }
                }
            }
            node.setAttribute("ui.label",nazwa);
        }
    }
    private void readSetting() {
        Preferences prefs = Preferences.userNodeForPackage(UstawieniaController.class);
        shapeWezlow = prefs.get("shapeWezlow", "box");
        shapeLisci = prefs.get("shapeLisci", "box");
        kolorWezlow = prefs.get("kolorWezlow", "black");
        kolorLisci =  prefs.get("kolorLisci", "black");
        obramowaniePx = prefs.get("obramowaniePx", "1px");
        kolorObramowania = prefs.get("obramowaniePx", "black");
    }
    private void ustawLiscie(Drzewo drzewo){
        Graph graf = drzewo.getGraf();
        for(Wierzcholek wierzcholek: drzewo.getListaWierzcholkow())
        {
            if(wierzcholek.getDzieciId().size()==0)
            {
                System.out.println("Ustawiam");
                drzewo.getGraf().getNode(wierzcholek.getId()).setAttribute("ui.style","shape: "+shapeLisci+"; padding: 10px; fill-color: " +kolorLisci+";" );
            }
        }
    }

    @FXML
    protected void Powrot() throws IOException {
        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("menu-view.fxml"));
        Parent root = loader.load();
        Scene currentScene = borderPaneGlowny.getScene();
        Stage stage = (Stage) currentScene.getWindow();
        // root.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        //WynikiModel.getInstance().getWyniki().add(new Wynik("GraczXD", savedDifficulty, 100));
        stage.setScene(new Scene(root, currentScene.getWidth(), currentScene.getHeight()));
    }

}