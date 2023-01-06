from sklearn.datasets import fetch_mldata
from sklearn import linear_model, metrics, cross_validation
from sklearn.neural_network import BernoulliRBM

from matplotlib import pyplot as plt
import numpy as np

# load in the dataset - maybe there is a way to only load in the 0s
mnist = fetch_mldata('MNIST original')
pixels, targets = mnist.data, mnist.target



# returns the RBMS as well as the training and testing sets
'''
    Returns: 
            RBMS - list of the 10 trained RBMS
            X_train, Y_train - matrix of all training inputs and targets used
            X_test, Y_test - matrix of all testing inputs and targets not used yet
'''
def train_RBMs():
  X_train, Y_train, X_test, Y_test = np.array([]), np.array([]), np.array([]), np.array([])
  RBMS = []

  for number in range(10):
    rbm = BernoulliRBM(n_components=200, n_iter=2, random_state=0, verbose=True)
    one_number, one_target = [], []
    # get all data for specific number
    for index in range(len(pixels)):
      if targets[index] == number:
        one_number.append(pixels[index])
        one_target.append(targets[index])
    x_digit, Y = np.asarray(one_number, 'float32'), np.asarray(one_target)
    # covnert to binary image
    X = (x_digit - np.min(x_digit,0)) / (np.max(x_digit,0) + .0001)
    X = X > 0.5

    # training and testing data randomly split, specific to one type of digit (0-9)
    X1_train, X1_test, Y1_train, Y1_test = cross_validation.train_test_split(X, Y, test_size=.35, random_state=0)

    # 1st time through, make the list and the initial shape of the matrix
    if number == 0:
      X_train, Y_train, X_test, Y_test = X1_train, Y1_train, X1_test, Y1_test
   # just append to the end of the matrices after
    else:
      X_train, Y_train = np.append(X_train, X1_train, axis=0), np.append(Y_train, Y1_train, axis=0)
      X_test, Y_test = np.append(X_test, X1_test, axis=0), np.append(Y_test, Y1_test, axis=0)

    trained_rbm = rbm.fit(X1_train)
    print 'Trained RBM for digit: ',number
    RBMS.append(trained_rbm)


  return RBMS, X_train, Y_train, X_test, Y_test

'''
    Parameters: the list of trained RBMs, matrix X and its target value column-matrix Y
    Returns: the feature space and target values for the logistic regression classifier
'''
def build_log_features(RBMS, Xs, Ys):
  logistic_trainX, logistic_trainY = [], []
  for num,rbm in enumerate(RBMS):
    # the rbm that is trained for a specific  digit is matched up with the variale 'num'

    # fill up the lists the first time arround
    if num == 0:
      for i,picture in enumerate(Xs):
        hidden_layer = rbm.transform(picture)
        logistic_trainX.append(hidden_layer)
        logistic_trainY.append(Ys[i])
    # just append onto the elements of the list
    # each appendage represents a different RBM's hidden layer probabilites (200)
    else:
      for i,picture in enumerate(Xs):
        hidden_layer = rbm.transform(picture)
        newInput = logistic_trainX[i]
        newInput = np.append(newInput, hidden_layer, 0)
        logistic_trainX[i] = newInput

  return logistic_trainX, logistic_trainY


class_names = ['0','1','2','3','4','5','6','7','8','9']


RBMs, training_X, training_Y, testing_X, testing_Y = train_RBMs()
log1 = linear_model.LogisticRegression(C=10)
log1.fit(training_X, training_Y)
predicted = log1.predict(testing_X)
print metrics.classification_report(testing_Y, predicted, target_names=class_names)


X_train, Y_train = build_log_features(RBMs, training_X, training_Y)
X_test, Y_test = build_log_features(RBMs, testing_X, testing_Y)
print 'Size of feature space for logistic regression: ',np.shape(X_train)

# SHOULD BE POSSIBLY REGULARIZE? # 
logistic = linear_model.LogisticRegression(C=10)
logistic.fit(X_train,Y_train)
predicted = logistic.predict(X_test)

#class_names = ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9']
print metrics.classification_report(Y_test, predicted, target_names=class_names)
'''
confidence_scores = logistic.predict_proba(X_test)
zero, one = [], []
for i in range(len(X_test)):
  if Y_test[i] == 0:
    zero.append(confidence_scores[i])
  else:
    one.append(confidence_scores[i])

fig, ax = plt.subplots()
f = plt.plot()
ax.plot(np.zeros((len(zero))), zero, 'ro', label='Zero')
ax.plot(np.ones((len(one))), one, 'D', label='One')
f.ylim(-1,2,1)
f.xlim(-1,2,1)
plt.show()

'''
