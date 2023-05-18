package org.ssl.model;

import javafx.scene.image.Image;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Helper {

    private static String playGameInfo = "playgame.json";

    public static Image loadImage(String resource, int width, int height) {
        return new Image(Helper.class.getClassLoader().getResourceAsStream(resource), width, height, true, true);
    }

    public static byte[] loadDataFromFile(File file) {
        try {
            return Files.readAllBytes(Paths.get(file.toURI()));
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static byte[] loadDataFromResource(String resource) {
        URI uri = null;
        try {
            URL url = Helper.class.getClassLoader().getResource(resource);
            if (url != null) {
                uri = url.toURI();
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        if (uri == null) {
            return null;
        }
        try {
            return Files.readAllBytes(Paths.get(uri));
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static void saveDataToFile(byte[] data, File file) {
        try {
     //       byte[] bytes = loadDataFromFile(file);
            Files.write(Paths.get(file.toURI()), data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveGameInfoToFile(byte[] data) {
        URI uri = null;
        try {
            URL url = Helper.class.getClassLoader().getResource(playGameInfo);
            if (url != null) {
                uri = url.toURI();
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        if (uri == null) {
            return ;
        }
        try {
            Files.write(Paths.get(uri), data);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public static void saveDataToFile(byte[] data) {
        URI uri = null;
        try {
            URL url = Helper.class.getClassLoader().getResource(playGameInfo);
            if (url != null) {
                uri = url.toURI();
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        if (uri == null) {
            return;
        }
        try {
            Files.write(Paths.get(uri), data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
