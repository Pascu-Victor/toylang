package view.commands;

import controller.IProgController;
import exceptions.ExecutionException;

public class RunExampleCommand extends Command {
    private IProgController ctr;
    public RunExampleCommand(String key, String desc, IProgController ctr){
        super(key, desc);
        this.ctr=ctr;
    }
    @Override
    public void execute() {
        try {
            this.ctr.allStep();
        } catch(ExecutionException e) {
            System.err.println(e.toString());
        }
    }
}
