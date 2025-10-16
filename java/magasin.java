import java.util.Vector;
import java.util.Random;
import java.util.Map;
import java.util.HashMap;

class magasin {
    EquipementsTerrain equipementTerrain;
    EquipementsJoueurs equipementJoueurs;
    EquipementsProtectionJoueurs equipementProtectionJoueurs;

    private Vector<EquipementsTerrain> listeEquipementsTerrain = new Vector<>();
    private Vector<EquipementsJoueurs> listeEquipementsJoueurs = new Vector<>();
    private Vector<EquipementsProtectionJoueurs> listeEquipementsProtectionJoueurs = new Vector<>();

    private final FileManager fm = new FileManager("magasin_bdd");

    // Index pour recherche rapide par référence -> {type, index}
    private Map<String, int[]> indexMap = new HashMap<>();

    public magasin() {
        // Charger les données existantes via FileManager
        listeEquipementsTerrain.addAll(fm.loadTerrains());
        listeEquipementsJoueurs.addAll(fm.loadJoueurs());
        listeEquipementsProtectionJoueurs.addAll(fm.loadProtections());
        // construire l'index après chargement
        rebuildIndex();
    }

    // ==================================
    // * Equipements Terrain
    // ==================================

    public void addEquipementTerrain(EquipementsTerrain e) {
        if (e == null)
            return;
        // Vérifier unicité globale via indexMap
        if (e.reference != null && indexMap.containsKey(e.reference)) {
            System.out.println("La référence " + e.reference + " existe déjà pour un équipement.");
            return;
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

    public void showEquipements(String ref) {
        for (EquipementsTerrain et : listeEquipementsTerrain) {
            if (et.reference.equals(ref)) {
                System.out.println("Référence: " + et.reference + ", Sport: " + et.sport + ", Désignation: "
                        + et.designation
                        + ", Prix: " + et.prix + ", Nombre: " + et.nombre + ", Hauteur: " + et.hauteur
                        + ", Largeur: "
                        + et.largeur + ", Poids: " + et.poids);
                return;
            }
        }
        for (EquipementsJoueurs ej : listeEquipementsJoueurs) {
            if (ej.reference.equals(ref)) {
                System.out.println("Référence: " + ej.reference + ", Sport: " + ej.sport + ", Désignation: "
                        + ej.designation
                        + ", Prix: " + ej.prix + ", Nombre: " + ej.nombre + ", Taille: " + ej.taille
                        + ", Couleur: " + ej.couleur);
                return;
            }
        }
        for (EquipementsProtectionJoueurs ep : listeEquipementsProtectionJoueurs) {
            if (ep.reference.equals(ref)) {
                System.out.println("Référence: " + ep.reference + ", Sport: " + ep.sport + ", Désignation: "
                        + ep.designation
                        + ", Prix: " + ep.prix + ", Nombre: " + ep.nombre + ", Taille: " + ep.taille
                        + ", Couleur: " + ep.couleur + ", Niveau: " + ep.niveau);
                return;
            }
        }
        System.out.println("Aucun équipement trouvé avec la référence: " + ref);
    }
    // ==================================
    // * Equipements Joueurs
    // ==================================

    public void addEquipementJoueurs(EquipementsJoueurs e) {
        if (e == null)
            return;
        // Vérifier unicité globale via indexMap
        if (e.reference != null && indexMap.containsKey(e.reference)) {
            System.out.println("La référence " + e.reference + " existe déjà pour un équipement.");
            return;
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
        // Vérifier unicité globale via indexMap
        if (e.reference != null && indexMap.containsKey(e.reference)) {
            System.out.println("La référence " + e.reference + " existe déjà pour un équipement.");
            return;
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
        rebuildIndex();
    }

    private void saveJoueurs() {
        fm.saveJoueurs(listeEquipementsJoueurs);
        rebuildIndex();
    }

    private void saveProtections() {
        fm.saveProtections(listeEquipementsProtectionJoueurs);
        rebuildIndex();
    }

    /**
     * Reconstruit l'index de recherche à partir des listes en mémoire.
     * indexMap: clé = référence -> int[]{type, index}
     * type: 0 = terrain, 1 = joueurs, 2 = protection
     */
    private void rebuildIndex() {
        indexMap.clear();
        for (int i = 0; i < listeEquipementsTerrain.size(); i++) {
            EquipementsTerrain et = listeEquipementsTerrain.get(i);
            if (et != null && et.reference != null)
                indexMap.put(et.reference, new int[] { 0, i });
        }
        for (int i = 0; i < listeEquipementsJoueurs.size(); i++) {
            EquipementsJoueurs ej = listeEquipementsJoueurs.get(i);
            if (ej != null && ej.reference != null)
                indexMap.put(ej.reference, new int[] { 1, i });
        }
        for (int i = 0; i < listeEquipementsProtectionJoueurs.size(); i++) {
            EquipementsProtectionJoueurs ep = listeEquipementsProtectionJoueurs.get(i);
            if (ep != null && ep.reference != null)
                indexMap.put(ep.reference, new int[] { 2, i });
        }
    }

    // ==================================
    // * gestion global des équipements
    // ==================================

    public void changerReference(String ancienneReference, String nouvelleReference) {
        if (nouvelleReference == null || nouvelleReference.trim().isEmpty()) {
            System.out.println("Nouvelle référence invalide.");
            return;
        }
        nouvelleReference = nouvelleReference.trim();

        // Vérifier unicité globale
        if (indexMap.containsKey(nouvelleReference)) {
            System.out.println("La nouvelle référence existe déjà pour un équipement.");
            return;
        }

        int[] res = rechercheEquipement(ancienneReference);
        if (res == null || res.length < 2 || res[0] == -1) {
            System.out.println("Équipement avec la référence " + ancienneReference + " non trouvé.");
            return;
        }
        int type = res[0];
        int idx = res[1];
        switch (type) {
            case 0:
                listeEquipementsTerrain.get(idx).reference = nouvelleReference;
                saveTerrains();
                System.out.println("Référence mise à jour pour l'équipement de terrain: " + ancienneReference + " -> "
                        + nouvelleReference);
                return;
            case 1:
                listeEquipementsJoueurs.get(idx).reference = nouvelleReference;
                saveJoueurs();
                System.out.println("Référence mise à jour pour l'équipement de joueur: " + ancienneReference + " -> "
                        + nouvelleReference);
                return;
            case 2:
                listeEquipementsProtectionJoueurs.get(idx).reference = nouvelleReference;
                saveProtections();
                System.out.println("Référence mise à jour pour l'équipement de protection: " + ancienneReference
                        + " -> " + nouvelleReference);
                return;
            default:
                System.out.println("Équipement avec la référence " + ancienneReference + " non trouvé.");
        }
    }

    public void changerSport(String reference, String nouveauSport) {
        int[] res = rechercheEquipement(reference);
        if (res == null || res.length < 2 || res[0] == -1) {
            System.out.println("Équipement avec la référence " + reference + " non trouvé.");
            return;
        }
        int type = res[0];
        int idx = res[1];
        switch (type) {
            case 0:
                listeEquipementsTerrain.get(idx).sport = nouveauSport;
                saveTerrains();
                System.out.println("Sport mis à jour pour l'équipement de terrain avec la référence " + reference);
                return;
            case 1:
                listeEquipementsJoueurs.get(idx).sport = nouveauSport;
                saveJoueurs();
                System.out.println("Sport mis à jour pour l'équipement de joueur avec la référence " + reference);
                return;
            case 2:
                listeEquipementsProtectionJoueurs.get(idx).sport = nouveauSport;
                saveProtections();
                System.out.println("Sport mis à jour pour l'équipement de protection avec la référence " + reference);
                return;
            default:
                System.out.println("Équipement avec la référence " + reference + " non trouvé.");
        }
    }

    public void changerDesignation(String reference, String nouvelleDesignation) {
        int[] res = rechercheEquipement(reference);
        if (res == null || res.length < 2 || res[0] == -1) {
            System.out.println("Équipement avec la référence " + reference + " non trouvé.");
            return;
        }
        int type = res[0];
        int idx = res[1];
        switch (type) {
            case 0:
                listeEquipementsTerrain.get(idx).designation = nouvelleDesignation;
                saveTerrains();
                System.out
                        .println("Désignation mise à jour pour l'équipement de terrain avec la référence " + reference);
                return;
            case 1:
                listeEquipementsJoueurs.get(idx).designation = nouvelleDesignation;
                saveJoueurs();
                System.out
                        .println("Désignation mise à jour pour l'équipement de joueur avec la référence " + reference);
                return;
            case 2:
                listeEquipementsProtectionJoueurs.get(idx).designation = nouvelleDesignation;
                saveProtections();
                System.out.println(
                        "Désignation mise à jour pour l'équipement de protection avec la référence " + reference);
                return;
            default:
                System.out.println("Équipement avec la référence " + reference + " non trouvé.");
        }
    }

    public void changerPrix(String reference, int nouveauPrix) {
        int[] res = rechercheEquipement(reference);
        if (res == null || res.length < 2 || res[0] == -1) {
            System.out.println("Équipement avec la référence " + reference + " non trouvé.");
            return;
        }
        int type = res[0];
        int idx = res[1];
        switch (type) {
            case 0:
                EquipementsTerrain et = listeEquipementsTerrain.get(idx);
                et.prix = nouveauPrix;
                saveTerrains();
                System.out.println("Prix mis à jour pour l'équipement de terrain avec la référence " + reference);
                return;
            case 1:
                EquipementsJoueurs ej = listeEquipementsJoueurs.get(idx);
                ej.prix = nouveauPrix;
                saveJoueurs();
                System.out.println("Prix mis à jour pour l'équipement de joueur avec la référence " + reference);
                return;
            case 2:
                EquipementsProtectionJoueurs ep = listeEquipementsProtectionJoueurs.get(idx);
                ep.prix = nouveauPrix;
                saveProtections();
                System.out.println("Prix mis à jour pour l'équipement de protection avec la référence " + reference);
                return;
            default:
                System.out.println("Équipement avec la référence " + reference + " non trouvé.");
        }
    }

    public void changerQuantite(String reference, int nouvelleQuantite) {
        int[] res = rechercheEquipement(reference);
        if (res == null || res.length < 2 || res[0] == -1) {
            System.out.println("Équipement avec la référence " + reference + " non trouvé.");
            return;
        }
        int type = res[0];
        int idx = res[1];
        switch (type) {
            case 0:
                listeEquipementsTerrain.get(idx).nombre = nouvelleQuantite;
                saveTerrains();
                System.out.println("Quantité mise à jour pour l'équipement de terrain avec la référence " + reference);
                return;
            case 1:
                listeEquipementsJoueurs.get(idx).nombre = nouvelleQuantite;
                saveJoueurs();
                System.out.println("Quantité mise à jour pour l'équipement de joueur avec la référence " + reference);
                return;
            case 2:
                listeEquipementsProtectionJoueurs.get(idx).nombre = nouvelleQuantite;
                saveProtections();
                System.out
                        .println("Quantité mise à jour pour l'équipement de protection avec la référence " + reference);
                return;
            default:
                System.out.println("Équipement avec la référence " + reference + " non trouvé.");
        }
    }

    public void incrementerStock(String reference, int quantite) {
        int[] res = rechercheEquipement(reference);
        if (res == null || res.length < 2 || res[0] == -1) {
            System.out.println("Équipement avec la référence " + reference + " non trouvé.");
            return;
        }
        int type = res[0];
        int idx = res[1];
        switch (type) {
            case 0:
                listeEquipementsTerrain.get(idx).nombre += quantite;
                saveTerrains();
                System.out.println("Stock mis à jour pour l'équipement de terrain avec la référence " + reference
                        + ". nouveau nombre: " + listeEquipementsTerrain.get(idx).nombre);
                return;
            case 1:
                listeEquipementsJoueurs.get(idx).nombre += quantite;
                saveJoueurs();
                System.out.println("Stock mis à jour pour l'équipement de joueur avec la référence " + reference
                        + ". nouveau nombre: " + listeEquipementsJoueurs.get(idx).nombre);
                return;
            case 2:
                listeEquipementsProtectionJoueurs.get(idx).nombre += quantite;
                saveProtections();
                System.out.println("Stock mis à jour pour l'équipement de protection avec la référence " + reference
                        + ". nouveau nombre: " + listeEquipementsProtectionJoueurs.get(idx).nombre);
                return;
            default:
                System.out.println("Équipement avec la référence " + reference + " non trouvé.");
        }
    }

    public void decrementerStock(String reference, int quantite) {
        int[] res = rechercheEquipement(reference);
        if (res == null || res.length < 2 || res[0] == -1) {
            System.out.println("Équipement avec la référence " + reference + " non trouvé.");
            return;
        }
        int type = res[0];
        int idx = res[1];
        switch (type) {
            case 0: {
                EquipementsTerrain et = listeEquipementsTerrain.get(idx);
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
            case 1: {
                EquipementsJoueurs ej = listeEquipementsJoueurs.get(idx);
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
            case 2: {
                EquipementsProtectionJoueurs ep = listeEquipementsProtectionJoueurs.get(idx);
                if (ep.nombre >= quantite) {
                    ep.nombre -= quantite;
                    saveProtections();
                    System.out.println("Stock mis à jour pour l'équipement de protection avec la référence " + reference
                            + ". nouveau nombre: " + ep.nombre);
                } else {
                    System.out.println(
                            "Stock insuffisant pour l'équipement de protection avec la référence " + reference);
                }
                return;
            }
            default:
                System.out.println("Équipement avec la référence " + reference + " non trouvé.");
        }
    }

    public void changerHauteur(String reference, int nouvelleHauteur) {
        int[] res = rechercheEquipement(reference);
        if (res == null || res[0] != 0) {
            System.out.println("Équipement de terrain avec la référence " + reference + " non trouvé.");
            return;
        }
        int idx = res[1];
        listeEquipementsTerrain.get(idx).hauteur = nouvelleHauteur;
        saveTerrains();
        System.out.println("Hauteur mise à jour pour l'équipement de terrain avec la référence " + reference);
    }

    public void changerLargeur(String reference, int nouvelleLargeur) {
        int[] res = rechercheEquipement(reference);
        if (res == null || res[0] != 0) {
            System.out.println("Équipement de terrain avec la référence " + reference + " non trouvé.");
            return;
        }
        int idx = res[1];
        listeEquipementsTerrain.get(idx).largeur = nouvelleLargeur;
        saveTerrains();
        System.out.println("Largeur mise à jour pour l'équipement de terrain avec la référence " + reference);
    }

    public void changerPoids(String reference, int nouveauPoids) {
        int[] res = rechercheEquipement(reference);
        if (res == null || res[0] != 0) {
            System.out.println("Équipement de terrain avec la référence " + reference + " non trouvé.");
            return;
        }
        int idx = res[1];
        listeEquipementsTerrain.get(idx).poids = nouveauPoids;
        saveTerrains();
        System.out.println("Poids mis à jour pour l'équipement de terrain avec la référence " + reference);
    }

    public void changerTaille(String reference, String nouvelleTaille) {
        int[] res = rechercheEquipement(reference);
        if (res == null || (res[0] != 1 && res[0] != 2)) {
            System.out.println("Équipement joueur/protection avec la référence " + reference + " non trouvé.");
            return;
        }
        int type = res[0];
        int idx = res[1];
        if (type == 1) {
            listeEquipementsJoueurs.get(idx).taille = nouvelleTaille;
            saveJoueurs();
            System.out.println("Taille mise à jour pour l'équipement de joueur avec la référence " + reference);
            return;
        } else {
            listeEquipementsProtectionJoueurs.get(idx).taille = nouvelleTaille;
            saveProtections();
            System.out.println("Taille mise à jour pour l'équipement de protection avec la référence " + reference);
            return;
        }
    }

    public void changerCouleur(String reference, String nouvelleCouleur) {
        int[] res = rechercheEquipement(reference);
        if (res == null || (res[0] != 1 && res[0] != 2)) {
            System.out.println("Équipement joueur/protection avec la référence " + reference + " non trouvé.");
            return;
        }
        int type = res[0];
        int idx = res[1];
        if (type == 1) {
            listeEquipementsJoueurs.get(idx).couleur = nouvelleCouleur;
            saveJoueurs();
            System.out.println("Couleur mise à jour pour l'équipement de joueur avec la référence " + reference);
            return;
        } else {
            listeEquipementsProtectionJoueurs.get(idx).couleur = nouvelleCouleur;
            saveProtections();
            System.out.println("Couleur mise à jour pour l'équipement de protection avec la référence " + reference);
            return;
        }
    }

    public void changerNiveau(String reference, String nouveauNiveau) {
        int[] res = rechercheEquipement(reference);
        if (res == null || res[0] != 2) {
            System.out.println("Équipement de protection avec la référence " + reference + " non trouvé.");
            return;
        }
        int idx = res[1];
        listeEquipementsProtectionJoueurs.get(idx).niveau = nouveauNiveau;
        saveProtections();
        System.out.println("Niveau mis à jour pour l'équipement de protection avec la référence " + reference);
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
                // Ne PAS renuméroter les autres références: conserver des identifiants stables
                saveTerrains();
                System.out.println("Équipement de terrain avec la référence " + reference + " supprimé.");
                return;
            }
        }
        for (int i = 0; i < listeEquipementsJoueurs.size(); i++) {
            if (listeEquipementsJoueurs.get(i).reference != null
                    && listeEquipementsJoueurs.get(i).reference.equals(reference)) {
                listeEquipementsJoueurs.remove(i);
                // Ne PAS renuméroter les autres références: conserver des identifiants stables
                saveJoueurs();
                System.out.println("Équipement de joueur avec la référence " + reference + " supprimé.");
                return;
            }
        }
        for (int i = 0; i < listeEquipementsProtectionJoueurs.size(); i++) {
            if (listeEquipementsProtectionJoueurs.get(i).reference != null
                    && listeEquipementsProtectionJoueurs.get(i).reference.equals(reference)) {
                listeEquipementsProtectionJoueurs.remove(i);
                // Ne PAS renuméroter les autres références: conserver des identifiants stables
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

        // Pool de valeurs variées
        String[] ballDesignations = { "Ballon", "Balle", "Pneu d'entraînement", "Cible" };
        String[] terrainDesignations = { "Filet", "Panneau", "Plot", "Cage" };
        String[] joueurDesignations = { "Maillot", "Short", "Chaussure", "Chaussette", "Gants" };
        String[] protectionDesignations = { "Casque", "Genouillière", "Coudière", "Protège-tibia", "Gilet" };
        String[] tailles = { "XS", "S", "M", "L", "XL" };
        String[] couleurs = { "Rouge", "Bleu", "Vert", "Noir", "Blanc", "Jaune", "Orange" };

        // Terrains : objets gros, prix moyen/élevé
        for (String s : sport) {
            EquipementsTerrain et = new EquipementsTerrain();
            et.reference = nextReference();
            et.sport = s;
            // mélanger designations
            if (rand.nextBoolean())
                et.designation = ballDesignations[rand.nextInt(ballDesignations.length)];
            else
                et.designation = terrainDesignations[rand.nextInt(terrainDesignations.length)];
            // prix plus élevé pour équipements de terrain
            et.prix = 50 + rand.nextInt(151); // 50..200
            et.nombre = 1 + rand.nextInt(20); // 1..20
            et.hauteur = 50 + rand.nextInt(451); // 50..500 cm
            et.largeur = 50 + rand.nextInt(1001); // 50..1050 cm
            et.poids = 100 + rand.nextInt(50000); // 100g..50kg
            addEquipementTerrain(et);
        }

        // Joueurs : vêtements et chaussures, plus de variété
        for (String s : sport) {
            int items = 3 + rand.nextInt(4); // 3..6 items par sport
            for (int i = 0; i < items; i++) {
                EquipementsJoueurs ej = new EquipementsJoueurs();
                ej.reference = nextReference();
                ej.sport = s;
                ej.designation = joueurDesignations[rand.nextInt(joueurDesignations.length)];
                ej.prix = 10 + rand.nextInt(191); // 10..200
                ej.nombre = rand.nextInt(50); // 0..49
                ej.taille = tailles[rand.nextInt(tailles.length)];
                ej.couleur = couleurs[rand.nextInt(couleurs.length)];
                addEquipementJoueurs(ej);
            }
        }

        // Protections : équipements de sécurité
        for (String s : sport) {
            int items = 1 + rand.nextInt(3); // 1..3 items
            for (int i = 0; i < items; i++) {
                EquipementsProtectionJoueurs ep = new EquipementsProtectionJoueurs();
                ep.reference = nextReference();
                ep.sport = s;
                ep.designation = protectionDesignations[rand.nextInt(protectionDesignations.length)];
                ep.prix = 15 + rand.nextInt(186); // 15..200
                ep.nombre = rand.nextInt(30); // 0..29
                ep.taille = tailles[rand.nextInt(tailles.length)];
                ep.couleur = couleurs[rand.nextInt(couleurs.length)];
                // niveau aléatoire: débutant/intermédiaire/pro
                String[] niveaux = { "Débutant", "Intermédiaire", "Pro" };
                ep.niveau = niveaux[rand.nextInt(niveaux.length)];
                addEquipementProtectionJoueurs(ep);
            }
        }
    }

    /**
     * Génère une nouvelle référence unique de la forme REF{n}.
     */
    private String nextReference() {
        int max = 0;
        // parcourir toutes les clés de l'index pour trouver le max
        for (String k : indexMap.keySet()) {
            if (k != null && k.startsWith("REF")) {
                try {
                    int v = Integer.parseInt(k.substring(3));
                    if (v > max)
                        max = v;
                } catch (NumberFormatException ex) {
                    // ignorer
                }
            }
        }
        return "REF" + (max + 1);
    }

    public int[] rechercheEquipement(String reference) {
        if (reference == null)
            return new int[] { -1, -1 };

        // Vérifier l'index
        int[] cached = indexMap.get(reference);
        if (cached != null) {
            // vérifier que l'élément existe toujours à cet index
            int type = cached[0];
            int idx = cached[1];
            try {
                switch (type) {
                    case 0:
                        if (idx >= 0 && idx < listeEquipementsTerrain.size()
                                && listeEquipementsTerrain.get(idx).reference != null
                                && listeEquipementsTerrain.get(idx).reference.equals(reference))
                            return new int[] { 0, idx };
                        break;
                    case 1:
                        if (idx >= 0 && idx < listeEquipementsJoueurs.size()
                                && listeEquipementsJoueurs.get(idx).reference != null
                                && listeEquipementsJoueurs.get(idx).reference.equals(reference))
                            return new int[] { 1, idx };
                        break;
                    case 2:
                        if (idx >= 0 && idx < listeEquipementsProtectionJoueurs.size()
                                && listeEquipementsProtectionJoueurs.get(idx).reference != null
                                && listeEquipementsProtectionJoueurs.get(idx).reference.equals(reference))
                            return new int[] { 2, idx };
                        break;
                }
            } catch (IndexOutOfBoundsException ex) {
                // fallback au scan linéaire
            }
        }

        // fallback : scan linéaire (sécurisé)
        for (int i = 0; i < listeEquipementsTerrain.size(); i++) {
            if (listeEquipementsTerrain.get(i).reference != null
                    && listeEquipementsTerrain.get(i).reference.equals(reference)) {
                return new int[] { 0, i }; // 0 pour terrain
            }
        }
        for (int i = 0; i < listeEquipementsJoueurs.size(); i++) {
            if (listeEquipementsJoueurs.get(i).reference != null
                    && listeEquipementsJoueurs.get(i).reference.equals(reference)) {
                return new int[] { 1, i }; // 1 pour joueurs
            }
        }
        for (int i = 0; i < listeEquipementsProtectionJoueurs.size(); i++) {
            if (listeEquipementsProtectionJoueurs.get(i).reference != null
                    && listeEquipementsProtectionJoueurs.get(i).reference.equals(reference)) {
                return new int[] { 2, i }; // 2 pour protection joueurs
            }
        }
        return new int[] { -1, -1 }; // Équipement non trouvé
    }

}