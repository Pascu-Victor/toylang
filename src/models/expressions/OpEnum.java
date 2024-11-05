package models.expressions;

public enum OpEnum {
    PLUS("+"),
    MINUS("-"),
    STAR("*"),
    DIVIDE("/"),
    AND("&&"),
    OR("||"),
    LESS("<"),
    LESSEQ("<="),
    EQUAL("=="),
    NOTEQ("!="),
    GREATER(">"),
    GREATEREQ(">=");

    private final String symbol;

    OpEnum(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }
}
