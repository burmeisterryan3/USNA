import numpy as np
import matplotlib.pyplot as plt
import matplotlib.pyplot as plt

# Get Phi matrix
def get_phi(N, M1, x_values):
  Phi=np.ones((N,M1))
  for column in range(1,M1):
    Phi[:,column]=np.power(x_values,column)[:,0]
  return Phi

# Get S matrix
def get_s(N, M1, phi_train, beta, alpha):
  I = np.identity((M1))
  temp = np.zeros((M1,M1))
  count = 0
  for count in range(0,N):
    phi_row = np.array(phi_train[count,:])
    phi_row.shape = (M1,1)
    phi_rowT = np.transpose(phi_row)
    temp += np.dot(phi_row, phi_rowT)
  right_side = np.dot(beta, temp)
  left_side = np.dot(alpha, I)
  s_inverse = np.add(left_side, right_side)
  S = np.linalg.inv(s_inverse)
  return S

# Get mean  
def get_mean(N, M1, phi_train, phi_test, beta, t_train, S):
  summation = np.zeros((M1,1))
  means = np.zeros((1,N))
  for row in range(0,M1):
    train_phi_row = np.array(phi_train[row,:])
    train_phi_row.shape = (M1,1)
    t = np.float(t_train[row,:])
    temp = np.dot(train_phi_row, t)
    summation += temp

  S_sum = np.dot(S, summation)

  for count in range(0,N):
    test_phi_row = phi_test[count,:]
    test_phi_row.shape = (M1,1)
    test_phi_rowT = np.transpose(test_phi_row)
    temp = np.dot(beta,test_phi_rowT)
    mean = np.dot(temp, S_sum)
    means[:,count] = mean
  
  return means  

# Get variance
def get_variance(N, M1, phi_test, beta, S):
  variance = np.zeros((1,N))
  for count in range(0,N):
    test_phi_row = phi_test[count,:]
    test_phi_row.shape = (M1,1)
    test_phi_rowT = np.transpose(test_phi_row)
    temp = np.dot(test_phi_rowT, S)
    var = np.dot(temp,test_phi_row)
    variance[:,count] = var + (1/beta)
    
  return variance


# setting training data 
N = 3
M = 2
M1 = 3
x_train = np.array([[.25],[.5],[.75]])
t_train = np.sin(2*np.pi*x_train)
phi_train = np.ones((N,M1))
phi_train = get_phi(N,M1,x_train)

# set test data
x_test = np.array([[.15],[.4],[.65]])
phi_test = np.ones((N,M1))
phi_test = get_phi(N,M1,x_test)

# what the t_values should be for the x_test values
# these values are never used, only for comparison
t_actual = np.sin(2*np.pi*x_test)


alpha = .005
beta = 11.1

S = get_s(N, M1, phi_train, beta, alpha)

means = np.zeros((1,N))
means = get_mean(N, M1, phi_train, phi_test, beta, t_train, S)
means.shape = (N,1)

variances = np.zeros((1,N))
variances = get_variance(N, M1, phi_test, beta, S)

print means
print x_test
print variances


plt.plot(x_train,t_train,'o',x_test,np.sin(2*np.pi*x_test), x_test, means)
plt.show()
