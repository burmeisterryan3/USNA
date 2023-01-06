from sklearn.datasets import fetch_mldata
from sklearn.pipeline import Pipeline
from sklearn import linear_model, metrics, cross_validation
from sklearn.neural_network import BernoulliRBM
from sklearn.cross_validation import ShuffleSplit
from matplotlib import pyplot as plt
import numpy as np

# load in the dataset - maybe there is a way to only load in the 0s
mnist = fetch_mldata('MNIST original')


temp = []
targets = []
# load in the 0s
for index in range(len(mnist.data)):
  
  # uncomment this if you want to train on a smaller portion of 0s or mess with the training
#  if index == 10:
#    break
  # train on 0s and 1s 
  #if mnist.target[index] == 0.0 or mnist.target[index] == 1.0:
    #temp.append(mnist.data[index])
#  if mnist.target[index] == 0 or mnist.target[index] == 1 or mnist.target[index] == 5:
   if mnist.target[index] == 0:
    temp.append(mnist.data[index])
    targets.append(mnist.target[index])

# if you want to do the whole dataset do the following:
# temp,targets = mnist.data, mnist.target
X = np.asarray(temp, 'float32')
Y = np.asarray(targets)

# convert to binary image - this helps the RBM out a lot!
X = (X - np.min(X,0)) / (np.max(X,0) + .0001)
X = X > 0.5



### show what a random picture looks like after you bounce it back and forth in RBM ###

# reduce demensions to 20x20
# n_iter = 2 (this is k, text says that it is typically 2)
model = BernoulliRBM(n_components=200, learning_rate=0.1, n_iter=40, verbose=True)
model.fit(X)

# generate a trash image
v=np.zeros((784))
for i in range(784):
  v[i]=np.random.randint(0,2)

plt.imshow(v.reshape((28,28)),cmap=plt.cm.gray_r,interpolation='nearest')
plt.show()

# turn trash into picture
for i in range(100):
  v = model.gibbs(v)
  plt.imshow(v.reshape((28,28)),cmap=plt.cm.gray_r,interpolation='nearest')
  if i % 10 == 0:
    plt.show()
# picture from rbm components
plt.imshow(v.reshape((28,28)),cmap=plt.cm.gray_r,interpolation='nearest')
plt.show()

print np.size(model.components_)
for i,comp in enumerate(model.components_):
  plt.imshow(comp.reshape((28,28)),cmap=plt.cm.gray_r,interpolation='nearest')
plt.show()
'''


#### (compare log reg.) vs. (rbm w/ log reg) ###
# could you train one rbm with just the zero's training set and then test it on the rest of the data
# that way when you shot data into the log. reg there would be multiple classes and it would predict when it got the 0's right or wrong
# as of now, you are training one RBM on all of the data, that is what all of the examples are though

# training
logistic = linear_model.LogisticRegression(C=10)
rbm = BernoulliRBM(n_components=200, n_iter=10, random_state=None, verbose=True)
classifier = Pipeline(steps=[('rbm', rbm), ('logistic', logistic)])
X_train, X_test, Y_train, Y_test = cross_validation.train_test_split(X, Y, test_size=.2, random_state=0)

classifier.fit(X_train, Y_train)
logistic = linear_model.LogisticRegression(C=10)
logistic.fit(X_train, Y_train)

# evaluation - as you increase the number of classes, the prediction gets worse
print ('Logistic regression using RBM freatures:\n%s\n' % (
  metrics.classification_report(Y_test, classifier.predict(X_test))))

print ('Logistic regression using raw pixel features:\n%s\n' % (
  metrics.classification_report(Y_test, logistic.predict(X_test))))

for i,comp in enumerate(rbm.components_):
  plt.imshow(comp.reshape((28,28)),cmap=plt.cm.gray_r,interpolation='nearest')
plt.show()


### Look at what stacks of RBMs can do ###

X_train, X_test, Y_train, Y_test = cross_validation.train_test_split(X, Y, test_size=.2, random_state=0)
logistic = linear_model.LogisticRegression(C=10)
rbm1 = BernoulliRBM(n_components=200, n_iter=2, random_state=None, verbose=True)
rbm2 = BernoulliRBM(n_components=100, n_iter=2, random_state=None, verbose=True)
rbm3 = BernoulliRBM(n_components=50, n_iter=2, random_state=None, verbose=True)
rbm4 = BernoulliRBM(n_components=100, n_iter=2, random_state=None, verbose=True)
rbm5 = BernoulliRBM(n_components=200, n_iter=2, random_state=None, verbose=True)
rbm6 = BernoulliRBM(n_components=784, n_iter=2, random_state=None, verbose=True)
rbm7 = BernoulliRBM(n_components=200, n_iter=2, random_state=None, verbose=True)


# just do this to see what it looks like when it starts at 200 and when it goes to 50 then back to 200
classifier3 = Pipeline(steps=[('rbm1',rbm1), ('logistic',logistic)])
classifier3.fit(X_train, Y_train)
print ('Logistic regression using RBM features:\n%s\n' % (
  metrics.classification_report(Y_test, classifier3.predict(X_test))))


classifier2 = Pipeline(steps=[('rbm1',rbm1), ('rbm2', rbm2), ('rbm3',rbm3), ('logistic',logistic)])
classifier2.fit(X_train, Y_train)
print ('Logistic regression using 3 stacks of features:\n%s\n' % (
  metrics.classification_report(Y_test, classifier2.predict(X_test))))

X = []
Y = []
for index in range(len(mnist.data)):
  if mnist.target[index] == 0:
    X.append(mnist.data[index])
    Y.append(mnist.target[index])
X_train, X_test, Y_train, Y_test = cross_validation.train_test_split(X, Y, test_size=.2, random_state=0)
    
classifier = Pipeline(steps=[('rbm1',rbm1), ('rbm2', rbm2), ('rbm3',rbm3), ('rbm4',rbm4), ('rbm5', rbm5), ('rbm6', rbm6), ('rbm7', rbm7)])
classifier.fit(X_train, Y_train)

# show what it looks like after it has been shot forward and then back again
for i,comp in enumerate(rbm1.components_):
  plt.imshow(comp.reshape((28,28)),cmap=plt.cm.gray_r,interpolation='nearest')
plt.show()
for i,comp in enumerate(rbm7.components_):
  plt.imshow(comp.reshape((28,28)),cmap=plt.cm.gray_r,interpolation='nearest')
plt.show()

v=np.zeros((784))
for i in range(784):
  v[i]=np.random.randint(0,2)

plt.imshow(v.reshape((28,28)),cmap=plt.cm.gray_r,interpolation='nearest')
plt.show()

# turn trash into picture
for i in range(100):
  v = rbm7.gibbs(v)
  plt.imshow(v.reshape((28,28)),cmap=plt.cm.gray_r,interpolation='nearest')
  if i % 10 == 0:
    plt.show()
# picture from rbm components
plt.imshow(v.reshape((28,28)),cmap=plt.cm.gray_r,interpolation='nearest')
plt.show()
'''

