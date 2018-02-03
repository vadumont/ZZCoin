#!/usr/bin/env python3
import numpy as np
import random
import matplotlib.pyplot as plt

import cv2
def creerMatricePoids(nbrParCouche):
	
	W = [] #liste contenant toutes les matrices des poids

	x = 0
	for j in range(len(nbrParCouche)-1):	
		x = np.array([(np.random.randn() * np.sqrt(2/nbrParCouche[j])) for i in range(nbrParCouche[j]*nbrParCouche[j+1])]).reshape(nbrParCouche[j+1],nbrParCouche[j])
		W.append(x)
	
	return(W)

def creerMatriceBiais(nbrParCouche):

	B = [] #liste contenant toutes les matrices colonnes des biais

	x = 0
	for j in range(1,len(nbrParCouche)):	
		x = np.array([(np.random.randn() * np.sqrt(2/nbrParCouche[j])) for i in range(nbrParCouche[j])]).reshape(nbrParCouche[j],1) # création d'une matrice colonne
		B.append(x)
	
	return(B)
	
def forward(E,W,B):
	
	Z = [] # liste de toutes les WA + B
	z = 0
	z = np.dot(W[0],E) + B[0]
	Z.append(z)
	A = []
	A.append(fonctionSigmoid(z))

	for i in range(1,len(W)):
		z = np.dot(W[i],A[len(A)-1]) + B[i]		
		Z.append(z)
		A.append(fonctionSigmoid(z))
	
	return(A,Z)

def backpropagation(E,A,Z,Y,W):
	# Calcul des deltas
	
	delta1 = deriveePartielle(A[-1],Y) * fonctionSigmoidPrim(Z[-1])
	
	delta2 = []
	
	temp = 0
	temp = np.dot(np.transpose(W[-1]),delta1) * fonctionSigmoidPrim(Z[-2])
	delta2.append(temp)
	
	for i in range(2,len(W)):
		temp = np.dot(np.transpose(W[-i]),delta2[-i+2]) * fonctionSigmoidPrim(Z[-i-1])	
		delta2.append(temp)

	#calcul des dérivées partielles	
	dEdB = []
	
	dEdB.append(delta1)
	for j in range(len(delta2)):
		dEdB.append(delta2[j])
		
	
	dEdW = []
	dEdW.append(np.dot(delta1,np.transpose(A[-2])))


	
	for k in range(len(delta2)-1):
		dEdW.append(np.dot(delta2[k],np.transpose(A[-3-k])))
	dEdW.append(np.dot(delta2[len(delta2)-1],np.transpose(E)))

	return(dEdB,dEdW)

def getTrainSet():
		
	train_set = []	
	f = open("DataBase/database.txt","r")
	sortie = []

	ligne = f.readline()	
	
	while ligne:	
		sortie.append(int(ligne[:-1]))
		ligne = f.readline()
		
	for i in range(39):
		img = cv2.imread("DataBase/" + str(i) + ".jpg",0)
		img = resize(img)
		img = img.reshape(1200,1)
		train_set.append([img/255,sortie[i]])

	return(train_set)

def getTestSet():
		
	test_set = []	
	f = open("TestBase/testbase.txt","r")
	sortie = []

	ligne = f.readline()	
	
	while ligne:	
		sortie.append(int(ligne[:-1]))
		ligne = f.readline()
		
	for i in range(6):
		img = cv2.imread("TestBase/" + str(i) + ".jpg",0)
		img = resize(img)
		img = img.reshape(1200,1)
		test_set.append([img/255,sortie[i]])
	return(test_set)
	
def stochasticGradientDescent(nbrParCouche):
	eta = 0.1

	train_set = getTrainSet()
	test_set = getTestSet()

	print(len(test_set))

	W = creerMatricePoids(nbrParCouche)
	B = creerMatriceBiais(nbrParCouche)

	erreur = []

	for k in range(10): # 30 epochs
		print("Epoch {} : ".format(k),end="")

		for j in range(len(train_set)): # on entraine
	
			A,Z = forward(train_set[j][0],W,B)
			
			dEdB,dEdW = backpropagation(train_set[j][0],A,Z,numberToVector(train_set[j][1]),W)
	
			for i in range(len(dEdB)):
		
				B[i] = B[i] - eta*dEdB[-1-i]
			
			for i in range(len(dEdW)):
				#print(eta*dEdW[-1-i])
				W[i] = W[i] - eta*dEdW[-1-i]
		count = 0

		for j in range(len(test_set)):
			A,Z = forward(test_set[j][0],W,B)
			if vectorToNumber(A[-1]) == test_set[j][1]:
				count += 1

		print("{} / {}".format(count,len(test_set)))

	A,Z = forward(test_set[3][0],W,B)
	
	print(A[-1])

def vectorToNumber(vector):

	maximun = 0
	indice = 0
	for i in range(len(vector)):
		if vector[i][0] >= maximun:
			maximun = vector[i][0]
			indice = i
	return(indice)	 

def numberToVector(x):
	y = []
	for i in range(2):
		if i == x:
			y.append(1)
		else:
			y.append(0)
	
	y = np.array(y).reshape(2,1)
	
	return(y)

def resize(img):
	ratio = 40.0 / img.shape[1]
	dim = (40, int(img.shape[0] * ratio))
 
	# perform the actual resizing of the image and show it
	resized = cv2.resize(img, dim, interpolation = cv2.INTER_AREA)
	
	return(resized)


def fonctionSigmoid(x):
	return(1.0/(1.0+np.exp(-x)))

def fonctionSigmoidPrim(x):
	return(fonctionSigmoid(x)*(1-fonctionSigmoid(x)))

def deriveePartielle(A,Y):
	return(A-Y)



#####################################################################################
nbrParCouche = [2,50,17]
stochasticGradientDescent(nbrParCouche)

























