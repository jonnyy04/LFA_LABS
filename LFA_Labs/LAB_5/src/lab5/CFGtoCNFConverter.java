package lab5;

import java.util.*;
import java.util.stream.Collectors;

public class CFGtoCNFConverter {
    private Set<String> nonTerminals;
    private Set<String> terminals;
    private Map<String, List<List<String>>> productions;
    private String startSymbol;

    public CFGtoCNFConverter(Set<String> nonTerminals, Set<String> terminals,
                             Map<String, List<List<String>>> productions, String startSymbol) {
        this.nonTerminals = new HashSet<>(nonTerminals);
        this.terminals = new HashSet<>(terminals);
        this.productions = new HashMap<>();
        productions.forEach((k, v) -> this.productions.put(k, new ArrayList<>(v)));
        this.startSymbol = startSymbol;
    }

    public void convertToCNF() {
        System.out.println("Original Grammar:");
        printGrammar();

        eliminateEpsilonProductions();
        System.out.println("\nAfter eliminating ε productions:");
        printGrammar();

        eliminateRenaming();
        System.out.println("\nAfter eliminating renaming:");
        printGrammar();

        eliminateInaccessibleSymbols();
        System.out.println("\nAfter eliminating inaccessible symbols:");
        printGrammar();

        eliminateNonProductiveSymbols();
        System.out.println("\nAfter eliminating non-productive symbols:");
        printGrammar();

        convertToChomskyNormalForm();
        System.out.println("\nFinal CNF:");
        printGrammar();
    }

    private void eliminateEpsilonProductions() {
        // Step 1: Find all nullable non-terminals
        Set<String> nullable = new HashSet<>();
        boolean changed;
        do {
            changed = false;
            for (Map.Entry<String, List<List<String>>> entry : productions.entrySet()) {
                String nt = entry.getKey();
                for (List<String> production : entry.getValue()) {
                    if (production.equals(Arrays.asList("ε")) || production.stream().allMatch(nullable::contains)) {
                        if (!nullable.contains(nt)) {
                            nullable.add(nt);
                            changed = true;
                        }
                    }
                }
            }
        } while (changed);

        // Step 2: For each production, generate all possible combinations without nullable symbols
        Map<String, List<List<String>>> newProductions = new HashMap<>();
        for (Map.Entry<String, List<List<String>>> entry : productions.entrySet()) {
            String nt = entry.getKey();
            List<List<String>> newProdList = new ArrayList<>();

            for (List<String> production : entry.getValue()) {
                if (production.equals(Arrays.asList("ε"))) {
                    continue; // Skip the ε production itself
                }

                // Find all nullable positions in this production
                List<Integer> nullablePositions = new ArrayList<>();
                for (int i = 0; i < production.size(); i++) {
                    if (nullable.contains(production.get(i))) {
                        nullablePositions.add(i);
                    }
                }

                // Generate all possible combinations
                int n = nullablePositions.size();
                for (int mask = 0; mask < (1 << n); mask++) {
                    List<String> newProd = new ArrayList<>();
                    for (int i = 0; i < production.size(); i++) {
                        if (!nullable.contains(production.get(i)) || (mask & (1 << nullablePositions.indexOf(i))) != 0) {
                            newProd.add(production.get(i));
                        }
                    }
                    if (!newProd.isEmpty()) {
                        newProdList.add(newProd);
                    }
                }
            }

            // Remove duplicates
            newProdList = newProdList.stream().distinct().collect(Collectors.toList());
            newProductions.put(nt, newProdList);
        }

        productions = newProductions;

        // If start symbol is nullable, add new start symbol
        if (nullable.contains(startSymbol)) {
            String newStart = startSymbol + "'";
            nonTerminals.add(newStart);
            productions.put(newStart, Arrays.asList(
                    Arrays.asList(startSymbol),
                    Arrays.asList("ε")
            ));
            startSymbol = newStart;
        } else {
            // Remove any remaining ε productions from non-start symbols
            for (List<List<String>> prods : productions.values()) {
                prods.removeIf(p -> p.equals(Arrays.asList("ε")));
            }
        }
    }

    private void eliminateRenaming() {
        boolean changed;
        do {
            changed = false;
            Map<String, List<List<String>>> newProductions = new HashMap<>();

            for (String nt : productions.keySet()) {
                List<List<String>> newProdList = new ArrayList<>();

                for (List<String> production : productions.get(nt)) {
                    if (production.size() == 1 && nonTerminals.contains(production.get(0))) {
                        // This is a renaming production A → B
                        String rhsNT = production.get(0);
                        for (List<String> rhsProd : productions.get(rhsNT)) {
                            if (!newProdList.contains(rhsProd)) {
                                newProdList.add(rhsProd);
                            }
                        }
                        changed = true;
                    } else {
                        if (!newProdList.contains(production)) {
                            newProdList.add(production);
                        }
                    }
                }

                newProductions.put(nt, newProdList);
            }

            productions = newProductions;
        } while (changed);
    }

    private void eliminateInaccessibleSymbols() {
        Set<String> accessible = new HashSet<>();
        accessible.add(startSymbol);
        boolean changed;

        do {
            changed = false;
            Set<String> newAccessible = new HashSet<>(accessible);

            for (String nt : accessible) {
                for (List<String> production : productions.getOrDefault(nt, Collections.emptyList())) {
                    for (String symbol : production) {
                        if (nonTerminals.contains(symbol) && !newAccessible.contains(symbol)) {
                            newAccessible.add(symbol);
                            changed = true;
                        }
                    }
                }
            }

            accessible = newAccessible;
        } while (changed);

        // Remove non-terminals that aren't accessible
        nonTerminals.retainAll(accessible);
        productions.keySet().retainAll(accessible);
    }

    private void eliminateNonProductiveSymbols() {
        Set<String> productive = new HashSet<>();
        boolean changed;

        // First, find all non-terminals that directly produce terminal strings
        do {
            changed = false;
            for (Map.Entry<String, List<List<String>>> entry : productions.entrySet()) {
                String nt = entry.getKey();
                if (productive.contains(nt)) continue;

                for (List<String> production : entry.getValue()) {
                    boolean allProductive = true;
                    for (String symbol : production) {
                        if (nonTerminals.contains(symbol) && !productive.contains(symbol)) {
                            allProductive = false;
                            break;
                        }
                    }
                    if (allProductive) {
                        productive.add(nt);
                        changed = true;
                        break;
                    }
                }
            }
        } while (changed);

        // Remove non-productive symbols
        nonTerminals.retainAll(productive);
        productions.keySet().retainAll(productive);

        // Remove productions that contain non-productive symbols
        Map<String, List<List<String>>> newProductions = new HashMap<>();
        for (Map.Entry<String, List<List<String>>> entry : productions.entrySet()) {
            String nt = entry.getKey();
            List<List<String>> validProds = new ArrayList<>();

            for (List<String> production : entry.getValue()) {
                boolean isValid = true;
                for (String symbol : production) {
                    if (nonTerminals.contains(symbol) && !productive.contains(symbol)) {
                        isValid = false;
                        break;
                    }
                }
                if (isValid) {
                    validProds.add(production);
                }
            }

            if (!validProds.isEmpty()) {
                newProductions.put(nt, validProds);
            }
        }

        productions = newProductions;
    }

    private void convertToChomskyNormalForm() {
        int terminalCounter = 1;
        int newVarCounter = 1;

        // Step 1: Introduce new non-terminals for terminals
        Map<String, String> terminalToNonTerminal = new HashMap<>();

        for (String terminal : terminals) {
            String newNT = "Y" + terminalCounter++; // Creează Y1, Y2, ...
            terminalToNonTerminal.put(terminal, newNT);
            nonTerminals.add(newNT);
            productions.put(newNT, Arrays.asList(Arrays.asList(terminal)));
        }

        // Replace terminals in productions (except those in productions of length 1)
        Map<String, List<List<String>>> newProductions = new HashMap<>();
        for (Map.Entry<String, List<List<String>>> entry : productions.entrySet()) {
            String nt = entry.getKey();
            List<List<String>> newProdList = new ArrayList<>();

            for (List<String> production : entry.getValue()) {
                if (production.size() == 1 && terminals.contains(production.get(0))) {
                    newProdList.add(production); // Keep A → a
                } else {
                    List<String> newProd = new ArrayList<>();
                    for (String symbol : production) {
                        if (terminals.contains(symbol)) {
                            newProd.add(terminalToNonTerminal.get(symbol));
                        } else {
                            newProd.add(symbol);
                        }
                    }
                    newProdList.add(newProd);
                }
            }

            newProductions.put(nt, newProdList);
        }

        productions = newProductions;

        // Step 2: Break down productions with more than 2 symbols
        boolean changed;
        do {
            changed = false;
            newProductions = new HashMap<>();

            for (Map.Entry<String, List<List<String>>> entry : productions.entrySet()) {
                String nt = entry.getKey();
                List<List<String>> newProdList = new ArrayList<>();

                for (List<String> production : entry.getValue()) {
                    if (production.size() <= 2) {
                        newProdList.add(production);
                    } else {
                        changed = true;
                        String currentNT = nt;

                        for (int i = 0; i < production.size() - 2; i++) {
                            String newNT = "X" + newVarCounter++;
                            nonTerminals.add(newNT);

                            List<String> newProd = new ArrayList<>();
                            newProd.add(production.get(i));
                            newProd.add(newNT);

                            newProductions.computeIfAbsent(currentNT, k -> new ArrayList<>()).add(newProd);
                            currentNT = newNT;
                        }

                        // Add the last pair
                        List<String> lastProd = new ArrayList<>();
                        lastProd.add(production.get(production.size() - 2));
                        lastProd.add(production.get(production.size() - 1));
                        newProductions.computeIfAbsent(currentNT, k -> new ArrayList<>()).add(lastProd);
                    }
                }

                if (!newProdList.isEmpty()) {
                    newProductions.computeIfAbsent(nt, k -> new ArrayList<>()).addAll(newProdList);
                }
            }

            productions = newProductions;
        } while (changed);
    }


    public void printGrammar() {
        System.out.println("Non-terminals: " + nonTerminals);
        System.out.println("Terminals: " + terminals);
        System.out.println("Start symbol: " + startSymbol);
        System.out.println("Productions:");
        for (Map.Entry<String, List<List<String>>> entry : productions.entrySet()) {
            System.out.print(entry.getKey() + " → ");
            for (int i = 0; i < entry.getValue().size(); i++) {
                if (i > 0) System.out.print(" | ");
                System.out.print(String.join(" ", entry.getValue().get(i)));
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        // Example usage with the provided grammar
        Set<String> nonTerminals = new HashSet<>(Arrays.asList("S", "A", "B", "C", "E"));
        Set<String> terminals = new HashSet<>(Arrays.asList("a", "b"));

        Map<String, List<List<String>>> productions = new HashMap<>();
        productions.put("S", Arrays.asList(
                Arrays.asList("b", "A"),
                Arrays.asList("B")
        ));
        productions.put("A", Arrays.asList(
                Arrays.asList("a"),
                Arrays.asList("a", "S"),
                Arrays.asList("b", "A", "a", "A", "b")
        ));
        productions.put("B", Arrays.asList(
                Arrays.asList("A", "C"),
                Arrays.asList("b", "S"),
                Arrays.asList("a", "A", "a")
        ));
        productions.put("C", Arrays.asList(
                Arrays.asList("ε"),
                Arrays.asList("A", "B")
        ));
        productions.put("E", Arrays.asList(
                Arrays.asList("B", "A")
        ));

        CFGtoCNFConverter converter = new CFGtoCNFConverter(nonTerminals, terminals, productions, "S");
        converter.convertToCNF();
    }
}