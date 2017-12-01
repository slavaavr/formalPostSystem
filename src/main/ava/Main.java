package main.ava;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


public class Main {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("src/input.txt"), StandardCharsets.UTF_8);
        ProcessingOfRawData p = new ProcessingOfRawData(lines);
        Alg alg = new Alg(p, "src/output.txt");
//        alg.run();
        Integer i=0;
        foo(i);
        System.out.println(i);
        LinkedHashSet<String> s = new LinkedHashSet<>();
        List<Integer> l = new ArrayList<>();

    }
    static void foo(Integer i){
        i++;
    }

}


/**
 line = {1+11111111/1111=}
 A = {1,+,/,=}
 X = {x,y}
 A1 = {1}
 R = {1+yx/x=->1+y/x=1}

 */