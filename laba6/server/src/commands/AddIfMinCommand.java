package commands;

import collection.CollectionManager;
import common.commands.CommandImpl;
import common.commands.CommandType;
import common.data.Worker;
import common.exceptions.CommandException;
public class AddIfMinCommand extends CommandImpl{
    private CollectionManager<Worker> collectionManager;
    public AddIfMinCommand(CollectionManager<Worker> cm){
        super("add_if_min",CommandType.NORMAL);
        collectionManager = cm;
    }

    @Override
    public String execute() {
        boolean success = collectionManager.addIfMin(getWorkerArg());
        if (success) return ("Added element: " + getWorkerArg().toString());
        else throw new CommandException("cannot add (most often it happens because you forgot to specify the salary)");
    }
    
}
