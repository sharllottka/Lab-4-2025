package functions.meta;

import functions.Function;

public class Power implements Function{
    private Function f;
    private double n;

    public Power(Function f, double n) {
        this.f = f;
        this.n = n;
    }

    public double getLeftDomainBorder() {
        return f.getLeftDomainBorder();
    }

    public double getRightDomainBorder() {
        return f.getRightDomainBorder();
    }

    public double getFunctionValue(double x) {
        return Math.pow(f.getFunctionValue(x), n);
    }
}
