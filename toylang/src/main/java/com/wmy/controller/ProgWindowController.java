package com.wmy.controller;

import java.util.Map;
import java.util.Map.Entry;

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
    public TableView<Map.Entry<String, IValue>> selectedProgramStateSymTable;

    @FXML
    public ListView<String> selectedProgramStateExeStack;

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
                        .map(e -> Map.entry(e.getKey().toString(), e.getValue()))
                        .toList()));
        heapTable.setItems(FXCollections
                .observableArrayList(program.getSelectedProgramStateHeapTable().entrySet().stream()
                        .map(e -> Map.entry(e.getKey(), e.getValue().getVal()))
                        .toList()));
        outputList.setItems(FXCollections
                .observableArrayList(program.getSelectedProgramStateOutput().stream()
                        .map(Object::toString).toList()));
        fileTable.setItems(
                FXCollections.observableArrayList(program.getSelectedProgramStateFileTable().entrySet().stream()
                        .map(e -> e.getKey().toString())
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

        TableColumn<Map.Entry<Integer, IValue>, Integer> heapAddressCol = new TableColumn<>("Address");
        heapAddressCol.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().getKey()));
        heapTable.getColumns().add(0, heapAddressCol);

        TableColumn<Map.Entry<Integer, IValue>, IValue> heapValueCol = new TableColumn<>("Value");
        heapValueCol.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().getValue()));
        heapTable.getColumns().add(1, heapValueCol);

        TableColumn<Map.Entry<String, IValue>, String> symTableVarNameCol = new TableColumn<>("Variable");
        symTableVarNameCol.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().getKey()));
        selectedProgramStateSymTable.getColumns().add(0, symTableVarNameCol);

        TableColumn<Map.Entry<String, IValue>, IValue> symTableValueCol = new TableColumn<>("Value");
        symTableValueCol.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().getValue()));
        selectedProgramStateSymTable.getColumns().add(1, symTableValueCol);

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
