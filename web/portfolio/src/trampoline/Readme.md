# Projet : Programmation Temps Réel avec la Jetson Nano

## Objectif
L’objectif de ce TP est de nous initier à la programmation de systèmes en temps réel sous Linux, en particulier avec la carte **Jetson Nano**. Ce travail pratique est conçu pour nous apprendre :
- La manipulation du temps en programmation embarquée.
- La programmation multitâche et parallèle avec les threads POSIX (`pthread`).
- Les techniques de synchronisation dans un environnement embarqué.

## Ce que l’on apprend
Ce projet nous permet d’acquérir des compétences essentielles en gestion du temps dans les systèmes embarqués, en particulier :
- La maîtrise des structures `struct timespec` pour manipuler le temps avec précision (en microsecondes).
- L’implémentation d’opérations telles que l’addition et la soustraction de temps.
- La création et la gestion de threads pour organiser des tâches en parallèle.
- L’utilisation des mutex pour garantir la synchronisation entre les threads.

Ces compétences sont fondamentales dans des applications critiques, comme la robotique ou l’IoT, où la précision temporelle est primordiale.

## Ce qu'on doit faire

### 1. Implémentation des Fonctions Temps
Nous développons une bibliothèque contenant des fonctions pour :
- **Conversion** : Convertir une structure `timespec` en microsecondes (`timespec_to_micros`).
- **Addition** : Additionner deux structures `timespec` (`timespec_add`).
- **Soustraction** : Soustraire une structure `timespec` d’une autre (`timespec_sub`).

Ces fonctions sont implémentées dans les fichiers suivants :
- `src/utimes.c`
- `include/utimes.h`

### 2. Création de Threads
À partir des fonctions développées, nous utilisons les threads pour :
- Surveiller en temps réel la distance mesurée par un capteur ultra-son.
- Déclencher des alertes visuelles via des LED en fonction des distances critiques.

### 3. Architecture du Système Temps Réel
Nous concevons une architecture fonctionnelle qui :
- Décrit les threads nécessaires pour la gestion des capteurs et des LED.
- Spécifie les communications et interactions entre les threads.

### 4. Tests et Validation
#### Tests de la bibliothèque
Les premières vérifications sont effectuées à l’aide de **Unity**, une bibliothèque de tests unitaires pour le langage C. Ces tests vérifient les fonctions de la bibliothèque `utimes` :
- La conversion d’une structure `timespec` en microsecondes.
- L’addition et la soustraction de deux structures `timespec`.

#### Tests réalisés pendant la séance de TP
Pendant la séance de TP, nous testons le fonctionnement de la bibliothèque et des threads en conditions réelles. Les tests incluent :
- **Génération d’un signal carré** : Vérification que le signal généré correspond bien aux spécifications.
- **Synchronisation entre les threads `trigger` et `echo`** : Test des interactions pour garantir une mesure précise de la distance.

#### Tests sur Raspberry Pi
Enfin, la bibliothèque et le système sont portés sur un **Raspberry Pi**. Tous les tests réalisés sur la Jetson Nano sont répétés, à une exception près : la bibliothèque utilisée pour gérer les GPIO est spécifique au Raspberry Pi. En dehors de ce détail, l’ensemble des tests reste identique.

---

## Vidéo Démonstrative
Voici une vidéo illustrant le fonctionnement du projet :

![Vidéo de démonstration](vid/demo.mp4)

---

## Documentation
Pour plus de détails sur l'implémentation et l'utilisation des fonctions et threads, veuillez consulter la [documentation complète](https://trampoline-e227201h-9090d2741921cb93ae4742584cf8ff5658d31b0f518.univ-nantes.io).

