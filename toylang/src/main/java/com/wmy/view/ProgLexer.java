package com.wmy.view;

import java.util.ArrayList;
import java.util.List;

import com.wmy.models.adt.Entry;
import com.wmy.models.adt.AList;
import com.wmy.models.adt.CloneableString;
import com.wmy.models.expressions.*;
import com.wmy.models.statements.*;
import com.wmy.models.types.*;
import com.wmy.models.values.*;

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
        if (currentChar == '/') {
            int oldPos = pos;
            char oldChar = currentChar;
            advance();
            if (currentChar != '/') {
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
                consume('>');
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

    // A reference type is a recursive type such that,
    // in our ToyLanguage we can allow the following types:
    // Ref(int) x1;
    // Ref bool x2;
    // Ref (Ref (int)) x3;
    // Ref Ref Ref string x4;
    // Ref Ref Ref Ref bool x5;

    private IType parseType() {
        skipWhitespace();
        if (lookaheadToken().equals("Ref")) {
            consumeKeyword("Ref");
            skipWhitespace();
            return new RefType(parseType());
        }
        switch (lookaheadToken()) {
            case "int":
                consumeKeyword("int");
                return new IntType();
            case "bool":
                consumeKeyword("bool");
                return new BoolType();
            case "string":
                consumeKeyword("string");
                return new StringType();
            default:
                throw new RuntimeException("Unexpected type: " + lookaheadToken());
        }
    }

    private VarDeclStmt varDeclRef() {
        consumeKeyword("Ref");
        skipWhitespace();
        IType innerType = parseType();
        skipWhitespace();
        String id = id();
        return new VarDeclStmt(id, new RefType(innerType));
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
            case "Ref":
                return varDeclRef();
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
            case "while":
                return whileStmt();
            case "new":
                return newStmt();
            case "wH":
                return whStmt();
            case "fork":
                return forkStmt();
            case "switch":
                return switchStmt();
            case "newLatch":
                return newLatchStmt();
            case "countDown":
                return countDownStmt();
            case "await":
                return awaitStmt();
            case "repeat":
                return repeatUntilStmt();
            default:
                return assignStmt();
        }
    }

    private RepeatUntilStmt repeatUntilStmt() {
        consumeKeyword("repeat");
        skipWhitespace();
        IStmt stmts = consumeCodeBlock();
        skipWhitespace();
        consumeKeyword("until");
        skipWhitespace();
        consume('(');
        skipWhitespace();
        IExp exp = expression();
        skipWhitespace();
        consume(')');
        skipWhitespace();
        return new RepeatUntilStmt(exp, stmts);
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
        if (currentChar == '"') {
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

    private NewLatchStmt newLatchStmt() {
        consumeKeyword("newLatch");
        skipWhitespace();
        consume('(');
        skipWhitespace();
        CloneableString varName = new CloneableString(id());
        skipWhitespace();
        consume(',');
        skipWhitespace();
        IExp exp = expression();
        skipWhitespace();
        consume(')');
        return new NewLatchStmt(varName, exp);
    }

    private CountDownStmt countDownStmt() {
        consumeKeyword("countDown");
        skipWhitespace();
        consume('(');
        skipWhitespace();
        CloneableString varName = new CloneableString(id());
        skipWhitespace();
        consume(')');
        return new CountDownStmt(varName);
    }

    private AwaitStmt awaitStmt() {
        consumeKeyword("await");
        skipWhitespace();
        consume('(');
        skipWhitespace();
        CloneableString varName = new CloneableString(id());
        skipWhitespace();
        consume(')');
        return new AwaitStmt(varName);
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

    private IStmt consumeCodeBlock() {
        skipWhitespace();
        consume('{');
        List<IStmt> stmts = new ArrayList<>();
        while (currentChar != '}') {
            IStmt stmt = statement();
            if (stmt != null) {
                stmts.add(stmt);
            }
            skipWhitespace();
        }
        consume('}');
        IStmt comp = stmts.get(stmts.size() - 1);
        for (int i = stmts.size() - 2; i >= 0; i--) {
            comp = new CompStmt(stmts.get(i), comp);
        }
        skipWhitespace();
        return comp;
    }

    private WhileStmt whileStmt() {
        consumeKeyword("while");
        skipWhitespace();
        consume('(');
        skipWhitespace();
        IExp condExp = expression();
        skipWhitespace();
        consume(')');
        skipWhitespace();
        IStmt stmts = consumeCodeBlock();
        skipWhitespace();
        return new WhileStmt(condExp, stmts);
    }

    private ForkStmt forkStmt() {
        consumeKeyword("fork");
        skipWhitespace();
        consume('(');
        skipWhitespace();
        IStmt subprogram = consumeCodeBlock();
        skipWhitespace();
        consume(')');
        skipWhitespace();
        return new ForkStmt(subprogram);
    }

    private SwitchStmt switchStmt() {
        consumeKeyword("switch");
        skipWhitespace();
        consume('(');
        skipWhitespace();
        IExp swExp = expression();
        skipWhitespace();
        consume(')');
        skipWhitespace();
        consume('{');
        skipWhitespace();
        var cases = new AList<Entry<IExp, IStmt>>();
        IStmt defaultStmt = null;
        while (currentChar != '}') {
            // can be case or default
            skipWhitespace();
            if (lookaheadToken().equals("case")) {
                consumeKeyword("case");
                skipWhitespace();
                consume('(');
                skipWhitespace();
                IExp caseExp = expression();
                skipWhitespace();
                consume(')');
                skipWhitespace();
                IStmt caseStmt = consumeCodeBlock();
                skipWhitespace();
                cases.add(new Entry<IExp, IStmt>(caseExp, caseStmt));
            } else {
                consumeKeyword("default");
                skipWhitespace();
                defaultStmt = consumeCodeBlock();
                skipWhitespace();
            }
        }
        skipWhitespace();
        consume('}');
        skipWhitespace();
        return new SwitchStmt(swExp, cases, defaultStmt);
    }

    private NewStmt newStmt() {
        consumeKeyword("new");
        skipWhitespace();
        consume('(');
        skipWhitespace();
        String refVar = id();
        skipWhitespace();
        consume(',');
        skipWhitespace();
        IExp addrExp = expression();
        skipWhitespace();
        consume(')');
        skipWhitespace();
        return new NewStmt(refVar, addrExp);
    }

    private WHStmt whStmt() {
        consumeKeyword("wH");
        skipWhitespace();
        consume('(');
        skipWhitespace();
        String refName = id();
        skipWhitespace();
        consume(',');
        skipWhitespace();
        IExp valExp = expression();
        skipWhitespace();
        consume(')');
        skipWhitespace();
        return new WHStmt(refName, valExp);
    }

    private RHExp rhExp() {
        consumeKeyword("rH");
        skipWhitespace();
        consume('(');
        skipWhitespace();
        IExp addrExp = expression();
        skipWhitespace();
        consume(')');
        skipWhitespace();
        return new RHExp(addrExp);
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
        if (token.equals("rH")) {
            return rhExp();
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
            case PLUS:
            case MINUS:
            case STAR:
            case DIVIDE:
                return new ArithExp(exp1, exp2, op);
            case AND:
            case OR:
                return new LogicExp(exp1, exp2, op);
            case LESS:
            case LESSEQ:
            case EQUAL:
            case NOTEQ:
            case GREATER:
            case GREATEREQ:
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
            case '<': {
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
            case '>': {
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
            case '=': {
                var tk = lookaheadOperator();
                if (tk.equals("==")) {
                    consumeOperator(OpEnum.EQUAL);
                    return OpEnum.EQUAL;
                }
                throw new RuntimeException("Unexpected operator: " + currentChar);
            }
            case '!': {
                var tk = lookaheadOperator();
                if (tk.equals("!=")) {
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
