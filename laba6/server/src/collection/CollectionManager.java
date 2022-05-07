package collection;
import java.util.List;
import java.util.TreeSet;

/**
 * interface for storing elements
 * @param <T> type of elements
 */
public interface CollectionManager<T> {
    /**
     * generates new unique ID for collection
     * @return
     */
    public Long generateNextId();

//    /**
//     * sorts collection
//     */
//    public void sort();

    public TreeSet<T> getCollection();

    /**
     * adds new element
     * @param element
     */
    public void add(T element);

    /**
     * get information about collection
     * @return info
     */
    public String getInfo();

    /**
     * checks if collection contains element with particular id
     * @param ID
     * @return
     */
    public boolean checkID(Long ID);

    /**
     * removes element by id
     * @param id
     */
    public boolean removeByID(Long id);

    /**
     * updates element by id
     * @param id
     * @param newElement
     * @return
     */
    public boolean updateID(Long id, T newElement);

    /**
     * get collection size
     * @return
     */
    public int getSize();
   
    public void clear();

    public boolean addIfMax(T element);


    /**
     * adds element if it is smaller than min
     * @param element
     */
    public boolean addIfMin(T element);

    /**
     * print all elements which name starts with substring
     * @param salary
     */
    public  List<T> printGreaterThanSalary(Long salary);


    /**
     * print the sum of the values of the salary field for all elements of the collection
     */
    public Long sumOfSalary();

    /**
     * print the average value of the salary field for all items in the collection
     */
    public Long averageOfSalary();

    /**
     * convert collection to json
     * @param json
     * @return true if success, false if not
     */
    public boolean deserializeCollection(String json);

    /**
     * parse collection from json
     * @return
     */
    public String serializeCollection();
   
}
