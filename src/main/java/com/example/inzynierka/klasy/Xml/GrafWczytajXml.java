package com.example.inzynierka.klasy.Xml;

import com.example.inzynierka.klasy.ElementyDrzewa.Drzewo;
import com.example.inzynierka.klasy.ElementyDrzewa.Krawedz;
import com.example.inzynierka.klasy.ElementyDrzewa.Wierzcholek;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;

public class GrafWczytajXml {

    private String[] wykorzystaneNazwy;

    public GrafWczytajXml() {
        this.wykorzystaneNazwy = new String[0];

    }
    public void loadGraph(String filePath, Drzewo drzewo,boolean czyPrzykladowy) throws JAXBException {

        JAXBContext context = JAXBContext.newInstance(XmlDrzewo.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        if(czyPrzykladowy)
        {
            try (InputStream inputStream = getClass().getResourceAsStream(filePath)) {
                if (inputStream == null) {
                    throw new IllegalArgumentException("Plik nie został znaleziony w zasobach: " + filePath);
                }
                XmlDrzewo drzewoXml = (XmlDrzewo) unmarshaller.unmarshal(inputStream);
                // Przypisanie danych do obiektu Drzewo
                Drzewo noweDrzewo = przypiszDane(drzewoXml.getNode(), drzewo, null, null, null,0);

                for (Wierzcholek wierzcholek : noweDrzewo.getListaWierzcholkow()) {
                    System.out.println(wierzcholek.getId() + "   " + wierzcholek.getLabel());
                }

            } catch (Exception e) {
                throw new RuntimeException("Błąd podczas wczytywania pliku XML: " + filePath, e);
            }
        }
        else
        {
            try (InputStream inputStream = new FileInputStream(filePath)) {
                XmlDrzewo drzewoXml = (XmlDrzewo) unmarshaller.unmarshal(inputStream);
                Drzewo noweDrzewo = przypiszDane(drzewoXml.getNode(), drzewo, null, null, null,0);

                for (Wierzcholek wierzcholek : noweDrzewo.getListaWierzcholkow()) {
                    System.out.println(wierzcholek.getId() + "   " + wierzcholek.getLabel());
                }

            } catch (FileNotFoundException e) {
                throw new IllegalArgumentException("Plik nie został znaleziony: " + filePath, e);
            } catch (Exception e) {
                throw new RuntimeException("Błąd podczas wczytywania pliku XML: " + filePath, e);
            }
        }

    }

    private Drzewo przypiszDane(Wezel node, Drzewo drzewo, Wierzcholek poprzedniWierzcholek, DaneNazwyXml poprzedniDaneNazwy, String czyTak,int glebokosc)
    {
       if(node.getQuestion() != null)
       {
           DaneNazwyXml daneNazwyXml = new DaneNazwyXml(node);
           String idWierzcholka =  daneNazwyXml.getNazwa() + ".".repeat(obliczIleRazyTaSamaNazwa(daneNazwyXml.getNazwa()));
           wykorzystaneNazwy = Arrays.copyOf(wykorzystaneNazwy, wykorzystaneNazwy.length + 1);
           wykorzystaneNazwy[wykorzystaneNazwy.length-1]=daneNazwyXml.getNazwa();
           Wierzcholek wierzcholek = new Wierzcholek(idWierzcholka,daneNazwyXml.getNazwa(), daneNazwyXml.getNazwa()+" "+daneNazwyXml.getLabel());
           wierzcholek.setGlebokosc(glebokosc);
           drzewo.dodajWierzcholek(wierzcholek);
           drzewo.setMaksymalnaGlebokosc(glebokosc);
           if (poprzedniWierzcholek==null)
           {
               drzewo.getGraf().getNode(wierzcholek.getId()).setAttribute("xy",0,0);
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
           if(node.getYes()!=null)
           {
               przypiszDane(node.getYes(),drzewo,poprzedniWierzcholek,daneNazwyXml,"yes",glebokosc+1);
           }
           if(node.getNo()!=null)
           {
               przypiszDane(node.getNo(),drzewo,poprzedniWierzcholek,daneNazwyXml,"no",glebokosc+1);
           }

       }
        if(node.getLeaf()!=null)
        {
            DaneNazwyXml daneNazwyXml = new DaneNazwyXml(node.getLeaf());
            String idWierzcholka =  daneNazwyXml.getNazwa() + ".".repeat(obliczIleRazyTaSamaNazwa(daneNazwyXml.getNazwa()));
            wykorzystaneNazwy = Arrays.copyOf(wykorzystaneNazwy, wykorzystaneNazwy.length + 1);
            wykorzystaneNazwy[wykorzystaneNazwy.length-1]=daneNazwyXml.getNazwa();
            Wierzcholek wierzcholek = new Wierzcholek(idWierzcholka,daneNazwyXml.getNazwa(), daneNazwyXml.getNazwa()+" "+daneNazwyXml.getLabel());
            drzewo.dodajWierzcholek(wierzcholek);
            wierzcholek.setGlebokosc(glebokosc);
            drzewo.setMaksymalnaGlebokosc(glebokosc);
            Krawedz krawedz = new Krawedz(poprzedniWierzcholek.getId()+wierzcholek.getId(),poprzedniWierzcholek.getId(),wierzcholek.getId()
                    ,czyTak);
            drzewo.dodajKrawedz(krawedz);
            ustawPolozenieWierzcholka(wierzcholek,drzewo,czyTak,poprzedniWierzcholek);
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
                odwroconyOperator = operator;  // Jeśli nie rozpoznano operatora, zwróć oryginalny
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
