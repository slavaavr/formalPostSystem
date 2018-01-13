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

 line = {111111111/1111=}
 A = {1,/,.,=}
 X = {x,y,z}
 A1 = {1}
 R = {x/x=->/x=1,y/x=z->z.y}

 line = {11111/11=}
 A = {1,/,=}
 X = {x}
 A1 = {1}
 R = {x/x=->/x=1}

 */