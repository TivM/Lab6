package commands;

import collection.CollectionManager;
import common.commands.Command;
import common.commands.CommandManager;
import common.connection.AnswerMsg;
import common.connection.Request;
import common.data.Worker;
import common.exceptions.CommandException;
import common.file.ReaderWriter;
import log.*;
import server.*;

public class ServerCommandManager extends CommandManager{
    private Server server;
    private CollectionManager<Worker> collectionManager;
    private ReaderWriter fileManager;

    public ServerCommandManager(Server  serv){
        server = serv;
        collectionManager = server.getCollectionManager();
        fileManager = server.getFileManager();
        addCommand(new ExitCommand());
        addCommand(new HelpCommand());
        addCommand(new ExecuteScriptCommand(this));
        addCommand(new InfoCommand(collectionManager));
        addCommand(new AddCommand(collectionManager));
        addCommand(new SumOfSalaryCommand(collectionManager));
        addCommand(new AverageOfSalaryCommand(collectionManager));
        addCommand(new AddIfMinCommand(collectionManager));
        addCommand(new UpdateCommand(collectionManager));
        addCommand(new RemoveByIdCommand(collectionManager));
        addCommand(new ClearCommand(collectionManager));
        addCommand(new AddIfMaxCommand(collectionManager));
        addCommand(new ShowCommand(collectionManager));
        addCommand(new FilterGreaterThanSalaryCommand(collectionManager));
        addCommand(new SaveCommand(collectionManager,fileManager));
        addCommand(new LoadCommand(collectionManager,fileManager));
    }
    public Server getServer(){
        return server;
    }
    @Override
    public AnswerMsg runCommand(Request msg) {
        AnswerMsg res = new AnswerMsg();
        try {
            Command cmd = getCommand(msg.getCommandName());
            cmd.setArgument(msg);
            res = (AnswerMsg) cmd.run();
        } catch (CommandException e){
            res.error(e.getMessage());
        }
        switch (res.getStatus()){
            case EXIT:
                Log.logger.fatal(res.getMessage());
                server.close();
                break;
            case ERROR:
                Log.logger.error(res.getMessage());
                break;
            default:
                Log.logger.info(res.getMessage());
                break;
        }
        return res;
    }
}
