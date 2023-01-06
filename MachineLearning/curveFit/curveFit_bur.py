#!/usr/bin/python
import numpy as np
import sys
import matplotlib.pyplot as plt

def output_training(n,xs):
    t=np.sin(2*np.pi*xs) + np.random.normal(scale=.15,size=(n,1))
    return t

def output_test(x,mean,sd):
    output=np.sin(2*np.pi*x) + np.random.normal(loc=mean,scale=sd)
    return output

def phi_matrix(n,m,xs):
    Phi = np.ones((n,m+1))
    #don't know why the if/else is needed
    if (n is not 1):
        for column in range(1,m+1):
            Phi[:,column]=np.power(xs,column)[:,0]
    else:
        for column in range(1,m+1):
            Phi[0,column]=np.power(xs,column)
        Phi = Phi.T

    return Phi

#def calc_S(Phi_train,Phi_test,m,n,alpha,beta):
def calc_S(Phi_train,m,n,alpha,beta):
    #calc alpha term
    I=np.identity((m+1))
    alpha_term=alpha*I
    
    #calc beta term with summation
    #find way to break down into sum into matrix mult
    summation=np.zeros((m+1,m+1))
    for data_point in range (0,n):
        Phi_xn=np.array(Phi_train[data_point,:])[np.newaxis]
        summation+=Phi_xn.dot(Phi_xn.T)
    beta_term=beta*summation

    #add alpha and beta term
    S_inv=alpha_term+beta_term
    S=np.linalg.inv(S_inv)
    return S

def calc_mean(Phi_train,Phi_test,n,m,S,t_n,beta):
    #calc sum
    """t_diag=np.zeros((n,n))
    for i in range(len(t_n)):
       t_diag[i][i]=t_n.item(i)
    sum_equiv=Phi_train.dot(t_diag)"""
    summation=np.zeros((m+1,1))
    for data_point in range (0,n):
        #doesnt work with np.arrays
        Phi_xnT=np.array(Phi_train[data_point,:])[np.newaxis] #1 x m+1
        Phi_xn=Phi_xnT.T #m+1x1
        t=t_n.item(data_point)
        summation+=Phi_xn*t

    #calc beta*phi_T*S
    #Phi_testT=Phi_test.T
    temp=beta*Phi_testT

    #calc mean through multiplication
    temp=temp.dot(S)
    mean=temp.dot(summation)    
    return mean

def calc_sd(Phi_test,S,beta):
    beta_inv=1/beta

    Phi_testT=Phi_test.T
    temp=Phi_testT.dot(S)
    second_term=temp.dot(Phi_test)

    var=beta_inv+second_term
    sd=np.sqrt(var)
    return sd

def main():
    #################### Constants ##################
    n=int(sys.argv[1])
    m=int(sys.argv[2])
    alpha=.005
    beta=11.1
    #################################################
    
    ############## Training Data ####################
    training_xs=np.random.rand(n,1)
    t_n=output_training(n,training_xs)
    Phi_train=phi_matrix(n,m,training_xs)
    #################################################
    
    ############### Test Data #######################
    #use smaller test set
    n_test=500
    #test_xs=np.linspace(0,1).T #do i use random.rand or linespace here
    #test_xs=np.random.rand(n_test,1)
    test_xs=np.linspace(0,1,num=n_test)

    #create matrix for output values 
    mean_matrix=np.ones((n_test,1))
    sd_matrix=np.ones((n_test,1))
    output_matrix=np.ones((n_test,1))
    m_point_matrix=np.ones((n_test,1))

    """need to calculate S matrix, mean, and variance
    for each point and calculate output for x value"""
    for point in range(0,n_test):
        data_point=test_xs.item(point)
        Phi_test=phi_matrix(1,m,data_point)

        S=calc_S(Phi_train,m,n,alpha,beta)
        m_point=calc_mean(Phi_train,Phi_test,n,m,S,t_n,beta)
        m_point_matrix[point,0]=m_point
        #sd_point=calc_sd(Phi_test,S,beta)

        #output_matrix[point,0]=output_test(data_point,m_point,sd_point)
        #mean_matrix[point,0]=m_point
        #sd_point[point,0]=sd_point
    #################################################

    #plots xs with t as a scatter plot,
    #the true function in green
    #the approximate function in red
    plt.plot(training_xs,t_n,'o',test_xs,np.sin(2*np.pi*test_xs),test_xs,m_point_matrix)
    plt.show()

if __name__ == "__main__":
  main()
