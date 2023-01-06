import numpy as np
import matplotlib.pyplot as plt
import sys

from sklearn.linear_model import LogisticRegression
from sklearn import metrics
from objectreader import Reader
from sklearn.cross_validation import cross_val_score
from sklearn.cross_validation import train_test_split

filename = sys.argv[1]
read = Reader(filename)

outs, ins = read.split()
outs = np.ravel(outs)

# Split data into training and test sets
xs, xt, ts, tt = train_test_split(ins, outs, \
  test_size=.25, random_state=0)
model = LogisticRegression()
model.fit(xs, ts)

# accuracy on training set
print "training accuracy: ", model.score(xs, ts), "\n"

# Predicted is the predicted class labels
predicted = model.predict(xt)
# What is our accuracy?
print "test accuracy: ", metrics.accuracy_score(tt, predicted), "\n"

# report
print "classification report:\n",\
  metrics.classification_report(tt, predicted)

# Do same thing as above but as many times as I want (10 here)
scores = cross_val_score(LogisticRegression(), ins, outs,\
   scoring='accuracy', cv=10)
print scores, "\n"
print scores.mean() 
