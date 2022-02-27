package griglog.damage_debug;

import javax.management.RuntimeErrorException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class Config {
    public static boolean show = true;
    public static boolean showHeals = true;
    private static final String path = "config/damage_debug.txt";
    public static void save(){
        try {
            String file = String.format("show:%b\nshowHeals:%b", show, showHeals);
            FileWriter fw = new FileWriter(path);
            fw.write(file);
            fw.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void load(){
        try {
            String file = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
            String[] lines = file.split("\n");
            show = getBool(lines[0].split(":")[1]);
            showHeals = getBool(lines[1].split(":")[1]);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private static boolean getBool(String str){
        if (str.equals("true"))
            return true;
        else if (str.equals("false"))
            return false;
        throw new RuntimeException();
    }
}
