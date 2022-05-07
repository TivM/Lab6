package commands;

import collection.CollectionManager;
import common.commands.CommandImpl;
import common.commands.CommandType;
import common.data.Worker;
import common.exceptions.InvalidDataException;

public class InfoCommand extends CommandImpl{
    private CollectionManager<Worker> collectionManager;
    public InfoCommand(CollectionManager<Worker> cm){
        super("info",CommandType.NORMAL);
        collectionManager = cm;
    }

    @Override
    public String execute() throws InvalidDataException {
        return collectionManager.getInfo();
    }

}
