**Support Vector Machines**

**Linear SVM**

- We first generate a set of positive and negative examples from Gaussian function, and then split the dataset into a training set (80%) and a test set (20%).
- Now we train a Linear SVM on the training set by using the package kernlab with parameter C = 100.
- While the model has been built, look and understand what svp contains.
- Now we can use the trained SVM to predict the label of points in the test set, and we analyze the results using variant metrics.
- Compute the various performance of the SVM by k-fold cross-validation. Indeed, the ksvm function can automatically compute the k-fold cross-validation accuracy.

**Non-Linear SVM**

Sometimes Linear SVM are not enough for datasets where positive and negative not linearly separable. To solve this problem, we should instead use a Non-Linear SVM. This is obtained by simply changing the kernel parameter.

- A useful heuristic to choose σ is implemented in kernlab. It is based on the quantiles of the distances between the training point.

Generate a not linearly separable dataset of 200 datapoints and test SVM with different kernels (please check kernlab documents) and different values of C.

The package e1071 provides an interface to libsvm complemented by visualization and tuning func- tions, it basically provides a training function with standard and formula interfaces, and a predict() method. In addition, a plot() method visualizing data, support vectors, and decision boundaries if provided.

