package com.example.inzynierka.klasy;

import com.example.inzynierka.klasy.Json.JsonDrzewo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class DrzewoGenerator {
    private static final Random random = new Random();

    public static JsonDrzewo wygenerujDrzewo(int glebokosc) {
        if (glebokosc <= 0) {
            return stworzLisc();
        }

        String question = "F" + random.nextInt(10) + " <= " + String.format("%.6f", random.nextDouble());
        return new JsonDrzewo(
                question,
                "node",
                wygenerujDrzewo(glebokosc - 1),
                random.nextBoolean() ? wygenerujDrzewo(glebokosc - 1) : null
        );
    }

    private static JsonDrzewo stworzLisc() {
        int label = random.nextInt(12);
        String value = String.format("%d (%c) (%d/%d)", label, (char) ('A' + label), random.nextInt(5000), random.nextInt(5000));
        return new JsonDrzewo(value, "leaf", null, null);
    }

    public static File drzewoDoPliku(int maxGlebokosc, String filePath) throws IOException {
        JsonDrzewo tree = wygenerujDrzewo(maxGlebokosc);
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File(filePath);
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, tree);
        return file;
    }
}
