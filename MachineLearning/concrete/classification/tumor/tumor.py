import numpy as np
import matplotlib.pyplot as plt
import sys
from sklearn.linear_model import LogisticRegression
from sklearn import metrics
from objectreader import Reader


filename = sys.argv[1]
read = Reader(filename)

# use this for finding best reg value between different splits
"""
bestReg = 0
bestAcc = 0
plotx = []
ploty = []
for reg in np.linspace(.0000001, 10, 20):
	plotx.append(reg)
	###
		#	parameters: penalty: l1 or l2
		#							dual: (somehow relates to finding the global min) 
		#							C: regularization parameter (in this case how much we step by?)
		#							fit_intercept: why wouldn't you want to have this set to true?
	###
	logreg = LogisticRegression(penalty='l2', dual=False, C=reg)
	
	splitScore = []
	for i in range(50):
		xs, ts, xt, tt = read.split(.75)
		ts.shape = len(ts)
		tt.shape = len(tt)

		xmean = np.mean(xs, axis=0)
		xstd = np.std(xs, axis=0)
		xs = np.subtract(xs, xmean)
		xs = np.divide(xs, xstd)
		xt = np.subtract(xt, xmean)
		xt = np.divide(xt, xstd)

		# fit the data
		logreg.fit(xs, ts)
		# predict
		T = logreg.predict(xt)
	
		# get probability estimates for each prediction
		# the closer to 50% each prediction is, the more unsure the classifie was?
		gp = logreg.predict_proba(xt)
		temp = logreg.decision_function(xt)
		

		# get total score for the split and for the reg. parameter
		score = logreg.score(xt, tt)
		splitScore.append(score)

	splitScore = np.mean(splitScore)
	ploty.append(splitScore)
	print splitScore
	if splitScore > bestAcc:
		bestAcc = splitScore
		bestReg = reg

print '\nBest accuracy: ',bestAcc
print 'Best reg. param: ',bestReg

plt.plot(plotx, ploty)
plt.show()
"""

logreg = LogisticRegression(penalty='l2', dual=False, C=.5)
xs, ts, xt, tt = read.split(.75)

xmean = np.mean(xs, axis=0)
xstd = np.std(xs, axis=0)
xs = np.subtract(xs, xmean)
xs = np.divide(xs, xstd)
xt = np.subtract(xt, xmean)
xt = np.divide(xt, xstd)

ts.shape = len(ts)
tt.shape = len(tt)
# fit the data
logreg.fit(xs, ts)
print 'coefficients',logreg.coef_
# predict
T = logreg.predict(xt)

class_names = ['malignant', 'benign']
print metrics.classification_report(tt, T, target_names=class_names)

# get the error values (distance from the hyperplane) for each prediction -- the closer = worse prediction
errorValues = logreg.decision_function(xt)
malignant,mx,benign,bx = [],[],[],[]
for i in range(len(tt)):
	if errorValues[i] > 0:
		bx.append(i)
		benign.append(errorValues[i])
	else:
		mx.append(i)
		malignant.append(errorValues[i])

fig,ax = plt.subplots()
ax.plot(mx, malignant, 'ro', label='Malignant')
ax.plot(bx, benign, 'D', label='Benign')
ax.plot([0,200], [0,0], label='Decision Boundary')
legend = ax.legend(loc='upper right', shadow=True)
plt.show()

