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
        alg.run();
//        String s = "abcd";
//        System.out.println(s.substring(0,1));
//        List<Integer> l = new ArrayList<>();
//        l.add(1);
//        l.add(2);
//        Integer[] o = l.toArray(new Integer[l.size()]);
//        o[0] = 33;
//        System.out.println(o[0]);

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

 line = {1111/11=}
 A = {1,/,=}
 X = {x,y}
 A1 = {1}
 R = {yx/x=->y/x=1}

 */