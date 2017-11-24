package main.ava;

import javafx.util.Pair;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.*;

/**
 * Created by slava on 18.11.2017.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("src/input.txt"), StandardCharsets.UTF_8);
        ProcessingOfRawData p = new ProcessingOfRawData(lines);
        Alg alg = new Alg(p, "src/output.txt");
        alg.run();
//        Map<String,String> m = new HashMap<>();
//        m.put("1", "2");
//        System.out.println(m.containsKey("4"));
    }

}
