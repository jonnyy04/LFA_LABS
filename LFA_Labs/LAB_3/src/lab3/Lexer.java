package lab3;

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
