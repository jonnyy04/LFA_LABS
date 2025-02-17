package pkg1;

import java.util.*;

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

    public String generateString() {
        Random random = new Random();
        String current = startSymbol;

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

    public FiniteAutomaton toFiniteAutomaton() {
        return new FiniteAutomaton();
    }
}

