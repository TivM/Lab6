package commands;

import collection.CollectionManager;
import common.commands.CommandImpl;
import common.commands.CommandType;
import common.data.Worker;
import common.exceptions.CommandException;
import common.exceptions.InvalidDataException;

import java.util.LinkedList;

public class AddCommand extends CommandImpl{
    private CollectionManager<Worker> collectionManager;
    public AddCommand(CollectionManager<Worker> cm){
        super("add",CommandType.NORMAL);
        collectionManager = cm;
    }
    public CollectionManager<Worker> getManager(){
        return collectionManager;
    }

    @Override
    public String execute() throws InvalidDataException, CommandException {
        getManager().add(getWorkerArg());
        return "Added element: " + getWorkerArg().toString();
    }
}
