public class windowUi {

    public windowUi() {
        System.out.println("===========================================");
        System.out.println("=  Bienvenue dans gestionaire de stock !  =");
        System.out.println("===========================================\n");
        System.out.println("TP Info EPF saint nazaire");
    }

    public void mainwindow() {
        System.out.println("\n======================================");
        System.out.println("*        Menu principal              *");
        System.out.println("======================================\n");
        System.out.println("1. gestion de stock");
        System.out.println("2. gestion des commandes");
        System.out.println("0. Quitter");
        System.out.print("\nChoix : ");
    }

    public void gestionStock() {
        System.out.println("\n======================================");
        System.out.println("*            Gestion de stock        *");
        System.out.println("======================================\n");
        System.out.println("1. Ajouter un équipement");
        System.out.println("2. Incrémenter un équipement");
        System.out.println("3. Décrémenter un équipement");
        System.out.println("4. Supprimer un équipement");
        System.out.println("5. Afficher les équipements");
        System.out.println("6. Modifier un équipement");
        System.out.println("7. Rechercher un équipement");
        System.out.println("0. retour");
        System.out.print("\nChoix : ");
    }

    public void ajouterEquipement() {
        System.out.println("\n======================================");
        System.out.println("*        Ajouter un équipement       *");
        System.out.println("======================================\n");
        System.out.println("1. Equipement de terrain");
        System.out.println("2. Equipement de joueurs");
        System.out.println("3. Equipement de protection des joueurs");
        System.out.println("0. retour");
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
                System.out.print("\ntaille : ");
                break;
            case 7:
                System.out.print("\ncouleur : ");
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
                System.out.print("\ntaille : ");
                break;
            case 7:
                System.out.print("\ncouleur : ");
                break;
            case 8:
                System.out.print("\nniveau : ");
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
        System.out.println("4. Tout afficher");
        System.out.println("0. retour");
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
        System.out.println("\n====================================================");
        System.out.println("* Liste des équipements de protection des joueurs : *");
        System.out.println("====================================================\n");
    }

    public void supprimerEquipement() {
        System.out.println("\n======================================");
        System.out.println("*        Supprimer un équipement      *");
        System.out.println("======================================\n");
        System.out.print("\n supprimer un équipement (par référence) : ");
    }

    public void incrimenterEquipement() {
        System.out.println("\n======================================");
        System.out.println("*      Incrémenter un équipement     *");
        System.out.println("======================================\n");
        System.out.print("Référence de l'équipement à incrémenter (ou 0 pour annuler) : ");
    }

    public void decrementerEquipement() {
        System.out.println("\n======================================");
        System.out.println("*      Décrémenter un équipement     *");
        System.out.println("======================================\n");
        System.out.print("Référence de l'équipement à décrémenter (ou 0 pour annuler) : ");
    }

    public void rechercherEquipement() {
        System.out.println("\n======================================");
        System.out.println("*        Rechercher un équipement     *");
        System.out.println("======================================\n");
        System.out.print("\n rechercher un équipement (par référence) : ");
    }

    public void modifierEquipementTerrain(int step) {
        switch (step) {
            case 0:
                System.out.println("\n======================================");
                System.out.println("* Modifier un équipement de terrain : *");
                System.out.println("======================================\n");
                break;

            case 1:
                System.out.print("\nreference (clé, non modifiable) : ");
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
                System.out.println("* Modifier un équipement de terrain : *");
                System.out.println("======================================\n");
                break;
        }

    }

    public void modifierEquipementJoueurs(int step) {
        switch (step) {
            case 0:
                System.out.println("\n======================================");
                System.out.println("* Modifier un équipement de joueurs : *");
                System.out.println("======================================\n");
                break;

            case 1:
                System.out.print("\nreference (clé, non modifiable) : ");
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
                System.out.print("\ntaille : ");
                break;
            case 7:
                System.out.print("\ncouleur : ");
                break;

            default:
                System.out.println("\n======================================");
                System.out.println("* Modifier un équipement de joueurs : *");
                System.out.println("======================================\n");
                break;
        }

    }

    public void modifierEquipementProtectionJoueurs(int step) {
        switch (step) {
            case 0:
                System.out.println("\n==============================================");
                System.out.println("* Modifier un équipement de protection joueurs : *");
                System.out.println("==============================================\n");
                break;

            case 1:
                System.out.print("\nreference (clé, non modifiable) : ");
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
                System.out.print("\ntaille : ");
                break;
            case 7:
                System.out.print("\ncouleur : ");
                break;
            case 8:
                System.out.print("\nniveau : ");
                break;

            default:
                System.out.println("\n==============================================");
                System.out.println("* Modifier un équipement de protection joueurs : *");
                System.out.println("==============================================\n");
                break;
        }

    }

    public void quiter() {
        System.out.println("\n======================================");
        System.out.println("*            Au revoir !             *");
        System.out.println("======================================\n");
    }
}
