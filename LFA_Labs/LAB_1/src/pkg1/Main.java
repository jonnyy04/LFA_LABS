package pkg1;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Grammar grammar = new Grammar();
        System.out.println("Generated Strings:");
        for (int i = 0; i < 5; i++) {
            System.out.println(grammar.generateString());
        }

        FiniteAutomaton fa = grammar.toFiniteAutomaton();
        System.out.println("\nString recognition test:");
        List<String> testStrings = Arrays.asList("aaacbcaaaacbaaccbccbab", "aacb", "cbcaccaaaaaacbab", "cbcacbcaaaaaacbab", "aaaa");
        for (String test : testStrings) {
            System.out.println("Does '" + test + "' belong to the language? " + fa.stringBelongToLanguage(test));
        }
    }
}