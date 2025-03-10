package lab2;

import java.util.*;

public class Main {
    public static void main(String[] args) {

        GrammarTypeChecker checker = new GrammarTypeChecker();
        String type = checker.determineGrammarType();
        System.out.println("Grammar type: " + type);


        System.out.println("Regular Grammar (RG) for the given FA:");
        FAtoRGConverter.generateRegularGrammar();


        Set<String> states = new HashSet<>(Arrays.asList("q0", "q1", "q2"));
        Set<Character> alphabet = new HashSet<>(Arrays.asList('a', 'b', 'c'));
        String startState = "q0";
        Set<String> finalStates = new HashSet<>(Collections.singleton("q2"));

        FiniteAutomaton ndfa = new FiniteAutomaton(states, alphabet, startState, finalStates);
        ndfa.addTransition("q0", 'a', "q0");
        ndfa.addTransition("q0", 'a', "q1");
        ndfa.addTransition("q1", 'c', "q0");
        ndfa.addTransition("q1", 'b', "q1");
        ndfa.addTransition("q1", 'a', "q2");
        ndfa.addTransition("q2", 'a', "q2");

        System.out.println("NDFA Transitions:");
        ndfa.printTransitions();

        System.out.println("\nIs Deterministic? " + ndfa.isDeterministic());

        FiniteAutomaton dfa = ndfa.convertToDFA();
        System.out.println("\nDFA Transitions:");
        dfa.printTransitions();

    }
}
