package lab2;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;

public class Bonus {
    public void drawGraph() {
        // Configurăm UI-ul să folosească Swing pentru vizualizare
        System.setProperty("org.graphstream.ui", "swing");
        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");

        // Creăm un graf orientat
        Graph graph = new SingleGraph("DFA");

        // Adăugăm stările (noduri)
        String[] states = {"q0", "q0q1", "q1", "q2", "q0q1q2"};
        for (String state : states) {
            Node node = graph.addNode(state);
            node.setAttribute("ui.label", state); // Adaugă etichetă
            node.setAttribute("ui.style", "text-alignment: center; text-size: 20; shape: circle; size: 50px;");
        }

        // Stare inițială (q0)
        graph.getNode("q0").setAttribute("ui.style", "fill-color: yellow; text-alignment: center; text-size: 20; shape: circle; size: 50px;");

        // Stări finale
        graph.getNode("q2").setAttribute("ui.style", "fill-color: green; shape: circle; size: 50px; stroke-mode: plain; stroke-color: black;");
        graph.getNode("q0q1q2").setAttribute("ui.style", "fill-color: green; shape: circle; size: 50px; stroke-mode: plain; stroke-color: black;");

        // Adăugăm tranzițiile (muchii)
        graph.addEdge("q0-q0q1", "q0", "q0q1", true).setAttribute("ui.label", "a");
        graph.addEdge("q0q1-q0q1q2", "q0q1", "q0q1q2", true).setAttribute("ui.label", "a");
        graph.addEdge("q0q1-q1", "q0q1", "q1", true).setAttribute("ui.label", "b");
        graph.addEdge("q0q1-q0", "q0q1", "q0", true).setAttribute("ui.label", "c");
        graph.addEdge("q1-q2", "q1", "q2", true).setAttribute("ui.label", "a");
        graph.addEdge("q1-loop", "q1", "q1", true).setAttribute("ui.label", "b");
        graph.getEdge("q1-loop").setAttribute("ui.class", "loop");
        graph.addEdge("q1-q0", "q1", "q0", true).setAttribute("ui.label", "c");
        graph.addEdge("q2-loop", "q2", "q2", true).setAttribute("ui.label", "a");
        graph.getEdge("q2-loop").setAttribute("ui.class", "loop");
        graph.addEdge("q0q1q2-loop", "q0q1q2", "q0q1q2", true).setAttribute("ui.label", "a");
        graph.getEdge("q0q1q2-loop").setAttribute("ui.class", "loop");
        graph.addEdge("q0q1q2-q1", "q0q1q2", "q1", true).setAttribute("ui.label", "b");
        graph.addEdge("q0q1q2-q0", "q0q1q2", "q0", true).setAttribute("ui.label", "c");

        // Stilizare CSS
        graph.setAttribute("ui.stylesheet",
                "node { fill-color: lightblue; size: 50px; text-size: 20; text-alignment: center; shape: circle; }" +
                        "edge { fill-color: gray; text-size: 14; }" +
                        "edge.loop { shape: cubic-curve; size: 3px; fill-color: red; arrow-shape: none; }" +
                        "edge.loop .edge-label { text-alignment: above; text-offset: 0px, -15px; }"
        );

        // Afișăm graful
        graph.display();
    }
}