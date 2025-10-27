# Projet : Bataille Navale (TP)

Ce dépôt contient une implémentation simple en Python d'un jeu de *Bataille Navale* en mode texte,
avec un système de sauvegarde/reprise de partie et un suivi des meilleurs scores (highscores).

## Objectifs fonctionnels

- Permettre à deux joueurs de jouer en local, tour par tour.
- Sauvegarder automatiquement l'état de la partie afin de pouvoir la reprendre après une coupure.
- Conserver un classement des meilleurs scores (meilleur nombre de touches par joueur).

## Outils et méthodes utilisés

Pendant le développement de ce TP, j'ai utilisé les outils et méthodes suivants :

- Visual Studio Code (VS Code) : éditeur principal pour l'écriture du code, navigation dans
  l'arborescence du projet, exécution rapide de scripts et intégration avec le terminal.
- Python 3.x : langage d'implémentation. Modules standards utilisés : `json`, `os`, `random`, `typing`.
- PowerShell (Windows) / terminal : pour lancer les scripts (`python .\tp.py`, `python .\test.py`) et
  pour inspecter les fichiers JSON (`Get-Content`, `Remove-Item`).
- Git : gestion de versions (commits, branches). Pratique recommandée pour suivre les évolutions.
- Fichiers JSON : format choisi pour la sauvegarde (`save_game.json`) et les `highscores.json` — lisible
  et facilement éditable.

L'utilisation de l'IA pour ce projet
- Pour la manipulation de tableaux (grilles) en Python :
  - Les listes imbriquées suffisent pour une grille 2D simple ; utilisez des comprehensions pour
    l'initialisation (`[["░" for _ in range(size)] for _ in range(size)]`).
  - L'IA peut suggérer des fonctions utilitaires (extraction de voisins, validation de positions) utiles
    pour des règles de placement plus strictes.
- Pour la sauvegarde JSON :
  - Demandez à l'IA de générer des fonctions `to_dict()` / `from_dict()` pour chaque classe importante.
  - Toujours sérialiser des types JSON-compatibles (listes, dicts, nombres, chaînes). Convertissez
    les tuples en listes si nécessaire.
- Tests : demandez à l'IA d'écrire des petits tests `pytest` pour valider `placer_bateau`, `recevoir_tir`,
  et les fonctions de gestion de sauvegarde.

Remarque finale
L'IA est un accélérateur : elle me propose des motifs et résout des problèmes de routine rapidement.
Toujours relire, tester et comprendre le code produit. Pour ce projet, l'IA a été utilisée pour
m'aider à sortir de la logique c++, en utilisant des fonctions avec des entrées plus permisive.


## Arborescence (fichiers principaux)

- `class_joueur.py` : définition de la classe `Joueur` (grille, placement de bateaux, réception d'un tir, sérialisation).
- `algo_jeu.py` : définition de la classe `Jeu` qui gère la boucle de jeu tour-par-tour et appelle l'autosave.
- `gestion_sauvegarde.py` : fonctions pour sauvegarder/recharger la partie (`save_game`, `load_game`, `delete_save`) et pour gérer les `highscores`.
- `tp.py` : point d'entrée CLI. Propose de reprendre une sauvegarde ou de lancer une nouvelle partie.
- `save_game.json` : (généré) fichier de sauvegarde de la partie.
- `highscores.json` : (généré) dictionnaire des meilleurs scores {nom: score}.

> Tous les fichiers `.py` sont dans le dossier racine `python/`.

## Comment exécuter

Exemples depuis PowerShell dans le dossier `python` :


Lancer le jeu interactif :

```powershell
python .\tp.py
```

ou via les fonctionalitées de vscode

Comportement au lancement de `tp.py` :
- Si `save_game.json` existe, on vous propose de reprendre la partie.
- Sinon, vous placez les bateaux pour chaque joueur (automatique ou manuel) puis la partie démarre.
- La sauvegarde est mise à jour automatiquement après chaque action importante (tir, changement de tour).
- À la fin d'une partie gagnée, le fichier `highscores.json` est mis à jour si le score du gagnant est supérieur à son précédent.

Fichiers de données (générés) :
- `save_game.json` : état complet de la partie (tour, joueurs, grilles, bateaux, scores).
- `highscores.json` : dictionnaire JSON `{ "NomJoueur": score, ... }`.

## Description technique

- `Joueur`
  - Attributs : `nom`, `size`, `grille`, `bateaux`, `score`.
  - Méthodes : `placer_bateau(taille, (r,c), orientation)`, `recevoir_tir((r,c))`, `afficher_grille(reveal=False)`, `to_dict()`, `from_dict()`.

- `Jeu`
  - Gère la boucle de jeu interactive.
  - Méthode `_parse_coord` pour convertir une saisie `A1` -> `(0,0)`.
  - `demarrer()` : boucle principale, alternance des joueurs, traitement des résultats `hit/miss/repeat/invalid`.
  - Appelle `gestion_sauvegarde.save_game(self)` pour l'autosave.

- `gestion_sauvegarde`
  - `save_game(jeu)` : serialise l'état de `jeu` (tour, joueur1.to_dict(), joueur2.to_dict()) en JSON.
  - `load_game()` : lit le JSON et reconstruit des instances `Joueur` via `Joueur.from_dict()`.
  - `delete_save()` : supprime le fichier de sauvegarde.
  - `load_highscores()`, `save_highscores()`, `update_highscores(joueur)` : gestion simple du fichier de meilleurs scores.

## Démarche et étapes réalisées

1. Repérage des grands axes du TP :
   - Gestion des joueurs et de leurs grilles,
   - Boucle tour-par-tour pour l'échange des tirs,
   - Sauvegarde/reprise de la partie,
   - Mécanisme de meilleurs scores.

2. Conception sur papier : j'ai décrit la classe `Joueur` (API attendue), imaginé la boucle de jeu et le format de sauvegarde (JSON simple). Cela aide à découper l'implémentation en tâches.

3. Implémentation itérative :
   - Création de la classe `Joueur` (méthodes de base, sérialisation).
   - Écriture du module de sauvegarde (`gestion_sauvegarde`) pour garantir la compatibilité JSON et la reconstruction des objets.
   - Implémentation de `Jeu` (tour par tour) en appelant l'autosave après les actions importantes.
   - Écriture de `tp.py` comme point d'entrée pour lancer ou reprendre une partie.
   - Tests progressifs avec `test.py` pour valider la sauvegarde/rechargement.

4. Tests : j'ai exécuté `test.py` pour vérifier que la sauvegarde s'écrit et peut être relue sans erreur. J'ai aussi testé des sessions interactives manuelles.

## Choix de conception / remarques

- Format de sauvegarde : JSON simple, lisible et suffisant pour ce TP. Avantage : facilité d'inspection et d'édition manuelle. Limite : pas de versioning explicite actuellement (on pourrait ajouter une clé `version` au JSON).

- Sérialisation des bateaux : stockés en tant que listes de coordonnées (tuples convertis en listes via JSON). `Joueur.from_dict` reconstruit correctement l'état.

- Robustesse : les appels à la sauvegarde sont entourés de try/except pour éviter qu'une erreur d'E/S rompe la partie.

## Tests automatisés et qualité

- Pour l'instant il n'y a pas de suite de tests unitaires formelle (pytest/unittest). Le fichier `test.py` sert de test d'intégration rapide.

- Si vous souhaitez, je peux ajouter :
  - Tests unitaires pour `Joueur` et `gestion_sauvegarde` (pytest),
  - Validation du format JSON (schéma),
  - Nettoyage automatique de la sauvegarde à la fin d'une partie gagnée.



## Logique complète du jeu

Cette section explique en détail la logique, les règles et la manière dont le code implémente la Bataille Navale.

1) Initialisation
- Le programme démarre dans `tp.py` qui vérifie s'il existe une sauvegarde (`save_game.json`).
- Si une sauvegarde existe, l'utilisateur peut choisir de la reprendre ou non.
  - S'il reprend : les objets `Joueur` sont rechargés depuis le JSON et la boucle de jeu reprend à l'état sauvegardé.
  - S'il refuse : on propose de supprimer la sauvegarde, sinon on commence une nouvelle partie.
- Si aucune sauvegarde, on demande les noms des joueurs et on procède au placement des bateaux pour chaque joueur.

2) Placement des bateaux
- Pour chaque joueur, on place une liste de bateaux (par défaut tailles `[3, 2]`).
- Placement automatique : le programme choisit une orientation (H/V) et une position valide aléatoirement.
- Placement manuel : l'utilisateur saisit la coordonnée de départ (ex. `A1`) et l'orientation `H` ou `V`.
- Le placement vérifie les collisions et les limites de grille ; en cas d'échec, on redemande.

3) Boucle tour-par-tour
- La classe `Jeu` gère l'alternance des tours via un attribut `tour` (int). Le joueur courant est `joueurs[self.tour % 2]`.
- À son tour, le joueur saisit une cible (ex. `B2`) qui est convertie en coordonnées 0-based.
- L'adversaire traite le tir via `recevoir_tir((r,c))` et renvoie :
  - `'hit'` si un bateau est touché (la case passe de 'B' à 'X'),
  - `'miss'` si l'eau est touchée (la case passe à '⬤'),
  - `'repeat'` si la case a déjà été ciblée,
  - `'invalid'` si la coordonnée est hors-grille.
- En cas de `hit`, le score du tireur est incrémenté; en cas de `miss`, on incrémente `tour` pour passer au joueur suivant.

4) Vérification de victoire
- Après chaque action, on vérifie si l'adversaire a encore des cellules `'B'` dans sa grille.
- Si l'adversaire n'a plus de `'B'`, la partie est terminée et le tireur courant est déclaré gagnant.

5) Sauvegarde (autosave)
- Après chaque action importante (début de partie, après un tir, après changement de tour), le jeu appelle `gestion_sauvegarde.save_game(self)`.
- `save_game` sérialise l'état minimal nécessaire : `tour`, `joueur1.to_dict()` et `joueur2.to_dict()` et écrit tout ça en JSON dans `save_game.json`.
- Si une erreur d'E/S survient, l'appel est encapsulé dans un `try/except` pour que la partie puisse continuer sans plantage.

6) Fin de partie et highscore
- Lorsque la partie est terminée, on met à jour les `highscores` via `gestion_sauvegarde.update_highscores(winner)`.
- Principe du highscore :
  - Le `highscore` est un dictionnaire `{nom: score}` stocké dans `highscores.json`.
  - `score` correspond au nombre de touches (`hit`) réalisées pendant la partie par ce joueur.
  - `update_highscores` lit le fichier, compare le score courant au score stocké pour le même nom, et remplace si le score courant est supérieur.
  - Ainsi, le highscore conserve la meilleure performance enregistrée par joueur (score maximum atteint).
- Après la mise à jour du highscore, le programme supprime la sauvegarde si la partie était une reprise, ou si l'utilisateur l'avait explicitement demandée avant de commencer (pour éviter de proposer indéfiniment une ancienne partie).

## Définitions et signification du score

- `score` : pour chaque joueur, c'est le nombre total de cases ennemies marquées `'X'` (touchées) pendant la partie.
- Un score élevé signifie que le joueur a touché plus de cases adverses au cours de la partie.
- Le `highscore` stocke le score maximal atteint par un joueur (identifié par son nom) sur toutes les parties jouées sur la machine.

## Exemples de cas d'utilisation / scénarios

- Scénario 1 : coupure pendant la partie
  - Le jeu a appelé `save_game.json` après le dernier tir. Vous relancez `tp.py` : il détecte la sauvegarde et propose de reprendre. Si vous acceptez, la partie reprend exactement là où elle s'était arrêtée.

- Scénario 2 : vous trouvez la sauvegarde mais vous ne voulez pas la reprendre
  - Vous répondez `n` à la reprise et `o` à la suppression : le fichier est supprimé immédiatement et vous commencez une nouvelle partie.

  ## Améliorations possibles (futures)

- Ajouter une interface graphique légère (Tkinter ou web).
- Ajouter des tests unitaires automatisés et CI (GitHub Actions).
- Ajouter versioning du format de sauvegarde et migration entre versions.
- Ajouter un mode réseau (TCP) pour jouer à distance.
- Empêcher le placement de bateaux adjacents (règles plus strictes) et ajouter détection d'erreur d'entrée plus complète.

## Contact / crédits

Ce README explique la logique du jeu et le fonctionnement interne de l'implémentation fournie.

Si vous voulez que j'ajoute :
- des tests unitaires,
- une page de documentation automatique (Sphinx) générée depuis les docstrings,
- ou une simulation automatisée pour valider la suppression de la sauvegarde,

faites-le moi savoir.
