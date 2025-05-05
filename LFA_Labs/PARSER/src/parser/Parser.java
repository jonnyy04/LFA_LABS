package parser;

import lab3.Lexer;
import lab3.Token;
import lab3.TokenType;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class Parser {
    private final Iterator<Token> tokenIterator;
    private Token currentToken;

    public Parser(List<Token> tokens) {
        this.tokenIterator = tokens.iterator();
        advance();
    }

    private void advance() {
        currentToken = tokenIterator.hasNext() ? tokenIterator.next() : null;
    }

    private void match(TokenType expectedType) {
        if (currentToken != null && currentToken.getType() == expectedType) {
            advance();
        } else {
            throw new RuntimeException("Expected " + expectedType + " but found " +
                    (currentToken != null ? currentToken.getType() : "EOF"));
        }
    }

    public ASTNode parse() {
        ASTNode root = new ASTNode("root");

        while (currentToken != null) {
            switch (currentToken.getType()) {
                case SIZE:
                    root.addChild(parseSize());
                    break;
                case BACKGROUND:
                    root.addChild(parseBackground());
                    break;
                case STROKE:
                    root.addChild(parseStroke());
                    break;
                case FILL:
                    root.addChild(parseFill());
                    break;
                case SHAPE_CIRCLE:
                    root.addChild(parseCircle());
                    break;
                case SHAPE_RECT:
                    root.addChild(parseRect());
                    break;
                case SHAPE_LINE:
                    root.addChild(parseLine());
                    break;
                case SHAPE_TRIANGLE:
                    root.addChild(parseTriangle());
                    break;
                default:
                    throw new RuntimeException("Unexpected token: " + currentToken.getType());
            }
        }

        return root;
    }

    private ASTNode parseSize() {
        ASTNode node = new ASTNode("size");
        match(TokenType.SIZE);
        match(TokenType.COLON);
        node.addChild(new ASTNode("width", currentToken.getValue()));
        match(TokenType.NUMBER);
        node.addChild(new ASTNode("height", currentToken.getValue()));
        match(TokenType.NUMBER);
        return node;
    }

    private ASTNode parseBackground() {
        ASTNode node = new ASTNode("background");
        match(TokenType.BACKGROUND);
        match(TokenType.COLON);
        node.addChild(new ASTNode("color", currentToken.getValue()));
        match(TokenType.COLOR);
        return node;
    }

    private ASTNode parseStroke() {
        ASTNode node = new ASTNode("stroke");
        match(TokenType.STROKE);
        match(TokenType.COLON);
        node.addChild(new ASTNode("color", currentToken.getValue()));
        match(TokenType.COLOR);
        node.addChild(new ASTNode("width", currentToken.getValue()));
        match(TokenType.NUMBER);
        return node;
    }

    private ASTNode parseFill() {
        ASTNode node = new ASTNode("fill");
        match(TokenType.FILL);
        match(TokenType.COLON);
        node.addChild(new ASTNode("color", currentToken.getValue()));
        match(TokenType.COLOR);
        return node;
    }

    private ASTNode parseCircle() {
        ASTNode node = new ASTNode("circle");
        match(TokenType.SHAPE_CIRCLE);
        match(TokenType.COLON);
        node.addChild(new ASTNode("x", currentToken.getValue()));
        match(TokenType.NUMBER);
        node.addChild(new ASTNode("y", currentToken.getValue()));
        match(TokenType.NUMBER);
        node.addChild(new ASTNode("radius", currentToken.getValue()));
        match(TokenType.NUMBER);
        return node;
    }

    private ASTNode parseRect() {
        ASTNode node = new ASTNode("rect");
        match(TokenType.SHAPE_RECT);
        match(TokenType.COLON);
        node.addChild(new ASTNode("x", currentToken.getValue()));
        match(TokenType.NUMBER);
        node.addChild(new ASTNode("y", currentToken.getValue()));
        match(TokenType.NUMBER);
        node.addChild(new ASTNode("width", currentToken.getValue()));
        match(TokenType.NUMBER);
        node.addChild(new ASTNode("height", currentToken.getValue()));
        match(TokenType.NUMBER);
        if (currentToken != null && currentToken.getType() == TokenType.COLOR) {
            node.addChild(new ASTNode("color", currentToken.getValue()));
            match(TokenType.COLOR);
        }
        return node;
    }

    private ASTNode parseLine() {
        ASTNode node = new ASTNode("line");
        match(TokenType.SHAPE_LINE);
        match(TokenType.COLON);
        node.addChild(new ASTNode("x1", currentToken.getValue()));
        match(TokenType.NUMBER);
        node.addChild(new ASTNode("y1", currentToken.getValue()));
        match(TokenType.NUMBER);
        node.addChild(new ASTNode("x2", currentToken.getValue()));
        match(TokenType.NUMBER);
        node.addChild(new ASTNode("y2", currentToken.getValue()));
        match(TokenType.NUMBER);
        if (currentToken != null && currentToken.getType() == TokenType.COLOR) {
            node.addChild(new ASTNode("color", currentToken.getValue()));
            match(TokenType.COLOR);
        }
        return node;
    }

    private ASTNode parseTriangle() {
        ASTNode node = new ASTNode("triangle");
        match(TokenType.SHAPE_TRIANGLE);
        match(TokenType.COLON);
        node.addChild(new ASTNode("x1", currentToken.getValue()));
        match(TokenType.NUMBER);
        node.addChild(new ASTNode("y1", currentToken.getValue()));
        match(TokenType.NUMBER);
        node.addChild(new ASTNode("x2", currentToken.getValue()));
        match(TokenType.NUMBER);
        node.addChild(new ASTNode("y2", currentToken.getValue()));
        match(TokenType.NUMBER);
        node.addChild(new ASTNode("x3", currentToken.getValue()));
        match(TokenType.NUMBER);
        node.addChild(new ASTNode("y3", currentToken.getValue()));
        match(TokenType.NUMBER);
        if (currentToken != null && currentToken.getType() == TokenType.COLOR) {
            node.addChild(new ASTNode("color", currentToken.getValue()));
            match(TokenType.COLOR);
        }
        return node;
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
        Parser parser = new Parser(tokens);
        ASTNode ast = parser.parse();
        ast.print();
    }
}

class ASTNode {
    private final String name;
    private final String value;
    private final List<ASTNode> children;

    public ASTNode(String name) {
        this(name, null);
    }

    public ASTNode(String name, String value) {
        this.name = name;
        this.value = value;
        this.children = new ArrayList<>();
    }

    public void addChild(ASTNode child) {
        children.add(child);
    }

    public void print() {
        print("", true);
    }

    private void print(String prefix, boolean isTail) {
        System.out.println(prefix + (isTail ? "└── " : "├── ") +
                name + (value != null ? ": " + value : ""));

        for (int i = 0; i < children.size() - 1; i++) {
            children.get(i).print(prefix + (isTail ? "    " : "│   "), false);
        }

        if (!children.isEmpty()) {
            children.get(children.size() - 1)
                    .print(prefix + (isTail ? "    " : "│   "), true);
        }
    }
}