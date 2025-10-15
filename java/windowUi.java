public class windowUi {

    public windowUi() {
        System.out.println("===========================================");
        System.out.println("=  Bienvenue dans gestionaire de stock !  =");
        System.out.println("===========================================\n");
        System.out.println("TP Info EPF saint nazaire");
    }

    public void Mainwindow() {
        System.out.println("\n======================================");
        System.out.println("*            Menu Principal          *");
        System.out.println("======================================\n");
        System.out.println("1. Ajouter un équipement");
        System.out.println("2. Supprimer un équipement");
        System.out.println("3. Afficher les équipements");
        System.out.println("4. Modifier un équipement");
        System.out.println("5. Rechercher un équipement");
        System.out.println("0. Quitter");
        System.out.print("\nChoix : ");
    }

    public void ajouterEquipement() {
        System.out.println("\n======================================");
        System.out.println("*        Ajouter un équipement       *");
        System.out.println("======================================\n");
        System.out.println("1. Equipement de terrain");
        System.out.println("2. Equipement de joueurs");
        System.out.println("3. Equipement de protection des joueurs");
        System.out.println("0. Retour au menu principal");
        System.out.print("\nChoix : ");
    }

    public void ajouterEquipementTerrain(int step) {
        switch (step) {
            case 0:
                System.out.println("\n======================================");
                System.out.println("* Ajouter un équipement de terrain : *");
                System.out.println("======================================\n");
                break;

            case 1:
                System.out.print("\nreference : ");
                break;
            case 2:
                System.out.print("\nsport : ");
                break;
            case 3:
                System.out.print("\ndesignation : ");
                break;
            case 4:
                System.out.print("\nprix : ");
                break;
            case 5:
                System.out.print("\nnombre : ");
                break;
            case 6:
                System.out.print("\nhauteur : ");
                break;
            case 7:
                System.out.print("\nlargeur : ");
                break;
            case 8:
                System.out.print("\npoids : ");
                break;

            default:
                System.out.println("\n======================================");
                System.out.println("* Ajouter un équipement de terrain : *");
                System.out.println("======================================\n");
                break;
        }

    }

    public void ajouterEquipementJoueurs(int step) {
        switch (step) {
            case 0:
                System.out.println("\n======================================");
                System.out.println("* Ajouter un équipement de joueurs : *");
                System.out.println("======================================\n");
                break;

            case 1:
                System.out.println("\nreference : ");
                break;
            case 2:
                System.out.println("sport : ");
                break;
            case 3:
                System.out.println("designation : ");
                break;
            case 4:
                System.out.println("prix : ");
                break;
            case 5:
                System.out.println("nombre : ");
                break;
            case 6:
                System.out.println("taille : ");
                break;
            case 7:
                System.out.println("couleur : ");
                break;

            default:
                System.out.println("\n======================================");
                System.out.println("* Ajouter un équipement de joueurs : *");
                System.out.println("======================================\n");
                break;
        }

    }

    public void ajouterEquipementProtectionJoueurs(int step) {
        switch (step) {
            case 0:
                System.out.println("\n==============================================");
                System.out.println("* Ajouter un équipement de protection joueurs : *");
                System.out.println("==============================================\n");
                break;

            case 1:
                System.out.println("\nreference : ");
                break;
            case 2:
                System.out.println("sport : ");
                break;
            case 3:
                System.out.println("designation : ");
                break;
            case 4:
                System.out.println("prix : ");
                break;
            case 5:
                System.out.println("nombre : ");
                break;
            case 6:
                System.out.println("taille : ");
                break;
            case 7:
                System.out.println("couleur : ");
                break;
            case 8:
                System.out.println("niveau : ");
                break;

            default:
                System.out.println("\n==============================================");
                System.out.println("* Ajouter un équipement de protection joueurs : *");
                System.out.println("==============================================\n");
                break;
        }

    }

    public void afficherEquipement() {
        System.out.println("\n======================================");
        System.out.println("*        Afficher les équipements     *");
        System.out.println("======================================\n");

        System.out.println("1. Equipement de terrain");
        System.out.println("2. Equipement de joueurs");
        System.out.println("3. Equipement de protection des joueurs");
        System.out.println("0. Retour au menu principal");
        System.out.print("\nChoix : ");
    }

    public void afficherEquipementTerrain() {
        System.out.println("\n======================================");
        System.out.println("* Liste des équipements de terrain : *");
        System.out.println("======================================\n");

    }

    public void afficherEquipementJoueurs() {
        System.out.println("\n======================================");
        System.out.println("* Liste des équipements de joueurs : *");
        System.out.println("======================================\n");
    }

    public void afficherEquipementProtectionJoueurs() {
        System.out.println("\n======================================");
        System.out.println("* Liste des équipements de protection des joueurs : *");
        System.out.println("======================================\n");
    }

    public void supprimerEquipement() {
        System.out.println("\n======================================");
        System.out.println("*        Supprimer un équipement      *");
        System.out.println("======================================\n");
        System.out.println("\n supprimer un équipement (par référence) : ");
    }

    public void quiter() {
        System.out.println("\n======================================");
        System.out.println("*            Au revoir !             *");
        System.out.println("======================================\n");
    }
}
