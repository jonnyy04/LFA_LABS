package lab2;

import java.util.*;

class FiniteAutomaton {
    private Set<String> states;
    private Set<Character> alphabet;
    private Map<String, Map<Character, Set<String>>> transitions;
    private String startState;
    private Set<String> finalStates;

    public FiniteAutomaton(Set<String> states, Set<Character> alphabet, String startState, Set<String> finalStates) {
        this.states = states;
        this.alphabet = alphabet;
        this.startState = startState;
        this.finalStates = finalStates;
        this.transitions = new HashMap<>();
    }

    public void addTransition(String fromState, char symbol, String toState) {
        transitions.putIfAbsent(fromState, new HashMap<>());
        transitions.get(fromState).putIfAbsent(symbol, new HashSet<>());
        transitions.get(fromState).get(symbol).add(toState);
    }

    public boolean isDeterministic() {
        for (Map<Character, Set<String>> stateTransitions : transitions.values()) {
            for (Set<String> destinations : stateTransitions.values()) {
                if (destinations.size() > 1) {
                    return false;
                }
            }
        }
        return true;
    }

    public FiniteAutomaton convertToDFA() {
        Set<Set<String>> dfaStates = new HashSet<>();
        Queue<Set<String>> queue = new LinkedList<>();
        Map<Set<String>, String> stateNames = new HashMap<>();

        Set<String> startSet = epsilonClosure(Set.of(startState));
        dfaStates.add(startSet);
        queue.add(startSet);
        stateNames.put(startSet, setToString(startSet));

        Map<String, Map<Character, String>> dfaTransitions = new HashMap<>();

        while (!queue.isEmpty()) {
            Set<String> currentState = queue.poll();
            String currentStateName = stateNames.get(currentState);
            dfaTransitions.putIfAbsent(currentStateName, new HashMap<>());

            for (char symbol : alphabet) {
                Set<String> newState = new HashSet<>();
                for (String state : currentState) {
                    if (transitions.containsKey(state) && transitions.get(state).containsKey(symbol)) {
                        newState.addAll(transitions.get(state).get(symbol));
                    }
                }
                newState = epsilonClosure(newState);

                if (!dfaStates.contains(newState)) {
                    dfaStates.add(newState);
                    queue.add(newState);
                    stateNames.put(newState, setToString(newState));
                }
                String newStateName = stateNames.get(newState);
                dfaTransitions.get(currentStateName).put(symbol, newStateName);
            }
        }

        // Creăm mulțimea de stări finale pentru DFA
        Set<String> dfaFinalStates = new HashSet<>();
        for (Set<String> state : dfaStates) {
            for (String s : state) {
                if (finalStates.contains(s)) {
                    dfaFinalStates.add(stateNames.get(state));
                    break;
                }
            }
        }

        // Creăm noul DFA
        FiniteAutomaton dfa = new FiniteAutomaton(new HashSet<>(stateNames.values()), alphabet, setToString(startSet), dfaFinalStates);

        // Adăugăm efectiv tranzițiile în noul DFA
        for (String state : dfaTransitions.keySet()) {
            for (char symbol : dfaTransitions.get(state).keySet()) {
                dfa.addTransition(state, symbol, dfaTransitions.get(state).get(symbol));
            }
        }

        return dfa;
    }

    private Set<String> epsilonClosure(Set<String> states) {
        return states; // Nu avem tranziții epsilon
    }

    private String setToString(Set<String> stateSet) {
        return "{" + String.join(", ", stateSet) + "}";
    }

    public void printTransitions() {
        for (String state : transitions.keySet()) {
            for (char symbol : transitions.get(state).keySet()) {
                for (String dest : transitions.get(state).get(symbol)) {
                    System.out.println(state + " -- " + symbol + " --> " + dest);
                }
            }
        }
    }
}
