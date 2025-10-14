from class_joueur import Joueur  # importer la classe Joueur depuis class_joueur.py
from algo_jeu import jeu  # importer la classe jeu depuis algo_jeu.py

# fonction principale du jeu

def main():
	j1 = Joueur('Joueur1')  # création du joueur 1 avec nom 'Joueur1'
	j2 = Joueur('Joueur2')  # création du joueur 2 avec nom 'Joueur2'
	sizes = [3, 2]  # tailles des bateaux à placer pour chaque joueur
	for joueur in (j1, j2):
		print(f"Placement pour {joueur.nom}")  # indiquer pour quel joueur on place les bateaux
		for s in sizes:
			placed = False  # drapeau indiquant si le bateau a été placé correctement
			while not placed:
				choice = input(f"Placer bateau taille {s} automatiquement ? (o/n) : ")  # demander automatique ou manuel
				if choice.lower().startswith('o'):
					import random  # import local de random pour placement automatique
					orientation = random.choice(['H', 'V'])  # choisir orientation aléatoire H/V
					if orientation == 'H':
						r = random.randint(0, joueur.size - 1)  # ligne aléatoire valide
						c = random.randint(0, joueur.size - s)  # colonne aléatoire valide pour tenir le bateau
					else:
						r = random.randint(0, joueur.size - s)  # ligne aléatoire valide pour orientation verticale
						c = random.randint(0, joueur.size - 1)  # colonne aléatoire valide
					placed = joueur.placer_bateau(s, (r, c), orientation)  # tenter le placement automatique
				else:
					raw = input('Entrer position (ex A1) et orientation H/V séparés par espace: ')  # saisie manuelle
					parts = raw.split()  # séparation position et orientation
					if len(parts) < 2:
						print('Entrée invalide')  # vérification minimale
						continue
					pos, orientation = parts[0], parts[1].upper()  # extraction de la position et orientation
					row = ord(pos[0].upper()) - ord('A')  # conversion lettre -> indice ligne
					try:
						col = int(pos[1:]) - 1  # conversion chiffre -> indice colonne
					except ValueError:
						print('Entrée invalide')  # gestion d'erreur de conversion
						continue
					placed = joueur.placer_bateau(s, (row, col), orientation)  # tenter le placement manuel
					if not placed:
						print('Placement invalide, réessayero')  # message d'erreur en cas d'échec de placement
	g = jeu(j1, j2)  # instancier le jeu avec les deux joueurs
	winner = g.demarrer()  # démarrer le jeu et récupérer le gagnant
	print('Winner:', winner.nom)  # afficher le nom du gagnant
	print(j1.nom + ' grille:')  # afficher la grille du joueur 1
	print(j1.afficher_grille())  # afficher représentation textuelle de la grille 1
	print(j2.nom + ' grille:')  # afficher la grille du joueur 2
	print(j2.afficher_grille())  # afficher représentation textuelle de la grille 2

if __name__ == '__main__':
	main()  # exécuter main si le script est lancé directement
