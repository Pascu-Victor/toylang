import controller.ProgController;
import models.PrgState;
import models.adt.*;
import models.expressions.ArithExp;
import models.expressions.OpEnum;
import models.expressions.ValueExp;
import models.expressions.VarExp;
import models.statements.AssignStmt;
import models.statements.CompStmt;
import models.statements.IStmt;
import models.statements.PrintStmt;
import models.statements.VarDeclStmt;
import models.types.IntType;
import models.values.*;
import repo.StateRepo;
import view.ProgLexer;
import view.ProgView;

public class App {
    public static void main(String[] args) throws Exception {

        // var source = "int v; int m; v=2; m=3; v=(v+m); nop; print(v); print(m)";
        // var source = "bool a; a=true; int v; if a then v=2 else v=3; print(v)";
        var source = "int a; a=(2+(3*5)); int b; b=((a-(4/2)) + 7); print(b)";
        var lexer = new ProgLexer(source);

        var stmts = new AList<IStmt>();

        while (true) {
            var stmt = lexer.parse();
            if (stmt == null) {
                break;
            }
            stmts.add(stmt);
        }

        //compound statements from list starting in reverse
        IStmt comp = stmts.get(stmts.size() - 1);
        for (int i = stmts.size() - 2; i >= 0; i--) {
            comp = new CompStmt(stmts.get(i), comp);
        }

        var repo = new StateRepo();
        var stk = new AStack<IStmt>();
        var st = new ADict<String, IValue>();
        var out = new AList<IValue>();
        var prg = new PrgState(stk, st, out, comp);
        repo.add(prg);
        var controller = new ProgController(repo);
        var view = new ProgView(controller);
        controller.setConsumer(s -> ProgView.displayString(s));
        view.run();

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
