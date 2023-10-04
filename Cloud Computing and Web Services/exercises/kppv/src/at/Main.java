package at;

import java.util.ArrayList;
import java.util.List;

public class Main {

	public static void main(String[] args) {
		IrisFile irisFile = new IrisFile("iris.data");
		List<Iris> irisDataset = irisFile.getIrisDataList();
		System.out.println("Dataset : \n" + irisDataset.toString()+"\n");
		Kppv kppv = new Kppv();
		List<Object> trainTest = kppv.train_test_split(irisDataset, 0.5, 3);
		List<Iris> X_train = new ArrayList<>();
		List<Iris> X_test = new ArrayList<>();
		X_train = (List<Iris>) trainTest.get(0);
		X_test = (List<Iris>) trainTest.get(1);
		System.out.println("OnepvvClassifier :");
		kppv.OnepvvClassifier(X_train, X_test);
		kppv.metric();
		System.out.println("KpvvClassifier :");
		kppv.KpvvClassifier(20, X_train, X_test);
		kppv.metric();
	}
}