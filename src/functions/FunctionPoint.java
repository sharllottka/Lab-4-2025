package functions;

import java.io.*;

// класс описывает одну точку табулированной функции
public class FunctionPoint implements Serializable {
    private double x;
    private double y;

    // конструктор с координатами
    public FunctionPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // конструктор копирования
    public FunctionPoint(FunctionPoint point) {
        this.x = point.x;
        this.y = point.y;
    }

    // точка (0, 0) по умолчанию
    public FunctionPoint() {
        x = 0;
        y = 0;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + x + "; " + y + ")";
    }
}
