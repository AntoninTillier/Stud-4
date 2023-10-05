**Mining Association Rule Models in R**

**Context**

The sinking of the RMS Titanic is one of the most infamous shipwrecks in history. On April 15, 1912, during her maiden voyage, the Titanic sank after colliding with an iceberg, killing 1502 out of 2224 passengers and crew. This sensational tragedy shocked the international community and led to better safety regulations for ships.
One of the reasons that the shipwreck led to such loss of life was that there were not enough lifeboats for the passengers and crew. Although there was some element of luck involved in surviving the sinking, some groups of people were more likely to survive than others, such as women, children, and the upper-class.
In this laboratory, we ask you to complete the analysis of what sorts of people were likely to survive. In particular, we ask you to apply the tools of association rule mining to predict which passengers survived the tragedy.
*Objectives*
- Prepare data for data analysis
- Mine association rules using arules package
- Build associative classifiers

**Exploration of data**

Throughout this laboratory, you will use a dataset file “titanic.raw.RData”.

- Load titanic.raw.RData file in order to obtain a new data frame named titanic.raw.
- Use functions summary() and View() and answer the following questions about titanic.raw:
    - What is the number of attribute?
    - What is the number of records?
    - What is the proportion of passengers who survived? What is the accuracy of the majority class prediction?
    - What is the age of the youngest/oldest passenger?

**Preparation of data**

This part focuses on the construction of a set of transactions. Note that each transaction is a set of items and each item is a pair Attribute=Value. The package arules provides a basic
infrastructure for creating and manipulating input datasets starting from a data frame. Nevertheless, we need to map the metric attributes (Age) to ordinal attributes by building suitable categories. We want to divide this attribute into suitable categories using knowledge about typical age groups (child when age is lower than or equal to 18 and adult otherwise). To do this, the below example shows how to divide the attribute hours-per-week (for the data frame AdultUCI) into 4 categories where the first one is between 0 and 25 hours and is named Part-time; the second one is between 25 and 40 hours and is named Full-time; and so on.

- Prepare the attribute Age using the functions ordered() and cut().
- Save the data frame titanic.raw in titanic_new.raw.RData
- Convert the data frame into a set of transactions using function as(). If necessary, load the packages arules.
- Analyze the set of transactions titanic.trans using and answer the following questions:
    - What is the number of items?
    - What is the number of transactions?
    - What are the 3 most frequent items?

**Mining association rules**

The R package arules includes interfaces to two fast mining algorithms, the popular C
implementations of Apriori and Eclat by Christian Borgelt. These algorithms can be used to mine frequent itemsets, maximal frequent itemsets, closed frequent itemsets and association rules.

- Launch Apriori algorithm with default parameters for mining a set of association rules
- Answer the following questions:
    - How many association rules have been extracted?
    - What is the minimal confidence threshold?
    - What is the minimal support threshold?
    - Why are there only 9 items (and not 10) after the sorting and recoding phase?
    - What is the size of the longest association rules (i.e., the number of Apriori levels)?

Classification rule is association rule whose right-hand-side only concerns one target attribute (e.g., the attribute Survived). The idea is that when you have a new record that satisfies the left-hand-side of the rule, then we can apply the rule. If the right-hand-side of the rule contains Survived=No, we conclude that the passenger did not survive.

- Launch Apriori algorithm for mining all classification rules about the attribute Survived with 0.001 as minimal support threshold, 0.5 as minimal confidence threshold, at least 2 items such that the right hand side of each rule contains either Survived=No or Survived=Yes.

**Classification**
Associative classification rule mining aims to discover a small set of classification rules in the database that forms an accurate classifier.

- Do function computes the accuracy of the set of classification rules M (used as a classifier) on the set of transactions T
- Implement an associative classifier for transactions starting from classification rules





