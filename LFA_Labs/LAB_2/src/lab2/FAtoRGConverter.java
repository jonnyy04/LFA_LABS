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

        for (State state : State.values()) {
               StringBuilder productionRule = new StringBuilder("S_" + state.name() + " -> ");

                        boolean firstTransition = true; // To handle the "|" separator

            for (Map.Entry<Character, State> transition : transitionFunction.get(state).entrySet()) {
                char symbol = transition.getKey();
                State nextState = transition.getValue();

                                if (!firstTransition) {
                    productionRule.append(" | ");
                }
                firstTransition = false;


                productionRule.append(symbol + " S_" + nextState.name());
            }

            if (state == State.q2) {
                productionRule.append(" | epsilon");
            }

            System.out.println(productionRule);
        }
    }

}

