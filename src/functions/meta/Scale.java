package functions.meta;

import functions.Function;

public class Scale implements Function{
    private Function f;
    private double scaleY;
    private double scaleX;

    public Scale(Function f, double scaleY, double scaleX) {
        this.f = f;
        this.scaleY = scaleY;
        this.scaleX = scaleX;
    }

    public double getLeftDomainBorder() {
        double right = f.getRightDomainBorder() * scaleX;
        double left = f.getLeftDomainBorder() * scaleX;
        return Math.min(left, right);
    }

    public double getRightDomainBorder() {
        double right = f.getRightDomainBorder() * scaleX;
        double left = f.getLeftDomainBorder() * scaleX;
        return Math.max(left, right);
    }

    public double getFunctionValue(double x) {
        return scaleY * f.getFunctionValue(x / scaleX);
    }
}
