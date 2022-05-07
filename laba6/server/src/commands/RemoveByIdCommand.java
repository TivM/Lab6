package commands;

import collection.CollectionManager;
import common.commands.CommandImpl;
import common.commands.CommandType;
import common.data.Worker;
import common.exceptions.*;

import static common.utils.Parser.parseId;
public class RemoveByIdCommand extends CommandImpl{
    private CollectionManager<Worker> collectionManager;
    public RemoveByIdCommand(CollectionManager<Worker> cm){
        super("remove_by_id",CommandType.NORMAL);
        collectionManager = cm;
    }


    @Override
    public String execute() throws InvalidDataException {
        if(collectionManager.getCollection().isEmpty()) throw new EmptyCollectionException();
        if(!hasStringArg()) throw new MissedCommandArgumentException();
        Long id = parseId(getStringArg());
        if(!collectionManager.checkID(id)) throw new InvalidCommandArgumentException("no such id");

        boolean success = collectionManager.removeByID(id);
        if (success) return "element #" + Long.toString(id) + " removed";
        else throw new CommandException("cannot remove");
    }
    
}
