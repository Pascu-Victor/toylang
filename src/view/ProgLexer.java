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
        skipWhitespace();
        if(currentChar == '/') {
            int oldPos = pos;
            char oldChar = currentChar;
            advance();
            if(currentChar != '/') {
                pos = oldPos;
                currentChar = oldChar;
                return;
            }
            consume('/');
            while (currentChar != '\0' && currentChar != '\n') {
                advance();
            }
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
            throw new RuntimeException("Unexpected character: " + currentChar + " expected: " + expected);
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
            case AND:
                consumeKeyword("&&");
                break;
            case OR:
                consumeKeyword("||");
                break;
            case LESS:
                consume('<');
                break;
            case LESSEQ:
                consumeKeyword("<=");
                break;
            case EQUAL:
                consumeKeyword("==");
                break;
            case GREATER:
                consumeKeyword(">=");
                break;
            case GREATEREQ:
                consumeKeyword(">=");
                break;
            case NOTEQ:
                consumeKeyword("!=");
                break;
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
        skipComment();
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
            case "string":
                return varDeclString();
            case "print":
                return printStmt();
            case "if":
                return ifStmt();
            case "nop":
                return nopStmt();
            case "openRFile":
                return openRFileStmt();
            case "readFile":
                return readFileStmt();
            case "closeRFile":
                return closeRFileStmt();
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

    private VarDeclStmt varDeclString() {
        consumeKeyword("string");
        skipWhitespace();
        String id = id();
        return new VarDeclStmt(id, new StringType());
    }

    private AssignStmt assignStmt() {
        String id = id();
        skipWhitespace();
        consume('=');
        skipWhitespace();
        if(currentChar == '"') {
            advance();
            StringBuilder result = new StringBuilder();
            while (currentChar != '\0' && currentChar != '"') {
                result.append(currentChar);
                advance();
            }
            consume('"');
            return new AssignStmt(id, new ValueExp(new StringValue(result.toString())));
        }
        IExp exp = expression();
        return new AssignStmt(id, exp);
    }

    private OpenRFileStmt openRFileStmt() {
        consumeKeyword("openRFile");
        consume('(');
        IExp fileName = expression();
        consume(')');
        return new OpenRFileStmt(fileName);
    }

    private CloseRFileStmt closeRFileStmt() {
        consumeKeyword("closeRFile");
        consume('(');
        IExp fileName = expression();
        consume(')');
        return new CloseRFileStmt(fileName);
    }

    private ReadFileStmt readFileStmt() {
        consumeKeyword("readFile");
        consume('(');
        IExp fileName = expression();
        consume(',');
        String varName = id();
        consume(')');
        return new ReadFileStmt(fileName, varName);
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
        skipWhitespace();
        IExp exp1 = expression();
        skipWhitespace();
        OpEnum op = operator();
        skipWhitespace();
        IExp exp2 = expression();
        skipWhitespace();
        consume(')');
        switch (op) {
            case OpEnum.PLUS:
            case OpEnum.MINUS:
            case OpEnum.STAR:
            case OpEnum.DIVIDE:
                return new ArithExp(exp1, exp2, op);
            case OpEnum.AND:
            case OpEnum.OR:
                return new LogicExp(exp1, exp2, op);
            case OpEnum.LESS:
            case OpEnum.LESSEQ:
            case OpEnum.EQUAL:
            case OpEnum.NOTEQ:
            case OpEnum.GREATER:
            case OpEnum.GREATEREQ:
                return new RelExp(exp1, exp2, op);
            default:
                throw new RuntimeException("Could not handle operator: " + op);
        }
    }

    private String lookaheadOperator() {
        int oldPos = pos;
        char oldChar = currentChar;
        StringBuilder result = new StringBuilder();
        if (currentChar == '<' || currentChar == '>' || currentChar == '=' || currentChar == '!') {
            result.append(currentChar);
            advance();
            if (currentChar == '=') {
            result.append(currentChar);
            advance();
            }
        }
        pos = oldPos;
        currentChar = oldChar;
        return result.toString().trim();
    }
    
    private OpEnum operator() {
        skipWhitespace();
        switch (currentChar) {
            case '+':
                consumeOperator(OpEnum.PLUS);
                return OpEnum.PLUS;
            case '-':
                consumeOperator(OpEnum.MINUS);
                return OpEnum.MINUS;
            case '*':
                consumeOperator(OpEnum.STAR);
                return OpEnum.STAR;
            case '/':
                consumeOperator(OpEnum.DIVIDE);
                return OpEnum.DIVIDE;
            case '&':
                consumeOperator(OpEnum.AND);
                return OpEnum.AND;
            case '|':
                consumeOperator(OpEnum.OR);
                return OpEnum.OR;
            case '<':
            {
                var tk = lookaheadOperator();
                switch (tk) {
                    case "<":
                        consumeOperator(OpEnum.LESS);
                        return OpEnum.LESS;
                    case "<=":
                        consumeOperator(OpEnum.LESSEQ);
                        return OpEnum.LESSEQ;
                    default:
                        throw new RuntimeException("Unexpected operator: " + currentChar);
                }
            }
            case '>':
            {
                var tk = lookaheadOperator();
                switch (tk) {
                    case ">":
                        consumeOperator(OpEnum.GREATER);
                        return OpEnum.GREATER;
                    case "<=":
                        consumeOperator(OpEnum.GREATEREQ);
                        return OpEnum.GREATEREQ;
                    default:
                        throw new RuntimeException("Unexpected operator: " + currentChar);
                }
            }
            case '=':
            {
                var tk = lookaheadOperator();
                if(tk.equals("==")) {
                    consumeOperator(OpEnum.EQUAL);
                    return OpEnum.EQUAL;
                }
                throw new RuntimeException("Unexpected operator: " + currentChar);
            }
            case '!':
            {
                var tk = lookaheadOperator();
                if(tk.equals("!=")) {
                    consumeOperator(OpEnum.NOTEQ);
                    return OpEnum.NOTEQ;
                }
                throw new RuntimeException("Unexpected operator: " + currentChar);
            }
            default:
                throw new RuntimeException("Unexpected operator: " + currentChar);
        }
    }

    public IStmt parse() {
        return statement();
    }
}
