import java.util.Scanner;

public class tp {

    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);
        magasin mag = new magasin();
        windowUi mw = new windowUi();

        while (true) {

            mw.Mainwindow();
            int choix = scan.nextInt();

            switch (choix) {
                case 0:
                    mw.quiter();
                    System.exit(0);
                    break;

                case 1:
                    int exitAjout = 0;
                    while (exitAjout == 0) {

                        mw.ajouterEquipement();
                        int choixAjout = scan.nextInt();

                        switch (choixAjout) {
                            case 0:
                                exitAjout = 1;
                                break;
                            case 1:
                                EquipementsTerrain et = saisirEquipementTerrain(scan, mw);
                                mag.addEquipementTerrain(et);
                                System.out.println("\nÉquipement de terrain ajouté avec succès !");
                                exitAjout = 1;

                                break;
                            case 2:
                                EquipementsJoueurs ej = saisirEquipementJoueurs(scan, mw);
                                mag.addEquipementJoueurs(ej);
                                System.out.println("\nÉquipement de joueurs ajouté avec succès !");
                                exitAjout = 1;

                                break;
                            case 3:
                                EquipementsProtectionJoueurs ep = saisirEquipementProtectionJoueurs(scan, mw);
                                mag.addEquipementProtectionJoueurs(ep);
                                System.out.println("\nÉquipement de protection de joueurs ajouté avec succès !");
                                exitAjout = 1;

                                break;

                            default:
                                System.out.println("\nChoix invalide. Veuillez réessayer.");
                                break;
                        }
                    }
                    break;
                case 2:
                    // suppr equip $$

                    mw.supprimerEquipement();

                    break;

                case 3:
                    int exitAff = 0;
                    while (exitAff == 0) {
                        mw.afficherEquipement();
                        int choixAff = scan.nextInt();

                        switch (choixAff) {
                            case 1:
                                mw.afficherEquipementTerrain();
                                mag.showEquipementsTerrain();
                                exitAff = 1;
                                break;

                            case 2:
                                mw.afficherEquipementJoueurs();
                                mag.showEquipementsJoueurs();
                                exitAff = 1;
                                break;

                            case 3:
                                mw.afficherEquipementProtectionJoueurs();
                                mag.showEquipementsProtectionJoueurs();
                                exitAff = 1;
                                break;

                            default:
                                System.out.println("\nChoix invalide. Veuillez réessayer.");
                                break;
                        }
                    }
                    break;

                case 4:
                    // modif equip $$
                    break;

                case 5:
                    // recherche $$
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

    private static int readIntSafe(Scanner scan) {
        while (!scan.hasNextInt()) {
            System.out.print("Veuillez entrer un entier valide : ");
            scan.next();
        }
        return scan.nextInt();
    }
}