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
                                EquipementsTerrain et = new EquipementsTerrain();
                                mw.ajouterEquipementTerrain(0);
                                mw.ajouterEquipementTerrain(1);
                                et.reference = scan.next();
                                mw.ajouterEquipementTerrain(2);
                                et.sport = scan.next();
                                mw.ajouterEquipementTerrain(3);
                                et.designation = scan.next();
                                mw.ajouterEquipementTerrain(4);
                                et.prix = scan.nextInt();
                                mw.ajouterEquipementTerrain(5);
                                et.nombre = scan.nextInt();
                                mw.ajouterEquipementTerrain(6);
                                et.hauteur = scan.nextInt();
                                mw.ajouterEquipementTerrain(7);
                                et.largeur = scan.nextInt();
                                mw.ajouterEquipementTerrain(8);
                                et.poids = scan.nextInt();
                                mag.addEquipementTerrain(et);
                                System.out.println("\nÉquipement de terrain ajouté avec succès !");
                                exitAjout = 1;

                                break;
                            case 2:
                                // equip joueurs $
                                break;
                            case 3:
                                // equip protec joueurs $
                                break;

                            default:
                                System.out.println("\nChoix invalide. Veuillez réessayer.");
                                break;
                        }
                    }
                    break;
                case 2:
                    // suppr equip $$
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
}