package functions;

import java.io.*;
import java.util.Arrays;
import java.util.Comparator;

public class ArrayTabulatedFunction implements TabulatedFunction, Serializable {
// public class ArrayTabulatedFunction implements TabulatedFunction, Externalizable {
    private FunctionPoint[] points;
    private int pointsCount;
    private static final double EPS = 1e-9;


    // конструктор, если нужно создать функцию с нулями
    public ArrayTabulatedFunction(double leftX, double rightX, int pointsCount) {
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница >= правой");
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек < 2");
        }

        this.pointsCount = pointsCount;
        points = new FunctionPoint[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);

        for (int i = 0; i < pointsCount; i++) {
            points[i] = new FunctionPoint(leftX + i * step, 0);
        }
    }

    // конструктор с уже готовыми значениями y
    public ArrayTabulatedFunction(double leftX, double rightX, double[] values) {
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница >= правой");
        }
        if (values.length < 2) {
            throw new IllegalArgumentException("Количество точек < 2");
        }

        pointsCount = values.length;
        points = new FunctionPoint[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);

        for (int i = 0; i < pointsCount; i++) {
            points[i] = new FunctionPoint(leftX + i * step, values[i]);
        }
    }

    public ArrayTabulatedFunction(FunctionPoint[] points) {
        if (points == null || points.length < 2) {
            throw new IllegalArgumentException("Количество точек < 2");
        }

        Arrays.sort(points, Comparator.comparingDouble(FunctionPoint::getX));
        for (int i = 0; i < points.length - 1; i++) {
            if (Math.abs(points[i + 1].getX() - points[i].getX()) <= EPS) {
                throw new IllegalArgumentException("Такая точка уже есть");
            }
        }
        this.points = new FunctionPoint[points.length];
        for (int i = 0; i < points.length; i++) {
            this.points[i] = new FunctionPoint(points[i]);
        }
        this.pointsCount = points.length;
    }

    @Override
    public int getPointsCount() {
        return pointsCount;
    }

    @Override
    public double getLeftDomainBorder() {
        return points[0].getX();
    }

    @Override
    public double getRightDomainBorder() {
        return points[pointsCount - 1].getX();
    }

    @Override
    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            return Double.NaN;
        }

        for (int i = 0; i < pointsCount - 1; i++) {
            double x1 = points[i].getX();
            double x2 = points[i + 1].getX();
            if (x >= x1 && x <= x2) {
                double y1 = points[i].getY();
                double y2 = points[i + 1].getY();
                double k = (y2 - y1) / (x2 - x1);
                return y1 + k * (x - x1);
            }
        }

        return Double.NaN;
    }

    @Override
    public FunctionPoint getPoint(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Неверный индекс: " + index);
        }
        return new FunctionPoint(points[index]);
    }

    @Override
    public void setPoint(int index, FunctionPoint point)
            throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {

        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Неверный индекс");
        }

        // проверяем, чтобы x не нарушил порядок
        if ((index > 0 && point.getX() < points[index - 1].getX() + EPS) ||
                (index < pointsCount - 1 && point.getX() > points[index + 1].getX() - EPS)) {
            throw new InappropriateFunctionPointException("x нарушает порядок точек");
        }

        points[index] = new FunctionPoint(point);
    }

    @Override
    public double getPointX(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Неверный индекс");
        }
        return points[index].getX();
    }

    @Override
    public void setPointX(int index, double x)
            throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {

        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Неверный индекс");
        }

        if ((index > 0 && x <= points[index - 1].getX() + EPS) ||
                (index < pointsCount - 1 && x >= points[index + 1].getX() - EPS)) {
            throw new InappropriateFunctionPointException("x нарушает порядок точек");
        }

        points[index].setX(x);
    }

    @Override
    public double getPointY(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Неверный индекс");
        }
        return points[index].getY();
    }

    @Override
    public void setPointY(int index, double y) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Неверный индекс");
        }
        points[index].setY(y);
    }

    @Override
    public void deletePoint(int index) {
        if (pointsCount <= 2) {
            throw new IllegalStateException("Нельзя удалить — останется меньше двух точек");
        }

        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Неверный индекс");
        }

        FunctionPoint[] newArr = new FunctionPoint[pointsCount - 1];
        for (int i = 0, j = 0; i < pointsCount; i++) {
            if (i != index) {
                newArr[j++] = points[i];
            }
        }
        points = newArr;
        pointsCount--;
    }

    @Override
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        // проверка на совпадение x
        for (int i = 0; i < pointsCount; i++) {
            if (Math.abs(points[i].getX() - point.getX()) < EPS) {
                throw new InappropriateFunctionPointException("Такая точка уже есть");
            }
        }

        // создаем новый массив на 1 больше
        FunctionPoint[] newArr = new FunctionPoint[pointsCount + 1];
        int i = 0;
        while (i < pointsCount && points[i].getX() < point.getX() - EPS) {
            newArr[i] = points[i];
            i++;
        }

        newArr[i] = new FunctionPoint(point);

        for (int j = i; j < pointsCount; j++) {
            newArr[j + 1] = points[j];
        }

        points = newArr;
        pointsCount++;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("ArrayTabulatedFunction:\n");
        for (int i = 0; i < pointsCount; i++) {
            sb.append(i).append(": (")
                    .append(points[i].getX()).append(", ")
                    .append(points[i].getY()).append(")\n");
        }
        return sb.toString();
    }

    /* @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(pointsCount);
        for (int i = 0; i < pointsCount; i++) {
            out.writeDouble(points[i].getX());
            out.writeDouble(points[i].getY());
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        pointsCount = in.readInt();
        points = new FunctionPoint[pointsCount];
        for (int i = 0; i < pointsCount; i++) {
            double x = in.readDouble();
            double y = in.readDouble();
            points[i] = new FunctionPoint(x, y);
        }
    }*/
}
