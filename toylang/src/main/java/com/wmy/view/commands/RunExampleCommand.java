package com.wmy.view.commands;

import com.wmy.controller.IProgController;
import com.wmy.exceptions.ExecutionException;

public class RunExampleCommand extends Command {
    private IProgController ctr;

    public RunExampleCommand(String key, String desc, IProgController ctr) {
        super(key, desc);
        this.ctr = ctr;
    }

    @Override
    public void execute() {
        try {
            this.ctr.allStep();
        } catch (ExecutionException e) {
            System.err.println(e.toString());
        }
    }

    public IProgController getCtr() {
        return ctr;
    }
}
