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
        Map<String, Set<String>> variableToNumbers = new LinkedHashMap<>();
        List<Map<String, List<String>>> variableToNumbersForComplexTypeTemp = new ArrayList<>();
        Character c_line;
        Character c_rule;
        StringBuilder lineOfNumbers = null;
        StringBuilder tempLineOfNumbersForComplexType = new StringBuilder();
        int tempIndexLineOfNumbersForComplexType;
        boolean allocateMemoryForNumbersStringFlag;
        Pair<String, String> rule;
        int currentElementRuleIndex;
        int offsetTheLine;
        for (int i = 0; i < rules.size(); i++) {
            rule = rules.get(i);
            variableToNumbers.clear();
            variableToNumbersForComplexTypeTemp.clear();
            allocateMemoryForNumbersStringFlag = true;
            currentElementRuleIndex = 0;
            offsetTheLine = 0;
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
                            Map<String, List<String>> var_value = new LinkedHashMap<>();
                            currentElementRuleIndex--;
                            while ((currentElementRuleIndex < rule.getKey().length()) && isItVariable(c_rule = rule.getKey().charAt(currentElementRuleIndex))) {
                                if (!variableToNumbers.containsKey(Character.toString(c_rule))) {
                                    variableToNumbers.put(Character.toString(c_rule), new HashSet<>());
                                }
                                if (!var_value.containsKey(Character.toString(c_rule))) {
                                    var_value.put(Character.toString(c_rule), new ArrayList<>());
                                }
                                currentElementRuleIndex++;
                            }
                            tempIndexLineOfNumbersForComplexType = j - 1;
                            while ((tempIndexLineOfNumbersForComplexType >= offsetTheLine) && isItAxiom(c_line = line.charAt(tempIndexLineOfNumbersForComplexType))) {
                                tempLineOfNumbersForComplexType.append(c_line);
                                tempIndexLineOfNumbersForComplexType--;
                            }
                            tempLineOfNumbersForComplexType.reverse();
                            if (var_value.size() > tempLineOfNumbersForComplexType.length()) {
                               System.err.println("Ошибка в правиле (количество переменных больше количества символов): " + rule.getKey() + " | " + tempLineOfNumbersForComplexType);
                                break;
                            }
                            variableToNumbersForComplexTypeTemp.add(var_value);
                            // шаманство/
                            if(rule.getKey().charAt(currentElementRuleIndex) != line.charAt(j)){
                                break;
                            }
                            Integer[] tempArray = new Integer[var_value.size()];
                            Set<String> set = new LinkedHashSet<>();
                            StringBuilder ans = new StringBuilder(); // format like - 1|1|111
                            int t, lastValue;

                            for (int k = 0; k < tempArray.length; k++) {
                                tempArray[k] = k;
                            }
                            t = tempArray.length - 1;
                            lastValue = tempLineOfNumbersForComplexType.length() - 1;
                            do {
                                ans.delete(0, ans.length());
                                for (int k = 0; k < tempArray.length - 1; k++) {
                                    ans.append(tempLineOfNumbersForComplexType.substring(tempArray[k], tempArray[k + 1])).append(" ");
                                    if (k == tempArray.length - 2) {
                                        if (tempArray[0] > 0) {
                                            ans.append(tempLineOfNumbersForComplexType.substring(tempArray[k + 1])).append(tempLineOfNumbersForComplexType.substring(0, tempArray[0]));
                                        } else {
                                            ans.append(tempLineOfNumbersForComplexType.substring(tempArray[k + 1]));
                                        }
                                    }
                                }
                                if (t>=0 && tempArray[t] < lastValue) {
                                    tempArray[t]++;
                                    if (tempArray[t] == lastValue) {
                                        lastValue = tempArray[t] - 1;
                                        t--;
                                    }
                                }
                            } while (set.add(ans.toString()));
                            for (String s : set) {
                                t = 0;
                                String[] split = s.split(" ");
                                for (String key : var_value.keySet()) {
                                    var_value.get(key).add(split[t]);
                                    t++;
                                }
                            }
                            tempLineOfNumbersForComplexType.delete(0, tempLineOfNumbersForComplexType.length());
                            for (String key : var_value.keySet()) {
                                variableToNumbers.get(key).add(var_value.get(key).get(0));
                            }
                            lineOfNumbers.delete(0, lineOfNumbers.length());
                            tempLineOfNumbersForComplexType.delete(0, tempLineOfNumbersForComplexType.length());
                            currentElementRuleIndex++;
                            // /шаманство
                        } else {
                            if (c_line != c_rule || lineOfNumbers.length() == 0) {
                                break;
                            }
                            variableToNumbers.get(Character.toString(rule.getKey().charAt(currentElementRuleIndex - 1))).add(lineOfNumbers.toString());
                            allocateMemoryForNumbersStringFlag = true;
                            currentElementRuleIndex++;
                        }
                    }
                } else if (!isItVariable(c_rule)) { // - 1/ 1/
                    if (c_line != c_rule) {
                        break;
                    }
                    if (lineOfNumbers.length() > 0) {
                        variableToNumbers.get(Character.toString(rule.getKey().charAt(currentElementRuleIndex - 1))).add(lineOfNumbers.toString());
                        allocateMemoryForNumbersStringFlag = true;
                    }
                    currentElementRuleIndex++;
                }
                if ((j == line.length() - 1) || (currentElementRuleIndex > rule.getKey().length() - 1)) {
                    if(currentElementRuleIndex == rule.getKey().length() - 1 && lineOfNumbers.length() > 0){
                        variableToNumbers.get(Character.toString(c_rule)).add(lineOfNumbers.toString());
                        tempLineOfNumbersForComplexType.delete(0, tempLineOfNumbersForComplexType.length());
                    }
                    boolean rightRuleFlag = false;
                    Map<String, List<String>> var_val;
                    while (!(rightRuleFlag = isItRightRule(variableToNumbers)) && variableToNumbersForComplexTypeTemp.size() > 0) {
                        var_val = variableToNumbersForComplexTypeTemp.remove(variableToNumbersForComplexTypeTemp.size() - 1);
                        while (doesMapHaveElementsInList(var_val)) {
                            for (String key : var_val.keySet()) {
                                if (var_val.get(key).size() > 1) {
                                    variableToNumbers.get(key).remove(var_val.get(key).remove(0));
                                    variableToNumbers.get(key).add(var_val.get(key).get(0));
                                }
                            }
                            if (rightRuleFlag = isItRightRule(variableToNumbers)) {
                                break;
                            }
                        }
                    }
                    if (!rightRuleFlag && offsetTheLine < line.length()) {
                        j = offsetTheLine;
                        offsetTheLine++;
                        currentElementRuleIndex = 0;
                        variableToNumbers.clear();
                        variableToNumbersForComplexTypeTemp.clear();
                    } else if (rightRuleFlag) {
                        String leftPartOfRule = rule.getKey();
                        String rightPartOfRule = rule.getValue();
                        for (Map.Entry<String, Set<String>> setEntry : variableToNumbers.entrySet()) {
                            leftPartOfRule = leftPartOfRule.replaceAll(setEntry.getKey(), setEntry.getValue().toArray()[0].toString());
                            rightPartOfRule = rightPartOfRule.replaceAll(setEntry.getKey(), setEntry.getValue().toArray()[0].toString());
                        }
                        line = line.replace(leftPartOfRule, rightPartOfRule);
                        fw.write(line + '\n');
                        fw.flush();
                        i = -1;
                        break;
                    }
                }
            }
        }
    }

    private boolean doesMapHaveElementsInList(Map<String, List<String>> map) {
        for (List<String> val : map.values()) {
            if (val.size() > 1) {
                return true;
            }
        }
        return false;
    }

    private boolean isItRightRule(Map<String, Set<String>> variableToNumbers) {
        for (Set<String> set : variableToNumbers.values()) {
            if (set.size() != 1) {
                return false;
            }
        }
        return true;
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
