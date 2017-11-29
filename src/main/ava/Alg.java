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
        Map<String, Set<String>> variableToNumbers = new HashMap<>();
        Character c_line;
        Character c_rule;
        StringBuilder lineOfNumbers = null;
        boolean allocateMemoryForNumbersStringFlag;
        Pair<String, String> rule;
        int currentElementIndexForRule;
        int offsetTheLine = 0;
        int controlPointIndex;
        boolean isItSafetyToSaveControlPointIndex;
        for (int i = 0; i < rules.size(); i++) {
            rule = rules.get(i);
            variableToNumbers.clear();
            isItSafetyToSaveControlPointIndex = true;
            allocateMemoryForNumbersStringFlag = true;
            controlPointIndex = 0;
            currentElementIndexForRule = 0;
            newRule:
            for (int j = 0; j < line.length(); j++) {
                c_line = line.charAt(j);
                c_rule = rule.getKey().charAt(currentElementIndexForRule);
                if (allocateMemoryForNumbersStringFlag) {
                    lineOfNumbers = new StringBuilder();
                    allocateMemoryForNumbersStringFlag = false;
                }
                if (isItVariable(c_rule) && !variableToNumbers.containsKey(Character.toString(c_rule))) {
                    variableToNumbers.put(Character.toString(c_rule), new HashSet<>());
                }

                if (isItAxiom(c_line) && isItVariable(c_rule)) { // - 1 x
                    isItSafetyToSaveControlPointIndex = false;
                    lineOfNumbers.append(c_line);
                } else if (!isItAxiom(c_line) && isItVariable(c_rule)) { // - / x
                    currentElementIndexForRule++;
                    c_rule = rule.getKey().charAt(currentElementIndexForRule);
                    if(isItVariable(c_rule)){

                    } else {
                        if(c_line != c_rule){
                            break;
                        }
                        currentElementIndexForRule++;
                        allocateMemoryForNumbersStringFlag = true;
                    }
                } else if (!isItVariable(c_rule)) { // - 1/ 1/
                    if (c_line != c_rule) {
                        break;
                    }
                    if (isItSafetyToSaveControlPointIndex) {
                        controlPointIndex = j;
                    }
                    if (lineOfNumbers.length() > 0) {
                        variableToNumbers.get(Character.toString(c_rule)).add(lineOfNumbers.toString());
                        allocateMemoryForNumbersStringFlag = true;
                    }
                    currentElementIndexForRule++;
                }
                if (j == line.length() - 1) {
                    for (Set<String> values : variableToNumbers.values()) {
                        if(values.size() != 1){
                            break newRule;
                        }
                    }
                    String leftPartOfRule = rule.getKey();
                    String rightPartOfRule = rule.getValue();
                    for (Map.Entry<String, Set<String>> setEntry : variableToNumbers.entrySet()) {
                        leftPartOfRule = leftPartOfRule.replaceAll(setEntry.getKey(), setEntry.getValue().toArray()[0].toString());
                        rightPartOfRule = rightPartOfRule.replaceAll(setEntry.getKey(), setEntry.getValue().toArray()[0].toString());
                    }
                    line = line.replace(leftPartOfRule, rightPartOfRule);
                    i = -1;
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
