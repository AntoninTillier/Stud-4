package at;

import java.util.*;
import java.util.stream.Collectors;

import at.Iris.IrisClass;

public class Kppv {
    private List<IrisClass> classPredict = new ArrayList<>();
    private List<IrisClass> classReal = new ArrayList<>();

    public static List<Object> train_test_split(List<Iris> dataset, double test_size, int nbClass) {
        int sizeDataset = dataset.size();
        int nbValuePerClass = sizeDataset / nbClass;
        int sizeTrain = (int) (nbValuePerClass * test_size);
        List<Object> res = new ArrayList<>();
        List<Iris> X_train = new ArrayList<>();
        List<Iris> X_test = new ArrayList<>();
        int counterTrain = 0;
        int counterTest = 0;
        for (Iris iris : dataset) {
            if (counterTrain < sizeTrain) {
                X_train.add(iris);
            }
            counterTrain++;
            if (counterTrain > sizeTrain) {
                X_test.add(iris);
                counterTest++;
            }
            if (counterTest == sizeTrain) {
                counterTrain = 0;
                counterTest = 0;
            }
        }
        res.add(X_train);
        res.add(X_test);
        return res;
    }

    public void OnepvvClassifier(List<Iris> X_train, List<Iris> X_test) {
        classReal.clear();
        classPredict.clear();
        for (Iris iris : X_test) {
            classReal.add(iris.getIrisClass());
            classPredict.add(getIrisType(1, X_train, iris));
        }
    }

    public void KpvvClassifier(int k, List<Iris> X_train, List<Iris> X_test) {
        classReal.clear();
        classPredict.clear();
        for (Iris iris : X_test) {
            classReal.add(iris.getIrisClass());
            classPredict.add(getIrisType(k, X_train, iris));
        }
    }

    private double getDistance(double[] iris1, double[] iris2) {
        int dimension = iris1.length;
        double distance = 0;
        for (int i = 0; i < dimension; i++) {
            distance += Math.pow(Math.abs(iris1[i] - iris2[i]), 2);
        }
        return Math.sqrt(distance);
    }

    private IrisClass getIrisType(int k, List<Iris> X_train, Iris newIris) {
        Map<Double, Iris> neighbourDistance = new TreeMap<>();
        for (Iris iris : X_train) {
            neighbourDistance.put(getDistance(newIris.getAllSize(), iris.getAllSize()), iris);
        }
        int counter = 0;
        Map<IrisClass, Integer> irisClasses = new TreeMap<>();
        for (Map.Entry<Double, Iris> neighbour : neighbourDistance.entrySet()) {
            if (counter == k)
                break;
            if (k == 1) {
                irisClasses.put(neighbour.getValue().getIrisClass(),
                        (irisClasses.get(neighbour.getValue().getIrisClass()) == null ? 0
                                : irisClasses.get(neighbour.getValue().getIrisClass())) + 1);
            } else {
                irisClasses.put(neighbour.getValue().getIrisClass(),
                        (irisClasses.get(neighbour.getValue().getIrisClass()) == null ? 0
                                : irisClasses.get(neighbour.getValue().getIrisClass())) + 1);
            }
            counter++;
        }
        IrisClass irisClass = null;
        if (k > 1) {
            if (irisClasses.size() > 1) {
                int setosa = 0, versicolor = 0, virginica = 0, res = 0;
                for (Map.Entry<IrisClass, Integer> classes : irisClasses.entrySet()) {
                    if (classes.getKey() == IrisClass.setosa) setosa = classes.getValue();
                    if (classes.getKey() == IrisClass.versicolor) versicolor = classes.getValue();
                    if (classes.getKey() == IrisClass.virginica) virginica = classes.getValue();
                }
                int max = Math.max(Math.max(setosa, versicolor), virginica);
                if (max == setosa) res = 0;
                if (max == versicolor) res = 1;
                if (max == virginica) res = 2;
                if (max == setosa && max == versicolor && max == virginica) res = (int) (Math.random() * 3);
                if (max == setosa && max == versicolor) res = (int) (Math.random() * 2);
                if (max == versicolor && max == virginica) res = (int) (Math.random() * 2) + 1;
                if (max == setosa && max == virginica)
                    if ((int) (Math.random() * 2) == 0) res = 0;
                    else res = 2;
                if (res == 0) irisClass = IrisClass.setosa;
                if (res == 1) irisClass = IrisClass.versicolor;
                if (res == 2) irisClass = IrisClass.virginica;
            } else {
                irisClass = irisClasses.entrySet().iterator().next().getKey();
            }
        } else {
            irisClass = irisClasses.entrySet().iterator().next().getKey();
        }
        return irisClass;
    }

    public void crossValidation() { }

    public void metric() {
        IrisClass[] tabClass = IrisClass.values();
        ArrayList<IrisClass> getAllClass = new ArrayList<IrisClass>(Arrays.asList(tabClass));
        int sizeMatrix = tabClass.length;
        int[][] ConfMatrix = new int[sizeMatrix][sizeMatrix];
        for (int i = 0; i < classReal.size(); i++) {
            int classe = getAllClass.indexOf(classReal.get(i));
            int classeTest = getAllClass.indexOf(classPredict.get(i));
            ConfMatrix[classe][classeTest] += 1;
        }
        confusionMatrix(ConfMatrix);
        accuracy(ConfMatrix);
        double r = recall(ConfMatrix);
        double p = precision(ConfMatrix);
        f1Score(r, p);
    }

    private void confusionMatrix(int[][] confMatrix) {
        System.out.println("Confusion Matrix :");
        System.out.println("\t\t\\ Iris-setosa \t| Iris-versicolor \t| Iris-virginica |");
        for (int i = 0; i < confMatrix.length; i++) {
            if (i == 0) {
                System.out.print("Iris-setosa     |\t");
            } else if (i == 1) {
                System.out.print("Iris-versicolor |\t");
            } else {
                System.out.print("Iris-virginica  |\t");
            }
            for (int j = 0; j < confMatrix[i].length; j++) {
                if (j == 0) {
                    System.out.print(confMatrix[i][j] + "\t|");
                } else if (j == 1) {
                    System.out.print("\t" + confMatrix[i][j] + "\t\t|");
                } else {
                    System.out.print("\t" + confMatrix[i][j] + "\t |");
                }
            }
            System.out.println();
        }
    }

    private void accuracy(int[][] confusionMatrix) {
        System.out.println();
        double goodClass = 0;
        double badClass = 0;
        for (int i = 0; i < confusionMatrix.length; i++) {
            for (int j = 0; j < confusionMatrix[i].length; j++) {
                if (i == j) {
                    goodClass += confusionMatrix[i][j];
                } else {
                    badClass += confusionMatrix[i][j];
                }
            }
        }
        System.out.println("Accuracy : " + goodClass * 100 / (goodClass + badClass) + "%");
    }

    private double recall(int[][] confMatrix) {
        double setosa = 0, versicolor = 0, virginica = 0;
        double sumSetosaC = 0, sumVersicolorC = 0, sumVirginicaC = 0, sumSetosaL = 0, sumVersicolorL = 0,
                sumVirginicaL = 0;
        for (int i = 0; i < confMatrix.length; i++) {
            for (int j = 0; j < confMatrix[i].length; j++) {
                if (i == j) {
                    if (i == 0)
                        setosa = confMatrix[i][j];
                    if (i == 1)
                        versicolor = confMatrix[i][j];
                    if (i == 2)
                        virginica = confMatrix[i][j];
                }
                if (i == 0)
                    sumSetosaL += confMatrix[i][j];
                if (i == 1)
                    sumVersicolorL += confMatrix[i][j];
                if (i == 2)
                    sumVirginicaL += confMatrix[i][j];
            }
        }
        double r0 = setosa / sumSetosaL, r1 = versicolor / sumVersicolorL, r2 = virginica / sumVirginicaL,
                total = sumSetosaL + sumVersicolorL + sumVirginicaL;
        double avgRecall = ((r0 * sumSetosaL + r1 * sumVersicolorL + r2 * sumVirginicaL) / total);
        System.out.println("Recall : " + (avgRecall * 100) + "%");
        return avgRecall;
    }

    private double precision(int[][] confMatrix) {
        double setosa = 0, versicolor = 0, virginica = 0;
        double sumSetosaC = 0, sumVersicolorC = 0, sumVirginicaC = 0, sumSetosaL = 0, sumVersicolorL = 0,
                sumVirginicaL = 0;
        for (int i = 0; i < confMatrix.length; i++) {
            for (int j = 0; j < confMatrix[i].length; j++) {
                if (i == j) {
                    if (i == 0)
                        setosa = confMatrix[i][j];
                    if (i == 1)
                        versicolor = confMatrix[i][j];
                    if (i == 2)
                        virginica = confMatrix[i][j];
                }
                if (i == 0)
                    sumSetosaL += confMatrix[i][j];
                if (i == 1)
                    sumVersicolorL += confMatrix[i][j];
                if (i == 2)
                    sumVirginicaL += confMatrix[i][j];
                if (j == 0)
                    sumSetosaC += confMatrix[i][j];
                if (j == 1)
                    sumVersicolorC += confMatrix[i][j];
                if (j == 2)
                    sumVirginicaC += confMatrix[i][j];
            }
        }
        double p0 = setosa / sumSetosaC, p1 = versicolor / sumVersicolorC, p2 = virginica / sumVirginicaC,
                total = sumSetosaL + sumVersicolorL + sumVirginicaL;
        double avgPrecision = ((p0 * sumSetosaL + p1 * sumVersicolorL + p2 * sumVirginicaL) / total);
        System.out.println("Precision : " + (avgPrecision * 100) + "%");
        return avgPrecision;
    }

    private void f1Score(double r, double p) {
        double score = 2 * (r * p) / (r + p);
        System.out.println("F1 Score : " + (score * 100) + "%");
    }
}