"""
@file gestion_sauvegarde.py
@brief Gestion des sauvegardes de partie et des meilleurs scores.

Ce module fournit des fonctions pour sauvegarder/charger l'état d'une
partie (JSON), supprimer la sauvegarde, et gérer un fichier de meilleurs
scores (highscores). Les fonctions exposées sont conçues pour être
robustes face aux erreurs d'E/S.
"""

import json
import os
from typing import Optional, Dict, Any

from class_joueur import Joueur


SAVE_FILE = os.path.join(os.path.dirname(__file__), 'save_game.json')
HIGHSCORES_FILE = os.path.join(os.path.dirname(__file__), 'highscores.json')


def save_game(jeu: Any, path: str = SAVE_FILE) -> None:
    """
    @brief Sauvegarde l'état du jeu (tour et joueurs) en JSON.

    @param jeu: objet représentant la partie (doit avoir attributs joueur1, joueur2, tour)
    @param path: chemin du fichier de sauvegarde
    """
    data = {
        'tour': getattr(jeu, 'tour', 0),
        'joueur1': jeu.joueur1.to_dict(),
        'joueur2': jeu.joueur2.to_dict(),
    }
    try:
        with open(path, 'w', encoding='utf-8') as f:
            json.dump(data, f, ensure_ascii=False, indent=2)
    except Exception as e:
        print(f"Erreur lors de la sauvegarde de la partie: {e}")


def load_game(path: str = SAVE_FILE) -> Optional[Dict[str, Any]]:
    """
    @brief Charge la sauvegarde et reconstruit les objets `Joueur`.

    @param path: chemin du fichier de sauvegarde
    @return: dict {'tour': int, 'joueur1': Joueur, 'joueur2': Joueur} ou None
    """
    if not os.path.exists(path):
        return None
    try:
        with open(path, 'r', encoding='utf-8') as f:
            data = json.load(f)
        j1 = Joueur.from_dict(data.get('joueur1', {}))
        j2 = Joueur.from_dict(data.get('joueur2', {}))
        return {'tour': data.get('tour', 0), 'joueur1': j1, 'joueur2': j2}
    except Exception as e:
        print(f"Erreur lors du chargement de la sauvegarde: {e}")
        return None


def delete_save(path: str = SAVE_FILE) -> None:
    """
    @brief Supprime le fichier de sauvegarde si présent.
    """
    try:
        if os.path.exists(path):
            os.remove(path)
    except Exception as e:
        print(f"Impossible de supprimer la sauvegarde: {e}")


def load_highscores(path: str = HIGHSCORES_FILE) -> Dict[str, int]:
    """
    @brief Charge les meilleurs scores depuis un fichier JSON.

    @return: dictionnaire {nom: score}
    """
    if not os.path.exists(path):
        return {}
    try:
        with open(path, 'r', encoding='utf-8') as f:
            data = json.load(f)
            if isinstance(data, dict):
                return {k: int(v) for k, v in data.items()}
    except Exception as e:
        print(f"Erreur lors du chargement des highscores: {e}")
    return {}


def save_highscores(scores: Dict[str, int], path: str = HIGHSCORES_FILE) -> None:
    """
    @brief Écrit le dictionnaire de highscores sur disque.
    """
    try:
        with open(path, 'w', encoding='utf-8') as f:
            json.dump(scores, f, ensure_ascii=False, indent=2)
    except Exception as e:
        print(f"Erreur lors de la sauvegarde des highscores: {e}")


def update_highscores(joueur: Joueur, path: str = HIGHSCORES_FILE) -> Dict[str, int]:
    """
    @brief Met à jour les highscores avec le score du joueur s'il est supérieur
    au précédent.

    @param joueur: instance de `Joueur` dont le score doit être pris en compte
    @return: dictionnaire des scores mis à jour
    """
    scores = load_highscores(path)
    name = joueur.nom
    score = int(joueur.score)
    prev = scores.get(name, -1)
    if score > prev:
        scores[name] = score
        save_highscores(scores, path)
    return scores

