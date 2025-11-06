package functions;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TabulatedFunctions {

    private TabulatedFunctions() {}

    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount) {
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница >= правой");
        }
        double step = (rightX - leftX) / (pointsCount - 1);
        List<FunctionPoint> validPoints = new ArrayList<>();
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            double y = function.getFunctionValue(x);
            if (Double.isNaN(y) || Double.isInfinite(y)) {
                continue;
            }
            validPoints.add(new FunctionPoint(x, y));;
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек < 2");
        }
        FunctionPoint[] points = validPoints.toArray(new FunctionPoint[0]);
        return new ArrayTabulatedFunction(points);
    }

    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out) throws IOException {
        DataOutputStream dataOut = new DataOutputStream(out);
        int pointsCount = function.getPointsCount();
        dataOut.writeInt(pointsCount);
        for (int i = 0; i < pointsCount; i++) {
            dataOut.writeDouble(function.getPointX(i));
            dataOut.writeDouble(function.getPointY(i));
        }
        dataOut.flush();
    }

    public static TabulatedFunction inputTabulatedFunction(InputStream in) throws IOException {
        DataInputStream dataIn = new DataInputStream(in);
        int pointsCount = dataIn.readInt();
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек < 2");
        }
        FunctionPoint[] points = new FunctionPoint[pointsCount];
        for (int i = 0; i < pointsCount; i++) {
            double x = dataIn.readDouble();
            double y = dataIn.readDouble();
            points[i] = new FunctionPoint(x, y);
        }
        return new ArrayTabulatedFunction(points);
    }

    public static void writeTabulatedFunction(TabulatedFunction function, Writer out) throws IOException {
        out.write(String.valueOf(function.getPointsCount()));
        out.write(" ");
        for (int i = 0; i < function.getPointsCount(); i++) {
            out.write(String.valueOf(function.getPointX(i)));
            out.write(" ");
            out.write(String.valueOf(function.getPointY(i)));
            out.write(" ");
        }
        out.write("\n");
        out.flush();
    }

    public static TabulatedFunction readTabulatedFunction(Reader in) throws IOException{
        StreamTokenizer st = new StreamTokenizer(in);
        st.nextToken();
        int pointsCount = (int) st.nval;
        FunctionPoint[] points = new FunctionPoint[pointsCount];
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек < 2");
        }
        for (int i = 0; i < pointsCount; i++) {
            st.nextToken();
            double x = st.nval;
            st.nextToken();
            double y = st.nval;
            points[i] = new FunctionPoint(x, y);
        }
        return new ArrayTabulatedFunction(points);
    }
}
