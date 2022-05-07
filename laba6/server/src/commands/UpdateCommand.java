package commands;

import collection.CollectionManager;
import common.commands.CommandImpl;
import common.commands.CommandType;
import common.data.Worker;
import common.exceptions.*;

import static common.utils.Parser.parseId;
public class UpdateCommand extends CommandImpl{
    private CollectionManager<Worker> collectionManager;
    public UpdateCommand(CollectionManager<Worker> cm){
        super("update",CommandType.NORMAL);
        collectionManager = cm;
    }


    @Override
    public String execute() throws InvalidDataException{
        if(collectionManager.getCollection().isEmpty()) throw new EmptyCollectionException();
        if(!hasStringArg()||!hasWorkerArg()) throw new MissedCommandArgumentException();
        Long id = parseId(getStringArg());
        if(!collectionManager.checkID(id)) throw new InvalidCommandArgumentException("no such id");

        boolean success = collectionManager.updateID(id,getWorkerArg());
        if (success) return "element #" + Long.toString(id) + " updated";
        else throw new CommandException("cannot update");
    }
    
}
