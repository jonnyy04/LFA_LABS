package pkg1;

import java.util.*;

class FiniteAutomaton {
    private final Set<String> Q = new HashSet<>(Arrays.asList("S", "D", "R")); // Set of states
    private final Set<Character> Sigma = new HashSet<>(Arrays.asList('a', 'b', 'c')); // Input alphabet
    private final Map<String, Map<Character, String>> delta = new HashMap<>(); // Transition function
    private final String q0 = "S"; // Initial state
    private final Set<String> F = new HashSet<>(Collections.singletonList("R")); // Final states

    public FiniteAutomaton() {
        delta.put("S", Map.of('a', "S", 'c', "D"));
        delta.put("D", Map.of('b', "R"));
        delta.put("R", Map.of('a', "R", 'b', "R", 'c', "S"));
    }

    public boolean stringBelongToLanguage(String input) {
        String currentState = q0;
        for (char c : input.toCharArray()) {
            if (delta.containsKey(currentState) && delta.get(currentState).containsKey(c)) {
                currentState = delta.get(currentState).get(c);
            } else {
                return false;
            }
        }
        return F.contains(currentState);
    }
}
