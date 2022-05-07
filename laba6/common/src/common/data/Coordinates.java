package common.data;

import java.io.Serializable;

public class Coordinates implements Validateable, Serializable {
    private double x; //Максимальное значение поля: 850
    private Integer y; //Поле не может быть null
    public Coordinates(double x, Integer y){
        this.x = x;
        this.y = y;
    }

    /**
     * @return x coord
     */
    public double getX() {
        return x;
    }

    /**
     * @return y coord
     */
    public Integer getY() {
        return y;
    }

    @Override
    public String toString(){
        String s = "";
        s += "{\"x\" : " + Double.toString(x) + ", ";
        s += "\"y\" : " + Integer.toString(y) + "}";
        return s;
    }

    @Override
    public boolean validate(){
        return !(y==null || (Double)x > 850 || Double.isInfinite(x) || Double.isNaN(x));
    }


}
