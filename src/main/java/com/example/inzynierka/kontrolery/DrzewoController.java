package com.example.inzynierka.kontrolery;

import com.example.inzynierka.MainApplication;
import com.example.inzynierka.klasy.Drzewo;
import com.example.inzynierka.klasy.GrafWczytajTxt;
import com.example.inzynierka.klasy.Json.GrafWczytajJson;
import com.example.inzynierka.klasy.Krawedz;
import com.example.inzynierka.klasy.Wierzcholek;
import com.example.inzynierka.klasy.Xml.GrafWczytajXml;
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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
//import org.graphstream.algorithm.Toolkit;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.stream.ProxyPipe;
import org.graphstream.stream.file.FileSinkSVG;
import org.graphstream.ui.fx_viewer.FxViewPanel;
import org.graphstream.ui.fx_viewer.FxViewer;
import org.graphstream.ui.geom.Point2;
import org.graphstream.ui.geom.Point3;
import org.graphstream.ui.javafx.FxGraphRenderer;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerListener;
import org.graphstream.ui.view.ViewerPipe;
//import static org.graphstream.algorithm.Toolkit.*;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.xml.bind.JAXBException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;

import static com.example.inzynierka.klasy.ObliczajacaPozycje.nodePosition;

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
    private Button zapiszSvg;
    @FXML
    private Button usunZaznaczenie;
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
    private String shapeWezlow, shapeLisci,liczbaPx,kolorWezlow,kolorLisci,obramowaniePx,kolorObramowania,sizeMode,kolorTekstu,kolorWyboranegoElementu,format,sciezka;
    private int paddingWezlow=4, paddingLisci=4;
    boolean buttonClicked = false,wyszedlemZeZmiany = false;
    public void initialize() throws IOException, JAXBException {
        Drzewo drzewo = new Drzewo();
        readSetting();
        System.out.println("TAKI SHAPE: "+ shapeWezlow);
        wczytajZPliku(drzewo);

        System.out.println("Kolor Tekstu: "+kolorTekstu);
        drzewo.getGraf().setAttribute("ui.stylesheet"," node { shape: " + shapeWezlow + ";text-alignment:center; text-offset: 4px, 1px; size-mode: "+sizeMode+"; size: "+liczbaPx+"; text-color:"+kolorTekstu+"; fill-color: "+kolorWezlow+";" +
                "stroke-mode: plain; stroke-color:"+kolorObramowania+"; stroke-width:"+obramowaniePx+"; padding:"+paddingLisci+",3px;} graph {padding: 47px;}");
//        drzewo.getGraf().setAttribute("ui.stylesheet", "node { fill-color: red; shape: box; size:40px; text-size: 6px; text-alignment:center; }" +
//                "edge { text-alignment:under; text-background-mode: plain; text-size: 12px; }");
        ustawLiscie(drzewo,true);
        System.out.println(drzewo.getGraf().getNodeCount());
        String tekst = "";
        zmienWyswietlenie(drzewo);
        drzewo.poprawUstawienieWierzcholkow();
        nodeColumn.setCellValueFactory(new PropertyValueFactory<>("label"));
        parentColumn.setCellValueFactory(new PropertyValueFactory<>("rodzicId"));
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("wartosc"));
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
            System.out.println(wierzcholek.getId()+" x: "+wierzcholek.getPozX()+"   y: "+wierzcholek.getPozY());
        }
        System.out.println(drzewo.getWierzcholekByid("F9.").getStrona());
        tableView.setItems(daneDrzewa); // Ustawiamy dane w tabeli
        // Dodajemy listener kliknięcia wiersza tabeli
        tableView.setRowFactory(tv -> {
            TableRow<Wierzcholek> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    System.out.println("Kliknalem w taberli zeby edytowac");
                    Wierzcholek wybranyWierzcholek = row.getItem();
                    String nodeId = wybranyWierzcholek.getId();
                    double wierzcholekColumnStart = nodeColumn.getWidth();
                    double rodzicColumnStart = parentColumn.getWidth() + wierzcholekColumnStart;
                    double wartoscColumnStart = valueColumn.getWidth() + rodzicColumnStart;

                    if (event.getX() < wierzcholekColumnStart) {
                        buttonClicked=true;
                        zmienWierzcholek2(drzewo, nodeId,true);
                        wyszedlemZeZmiany=true;
                    }  else if (event.getX() >= rodzicColumnStart && event.getX() < wartoscColumnStart) {
                        if(wybranyWierzcholek.getRodzicId()!=null)
                            zmienWartoscKrawedzi(drzewo,wybranyWierzcholek);


                    }
                }
                if(event.getClickCount() == 1 &&!row.isEmpty() )
                {
                    Wierzcholek wybranyWierzcholek = row.getItem();
                    String nodeId = wybranyWierzcholek.getId();
                    zmienKolorWybranychElementow(drzewo,wybranyWierzcholek,false);
                    poprzednioWybranyWierzcholek=wybranyWierzcholek;
                }
            });
            return row;
        });

        for (int i = 0; i < drzewo.getGraf().getNodeCount(); i++) {
            tekst = tekst + drzewo.getGraf().getNode(i).getId() + " ";
        }


        Object obj = drzewo.getGraf().getNode(0).getAttribute("xy");
        if (obj instanceof double[]) {
            double[] position = (double[]) obj;
            System.out.println("Pozycja pierwszego elementu: x=" + position[0] + ", y=" + position[1]);
        } else {
            System.out.println("Brak pozycji dla węzła lub błąd w typie atrybutu.");
        }
        FxViewer view = new FxViewer(drzewo.getGraf(), FxViewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        FxViewPanel panel = (FxViewPanel) view.addView(FxViewer.DEFAULT_VIEW_ID, new FxGraphRenderer());
        view.getDefaultView().enableMouseOptions();
        StackPane graphPane = new StackPane();
        graphPane.getChildren().addAll(panel);


        for (int i = 0; i < drzewo.getMaksymalnaGlebokosc(); i++) {
            int finalI = i;
            String wierzcholkiNaGlebokosci = drzewo.getListaWierzcholkow().stream()
                    .filter(w -> w.getGlebokosc() == finalI)   // Filtrujemy wierzchołki o głębokości równej i
                    .map(Wierzcholek::getId)             // Mapujemy do ich ID
                    .collect(Collectors.joining(", ")); // Łączymy ID w jeden string, rozdzielając przecinkami

            System.out.println("Wierzchołki na głębokości " + i + ": " + wierzcholkiNaGlebokosci);
        }




        ViewerPipe pipe = view.newViewerPipe();
        ProxyPipe pipe2 = view.newViewerPipe();
        pipe2.addAttributeSink(drzewo.getGraf());

        pipe.addViewerListener(new ViewerListener() {
            @Override
            public void viewClosed(String viewName) {

            }

            @Override
            public void buttonPushed(String id) {
                if (buttonClicked) return;
                buttonClicked = true;
                Platform.runLater(() -> {
                    if (drzewo.getGraf().getEdge(id) != null) {
                        System.out.println("Kliknięto krawędź: " + id);
                    } else if (drzewo.getGraf().getNode(id) != null) {

                        System.out.println("Kliknięto węzeł: " + id);


                        zmienWierzcholek2(drzewo, id,false);
                    }
                    wyszedlemZeZmiany=true;
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
                int time = 100;
                if(buttonClicked&&wyszedlemZeZmiany)
                {
                    wyszedlemZeZmiany=false;
                    buttonClicked=false;
                 //   time = 500;
                }
                try {
                    Thread.sleep(time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        borderPaneGlowny.setCenter(graphPane);
        zapiszPng.setOnAction(event -> {
            konwertujPNG();
        });

        zapiszJpg.setOnAction(event -> {
           konwertujJPG();
        });
        pokazCheckBoxy.setOnAction(event -> {
            checkBoxMenu.setVisible(!checkBoxMenu.isVisible());
            tableMenu.setVisible(false);

        });
        usunZaznaczenie.setOnAction(event -> {
            tableView.getSelectionModel().clearSelection();
            zmienKolorWybranychElementow(drzewo,null,true);
        });
        zapiszSvg.setOnAction(event -> {
            konwertujSVG(drzewo);
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

    private void zmienWierzcholek2(Drzewo drzewo, String nodeId,boolean tabela) {
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
            field1.setText(usunKropki(nodeId));
            TextField field2 = new TextField();
            String[] parts = wierzcholek.getFullLabel().split(" (?=\\S)");
            field2.setText(parts[1]+" "+parts[2]);

            TextField field0 = new TextField();
            field0.setText(nodeId);
            field0.setEditable(false);
            field0.setStyle("-fx-background-color: lightgray; ");
            if(wierzcholek.getDzieciId().size()>0)
            {
                vbox.getChildren().addAll(new Label("Prawdziwe id"),field0,new Label("Nazwa węzła:"), field1, new Label("Podaj warunek: "), field2);
            }
            else
            {
                vbox.getChildren().addAll(new Label("Prawdziwe id"),field0,new Label("Nazwa węzła:"), field1, new Label("Podaj klasę i liczbę prób: "), field2);
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
                    if(finalWierzcholek.getId().equals(newId)||newId.equals(usunKropki(finalWierzcholek.getId())))
                    {
                        finalWierzcholek.setFullLabel(usunKropki(finalWierzcholek.getId())+" "+field2.getText().trim());

                    }
                    else
                    {
                        if (!newId.isEmpty()) {
                            if(drzewo.getGraf().getNode(newId)!=null)
                            {
                                newId= newId + ".".repeat(drzewo.obliczIleJuzJestTakichWierzcholkow(newId));
                            }
                            // Zmieniamy nazwę węzła
                            System.out.println("Zmieniono ID węzła na: " + newId);

                            // Utwórz nowy węzeł z nowym ID
                            Node newNode = drzewo.getGraf().addNode(newId);
                            Wierzcholek nowyWierzcholek = new Wierzcholek(newId, usunKropki(newId),usunKropki(newId) +" " +field2.getText().trim());

                            // Skopiuj atrybuty ze starego węzła
                            originalNode.attributeKeys().forEach(key -> {
                                if (!"label".equals(key)) {
                                    Object value = originalNode.getAttribute(key);
                                    newNode.setAttribute(key, value);
                                }
                            });
                            newNode.setAttribute("label", usunKropki(newId));

                            // Przenieś krawędzie
                            String finalNewId = newId;
                            originalNode.edges().forEach(edge -> {
                                if (Objects.equals(edge.getSourceNode(), originalNode)) {
                                    // Krawędź wychodząca
                                    Krawedz krawedz = new Krawedz(finalNewId + edge.getTargetNode().getId(), finalNewId,
                                            edge.getTargetNode().getId(), edge.getAttribute("ui.label").toString());
                                    drzewo.dodajKrawedz(krawedz);
                                } else {
                                    // Krawędź przychodząca
                                    String sourceId = edge.getSourceNode().getId();
                                    Krawedz krawedz = new Krawedz(sourceId + finalNewId, sourceId, finalNewId, edge.getAttribute("ui.label").toString());
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
                   ustawLiscie(drzewo,true);
                    if(tabela)
                    {
                        zmienKolorWybranychElementow(drzewo,finalWierzcholek,false);
                    }
                    System.out.println();
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
    private void zmienKolorWybranychElementow(Drzewo drzewo,Wierzcholek wierzcholek,boolean reset)
    {
        if(poprzednioWybranyWierzcholek!=null)
        {
            if(poprzednioWybranyWierzcholek.getDzieciId().size()==0)
            {
                drzewo.getGraf().getNode(poprzednioWybranyWierzcholek.getId()).setAttribute("ui.style","fill-color:"+kolorLisci+";");
            }
            else
            {
                drzewo.getGraf().getNode(poprzednioWybranyWierzcholek.getId()).setAttribute("ui.style","fill-color:"+kolorWezlow+";");
            }
            Edge edge =  drzewo.getGraf().getEdge(poprzednioWybranyWierzcholek.getRodzicId()+poprzednioWybranyWierzcholek.getId());
            if(edge!=null)
            {
                edge.setAttribute("ui.style","fill-color: black;");
            }
        }
        if(!reset)
        {
            drzewo.getGraf().getNode(wierzcholek.getId()).setAttribute("ui.style","fill-color: "+kolorWyboranegoElementu+";");
            Edge edge = drzewo.getGraf().getEdge(wierzcholek.getRodzicId()+wierzcholek.getId());
            if(edge!=null)
            {
                edge.setAttribute("ui.style","fill-color:"+kolorWyboranegoElementu+";");
            }
        }
        else
        {
            poprzednioWybranyWierzcholek=null;
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
                            int ustaw = paddingLisci + 3;
                            drzewo.getGraf().getNode(wierzcholek.getId()).setAttribute("ui.style","padding:"+ustaw+",3px;" );
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
                           // drzewo.getGraf().getNode(wierzcholek.getId()).setAttribute("ui.style","padding: 20px;" );
                            break;
                        }
                    }
                }
            }
            node.setAttribute("label",nazwa);
        }
    }
    private void readSetting() {
        Preferences prefs = Preferences.userNodeForPackage(UstawieniaController.class);
        shapeWezlow = prefs.get("shapeWezlow", "circle");
        shapeLisci = prefs.get("shapeLisci", "box");
        kolorWezlow = prefs.get("kolorWezlow", "white");
        kolorLisci =  prefs.get("kolorLisci", "green");
        obramowaniePx = prefs.get("obramowaniePx", "1px");
        kolorObramowania = prefs.get("kolorObramowania", "black");
        sizeMode = prefs.get("sizeMode", "fit");
        liczbaPx = prefs.get("liczbaPx","11");
        kolorTekstu = prefs.get("kolorTekstu","black");
        kolorWyboranegoElementu = prefs.get("kolorWyboranegoElementu","red");
        format = prefs.get("format","txt");
        sciezka = prefs.get("sciezka","");
    }
    private void ustawLiscie(Drzewo drzewo,boolean kolor){
        for(Wierzcholek wierzcholek: drzewo.getListaWierzcholkow())
        {
            if(wierzcholek.getDzieciId().size()==0)
            {
                if(kolor)
                {
                    drzewo.getGraf().getNode(wierzcholek.getId()).setAttribute("ui.style","shape: "+shapeLisci+";  fill-color: " +kolorLisci+";" );
                }
                else
                {
                    drzewo.getGraf().getNode(wierzcholek.getId()).setAttribute("ui.style","shape: "+shapeLisci+";" );
                }
            }
        }
    }

    private void wczytajZPliku(Drzewo drzewo) throws IOException, JAXBException {
        if(format.equals("txt"))
        {
            GrafWczytajTxt grafWczytajTxt = new GrafWczytajTxt();
            if(sciezka.equals(""))
            {
                grafWczytajTxt.loadGraph("/Dane/drzewo_decyzyjne_1.txt", drzewo,true);
            }
            else
            {
                grafWczytajTxt.loadGraph(sciezka, drzewo,false);
            }

        }
        else if (format.equals("json"))
        {
            GrafWczytajJson grafWczytajJson = new GrafWczytajJson();

            if(sciezka.equals(""))
            {
                grafWczytajJson.loadGraph("/Dane/drzewo_decyzyjne_json.json",drzewo,true);
            }
            else
            {
                grafWczytajJson.loadGraph(sciezka, drzewo,false);
            }

        }
        else if (format.equals("xml"))
        {
            GrafWczytajXml grafWczytajXml = new GrafWczytajXml();

            if(sciezka.equals(""))
            {
                System.out.println("przykladowy");
                grafWczytajXml.loadGraph("/Dane/decisionTree.xml",drzewo,true);
            }
            else
            {
                grafWczytajXml.loadGraph(sciezka, drzewo,false);
            }
        }
    }

    private void konwertujPNG()
    {
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
    }

    private void konwertujJPG()
    {
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
    }

    private void konwertujSVG(Drzewo drzewo)
    {
        FileSinkSVG fileSink = new FileSinkSVG();
        try {
            String currentStylesheet = drzewo.getGraf().getAttribute("ui.stylesheet", String.class);

            if (currentStylesheet == null) {
                currentStylesheet = "";
            }

//                String additionalStyle = "";
//
//                String updatedStylesheet = currentStylesheet + " " + additionalStyle;
            Graph nowyGraph = new SingleGraph("DrzewoSVG");
            Drzewo drzewo1 = new Drzewo();
            List<Wierzcholek> nowaListaWierzcholkow = new ArrayList<>();
            for(Wierzcholek wierzcholek: drzewo.getListaWierzcholkow())
            {
                Wierzcholek nowyWierzcholek = new Wierzcholek(wierzcholek.getId(),wierzcholek.getLabel(),wierzcholek.getFullLabel());
                nowyWierzcholek.setRodzicId(wierzcholek.getRodzicId());
                nowyWierzcholek.setDzieciId(wierzcholek.getDzieciId());
                nowyWierzcholek.setPozX(wierzcholek.getPozX());
                nowyWierzcholek.setPozY(wierzcholek.getPozY());
                nowyWierzcholek.setRoznica(wierzcholek.getRoznica());
                nowyWierzcholek.setWartosc(wierzcholek.getWartosc());
                nowyWierzcholek.setStrona(wierzcholek.getStrona());
                nowyWierzcholek.setGlebokosc(wierzcholek.getGlebokosc());
                nowaListaWierzcholkow.add(nowyWierzcholek);
            }
            drzewo1.setListaWierzcholkow(nowaListaWierzcholkow);
            drzewo1.setMinOdlegosc(60);
            drzewo1.setGraf(nowyGraph);
            drzewo1.setMaksymalnaGlebokosc(drzewo.getMaksymalnaGlebokosc());
            for(Node node : drzewo.getGraf())
            {
//                    double[] xyz = new double[3];
//                    nodePosition(node, xyz);
                Node newNode = nowyGraph.addNode(node.getId());
                node.attributeKeys().forEach(key -> {
                    if (!"xy".equals(key)) {
                        Object value = node.getAttribute(key);
                        if ("ui.style".equals(key)) {
                            // Jeśli już istnieje styl, dodaj do niego nowy
                            String existingStyle = value.toString();
                            value = existingStyle + " size: 90px,15px;";
                        }

                        newNode.setAttribute(key, value);
                    }

                });
                String size = drzewo.getWierzcholekByid(node.getId()).getDzieciId().size()>0 ? "90px,15px" : "75px,15px";
                String currentStyle = newNode.getAttribute("ui.style") != null
                        ? newNode.getAttribute("ui.style").toString()
                        : "";
                newNode.setAttribute("ui.style", currentStyle + " size: " + size + ";");
//                    newNode.setAttribute("xy", xyz[0], -xyz[1]);
            }

            drzewo1.poprawUstawienieWierzcholkow();
            for(Node node : nowyGraph)
            {
                double[] xyz = new double[3];
                nodePosition(node, xyz);
                node.setAttribute("xy", xyz[0], -xyz[1]);
            }
            for(int i=0;i<drzewo.getGraf().getEdgeCount();i++)
            {
                nowyGraph.addEdge(drzewo.getGraf().getEdge(i).getId(),drzewo.getGraf().getEdge(i).getNode0().getId(),drzewo.getGraf().getEdge(i).getNode1().getId());
            }

            nowyGraph.setAttribute("ui.stylesheet",currentStylesheet);
            fileSink.writeAll(nowyGraph, "output_graph.svg");

            System.out.println("Graf został zapisany jako SVG: output_graph.svg");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void Powrot() throws IOException {
        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("menu-view.fxml"));
        Parent root = loader.load();
        Scene currentScene = borderPaneGlowny.getScene();
        Stage stage = (Stage) currentScene.getWindow();
        stage.setScene(new Scene(root, currentScene.getWidth(), currentScene.getHeight()));
    }

}