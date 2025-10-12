# class joueur bataille navale

class Joueur:
    def __init__(self, nom):
        self.nom = nom
        self.size = 5
        self.grille = [["." for _ in range(self.size)] for _ in range(self.size)]
        self.bateaux = []
        self.score = 0
        
    def placer_bateau(self, bateau, position, orientation):
        taille = bateau
        r, c = position
        coords = []
        if orientation == 'H':
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

    def tirer(self, cible):
        if isinstance(cible, tuple) and len(cible) == 2:
            return cible
        raise ValueError('cible must be a tuple (r,c)')

    def recevoir_tir(self, cible):
        r, c = cible
        if r < 0 or r >= self.size or c < 0 or c >= self.size:
            return 'invalid'
        cell = self.grille[r][c]
        if cell == 'B':
            self.grille[r][c] = 'X'
            return 'hit'
        if cell in ('X', 'O'):
            return 'repeat'
        self.grille[r][c] = 'O'
        return 'miss'

    def afficher_grille(self):
        header = '  ' + ' '.join(str(i + 1) for i in range(self.size))
        lines = [header]
        for i, row in enumerate(self.grille):
            display = []
            for cell in row:
                if cell == 'B':
                    display.append('.')
                else:
                    display.append(cell)
            lines.append(chr(ord('A') + i) + ' ' + ' '.join(display))
        return '\n'.join(lines)
    