# Chomsky Normal Form

### Course: Formal Languages & Finite Automata
### Author: Vornicescu Ion

## Theory
Chomsky Normal Form (CNF) is a simplified form of context-free grammar where all production rules must be of one of two forms:
1. A → BC (where A, B, C are non-terminals)
2. A → a (where A is a non-terminal and 'a' is a terminal)

The conversion to CNF involves several key steps:
1. **Eliminate ε-productions** (A → ε)
2. **Eliminate renaming productions** (A → B)
3. **Eliminate inaccessible symbols**
4. **Eliminate non-productive symbols**
5. **Convert remaining productions to CNF**

CNF is important because it simplifies parsing algorithms and enables efficient analysis of context-free grammars.

---

## Objectives
* Learn about Chomsky Normal Form (CNF)
* Get familiar with the approaches of normalizing a grammar
* Implement a method for normalizing an input grammar by the rules of CNF

---

## Implementation Description

The implementation consists of several key methods that transform the grammar step-by-step into CNF. Here are the most important parts:

### 1. Eliminating ε-Productions
```java
private void eliminateEpsilonProductions() {
    Set<String> nullable = new HashSet<>();
    boolean changed;
    do {
        changed = false;
        for (Map.Entry<String, List<List<String>>> entry : productions.entrySet()) {
            String nt = entry.getKey();
            for (List<String> production : entry.getValue()) {
                if (production.equals(Arrays.asList("ε")) {
                    nullable.add(nt);
                    changed = true;
                }
            }
        }
    } while (changed);
  
}
```
This method first identifies all nullable non-terminals (those that can derive ε). Then it generates all possible combinations of productions without the nullable symbols. If the start symbol is nullable, it adds a new start symbol.


### Eliminating Renaming Productions
```java
private void eliminateRenaming() {
    do {
        changed = false;
        // For each production A → B, replace with all productions of B
        for (List<String> production : productions.get(nt)) {
            if (production.size() == 1 && nonTerminals.contains(production.get(0))) {
                // Replace with all productions of the right-hand side non-terminal
                changed = true;
            }
        }
    } while (changed);
}
```
This step removes chain productions (A → B) by substituting them with all productions of B. The process repeats until no more renaming productions exist.

### Eliminating Inaccessible Symbols
```java
     private void eliminateInaccessibleSymbols() {
    Set<String> accessible = new HashSet<>();
    accessible.add(startSymbol);
    
}   
```
This method performs a graph traversal starting from the start symbol to find all reachable non-terminals, then removes any that are unreachable.

### Converting to CNF

```java
private void convertToChomskyNormalForm() {
    // Step 1: Replace terminals with new non-terminals
    Map<String, String> terminalToNonTerminal = new HashMap<>();
    // Step 2: Break long productions into binary productions
    while (exists productions with length > 2) {
        // Split into pairs with new non-terminals
    }
}
```
The final conversion step ensures all productions are either:

A → BC (two non-terminals), or

A → a (single terminal)

### **Sample Outputs**
```plaintext
After eliminating ε productions:
Non-terminals: [A, B, S, C, E]
Terminals: [a, b]
Start symbol: S
Productions:
A → a | a S
B → A | A C | b S | a A a
S → b A | B
C → A B
E → B A

After eliminating renaming:
Non-terminals: [A, B, S, C, E]
Terminals: [a, b]
Start symbol: S
Productions:
A → a | a S
B → a | a S | A C | b S | a A a
S → b A | a | a S | A C | b S | a A a
C → A B
E → B A

After eliminating inaccessible symbols:
Non-terminals: [A, B, S, C]
Terminals: [a, b]
Start symbol: S
Productions:
A → a | a S
B → a | a S | A C | b S | a A a
S → b A | a | a S | A C | b S | a A a
C → A B

After eliminating non-productive symbols:
Non-terminals: [A, B, S, C]
Terminals: [a, b]
Start symbol: S
Productions:
A → a | a S
B → a | a S | A C | b S | a A a
S → b A | a | a S | A C | b S | a A a
C → A B

Final CNF:
Non-terminals: [A, B, S, C, Y1, Y2, X1, X2]
Terminals: [a, b]
Start symbol: S
Productions:
A → a | Y1 S
B → Y1 X1 | a | Y1 S | A C | Y2 S
S → Y1 X2 | Y2 A | a | Y1 S | A C | Y2 S
C → A B
Y1 → a
X1 → A Y1
Y2 → b
X2 → A Y1
```

---

## Conclusion
This laboratory work successfully implemented the conversion of a context-free grammar to Chomsky Normal Form. The step-by-step approach demonstrated how complex grammars can be systematically simplified while preserving their generative power. Key challenges included handling ε-productions and efficiently breaking down long productions while introducing minimal new non-terminals. The implementation shows how theoretical concepts of formal language theory can be practically applied in software.

## References
* Regular Expressions - Formal Language Theory
* Finite State Machines & Regular Expressions
* LFPC Guide

