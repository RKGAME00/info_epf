# class joueur bataille navale

class Joueur:
    def __init__(self, nom):
        self.nom = nom  # nom du joueur
        self.size = 5  # taille par défaut de la grille (5x5)
        self.grille = [["." for _ in range(self.size)] for _ in range(self.size)]  # initialisation de la grille vide
        self.bateaux = []  # liste des bateaux (listes de coordonnées)
        self.score = 0  # score du joueur initialisé 
        
    def placer_bateau(self, bateau, position, orientation):
        taille = bateau  # taille du bateau à placer
        r, c = position  # position de départ (r, c)
        coords = []  # liste des coordonnées occupées par le bateau
        if orientation == 'H':
            for i in range(taille):  # pour chaque cellule du bateau en horizontal
                coords.append((r, c + i))  # ajouter la coordonnée correspondante
        else:
            for i in range(taille):  # pour chaque cellule du bateau en vertical
                coords.append((r + i, c))  # ajouter la coordonnée correspondante
        for rr, cc in coords:
            if rr < 0 or rr >= self.size or cc < 0 or cc >= self.size:  # vérification des limites
                return False  # placement invalide (hors grille)
            if self.grille[rr][cc] == 'B':  # vérification qu'il n'y a pas déjà un bateau
                return False  # placement invalide (collision)
        for rr, cc in coords:
            self.grille[rr][cc] = 'B'  # marquer les cases du bateau par 'B'
        self.bateaux.append(coords)  # ajouter le bateau à la liste des bateaux
        return True  # placement réussi

    def tirer(self, cible):
        if isinstance(cible, tuple) and len(cible) == 2:  # s'assurer que la cible est un tuple (r, c)
            return cible  # renvoyer la cible telle quelle
        raise ValueError('cible must be a tuple (r,c)')  # sinon lever une erreur explicite

    def recevoir_tir(self, cible):
        r, c = cible  # déballer la cible
        if r < 0 or r >= self.size or c < 0 or c >= self.size:  # vérification des limites
            return 'invalid'  # cible hors grille
        cell = self.grille[r][c]  # lire le contenu de la cellule visée
        if cell == 'B':
            self.grille[r][c] = 'X'  # marquer touché par 'X'
            return 'hit'  # renvoyer 'hit' si bateau touché
        if cell in ('X', 'O'):
            return 'repeat'  # case déjà ciblée précédemment
        self.grille[r][c] = 'O'  # marquer manqué par 'O'
        return 'miss'  # renvoyer 'miss' si eau touchée

    def afficher_grille(self):
        # en-tête des colonnes numérotées (chaîne)
        header = '    ' + '   '.join(str(i + 1) for i in range(self.size))
        # liste des lignes à afficher (commence par l'en-tête)
        lines = [header]
        lines.append('  ' + ' ---' * self.size + ' ')  # séparateur visuel entre l'en-tête et les lignes
        for i, row in enumerate(self.grille):
            display = []  # représentation affichée pour la ligne (masque les bateaux)
            for cell in row:
                if cell == 'B':
                    display.append('.')  # masquer les bateaux en affichage public
                else:
                    display.append(cell)  # afficher le reste tel quel ('.','X','O')
            # ajouter la ligne avec lettre de ligne et colonnes formatées
            lines.append(chr(ord('A') + i) + ' | ' + ' | '.join(display) + ' | ')
            # séparateur visuel entre les lignes (aligné sur les colonnes)
            lines.append('  ' + ' ---' * self.size + ' ')
        return '\n'.join(lines)  # renvoyer la représentation multi-lignes sous forme de string
