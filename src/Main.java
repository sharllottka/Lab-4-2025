import functions.*;
import functions.basic.Cos;
import functions.basic.Exp;
import functions.basic.Log;
import functions.basic.Sin;
import functions.Function;
import functions.meta.Composition;


import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        double EPS = 1e-9;
        double[] vals = {0, 1, 4, 9, 16};
        FunctionPoint[] points = {
                new FunctionPoint(0.0, 0.0),
                new FunctionPoint(1.0, 1.0),
                new FunctionPoint(2.0, 4.0),
                new FunctionPoint(3.0, 6.0)
        };

        Function sin = new Sin();
        Function cos = new Cos();
        for (double i = 0; i <= Math.PI + EPS; i += 0.1) {
            System.out.println("x = " + i + ", sin(x) = " + sin.getFunctionValue(i) + ", cos(x) = " + cos.getFunctionValue(i));
        }


        TabulatedFunction tabSin = TabulatedFunctions.tabulate(sin, 0, Math.PI, 10);
        TabulatedFunction tabCos = TabulatedFunctions.tabulate(cos, 0, Math.PI, 10);
        double right = tabSin.getRightDomainBorder();
        double left = tabSin.getLeftDomainBorder();
        double step = (right - left) / 10;
        for (double i = left; i <= right + EPS; i += step) {
            System.out.println("x = " + i + ", sin(x) = " + sin.getFunctionValue(i) + ", tabsin(x) = " + tabSin.getFunctionValue(i));
            System.out.println("x = " + i + ", cos(x) = " + cos.getFunctionValue(i) + ", tabcos(x) = " + tabCos.getFunctionValue(i));
        }

        Function tabSin2 = Functions.power(tabSin, 2);
        Function tabCos2 = Functions.power(tabCos, 2);
        Function sumSquares = Functions.sum(tabSin2, tabCos2);
        for (double i = left; i <= right + EPS; i += step) {
            System.out.println("x = " + i + ", sin^2 + cos^2 = " + sumSquares.getFunctionValue(i));
        }

        Function exp = new Exp();
        TabulatedFunction tabExp = TabulatedFunctions.tabulate(exp, 0, 10, 11);
        try (Writer out = new FileWriter("exp.txt")) {
            TabulatedFunctions.writeTabulatedFunction(tabExp, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        TabulatedFunction readExp = null;
        try (Reader in = new FileReader("exp.txt")) {
            readExp = TabulatedFunctions.readTabulatedFunction(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (double i = 0; i <= 10; i++) {
            System.out.println("x = " + i + ", exp(x) = " + exp.getFunctionValue(i) + ", tabExp(x) = " + tabExp.getFunctionValue(i) + ", readExp(x) = " + readExp.getFunctionValue(i));
        }

        Function log = new Log(Math.E);
        TabulatedFunction tabLog = TabulatedFunctions.tabulate(log, 0, 10, 11);
        try (OutputStream out = new FileOutputStream("log.txt")) {
            TabulatedFunctions.outputTabulatedFunction(tabLog, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        TabulatedFunction inputLog = null;
        try (InputStream in = new FileInputStream("log.txt")) {
            inputLog = TabulatedFunctions.inputTabulatedFunction(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (double i = 0.1; i <= 10; i++) {
            System.out.println("x = " + i + ", log(x) = " + log.getFunctionValue(i) + ", tabLog(x) = " + tabLog.getFunctionValue(i) + ", readLog(x) = " + inputLog.getFunctionValue(i));
        }

        Function logFunc = new Log(Math.E);
        Function logExp = new Composition(logFunc, exp);

        TabulatedFunction arrayTabLogExp = TabulatedFunctions.tabulate(logExp, 0.1, 10, 11);
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("arrayLogExp.ser"))) {
            out.writeObject(arrayTabLogExp);
        } catch (IOException e) {
            e.printStackTrace();
        }
        TabulatedFunction readArrayTabLogExp = null;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("arrayLogExp.ser"))) {
            readArrayTabLogExp = (TabulatedFunction) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        TabulatedFunction linkedTabLogExp = new LinkedListTabulatedFunction(arrayTabLogExp);
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("linkedLogExp.ser"))) {
            out.writeObject(linkedTabLogExp);
        } catch (IOException e) {
            e.printStackTrace();
        }
        TabulatedFunction readLinkedTabLogExp = null;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("linkedLogExp.ser"))) {
            readLinkedTabLogExp = (LinkedListTabulatedFunction) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        for (double i = 0.1; i <= 10; i++) {
            System.out.println("x = " + i + ", log(exp(x)) = " + logExp.getFunctionValue(i) + ", arrayTabLogExp(x) = " + arrayTabLogExp.getFunctionValue(i) + ", readArrayTabLogExp(x) = " + readArrayTabLogExp.getFunctionValue(i)
                    + ", linkedTabLogExp(x) = " + linkedTabLogExp.getFunctionValue(i) + ", readLinkedTabLogExp(x) = " + readLinkedTabLogExp.getFunctionValue(i));
        }


        // можно переключать реализацию: Array или LinkedList
        //TabulatedFunction func = new ArrayTabulatedFunction(0, 4, vals);
        /*TabulatedFunction func = new LinkedListTabulatedFunction(0, 4, vals);

        System.out.println("Точки исходной функции:");
        print(func);

        System.out.println("f(2.5) = " + func.getFunctionValue(2.5));
        System.out.println();

        // попытка установить x, который нарушает порядок
        try {
            func.setPointX(1, 10);
        } catch (InappropriateFunctionPointException e) {
            System.out.println("Поймано исключение при setPointX: " + e.getMessage());
        }

        // добавление новой точки
        try {
            func.addPoint(new FunctionPoint(2.2, 5));
            System.out.println("После добавления (2.2,5):");
            print(func);
        } catch (InappropriateFunctionPointException e) {
            System.out.println("Не удалось добавить точку: " + e.getMessage());
        }


        try {
            func.deletePoint(0);
            func.deletePoint(0);
            System.out.println("После двух удалений:");
            print(func);
        } catch (IllegalStateException e) {
            System.out.println("Ошибка состояния при удалении: " + e.getMessage());
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("Неправильный индекс при удалении: " + e.getMessage());
        }
    }

    private static void print(TabulatedFunction f) {
        for (int i = 0; i < f.getPointsCount(); i++) {
            System.out.println(i + ": " + f.getPoint(i));
        }
        System.out.println();
    }*/
    }
}
