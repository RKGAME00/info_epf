
from class_joueur import Joueur
from algo_jeu import jeu

def main():
	j1 = Joueur('Joueur1')
	j2 = Joueur('Joueur2')
	sizes = [3, 2]
	for joueur in (j1, j2):
		print(f"Placement pour {joueur.nom}")
		for s in sizes:
			placed = False
			while not placed:
				choice = input(f"Placer bateau taille {s} automatiquement ? (o/n) : ")
				if choice.lower().startswith('o'):
					import random
					orientation = random.choice(['H', 'V'])
					if orientation == 'H':
						r = random.randint(0, joueur.size - 1)
						c = random.randint(0, joueur.size - s)
					else:
						r = random.randint(0, joueur.size - s)
						c = random.randint(0, joueur.size - 1)
					placed = joueur.placer_bateau(s, (r, c), orientation)
				else:
					raw = input('Entrer position (ex A1) et orientation H/V séparés par espace: ')
					parts = raw.split()
					if len(parts) < 2:
						print('Entrée invalide')
						continue
					pos, orientation = parts[0], parts[1].upper()
					row = ord(pos[0].upper()) - ord('A')
					try:
						col = int(pos[1:]) - 1
					except ValueError:
						print('Entrée invalide')
						continue
					placed = joueur.placer_bateau(s, (row, col), orientation)
					if not placed:
						print('Placement invalide, réessayer')
	g = jeu(j1, j2)
	winner = g.demarrer()
	print('Winner:', winner.nom)
	print(j1.nom + ' grille:')
	print(j1.afficher_grille())
	print(j2.nom + ' grille:')
	print(j2.afficher_grille())

if __name__ == '__main__':
	main()
