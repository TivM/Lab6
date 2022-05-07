package commands;

import collection.CollectionManager;
import common.commands.CommandImpl;
import common.commands.CommandType;
import common.data.Worker;
import common.exceptions.CommandException;
import common.exceptions.ConnectionException;
import common.exceptions.FileException;
import common.exceptions.InvalidDataException;

public class AddIfMaxCommand extends CommandImpl{
    private CollectionManager<Worker> collectionManager;
    public AddIfMaxCommand(CollectionManager<Worker> cm){
        super("add_if_max",CommandType.NORMAL);
        collectionManager = cm;
    }

    @Override
    public String execute(){
        boolean success = collectionManager.addIfMax(getWorkerArg());
        if (success) return ("Added element: " + getWorkerArg().toString());
        else throw new CommandException("cannot add(most likely this element is not more than the maximum)");
    }

}
