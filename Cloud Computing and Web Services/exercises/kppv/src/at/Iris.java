package at;

public class Iris {
    public enum IrisClass {setosa, versicolor, virginica}

    private double sepalLength;
    private double sepalWidth;
    private double petalLength;
    private double petalWidth;
    private IrisClass irisClass;

    public Iris(double sepalLength, double sepalWidth, double petalLength, double petalWidth, IrisClass irisClass) {
        this.sepalLength = sepalLength;
        this.sepalWidth = sepalWidth;
        this.petalLength = petalLength;
        this.petalWidth = petalWidth;
        this.irisClass = irisClass;
    }

    public Iris(double sepalLength, double sepalWidth, double petalLength, double petalWidth) {
        this.sepalLength = sepalLength;
        this.sepalWidth = sepalWidth;
        this.petalLength = petalLength;
        this.petalWidth = petalWidth;
    }

    public double[] getAllSize() {
        return new double[]{this.sepalLength, this.sepalWidth, this.petalWidth, this.petalWidth};
    }

    public IrisClass getIrisClass() {
        return irisClass;
    }

    public void setIrisClass(IrisClass irisClass) {
        this.irisClass = irisClass;
    }

    @Override
    public String toString() {
        return this.sepalLength + "," +
                this.sepalWidth + "," +
                this.petalLength + "," +
                this.petalWidth + "," +
                this.irisClass;
    }
}