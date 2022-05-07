package commands;

import collection.CollectionManager;
import common.commands.CommandImpl;
import common.commands.CommandType;
import common.data.Worker;
import common.exceptions.EmptyCollectionException;
import common.exceptions.InvalidDataException;
public class ClearCommand extends CommandImpl{
    private CollectionManager<Worker> collectionManager;
    public ClearCommand(CollectionManager<Worker> cm){
        super("clear",CommandType.NORMAL);
        collectionManager = cm;
    }

    @Override
    public String execute() throws InvalidDataException {
        if(collectionManager.getCollection().isEmpty()) throw new EmptyCollectionException();
        collectionManager.clear();
        return "collection cleared";
    }

}
