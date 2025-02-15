package pkg1;

import java.util.*;

// Class representing a formal grammar
class Grammar {
    private final Set<String> VN = new HashSet<>(Arrays.asList("S", "D", "R")); // Set of non-terminal symbols
    private final Set<Character> VT = new HashSet<>(Arrays.asList('a', 'b', 'c')); // Set of terminal symbols
    private final Map<String, List<String>> P = new HashMap<>(); // Production rules
    private final String startSymbol = "S"; // Start symbol

    public Grammar() {
        // Defining production rules
        P.put("S", Arrays.asList("aS", "cD"));
        P.put("D", Arrays.asList("bR"));
        P.put("R", Arrays.asList("aR", "b", "cS"));
    }

    // Generates a valid string based on the grammar rules
    public String generateString() {
        Random random = new Random();
        String current = startSymbol;

        // Apply production rules until only terminal symbols remain
        while (!current.chars().allMatch(c -> VT.contains((char) c))) {
            StringBuilder next = new StringBuilder();
            for (char c : current.toCharArray()) {
                String symbol = String.valueOf(c);
                if (VN.contains(symbol) && P.containsKey(symbol)) {
                    List<String> productions = P.get(symbol);
                    next.append(productions.get(random.nextInt(productions.size())));
                } else {
                    next.append(symbol);
                }
            }
            current = next.toString();
        }
        return current;
    }

    // Converts the grammar to a finite automaton
    public FiniteAutomaton toFiniteAutomaton() {
        return new FiniteAutomaton();
    }
}

// Class representing a finite automaton
class FiniteAutomaton {
    private final Set<String> Q = new HashSet<>(Arrays.asList("S", "D", "R")); // Set of states
    private final Set<Character> Sigma = new HashSet<>(Arrays.asList('a', 'b', 'c')); // Input alphabet
    private final Map<String, Map<Character, String>> delta = new HashMap<>(); // Transition function
    private final String q0 = "S"; // Initial state
    private final Set<String> F = new HashSet<>(Collections.singletonList("R")); // Final states

    public FiniteAutomaton() {
        // Defining transitions
        delta.put("S", Map.of('a', "S", 'c', "D"));
        delta.put("D", Map.of('b', "R"));
        delta.put("R", Map.of('a', "R", 'b', "R", 'c', "S"));
    }

    // Checks if a given string belongs to the language accepted by the automaton
    public boolean stringBelongToLanguage(String input) {
        String currentState = q0;
        for (char c : input.toCharArray()) {
            if (delta.containsKey(currentState) && delta.get(currentState).containsKey(c)) {
                currentState = delta.get(currentState).get(c);
            } else {
                return false; // Reject if transition is undefined
            }
        }
        return F.contains(currentState); // Accept if final state is reached
    }
}

