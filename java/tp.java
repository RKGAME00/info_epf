import java.util.Scanner;

public class tp {

    public static void main(String[] args) {
        // If user passed --nogui, run the original console UI. Otherwise launch JavaFX
        // GUI.
        for (String a : args) {
            if (a != null && a.equalsIgnoreCase("--nogui")) {
                runConsoleUI();
                return;
            }
        }

        // Try to launch JavaFX GUI. If JavaFX not available, try Swing before falling
        // back to console UI.
        // Try to launch JavaFX GUI by reflection (so compilation doesn't require
        // JavaFX).
        try {
            try {
                Class<?> gclass = Class.forName("Gui");
                java.lang.reflect.Method m = gclass.getMethod("main", String[].class);
                m.invoke(null, (Object) args);
                return;
            } catch (ClassNotFoundException cnf) {
                throw cnf;
            }
        } catch (Throwable t) {
            System.out
                    .println("Gui non disponible ou erreur au lancement. Tentative de fallback vers Swing...");
            try {
                Gui.main(args);
                return;
            } catch (Throwable t2) {
                System.out.println("Swing non disponible ou erreur. Basculage vers UI console.");
                runConsoleUI();
                return;
            }
        }
    }

    private static void runConsoleUI() {
        Scanner scan = new Scanner(System.in);
        magasin mag = new magasin();
        windowUi mw = new windowUi();

        while (true) {
            mw.mainwindow();
            int choix = readIntSafe(scan);

            switch (choix) {
                case 0:
                    mw.quiter();
                    System.exit(0);
                    break;
                case 1: {
                    boolean exitGestionStock = false;
                    while (!exitGestionStock) {
                        mw.gestionStock();
                        int choixEquip = readIntSafe(scan);
                        switch (choixEquip) {
                            case 0:
                                // retour au menu principal
                                exitGestionStock = true;
                                break;
                            case 1: { // ajouter équipement
                                boolean exitAjout = false;
                                while (!exitAjout) {
                                    mw.ajouterEquipement();
                                    int choixAjout = readIntSafe(scan);
                                    switch (choixAjout) {
                                        case 0:
                                            // retour au menu gestionStock
                                            exitAjout = true;
                                            break;
                                        case 1:
                                            EquipementsTerrain et = saisirEquipementTerrain(scan, mw);
                                            mag.addEquipementTerrain(et);
                                            System.out.println("\nÉquipement de terrain ajouté avec succès !");
                                            // rester dans gestionStock (exitAjout true returns to gestionStock)
                                            exitAjout = true;
                                            break;
                                        case 2:
                                            EquipementsJoueurs ej = saisirEquipementJoueurs(scan, mw);
                                            mag.addEquipementJoueurs(ej);
                                            System.out.println("\nÉquipement de joueurs ajouté avec succès !");
                                            exitAjout = true;
                                            break;
                                        case 3:
                                            EquipementsProtectionJoueurs ep = saisirEquipementProtectionJoueurs(scan,
                                                    mw);
                                            mag.addEquipementProtectionJoueurs(ep);
                                            System.out.println(
                                                    "\nÉquipement de protection de joueurs ajouté avec succès !");
                                            exitAjout = true;
                                            break;
                                        default:
                                            System.out.println("\nChoix invalide. Veuillez réessayer.");
                                            break;
                                    }
                                }
                            }
                                break;
                            case 2: { // Incrémenter
                                mw.incrimenterEquipement();
                                String refInc = scan.next();
                                if (refInc.equals("0")) {
                                    System.out.println("Annulation — retour.");
                                    break;
                                }
                                refInc = refInc.trim().toUpperCase();
                                System.out.print("Quantité à ajouter (entier) : ");
                                int qInc = readIntSafe(scan);
                                mag.incrementerStock(refInc, qInc);
                                break;
                            }
                            case 3: { // Décrémenter
                                mw.decrementerEquipement();
                                String refDec = scan.next();
                                if (refDec.equals("0")) {
                                    System.out.println("Annulation — retour.");
                                    break;
                                }
                                refDec = refDec.trim().toUpperCase();
                                System.out.print("Quantité à retirer (entier) : ");
                                int qDec = readIntSafe(scan);
                                mag.decrementerStock(refDec, qDec);
                                break;
                            }
                            case 4: { // Supprimer
                                mw.supprimerEquipement();
                                String refToDelete = scan.next();
                                if (refToDelete.equals("0")) {
                                    System.out.println("Annulation — retour.");
                                    break;
                                }
                                mag.supprimerEquipement(refToDelete.trim().toUpperCase());
                                break;
                            }
                            case 5: { // Afficher
                                boolean exitAff = false;
                                while (!exitAff) {
                                    mw.afficherEquipement();
                                    int choixAff = readIntSafe(scan);
                                    switch (choixAff) {
                                        case 0:
                                            exitAff = true; // retour gestionStock
                                            break;
                                        case 1:
                                            mw.afficherEquipementTerrain();
                                            mag.showEquipementsTerrain();
                                            break;
                                        case 2:
                                            mw.afficherEquipementJoueurs();
                                            mag.showEquipementsJoueurs();
                                            break;
                                        case 3:
                                            mw.afficherEquipementProtectionJoueurs();
                                            mag.showEquipementsProtectionJoueurs();
                                            break;
                                        case 4:
                                            mag.showAll();
                                            break;
                                        default:
                                            System.out.println("\nChoix invalide. Veuillez réessayer.");
                                            break;
                                    }
                                }
                                break;
                            }
                            case 6: // Modifier
                                modifierEquipement(scan, mw, mag);
                                break;
                            case 7: { // Recherche
                                boolean exitSearch = false;
                                while (!exitSearch) {
                                    mw.rechercherEquipement();

                                    String refToSearch = scan.next();
                                    if (refToSearch.equals("0")) {
                                        System.out.println("Recherche annulée.");
                                        break;
                                    }
                                    refToSearch = refToSearch.trim().toUpperCase();
                                    int equipement[] = mag.rechercheEquipement(refToSearch);
                                    if (equipement != null && equipement.length >= 2 && equipement[0] != -1) {
                                        // afficher selon le type
                                        System.out.println();
                                        mag.showEquipements(refToSearch);
                                    } else {
                                        System.out.println("\nÉquipement non trouvé.");
                                    }
                                    System.out.print("\nVoulez-vous rechercher un autre équipement ? (O/N) : ");
                                    String reponse = scan.next();
                                    if (!reponse.equalsIgnoreCase("O")) {
                                        exitSearch = true;
                                    }
                                }
                                break;
                            }
                            case 8:
                                // debug: simulation rapide
                                mag.simulationBdd();
                                break;
                            default:
                                System.out.println("\nChoix invalide. Veuillez réessayer.");
                                break;
                        }
                    }
                }
                    break;
                case 2:
                    // gestion commandes (non implémenté)
                    break;
                default:
                    System.out.println("\nChoix invalide. Veuillez réessayer.");
                    break;
            }
        }
    }

    private static EquipementsTerrain saisirEquipementTerrain(Scanner scan, windowUi mw) {
        EquipementsTerrain et = new EquipementsTerrain();
        mw.ajouterEquipementTerrain(0);
        mw.ajouterEquipementTerrain(1);
        et.reference = scan.next();
        mw.ajouterEquipementTerrain(2);
        et.sport = scan.next();
        mw.ajouterEquipementTerrain(3);
        et.designation = scan.next();
        mw.ajouterEquipementTerrain(4);
        et.prix = readIntSafe(scan);
        mw.ajouterEquipementTerrain(5);
        et.nombre = readIntSafe(scan);
        mw.ajouterEquipementTerrain(6);
        et.hauteur = readIntSafe(scan);
        mw.ajouterEquipementTerrain(7);
        et.largeur = readIntSafe(scan);
        mw.ajouterEquipementTerrain(8);
        et.poids = readIntSafe(scan);
        return et;
    }

    private static EquipementsJoueurs saisirEquipementJoueurs(Scanner scan, windowUi mw) {
        EquipementsJoueurs ej = new EquipementsJoueurs();
        mw.ajouterEquipementJoueurs(0);
        mw.ajouterEquipementJoueurs(1);
        ej.reference = scan.next();
        mw.ajouterEquipementJoueurs(2);
        ej.sport = scan.next();
        mw.ajouterEquipementJoueurs(3);
        ej.designation = scan.next();
        mw.ajouterEquipementJoueurs(4);
        ej.prix = readIntSafe(scan);
        mw.ajouterEquipementJoueurs(5);
        ej.nombre = readIntSafe(scan);
        mw.ajouterEquipementJoueurs(6);
        ej.taille = scan.next();
        mw.ajouterEquipementJoueurs(7);
        ej.couleur = scan.next();
        return ej;
    }

    private static EquipementsProtectionJoueurs saisirEquipementProtectionJoueurs(Scanner scan, windowUi mw) {
        EquipementsProtectionJoueurs ep = new EquipementsProtectionJoueurs();
        mw.ajouterEquipementProtectionJoueurs(0);
        mw.ajouterEquipementProtectionJoueurs(1);
        ep.reference = scan.next();
        mw.ajouterEquipementProtectionJoueurs(2);
        ep.sport = scan.next();
        mw.ajouterEquipementProtectionJoueurs(3);
        ep.designation = scan.next();
        mw.ajouterEquipementProtectionJoueurs(4);
        ep.prix = readIntSafe(scan);
        mw.ajouterEquipementProtectionJoueurs(5);
        ep.nombre = readIntSafe(scan);
        mw.ajouterEquipementProtectionJoueurs(6);
        ep.taille = scan.next();
        mw.ajouterEquipementProtectionJoueurs(7);
        ep.couleur = scan.next();
        mw.ajouterEquipementProtectionJoueurs(8);
        ep.niveau = scan.next();
        return ep;
    }

    private static void modifierEquipement(Scanner scan, windowUi mw, magasin mag) {
        System.out.print("Référence de l'équipement à modifier (ou 0 pour annuler) : ");
        String reference = scan.next();
        if (reference.equals("0")) {
            System.out.println("Annulation — retour.");
            return;
        }
        int[] res = mag.rechercheEquipement(reference);
        if (res == null || res.length < 2 || res[0] == -1) {
            System.out.println("Équipement non trouvé pour la référence : " + reference);
            return;
        }

        int type = res[0];
        int index = res[1];

        boolean exitMod = false;
        while (!exitMod) {
            System.out.println("\nÉquipement trouvé (type=" + type + ", index=" + index + ")");
            switch (type) {
                case 0: { // terrain
                    EquipementsTerrain et = mag.getListeEquipementsTerrain().get(index);
                    System.out.println("Réf: " + et.reference + ", sport: " + et.sport + ", désignation: "
                            + et.designation + ", prix: " + et.prix + ", nombre: " + et.nombre + ", hauteur: "
                            + et.hauteur + ", largeur: " + et.largeur + ", poids: " + et.poids);
                    System.out.println(
                            "1. Changer référence\n2. Changer sport\n3. Changer désignation\n4. Changer prix\n5. Changer quantité\n6. Changer hauteur\n7. Changer largeur\n8. Changer poids\n0. Retour");
                    System.out.print("Choix : ");
                    int c = readIntSafe(scan);
                    switch (c) {
                        case 0:
                            exitMod = true;
                            break;
                        case 1:
                            System.out.print("Nouvelle référence (ou 0 pour annuler) : ");
                            String newRef = scan.next();
                            if (newRef.equals("0")) {
                                System.out.println("Modification annulée.");
                                break;
                            }
                            mag.changerReference(et.reference, newRef);
                            exitMod = true;
                            break;
                        case 2:
                            System.out.print("Nouveau sport (ou 0 pour annuler) : ");
                            String ns = scan.next();
                            if (ns.equals("0")) {
                                System.out.println("Modification annulée.");
                                break;
                            }
                            mag.changerSport(et.reference, ns);
                            break;
                        case 3:
                            System.out.print("Nouvelle désignation (ou 0 pour annuler) : ");
                            String nd = scan.next();
                            if (nd.equals("0")) {
                                System.out.println("Modification annulée.");
                                break;
                            }
                            mag.changerDesignation(et.reference, nd);
                            break;
                        case 4:
                            System.out.print("Nouveau prix (entier) ou 0 pour annuler : ");
                            String np = scan.next();
                            if (np.equals("0")) {
                                System.out.println("Modification annulée.");
                                break;
                            }
                            try {
                                int val = Integer.parseInt(np);
                                mag.changerPrix(et.reference, val);
                            } catch (NumberFormatException ex) {
                                System.out.println("Valeur invalide — aucune modification effectuée.");
                            }
                            break;
                        case 5:
                            System.out.print("Nouvelle quantité (entier) ou 0 pour annuler : ");
                            String nq = scan.next();
                            if (nq.equals("0")) {
                                System.out.println("Modification annulée.");
                                break;
                            }
                            try {
                                int val = Integer.parseInt(nq);
                                mag.changerQuantite(et.reference, val);
                            } catch (NumberFormatException ex) {
                                System.out.println("Valeur invalide — aucune modification effectuée.");
                            }
                            break;
                        case 6:
                            System.out.print("Nouvelle hauteur (entier) ou 0 pour annuler : ");
                            String nh = scan.next();
                            if (nh.equals("0")) {
                                System.out.println("Modification annulée.");
                                break;
                            }
                            try {
                                int val = Integer.parseInt(nh);
                                mag.changerHauteur(et.reference, val);
                            } catch (NumberFormatException ex) {
                                System.out.println("Valeur invalide — aucune modification effectuée.");
                            }
                            break;
                        case 7:
                            System.out.print("Nouvelle largeur (entier) ou 0 pour annuler : ");
                            String nl = scan.next();
                            if (nl.equals("0")) {
                                System.out.println("Modification annulée.");
                                break;
                            }
                            try {
                                int val = Integer.parseInt(nl);
                                mag.changerLargeur(et.reference, val);
                            } catch (NumberFormatException ex) {
                                System.out.println("Valeur invalide — aucune modification effectuée.");
                            }
                            break;
                        case 8:
                            System.out.print("Nouveau poids (entier) ou 0 pour annuler : ");
                            String npw = scan.next();
                            if (npw.equals("0")) {
                                System.out.println("Modification annulée.");
                                break;
                            }
                            try {
                                int val = Integer.parseInt(npw);
                                mag.changerPoids(et.reference, val);
                            } catch (NumberFormatException ex) {
                                System.out.println("Valeur invalide — aucune modification effectuée.");
                            }
                            break;
                        default:
                            System.out.println("Choix invalide.");
                    }
                }
                    break;
                case 1: { // joueurs
                    EquipementsJoueurs ej = mag.getListeEquipementsJoueurs().get(index);
                    System.out.println("Réf: " + ej.reference + ", sport: " + ej.sport + ", désignation: "
                            + ej.designation + ", prix: " + ej.prix + ", nombre: " + ej.nombre + ", taille: "
                            + ej.taille + ", couleur: " + ej.couleur);
                    System.out.println(
                            "1. Changer référence\n2. Changer sport\n3. Changer désignation\n4. Changer prix\n5. Changer quantité\n6. Changer taille\n7. Changer couleur\n0. Retour");
                    System.out.print("Choix : ");
                    int c = readIntSafe(scan);
                    switch (c) {
                        case 0:
                            exitMod = true;
                            break;

                        case 1:
                            System.out.print("Nouvelle référence (ou 0 pour annuler) : ");
                            String newRefJ = scan.next();
                            if (newRefJ.equals("0")) {
                                System.out.println("Modification annulée.");
                                break;
                            }
                            mag.changerReference(ej.reference, newRefJ);
                            exitMod = true;
                            break;
                        case 2:
                            System.out.print("Nouveau sport (ou 0 pour annuler) : ");
                            String nsj = scan.next();
                            if (nsj.equals("0")) {
                                System.out.println("Modification annulée.");
                                break;
                            }
                            mag.changerSport(ej.reference, nsj);
                            break;
                        case 3:
                            System.out.print("Nouvelle désignation (ou 0 pour annuler) : ");
                            String ndj = scan.next();
                            if (ndj.equals("0")) {
                                System.out.println("Modification annulée.");
                                break;
                            }
                            mag.changerDesignation(ej.reference, ndj);
                            break;
                        case 4:
                            System.out.print("Nouveau prix (entier) ou 0 pour annuler : ");
                            String npj = scan.next();
                            if (npj.equals("0")) {
                                System.out.println("Modification annulée.");
                                break;
                            }
                            try {
                                int val = Integer.parseInt(npj);
                                mag.changerPrix(ej.reference, val);
                            } catch (NumberFormatException ex) {
                                System.out.println("Valeur invalide — aucune modification effectuée.");
                            }
                            break;
                        case 5:
                            System.out.print("Nouvelle quantité (entier) ou 0 pour annuler : ");
                            String nqj = scan.next();
                            if (nqj.equals("0")) {
                                System.out.println("Modification annulée.");
                                break;
                            }
                            try {
                                int val = Integer.parseInt(nqj);
                                mag.changerQuantite(ej.reference, val);
                            } catch (NumberFormatException ex) {
                                System.out.println("Valeur invalide — aucune modification effectuée.");
                            }
                            break;
                        case 6:
                            System.out.print("Nouvelle taille (ou 0 pour annuler) : ");
                            String ntj = scan.next();
                            if (ntj.equals("0")) {
                                System.out.println("Modification annulée.");
                                break;
                            }
                            mag.changerTaille(ej.reference, ntj);
                            break;
                        case 7:
                            System.out.print("Nouvelle couleur (ou 0 pour annuler) : ");
                            String ncj = scan.next();
                            if (ncj.equals("0")) {
                                System.out.println("Modification annulée.");
                                break;
                            }
                            mag.changerCouleur(ej.reference, ncj);
                            break;
                        default:
                            System.out.println("Choix invalide.");
                    }
                }
                    break;
                case 2: { // protection
                    EquipementsProtectionJoueurs ep = mag.getListeEquipementsProtectionJoueurs().get(index);
                    System.out.println("Réf: " + ep.reference + ", sport: " + ep.sport + ", désignation: "
                            + ep.designation + ", prix: " + ep.prix + ", nombre: " + ep.nombre + ", taille: "
                            + ep.taille + ", couleur: " + ep.couleur + ", niveau: " + ep.niveau);
                    System.out.println(
                            "1. Changer référence\n2. Changer sport\n3. Changer désignation\n4. Changer prix\n5. Changer quantité\n6. Changer taille\n7. Changer couleur\n8. Changer niveau\n0. Retour");
                    System.out.print("Choix : ");
                    int c = readIntSafe(scan);
                    switch (c) {
                        case 0:
                            exitMod = true;
                            break;
                        case 1:
                            System.out.print("Nouvelle référence (ou 0 pour annuler) : ");
                            String newRefP = scan.next();
                            if (newRefP.equals("0")) {
                                System.out.println("Modification annulée.");
                                break;
                            }
                            mag.changerReference(ep.reference, newRefP);
                            exitMod = true;
                            break;
                        case 2:
                            System.out.print("Nouveau sport (ou 0 pour annuler) : ");
                            String nsp = scan.next();
                            if (nsp.equals("0")) {
                                System.out.println("Modification annulée.");
                                break;
                            }
                            mag.changerSport(ep.reference, nsp);
                            break;
                        case 3:
                            System.out.print("Nouvelle désignation (ou 0 pour annuler) : ");
                            String ndp = scan.next();
                            if (ndp.equals("0")) {
                                System.out.println("Modification annulée.");
                                break;
                            }
                            mag.changerDesignation(ep.reference, ndp);
                            break;
                        case 4:
                            System.out.print("Nouveau prix (entier) ou 0 pour annuler : ");
                            String npp = scan.next();
                            if (npp.equals("0")) {
                                System.out.println("Modification annulée.");
                                break;
                            }
                            try {
                                int val = Integer.parseInt(npp);
                                mag.changerPrix(ep.reference, val);
                            } catch (NumberFormatException ex) {
                                System.out.println("Valeur invalide — aucune modification effectuée.");
                            }
                            break;
                        case 5:
                            System.out.print("Nouvelle quantité (entier) ou 0 pour annuler : ");
                            String nqp = scan.next();
                            if (nqp.equals("0")) {
                                System.out.println("Modification annulée.");
                                break;
                            }
                            try {
                                int val = Integer.parseInt(nqp);
                                mag.changerQuantite(ep.reference, val);
                            } catch (NumberFormatException ex) {
                                System.out.println("Valeur invalide — aucune modification effectuée.");
                            }
                            break;
                        case 6:
                            System.out.print("Nouvelle taille (ou 0 pour annuler) : ");
                            String ntp = scan.next();
                            if (ntp.equals("0")) {
                                System.out.println("Modification annulée.");
                                break;
                            }
                            mag.changerTaille(ep.reference, ntp);
                            break;
                        case 7:
                            System.out.print("Nouvelle couleur (ou 0 pour annuler) : ");
                            String ncp = scan.next();
                            if (ncp.equals("0")) {
                                System.out.println("Modification annulée.");
                                break;
                            }
                            mag.changerCouleur(ep.reference, ncp);
                            break;
                        case 8:
                            System.out.print("Nouveau niveau (ou 0 pour annuler) : ");
                            String nlv = scan.next();
                            if (nlv.equals("0")) {
                                System.out.println("Modification annulée.");
                                break;
                            }
                            mag.changerNiveau(ep.reference, nlv);
                            break;
                        default:
                            System.out.println("Choix invalide.");
                    }
                }
                    break;
                default:
                    System.out.println("Type d'équipement inconnu.");
                    return;
            }
        }
    }

    private static int readIntSafe(Scanner scan) {
        while (true) {
            String line;
            try {
                line = scan.nextLine();
            } catch (Exception ex) {
                // fallback to token-based consumption
                while (!scan.hasNextInt()) {
                    System.out.print("Veuillez entrer un entier valide : ");
                    scan.next();
                }
                return scan.nextInt();
            }
            if (line == null)
                continue;
            line = line.trim();
            if (line.isEmpty()) {
                System.out.print("Veuillez entrer un entier valide : ");
                continue;
            }
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException ex) {
                System.out.print("Veuillez entrer un entier valide : ");
            }
        }
    }
}