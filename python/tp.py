"""
@file tp.py
@brief Point d'entrée CLI pour lancer une partie de Bataille Navale.

Le script propose de reprendre une sauvegarde si elle existe, sinon il
procède au placement des bateaux (manuel ou automatique) puis démarre la
boucle de jeu fournie par `Jeu`. À la fin de la partie, le meilleur score
est mis à jour.
"""

from class_joueur import Joueur
from algo_jeu import Jeu
import gestion_sauvegarde as sauvegarde


def main():
	"""
	@brief Point d'entrée principal.

	- Si une sauvegarde existe, propose de la reprendre.
	- Sinon, permet de placer les bateaux pour chaque joueur et lance la partie.
	"""
	saved = sauvegarde.load_game()
	resumed = False
	deleted_by_user = False
	if saved:
		choice = input('Une sauvegarde existe. Reprendre la partie ? (o/n) : ')
		if choice.lower().startswith('o'):
			j1 = saved['joueur1']
			j2 = saved['joueur2']
			g = Jeu(j1, j2)
			g.tour = saved.get('tour', 0)
			winner = g.demarrer()
			sauvegarde.update_highscores(winner)
			# supprimer la sauvegarde car la partie reprise est terminée
			try:
				sauvegarde.delete_save()
			except Exception:
				pass
			print('Winner:', winner.nom)
			print(j1.nom + ' grille:')
			print(j1.afficher_grille())
			print(j2.nom + ' grille:')
			print(j2.afficher_grille())
			return
		else:
			# si l'utilisateur ne veut pas reprendre, proposer de supprimer la sauvegarde
			choice2 = input('Voulez-vous supprimer la sauvegarde existante ? (o/n) : ')
			if choice2.lower().startswith('o'):
				try:
					sauvegarde.delete_save()
					deleted_by_user = True
					print('Sauvegarde supprimée.')
				except Exception:
					print('Impossible de supprimer la sauvegarde.')

	# demander les noms des joueurs pour les sauvegarder correctement
	name1 = input('Nom du joueur 1 : ').strip()
	if not name1:
		name1 = 'Joueur1'
	name2 = input('Nom du joueur 2 : ').strip()
	if not name2:
		name2 = 'Joueur2'
	j1 = Joueur(name1)
	j2 = Joueur(name2)
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

	g = Jeu(j1, j2)
	winner = g.demarrer()
	print('Winner:', winner.nom)
	sauvegarde.update_highscores(winner)
	# supprimer la sauvegarde seulement si la partie a été reprise ou si l'utilisateur
	# a explicitement demandé la suppression au départ (voir above).
	if resumed or deleted_by_user:
		try:
			sauvegarde.delete_save()
		except Exception:
			pass
	print(j1.nom + ' grille:')
	print(j1.afficher_grille())
	print(j2.nom + ' grille:')
	print(j2.afficher_grille())


if __name__ == '__main__':
	main()
