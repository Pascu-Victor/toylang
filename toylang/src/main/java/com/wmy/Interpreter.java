package com.wmy;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.wmy.controller.IProgController;
import com.wmy.controller.ProgController;
import com.wmy.controller.ProgWindowController;
import com.wmy.exceptions.TypeException;
import com.wmy.models.PrgState;
import com.wmy.models.adt.*;
import com.wmy.models.statements.CompStmt;
import com.wmy.models.statements.IStmt;
import com.wmy.models.types.IType;
import com.wmy.models.values.*;
import com.wmy.repo.StateRepo;
import com.wmy.view.ProgLexer;
import com.wmy.view.commands.Command;
import com.wmy.view.commands.RunExampleCommand;
import com.wmy.view.commands.InvalidCommand;

public class Interpreter extends Application {

    private static Scene scene;
    private static Parent primaryScene;
    private static ListView<?> listView;

    private static IProgController parseProgram(String source, List<String> procedures, String logFile)
            throws TypeException {
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

        var typeEnv = new ADict<CloneableString, IType>();

        var procTable = new ProcTable();
        if (procedures == null) {
            procedures = new ArrayList<>();
        }
        for (var proc : procedures) {
            var lexerProc = new ProgLexer(proc);
            var procedureInfo = lexerProc.parseProcedure();
            if (procedureInfo == null) {
                continue;
            }
            procTable.set(procedureInfo.getKey(), procedureInfo.getValue());
        }

        comp.typecheck(typeEnv);

        var stk = new AStack<IStmt>();
        var sts = new AStack<IDict<CloneableString, IValue>>();
        var st = new ADict<CloneableString, IValue>();
        sts.push(st);
        var out = new AList<IValue>();
        var fbs = new ADict<CloneableString, CloneableBufferedReader>();
        var heap = new Heap();
        var latchTable = new LatchTable();
        var prg = new PrgState(stk, sts, out, fbs, heap, latchTable, procTable, comp);
        var repo = new StateRepo(prg, logFile);
        return new ProgController(repo);
    }

    private static Command command(String logName, String progKey, String progDesc, String src) {
        try {
            var c = parseProgram(src, null, logName);
            return new RunExampleCommand(progKey, progDesc, c);
        } catch (TypeException e) {
            return new InvalidCommand(progKey, e.toString());
        }
    }

    private static Command commandWithProcedures(String logName, String progKey, String progDesc, String src,
            List<String> procedures) {
        try {
            var c = parseProgram(src, procedures, logName);
            return new RunExampleCommand(progKey, progDesc, c);
        } catch (TypeException e) {
            return new InvalidCommand(progKey, e.toString());
        }
    }

    private static void loadCommands() {
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
        var sourceGC1 = "Ref int v; Ref int c; new(c,200);new(v,20);Ref Ref int a; new(a,v); new(v,30);print(rH(rH(a)));new(a,c)";
        var sourceGC2 = "Ref int v; Ref Ref Ref int c;new(v,20);Ref Ref int a; new(a,v);new(c,a); new(v,30);print(rH(rH(rH(c))))";
        var sourcewhile = "int v; v=4; while ((v>0)){ print(v);v=(v-1);print(v)}";
        var sourceFork = "int v; Ref int a; v=10;new(a,22);\n" + //
                "fork({wH(a,30);v=32;print(v);print(rH(a))});\n" + //
                "print(v);print(rH(a))";
        var sourceForkFork = "int v; Ref int a; v=10;new(a,22);\n" + //
                "fork({" + //
                "   fork({wH(a,30);v=32;print(v);print(rH(a))});\n" + //
                "wH(a,30);v=32;print(v);print(rH(a))" + //
                "});\n" + //
                "print(v);print(rH(a))";
        var sourceForkConsec = "int v; Ref int a; v=10;new(a,22);\n" + //
                "fork({wH(a,30);v=32;print(v);print(rH(a))});\n" + //
                "fork({wH(a,30);v=32;print(v);print(rH(a))});\n" + //
                "print(v);print(rH(a))";
        var sourceSwitchExp = "int v; v=10; switch(v) { case(1) { print(1) } case(2) { print(2) } default { print(3) } }";
        var sourceSwitchExpNodefault = "int v; v=10; switch(v) { case(1) { print(1) } case(3) { print(3) } }";

        var sourceCountdownLatch = "Ref int v1; Ref int v2; Ref int v3; int cnt;\n" +
                "new(v1,2);new(v2,3);new(v3,4);newLatch(cnt,rH(v2));\n" +
                "fork({\n" +
                "    wH(v1,(rH(v1)*10));\n" +
                "    print(rH(v1));\n" +
                "    countDown(cnt);\n" +
                "    fork({\n" +
                "        wH(v2,(rH(v2)*10));\n" +
                "        print(rH(v2));\n" +
                "        countDown(cnt);\n" +
                "        fork({\n" +
                "            wH(v3,(rH(v3)*10));\n" +
                "            print(rH(v3));\n" +
                "            countDown(cnt)\n" +
                "        })\n" +
                "    })\n" +
                "});\n" +
                "await(cnt);\n" +
                "print(100);\n" +
                "countDown(cnt);\n" +
                "print(100)";

        var sourceRepeatUntil = "int x; int y; int z; int w; int v;" + //
                "v=0;\n" + //
                "repeat { fork({ print(v); v=(v-1) }); v=(v+1) } until ((v==3));\n" + //
                "x=1;y=2;z=3;w=4;\n" + //
                "print((v*10))";

        var sourceInvalidType = "int v; v=\"123\"";

        var sourceProcedures1 = "call p(); call p();";
        var sourceProcedureSum = "procedure sum(int a, int b){v=(a+b);print(v)}";
        var sourceProcedureProduct = "procedure product(int a, int b){v=(a*b);print(v)}";
        var sourceProcedures2 = "int v; v=2; int w; w=5" + //
                "call sum((v*10),w); print(v)" + //
                "fork({ call product(v,w); " + //
                "       fork({ call sum(v,w) })" + //
                "});";

        var programListUnchecked = scene.lookup("#programList");

        if (!(programListUnchecked instanceof ListView<?>)) {
            throw new RuntimeException("programList is not a ListView");
        }

        @SuppressWarnings("unchecked")
        ListView<Command> programList = (ListView<Command>) programListUnchecked;

        programList.setCellFactory(_ -> {
            return new ListCell<>() {
                @Override
                protected void updateItem(Command command, boolean empty) {
                    super.updateItem(command, empty);
                    if (empty || command == null) {
                        setText(null);
                    } else {
                        setText(command.toString());
                        if (command instanceof InvalidCommand) {
                            setStyle("-fx-background-color: #ff0000; -fx-text-fill: #ffffff;");
                        } else {
                            setStyle("");
                        }
                    }
                }
            };
        });

        var commands = new ArrayList<Command>();

        commands.add(command("prg1.log", "prg1", "basic print", source1));
        commands.add(command("prg2.log", "prg2", "if statement", source2));
        commands.add(command("prg3.log", "prg3", "arithmetic", source3));
        commands.add(command("prg4.log", "prg4", "open file", source4));
        commands.add(command("prg5.log", "prg5", "logical ops", source5));
        commands.add(command("prgNew.log", "New", "New program", sourceNew));
        commands.add(command("prgrH.log", "rH", "rH program", sourcerH));
        commands.add(command("prgwH.log", "wH", "wH program", sourcewH));
        commands.add(command("prgGC1.log", "GC1", "GC program 1", sourceGC1));
        commands.add(command("prgGC2.log", "GC2", "GC program 2", sourceGC2));
        commands.add(command("prgwhile.log", "while", "while program", sourcewhile));
        commands.add(command("prgFork.log", "fork", "fork program", sourceFork));
        commands.add(command("prgForkFork.log", "forkfork", "fork with a fork in it", sourceForkFork));
        commands.add(command("prgForkConsec.log", "forkconsec", "two consecutive forks", sourceForkConsec));
        commands.add(command("prgInvalid.log", "invalid", "invalid program", sourceInvalidType));
        commands.add(command("prgRepeatUntil.log", "repeatUntil", "repeat until expression", sourceRepeatUntil));
        commands.add(command("prgCountdownLatch.log", "countdown", "Countdown latch program", sourceCountdownLatch));
        commands.add(command("prgSwitchExp.log", "switchexp", "switch expression", sourceSwitchExp));
        commands.add(command("prgSwitchExpNodefault.log", "switchexpnodefault", "switch expression without default",
                sourceSwitchExpNodefault));
        commands.add(commandWithProcedures("prgProc.log", "proc", "procedure program", sourceProcedures1,
                List.of("procedure p() { int v; v=2; print(v) }")));
        commands.add(commandWithProcedures("prgProc2.log", "proc2", "procedure program 2", sourceProcedures2,
                List.of(sourceProcedureSum, sourceProcedureProduct)));
        programList.setItems(FXCollections
                .observableArrayList(commands.stream().filter(c -> c != null).collect(Collectors.toList())));

        Interpreter.listView = programList;
    }

    public static void switchToPrimary() {
        scene.setRoot(primaryScene);
        loadCommands();
    }

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(Interpreter.class.getResource("primary.fxml"));
        primaryScene = fxmlLoader.load();
        scene = new Scene(primaryScene, 640, 480);
        stage.setScene(scene);

        var programListUnchecked = scene.lookup("#programList");

        if (!(programListUnchecked instanceof ListView<?>)) {
            throw new RuntimeException("programList is not a ListView");
        }

        @SuppressWarnings("unchecked")
        ListView<Command> programList = (ListView<Command>) programListUnchecked;

        programList.setOnMouseClicked(e -> {
            var item = ((ListView<?>) e.getSource()).getSelectionModel().getSelectedItem();
            if (item != null) {
                var command = (Command) item;
                if (command instanceof RunExampleCommand) {
                    try {
                        FXMLLoader fxmlLoaderSecondary = new FXMLLoader(
                                Interpreter.class.getResource("secondary.fxml"));
                        fxmlLoaderSecondary.setControllerFactory(
                                _ -> new ProgWindowController(((RunExampleCommand) command).getCtr()));
                        Parent fxml = fxmlLoaderSecondary.load();
                        scene.setRoot(fxml);
                    } catch (IOException ex) {
                        // ex.printStackTrace();
                        throw new RuntimeException(ex);
                    }
                }
            }
        });

        loadCommands();

        stage.show();
    }

    public static ListView<?> getListView() {
        return listView;
    }

    public static void main(String[] args) throws Exception {

        // var textMenu = new TextMenu();

        launch(args);

        // textMenu.addCommand(new ExitCommand("exit", "exit program"));

        // textMenu.show();

        // var repo = new StateRepo();
        // // var ex1 = new CompStmt(
        // // new VarDeclStmt("v", new IntType()),
        // // new CompStmt(
        // // new AssignStmt("v", new ValueExp(new IntValue((2)))),
        // // new CompStmt(
        // // new VarDeclStmt("m", new IntType()),
        // // new CompStmt(
        // // new AssignStmt("m", new ValueExp(new IntValue((2)))),
        // // new CompStmt(
        // // new AssignStmt("v",
        // // new ArithExp(
        // // new VarExp("m"),
        // // new VarExp("v"),
        // // OpEnum.DIVIDE
        // // )
        // // ),
        // // new PrintStmt(new VarExp("v"))
        // // )
        // // )
        // // )
        // // )
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
