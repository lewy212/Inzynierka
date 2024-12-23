package com.example.inzynierka.klasy.Json;

import com.example.inzynierka.klasy.ElementyDrzewa.Drzewo;
import com.example.inzynierka.klasy.ElementyDrzewa.Krawedz;
import com.example.inzynierka.klasy.ElementyDrzewa.Wierzcholek;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class GrafWczytajJson {

    private String[] wykorzystaneNazwy;

    private double roznica;
    public GrafWczytajJson() {
        this.wykorzystaneNazwy = new String[0];
        this.roznica = 0.0;
    }

    public void loadGraph(String filePath, Drzewo drzewo,boolean czyPrzykladowy) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        InputStream inputStream = null;
        if(czyPrzykladowy)
        {
            inputStream = getClass().getResourceAsStream(filePath);
        }
        else
        {
            inputStream = new FileInputStream(filePath);
        }
        JsonDrzewo drzewoJson = objectMapper.readValue(inputStream, JsonDrzewo.class);
        Drzewo noweDrzewo = przypiszDane(drzewoJson,drzewo,null,null,null,0);
    }

    private Drzewo przypiszDane(JsonDrzewo jsonDrzewo,Drzewo drzewo,Wierzcholek poprzedniWierzcholek,DaneNazwy poprzedniDaneNazwy,String czyTak,int glebokosc)
    {
        if(jsonDrzewo!=null)
        {
            String[] parts = jsonDrzewo.getQuestion().split(" (?=\\S)");
            DaneNazwy daneNazwy = new DaneNazwy(jsonDrzewo);
            String idWierzcholka =  daneNazwy.getNazwa() + ".".repeat(obliczIleRazyTaSamaNazwa(daneNazwy.getNazwa()));
            wykorzystaneNazwy = Arrays.copyOf(wykorzystaneNazwy, wykorzystaneNazwy.length + 1);
            wykorzystaneNazwy[wykorzystaneNazwy.length-1]=daneNazwy.getNazwa();
            Wierzcholek wierzcholek = new Wierzcholek(idWierzcholka,daneNazwy.getNazwa(),daneNazwy.getNazwa()+" "+daneNazwy.getLabel());
            wierzcholek.setGlebokosc(glebokosc);
            drzewo.dodajWierzcholek(wierzcholek);
            drzewo.setMaksymalnaGlebokosc(glebokosc);
            if(poprzedniWierzcholek==null)
            {
                drzewo.getGraf().getNode(wierzcholek.getId()).setAttribute("xy",new double[]{0.0, 0.0});
                wierzcholek.setPozX(0.0);
                wierzcholek.setPozY(0.0);
                poprzedniWierzcholek=wierzcholek;
            }
            else
            {

                Krawedz krawedz = new Krawedz(poprzedniWierzcholek.getId()+wierzcholek.getId(),poprzedniWierzcholek.getId(),wierzcholek.getId()
                        ,czyTak);
                drzewo.dodajKrawedz(krawedz);
                ustawPolozenieWierzcholka(wierzcholek,drzewo,czyTak,poprzedniWierzcholek);
                poprzedniWierzcholek=wierzcholek;
            }
            if(jsonDrzewo.getYes() != null)
            {
                przypiszDane(jsonDrzewo.getYes(),drzewo,poprzedniWierzcholek,daneNazwy,"yes",glebokosc+1);
            }
            if(jsonDrzewo.getNo() != null)
            {
                przypiszDane(jsonDrzewo.getNo(),drzewo,poprzedniWierzcholek,daneNazwy,"no",glebokosc+1);
            }
        }
        return drzewo;
    }

    private int obliczIleRazyTaSamaNazwa(String nazwa)
    {
        int ileRazy = 0;
        if(wykorzystaneNazwy.length>0)
        {
            for(String tekst: wykorzystaneNazwy)
            {
                if(tekst.contains(nazwa))
                {
                    ileRazy++;
                }
            }
        }
        return ileRazy;
    }
    private String odwrocZnak(String tekst, String czyTak)
    {
        if(!"no".equals(czyTak))
        {
            return tekst;
        }
        String[] parts = tekst.split(" ",2);
        String operator = parts[0];
        String liczba = parts[1];
        String odwroconyOperator;

        switch (operator) {
            case "<=":
                odwroconyOperator = ">";
                break;
            case ">=":
                odwroconyOperator = "<";
                break;
            case "<":
                odwroconyOperator = ">=";
                break;
            case ">":
                odwroconyOperator = "<=";
                break;
            default:
                odwroconyOperator = operator;
                break;
        }
        return odwroconyOperator+" "+liczba;
    }

    private void ustawPolozenieWierzcholka(Wierzcholek wierzcholek,Drzewo drzewo,String czyTak, Wierzcholek poprzedniWierzcholek)
    {
        if("yes".equals(czyTak))
        {
            if(poprzedniWierzcholek.getPozX()!=null)
            {
                if(poprzedniWierzcholek.getPozX()==0.0)
                {
                    drzewo.getGraf().getNode(wierzcholek.getId()).setAttribute("xy",new double[]{150.0, -50.0});
                    wierzcholek.setPozX(150.0);
                    wierzcholek.setPozY(-50.0);
                    wierzcholek.setRoznica(150.0);
                }
                else
                {
                    wierzcholek.setPozX(poprzedniWierzcholek.getPozX()+ poprzedniWierzcholek.getRoznica()*1.2/2);
                    wierzcholek.setPozY(poprzedniWierzcholek.getPozY()-30);
                    drzewo.getGraf().getNode(wierzcholek.getId()).setAttribute("xy",new double[]{wierzcholek.getPozX(), wierzcholek.getPozY()});
                    wierzcholek.setRoznica(Math.abs(wierzcholek.getPozX()-poprzedniWierzcholek.getPozX()));
                }
            }

        }
        if("no".equals(czyTak))
        {
            if(poprzedniWierzcholek.getPozX()!=null)
            {
                if(poprzedniWierzcholek.getPozX()==0.0)
                {
                    drzewo.getGraf().getNode(wierzcholek.getId()).setAttribute("xy",new double[]{-150.0, -50.0});
                    wierzcholek.setPozX(-150.0);
                    wierzcholek.setPozY(-50.0);
                    wierzcholek.setRoznica(150.0);
                }
                else
                {
                    wierzcholek.setPozX(poprzedniWierzcholek.getPozX()-poprzedniWierzcholek.getRoznica()*1.2/2);
                    wierzcholek.setPozY(poprzedniWierzcholek.getPozY()-30);
                    drzewo.getGraf().getNode(wierzcholek.getId()).setAttribute("xy",new double[]{wierzcholek.getPozX(), wierzcholek.getPozY()});
                    wierzcholek.setRoznica(Math.abs(wierzcholek.getPozX()-poprzedniWierzcholek.getPozX()));
                }
            }
        }
    }
}
