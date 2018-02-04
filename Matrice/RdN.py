#!/usr/bin/env python3


import numpy as np
import random
import matplotlib.pyplot as plt
import sys
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
	
	train_set = [
	[(np.array([34, 119, 50, 59, 0, 0, 0, 0])/144).reshape(8,1),1],
	[(np.array([34, 93, 59, 0, 0, 0, 0, 0])/144).reshape(8,1),1],
	[(np.array([91, 29, 92, 83, 0, 0, 0, 0])/144).reshape(8,1),2],
	[(np.array([122, 29, 92, 83, 0, 0, 0, 0])/144).reshape(8,1),2],
	[(np.array([122, 29, 65, 83, 0, 0, 0, 0])/144).reshape(8,1),2],
	[(np.array([91, 29, 65, 83, 0, 0, 0, 0])/144).reshape(8,1),2],
	[(np.array([106, 120, 15, 83, 0, 0, 0, 0])/144).reshape(8,1),2],
	[(np.array([106, 53, 91, 83, 0, 0, 0, 0])/144).reshape(8,1),2],
	[(np.array([34, 93, 77, 69, 144, 60, 0, 0])/144).reshape(8,1),3],
	[(np.array([34, 137, 58, 59, 0, 0, 0, 0])/144).reshape(8,1),3],
	[(np.array([34, 86, 29, 24, 0, 0, 0, 0])/144).reshape(8,1),4],
	[(np.array([113, 61, 10, 29, 27, 0, 0, 0])/144).reshape(8,1),4],
	[(np.array([115, 22, 29, 24, 68, 66, 0, 0])/144).reshape(8,1),5],
	[(np.array([105, 30, 29, 80, 0, 0, 0, 0])/144).reshape(8,1),5],
	[(np.array([34, 137, 17, 29, 24, 0, 0, 0])/144).reshape(8,1),6],
	[(np.array([34, 126, 12, 29, 0, 0, 0, 0])/144).reshape(8,1),6],
	[(np.array([34, 111, 31, 39, 0, 0, 0, 0])/144).reshape(8,1),7],
	[(np.array([34, 124, 31, 39, 0, 0, 0, 0])/144).reshape(8,1),7],
	[(np.array([34, 61, 88, 29, 0, 0, 0, 0])/144).reshape(8,1),8],
	[(np.array([34, 118, 57, 37, 0, 0, 0, 0])/144).reshape(8,1),9],
	[(np.array([113, 61, 57, 37, 0, 0, 0, 0])/144).reshape(8,1),9],
	[(np.array([113, 61, 109, 37, 0, 0, 0, 0])/144).reshape(8,1),9],
	[(np.array([32, 44, 29, 24, 0, 0, 0, 0])/144).reshape(8,1),10],
	[(np.array([114, 108, 29, 24, 0, 0, 0, 0])/144).reshape(8,1),10],
	[(np.array([34, 124, 7, 74, 0, 0, 0, 0])/144).reshape(8,1),11],
	[(np.array([114, 82, 9, 125, 76, 0, 0, 0])/144).reshape(8,1),11],
	[(np.array([121, 16, 8, 74, 104, 0, 0, 0])/144).reshape(8,1),12],
	[(np.array([103, 121, 6, 74, 0, 0, 0, 0])/144).reshape(8,1),12],
	[(np.array([103, 121, 6, 84, 0, 0, 0, 0])/144).reshape(8,1),12],
	[(np.array([34, 121, 6, 84, 0, 0, 0, 0])/144).reshape(8,1),12],
	[(np.array([34, 121, 6, 74, 0, 0, 0, 0])/144).reshape(8,1),12],
	[(np.array([35, 74, 47, 100, 64, 6, 0, 0])/144).reshape(8,1),13],
	[(np.array([35, 76, 93, 100, 64, 0, 0, 0])/144).reshape(8,1),13],
	[(np.array([34, 111, 52, 93, 135, 101, 0, 0])/144).reshape(8,1),14],
	[(np.array([34, 62, 124, 91, 135, 0, 0, 0])/144).reshape(8,1),14],
	[(np.array([117, 29, 46, 70, 0, 0, 0, 0])/144).reshape(8,1),15],
	[(np.array([116, 29, 46, 70, 0, 0, 0, 0])/144).reshape(8,1),15],
	[(np.array([114, 136, 46, 70, 0, 0, 0, 0])/144).reshape(8,1),15],
	[(np.array([117, 46, 51, 0, 0, 0, 0, 0])/144).reshape(8,1),16],
	[(np.array([116, 46, 51, 0, 0, 0, 0, 0])/144).reshape(8,1),16],
	[(np.array([117, 29, 21, 131, 0, 0, 0, 0])/144).reshape(8,1),17],
	[(np.array([61, 97, 29, 24, 0, 0, 0, 0])/144).reshape(8,1),18],
	[(np.array([61, 141, 29, 24, 0, 0, 0, 0])/144).reshape(8,1),18],
	[(np.array([49, 129, 97, 29, 24, 0, 0, 0])/144).reshape(8,1),18],
	[(np.array([49, 129, 97, 29, 0, 0, 0, 0])/144).reshape(8,1),18],
	[(np.array([49, 129, 141, 29, 0, 0, 0, 0])/144).reshape(8,1),18],
	[(np.array([49, 129, 141, 29, 24, 0, 0, 0])/144).reshape(8,1),18],
	[(np.array([109, 97, 29, 0, 0, 0, 0, 0])/144).reshape(8,1),18],
	[(np.array([33, 55, 117, 61, 0, 0, 0, 0])/144).reshape(8,1),19],
	[(np.array([34, 61, 56, 0, 0, 0, 0, 0])/144).reshape(8,1),19],
	[(np.array([114, 134, 79, 0, 0, 0, 0, 0])/144).reshape(8,1),20],
	[(np.array([114, 99, 79, 0, 0, 0, 0, 0])/144).reshape(8,1),20],
	[(np.array([4, 5, 114, 43, 20, 89, 0, 0])/144).reshape(8,1),21],
	[(np.array([117, 45, 134, 63, 0, 0, 0, 0])/144).reshape(8,1),22],
	[(np.array([116, 45, 134, 63, 0, 0, 0, 0])/144).reshape(8,1),22],
	[(np.array([34, 124, 7, 74, 0, 0, 0, 0])/144).reshape(8,1),23],
	[(np.array([9, 130, 74, 0, 0, 0, 0, 0])/144).reshape(8,1),23],
	[(np.array([112, 48, 127, 0, 0, 0, 0, 0])/144).reshape(8,1),24],
	[(np.array([34, 48, 127, 0, 0, 0, 0, 0])/144).reshape(8,1),24],
	[(np.array([115, 22, 1, 0, 0, 0, 0, 0])/144).reshape(8,1),25],
	[(np.array([115, 73, 1, 0, 0, 0, 0, 0])/144).reshape(8,1),25],
	[(np.array([117, 14, 95, 0, 0, 0, 0, 0])/144).reshape(8,1),26],
	[(np.array([116, 14, 95, 0, 0, 0, 0, 0])/144).reshape(8,1),26],
	[(np.array([34, 81, 37, 75, 0, 0, 0, 0])/144).reshape(8,1),27],
	[(np.array([117, 37, 75, 0, 0, 0, 0, 0])/144).reshape(8,1),27],
	[(np.array([105, 128, 67, 96, 38, 0, 0, 0])/144).reshape(8,1),28],
	[(np.array([117, 43, 18, 139, 0, 0, 0, 0])/144).reshape(8,1),29],
	[(np.array([117, 3, 0, 0, 0, 0, 0, 0])/144).reshape(8,1),30],
	[(np.array([116, 3, 0, 0, 0, 0, 0, 0])/144).reshape(8,1),30],
	[(np.array([106, 13, 140, 24, 0, 0, 0, 0])/144).reshape(8,1),31],
	[(np.array([142, 13, 140, 24, 0, 0, 0, 0])/144).reshape(8,1),31],
	[(np.array([34, 78, 67, 36, 123, 0, 0, 0])/144).reshape(8,1),32],
	[(np.array([117, 94, 0, 0, 0, 0, 0, 0])/144).reshape(8,1),33],
	[(np.array([116, 94, 0, 0, 0, 0, 0, 0])/144).reshape(8,1),33],
	[(np.array([117, 87, 107, 0, 0, 0, 0, 0])/144).reshape(8,1),34],
	[(np.array([116, 87, 107, 0, 0, 0, 0, 0])/144).reshape(8,1),34],
	[(np.array([34, 42, 37, 25, 0, 0, 0, 0])/144).reshape(8,1),35],
	[(np.array([90, 42, 37, 25, 0, 0, 0, 0])/144).reshape(8,1),35],
	[(np.array([34, 19, 85, 138, 0, 0, 0, 0])/144).reshape(8,1),36],
	[(np.array([34, 19, 138, 0, 0, 0, 0, 0])/144).reshape(8,1),36],
	[(np.array([34, 30, 28, 98, 0, 0, 0, 0])/144).reshape(8,1),37],
	[(np.array([30, 28, 98, 0, 0, 0, 0, 0])/144).reshape(8,1),37],
	[(np.array([34, 40, 133, 25, 0, 0, 0, 0])/144).reshape(8,1),38],
	[(np.array([34, 23, 72, 132, 37, 0, 0, 0])/144).reshape(8,1),38],
	[(np.array([117, 134, 54, 0, 0, 0, 0, 0])/144).reshape(8,1),39],
	[(np.array([71, 134, 54, 0, 0, 0, 0, 0])/144).reshape(8,1),40],
	[(np.array([112, 11, 43, 18, 139, 0, 0, 0])/144).reshape(8,1),40],
	[(np.array([32, 79, 23, 0, 0, 0, 0, 0])/144).reshape(8,1),41],
	[(np.array([34, 26, 2, 0, 0, 0, 0, 0])/144).reshape(8,1),42]]

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
	eta = 2.3

	train_set = getTrainSet()
	test_set = getTrainSet()

	print(len(test_set))

	W = creerMatricePoids(nbrParCouche)
	B = creerMatriceBiais(nbrParCouche)

	erreur = []

	for k in range(10000): # 30 epochs
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

	A,Z = forward((np.array([116,45,134,63,0, 0, 0, 0])/144).reshape(8,1),W,B)
	np.save("weight1.npy",W[0])
	np.save("weight2.npy",W[1])
	np.save("bias1.npy",B[0])
	np.save("bias2.npy",B[1])
	print(vectorToNumber(A[-1]))
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
	for i in range(42):
		if i == x:
			y.append(1)
		else:
			y.append(0)
	
	y = np.array(y).reshape(42,1)
	
	return(y)

def getNumber(name):
	global listeWord
	
	for i in range(len(listeWord)):
		if(listeWord[i][0] == name):
			return(listeWord[i][1])

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

def getResponse():
	vector = []
	W = []
	B = []
	W.append(np.load("weight1.npy"))
	W.append(np.load("weight2.npy"))
	B.append(np.load("bias1.npy"))
	B.append(np.load("bias2.npy"))

	for arg in sys.argv:
		number = getNumber(arg)
		if(number != None):
			vector.append(number)
	
	for i in range(8 - len(vector)):
		vector.append(0)

	vector = (np.array(vector)/144).reshape(8,1)
	#print(vector)
	A,Z = forward(vector,W,B)
	#print(A[-1])
	print(vectorToNumber(A[-1]))

#####################################################################################
listeWord = [('PEL', 1), ('PTZ', 2), ('TEG', 3), ('Tiers', 4), ('Tous_risques', 5), ('achat', 6), ('achats', 7), ('achete', 8), ('acheter', 9), ('acquerir', 10), ('adherer', 11), ('aide', 12), ('annuler', 13), ('apport', 14), ('argent', 15), ('article', 16), ('assistance', 17), ('assurance', 18), ('assurer', 19), ('auto', 20), ('autorisation', 21), ('avantages', 22), ('avoir', 23), ('bancaire', 24), ('banque', 25), ('beneficier', 26), ('bleue', 27), ('bon', 28), ('carte', 29), ('choisir', 30), ('code', 31), ('combien', 32), ('commencer', 33), ('comment', 34), ('commercant', 35), ('complementaire', 36), ('compte', 37), ('conducteur', 38), ('confidentiel', 39), ('connaitre', 40), ('conseil', 41), ('consulter', 42), ('contrat', 43), ('coute', 44), ('credit', 45), ('debit', 46), ('debiter', 47), ('declarer', 48), ('demarche', 49), ('depense', 50), ('differe', 51), ('donnees', 52), ('effectuer', 53), ('endettement', 54), ('epargne', 55), ('epargner', 56), ('erreur', 57), ('especes', 58), ('etranger', 59), ('euro', 60), ('faire', 61), ('fais', 62), ('fixe', 63), ('fois', 64), ('fonctionne', 65), ('gamme', 66), ('garantie', 67), ('haut', 68), ('hors', 69), ('immediat', 70), ('information', 71), ('infos', 72), ('interets', 73), ('internet', 74), ('joint', 75), ('ligne', 76), ('liquide', 77), ('lire', 78), ('livret', 79), ('luxe', 80), ('marche', 81), ('moyen', 82), ('negation', 83), ('net', 84), ('nouveau', 85), ('obtenir', 86), ('offre', 87), ('opposition', 88), ('opter', 89), ('où', 90), ('paiement', 91), ('passe', 92), ('payer', 93), ('perp', 94), ('personnel', 95), ('personnelle', 96), ('perte', 97), ('placement', 98), ('plafond', 99), ('plusieurs', 100), ('portable', 101), ('posseder', 102), ('possibilite', 103), ('possible', 104), ('pourquoi', 105), ('pouvoir', 106), ('pret', 107), ('prix', 108), ('probleme', 109), ('profiter', 110), ('proteger', 111), ('quand', 112), ('que', 113), ('quel', 114), ('quels', 115), ('quoi', 116), ('qu’est-ce', 117), ('reagir', 118), ('regler', 119), ('retirer', 120), ('retourner', 121), ('retrait', 122), ('sante', 123), ('securiser', 124), ('securisée', 125), ('servir', 126), ('sinistre', 127), ('souscrire', 128), ('suivre', 129), ('surement', 130), ('systematique', 131), ('tarification', 132), ('tarifs', 133), ('taux', 134), ('telephone', 135), ('utilisation', 136), ('utiliser', 137), ('vehicule', 138), ('vie', 139), ('virement', 140), ('vol', 141), ('vouloir', 142), ('zero', 143), ('zone', 144)]
getResponse()

nbrParCouche = [8,50,42]

#stochasticGradientDescent(nbrParCouche)
























