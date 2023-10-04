package base;

import java.io.*;
import java.util.*;

public class kPPV {
    // General variables for dealing with Iris data (UCI repository)
    // NbEx: number of data per class in dataset
    // NbClasses: Number of classes to recognize
    // NbFeatures: Dimensionality of dataset
    // NbExLearning: Number of exemples per class used for learning (there are the
    // first ones in data storage for each class)

    static int NbEx = 50, NbClasses = 3, NbFeatures = 4, NbExLearning = 25;
    static Double data[][][] = new Double[NbClasses][NbEx][NbFeatures];// there are 50*3 exemples at all. All have 4
    // features

    public static void main(String[] args) {
        System.out.println("Starting kPPV");
        ReadFile();
        System.out.println("");
        // X is an exemple to classify (to take into data -test examples-)
        Double X[] = new Double[NbFeatures]; // { 6.7, 3.0, 5.2, 2.3 };
        System.out.print("Set X : ");
        X = setX(X);
        System.out.println("");
        // distances: table to store all distances between the given exemple X and all
        // exemples in learning set, using ComputeDistances
        Double distances[] = new Double[NbClasses * NbExLearning];
        System.out.println("ComputeDistances");
        distances = ComputeDistances(X, distances);
        affiche(distances);
        System.out.println("Classe de X : " + getClass(distances));
        System.out.println("");
        System.out.println("classify1PPV : \n");
        classify1PPV();
        System.out.println("");
        System.out.println("classifyKPPV : \n");
        classifyKPPV(20);
    }

    private static Double[] setX(Double x[]) {
        int NcClassesRand = 0 + (int) (Math.random() * ((NbClasses)));
        int NbExRand = NbExLearning + (int) (Math.random() * ((NbEx - NbExLearning)));
        for (int i = 0; i < x.length; i++) {
            x[i] = data[NcClassesRand][NbExRand][i];
        }
        affiche(x);
        return x;
    }

    // calcule les distance entre x et le training set de data
    private static Double[] ComputeDistances(Double x[], Double distances[]) {
        double distance = 0.0;
        int index = 0;
        // parcours les faces du cube
        for (Double[][] cl : data) {
            int c = 0;
            // parcours les colonnes de la face du cube
            for (Double[] ex : cl) {
                if (c < NbExLearning) {
                    for (int i = 0; i < ex.length; i++) {
                        // somme des distance entre les points de la colonne et ceux du tableau X
                        distance += Math.pow(x[i] - ex[i], 2);
                    }
                    // racine carré de la somme calculée precedemment
                    distances[index] = Math.sqrt(distance);
                    // reinitialisation de la variable distance
                    distance = 0.0;
                    index++;
                    c++;
                }
            }
        }
        return distances;
    }

    private static void affiche(Double x[]) {
        System.out.println("");
        System.out.print("{ ");
        for (int i = 0; i < x.length; i++) {
            if (i == x.length - 1) {
                System.out.print(x[i]);
            } else {
                System.out.print(x[i] + ",");
            }
        }
        System.out.println(" }");
    }

    // renvoie la classe de la valeur min du tableau distances par rapport a son
    // index
    // entre 0 et 24 -> 1
    // entre 25 et 50 -> 2
    // entre 50 et 75 -> 3
    private static int getClass(Double[] distances) {
        ArrayList<Double> arDist = new ArrayList<Double>(Arrays.asList(distances));
        int ind = arDist.indexOf(Collections.min(arDist));
        if (ind >= 0 && ind < 25)
            return 1;
        else if (ind >= 25 && ind < 50)
            return 2;
        else if (ind >= 50 && ind < 75)
            return 3;
        return 0;
    }

    //
    public static void classify1PPV() {
        // Initialisation de la matrice de confusion
        int[][] ConfMatrix = new int[NbClasses][NbClasses];
        // Initialisation tableau distances
        Double distances[] = new Double[NbClasses * NbExLearning];
        // Initialisation de l'entier classe qui indique dans quelle classe est le test
        int classe = 0;
        // parcours les faces cube
        for (Double[][] cl : data) {
            // incremente classe à chaque changement de classe
            classe++;
            // //Initialise compteur pour choisir le nombre de valeur du Test set
            int c = 0;
            // parcours les colonnes de la face du cube
            for (Double[] ex : cl) {
                // test set entre 25 et 49
                if (c >= NbExLearning && c < NbEx) {
                    // affiche(ex);
                    // Remplie le tableau des distances entre le test i et le learning set
                    distances = ComputeDistances(ex, distances);
                    // indique la classe qui correspond a l'index de la valeur ayant la distance
                    // la plus courte
                    int classeTest = getClass(distances);
                    // System.out.println("classeTest : " + classeTest + " --- classe : " + classe);
                    // Remplie la matrice de confusion
                    ConfMatrix[classe - 1][classeTest - 1] += 1;
                }
                // incremente le comteur
                c++;
            }
        }
        afficheMetrics(ConfMatrix);
    }


    public static int getClassIndex(int ind) {
        if (ind >= 0 && ind < 25)
            return 0;
        else if (ind >= 25 && ind < 50)
            return 1;
        else if (ind >= 50 && ind < 75)
            return 2;
        return -1;
    }

    public static int getClassK(int k, Double[] distances) {
        //Transformation du tableau en liste
        ArrayList<Double> arDist = new ArrayList<Double>(Arrays.asList(distances));
        // Creation d’une liste triee
        ArrayList<Double> arDistSort = new ArrayList<Double>(Arrays.asList(distances));
        Collections.sort(arDistSort);
        // initialisation des variables pour compter le nombre d’occurence //des classe parmis les k voisins
        int c1 = 0, c2 = 0, c3 = 0;
        // Pour les k plus proches voisin
        for (int i = 0; i < k; i++) {
            // Attribution de la classe correspondant au voisin
            int cl = getClassIndex(arDist.indexOf(arDistSort.get(i)));
            // Incrementation des varialbes selon la classe du voisin
            if (cl == 0) c1++;
            if (cl == 1) c2++;
            if (cl == 2) c3++;
        }
        // recherche de la classe majoritaire parmi les voisins
        int max = Math.max(Math.max(c1, c2), c3);
        // si il y a des egalite on choisie une classe al atoirement
        if (max == c1 && max == c2 && max == c3) return (int) (Math.random() * 3);
        if (max == c1 && max == c2) return (int) (Math.random() * 2);
        if (max == c2 && max == c3) return (int) (Math.random() * 2) + 1;
        if (max == c1 && max == c3)
            if ((int) (Math.random() * 2) == 0) return 0;
            else return 2;
        // sinon on prend la classe la plus presente parmi les voisins
        if (max == c1) return 0;
        if (max == c2) return 1;
        if (max == c3) return 2;
        else return -1;
    }

    private static void classifyKPPV(int k) {
        // Initialisation de la matrice de confusion
        int[][] ConfMatrix = new int[NbClasses][NbClasses];
        // Initialisation tableau distances
        Double distances[] = new Double[NbClasses * NbExLearning];
        int classe = 0;
        for (Double[][] cl : data) {
            classe++;
            int c = 0;
            for (Double[] ex : cl) {
                if (c >= NbExLearning && c < NbEx) {
                    distances = ComputeDistances(ex, distances);
                    int classeTest = getClassK(k, distances);
                    ConfMatrix[classe - 1][classeTest] += 1;
                }
                c++;
            }
        }
        afficheMetrics(ConfMatrix);
    }

    private static void afficheMetrics(int[][] confMatrix) {
        System.out.println("Informations :");
        confusionMatrix(confMatrix);
        System.out.println();
        accuracy(confMatrix);
        double r = recall(confMatrix);
        double p = precision(confMatrix);
        f1Score(r, p);
    }

    private static void confusionMatrix(int[][] confMatrix) {
        // Iris-setosa = 11 + 1
        // Iris-versicolor =15 + 1
        // Iris-virginica = 14 + 1
        System.out.println("\t\t| Iris-setosa \t| Iris-versicolor \t| Iris-virginica |");
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

    private static void accuracy(int[][] confusionMatrix) {
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

    private static double recall(int[][] confMatrix) {
        double setosa = 0, versicolor = 0, virginica = 0;
        double sumSetosaC = 0, sumVersicolorC = 0, sumVirginicaC = 0, sumSetosaL = 0, sumVersicolorL = 0, sumVirginicaL = 0;
        for (int i = 0; i < confMatrix.length; i++) {
            for (int j = 0; j < confMatrix[i].length; j++) {
                if (i == j) {
                    if (i == 0) setosa = confMatrix[i][j];
                    if (i == 1) versicolor = confMatrix[i][j];
                    if (i == 2) virginica = confMatrix[i][j];
                }
                if (i == 0) sumSetosaL += confMatrix[i][j];
                if (i == 1) sumVersicolorL += confMatrix[i][j];
                if (i == 2) sumVirginicaL += confMatrix[i][j];
            }
        }
        double r0 = setosa / sumSetosaL, r1 = versicolor / sumVersicolorL, r2 = virginica / sumVirginicaL, total = sumSetosaL + sumVersicolorL + sumVirginicaL;
        double avgRecall = ((r0 * sumSetosaL + r1 * sumVersicolorL + r2 * sumVirginicaL) / total);
        System.out.println("Recall : " + (avgRecall * 100) + "%");
        return avgRecall;
    }

    private static double precision(int[][] confMatrix) {
        double setosa = 0, versicolor = 0, virginica = 0;
        double sumSetosaC = 0, sumVersicolorC = 0, sumVirginicaC = 0, sumSetosaL = 0, sumVersicolorL = 0, sumVirginicaL = 0;
        for (int i = 0; i < confMatrix.length; i++) {
            for (int j = 0; j < confMatrix[i].length; j++) {
                if (i == j) {
                    if (i == 0) setosa = confMatrix[i][j];
                    if (i == 1) versicolor = confMatrix[i][j];
                    if (i == 2) virginica = confMatrix[i][j];
                }
                if (i == 0) sumSetosaL += confMatrix[i][j];
                if (i == 1) sumVersicolorL += confMatrix[i][j];
                if (i == 2) sumVirginicaL += confMatrix[i][j];
                if (j == 0) sumSetosaC += confMatrix[i][j];
                if (j == 1) sumVersicolorC += confMatrix[i][j];
                if (j == 2) sumVirginicaC += confMatrix[i][j];
            }
        }
        double p0 = setosa / sumSetosaC, p1 = versicolor / sumVersicolorC, p2 = virginica / sumVirginicaC, total = sumSetosaL + sumVersicolorL + sumVirginicaL;
        double avgPrecision = ((p0 * sumSetosaL + p1 * sumVersicolorL + p2 * sumVirginicaL) / total);
        System.out.println("Precision : " + (avgPrecision * 100) + "%");
        return avgPrecision;
    }

    private static void f1Score(double r, double p) {
        double score = 2 * (r * p) / (r + p);
        System.out.println("F1 Score : " + (score * 100) + "%");
    }

    // ——-Reading data from iris.data file
    // 1 line -> 1 exemple
    // 50 first lines are 50 exemples of class 0, next 50 of class 1 and 50 of class
    // 2
    private static void ReadFile() {
        String line, subPart;
        int classe = 0, n = 0;
        try {
            BufferedReader fic = new BufferedReader(new FileReader("iris.data"));
            while ((line = fic.readLine()) != null) {
                for (int i = 0; i < NbFeatures; i++) {
                    subPart = line.substring(i * NbFeatures, i * NbFeatures + 3);
                    data[classe][n][i] = Double.parseDouble(subPart);
                    // System.out.println(data[classe][n][i] + " " + classe + " " + n);
                }
                if (++n == NbEx) {
                    n = 0;
                    classe++;
                }
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}