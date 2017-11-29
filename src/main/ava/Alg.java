package main.ava;

import javafx.util.Pair;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


public class Alg {
    private String line;
    private List<Pair<String, String>> rules;
    private String[] axioms;
    private String[] variables;
    private FileWriter fw;

    public Alg(ProcessingOfRawData p, String outputFile) {
        this.line = p.getLine();
        this.rules = p.getRules();
        this.axioms = p.getAxioms();
        this.variables = p.getVariables();
        try {
            fw = new FileWriter(outputFile, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() throws IOException {
        Map<String, Set<String>> map_var_val = new HashMap<>();
        Character c_line;
        Character c_rule;
        StringBuilder valueOfVariable;
        int i, j, offset;

        for (int k = 0; k < rules.size(); k++) {
            Pair<String, String> rule = rules.get(k);
            j = 0;
            offset = 0;
            newRule:
            for (i = 0; i < rule.getKey().length(); i++) {
                c_rule = rule.getKey().charAt(i);
                if (isItVariable(c_rule)) {
                    if (!map_var_val.containsKey(Character.toString(c_rule)))
                        map_var_val.put(Character.toString(c_rule), new HashSet<>());
                    valueOfVariable = new StringBuilder();
                    for (; j < this.line.length(); j++) {
                        c_line = this.line.charAt(j);
                        if (isItAxiom(c_line)) {
                            valueOfVariable.append(c_line);
                            if (j == this.line.length() - 1) {
                                map_var_val.get(Character.toString(c_rule)).add(valueOfVariable.toString());
                            }
                        } else if (j == offset) {
                            map_var_val.clear();
                            break newRule;
                        } else {
                            if (valueOfVariable.length() != 0)
                                map_var_val.get(Character.toString(c_rule)).add(valueOfVariable.toString());
                            break;
                        }
                    }
                } else {
                    if (c_rule != this.line.charAt(j)) {
                        map_var_val.clear();
                        break;
                    } else {
                        j++;
                    }
                }
                if (i == rule.getKey().length() - 1) {
                    boolean flag = false;
                    for (Set<String> set : map_var_val.values()) {
                        if (set.size() != 1) {
                            map_var_val.clear();
                            i = -1;
                            offset++;
                            j = offset;
                            flag = true;
                            break;
                        }
                    }
                    if (!flag) {
                        String key = rule.getKey();
                        String value = rule.getValue();
                        for (Map.Entry<String, Set<String>> setEntry : map_var_val.entrySet()) {
                            key = key.replaceAll(setEntry.getKey(), setEntry.getValue().toArray()[0].toString());
                            value = value.replaceAll(setEntry.getKey(), setEntry.getValue().toArray()[0].toString());
                        }
                        this.line = this.line.replace(key, value);
                        fw.write(this.line + "\n");
                        fw.flush();
                        map_var_val.clear();
                        k--;
                    }
                }
            }
        }
    }

    private boolean isItAxiom(Character c) {
        for (String axiom : axioms) {
            if (axiom.equals(Character.toString(c)))
                return true;
        }
        return false;
    }

    private boolean isItVariable(Character c) {
        for (String variable : variables) {
            if (variable.equals(Character.toString(c)))
                return true;
        }
        return false;
    }
}
