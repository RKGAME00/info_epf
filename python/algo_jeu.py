import class_joueur  # import du module contenant la classe Joueur (utilisé pour type/interop)
import random  # import du module random si nécessaire pour des choix aléatoires


# fonction principale du jeu

class jeu:
    def __init__(self, joueur1, joueur2):
        self.joueur1 = joueur1  # joueur 1 de la partie
        self.joueur2 = joueur2  # joueur 2 de la partie
        self.tour = 0  # compteur de tours, 0 = joueur1 commence

    def demarrer(self):
        joueurs = [self.joueur1, self.joueur2]  # liste ordonnée des joueurs
        adversaires = {self.joueur1: self.joueur2, self.joueur2: self.joueur1}  # mapping pour accéder à l'adversaire
        current = joueurs[self.tour % 2]  # joueur courant selon le tour
        while True:
            adv = adversaires[current]  # adversaire du joueur courant
            print(f"Tour de {current.nom}")  # affichage du joueur courant
            print(adv.afficher_grille())  # afficher la grille de l'adversaire (masquée des bateaux)
            raw = input('Entrez cible (ex A1) : ')  # saisie de la cible par l'utilisateur
            if len(raw) < 2:
                print('Format invalide')  # contrôle minimal de la saisie
                continue
            row = ord(raw[0].upper()) - ord('A')  # conversion de la lettre en indice de ligne
            try:
                col = int(raw[1:]) - 1  # conversion des chiffres en indice de colonne (1-based -> 0-based)
            except ValueError:
                print('Format invalide')  # gestion d'une mauvaise saisie numérique
                continue
            cible = (row, col)  # cible sous forme de tuple (r, c)
            result = adv.recevoir_tir(cible)  # envoyer le tir à l'adversaire et récupérer le résultat
            match result:  # branchement selon le résultat du tir (Python 3.10+)
                case 'hit':
                    print('Touché!')  # affichage en cas de touché
                    current.score += 1  # incrémenter le score du joueur courant
                case 'miss':
                    print('Manqué')  # affichage en cas de manqué
                    self.tour += 1  # passer au tour suivant
                    current = joueurs[self.tour % 2]  # changer le joueur courant
                case 'repeat':
                    print('Case déjà ciblée')  # case déjà visée précédemment
                case 'invalid':
                    print('Cible hors grille')  # cible hors des limites de la grille
            if self.verifier_gagnant(adv):  # vérifier si l'adversaire n'a plus de bateaux
                return current  # retourner le gagnant (joueur courant)

    def verifier_gagnant(self, joueur):
        for row in joueur.grille:  # pour chaque ligne de la grille du joueur
            if 'B' in row:  # s'il reste un bateau (marqué 'B')
                return False  # pas encore gagnant
        return True  # aucun bateau restant -> gagnant
