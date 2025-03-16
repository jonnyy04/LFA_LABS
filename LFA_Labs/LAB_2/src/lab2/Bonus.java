//import processing.core.*;
//import java.util.*;
//
//public class Bonus extends PApplet {
//
//    // Clasa pentru reprezentarea unui nod (stare) din DFA
//    class State {
//        String name;
//        float x, y;
//        boolean isAccepting;
//
//        State(String name, float x, float y, boolean isAccepting) {
//            this.name = name;
//            this.x = x;
//            this.y = y;
//            this.isAccepting = isAccepting;
//        }
//    }
//
//    HashMap<String, State> states = new HashMap<>(); // Lista de stări
//    HashMap<String, HashMap<Character, String>> transitions = new HashMap<>(); // Tranzițiile între stări
//
//    public void settings() {
//        size(800, 600);
//    }
//    public void setup() {
//
//        // Inițializarea stărilor
//        states.put("q0", new State("q0", 100, 100, false));
//        states.put("q1", new State("q1", 400, 150, false));
//        states.put("q2", new State("q2", 600, 300, false));
//        states.put("q1q0", new State("{q1, q0}", 100, 400, false));
//        states.put("q1q2q0", new State("{q1, q2, q0}", 500, 450, false));
//
//        // Inițializarea tranzițiilor
//        transitions.put("q0", new HashMap<>(Map.of('a', "q1q0")));
//        transitions.put("q1", new HashMap<>(Map.of('a', "q2", 'b', "q1", 'c', "q0")));
//        transitions.put("q2", new HashMap<>(Map.of('a', "q2")));
//        transitions.put("q1q0", new HashMap<>(Map.of('a', "q1q2q0", 'b', "q1", 'c', "q0")));
//        transitions.put("q1q2q0", new HashMap<>(Map.of('a', "q1q2q0", 'b', "q1", 'c', "q0")));
//    }
//
//    public void draw() {
//        background(30, 30, 50); // Setează fundalul
//
//        // Desenează stările
//        for (State state : states.values()) {
//            fill(state.isAccepting ? color(0, 200, 0) : color(100, 150, 255)); // Culoare în funcție de tipul stării
//            stroke(255);
//            ellipse(state.x, state.y, 60, 60);
//            fill(255);
//            textAlign(CENTER, CENTER);
//            textSize(16);
//            text(state.name, state.x, state.y);
//        }
//
//        stroke(255);
//        // Desenează tranzițiile între stări
//        for (String from : transitions.keySet()) {
//            for (Map.Entry<Character, String> entry : transitions.get(from).entrySet()) {
//                State s1 = states.get(from);
//                State s2 = states.get(entry.getValue());
//                if (s1 != null && s2 != null) {
//                    if (s1 == s2) {
//                        drawSelfLoop(s1.x, s1.y, entry.getKey()); // Desenează self-loop
//                    } else {
//                        drawArrow(s1.x, s1.y, s2.x, s2.y, entry.getKey()); // Desenează o tranziție normală
//                    }
//                }
//            }
//        }
//    }
//
//    // Funcție pentru desenarea unei săgeți între două stări
//    void drawArrow(float x1, float y1, float x2, float y2, char label) {
//        float angle = atan2(y2 - y1, x2 - x1);
//        float len = dist(x1, y1, x2, y2) - 30; // Ajustează capătul săgeții
//        float newX = x1 + cos(angle) * len;
//        float newY = y1 + sin(angle) * len;
//
//        line(x1, y1, newX, newY);
//        fill(255);
//        text(label, (x1 + newX) / 2, (y1 + newY) / 2);
//
//        pushMatrix();
//        translate(newX, newY);
//        rotate(angle);
//        line(-5, -5, 0, 0); // Vârful săgeții
//        line(-5, 5, 0, 0);
//        popMatrix();
//    }
//
//    // Funcție pentru desenarea self-loop-urilor
//    void drawSelfLoop(float x, float y, char label) {
//        float loopRadius = 30;
//        noFill();
//        arc(x, y - 35, loopRadius * 2, loopRadius, -3 * PI / 4, -PI / 10); // Arc pentru loop
//        fill(255);
//        text(label, x, y - 50); // Eticheta tranziției
//    }
//
//    public static void main(String[] args) {
//        PApplet.main("Bonus");
//    }
//}