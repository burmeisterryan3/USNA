"""
Compares the difference between logistic regression
and logistic regression applied with rbms
"""
import numpy as np
from scipy.ndimage import convolve
from sklearn import linear_model, metrics
from sklearn.cross_validation import train_test_split
from sklearn.neural_network import BernoulliRBM
from sklearn.datasets import fetch_mldata
from sklearn.pipeline import Pipeline
from matplotlib import pyplot as plt

def nudge_dataset(X, Y):
    """
    This produces a dataset 5 times bigger than the original one,
    by moving the 8x8 images in X around by 1px to left, right, down, up
    """
    direction_vectors = [
        [[0, 1, 0],
         [0, 0, 0],
         [0, 0, 0]],

        [[0, 0, 0],
         [1, 0, 0],
         [0, 0, 0]],

        [[0, 0, 0],
         [0, 0, 1],
         [0, 0, 0]],

        [[0, 0, 0],
         [0, 0, 0],
         [0, 1, 0]]]

    shift = lambda x, w: convolve(x.reshape((28, 28)), mode='constant',
                                  weights=w).ravel()
    X = np.concatenate([X] +
                       [np.apply_along_axis(shift, 1, X, vector)
                        for vector in direction_vectors])
    Y = np.concatenate([Y for _ in range(5)], axis=0)
    return X, Y
############################################################
# Get data and separate zeros

mnist = fetch_mldata('MNIST original')

#train = mnist.data
#targets = mnist.target
train = np.asarray(mnist.data, 'float32')
train, targets = nudge_dataset(mnist.data, mnist.target)

train = (train - np.min(train, 0)) / (np.max(train, 0) + 0.0001)

# random_state = pseud-random number generator state used
#                for random sampling
xtrain, xtest, ytrain, ytest= train_test_split(train, targets,
                                 test_size=0.2, random_state = 0)

logistic = linear_model.LogisticRegression()
# Models verbose = verbosity level.  default = 0, silent mode
rbm = BernoulliRBM(random_state=0, verbose = True)
classifier = Pipeline(steps=[('rbm', rbm), ('logistic', logistic)])

############################################################
# Train

# Online example:
# Hyper-parameters set by utilizing GridSearchCV.  Could
# be option in the future
rbm.learning_rate = .06
rbm.n_iter = 20

# More components = better prediction but increased
# fitting time
rbm.n_components = 100

# inverse of regularization strength, smaller value specifies
# stronger regularization (penalty for large weights, limit
# overfitting)
logistic.C = 100

# Training RBM-Logistic Pipeline
classifier.fit(xtrain, ytrain)

# Training Logistic Regression
logistic_classifier = linear_model.LogisticRegression(C = 100)
logistic_classifier.fit(xtrain, ytrain)

############################################################
# Evaluation
print()
print("Logistic Regression with RBM as features:\n%s\n" % (
  metrics.classification_report(
    ytest, classifier.predict(xtest))))

print("Logistic Regression with pixels as features:\n%s\n" % (
  metrics.classification_report(
   ytest, logistic_classifier.predict(xtest))))

############################################################
# Generate trash and print image
v = np.zeros((784))
for i in range(len(v)):
  v[i]=np.random.randint(0,2)

# Turn trash into picture
plt.imshow(v.reshape((28,28)), cmap=plt.cm.gray_r, interpolation='nearest')
plt.show()

# Picture from rbm components
for i in range(100):
  v = rbm.gibbs(v)
  plt.imshow(v.reshape((28,28)), cmap=plt.cm.gray_r, interpolation='nearest')
  if i % 10 == 0:
    plt.show()
