package functions.meta;

import functions.Function;

public class Shift implements Function {
    private Function f;
    private double shiftY;
    private double shiftX;

    public Shift(Function f, double shiftY, double shiftX) {
        this.f = f;
        this.shiftY = shiftY;
        this.shiftX = shiftX;
    }

    public double getLeftDomainBorder() {
        double right = f.getRightDomainBorder() + shiftX;
        double left = f.getLeftDomainBorder() + shiftX;
        return Math.min(left, right);
    }

    public double getRightDomainBorder() {
        double right = f.getRightDomainBorder() + shiftX;
        double left = f.getLeftDomainBorder() + shiftX;
        return Math.max(left, right);
    }

    public double getFunctionValue(double x) {
        return f.getFunctionValue(x - shiftX) + shiftY;
    }
}
