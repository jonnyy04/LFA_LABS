package lab2;

import java.util.HashMap;
import java.util.Map;

public class FAtoRGConverter {

    // Define the states
    enum State {
        q0, q1, q2
    }

    // Define the transition function as a Map
    // State -> Map<symbol, State>
    private static final Map<State, Map<Character, State>> transitionFunction = new HashMap<>();

    // Initialize the FA transitions
    static {
        // Transition function setup
        transitionFunction.put(State.q0, new HashMap<>());
        transitionFunction.get(State.q0).put('a', State.q0);
        transitionFunction.get(State.q0).put('a', State.q1);

        transitionFunction.put(State.q1, new HashMap<>());
        transitionFunction.get(State.q1).put('c', State.q0);
        transitionFunction.get(State.q1).put('b', State.q1);
        transitionFunction.get(State.q1).put('a', State.q2);

        transitionFunction.put(State.q2, new HashMap<>());
        transitionFunction.get(State.q2).put('a', State.q2);
    }

    // Function to generate the regular grammar from the FA's transition function
    public static void generateRegularGrammar() {
        // Iterate over all states
        for (State state : State.values()) {
            // Start the production rule for the current state
            StringBuilder productionRule = new StringBuilder("S_" + state.name() + " -> ");

            // For each character in the alphabet, check the transition
            boolean firstTransition = true; // To handle the "|" separator

            for (Map.Entry<Character, State> transition : transitionFunction.get(state).entrySet()) {
                char symbol = transition.getKey();
                State nextState = transition.getValue();

                // If this is not the first transition, add a "|" separator
                if (!firstTransition) {
                    productionRule.append(" | ");
                }
                firstTransition = false;

                // Add the transition rule (symbol followed by the next state)
                productionRule.append(symbol + " S_" + nextState.name());
            }

            // Check if this state is a final state (in this case, q2 is the final state)
            if (state == State.q2) {
                // Add epsilon production for the final state
                productionRule.append(" | epsilon");
            }

            // Print the production rule for the current state
            System.out.println(productionRule);
        }
    }

}

