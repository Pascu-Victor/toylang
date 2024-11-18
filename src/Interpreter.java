import java.io.BufferedReader;

import controller.IProgController;
import controller.ProgController;
import models.PrgState;
import models.adt.*;
import models.statements.CompStmt;
import models.statements.IStmt;
import models.values.*;
import repo.StateRepo;
import view.ProgLexer;
import view.TextMenu;
import view.commands.ExitCommand;
import view.commands.RunExampleCommand;

public class Interpreter {

    private static IProgController parseProgram(String source, String logFile) {
        var lexer = new ProgLexer(source);
        var stmts = new AList<IStmt>();
        while (true) {
            var stmt = lexer.parse();
            if (stmt == null) {
                break;
            }
            stmts.add(stmt);
        }

        IStmt comp = stmts.get(stmts.size() - 1);
        for (int i = stmts.size() - 2; i >= 0; i--) {
            comp = new CompStmt(stmts.get(i), comp);
        }

        var stk = new AStack<IStmt>();
        var st = new ADict<String, IValue>();
        var out = new AList<IValue>();
        var fbs = new ADict<String, BufferedReader>();
        var heap = new Heap();
        var prg = new PrgState(stk, st, out, fbs, heap, comp);
        var repo = new StateRepo(prg,logFile);
        return new ProgController(repo);
    }

    private static void command(TextMenu menu, String logName, String progKey, String progDesc, String src) {
        var c = parseProgram(src, logName);
        var command = new RunExampleCommand(progKey, progDesc, c);
        menu.addCommand(command);
    }


    public static void main(String[] args) throws Exception {
        var source1 = "int v; int m; v=2; m=3; v=(v+m); nop; print(v); print(m)";
        var source2 = "bool a; a=true; int v; if a then v=2 else v=3; print(v)";
        var source3 = "int a; a=(2+(3*5)); int b; b=((a-(4/2)) + 7); print(b)";
        var source4 = "string varf;\n" +
                        "varf=\"test.in\";\n" +
                        "//cooooomeeeeent\n" +
                        "openRFile(varf);//comment\n" +
                        "int varc;\n" +
                        "readFile(varf,varc);print(varc);\n" +
                        "readFile(varf,varc);print(varc)\n" +
                        "closeRFile(varf)";
        var source5 = "bool a; int b;b = 1; a = (b <= 2); if(a && (b != 5)) then print(b) else print(0)";
        var sourceNew = "Ref int v;new(v,20);Ref Ref int a; new(a,v);print(v);print(a)";
        var sourcerH = "Ref int v;new(v,20);Ref Ref int a; new(a,v);print(rH(v));print((rH(rH(a))+5))";
        var sourcewH = "Ref int v;new(v,20);print(rH(v)); wH(v,30);print((rH(v)+5));";
        var sourceGC = "Ref int v;new(v,20);Ref Ref int a; new(a,v); new(v,30);print(rH(rH(a)))";
        var sourcewhile = "int v; v=4; while ((v>0)){ print(v);v=(v-1);print(v)}";

        var textMenu = new TextMenu();

        command(textMenu, "prg1.log", "prg1", "program 1", source1);
        command(textMenu, "prg2.log", "prg2", "program 2", source2);
        command(textMenu, "prg3.log", "prg3", "program 3", source3);
        command(textMenu, "prg4.log", "prg4", "program 4", source4);
        command(textMenu, "prg5.log", "prg5", "program 5", source5);
        command(textMenu, "prgNew.log", "New", "New program", sourceNew);
        command(textMenu, "prgrH.log", "rH", "rH program", sourcerH);
        command(textMenu, "prgwH.log", "wH", "wH program", sourcewH);
        command(textMenu, "prgGC.log", "GC", "GC program", sourceGC);
        command(textMenu, "prgwhile.log", "while", "while program", sourcewhile);

        textMenu.addCommand(new ExitCommand("exit", "exit program"));

        textMenu.show();

        // var repo = new StateRepo();
        // // var ex1 = new CompStmt(
        // //     new VarDeclStmt("v", new IntType()),
        // //     new CompStmt(
        // //         new AssignStmt("v", new ValueExp(new IntValue((2)))),
        // //         new CompStmt(
        // //             new VarDeclStmt("m", new IntType()),
        // //             new CompStmt(
        // //                 new AssignStmt("m", new ValueExp(new IntValue((2)))),
        // //                 new CompStmt(
        // //                     new AssignStmt("v", 
        // //                         new ArithExp(
        // //                             new VarExp("m"),
        // //                             new VarExp("v"),
        // //                             OpEnum.DIVIDE
        // //                         )
        // //                     ),
        // //                     new PrintStmt(new VarExp("v"))
        // //                 )
        // //             )
        // //         )
        // //     )
        // // );
        // var stk = new AStack<IStmt>();
        // var st = new ADict<String, IValue>();
        // var out = new AList<IValue>();
        // var prg = new PrgState(stk, st, out, ex1);
        // repo.add(prg);
        // var controller = new ProgController(repo);
        // var view = new ProgView(controller);
        // controller.setConsumer(s->ProgView.displayString(s));
        // view.run();
    }



}
