package main.ava;

import javafx.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by slava on 18.11.2017.
 */
public class ProcessingOfRawData {
    private Entity entity;
    private String line;
    private List<Pair<String, String>> rules;

    public ProcessingOfRawData(List<String> lines) {
        this.entity = new Entity();
        String prefix;
        lines.removeIf(s -> s.length() == 0);
        if (lines.size() != 5) {
            System.err.println("Присутствуют лишние входные данные");
            System.exit(1);
        }
        int counter = 0;
        for (String line : lines) {
            prefix = line.substring(0, line.indexOf('='));
            switch (prefix.trim().toLowerCase()) {
                case "line":
                    this.entity.setLine(line.substring(line.indexOf('{') + 1, line.indexOf('}')));
                    counter++;
                    break;
                case "a":
                    this.entity.setAlphabet(line.substring(line.indexOf('{') + 1, line.indexOf('}')).split(","));
                    counter++;
                    break;
                case "x":
                    this.entity.setVariables(line.substring(line.indexOf('{') + 1, line.indexOf('}')).split(","));
                    counter++;
                    break;
                case "r":
                    this.entity.setRules(line.substring(line.indexOf('{') + 1, line.indexOf('}')).split(","));
                    counter++;
                    break;
                case "a1":
                    this.entity.setAxioms(line.substring(line.indexOf('{') + 1, line.indexOf('}')).split(","));
                    counter++;
                    break;

                default:
                    System.err.println("Некорректные входные данные");
                    System.exit(1);
                    break;
            }
        }
        if (counter != 5) {
            System.err.println("На вход поданы не все блоки");
            System.exit(1);
        }
        checkLine();
        checkRules();
        this.line = entity.getLine();
        this.rules = generateRules();
    }

    public String[] getVariables(){
        return entity.getVariables();
    }

    public String getLine() {
        return line;
    }

    public List<Pair<String, String>> getRules() {
        return rules;
    }

    public String[] getAxioms(){
        return this.entity.getAxioms();
    }

    private List<Pair<String, String>> generateRules() {
        List<Pair<String, String>> rules = new ArrayList<>();
        String[] rules1 = entity.getRules();
        String[] split;
        for (String s : rules1) {
            split = s.split("->");
            rules.add(new Pair(split[0], split[1]));
        }
        return rules;
    }

    private void checkRules() {
        boolean flag;
        for (int i = 0; i < entity.getVariables().length; i++) {
            flag = false;
            for (int j = 0; j < entity.getRules().length; j++) {
                if (entity.getRules()[j].contains(entity.getVariables()[i])) {
                    flag = true;
                }
            }
            if (!flag) {
                System.err.println("На входе присутствуют лишние переменные");
                System.exit(1);
            }
        }
    }

    private void checkLine() {
        char[] chars = entity.getLine().toCharArray();
        Set<String> setOfLetters = new HashSet<>();
        for (int i = 0; i < chars.length; i++) {
            setOfLetters.add(String.valueOf(chars[i]));
        }
        HashSet<String> setOfAlphabet = Arrays.stream(this.entity.getAlphabet()).collect(Collectors.toCollection(HashSet::new));
        setOfLetters.removeAll(setOfAlphabet);
        if (setOfLetters.size() != 0) {
            System.err.println("Ошибка. В ленте присутств символы вне алфавита!");
            System.exit(1);
        }
    }

    private class Entity {
        private String line;
        private String[] alphabet;
        private String[] variables;
        private String[] rules;
        private String[] axioms;
         String getLine() {
            return line;
        }

        public String[] getAxioms() {
            return axioms;
        }

        public void setAxioms(String[] axioms) {
            this.axioms = axioms;
        }

        void setLine(String line) {
            this.line = line;
        }

         String[] getAlphabet() {
            return alphabet;
        }

         void setAlphabet(String[] alphabet) {
            this.alphabet = alphabet;
        }

         String[] getVariables() {
            return variables;
        }

         void setVariables(String[] variables) {
            this.variables = variables;
        }

         String[] getRules() {
            return rules;
        }

         void setRules(String[] rules) {
            this.rules = rules;
        }
    }
}
