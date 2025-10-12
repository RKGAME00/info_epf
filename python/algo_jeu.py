import class_joueur
import random

class jeu:
    def __init__(self, joueur1, joueur2):
        self.joueur1 = joueur1
        self.joueur2 = joueur2
        self.tour = 0

    def demarrer(self):
        joueurs = [self.joueur1, self.joueur2]
        adversaires = {self.joueur1: self.joueur2, self.joueur2: self.joueur1}
        current = joueurs[self.tour % 2]
        while True:
            adv = adversaires[current]
            print(f"Tour de {current.nom}")
            print(adv.afficher_grille())
            raw = input('Entrez cible (ex A1) : ')
            if len(raw) < 2:
                print('Format invalide')
                continue
            row = ord(raw[0].upper()) - ord('A')
            try:
                col = int(raw[1:]) - 1
            except ValueError:
                print('Format invalide')
                continue
            cible = (row, col)
            result = adv.recevoir_tir(cible)
            match result:
                case 'hit':
                    print('Touché!')
                    current.score += 1
                case 'miss':
                    print('Manqué')
                    self.tour += 1
                    current = joueurs[self.tour % 2]
                case 'repeat':
                    print('Case déjà ciblée')
                case 'invalid':
                    print('Cible hors grille')
            if self.verifier_gagnant(adv):
                return current

    def verifier_gagnant(self, joueur):
        for row in joueur.grille:
            if 'B' in row:
                return False
        return True
