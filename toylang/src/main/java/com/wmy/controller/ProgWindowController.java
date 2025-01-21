package com.wmy.controller;

import com.wmy.models.adt.Entry;
import com.wmy.models.adt.IList;
import com.wmy.models.adt.IProcTable.ProcTableEntry;

import com.wmy.exceptions.ExecutionException;
import com.wmy.models.PrgState;
import com.wmy.models.values.IValue;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.collections.ListChangeListener;
import javafx.beans.property.SimpleObjectProperty;

public class ProgWindowController {

    public ProgWindowController(IProgController program) {
        this.program = program;
    }

    private int selectedProgramStateIndex = 0;

    private IProgController program;

    @FXML
    public TextField prgStateCount;

    @FXML
    public TableView<Entry<Integer, IValue>> heapTable;

    @FXML
    public ListView<String> outputList;

    @FXML
    public ListView<String> fileTable;

    @FXML
    public ListView<PrgState> programStateList;

    @FXML
    public TableView<Entry<String, IValue>> selectedProgramStateSymTable;

    @FXML
    public ListView<String> selectedProgramStateExeStack;

    @FXML
    public TableView<Entry<Integer, Integer>> latchTable;

    @FXML
    public TableView<Entry<String, ProcTableEntry>> procedureTable;

    @FXML
    public TableView<Entry<Integer, Entry<Integer, IList<Integer>>>> semaphoreTable;

    @FXML
    public Button runOneStepButton;

    public void runOneStep() {
        try {
            program.runOneStep();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void runAllSteps() {
        try {
            program.allStep();
            updateUi();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        com.wmy.Interpreter.switchToPrimary();
    }

    public void updateUi() {
        if (program.getSelectedProgramState() == null) {
            return;
        }
        prgStateCount.setText(String.valueOf(program.getProgramStateList().size()));

        selectedProgramStateExeStack.setItems(FXCollections.observableArrayList(
                program.getSelectedProgramStateExeStack().stream()
                        .map(Object::toString).toList()));
        selectedProgramStateSymTable.setItems(FXCollections
                .observableArrayList(program.getSelectedProgramStateSymTable().entrySet().stream()
                        .map(e -> new Entry<String, IValue>(e.getKey().toString(), e.getValue()))
                        .toList()));
        heapTable.setItems(FXCollections
                .observableArrayList(program.getSelectedProgramStateHeapTable().entrySet().stream()
                        .map(e -> new Entry<Integer, IValue>(e.getKey(), e.getValue().getVal()))
                        .toList()));
        outputList.setItems(FXCollections
                .observableArrayList(program.getSelectedProgramStateOutput().stream()
                        .map(Object::toString).toList()));
        fileTable.setItems(
                FXCollections.observableArrayList(program.getSelectedProgramStateFileTable().entrySet().stream()
                        .map(e -> e.getKey().toString())
                        .toList()));
        latchTable.setItems(
                FXCollections
                        .observableArrayList(program.getSelectedProgramStateLatchTable().entrySet().stream().toList()));
        procedureTable.setItems(
                FXCollections.observableArrayList(program.getSelectedProgramStateProcedureTable().entrySet().stream()
                        .map(e -> new Entry<String, ProcTableEntry>(e.getKey().toString(), e.getValue()))
                        .toList()));
        semaphoreTable.setItems(
                FXCollections.observableArrayList(program.getSelectedProgramStateSemaphoreTable().entrySet().stream()
                        .map(e -> new Entry<Integer, Entry<Integer, IList<Integer>>>(e.getKey(), e.getValue()))
                        .toList()));
    }

    @FXML
    public void initialize() {
        programStateList.setItems(program.getProgramStateList());
        programStateList.setCellFactory(_ -> new ListCell<>() {
            @Override
            protected void updateItem(PrgState state, boolean empty) {
                super.updateItem(state, empty);
                if (empty || state == null) {
                    setText(null);
                } else {
                    setText("Program ID: " + state.getId());
                }
            }
        });

        TableColumn<Entry<Integer, IValue>, Integer> heapAddressCol = new TableColumn<>("Address");
        heapAddressCol.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().getKey()));
        heapTable.getColumns().add(0, heapAddressCol);

        TableColumn<Entry<Integer, IValue>, IValue> heapValueCol = new TableColumn<>("Value");
        heapValueCol.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().getValue()));
        heapTable.getColumns().add(1, heapValueCol);

        TableColumn<Entry<String, IValue>, String> symTableVarNameCol = new TableColumn<>("Variable");
        symTableVarNameCol.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().getKey()));
        selectedProgramStateSymTable.getColumns().add(0, symTableVarNameCol);

        TableColumn<Entry<String, IValue>, IValue> symTableValueCol = new TableColumn<>("Value");
        symTableValueCol.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().getValue()));
        selectedProgramStateSymTable.getColumns().add(1, symTableValueCol);

        TableColumn<Entry<Integer, Integer>, Integer> latchTableIndexCol = new TableColumn<>("Index");
        latchTableIndexCol.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().getKey()));
        latchTable.getColumns().add(0, latchTableIndexCol);

        TableColumn<Entry<Integer, Integer>, Integer> latchTableValueCol = new TableColumn<>("Value");
        latchTableValueCol.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().getValue()));
        latchTable.getColumns().add(1, latchTableValueCol);

        TableColumn<Entry<String, ProcTableEntry>, String> procTableProcNameCol = new TableColumn<>("Procedure");
        procTableProcNameCol.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().getKey()));
        procedureTable.getColumns().add(0, procTableProcNameCol);

        TableColumn<Entry<Integer, Entry<Integer, IList<Integer>>>, Integer> semaphoreTableIndexCol = new TableColumn<>(
                "Semaphore Id");
        semaphoreTableIndexCol.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().getKey()));
        semaphoreTable.getColumns().add(0, semaphoreTableIndexCol);

        TableColumn<Entry<Integer, Entry<Integer, IList<Integer>>>, Integer> semaphoreTableValueCol = new TableColumn<>(
                "Value");
        semaphoreTableValueCol.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().getValue().getKey()));
        semaphoreTable.getColumns().add(1, semaphoreTableValueCol);

        TableColumn<Entry<Integer, Entry<Integer, IList<Integer>>>, String> semaphoreTableListCol = new TableColumn<>(
                "Aquire List");
        semaphoreTableListCol.setCellValueFactory(p -> new SimpleObjectProperty<>(
                p.getValue().getValue().getValue().stream().map(e -> e.toString())
                        .reduce((a, b) -> a + ", " + b.toString()).orElse("")));
        semaphoreTable.getColumns().add(2, semaphoreTableListCol);

        TableColumn<Entry<String, ProcTableEntry>, String> procTableProcCodeCol = new TableColumn<>("Code");
        procTableProcCodeCol.setCellValueFactory(p -> {
            var pe = p.getValue().getValue();
            var params = pe.getParams();
            var procString = "("
                    + params.stream().map(e -> e.getValue() + " " + e.getKey()).reduce((a, b) -> a + ", " + b)
                            .orElse("")
                    + ") {\n"
                    + pe.getProcedure()
                    + "\n}";
            return new SimpleObjectProperty<>(procString);
        });
        procedureTable.getColumns().add(1, procTableProcCodeCol);

        this.program.getProgramStateList().addListener((ListChangeListener<PrgState>) (_) -> {
            programStateList.setItems(program.getProgramStateList());
            if (program.getSelectedProgramState() == null) {
                var index = Math.max(0, Math.min(selectedProgramStateIndex, program.getProgramStateList().size() - 1));
                program.setSelectedProgramState(program.getProgramStateList().get(index));
            }
            this.updateUi();
        });

        programStateList.getSelectionModel().selectedItemProperty().addListener((_, _, newVal) -> {
            program.setSelectedProgramState(newVal);
            if (programStateList.getSelectionModel().getSelectedIndex() != -1) {
                this.selectedProgramStateIndex = programStateList.getSelectionModel().getSelectedIndex();
            }
            this.updateUi();
        });
    }

}
