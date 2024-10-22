package view;

import models.expressions.*;
import models.statements.*;
import models.types.*;
import models.values.*;

public class ProgLexer {
    private String source;
    private int pos;
    private char currentChar;

    public ProgLexer(String source) {
        this.source = source;
        this.pos = 0;
        this.currentChar = source.charAt(0);
    }

    private void advance() {
        pos++;
        if (pos < source.length()) {
            currentChar = source.charAt(pos);
        } else {
            currentChar = '\0';
        }
    }

    private void skipWhitespace() {
        while (currentChar != '\0' && Character.isWhitespace(currentChar)) {
            advance();
        }
    }

    private void skipComment() {
        while (currentChar != '\0' && currentChar != '\n') {
            advance();
        }
    }

    private String integer() {
        StringBuilder result = new StringBuilder();
        while (currentChar != '\0' && Character.isDigit(currentChar)) {
            result.append(currentChar);
            advance();
        }
        return result.toString();
    }

    private String id() {
        StringBuilder result = new StringBuilder();
        while (currentChar != '\0' && Character.isLetterOrDigit(currentChar)) {
            result.append(currentChar);
            advance();
        }
        return result.toString();
    }

    private void consume(char expected) {
        if (currentChar == expected) {
            advance();
        } else {
            throw new RuntimeException("Unexpected character: " + currentChar);
        }
    }

    private void consumeKeyword(String keyword) {
        for (char c : keyword.toCharArray()) {
            consume(c);
        }
    }

    private void consumeOperator(OpEnum op) {
        switch (op) {
            case PLUS:
                consume('+');
                break;
            case MINUS:
                consume('-');
                break;
            case STAR:
                consume('*');
                break;
            case DIVIDE:
                consume('/');
                break;
            // case AND:
            //     consumeKeyword("and");
            //     break;
            // case OR:
            //     consumeKeyword("or");
            //     break;
            default:
                throw new RuntimeException("Unexpected operator: " + op);
        }
    }

    private String lookaheadToken() {
        int oldPos = pos;
        char oldChar = currentChar;
        skipWhitespace();
        String token = id();
        pos = oldPos;
        currentChar = oldChar;
        return token;
    }

    private IStmt statement() {
        skipWhitespace();
        if (currentChar == '\0') {
            return null;
        }
        if (currentChar == ';') {
            advance();
            return statement();
        }
        var nextToken = lookaheadToken();
        switch (nextToken) {
            case "int":
                return varDeclInt();
            case "bool":
                return varDeclBool();
            case "print":
                return printStmt();
            case "if":
                return ifStmt();
            case "nop":
                return nopStmt();
            default:
                return assignStmt();
        }
    }

    private NopStmt nopStmt() {
        consumeKeyword("nop");
        return new NopStmt();
    }

    private VarDeclStmt varDeclInt() {
        consumeKeyword("int");
        skipWhitespace();
        String id = id();
        return new VarDeclStmt(id, new IntType());
    }

    private VarDeclStmt varDeclBool() {
        consumeKeyword("bool");
        skipWhitespace();
        String id = id();
        return new VarDeclStmt(id, new BoolType());
    }

    private AssignStmt assignStmt() {
        String id = id();
        consume('=');
        IExp exp = expression();
        return new AssignStmt(id, exp);
    }

    private PrintStmt printStmt() {
        consumeKeyword("print");
        consume('(');
        IExp exp = expression();
        consume(')');
        return new PrintStmt(exp);
    }

    private IfStmt ifStmt() {
        consumeKeyword("if");
        skipWhitespace();
        IExp exp = expression();
        skipWhitespace();
        consumeKeyword("then");
        skipWhitespace();
        IStmt thenS = statement();
        skipWhitespace();
        consumeKeyword("else");
        skipWhitespace();
        IStmt elseS = statement();
        skipWhitespace();
        return new IfStmt(exp, thenS, elseS);
    }

    private IExp expression() {
        skipWhitespace();
        if (Character.isDigit(currentChar)) {
            return new ValueExp(new IntValue(Integer.parseInt(integer())));
        }
        var token = lookaheadToken();
        if (token.equals("true")) {
            consumeKeyword("true");
            return new ValueExp(new BoolValue(true));
        }
        if (token.equals("false")) {
            consumeKeyword("false");
            return new ValueExp(new BoolValue(false));
        }
        if (Character.isLetter(currentChar)) {
            return new VarExp(id());
        }
        consume('(');
        IExp exp1 = expression();
        OpEnum op = operator();
        IExp exp2 = expression();
        consume(')');
        return new ArithExp(exp1, exp2, op);
    }

    private OpEnum operator() {
        skipWhitespace();
        switch (currentChar) {
            case '+':
                advance();
                return OpEnum.PLUS;
            case '-':
                advance();
                return OpEnum.MINUS;
            case '*':
                advance();
                return OpEnum.STAR;
            case '/':
                advance();
                return OpEnum.DIVIDE;
            // case 'a':
            //     consumeKeyword("and");
            //     return OpEnum.AND;
            // case 'o':
            //     consumeKeyword("or");
            //     return OpEnum.OR;
            default:
                throw new RuntimeException("Unexpected operator: " + currentChar);
        }
    }

    public IStmt parse() {
        return statement();
    }
}
