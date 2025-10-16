import java.util.Vector;
import java.util.Random;

class magasin {
    EquipementsTerrain equipementTerrain;
    EquipementsJoueurs equipementJoueurs;
    EquipementsProtectionJoueurs equipementProtectionJoueurs;

    private Vector<EquipementsTerrain> listeEquipementsTerrain = new Vector<>();
    private Vector<EquipementsJoueurs> listeEquipementsJoueurs = new Vector<>();
    private Vector<EquipementsProtectionJoueurs> listeEquipementsProtectionJoueurs = new Vector<>();

    private final FileManager fm = new FileManager("magasin_bdd");

    public magasin() {
        // Charger les données existantes via FileManager
        listeEquipementsTerrain.addAll(fm.loadTerrains());
        listeEquipementsJoueurs.addAll(fm.loadJoueurs());
        listeEquipementsProtectionJoueurs.addAll(fm.loadProtections());
    }

    // ==================================
    // * Equipements Terrain
    // ==================================

    public void addEquipementTerrain(EquipementsTerrain e) {
        if (e == null)
            return;
        for (EquipementsTerrain et : listeEquipementsTerrain) {
            if (et.reference != null && et.reference.equals(e.reference)) {
                System.out.println("Un équipement de terrain avec la référence " + e.reference + " existe déjà.");
                return;
            }
        }
        listeEquipementsTerrain.add(e);
        saveTerrains();
    }

    public Vector<EquipementsTerrain> getListeEquipementsTerrain() {
        return listeEquipementsTerrain;
    }

    public void showEquipementsTerrain() {
        for (EquipementsTerrain et : listeEquipementsTerrain) {
            System.out.println("Référence: " + et.reference + ", Sport: " + et.sport + ", Désignation: "
                    + et.designation
                    + ", Prix: " + et.prix + ", Nombre: " + et.nombre + ", Hauteur: " + et.hauteur + ", Largeur: "
                    + et.largeur + ", Poids: " + et.poids);
        }
    }

    // ==================================
    // * Equipements Joueurs
    // ==================================

    public void addEquipementJoueurs(EquipementsJoueurs e) {
        if (e == null)
            return;
        for (EquipementsJoueurs ej : listeEquipementsJoueurs) {
            if (ej.reference != null && ej.reference.equals(e.reference)) {
                System.out.println("Un équipement de joueur avec la référence " + e.reference + " existe déjà.");
                return;
            }
        }
        listeEquipementsJoueurs.add(e);
        saveJoueurs();
    }

    public Vector<EquipementsJoueurs> getListeEquipementsJoueurs() {
        return listeEquipementsJoueurs;
    }

    public void showEquipementsJoueurs() {
        for (EquipementsJoueurs ej : listeEquipementsJoueurs) {
            System.out
                    .println("Référence: " + ej.reference + ", Sport: " + ej.sport + ", Désignation: " + ej.designation
                            + ", Prix: " + ej.prix + ", Nombre: " + ej.nombre + ", Taille: " + ej.taille + ", Couleur: "
                            + ej.couleur);
        }
    }

    // ==================================
    // * Equipements Protection Joueurs
    // ==================================

    public void addEquipementProtectionJoueurs(EquipementsProtectionJoueurs e) {
        if (e == null)
            return;
        for (EquipementsProtectionJoueurs ep : listeEquipementsProtectionJoueurs) {
            if (ep.reference != null && ep.reference.equals(e.reference)) {
                System.out.println("Un équipement de protection avec la référence " + e.reference + " existe déjà.");
                return;
            }
        }
        listeEquipementsProtectionJoueurs.add(e);
        saveProtections();
    }

    public Vector<EquipementsProtectionJoueurs> getListeEquipementsProtectionJoueurs() {
        return listeEquipementsProtectionJoueurs;
    }

    public void showEquipementsProtectionJoueurs() {
        for (EquipementsProtectionJoueurs ep : listeEquipementsProtectionJoueurs) {
            System.out
                    .println("Référence: " + ep.reference + ", Sport: " + ep.sport + ", Désignation: " + ep.designation
                            + ", Prix: " + ep.prix + ", Nombre: " + ep.nombre + ", Taille: " + ep.taille + ", Couleur: "
                            + ep.couleur + ", Niveau: " + ep.niveau);
        }
    }

    // ==================================
    // * Persistance via FileManager (wrappers)
    // ==================================

    private void saveTerrains() {
        fm.saveTerrains(listeEquipementsTerrain);
    }

    private void saveJoueurs() {
        fm.saveJoueurs(listeEquipementsJoueurs);
    }

    private void saveProtections() {
        fm.saveProtections(listeEquipementsProtectionJoueurs);
    }

    // ==================================
    // * gestion global des équipements
    // ==================================

    public void changerReference(String ancienneReference, String nouvelleReference) {
        // Vérifier si la nouvelle référence existe déjà
        for (EquipementsTerrain et : listeEquipementsTerrain) {
            if (et.reference != null && et.reference.equals(nouvelleReference)) {
                System.out.println("La nouvelle référence existe déjà pour un équipement de terrain.");
                return;
            }
        }
        for (EquipementsJoueurs ej : listeEquipementsJoueurs) {
            if (ej.reference != null && ej.reference.equals(nouvelleReference)) {
                System.out.println("La nouvelle référence existe déjà pour un équipement de joueur.");
                return;
            }
        }
        for (EquipementsProtectionJoueurs ep : listeEquipementsProtectionJoueurs) {
            if (ep.reference != null && ep.reference.equals(nouvelleReference)) {
                System.out.println("La nouvelle référence existe déjà pour un équipement de protection.");
                return;
            }
        }

        // Si la nouvelle référence n'existe pas, procéder au changement
        for (EquipementsTerrain et : listeEquipementsTerrain) {
            if (et.reference != null && et.reference.equals(ancienneReference)) {
                et.reference = nouvelleReference;
                saveTerrains();
                System.out.println(
                        "Référence mise à jour pour l'équipement de terrain: " + ancienneReference + " -> "
                                + nouvelleReference);
                return;
            }
        }
        for (EquipementsJoueurs ej : listeEquipementsJoueurs) {
            if (ej.reference != null && ej.reference.equals(ancienneReference)) {
                ej.reference = nouvelleReference;
                saveJoueurs();
                System.out.println("Référence mise à jour pour l'équipement de joueur: " + ancienneReference + " -> "
                        + nouvelleReference);
                return;
            }
        }
        for (EquipementsProtectionJoueurs ep : listeEquipementsProtectionJoueurs) {
            if (ep.reference != null && ep.reference.equals(ancienneReference)) {
                ep.reference = nouvelleReference;
                saveProtections();
                System.out.println("Référence mise à jour pour l'équipement de protection: " + ancienneReference
                        + " -> " + nouvelleReference);
                return;
            }
        }
        System.out.println("Équipement avec la référence " + ancienneReference + " non trouvé.");
    }

    public void changerSport(String reference, String nouveauSport) {
        for (EquipementsTerrain et : listeEquipementsTerrain) {
            if (et.reference != null && et.reference.equals(reference)) {
                et.sport = nouveauSport;
                saveTerrains();
                System.out.println("Sport mis à jour pour l'équipement de terrain avec la référence " + reference);
                return;
            }
        }
        for (EquipementsJoueurs ej : listeEquipementsJoueurs) {
            if (ej.reference != null && ej.reference.equals(reference)) {
                ej.sport = nouveauSport;
                saveJoueurs();
                System.out.println("Sport mis à jour pour l'équipement de joueur avec la référence " + reference);
                return;
            }
        }
        for (EquipementsProtectionJoueurs ep : listeEquipementsProtectionJoueurs) {
            if (ep.reference != null && ep.reference.equals(reference)) {
                ep.sport = nouveauSport;
                saveProtections();
                System.out
                        .println("Sport mis à jour pour l'équipement de protection avec la référence " + reference);
                return;
            }
        }
        System.out.println("Équipement avec la référence " + reference + " non trouvé.");
    }

    public void changerDesignation(String reference, String nouvelleDesignation) {
        for (EquipementsTerrain et : listeEquipementsTerrain) {
            if (et.reference != null && et.reference.equals(reference)) {
                et.designation = nouvelleDesignation;
                saveTerrains();
                System.out.println(
                        "Désignation mise à jour pour l'équipement de terrain avec la référence " + reference);
                return;
            }
        }
        for (EquipementsJoueurs ej : listeEquipementsJoueurs) {
            if (ej.reference != null && ej.reference.equals(reference)) {
                ej.designation = nouvelleDesignation;
                saveJoueurs();
                System.out
                        .println("Désignation mise à jour pour l'équipement de joueur avec la référence " + reference);
                return;
            }
        }
        for (EquipementsProtectionJoueurs ep : listeEquipementsProtectionJoueurs) {
            if (ep.reference != null && ep.reference.equals(reference)) {
                ep.designation = nouvelleDesignation;
                saveProtections();
                System.out.println(
                        "Désignation mise à jour pour l'équipement de protection avec la référence " + reference);
                return;
            }
        }
        System.out.println("Équipement avec la référence " + reference + " non trouvé.");
    }

    public void changerPrix(String reference, int nouveauPrix) {
        for (EquipementsTerrain et : listeEquipementsTerrain) {
            if (et.reference != null && et.reference.equals(reference)) {
                et.prix = nouveauPrix;
                saveTerrains();
                System.out.println("Prix mis à jour pour l'équipement de terrain avec la référence " + reference);
                return;
            }
        }
        for (EquipementsJoueurs ej : listeEquipementsJoueurs) {
            if (ej.reference != null && ej.reference.equals(reference)) {
                ej.prix = nouveauPrix;
                saveJoueurs();
                System.out.println("Prix mis à jour pour l'équipement de joueur avec la référence " + reference);
                return;
            }
        }
        for (EquipementsProtectionJoueurs ep : listeEquipementsProtectionJoueurs) {
            if (ep.reference != null && ep.reference.equals(reference)) {
                ep.prix = nouveauPrix;
                saveProtections();
                System.out.println("Prix mis à jour pour l'équipement de protection avec la référence " + reference);
                return;
            }
        }
        System.out.println("Équipement avec la référence " + reference + " non trouvé.");
    }

    public void changerQuantite(String reference, int nouvelleQuantite) {
        for (EquipementsTerrain et : listeEquipementsTerrain) {
            if (et.reference != null && et.reference.equals(reference)) {
                et.nombre = nouvelleQuantite;
                saveTerrains();
                System.out.println("Quantité mise à jour pour l'équipement de terrain avec la référence " + reference);
                return;
            }
        }
        for (EquipementsJoueurs ej : listeEquipementsJoueurs) {
            if (ej.reference != null && ej.reference.equals(reference)) {
                ej.nombre = nouvelleQuantite;
                saveJoueurs();
                System.out.println("Quantité mise à jour pour l'équipement de joueur avec la référence " + reference);
                return;
            }
        }
        for (EquipementsProtectionJoueurs ep : listeEquipementsProtectionJoueurs) {
            if (ep.reference != null && ep.reference.equals(reference)) {
                ep.nombre = nouvelleQuantite;
                saveProtections();
                System.out
                        .println("Quantité mise à jour pour l'équipement de protection avec la référence " + reference);
                return;
            }
        }
        System.out.println("Équipement avec la référence " + reference + " non trouvé.");
    }

    public void incrementerStock(String reference, int quantite) {
        for (EquipementsTerrain et : listeEquipementsTerrain) {
            if (et.reference != null && et.reference.equals(reference)) {
                et.nombre += quantite;
                saveTerrains();
                System.out.println("Stock mis à jour pour l'équipement de terrain avec la référence " + reference
                        + ". nouveau nombre: " + et.nombre);
                return;
            }
        }
        for (EquipementsJoueurs ej : listeEquipementsJoueurs) {
            if (ej.reference != null && ej.reference.equals(reference)) {
                ej.nombre += quantite;
                saveJoueurs();
                System.out.println("Stock mis à jour pour l'équipement de joueur avec la référence " + reference
                        + ". nouveau nombre: " + ej.nombre);
                return;
            }
        }
        for (EquipementsProtectionJoueurs ep : listeEquipementsProtectionJoueurs) {
            if (ep.reference != null && ep.reference.equals(reference)) {
                ep.nombre += quantite;
                saveProtections();
                System.out.println("Stock mis à jour pour l'équipement de protection avec la référence " + reference
                        + ". nouveau nombre: " + ep.nombre);
                return;
            }
        }
        System.out.println("Équipement avec la référence " + reference + " non trouvé.");
    }

    public void decrementerStock(String reference, int quantite) {
        for (EquipementsTerrain et : listeEquipementsTerrain) {
            if (et.reference != null && et.reference.equals(reference)) {
                if (et.nombre >= quantite) {
                    et.nombre -= quantite;
                    saveTerrains();
                    System.out.println("Stock mis à jour pour l'équipement de terrain avec la référence " + reference
                            + ". nouveau nombre: " + et.nombre);
                } else {
                    System.out.println("Stock insuffisant pour l'équipement de terrain avec la référence " + reference);
                }
                return;
            }
        }
        for (EquipementsJoueurs ej : listeEquipementsJoueurs) {
            if (ej.reference != null && ej.reference.equals(reference)) {
                if (ej.nombre >= quantite) {
                    ej.nombre -= quantite;
                    saveJoueurs();
                    System.out.println("Stock mis à jour pour l'équipement de joueur avec la référence " + reference
                            + ". nouveau nombre: " + ej.nombre);
                } else {
                    System.out.println("Stock insuffisant pour l'équipement de joueur avec la référence " + reference);
                }
                return;
            }
        }
        for (EquipementsProtectionJoueurs ep : listeEquipementsProtectionJoueurs) {
            if (ep.reference != null && ep.reference.equals(reference)) {
                if (ep.nombre >= quantite) {
                    ep.nombre -= quantite;
                    saveProtections();
                    System.out.println("Stock mis à jour pour l'équipement de protection avec la référence "
                            + reference + ". nouveau nombre: " + ep.nombre);
                } else {
                    System.out
                            .println(
                                    "Stock insuffisant pour l'équipement de protection avec la référence " + reference);
                }
                return;
            }
        }
        System.out.println("Équipement avec la référence " + reference + " non trouvé.");
    }

    public void changerHauteur(String reference, int nouvelleHauteur) {
        for (EquipementsTerrain et : listeEquipementsTerrain) {
            if (et.reference != null && et.reference.equals(reference)) {
                et.hauteur = nouvelleHauteur;
                saveTerrains();
                System.out.println(
                        "Hauteur mise à jour pour l'équipement de terrain avec la référence " + reference);
                return;
            }
        }
        System.out.println("Équipement avec la référence " + reference + " non trouvé.");
    }

    public void changerLargeur(String reference, int nouvelleLargeur) {
        for (EquipementsTerrain et : listeEquipementsTerrain) {
            if (et.reference != null && et.reference.equals(reference)) {
                et.largeur = nouvelleLargeur;
                saveTerrains();
                System.out.println(
                        "Largeur mise à jour pour l'équipement de terrain avec la référence " + reference);
                return;
            }
        }
        System.out.println("Équipement avec la référence " + reference + " non trouvé.");
    }

    public void changerPoids(String reference, int nouveauPoids) {
        for (EquipementsTerrain et : listeEquipementsTerrain) {
            if (et.reference != null && et.reference.equals(reference)) {
                et.poids = nouveauPoids;
                saveTerrains();
                System.out.println("Poids mis à jour pour l'équipement de terrain avec la référence " + reference);
                return;
            }
        }
        System.out.println("Équipement avec la référence " + reference + " non trouvé.");
    }

    public void changerTaille(String reference, String nouvelleTaille) {
        for (EquipementsJoueurs ej : listeEquipementsJoueurs) {
            if (ej.reference != null && ej.reference.equals(reference)) {
                ej.taille = nouvelleTaille;
                saveJoueurs();
                System.out.println("Taille mise à jour pour l'équipement de joueur avec la référence " + reference);
                return;
            }
        }
        for (EquipementsProtectionJoueurs ep : listeEquipementsProtectionJoueurs) {
            if (ep.reference != null && ep.reference.equals(reference)) {
                ep.taille = nouvelleTaille;
                saveProtections();
                System.out
                        .println("Taille mise à jour pour l'équipement de protection avec la référence " + reference);
                return;
            }
        }
        System.out.println("Équipement avec la référence " + reference + " non trouvé.");
    }

    public void changerCouleur(String reference, String nouvelleCouleur) {
        for (EquipementsJoueurs ej : listeEquipementsJoueurs) {
            if (ej.reference != null && ej.reference.equals(reference)) {
                ej.couleur = nouvelleCouleur;
                saveJoueurs();
                System.out.println("Couleur mise à jour pour l'équipement de joueur avec la référence " + reference);
                return;
            }
        }
        for (EquipementsProtectionJoueurs ep : listeEquipementsProtectionJoueurs) {
            if (ep.reference != null && ep.reference.equals(reference)) {
                ep.couleur = nouvelleCouleur;
                saveProtections();
                System.out
                        .println("Couleur mise à jour pour l'équipement de protection avec la référence " + reference);
                return;
            }
        }
        System.out.println("Équipement avec la référence " + reference + " non trouvé.");
    }

    public void changerNiveau(String reference, String nouveauNiveau) {
        for (EquipementsProtectionJoueurs ep : listeEquipementsProtectionJoueurs) {
            if (ep.reference != null && ep.reference.equals(reference)) {
                ep.niveau = nouveauNiveau;
                saveProtections();
                System.out
                        .println("Niveau mis à jour pour l'équipement de protection avec la référence " + reference);
                return;
            }
        }
        System.out.println("Équipement avec la référence " + reference + " non trouvé.");
    }

    public void showAll() {
        System.out.println("Équipements de Terrain:");
        showEquipementsTerrain();
        System.out.println("\nÉquipements pour Joueurs:");
        showEquipementsJoueurs();
        System.out.println("\nÉquipements de Protection pour Joueurs:");
        showEquipementsProtectionJoueurs();
    }

    public void supprimerEquipement(String reference) {
        for (int i = 0; i < listeEquipementsTerrain.size(); i++) {
            if (listeEquipementsTerrain.get(i).reference != null
                    && listeEquipementsTerrain.get(i).reference.equals(reference)) {
                listeEquipementsTerrain.remove(i);
                // Mettre à jour les références suivantes
                for (int j = i; j < listeEquipementsTerrain.size(); j++) {
                    listeEquipementsTerrain.get(j).reference = "REF" + (j + 1);
                }
                saveTerrains();
                System.out.println("Équipement de terrain avec la référence " + reference + " supprimé.");
                return;
            }
        }
        for (int i = 0; i < listeEquipementsJoueurs.size(); i++) {
            if (listeEquipementsJoueurs.get(i).reference != null
                    && listeEquipementsJoueurs.get(i).reference.equals(reference)) {
                listeEquipementsJoueurs.remove(i);
                // Mettre à jour les références suivantes
                for (int j = i; j < listeEquipementsJoueurs.size(); j++) {
                    listeEquipementsJoueurs.get(j).reference = "REF" + (j + 1);
                }
                saveJoueurs();
                System.out.println("Équipement de joueur avec la référence " + reference + " supprimé.");
                return;
            }
        }
        for (int i = 0; i < listeEquipementsProtectionJoueurs.size(); i++) {
            if (listeEquipementsProtectionJoueurs.get(i).reference != null
                    && listeEquipementsProtectionJoueurs.get(i).reference.equals(reference)) {
                listeEquipementsProtectionJoueurs.remove(i);
                // Mettre à jour les références suivantes
                for (int j = i; j < listeEquipementsProtectionJoueurs.size(); j++) {
                    listeEquipementsProtectionJoueurs.get(j).reference = "REF" + (j + 1);
                }
                saveProtections();
                System.out.println("Équipement de protection avec la référence " + reference + " supprimé.");
                return;
            }
        }
        System.out.println("Équipement avec la référence " + reference + " non trouvé.");
    }

    /* Test */
    String[] sport = { "Football", "Basketball", "Cyclisme", "Tennis", "Natation", "Rugby", "Handball", "Volleyball",
            "Athlétisme", "Golf" };

    public void simulationBdd() {
        Random rand = new Random();

        for (String s : sport) {
            int randomQuantite = rand.nextInt(10) + 1; // Quantité aléatoire entre 1 et 10
            int randomPrix = rand.nextInt(100) + 1; // Prix aléatoire entre 1 et 100

            EquipementsTerrain et = new EquipementsTerrain();
            et.reference = "REF" + (listeEquipementsTerrain.size() + 1);
            et.sport = s;
            et.designation = "Ballon";
            et.prix = randomPrix;
            et.nombre = randomQuantite;
            et.hauteur = 0;
            et.largeur = 0;
            et.poids = 450;
            addEquipementTerrain(et);
        }

        for (String s : sport) {
            int randomQuantite = rand.nextInt(10) + 1; // Quantité aléatoire entre 1 et 10
            int randomPrix = rand.nextInt(100) + 1; // Prix aléatoire entre 1 et 100

            EquipementsJoueurs ej = new EquipementsJoueurs();
            ej.reference = "REF" + (listeEquipementsJoueurs.size() + 1);
            ej.sport = s;
            ej.designation = "Maillot";
            ej.prix = randomPrix;
            ej.nombre = randomQuantite;
            ej.taille = "L";
            ej.couleur = "Rouge";
            addEquipementJoueurs(ej);
        }

        for (String s : sport) {
            int randomQuantite = rand.nextInt(10) + 1; // Quantité aléatoire entre 1 et 10
            int randomPrix = rand.nextInt(100) + 1; // Prix aléatoire entre 1 et 100

            EquipementsProtectionJoueurs ep = new EquipementsProtectionJoueurs();
            ep.reference = "REF" + (listeEquipementsProtectionJoueurs.size() + 1);
            ep.sport = s;
            ep.designation = "Casque";
            ep.prix = randomPrix;
            ep.nombre = randomQuantite;
            ep.taille = "M";
            ep.couleur = "Noir";
            ep.niveau = "Pro";
            addEquipementProtectionJoueurs(ep);
        }
    }

}