package collection;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import common.data.Worker;
import json.*;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static common.io.OutputManager.printErr;


//@Alias Vector<Worker>  NameToNumbers;
/**
 * Operates collection.
 */
public class WorkerCollectionManager implements CollectionManager<Worker>{
    private TreeSet<Worker> collection;
    private java.time.ZonedDateTime initDate;
    private HashSet<Long> uniqueIds;


    /**
     * Constructor, set start values
     */
    public WorkerCollectionManager() {
        uniqueIds = new HashSet<>();
        collection = new TreeSet<>();
        initDate = java.time.ZonedDateTime.now();
    }


    public Long generateNextId() {

        if (collection.isEmpty())
            return 1L;
        else {
            Long id = collection.last().getId() + 1;
            if (uniqueIds.contains(id)) {
                while (uniqueIds.contains(id)) id += 1;
            }
            uniqueIds.add(id);
            return id;
        }
    }

    /**
     * Return collection
     *
     * @return Collection
     */
    public TreeSet<Worker> getCollection() {
        return collection;
    }

    /**
     * Add element to collection
     *-
     * @param worker Element of collection
     */
    public void add(Worker worker){
        Long id = generateNextId();
        uniqueIds.add(id);
        worker.setId(id);
        collection.add(worker);
    }

    /**
     * Get information about collection
     *
     * @return Information
     */
    public String getInfo() {

        return "TreeSet of Worker, size: " + Integer.toString(collection.size()) + ", initialization date: " + initDate.toString();
    }



    /**
     * Give info about is this ID used
     *
     * @param ID
     * @return is it used or not
     */
    public boolean checkID(Long ID){
        if (uniqueIds.contains(ID)) return true;
        return false;
    }

    /**
     * Delete element by ID
     *
     * @param id ID
     */
    public boolean removeByID(Long id){
        Optional<Worker> worker = collection.stream()
                .filter(w->w.getId()==id)
                .findFirst();
        if(worker.isPresent()){
            collection.remove(worker.get());
            uniqueIds.remove(id);
            return  true;
        }
        return  false;
    }

    /**
     * Update element by ID
     *
     * @param id ID
     * @return
     */
    public boolean updateID(Long id, Worker newWorker) {
        Optional<Worker> worker = collection.stream()
                .filter(w-> w.getId() == id)
                .findFirst();
        if(worker.isPresent()){
            collection.remove(worker.get());
            newWorker.setId(id);
            collection.add(newWorker);
            return true;
        }
        return false;
    }

    /**
     * Get size of collection
     *
     * @return Size of collection
     */
    public int getSize() {
        return collection.size();
    }

    /**
     * clear collection
     */
    public void clear() {

        collection.clear();
        uniqueIds.clear();
    }

    /**
     * remove all items smaller than the specified one from the collection
     * @param worker
     */
    @Override
    public boolean addIfMax(Worker worker){
        if (collection.stream()
                .max(Worker::compareTo)
                .filter(w->w.compareTo(worker)==1)
                .isPresent())
        {
            return false;
        }
        add(worker);
        return true;
    }

    /**
     * Add if ID of element smaller than min in collection
     *
     * @param worker Element
     */
    public boolean addIfMin(Worker worker){
        if (collection.stream()
                .min(Worker::compareTo)
                .filter(w->w.compareTo(worker)==-1)
                .isPresent())
        {
            return false;
        }
        add(worker);
        return true;
    }

    /**
     * output elements whose salary field value is greater than the specified one
     * @param salary
     */
    @Override
    public List<Worker> printGreaterThanSalary(Long salary) {
        List<Worker> list = new LinkedList<>();
        for (Worker worker : collection) {
            if (worker.getSalary() == null) {
                continue;
            }
            else if (worker.getSalary() > salary){
                list.add(worker);

            }
            else
                continue;

        }
        return list;
    }

    /**
     * print the sum of the values of the salary field for all elements of the collection
     * @return
     */
    @Override
    public Long sumOfSalary() {

        Long sum = 0L;
        for (Worker worker : collection) {
            if (worker.getSalary()!=null)
                sum+=worker.getSalary();
            else
                continue;

        }
        return sum;
    }

    /**
     * print the average value of the salary field for all items in the collection
     * @return
     */
    @Override
    public Long averageOfSalary() {

        Long averageSalary = 0L;
        averageSalary = (long)sumOfSalary()/ (long)getSize();
        return averageSalary;
    }


    public boolean deserializeCollection(String json){
        boolean success = true;
        try {
            if (json == null || json.equals("")){
                collection =  new TreeSet<>();
            } else {
                Type collectionType = new TypeToken<TreeSet<Worker>>(){}.getType();
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeDeserializer())
                        .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer())
                        .registerTypeAdapter(collectionType, new CollectionDeserializer(uniqueIds))
                        .create();
                collection = gson.fromJson(json.trim(), collectionType);
            }
        } catch (Exception e){
            success = false;
            printErr("wrong json data");
            System.out.println(e.getMessage());;

        }
        return success;
    }

    public String serializeCollection(){
        if (collection == null || collection.isEmpty()) return "";
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeSerializer())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer())
                .setPrettyPrinting().create();
        String json = gson.toJson(collection);
        return json;
    }
}