package com.example.inzynierka.klasy;

import org.graphstream.graph.Edge;

import java.io.*;
import java.util.*;

public class GrafWczytajTxt {
    private String[] wykorzystaneNazwy;
    private String[] wykorzystaneLiscie;
    private String[] wykorzystaneEtykiety;
    private double odlegloscMiedzyWierzcholkami;
    private double aktualnySrodek;
    private boolean wKtoraStrone;
    private boolean odwrotnaStrona;
    private String poprzednieWciecie;
    private String poprzedniaLinia;
    private List<Para> wykorzystanePary;
    private List<Para> wczytaneDane;
    public GrafWczytajTxt(){
        this.wykorzystaneNazwy = new String[0];
        this.wykorzystaneLiscie = new String[0];
        this.wykorzystaneEtykiety = new String[0];
        this.wykorzystanePary = new ArrayList<>();
        this.wczytaneDane = new ArrayList<>();
        this.poprzednieWciecie = "";
        this.poprzedniaLinia = "";
        this.wKtoraStrone = true;
        this.odwrotnaStrona = false;
        this.aktualnySrodek=0;
        this.odlegloscMiedzyWierzcholkami = 15;
    }
    public void loadGraph(String filePath, Drzewo drzewo) {
        try(InputStream is = getClass().getResourceAsStream(filePath);
         BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                processLine(line, drzewo);
            }
        } catch (IOException e ) {
            e.printStackTrace();
        }
    }

    private void processLine(String linia, Drzewo drzewo)
    {
        int liczbaKresek = obliczLiczbeKresek(linia);
        String[] parts = linia.split(" (?=\\S)");
        String[] poprzednieParts = poprzednieWciecie.split(" (?=\\S)");
        if(wykorzystaneNazwy.length > 0)
        {
            if(obliczLiczbeKresek(poprzednieWciecie)>=liczbaKresek)
            {
                poprzednieWciecie = linia;
                poprzednieParts = poprzednieWciecie.split(" (?=\\S)");
            }
            else if(liczbaKresek == obliczLiczbeKresek(poprzednieWciecie)+2 )
            {
                poprzednieWciecie=poprzedniaLinia;
                poprzednieParts = poprzednieWciecie.split(" (?=\\S)");
            }
            int ileRazyTaSamaNazwa = 0;
            for(String nazwa: wykorzystaneNazwy){
                if(nazwa.contains((parts[liczbaKresek])))
                {
                    ileRazyTaSamaNazwa++;
                }
            }
            if(liczbaKresek > 0)
            {
                String nazwa = parts[liczbaKresek];
                if (!czyIstniejeWezel(nazwa,liczbaKresek))
                {
                    wykorzystaneNazwy = Arrays.copyOf(wykorzystaneNazwy, wykorzystaneNazwy.length + 1);
                    parts[liczbaKresek]=parts[liczbaKresek] + ".".repeat(ileRazyTaSamaNazwa);
                    Wierzcholek wierzcholek = new Wierzcholek(parts[liczbaKresek],nazwa);
                    drzewo.dodajWierzcholek(wierzcholek);
                    wykorzystaneNazwy[wykorzystaneNazwy.length-1] = parts[liczbaKresek];
                    wykorzystanePary.add(new Para(parts[liczbaKresek],liczbaKresek));
                    if(odwrotnaStrona)
                    {
                        ustawPolozenieWierzcholka(wierzcholek,drzewo,liczbaKresek,!wKtoraStrone,false,linia);
                    }
                    else
                    {
                        ustawPolozenieWierzcholka(wierzcholek,drzewo,liczbaKresek,wKtoraStrone,false,linia);
                    }
                    for(int i=wykorzystanePary.size()-2;i>=0;i--)
                    {
                        if(wykorzystanePary.get(i).getLiczbaKresek()==liczbaKresek-1)
                        {
                            int liczbaKresekPoprzednieWciecie = obliczLiczbeKresek(poprzednieWciecie);
                            Krawedz krawedz = new Krawedz(wykorzystanePary.get(i).getNazwa()+wykorzystanePary.get(wykorzystanePary.size()-1).getNazwa()
                                    ,wykorzystanePary.get(i).getNazwa(),
                                    wykorzystanePary.get(wykorzystanePary.size()-1).getNazwa(),
                                    poprzednieParts[liczbaKresekPoprzednieWciecie+1]+" "+poprzednieParts[liczbaKresekPoprzednieWciecie+2]);
                            drzewo.dodajKrawedz(krawedz);
                            break;
                        }
                    }
                }
                else
                {
                    for(int i=wykorzystanePary.size()-1;i>=0;i--)
                    {
                        if(wykorzystanePary.get(i).getLiczbaKresek()<liczbaKresek)
                        {
                            break;
                        }
                        if(wykorzystanePary.get(i).getLiczbaKresek()==liczbaKresek)
                        {
                            if(wykorzystanePary.get(i).getNazwa().contains(nazwa))
                            {
                                double starySrodek = aktualnySrodek;
                                aktualnySrodek = wykorzystanePary.get(i).getSrodek();
                                System.out.println("stare: "+ starySrodek+" nowy: "+aktualnySrodek+ " "+wykorzystanePary.get(i).getNazwa());
                                break;
                            }
                        }
                    }
                }
                if(linia.contains(":"))
                {
                    stworzLisc(linia,drzewo);
                }
            }
            else
            {
                wKtoraStrone = false;
            }
        }
        else
        {
            poprzednieWciecie=linia;

            wykorzystaneNazwy = Arrays.copyOf(wykorzystaneNazwy, wykorzystaneNazwy.length + 1);
            wykorzystaneEtykiety = Arrays.copyOf(wykorzystaneEtykiety,wykorzystaneEtykiety.length+1);
            Wierzcholek wierzcholek = new Wierzcholek(parts[liczbaKresek],parts[liczbaKresek]);
            drzewo.dodajWierzcholek(wierzcholek);
            wykorzystaneNazwy[wykorzystaneNazwy.length-1] = parts[liczbaKresek];
            wykorzystaneEtykiety[wykorzystaneEtykiety.length-1] = parts[liczbaKresek+1]+parts[liczbaKresek+2];
            wykorzystanePary.add(new Para(parts[liczbaKresek],liczbaKresek));
            wykorzystanePary.get(wykorzystanePary.size()-1).setSrodek(0);
        }
        poprzedniaLinia = linia;
        wczytaneDane.add(new Para(parts[liczbaKresek],liczbaKresek));
        System.out.println("Na koncu: "+wykorzystanePary.get(wykorzystanePary.size()-1).getNazwa() + " " + +wykorzystanePary.get(wykorzystanePary.size()-1).getSrodek());
        System.out.println(aktualnySrodek);
    }
    private int obliczLiczbeKresek(String linia)
    {
        int ile = 0;
        for (int i = 0; i < linia.length(); i++)
        {
            if (linia.charAt(i) == '|')
            {
                ile++;
            }
        }
        return ile;
    }
    private boolean czyIstniejeWezel(String nazwa,int ileKresek)
    {
        boolean odpowiedz = false;
        int ile=0;
        if(wczytaneDane.size()>1){
            for(int i=wczytaneDane.size()-1;i>=0;i--)
            {
                ile++;
                if(wczytaneDane.get(i).getLiczbaKresek()<ileKresek)
                {
                    break;
                }
                if(wczytaneDane.get(i).getLiczbaKresek()==ileKresek)
                {
                    if(wczytaneDane.get(i).getNazwa().contains(nazwa))
                    {
                        odpowiedz=true;
                        break;
                    }
                }
            }
        }
        if(odpowiedz==true)
        {
            odwrotnaStrona=true;
            System.out.println("Istnieje wezel");
        }
        return odpowiedz;
    }
    private void stworzLisc(String linia, Drzewo drzewo)
    {
        String[] parts = linia.split(" (?=\\S)");
        int liczbaKresek=obliczLiczbeKresek(linia);
        int ileRazyTaSamaNazwa = 0;
        if (wykorzystaneLiscie.length>0)
        {
            for(String lisc:wykorzystaneLiscie){
                if(lisc.contains((parts[liczbaKresek+5])))
                {
                    ileRazyTaSamaNazwa++;
                }
            }
        }

        wykorzystaneLiscie = Arrays.copyOf(wykorzystaneLiscie, wykorzystaneLiscie.length + 1);
        String nazwa=parts[liczbaKresek+5];
        parts[liczbaKresek+5]=parts[liczbaKresek+5] + ".".repeat(ileRazyTaSamaNazwa);

        Wierzcholek wierzcholek1 = new Wierzcholek(parts[liczbaKresek+5],nazwa);
        drzewo.dodajWierzcholek(wierzcholek1);
        if(odwrotnaStrona)
        {
            ustawPolozenieWierzcholka(wierzcholek1,drzewo,liczbaKresek,!wKtoraStrone,true,linia);
        }
        else
        {
            ustawPolozenieWierzcholka(wierzcholek1,drzewo,liczbaKresek,wKtoraStrone,true,linia);
        }
        String id="";
        for(int i=wykorzystanePary.size()-1;i>=0;i--)
        {
            if(wykorzystanePary.get(i).getNazwa().contains(parts[liczbaKresek]))
            {
                if(liczbaKresek==wykorzystanePary.get(i).getLiczbaKresek())
                {
                    id=wykorzystanePary.get(i).getNazwa();
                    break;
                }
            }
        }
        Krawedz krawedz1 = new Krawedz(parts[liczbaKresek+5]+id,id,parts[liczbaKresek+5],parts[liczbaKresek+1]+" "+parts[liczbaKresek+2]);

        drzewo.dodajKrawedz(krawedz1);
        wykorzystaneLiscie[wykorzystaneLiscie.length-1] = parts[liczbaKresek+5];
    }
    private void ustawPolozenieWierzcholka(Wierzcholek wierzcholek,Drzewo drzewo,int liczbaKresek,boolean prawoCzyLewo,boolean czyLisc,String linia)
    {
        double zmiennaDodajaca = 0;

        System.out.println("poprzednia linia: "+poprzedniaLinia+" : "+obliczLiczbeKresek(poprzedniaLinia));
        System.out.println("Aktualna linia: "+linia+" : "+liczbaKresek+" : "+obliczLiczbeKresek(linia));

//        if(obliczLiczbeKresek(poprzedniaLinia)==liczbaKresek)
//        {
//            for(int i=wykorzystanePary.size()-2;i>=0;i--)
//            {
//                if(obliczLiczbeKresek(linia)>wykorzystanePary.get(i).getLiczbaKresek())
//                {
//                    System.out.println("zmieniam: ");
//                    aktualnySrodek = wykorzystanePary.get(i).getSrodek();
//                    System.out.println("zmieniam: "+aktualnySrodek);
//                    break;
//                }
//            }
//        }


        if(czyLisc==true)
        {
            liczbaKresek++;
        }
        if(wKtoraStrone)
        {
            boolean flaga = true;
            if(liczbaKresek==1)
            {
                drzewo.getGraf().getNode(wierzcholek.getId()).setAttribute("xy",aktualnySrodek+odlegloscMiedzyWierzcholkami*4-liczbaKresek,-20*liczbaKresek+zmiennaDodajaca);
                flaga = false;
            }
            if(prawoCzyLewo)
            {
                if(flaga)
                drzewo.getGraf().getNode(wierzcholek.getId()).setAttribute("xy",aktualnySrodek+odlegloscMiedzyWierzcholkami-liczbaKresek,-20*liczbaKresek+zmiennaDodajaca);
                if(czyLisc)
                {
                    System.out.println("Pozycja liscia: "+(aktualnySrodek+odlegloscMiedzyWierzcholkami));
                }
                if(!czyLisc)
                {
                    if(flaga)
                    aktualnySrodek=aktualnySrodek+odlegloscMiedzyWierzcholkami;
                    else
                    {
                        aktualnySrodek=aktualnySrodek+odlegloscMiedzyWierzcholkami*4;
                    }
                }

            }
            else
            {
                System.out.println("Ide w lewo");
                drzewo.getGraf().getNode(wierzcholek.getId()).setAttribute("xy",aktualnySrodek-odlegloscMiedzyWierzcholkami+liczbaKresek,-20*liczbaKresek+zmiennaDodajaca);
                if(czyLisc)
                {
                    System.out.println("Pozycja liscia: "+(aktualnySrodek-odlegloscMiedzyWierzcholkami));
                }
                if(!czyLisc)
                {
                    aktualnySrodek=aktualnySrodek-odlegloscMiedzyWierzcholkami;

                }

            }

        }
        if(!wKtoraStrone)
        {
            if(prawoCzyLewo)
            {
                drzewo.getGraf().getNode(wierzcholek.getId()).setAttribute("xy",-aktualnySrodek+odlegloscMiedzyWierzcholkami-liczbaKresek,-20*liczbaKresek+zmiennaDodajaca);
                if(czyLisc)
                {
                    System.out.println("Pozycja liscia: "+(aktualnySrodek-odlegloscMiedzyWierzcholkami));
                }
                if(!czyLisc)
                {
                    aktualnySrodek=aktualnySrodek-odlegloscMiedzyWierzcholkami;
                }

            }
            else
            {
                System.out.println("Ide w lewo");
                drzewo.getGraf().getNode(wierzcholek.getId()).setAttribute("xy",-aktualnySrodek-odlegloscMiedzyWierzcholkami+liczbaKresek,-20*liczbaKresek+zmiennaDodajaca);
                if(czyLisc)
                {
                    System.out.println("Pozycja liscia: "+(aktualnySrodek+odlegloscMiedzyWierzcholkami));
                }
                if(!czyLisc)
                {
                    aktualnySrodek=aktualnySrodek+odlegloscMiedzyWierzcholkami;

                }

            }

        }
//        if(!wKtoraStrone)
//        {
//            if(prawoCzyLewo)
//            {
//                drzewo.getGraf().getNode(wierzcholek.getId()).setAttribute("xy",-25+4*liczbaKresek,-8*liczbaKresek+zmiennaDodajaca);
//            }
//            else
//            {
//                drzewo.getGraf().getNode(wierzcholek.getId()).setAttribute("xy",-25-4*liczbaKresek,-8*liczbaKresek+zmiennaDodajaca);
//            }
//        }
        if(!czyLisc)
        {
            wykorzystanePary.get(wykorzystanePary.size()-1).setSrodek(aktualnySrodek);
        }


        odwrotnaStrona = false;
    }
}
