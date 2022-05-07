package server;

import collection.CollectionManager;
import collection.WorkerCollectionManager;
import commands.ServerCommandManager;
import common.commands.CommandType;
import common.commands.Commandable;
import common.connection.*;
import common.data.Worker;
import common.exceptions.*;
import common.file.FileManager;
import common.file.ReaderWriter;
import exceptions.ServerOnlyCommandException;
import log.Log;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AlreadyBoundException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.DatagramChannel;
import java.time.ZonedDateTime;
import java.util.Arrays;

/**
 * server class
 */
public class Server extends Thread implements SenderReceiver {

    private CollectionManager<Worker> collectionManager;
    private ReaderWriter fileManager;
    private ServerCommandManager commandManager;
    private int port;
    private InetSocketAddress clientAddress;
    private DatagramChannel channel;

    private volatile boolean running;

    private void init(int p, String path) throws ConnectionException{
        running=true;
        port = p;
        collectionManager = new WorkerCollectionManager();
        fileManager = new FileManager(path);
        commandManager = new ServerCommandManager(this);
        try{
            collectionManager.deserializeCollection(fileManager.read());
        } catch (FileException e){
            Log.logger.error(e.getMessage());
        }
        host(port);
        setName("server thread");
        Log.logger.trace("starting server");
    }

    private void host(int p) throws ConnectionException{
        try{
            if(channel!=null && channel.isOpen()) channel.close();
            channel = DatagramChannel.open();
            channel.bind(new InetSocketAddress(port));
        }
        catch(AlreadyBoundException e){
            throw new PortAlreadyInUseException();
        }
        catch(IllegalArgumentException e){
            throw new InvalidPortException();
        }
        catch(IOException e){
            throw new ConnectionException("something went wrong during server initialization");
        }
    }

    public Server(int p, String path) throws ConnectionException{
        init(p,path);
    }

    /**
     * receives request from client
     * @return
     * @throws ConnectionException
     * @throws InvalidDataException
     */
    public Request receive() throws ConnectionException, InvalidDataException{
        ByteBuffer buf = ByteBuffer.allocate(BUFFER_SIZE);
        try {
            clientAddress = (InetSocketAddress) channel.receive(buf);
            Log.logger.trace("received request from " + clientAddress.toString());
        }catch (ClosedChannelException e){
            throw new ClosedConnectionException();
        } catch(IOException e){
            throw new ConnectionException("something went wrong during receiving request");
        }
        try{
            ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(buf.array()));
            Request req  = (Request) objectInputStream.readObject();
            return req;
        } catch(ClassNotFoundException|ClassCastException|IOException e){
            throw new InvalidReceivedDataException();
        }

    }

    /**
     * sends response
     * @param responses
     * @throws ConnectionException
     */
    public void send(Response[] responses)throws ConnectionException{
        if (clientAddress == null) throw new InvalidAddressException("no client address found");
        for (Response response : responses)
        try{
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(BUFFER_SIZE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(response);
            channel.send(ByteBuffer.wrap(byteArrayOutputStream.toByteArray()), clientAddress);
            System.out.println("packet sent, packets size = "+ responses.length);
            Log.logger.trace("sent response to " + clientAddress.toString());
        } catch(IOException e){
            throw new ConnectionException("something went wrong during sending response");
        }
    }

    /**
     * runs server
     */
    public void run() {
        while (running) {
            AnswerMsg answerMsg = new AnswerMsg();
            try {
                try {
                    Request commandMsg = receive();
                    if (commandMsg.getWorker() != null) {
                        commandMsg.getWorker().setCreationDate(ZonedDateTime.now());
                    }
                    if (commandManager.getCommand(commandMsg).getType() == CommandType.SERVER_ONLY) {
                        throw new ServerOnlyCommandException();
                    }
                    answerMsg = commandManager.runCommand(commandMsg);
                    if (answerMsg.getStatus() == Status.EXIT) {
                        close();
                    }
                } catch (CommandException e) {
                    answerMsg.error(e.getMessage());
                    Log.logger.error(e.getMessage());
                }
                send(splitAnswer(answerMsg));
            } catch (ConnectionException | InvalidDataException e) {
                Log.logger.error(e.getMessage());
                //e.printStackTrace();
            }
        }
    }
    public AnswerMsg[] splitAnswer (AnswerMsg answerMsg){

        int substringSize = 256;
        int totalSubstrings = (int) Math.ceil((double)answerMsg.getMessage().length()/substringSize);
        String[] s = new String[totalSubstrings];
        int index = 0;
        for(int i=0; i < answerMsg.getMessage().length(); i = i + substringSize){
            s[index++] =
                    answerMsg.getMessage().substring(i, Math.min(i + substringSize, answerMsg.getMessage().length()));
        }

        AnswerMsg[] answerMsgs = new AnswerMsg[s.length];
        for (int i = 0; i < s.length; i++) {
            answerMsgs[i] = new AnswerMsg();
            answerMsgs[i].info(s[i]);
            answerMsgs[i].setStatus(answerMsg.getStatus());
            answerMsgs[i].setFinish(false);
        }
        answerMsgs[answerMsgs.length-1].setFinish(true);
        return answerMsgs;
    }

    public void consoleMode(){
        commandManager.consoleMode();
    }

    /**
     * close server and connection
     */
    public void close(){
        try{
            running=false;
            channel.close();
        } catch (IOException e){
            Log.logger.error("cannot close channel");
        }
    }

    public Commandable getCommandManager(){
        return commandManager;
    }
    public ReaderWriter getFileManager(){
        return fileManager;
    }
    public CollectionManager<Worker> getCollectionManager(){
        return collectionManager;
    }

}