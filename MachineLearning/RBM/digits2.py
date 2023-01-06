from sklearn.datasets import fetch_mldata
from sklearn.pipeline import Pipeline
from sklearn import linear_model, metrics, cross_validation
from sklearn.neural_network import BernoulliRBM
from sklearn.cross_validation import ShuffleSplit
from matplotlib import pyplot as plt
import numpy as np

# load in the dataset - maybe there is a way to only load in the 0s
mnist = fetch_mldata('MNIST original')
pixels, targets = mnist.data, mnist.target

# create and train a RBM for each digit
RBMS = []

training_xs = []
training_ys = []

testing_xs = []
testing_ys = []

for number in range(10):
  rbm = BernoulliRBM(n_components=200, n_iter=2, random_state=None, verbose=True)
  one_number, one_target = [], []
  # get all data for specific number
  for index in range(len(pixels)):
    if targets[index] == number:
      one_number.append(pixels[index])
      one_target.append(targets[index])
  x_digit, Y = np.asarray(one_number, 'float32'), np.asarray(one_target)
  # covnert to binary image
  X = (x_digit - np.min(x_digit,0)) / (np.max(x_digit,0) + .0001)

  X_train, X_test, Y_train, Y_test = cross_validation.train_test_split(X, Y, test_size=.35, random_state=0)
  training_xs.append(X_train)
  training_ys.append(Y_train)
  testing_xs.append(X_test)
  testing_ys.append(Y_test) 
 
  trained_rbm = rbm.fit(X_train)
  RBMS.append(trained_rbm)

  # just for speed for figuring this out... will break after the rbm for 0 and 1 has been trained
  if number == 1:
    break
 
# train the Logistic Reg. classifier
logistic = linear_model.LogisticRegression(C=10)

# get the components of the rbms
# make those match up to the target values
X_train = []
for i in range(len(RBMS)):
  temp = RBMS[i].components_
  temp = np.array(temp)
  # make the components of each rbm raveled out so they are one input point
  temp = temp.ravel()
  X_train.append(temp)

train = np.ones((2,len(X_train[0]+X_train[1])))
# hard code the test labels in
train[0] = X_train[0]
train[1] = X_train[1]
digits = np.array([[0],[1]])
digits.shape = 2
# fit on the raveled rbm components and their respective labels
logistic.fit(X_train,digits)

logistic.score(testing_xs, testing_ys)
