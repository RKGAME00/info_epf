"""
@file class_joueur.py
@brief Définition de la classe `Joueur` utilisée pour la Bataille Navale.

La classe stocke l'état interne (grille, bateaux, score) et fournit des
opérations de base : placement de bateaux, réception d'un tir, affichage
et sérialisation/désérialisation vers/depuis des dictionnaires JSON-compatibles.
"""

from typing import List, Tuple


class Joueur:
    """
    @brief Représente un joueur dans la Bataille Navale.

    Attributs principaux :
    - nom : nom du joueur
    - size : taille de la grille (size x size)
    - grille : représentation 2D des cases (caractères)
    - bateaux : liste des bateaux (liste de coordonnées)
    - score : compteur de touches
    """

    def __init__(self, nom: str, size: int = 5):
        """
        Constructeur.

        @param nom: nom du joueur
        @param size: dimension de la grille (par défaut 5)
        """
        self.nom = nom
        self.size = size
        self.grille: List[List[str]] = [["░" for _ in range(self.size)] for _ in range(self.size)]
        self.bateaux: List[List[Tuple[int, int]]] = []
        self.score: int = 0

    def reset(self):
        """
        @brief Réinitialise la grille, les bateaux et le score.
        """
        self.grille = [["░" for _ in range(self.size)] for _ in range(self.size)]
        self.bateaux = []
        self.score = 0

    def placer_bateau(self, taille: int, position: Tuple[int, int], orientation: str) -> bool:
        """
        @brief Tente de placer un bateau de longueur `taille` à la position donnée.

        @param taille: longueur du bateau
        @param position: tuple (r, c) position de départ (0-based)
        @param orientation: 'H' pour horizontal, autre pour vertical
        @return: True si le placement a réussi, False sinon
        """
        r, c = position
        coords = []
        if orientation.upper() == 'H':
            for i in range(taille):
                coords.append((r, c + i))
        else:
            for i in range(taille):
                coords.append((r + i, c))

        for rr, cc in coords:
            if rr < 0 or rr >= self.size or cc < 0 or cc >= self.size:
                return False
            if self.grille[rr][cc] == 'B':
                return False

        for rr, cc in coords:
            self.grille[rr][cc] = 'B'
        self.bateaux.append(coords)
        return True

    def recevoir_tir(self, cible: Tuple[int, int]) -> str:
        """
        @brief Traite un tir reçu sur la grille.

        @param cible: tuple (r, c)
        @return: 'hit', 'miss', 'repeat' ou 'invalid'
        """
        r, c = cible
        if r < 0 or r >= self.size or c < 0 or c >= self.size:
            return 'invalid'
        cell = self.grille[r][c]
        if cell == 'B':
            self.grille[r][c] = 'X'
            return 'hit'
        if cell in ('X', '⬤'):
            return 'repeat'
        self.grille[r][c] = '⬤'
        return 'miss'

    def afficher_grille(self, reveal: bool = False) -> str:
        """
        @brief Renvoie une représentation textuelle de la grille.

        @param reveal: si True, montre les bateaux ('B'), sinon les masque
        @return: chaîne multi-lignes affichable
        """
        header = '    ' + '   '.join(str(i + 1) for i in range(self.size))
        lines = [header]
        lines.append('  ' + ' ---' * self.size + ' ')
        for i, row in enumerate(self.grille):
            display = []
            for cell in row:
                if cell == 'B' and not reveal:
                    display.append('░')
                else:
                    display.append(cell)
            lines.append(chr(ord('A') + i) + ' | ' + ' | '.join(display) + ' | ')
            lines.append('  ' + ' ---' * self.size + ' ')
        return '\n'.join(lines)

    def to_dict(self) -> dict:
        """
        @brief Sérialise l'état du joueur en dictionnaire JSON-serialisable.

        @return: dict contenant nom, size, grille, bateaux et score
        """
        return {
            'nom': self.nom,
            'size': self.size,
            'grille': self.grille,
            'bateaux': self.bateaux,
            'score': self.score,
        }

    @staticmethod
    def from_dict(data: dict) -> 'Joueur':
        """
        @brief Reconstruit une instance `Joueur` à partir d'un dictionnaire.

        @param data: dict produit par `to_dict`
        @return: instance de `Joueur`
        """
        j = Joueur(data.get('nom', 'Unknown'), data.get('size', 5))
        j.grille = data.get('grille', j.grille)
        j.bateaux = data.get('bateaux', [])
        j.score = int(data.get('score', 0))
        return j

