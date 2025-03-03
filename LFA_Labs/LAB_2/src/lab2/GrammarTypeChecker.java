package lab2;

import java.util.*;

public class GrammarTypeChecker {

    private final Set<String> VN; // Neterminale
    private final Set<String> VT; // Terminale
    private final Map<String, List<String>> RULES; // Reguli de producție

    public GrammarTypeChecker() {
        VN = Set.of("S", "D", "R");
        VT = Set.of("a", "b", "c");

        RULES = new HashMap<>();
        RULES.put("S", List.of("aS", "cD"));
        RULES.put("D", List.of("bR"));
        RULES.put("R", List.of("aR", "b", "cS"));
    }

    public String determineGrammarType() {
        boolean isRegular = true;
        boolean isContextFree = true;
        boolean isContextSensitive = true;

        for (Map.Entry<String, List<String>> entry : RULES.entrySet()) {
            String leftSide = entry.getKey();
            List<String> rightSides = entry.getValue();

            for (String rightSide : rightSides) {
                // Verificare Tip 2 (CFG): Stânga trebuie să fie un singur neterminal
                if (!VN.contains(leftSide) || leftSide.length() != 1) {
                    isContextFree = false;
                }

                // Verificare Tip 3 (Regulară): Forma trebuie să fie A → xB sau A → x
                if (rightSide.length() > 2) {
                    isRegular = false;
                } else if (rightSide.length() == 2) {
                    char firstChar = rightSide.charAt(0);
                    char secondChar = rightSide.charAt(1);
                    if (!VT.contains(String.valueOf(firstChar)) || !VN.contains(String.valueOf(secondChar))) {
                        isRegular = false;
                    }
                } else if (rightSide.length() == 1) {
                    if (!VT.contains(rightSide)) {
                        isRegular = false;
                    }
                }

                // Verificare Tip 1 (CSG): Lungimea părții drepte trebuie să fie ≥ lungimea părții stângi
                if (rightSide.length() < leftSide.length()) {
                    isContextSensitive = false;
                }
            }
        }

        // Returnează cel mai specific tip de gramatică detectat
        if (isRegular) return "Tip 3 (Regulară)";
        if (isContextFree) return "Tip 2 (Indep. de context - CFG)";
        if (isContextSensitive) return "Tip 1 (Dep. de context - CSG)";
        return "Tip 0 (Recursiv enumerabilă)";
    }
}
