package functions.basic;

import functions.Function;

public abstract class TrigonometricFunction implements Function {
    public abstract double getFunctionValue(double x);

    public double getLeftDomainBorder() {
        return Double.NEGATIVE_INFINITY;
    }

    public double getRightDomainBorder() {
        return Double.POSITIVE_INFINITY;
    }
}

