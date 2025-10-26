"""
@file algo_jeu.py
@brief Boucle de jeu en mode texte pour la Bataille Navale.

Ce module expose la classe `Jeu` qui orchestre le déroulement d'une partie
entre deux instances de `Joueur`. Il fournit des méthodes pour parser des
coordonnées, démarrer la boucle de jeu tour-par-tour et vérifier la condition
de victoire. Les opérations de sauvegarde automatique sont appelées via
le module `gestion_sauvegarde`.
"""

from typing import Tuple

import random
import class_joueur
import gestion_sauvegarde as sauvegarde


class Jeu:
    """
    @brief Gestionnaire de partie (mode console).

    @param joueur1 Objet Joueur correspondant au premier joueur.
    @param joueur2 Objet Joueur correspondant au second joueur.
    """

    def __init__(self, joueur1, joueur2):
        """
        Constructeur: initialise les références aux joueurs et le compteur de tour.

        @param joueur1: instance de `Joueur` pour le joueur A
        @param joueur2: instance de `Joueur` pour le joueur B
        """
        self.joueur1 = joueur1
        self.joueur2 = joueur2
        self.tour = 0

    def _parse_coord(self, raw: str) -> Tuple[int, int]:
        """
        @brief Convertit une saisie textuelle (ex. 'A1') en coordonnées (ligne, colonne).

        @param raw: chaîne saisie par l'utilisateur
        @return: tuple (row, col) 0-based
        @raises ValueError: si le format est invalide
        """
        raw = raw.strip().upper()
        if len(raw) < 2:
            raise ValueError('Format invalide')
        row = ord(raw[0]) - ord('A')
        col = int(raw[1:]) - 1
        return row, col

    def demarrer(self):
        """
        @brief Démarre la boucle de jeu interactive.

        La boucle alterne les joueurs en fonction de `self.tour`. Après chaque
        action importante, l'état du jeu est sauvegardé via
        `gestion_sauvegarde.save_game` pour permettre une reprise en cas de
        coupure. La méthode retourne l'objet `Joueur` gagnant lorsque la
        condition de victoire est remplie.

        @return: instance du joueur gagnant
        """
        joueurs = [self.joueur1, self.joueur2]
        adversaire = lambda p: self.joueur2 if p is self.joueur1 else self.joueur1

        try:
            sauvegarde.save_game(self)
        except Exception:
            # Ne pas faire échouer le jeu si la sauvegarde initiale échoue
            pass

        while True:
            current = joueurs[self.tour % 2]
            adv = adversaire(current)
            print(f"Tour de {current.nom}")
            print(adv.afficher_grille())
            raw = input('Entrez cible (ex A1) : ')
            try:
                cible = self._parse_coord(raw)
            except Exception as e:
                print(e)
                continue

            result = adv.recevoir_tir(cible)
            if result == 'hit':
                print('Touché!')
                current.score += 1
            elif result == 'miss':
                print('Manqué')
                self.tour += 1
            elif result == 'repeat':
                print('Case déjà ciblée')
            else:
                print('Cible hors grille')

            try:
                sauvegarde.save_game(self)
            except Exception:
                pass

            if self.verifier_gagnant(adv):
                return current

    def verifier_gagnant(self, joueur) -> bool:
        """
        @brief Vérifie si un joueur n'a plus de bateaux ('B') sur sa grille.

        @param joueur: instance de `Joueur` à tester
        @return: True si aucun bateau n'est trouvé, False sinon
        """
        for row in joueur.grille:
            if 'B' in row:
                return False
        return True
