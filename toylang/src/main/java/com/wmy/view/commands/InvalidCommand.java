package com.wmy.view.commands;

public class InvalidCommand extends Command {
    public InvalidCommand(String key, String err) {
        super(key, "error in program: \n" + err);
    }

    @Override
    public void execute() {
    }

}
