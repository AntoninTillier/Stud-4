load("titanic.raw.RData")

titanic_r <- titanic.raw

summary(titanic_r)

view(titanic_r)

library(arules)
require(arules)

# Preparation of data
print('Preparation of data')
titanic_r[[ "Age"]] <- ordered(cut(titanic_r[[ "Age"]], c(0,18,71)), 
                                         labels = c("child", "adult"))

save(titanic_r, file = "titanic_new.raw.RData")

titanic.trans <- as(titanic_r, "transactions")

inspect(titanic.trans)

summary(titanic.trans)

itemFrequencyPlot(titanic.trans)

# Mining association rules
print('Mining association rules')
rules <- apriori(titanic.trans)

inspect(rules)

summary(rules)

rule <- apriori(titanic_r, 
                parameter=list(minlen=2, supp=0.001, conf=0.5),  
                appearance = list(default = "lhs", rhs=c("Survived=Yes","Survived=No")))
rule.sorted <- sort(rule, by='confidence')

inspect(rule.sorted)

## Compute accuracy of a model on a test set
# model: a set of classification rules
# test: a set of transactions
test.model <- function(model, test) {
  error <- 0; # number of errors
  cover <- is.superset(test, lhs(model)); 
  # compute cover matrix with one row per transaction and one column per left handside rule, the indice is TRUE if the lhs of the rule can be applied to the transaction
  for (k in 1:length(test)) {
    rule <- model[which.max(cover[k,])]; 
    # compute the first rule of the model covering the kth transaction
    if (!is.superset(test[k], rule)[1,1]) 
      # check whether this rule makes a good prediction
    error <- error + 1;
  }
  (length(test) - error) / length(test); # return the accuracy
}

## Build an associative classifier for transactions starting from classification rules
# crules: set of classification rules where the rhs is a class value
# transactions: a set of transactions
# l: the minimal number of remaining transactions covered by a rule
build.model <- function(crules, training, l) {
  crules <- sort(crules, by="confidence") # Sort rules by confidence descending
  model <- crules[0] # Initialize the model
  k <- 1 # Index for rules
  while (k <= length(crules) && length(training) >= l) { # Loop while rules and transactions remain
    rule <- crules[k] # Select the kth rule
    cover <- which(apply(training, 1, function(row) all(rule %in% row))) # Compute transactions containing the rule
    if (length(cover) >= l) { # If cover contains at least l transactions
      model <- union(model, rule) # Add the rule to the model
      training <- training[-cover, ] # Remove the covered transactions from the training set
    }
    k <- k + 1 # Increment k for the next rule
  }
  return(model) # Return the model
}