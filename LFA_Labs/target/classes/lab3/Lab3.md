# Lexer & Scanner

### Course: Formal Languages & Finite Automata
### Author: Vornicescu Ion

## Theory
A tokenizer is a core component in compiler and language design that breaks a stream of text into meaningful units called tokens, which serve as the smallest grammatical units of a language. Tokenizers perform lexical analysis, identifying sequences like keywords, identifiers, operators, and literals, which are then passed to the parser for syntactic analysis. For creating a domain-specific language (DSL) or compiler, a tokenizer is essential because it translates raw text into a structured format that subsequent compilation phases can process and understand. Without proper tokenization, your compiler would struggle to recognize the structures and patterns defined in your language grammar. A well-designed tokenizer simplifies the parsing stage by handling low-level text processing concerns, allowing the parser to focus on higher-level language structure. Additionally, tokenizers often handle tasks like removing whitespace and comments, tracking line numbers for error reporting, and validating basic lexical rules of your language. Implementing a tokenizer is typically the first concrete step in building a language implementation, serving as the foundation upon which the rest of your compiler or interpreter will be built.

## Objectives

* Understand what lexical analysis is.
* Get familiar with the inner workings of a lexer/scanner/tokenizer.
* Implement a sample lexer and show how it works.

## Overview
This report describes the implementation of a **lexer (tokenizer)** for a custom **Domain-Specific Language (DSL)** called **DrawScript**. This DSL is designed to allow users to specify drawing commands in a simple text-based format.

The lexer extracts tokens from the input text, classifying them into different categories such as keywords, numbers, colors, and separators. The generated tokens will later be used by a parser to interpret and execute drawing commands in **Processing** or another graphics framework.

---

## DSL Grammar & Features
### **Supported Commands**
The DSL supports the following commands:

| Command | Example | Description |
|---------|---------|-------------|
| `size` | `size : 800 600` | Sets canvas size (width, height) |
| `background` | `background : white` | Sets background color |
| `stroke` | `stroke : black 2` | Sets stroke color and thickness |
| `fill` | `fill : red` | Sets fill color for shapes |
| `circle` | `circle : 100 200 50` | Draws a circle at (x, y) with radius r |
| `rect` | `rect : 50 50 100 150 blue` | Draws a rectangle (x, y, width, height, color) |
| `line` | `line : 10 10 200 200 green` | Draws a line from (x1, y1) to (x2, y2) |
| `triangle` | `triangle : 10 10 50 50 90 20 yellow` | Draws a triangle with three points |

The **colon (`:`)** is used as a separator between the command and its parameters.

---


## Implementation description
### **1. Token Types**
The **TokenType** enum defines all valid tokens:
```java
public enum TokenType {
    SIZE("size"),
    BACKGROUND("background"),
    STROKE("stroke"),
    FILL("fill"),
    SHAPE_CIRCLE("circle"),
    SHAPE_RECT("rect"),
    SHAPE_LINE("line"),
    SHAPE_TRIANGLE("triangle"),
    COLOR("(black|white|red|green|blue|yellow|purple|orange)"),
    NUMBER("\\d+"),
    COLON(":"),
    WHITESPACE("\\s+");

    public final String pattern;
    TokenType(String pattern) {
        this.pattern = pattern;
    }
}
```
### **Key Points:**
- Each **token type** has a **regex pattern** to match specific words or values.
- Colors are **predefined words** (e.g., `red`, `blue`, `green`).
- `NUMBER` matches **integer values**.
- `WHITESPACE` is ignored during tokenization.

---

### **2. Token Class**
A **Token** stores the type and value of an extracted lexeme:
```java
public class Token {
    private final TokenType type;
    private final String value;

    public Token(TokenType type, String value) {
        this.type = type;
        this.value = value;
    }

    public TokenType getType() { return type; }
    public String getValue() { return value; }

    @Override
    public String toString() {
        return "(" + type + ", \"" + value + "\")";
    }
}
```
### **Key Points:**
- Each token contains a **type** and **value**.
- The `toString()` method helps with debugging.

---

### **3. Lexer Implementation**
The **Lexer** scans the input and extracts tokens using regex:
```java
import java.util.*;
import java.util.regex.*;

public class Lexer {
    private final List<Token> tokens = new ArrayList<>();
    
    public List<Token> tokenize(String input) {
        String remainingInput = input;
        
        while (!remainingInput.isEmpty()) {
            boolean matched = false;
            
            for (TokenType type : TokenType.values()) {
                Pattern pattern = Pattern.compile("^" + type.pattern);
                Matcher matcher = pattern.matcher(remainingInput);
                
                if (matcher.find()) {
                    String match = matcher.group();
                    if (type != TokenType.WHITESPACE) {  // Ignore whitespace
                        tokens.add(new Token(type, match));
                    }
                    remainingInput = remainingInput.substring(match.length());
                    matched = true;
                    break;
                }
            }
            
            if (!matched) {
                throw new RuntimeException("Unexpected token: " + remainingInput);
            }
        }
        
        return tokens;
    }
    
    public static void main(String[] args) {
        Lexer lexer = new Lexer();
        String input = """
            size : 800 600
            background : white
            stroke : black 2
            fill : red
            circle : 100 200 50
            rect : 50 50 100 150 blue
            line : 10 10 200 200 green
            triangle : 10 10 50 50 90 20 yellow
            """;

        List<Token> tokens = lexer.tokenize(input);
        for (Token token : tokens) {
            System.out.println(token);
        }
    }
}
```
### **Key Points:**
- **Regex matching** is used to extract tokens.
- **Whitespace is ignored** to prevent unnecessary tokens.
- If an **unknown token** is encountered, an **error is thrown**.
- **Extracted tokens** are stored in a **list** for further processing.

---

## Example Execution
### **Input**
```plaintext
size : 800 600
background : white
stroke : black 2
fill : red
circle : 100 200 50
rect : 50 50 100 150 blue
line : 10 10 200 200 green
triangle : 10 10 50 50 90 20 yellow
```
### **Output**
```plaintext
(SIZE, "size")
(COLON, ":")
(NUMBER, "800")
(NUMBER, "600")
(BACKGROUND, "background")
(COLON, ":")
(COLOR, "white")
(STROKE, "stroke")
(COLON, ":")
(COLOR, "black")
(NUMBER, "2")
(FILL, "fill")
(COLON, ":")
(COLOR, "red")
(SHAPE_CIRCLE, "circle")
(COLON, ":")
(NUMBER, "100")
(NUMBER, "200")
(NUMBER, "50")
...
```
---

## Future Improvements
- **Support for RGB colors** (e.g., `rgb(255, 0, 0)` instead of named colors)
- **Support for text labels** (e.g., `text : "Hello" 100 200 blue`)
- **Parsing & Execution** (Processing or Java Graphics integration)

---

## Conclusion
This lexer successfully tokenizes DrawScript DSL, preparing it for parsing and execution. The design is modular, allowing for easy expansion and integration with a graphics engine. Future enhancements could include a parser and execution engine to bring the drawings to life! The flexibility of this approach makes it suitable for other graphical or domain-specific applications, demonstrating the power of lexical analysis in structured text processing.

## References
* Finite State Machine (Finite Automata)
* https://www.youtube.com/watch?v=Qa6csfkK7_I
* LFPC Guide