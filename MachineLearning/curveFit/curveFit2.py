import numpy as np
import math
import sys
import matplotlib.pyplot as plt

def get_target_values(n, x_values):
	# sin curve with noise (S.D = .15 for each n)
	t=np.sin(2*np.pi*x_values) + np.random.normal(scale=.15, size=(n,1))
	return t

def get_Phi(n, m1, x_values):
	# fill matrix with all ones
	Phi=np.ones((n,m1))
	# mult the value of x, each feature, by its cooresponding power, then store in Phi
	for column in range(1,m1):
		Phi[:,column]=np.power(x_values,column)[:,0]
	return Phi

def get_S_inverse(m1, n, Phi, beta, alpha):
	# unit matrix
	I = np.identity((m1))
	# hold values of inverse S before beta and alpha 
	temp = np.zeros((m1,m1))
	count = 0
	# sum in 1.72
	# from 1-n, sums up the product of phi vectors (Xi from i=0 to M) 
	for count in range(0,n):
		Phi_row = np.array(Phi[count,:])
		Phi_row.shape = (m1,1)
		Phi_rowT = np.transpose(Phi_row)
		temp += np.dot(Phi_row, Phi_rowT)
	# muliply I by alpha and summation by beta
	beta_Phi = np.dot(beta,temp)
	alpha_I  = alpha * np.matrix(I)
	S_inverse = beta_Phi + alpha_I
	return S_inverse

def get_variances(variances, test_Phi, m1, beta, S):
	for row in range(0, len(test_Phi)):
		Phi_row = np.array(test_Phi[row,:])
		Phi_row.shape = (m1, 1)
		Phi_rowT = np.transpose(Phi_row)
		temp = np.dot(Phi_rowT, S)
		temp2 = np.dot(temp, Phi_row )
		var = (1/beta) + temp2
		variances[:,row] = var

def get_mean(means, test_Phi, train_Phi, S, training_t_values, m1, n, beta):
	# calculate sum portion of mean equation
	summation = np.zeros((m1,1))
	for row in range(0, len(train_Phi)):
		train_Phi_row = np.array(train_Phi[row,:])
		train_Phi_row.shape = (m1, 1)
		t_value = training_t_values[row,:]
		temp = np.dot(train_Phi_row, t_value)
		temp.shape = (m1, 1)
		summation = summation + temp

	for row in range(0, len(test_Phi)):
		test_Phi_row = np.array(test_Phi[row,:])
		test_Phi_row.shape = (m1, 1)
		test_Phi_rowT = np.transpose(test_Phi_row)
		temp2 = np.dot(beta, test_Phi_rowT)
		temp3 = np.dot(temp2, S)
		mean = np.dot(temp3, summation)
		means[:,row] = mean
		



def main():
	# TRAINING SET #
	# generate x and t values of training set
	n=int(sys.argv[1])
	m=int(sys.argv[2])
	m1 = m+1
	training_x_values = np.random.rand(n,1)
	training_t_values = get_target_values(n, training_x_values)
	# fill Phi matrix
	train_Phi = get_Phi(n, m1, training_x_values)
	

	# TEST SET #
	# calculate Bayesian curve fit with new test_x_values#
	alpha = .005
	beta = 11.1
	
	# generate test_x_values that will use the train_x_values
	test_x_values = np.random.rand(n,1)
	test_Phi = get_Phi(n, m1, test_x_values)

	
	# calculate the inverse S
	S_inverse = get_S_inverse(m1, n, train_Phi, beta, alpha)
	S = (1/S_inverse)
	# calculate variance for each test_x_value
	variances = np.zeros((1,n))
	get_variances(variances, test_Phi, m1, beta, S)
	# calculate mean for each test_x_value
	means = np.zeros((1,n))
	get_mean(means, test_Phi, train_Phi, S, training_t_values, m1, n, beta)
	means = np.transpose(means)

	
#	plt.plot(training_x_values, training_t_values, 'o', test_x_values, np.sin(2*np.pi*test_x_values))
	plt.plot(test_x_values, means, 'o')
	plt.show()




if __name__ == "__main__":
	main()
