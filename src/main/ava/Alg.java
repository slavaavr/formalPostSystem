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
        Map<String, List<String>> variableToNumbersForComplexTypeTemp = new LinkedHashMap<>();
        Character c_line;
        Character c_rule;
        StringBuilder lineOfNumbers = null;
        StringBuilder tempLineOfNumbersForComplexType = new StringBuilder();
        int tempIndexLineOfNumbersForComplexType;
        boolean allocateMemoryForNumbersStringFlag;
        Pair<String, String> rule;
        int currentElementRuleIndex;
        int offsetTheLine = 0;
        int flashback;
        for (int i = 0; i < rules.size(); i++) {
            rule = rules.get(i);
            variableToNumbers.clear();
            allocateMemoryForNumbersStringFlag = true;
            currentElementRuleIndex = 0;
            flashback = 0;
            newRule:
            for (int j = 0; j < line.length(); j++) {
                c_line = line.charAt(j);
                c_rule = rule.getKey().charAt(currentElementRuleIndex);
                if (allocateMemoryForNumbersStringFlag) {
                    lineOfNumbers = new StringBuilder();
                    allocateMemoryForNumbersStringFlag = false;
                }
                if (isItVariable(c_rule) && !variableToNumbers.containsKey(Character.toString(c_rule))) {
                    variableToNumbers.put(Character.toString(c_rule), new HashSet<>());
                }

                if (isItAxiom(c_line) && isItVariable(c_rule)) { // - 1 x
                    lineOfNumbers.append(c_line);
                } else if (!isItAxiom(c_line) && isItVariable(c_rule)) { // - / x
                    currentElementRuleIndex++;
                    if (currentElementRuleIndex < rule.getKey().length()) {
                        c_rule = rule.getKey().charAt(currentElementRuleIndex); // next c_rule
                        if (isItVariable(c_rule)) {
                            currentElementRuleIndex--;
                            while(!isItVariable(c_rule = rule.getKey().charAt(currentElementRuleIndex)) || (currentElementRuleIndex < rule.getKey().length())){
                                if (!variableToNumbers.containsKey(Character.toString(c_rule))) {
                                    variableToNumbers.put(Character.toString(c_rule), new HashSet<>());
                                }
                                if(!variableToNumbersForComplexTypeTemp.containsKey(Character.toString(c_rule))){
                                    variableToNumbersForComplexTypeTemp.put(Character.toString(c_rule), new ArrayList<>());
                                }
                                currentElementRuleIndex++;
                            }
                            tempIndexLineOfNumbersForComplexType = j - 1;
                            while(!isItAxiom(c_line = line.charAt(tempIndexLineOfNumbersForComplexType)) || (tempIndexLineOfNumbersForComplexType > 0)){
                                tempLineOfNumbersForComplexType.append(c_line);
                                tempIndexLineOfNumbersForComplexType--;
                            }
                            tempLineOfNumbersForComplexType.reverse();
                            if(variableToNumbersForComplexTypeTemp.size() > tempLineOfNumbersForComplexType.length()){
                                System.err.println("Ошибка в правиле: " + rule.getKey());
                                System.exit(1);
                            }
                            // шаманство/
                            List<Integer> tempList = new ArrayList<>();
                            Set<String> set = new LinkedHashSet<>();
                            for (int k = 0; k < variableToNumbersForComplexTypeTemp.size(); k++) {
                                tempList.add(k);
                            }

                            // /шаманство
                        } else {
                            if (c_line != c_rule) {
                                break;
                            }
                            variableToNumbers.get(Character.toString(c_rule)).add(lineOfNumbers.toString());
                            allocateMemoryForNumbersStringFlag = true;
                            currentElementRuleIndex++;
                        }
                    }
                } else if (!isItVariable(c_rule)) { // - 1/ 1/
                    if (c_line != c_rule) {
                        break;
                    }
                    if (lineOfNumbers.length() > 0) {
                        variableToNumbers.get(Character.toString(c_rule)).add(lineOfNumbers.toString());
                        allocateMemoryForNumbersStringFlag = true;
                    }
                    currentElementRuleIndex++;
                }
                if ((j == line.length() - 1) || (currentElementRuleIndex > rule.getKey().length() - 1)) {
                    for (Set<String> values : variableToNumbers.values()) {
                        if (values.size() != 1) {
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
