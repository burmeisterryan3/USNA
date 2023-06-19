#!/bin/bash

luajit nnScripts/kernelSelection.lua -traindata SVHN_train_hog8.data -testdata SVHN_test_hog8.data -gridsearch false -threads 1 -model linear -nfeatures 648 -noutput 10 -numClasses 10 -stdDev 1.511 -nnodes 50 -nEpochs 1 -trsize .25
