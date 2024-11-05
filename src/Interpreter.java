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
        var prg = new PrgState(stk, st, out, fbs, comp);
        var repo = new StateRepo(prg,logFile);
        return new ProgController(repo);
    }

    public static void main(String[] args) throws Exception {
        // var source = "int v; int m; v=2; m=3; v=(v+m); nop; print(v); print(m)";
        // var source = "bool a; a=true; int v; if a then v=2 else v=3; print(v)";
        // var source = "int a; a=(2+(3*5)); int b; b=((a-(4/2)) + 7); print(b)";
        var source = "string varf;\n" + //
                        "varf=\"test.in\";\n" + //
                        "//cats are cool\n" +
                        "openRFile(varf);//caaaaats\n" + //
                        "int varc;\n" + //
                        "readFile(varf,varc);print(varc);\n" + //
                        "readFile(varf,varc);print(varc)\n" + //
                        "closeRFile(varf)";
        // var source = "bool a; a=(1 <= 2)";
        var controller = parseProgram(source, "A.log");
        var command1 = new RunExampleCommand("prg1", "program 1", controller);
        controller.setConsumer(s -> TextMenu.displayString(s));

        var textMenu = new TextMenu();

        textMenu.addCommand(command1);
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
