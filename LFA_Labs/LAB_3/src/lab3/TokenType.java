package lab3;

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
    NUMBER("\\d+"),  // Matches integers
    COLON(":"),
    WHITESPACE("\\s+");  // Ignore spaces

    public final String pattern;

    TokenType(String pattern) {
        this.pattern = pattern;
    }
}

