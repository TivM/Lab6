package common.connection;

import common.data.Worker;

import java.io.Serializable;
import java.util.LinkedList;

public interface Request extends Serializable{
    public String getStringArg();
    public Worker getWorker();
    public String getCommandName();
}
