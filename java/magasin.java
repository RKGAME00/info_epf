import java.util.Vector;
import java.util.Random;

class magasin {
    // Méthode pour ajouter un équipement
    EquipementsTerrain equipementTerrain;
    EquipementsJoueurs equipementJoueurs;
    EquipementsProtectionJoueurs equipementProtectionJoueurs;

    private Vector<EquipementsTerrain> listeEquipementsTerrain = new Vector<>();
    private Vector<EquipementsJoueurs> listeEquipementsJoueurs = new Vector<>();
    private Vector<EquipementsProtectionJoueurs> listeEquipementsProtectionJoueurs = new Vector<>();

    public magasin() {

    }

    // =======================
    // * Equipements Terrain */
    // =======================

    public void addEquipementTerrain(EquipementsTerrain e) {
        if (e != null)
            listeEquipementsTerrain.add(e);
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

    // =======================
    // * Equipements Joueurs */
    // =======================

    public void addEquipementJoueurs(EquipementsJoueurs e) {
        if (e != null)
            listeEquipementsJoueurs.add(e);
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
    // * Equipements Protection Joueurs */
    // ==================================

    public void addEquipementProtectionJoueurs(EquipementsProtectionJoueurs e) {
        if (e != null)
            listeEquipementsProtectionJoueurs.add(e);
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
    // * gestion global des équipements */
    // ==================================

    public void changerReference(String ancienneReference, String nouvelleReference) {
        // Vérifier si la nouvelle référence existe déjà
        for (EquipementsTerrain et : listeEquipementsTerrain) {
            if (et.reference.equals(nouvelleReference)) {
                System.out.println("La nouvelle référence existe déjà pour un équipement de terrain.");
                return;
            }
        }
        for (EquipementsJoueurs ej : listeEquipementsJoueurs) {
            if (ej.reference.equals(nouvelleReference)) {
                System.out.println("La nouvelle référence existe déjà pour un équipement de joueur.");
                return;
            }
        }
        for (EquipementsProtectionJoueurs ep : listeEquipementsProtectionJoueurs) {
            if (ep.reference.equals(nouvelleReference)) {
                System.out.println("La nouvelle référence existe déjà pour un équipement de protection.");
                return;
            }
        }

        // Si la nouvelle référence n'existe pas, procéder au changement
        for (EquipementsTerrain et : listeEquipementsTerrain) {
            if (et.reference.equals(ancienneReference)) {
                et.reference = nouvelleReference;
                System.out.println(
                        "Référence mise à jour pour l'équipement de terrain: " + ancienneReference + " -> "
                                + nouvelleReference);
                return;
            }
        }
        for (EquipementsJoueurs ej : listeEquipementsJoueurs) {
            if (ej.reference.equals(ancienneReference)) {
                ej.reference = nouvelleReference;
                System.out.println("Référence mise à jour pour l'équipement de joueur: " + ancienneReference + " -> "
                        + nouvelleReference);
                return;
            }
        }
        for (EquipementsProtectionJoueurs ep : listeEquipementsProtectionJoueurs) {
            if (ep.reference.equals(ancienneReference)) {
                ep.reference = nouvelleReference;
                System.out.println("Référence mise à jour pour l'équipement de protection: " + ancienneReference
                        + " -> " + nouvelleReference);
                return;
            }
        }
        System.out.println("Équipement avec la référence " + ancienneReference + " non trouvé.");
    }

    public void changerSport(String reference, String nouveauSport) {

        for (EquipementsTerrain et : listeEquipementsTerrain) {
            if (et.reference.equals(reference)) {
                et.sport = nouveauSport;
                System.out.println("Sport mis à jour pour l'équipement de terrain avec la référence " + reference);
                return;
            }
        }
        for (EquipementsJoueurs ej : listeEquipementsJoueurs) {
            if (ej.reference.equals(reference)) {
                ej.sport = nouveauSport;
                System.out.println("Sport mis à jour pour l'équipement de joueur avec la référence " + reference);
                return;
            }
        }
        for (EquipementsProtectionJoueurs ep : listeEquipementsProtectionJoueurs) {
            if (ep.reference.equals(reference)) {
                ep.sport = nouveauSport;
                System.out
                        .println("Sport mis à jour pour l'équipement de protection avec la référence " + reference);
                return;
            }
        }
        System.out.println("Équipement avec la référence " + reference + " non trouvé.");

    }

    public void changerDesignation(String reference, String nouvelleDesignation) {
        for (EquipementsTerrain et : listeEquipementsTerrain) {
            if (et.reference.equals(reference)) {
                et.designation = nouvelleDesignation;
                System.out.println(
                        "Désignation mise à jour pour l'équipement de terrain avec la référence " + reference);
                return;
            }
        }
        for (EquipementsJoueurs ej : listeEquipementsJoueurs) {
            if (ej.reference.equals(reference)) {
                ej.designation = nouvelleDesignation;
                System.out
                        .println("Désignation mise à jour pour l'équipement de joueur avec la référence " + reference);
                return;
            }
        }
        for (EquipementsProtectionJoueurs ep : listeEquipementsProtectionJoueurs) {
            if (ep.reference.equals(reference)) {
                ep.designation = nouvelleDesignation;
                System.out.println(
                        "Désignation mise à jour pour l'équipement de protection avec la référence " + reference);
                return;
            }
        }
        System.out.println("Équipement avec la référence " + reference + " non trouvé.");
    }

    public void changerPrix(String reference, int nouveauPrix) {
        for (EquipementsTerrain et : listeEquipementsTerrain) {
            if (et.reference.equals(reference)) {
                et.prix = nouveauPrix;
                System.out.println("Prix mis à jour pour l'équipement de terrain avec la référence " + reference);
                return;
            }
        }
        for (EquipementsJoueurs ej : listeEquipementsJoueurs) {
            if (ej.reference.equals(reference)) {
                ej.prix = nouveauPrix;
                System.out.println("Prix mis à jour pour l'équipement de joueur avec la référence " + reference);
                return;
            }
        }
        for (EquipementsProtectionJoueurs ep : listeEquipementsProtectionJoueurs) {
            if (ep.reference.equals(reference)) {
                ep.prix = nouveauPrix;
                System.out.println("Prix mis à jour pour l'équipement de protection avec la référence " + reference);
                return;
            }
        }
        System.out.println("Équipement avec la référence " + reference + " non trouvé.");
    }

    public void changerQuantite(String reference, int nouvelleQuantite) {
        for (EquipementsTerrain et : listeEquipementsTerrain) {
            if (et.reference.equals(reference)) {
                et.nombre = nouvelleQuantite;
                System.out.println("Quantité mise à jour pour l'équipement de terrain avec la référence " + reference);
                return;
            }
        }
        for (EquipementsJoueurs ej : listeEquipementsJoueurs) {
            if (ej.reference.equals(reference)) {
                ej.nombre = nouvelleQuantite;
                System.out.println("Quantité mise à jour pour l'équipement de joueur avec la référence " + reference);
                return;
            }
        }
        for (EquipementsProtectionJoueurs ep : listeEquipementsProtectionJoueurs) {
            if (ep.reference.equals(reference)) {
                ep.nombre = nouvelleQuantite;
                System.out
                        .println("Quantité mise à jour pour l'équipement de protection avec la référence " + reference);
                return;
            }
        }
        System.out.println("Équipement avec la référence " + reference + " non trouvé.");
    }

    public void incrementerStock(String reference, int quantite) {
        for (EquipementsTerrain et : listeEquipementsTerrain) {
            if (et.reference.equals(reference)) {
                et.nombre += quantite;
                System.out.println("Stock mis à jour pour l'équipement de terrain avec la référence " + reference
                        + ". nouveau nombre: " + et.nombre);
                return;
            }
        }
        for (EquipementsJoueurs ej : listeEquipementsJoueurs) {
            if (ej.reference.equals(reference)) {
                ej.nombre += quantite;
                System.out.println("Stock mis à jour pour l'équipement de joueur avec la référence " + reference
                        + ". nouveau nombre: " + ej.nombre);
                return;
            }
        }
        for (EquipementsProtectionJoueurs ep : listeEquipementsProtectionJoueurs) {
            if (ep.reference.equals(reference)) {
                ep.nombre += quantite;
                System.out.println("Stock mis à jour pour l'équipement de protection avec la référence " + reference
                        + ". nouveau nombre: " + ep.nombre);
                return;
            }
        }
        System.out.println("Équipement avec la référence " + reference + " non trouvé.");
    }

    public void decrementerStock(String reference, int quantite) {
        for (EquipementsTerrain et : listeEquipementsTerrain) {
            if (et.reference.equals(reference)) {
                if (et.nombre >= quantite) {
                    et.nombre -= quantite;
                    System.out.println("Stock mis à jour pour l'équipement de terrain avec la référence " + reference
                            + ". nouveau nombre: " + et.nombre);
                } else {
                    System.out.println("Stock insuffisant pour l'équipement de terrain avec la référence " + reference);
                }
                return;
            }
        }
        for (EquipementsJoueurs ej : listeEquipementsJoueurs) {
            if (ej.reference.equals(reference)) {
                if (ej.nombre >= quantite) {
                    ej.nombre -= quantite;
                    System.out.println("Stock mis à jour pour l'équipement de joueur avec la référence " + reference
                            + ". nouveau nombre: " + ej.nombre);
                } else {
                    System.out.println("Stock insuffisant pour l'équipement de joueur avec la référence " + reference);
                }
                return;
            }
        }
        for (EquipementsProtectionJoueurs ep : listeEquipementsProtectionJoueurs) {
            if (ep.reference.equals(reference)) {
                if (ep.nombre >= quantite) {
                    ep.nombre -= quantite;
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
            if (et.reference.equals(reference)) {
                et.hauteur = nouvelleHauteur;
                System.out.println(
                        "Hauteur mise à jour pour l'équipement de terrain avec la référence " + reference);
                return;
            }
        }

        for (EquipementsJoueurs ej : listeEquipementsJoueurs) {
            if (ej.reference.equals(reference)) {
                System.out.println("L'équipement avec la référence " + reference + " ne possède pas de hauteur.");
                return;
            }
        }

        for (EquipementsProtectionJoueurs ep : listeEquipementsProtectionJoueurs) {
            if (ep.reference.equals(reference)) {
                System.out.println("L'équipement avec la référence " + reference + " ne possède pas de hauteur.");
                return;
            }
        }
        System.out.println("Équipement avec la référence " + reference + " non trouvé.");
    }

    public void changerLargeur(String reference, int nouvelleLargeur) {
        for (EquipementsTerrain et : listeEquipementsTerrain) {
            if (et.reference.equals(reference)) {
                et.largeur = nouvelleLargeur;
                System.out.println(
                        "Largeur mise à jour pour l'équipement de terrain avec la référence " + reference);
                return;
            }
        }

        for (EquipementsJoueurs ej : listeEquipementsJoueurs) {
            if (ej.reference.equals(reference)) {
                System.out.println("L'équipement avec la référence " + reference + " ne possède pas de largeur.");
                return;
            }
        }

        for (EquipementsProtectionJoueurs ep : listeEquipementsProtectionJoueurs) {
            if (ep.reference.equals(reference)) {
                System.out.println("L'équipement avec la référence " + reference + " ne possède pas de largeur.");
                return;
            }
        }
        System.out.println("Équipement avec la référence " + reference + " non trouvé.");
    }

    public void changerPoids(String reference, int nouveauPoids) {
        for (EquipementsTerrain et : listeEquipementsTerrain) {
            if (et.reference.equals(reference)) {
                et.poids = nouveauPoids;
                System.out.println("Poids mis à jour pour l'équipement de terrain avec la référence " + reference);
                return;
            }
        }

        for (EquipementsJoueurs ej : listeEquipementsJoueurs) {
            if (ej.reference.equals(reference)) {
                System.out.println("L'équipement avec la référence " + reference + " ne possède pas de poids.");
                return;
            }
        }

        for (EquipementsProtectionJoueurs ep : listeEquipementsProtectionJoueurs) {
            if (ep.reference.equals(reference)) {
                System.out.println("L'équipement avec la référence " + reference + " ne possède pas de poids.");
                return;
            }
        }
        System.out.println("Équipement avec la référence " + reference + " non trouvé.");
    }

    public void changerTaille(String reference, String nouvelleTaille) {

        for (EquipementsJoueurs ej : listeEquipementsJoueurs) {
            if (ej.reference.equals(reference)) {
                ej.taille = nouvelleTaille;
                System.out.println("Taille mise à jour pour l'équipement de joueur avec la référence " + reference);
                return;
            }
        }

        for (EquipementsProtectionJoueurs ep : listeEquipementsProtectionJoueurs) {
            if (ep.reference.equals(reference)) {
                ep.taille = nouvelleTaille;
                System.out
                        .println("Taille mise à jour pour l'équipement de protection avec la référence " + reference);
                return;
            }
        }

        for (EquipementsTerrain et : listeEquipementsTerrain) {
            if (et.reference.equals(reference)) {
                System.out.println("L'équipement avec la référence " + reference + " ne possède pas de taille.");
                return;
            }
        }

        System.out.println("Équipement avec la référence " + reference + " non trouvé.");
    }

    public void changerCouleur(String reference, String nouvelleCouleur) {

        for (EquipementsJoueurs ej : listeEquipementsJoueurs) {
            if (ej.reference.equals(reference)) {
                ej.couleur = nouvelleCouleur;
                System.out.println("Couleur mise à jour pour l'équipement de joueur avec la référence " + reference);
                return;
            }
        }

        for (EquipementsProtectionJoueurs ep : listeEquipementsProtectionJoueurs) {
            if (ep.reference.equals(reference)) {
                ep.couleur = nouvelleCouleur;
                System.out
                        .println("Couleur mise à jour pour l'équipement de protection avec la référence " + reference);
                return;
            }
        }

        for (EquipementsTerrain et : listeEquipementsTerrain) {
            if (et.reference.equals(reference)) {
                System.out.println("L'équipement avec la référence " + reference + " ne possède pas de couleur.");
                return;
            }
        }

        System.out.println("Équipement avec la référence " + reference + " non trouvé.");
    }

    public void changerNiveau(String reference, String nouveauNiveau) {

        for (EquipementsProtectionJoueurs ep : listeEquipementsProtectionJoueurs) {
            if (ep.reference.equals(reference)) {
                ep.niveau = nouveauNiveau;
                System.out
                        .println("Niveau mis à jour pour l'équipement de protection avec la référence " + reference);
                return;
            }
        }

        for (EquipementsJoueurs ej : listeEquipementsJoueurs) {
            if (ej.reference.equals(reference)) {
                System.out.println("L'équipement avec la référence " + reference + " ne possède pas de niveau.");
                return;
            }
        }

        for (EquipementsTerrain et : listeEquipementsTerrain) {
            if (et.reference.equals(reference)) {
                System.out.println("L'équipement avec la référence " + reference + " ne possède pas de niveau.");
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