package commands;

import collection.CollectionManager;
import common.commands.CommandImpl;
import common.commands.CommandType;
import common.data.Worker;
import common.exceptions.EmptyCollectionException;
public class ShowCommand extends CommandImpl{
    private CollectionManager<Worker> collectionManager;
    public ShowCommand(CollectionManager<Worker> cm){
        super("show",CommandType.NORMAL);
        collectionManager = cm;
    }

    @Override
    public String execute(){
        if (collectionManager.getCollection().isEmpty()) return ("collection is empty");
        else return (collectionManager.serializeCollection());
        //return collectionManager.getCollection().stream().forEach();
    }

}
