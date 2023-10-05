n <- 200 # number of datapoints
p <- 2 # dimension
sigma <- 1 # variance of the distribution
mpos <- 0 # mean value ( centre of the distribution) of positive examples
mneg <- 3 # mean valuecentre of the distribution) of negative examples
npos <- round (n / 2) # number of positive examples
nneg <- n - npos # number of negative examples
# Generate the positive and negative examples
xpos <- matrix ( rnorm ( npos * p , mean = mpos , sd = sigma ), npos , p)
xneg <- matrix ( rnorm ( nneg * p , mean = mneg , sd = sigma ), nneg , p)
x <- rbind ( xpos , xneg )
# Generate labels
y <- matrix (c( rep (1,npos), rep ( -1 , nneg )))
# Visualize the data
plot (x , col = ifelse (y > 0, 1, 2))
legend ("topleft",c('Positive','Negative'),col=seq(2),pch=1,text.col=seq(2))
## Prepare a training and a test set ##
ntrain <- round (n * 0.8) # number of training examples
tindex <- sample (n , ntrain ) # indices of training samples
XT <- x[ tindex ,] # positive training examples
XX <- x[-tindex ,] # positive test examples
YT <- y[ tindex ] # negative training examples
YY <- y[-tindex] # negative test examples
istrain = rep (0 , n)
istrain [ tindex ] = 1
# Visualize
plot (x,col = ifelse (y > 0,1,2) , pch = ifelse (istrain == 1,1,2))
legend("topleft",c('Positive Train','Positive Test','Negative Train','Negative Test'),col=c(1,1,2,2),pch=c(1,2,1,2),text.col=c(1,1,2,2))

# Load the kernlab package
library ( kernlab )
# Train the SVM
svp<-ksvm(XT,YT,type="C-svc",kernel='vanilladot',C=100,scaled=c())

# General summary
svp
# Attributes that you can access
attributes(svp)
# For example , the support vectors
alpha(svp)
alphaindex(svp)
b(svp)
# Use the built - in function to pretty - plot the classifier
plot(svp,data=XT)

# Predict labels on test
YP = predict (svp,XX)
table (YY,YP)
# Compute accuracy
sum(YP==YY)/length(YY)
# Compute at the prediction scores
YPscore=predict(svp,XX,type="decision")
# Check that the predicted labels are the signs of the scores
table(YPscore>0,YP)
# Package to compute ROC curve , precision - recall , etc.
library (ROCR)
pred<-prediction(YPscore,YY)
# Plot ROC curve
perf<-performance(pred,measure="tpr",x.measure="fpr")
plot(perf)
# Plot precision/ recall curve
perf<-performance(pred,measure="prec",x.measure="rec")
plot(perf)
# Plot accuracy as function of threshold
perf<-performance(pred,measure="acc")
plot(perf)

svp <- ksvm(x, y, type = "C-svc", kernel = 'vanilladot', C = 1, scaled = c(), cross = 5)
print(cross(svp))

# Train a nonlinear SVM
svp <- ksvm(x, y, type = "C-svc", kernel = 'rbf', kpar = list(sigma = 1), C = 1)
# Visualize it
plot(svp, data = x)
# Train a nonlinear SVM with automatic selection of sigma by heuristic
svp <- ksvm(x, y, type="C-svc", kernel='rbf', C=1)
# Visualize it
plot(svp, data = x)

install.packages("e1071")
library(e1071)
set.seed(123)  # For reproducibility
n <- 200  # Number of data points
x1 <- runif(n, -2, 2)
x2 <- runif(n, -2, 2)
y <- ifelse(x1^2 + x2^2 < 1, 1, -1)  # Create labels (non-linear separation)
data <- data.frame(x1, x2, y)
# Create an index to split the data
set.seed(456)  # For reproducibility
index <- sample(1:n, n * 0.7)  # 70% for training, 30% for testing
# Split the data
train_data <- data[index, ]
test_data <- data[-index, ]
# SVM with Linear Kernel
svm_linear <- svm(y ~ ., data = train_data, kernel = "linear", cost = 1)
# SVM with Polynomial Kernel
svm_poly <- svm(y ~ ., data = train_data, kernel = "polynomial", degree = 3, cost = 1)
# SVM with Radial Basis Function (RBF) Kernel
svm_rbf <- svm(y ~ ., data = train_data, kernel = "radial", gamma = 1, cost = 1)
# Predictions
pred_linear <- predict(svm_linear, test_data)
pred_poly <- predict(svm_poly, test_data)
pred_rbf <- predict(svm_rbf, test_data)
# Calculate accuracy
accuracy_linear <- sum(pred_linear == test_data$y) / nrow(test_data)
accuracy_poly <- sum(pred_poly == test_data$y) / nrow(test_data)
accuracy_rbf <- sum(pred_rbf == test_data$y) / nrow(test_data)
print("Accuracy (Linear Kernel):", accuracy_linear, "\n")
print("Accuracy (Polynomial Kernel):", accuracy_poly, "\n")
print("Accuracy (RBF Kernel):", accuracy_rbf, "\n")
# Plot the data points
plot(data[, 1:2], col = ifelse(data$y == 1, "blue", "red"), pch = 19)
# Add decision boundaries for each SVM model
points(svm_linear, col = "green")
points(svm_poly, col = "purple")
points(svm_rbf, col = "orange")
# Legend
legend("topright", legend = c("Class 1", "Class -1", "Linear SVM", "Poly SVM", "RBF SVM"), col = c("blue", "red", "green", "purple", "orange"), pch = c(19, 19, 1, 1, 1))

