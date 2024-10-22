package view;

import controller.IProgController;
import exceptions.ExecutionException;

public class ProgView {
    private IProgController controller;

    public ProgView(IProgController controller) {
        this.controller = controller;
    }

    public static void displayString(String input) {
        System.err.println(input);
    }

    public void run() {
        controller.toggleDisplayState();
        try {
            controller.allStep();
        }
        catch (ExecutionException e) {
            System.err.println("Program hault, got error " + e.getMessage());
        }
    }
}
