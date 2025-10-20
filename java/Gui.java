import javax.swing.*;
import javax.swing.table.DefaultTableModel;
// Remplacer import java.awt.* par imports précis pour éviter l'ambiguïté List
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Component;
import java.awt.Point;
import java.awt.Dimension; // ajouté
import java.awt.Container; // ajouté : nécessaire pour la reconstruction dynamique de la sidebar
import java.awt.FlowLayout; // ajouté pour le panneau de filtres
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Gui {

    private JFrame frame;
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private JTextArea helpArea;
    // private JTextArea outputArea; // supprimé
    private File logFile; // nouveau champ pour fichier de log

    // Remplacement des champs DB internes par le magasin existant
    private JTable equipmentTable;
    private DefaultTableModel tableModel;
    // private EquipmentDatabase db; // supprimé
    private magasin mag; // utilisation du backend existant
    // Nouveau : vue en liste pour affichage ligne par ligne
    private JPanel listViewPanel;
    private JScrollPane listScrollPane;

    // Nouveau : configuration dynamique de la sidebar
    private JPanel sidebarPanel;
    private List<String> sidebarItems;
    private final File sidebarCfgFile = new File("sidebar.cfg");
    private static final List<String> ALL_ACTIONS = Arrays.asList(
            "Menu principal", "Gestion stock", "Ajouter", "Afficher", "Modifier", "Rechercher", "Supprimer", "Quitter");

    // Filtres et sélection pour l'affichage
    private JCheckBox chkTerrain;
    private JCheckBox chkJoueurs;
    private JCheckBox chkProtection;
    private JComboBox<String> sortCombo;
    private JTextField refSearchField; // champ texte pour entrer la référence à afficher
    // null = afficher tous les éléments pour les types cochés, sinon liste des
    // références choisies
    private List<String> currentSelectedRefs = null;
    private String currentSortKey = "Référence";

    public Gui() {
        // initialiser magasin (qui charge via FileManager)
        mag = new magasin();
        // initialiser fichier de log
        logFile = new File("gui.log");
        try {
            if (!logFile.exists()) {
                logFile.createNewFile();
            }
        } catch (IOException ex) {
            // si création échoue, on ignore pour ne pas bloquer l'UI
        }
        // charger config sidebar avant création UI
        loadSidebarConfig();
        createAndShow();
    }

    public void createAndShow() {
        frame = new JFrame("Gestionnaire de stock - TP EPF Saint-Nazaire");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1500, 600);
        // fenetre mai maximieser
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(new BorderLayout(10, 10));

        helpArea = new JTextArea();
        helpArea.setEditable(false);
        helpArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        helpArea.setBackground(new Color(245, 245, 245));
        helpArea.setMargin(new Insets(10, 10, 10, 10));
        helpArea.setText("Bienvenue dans le gestionnaire de stock !");
        frame.add(new JScrollPane(helpArea), BorderLayout.NORTH);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Ajouter les différents panneaux
        mainPanel.add(buildMainMenuPanel(), "main");
        mainPanel.add(buildGestionStockPanel(), "stock");
        mainPanel.add(buildAjouterPanel(), "ajouter");
        mainPanel.add(buildAfficherPanel(), "afficher"); // remplacé plus bas
        mainPanel.add(buildModifierPanel(), "modifier");
        mainPanel.add(buildRechercherPanel(), "rechercher");
        mainPanel.add(buildSupprimerPanel(), "supprimer");
        mainPanel.add(buildQuitterPanel(), "quit");

        frame.add(mainPanel, BorderLayout.CENTER);

        // utiliser panneau stocké pour pouvoir le reconstruire dynamiquement
        sidebarPanel = buildSidebar();
        // Barre de raccourcis horizontale placée en haut pour gagner de la largeur
        frame.add(sidebarPanel, BorderLayout.NORTH);

        frame.setVisible(true);
    }

    private JPanel buildSidebar() {
        JPanel side = new JPanel();
        // disposition horizontale : 1 ligne, colonnes variables
        side.setLayout(new GridLayout(1, 0, 6, 6));
        side.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
        // réduire la hauteur afin de libérer de la largeur utile pour le centre
        side.setPreferredSize(new Dimension(0, 48));

        // Construire boutons depuis la configuration
        for (String label : sidebarItems) {
            JButton btn = new JButton(label);
            // plus compact visuellement
            btn.setMargin(new Insets(4, 8, 4, 8));
            btn.addActionListener(e -> {
                switch (label) {
                    case "Menu principal":
                        switchPanel("main", "Menu principal");
                        break;
                    case "Gestion stock":
                        switchPanel("stock", "Gestion de stock");
                        break;
                    case "Ajouter":
                        switchPanel("ajouter", "Ajouter un équipement");
                        break;
                    case "Afficher":
                        switchPanel("afficher", "Afficher les équipements");
                        break;
                    case "Modifier":
                        switchPanel("modifier", "Modifier un équipement");
                        break;
                    case "Rechercher":
                        switchPanel("rechercher", "Rechercher un équipement");
                        break;
                    case "Supprimer":
                        switchPanel("supprimer", "Supprimer un équipement");
                        break;
                    case "Quitter":
                        switchPanel("quit", "Au revoir !");
                        break;
                    default:
                        // action personnalisée non reconnue (ne rien faire)
                }
            });
            side.add(btn);
        }

        // bouton config en bas (toujours présent)
        JButton configBtn = new JButton("Configurer");
        configBtn.setMargin(new Insets(4, 8, 4, 8));
        configBtn.addActionListener(e -> showConfigureDialog());
        side.add(configBtn);

        return side;
    }

    // charge sidebar.cfg ou initialise l'ordre par défaut
    private void loadSidebarConfig() {
        sidebarItems = new ArrayList<>();
        if (sidebarCfgFile.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(sidebarCfgFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    if (!line.isEmpty())
                        sidebarItems.add(line);
                }
            } catch (IOException ex) {
                // fallback à défaut
                sidebarItems = new ArrayList<>(ALL_ACTIONS);
            }
        } else {
            sidebarItems = new ArrayList<>(ALL_ACTIONS);
        }
        // garantir que les éléments sont valides (filtrer inconnus)
        sidebarItems.removeIf(s -> !ALL_ACTIONS.contains(s));
    }

    // sauvegarde la config courante
    private void saveSidebarConfig() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(sidebarCfgFile, false))) {
            for (String s : sidebarItems)
                pw.println(s);
        } catch (IOException ex) {
            log("Échec sauvegarde sidebar.cfg : " + ex.getMessage());
        }
    }

    // reconstruit la sidebar à chaud
    private void rebuildSidebarUI() {
        // retirer ancien panneau ou remplacer directement (maintenir en haut)
        if (sidebarPanel != null) {
            frame.remove(sidebarPanel);
        }
        sidebarPanel = buildSidebar();
        frame.add(sidebarPanel, BorderLayout.NORTH);
        frame.revalidate();
        frame.repaint();
    }

    // Dialogue de configuration : deux listes (disponibles / actifs) avec
    // add/remove/up/down
    private void showConfigureDialog() {
        JDialog dlg = new JDialog(frame, "Configurer les raccourcis", true);
        dlg.setLayout(new BorderLayout(8, 8));

        DefaultListModel<String> availableModel = new DefaultListModel<>();
        DefaultListModel<String> currentModel = new DefaultListModel<>();

        // available = ALL_ACTIONS - sidebarItems
        for (String a : ALL_ACTIONS) {
            if (!sidebarItems.contains(a))
                availableModel.addElement(a);
        }
        // current = sidebarItems
        for (String s : sidebarItems)
            currentModel.addElement(s);

        JList<String> availableList = new JList<>(availableModel);
        JList<String> currentList = new JList<>(currentModel);
        availableList.setVisibleRowCount(8);
        currentList.setVisibleRowCount(8);

        JPanel center = new JPanel(new GridLayout(1, 3, 8, 8));
        center.add(new JScrollPane(availableList));

        JPanel mid = new JPanel(new GridLayout(5, 1, 4, 4));
        JButton addBtn = new JButton("→");
        JButton removeBtn = new JButton("←");
        JButton upBtn = new JButton("↑");
        JButton downBtn = new JButton("↓");
        JButton clearBtn = new JButton("Clear");
        mid.add(addBtn);
        mid.add(removeBtn);
        mid.add(upBtn);
        mid.add(downBtn);
        mid.add(clearBtn);
        center.add(mid);

        center.add(new JScrollPane(currentList));
        dlg.add(center, BorderLayout.CENTER);

        // listeners
        addBtn.addActionListener(e -> {
            List<String> sel = availableList.getSelectedValuesList();
            for (String v : sel) {
                currentModel.addElement(v);
                availableModel.removeElement(v);
            }
        });
        removeBtn.addActionListener(e -> {
            List<String> sel = currentList.getSelectedValuesList();
            for (String v : sel) {
                availableModel.addElement(v);
                currentModel.removeElement(v);
            }
        });
        upBtn.addActionListener(e -> {
            int i = currentList.getSelectedIndex();
            if (i > 0) {
                String v = currentModel.get(i);
                currentModel.remove(i);
                currentModel.add(i - 1, v);
                currentList.setSelectedIndex(i - 1);
            }
        });
        downBtn.addActionListener(e -> {
            int i = currentList.getSelectedIndex();
            if (i >= 0 && i < currentModel.size() - 1) {
                String v = currentModel.get(i);
                currentModel.remove(i);
                currentModel.add(i + 1, v);
                currentList.setSelectedIndex(i + 1);
            }
        });
        clearBtn.addActionListener(e -> {
            while (currentModel.getSize() > 0) {
                availableModel.addElement(currentModel.remove(0));
            }
        });

        JPanel south = new JPanel(new GridLayout(1, 2, 8, 8));
        JButton ok = new JButton("OK");
        JButton cancel = new JButton("Annuler");
        south.add(cancel);
        south.add(ok);
        dlg.add(south, BorderLayout.SOUTH);

        cancel.addActionListener(e -> dlg.dispose());
        ok.addActionListener(e -> {
            // appliquer la nouvelle configuration
            sidebarItems = Collections.list(currentModel.elements());
            // sauvegarder et reconstruire UI
            saveSidebarConfig();
            rebuildSidebarUI();
            dlg.dispose();
        });

        dlg.pack();
        dlg.setLocationRelativeTo(frame);
        dlg.setVisible(true);
    }

    private JPanel buildMainMenuPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Menu principal"));

        JButton stock = new JButton("Gestion de stock");
        JButton commandes = new JButton("Gestion des commandes");
        JButton quit = new JButton("Quitter");

        stock.addActionListener(e -> switchPanel("stock", "Gestion de stock"));
        commandes.addActionListener(e -> log("Ouverture du module commandes (non implémenté)"));
        quit.addActionListener(e -> switchPanel("quit", "Au revoir !"));

        panel.add(stock);
        panel.add(commandes);
        panel.add(quit);
        return panel;
    }

    private JPanel buildGestionStockPanel() {
        // ...existing code... (conserve boutons)
        // on ajoute une action pour Incrémenter/Décrémenter qui opère sur la sélection
        // du tableau
        JPanel panel = new JPanel(new GridLayout(8, 1, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Gestion de stock"));

        String[] options = {
                "Ajouter un équipement",
                "Incrémenter un équipement",
                "Décrémenter un équipement",
                "Supprimer un équipement",
                "Afficher les équipements",
                "Modifier un équipement",
                "Rechercher un équipement",
                "Retour"
        };

        for (String option : options) {
            JButton btn = new JButton(option);
            btn.addActionListener(new StockAction(option));
            panel.add(btn);
        }

        return panel;
    }

    // Remplacement complet du panneau "Afficher" par une liste avec boutons par
    // ligne + filtre en tête
    private JPanel buildAfficherPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Afficher les équipements"));

        // Panneau filtres en haut
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        chkTerrain = new JCheckBox("Terrain", true);
        chkJoueurs = new JCheckBox("Joueurs", true);
        chkProtection = new JCheckBox("Protection", true);
        filterPanel.add(new JLabel("Types :"));
        filterPanel.add(chkTerrain);
        filterPanel.add(chkJoueurs);
        filterPanel.add(chkProtection);

        filterPanel.add(new JLabel("Tri :"));
        sortCombo = new JComboBox<>(new String[] { "Référence", "Désignation", "Prix", "Quantité" });
        filterPanel.add(sortCombo);

        // --- LÉGENDE : couleurs par type ---
        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
        // couleurs choisies
        Color colorTerrain = new Color(255, 0, 0);
        Color colorJoueurs = new Color(0, 255, 0);
        Color colorProtection = new Color(0, 0, 255);
        // helper pour créer un swatch + label
        legendPanel.add(createLegendItem(colorTerrain, "Terrain"));
        legendPanel.add(createLegendItem(colorJoueurs, "Joueurs"));
        legendPanel.add(createLegendItem(colorProtection, "Protection"));
        // ------------------------------------

        // Champ de recherche par référence (saisissez la référence puis "Afficher")
        refSearchField = new JTextField(15);
        JButton applyFilterBtn = new JButton("Afficher");
        applyFilterBtn.addActionListener(e -> {
            String v = refSearchField.getText().trim();
            if (v.isEmpty()) {
                currentSelectedRefs = null;
            } else {
                String[] parts = v.split("[,\\s]+");
                List<String> refs = new ArrayList<>();
                for (String p : parts) {
                    String t = p.trim();
                    if (!t.isEmpty())
                        refs.add(t);
                }
                currentSelectedRefs = refs.isEmpty() ? null : refs;
            }
            currentSortKey = (String) sortCombo.getSelectedItem();
            refreshTable();
        });
        JPanel refsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 2));
        refsPanel.add(new JLabel("Réf à afficher (laisser vide = tout) :"));
        refsPanel.add(refSearchField);
        refsPanel.add(applyFilterBtn);

        // Empiler légende au-dessus du panneau de filtres
        JPanel northStack = new JPanel(new BorderLayout());
        northStack.add(legendPanel, BorderLayout.NORTH);
        northStack.add(filterPanel, BorderLayout.SOUTH);

        // Liste verticale contenant les lignes
        listViewPanel = new JPanel();
        listViewPanel.setLayout(new BoxLayout(listViewPanel, BoxLayout.Y_AXIS));
        listViewPanel.setBackground(Color.WHITE);
        listViewPanel.setOpaque(true);

        listScrollPane = new JScrollPane(listViewPanel);
        listScrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Boutons d'action généraux (conservés)
        JPanel controls = new JPanel(new GridLayout(2, 3, 5, 5));
        JButton addBtn = new JButton("Ajouter");
        JButton editBtn = new JButton("Modifier (par réf)");
        JButton delBtn = new JButton("Supprimer (par réf)");
        JButton incBtn = new JButton("+ Quantité (par réf)");
        JButton decBtn = new JButton("- Quantité (par réf)");
        JButton refreshBtn = new JButton("Rafraîchir");

        addBtn.addActionListener(e -> showAddDialog());
        // actions générales demandant la référence
        editBtn.addActionListener(e -> {
            String ref = JOptionPane.showInputDialog(frame, "Référence à modifier :");
            if (ref != null && !ref.trim().isEmpty()) {
                int[] r = mag.rechercheEquipement(ref.trim());
                if (r != null && r.length >= 2 && r[0] != -1) {
                    int type = r[0], idx = r[1];
                    switch (type) {
                        case 0:
                            showEditDialogTerrain(mag.getListeEquipementsTerrain().get(idx));
                            break;
                        case 1:
                            showEditDialogJoueurs(mag.getListeEquipementsJoueurs().get(idx));
                            break;
                        case 2:
                            showEditDialogProtection(mag.getListeEquipementsProtectionJoueurs().get(idx));
                            break;
                    }
                    refreshTable();
                } else {
                    log("Référence introuvable : " + ref);
                }
            }
        });
        delBtn.addActionListener(e -> {
            String ref = JOptionPane.showInputDialog(frame, "Référence à supprimer :");
            if (ref != null && !ref.trim().isEmpty()) {
                int[] r = mag.rechercheEquipement(ref.trim());
                if (r != null && r.length >= 2 && r[0] != -1) {
                    mag.supprimerEquipement(ref.trim());
                    refreshTable();
                    log("Équipement supprimé : " + ref);
                } else {
                    log("Référence introuvable : " + ref);
                }
            }
        });
        incBtn.addActionListener(e -> {
            String ref = JOptionPane.showInputDialog(frame, "Référence à incrémenter :");
            if (ref != null && !ref.trim().isEmpty()) {
                int[] r = mag.rechercheEquipement(ref.trim());
                if (r != null && r.length >= 2 && r[0] != -1) {
                    mag.incrementerStock(ref.trim(), 1);
                    refreshTable();
                } else {
                    log("Référence introuvable : " + ref);
                }
            }
        });
        decBtn.addActionListener(e -> {
            String ref = JOptionPane.showInputDialog(frame, "Référence à décrémenter :");
            if (ref != null && !ref.trim().isEmpty()) {
                int[] r = mag.rechercheEquipement(ref.trim());
                if (r != null && r.length >= 2 && r[0] != -1) {
                    mag.decrementerStock(ref.trim(), 1);
                    refreshTable();
                } else {
                    log("Référence introuvable : " + ref);
                }
            }
        });
        refreshBtn.addActionListener(e -> {
            // les méthodes mag.* sauvegardent déjà ; on fait juste un rafraîchissement
            // visuel
            refreshTable();
            log("Base rafraîchie.");
        });

        controls.add(addBtn);
        controls.add(editBtn);
        controls.add(delBtn);
        controls.add(incBtn);
        controls.add(decBtn);
        controls.add(refreshBtn);

        // Composer panneau central : légende+filtres en haut, références à gauche,
        // liste au centre
        JPanel topCombined = new JPanel(new BorderLayout(8, 8));
        topCombined.add(northStack, BorderLayout.NORTH);
        topCombined.add(refsPanel, BorderLayout.WEST);

        panel.add(topCombined, BorderLayout.NORTH);
        panel.add(listScrollPane, BorderLayout.CENTER);
        panel.add(controls, BorderLayout.SOUTH);

        // pas d'initialisation de liste multiple — on utilise le champ de recherche
        // remplit la liste
        refreshTable();
        return panel;
    }

    // petit helper pour la légende (swatch + label)
    private JComponent createLegendItem(Color c, String text) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        JPanel sw = new JPanel();
        sw.setBackground(c);
        sw.setPreferredSize(new Dimension(14, 14));
        sw.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        p.add(sw);
        JLabel l = new JLabel(" " + text);
        p.add(l);
        return p;
    }

    // Mise à jour : refreshTable reconstruit maintenant la liste ligne par ligne
    // depuis magasin en tenant compte de currentSelectedRefs et currentSortKey
    private void refreshTable() {
        if (listViewPanel == null)
            return;
        listViewPanel.removeAll();

        // Construit une liste intermédiaire de lignes affichables
        class Row {
            String ref, designation, type;
            int qty, price;
            Object raw;

            Row(String ref, String designation, String type, int qty, int price, Object raw) {
                this.ref = ref;
                this.designation = designation;
                this.type = type;
                this.qty = qty;
                this.price = price;
                this.raw = raw;
            }
        }
        List<Row> rows = new ArrayList<>();

        boolean showTerrain = chkTerrain == null ? true : chkTerrain.isSelected();
        boolean showJoueurs = chkJoueurs == null ? true : chkJoueurs.isSelected();
        boolean showProtection = chkProtection == null ? true : chkProtection.isSelected();

        // helper to check if an item should be shown
        java.util.function.Predicate<String> refFilter = r -> {
            if (currentSelectedRefs == null) {
                // show if its type checkbox is selected
                return (showTerrain && containsInList(mag.getListeEquipementsTerrain(), r)) ||
                        (showJoueurs && containsInList(mag.getListeEquipementsJoueurs(), r)) ||
                        (showProtection && containsInList(mag.getListeEquipementsProtectionJoueurs(), r));
            } else {
                for (String s : currentSelectedRefs) {
                    if (s.equalsIgnoreCase(r))
                        return true;
                }
                return false;
            }
        };

        if (showTerrain) {
            for (EquipementsTerrain et : mag.getListeEquipementsTerrain()) {
                if (et.reference == null)
                    continue;
                if (!refFilter.test(et.reference))
                    continue;
                rows.add(new Row(et.reference, et.designation, "Terrain", et.nombre, et.prix, et));
            }
        }
        if (showJoueurs) {
            for (EquipementsJoueurs ej : mag.getListeEquipementsJoueurs()) {
                if (ej.reference == null)
                    continue;
                if (!refFilter.test(ej.reference))
                    continue;
                rows.add(new Row(ej.reference, ej.designation, "Joueurs", ej.nombre, ej.prix, ej));
            }
        }
        if (showProtection) {
            for (EquipementsProtectionJoueurs ep : mag.getListeEquipementsProtectionJoueurs()) {
                if (ep.reference == null)
                    continue;
                if (!refFilter.test(ep.reference))
                    continue;
                rows.add(new Row(ep.reference, ep.designation, "Protection", ep.nombre, ep.prix, ep));
            }
        }

        // tri selon currentSortKey
        Comparator<Row> comp;
        switch (currentSortKey) {
            case "Désignation":
            case "Designation": // fallback sans accent
                comp = Comparator.comparing(r -> r.designation == null ? "" : r.designation,
                        String.CASE_INSENSITIVE_ORDER);
                break;
            case "Prix":
                comp = Comparator.comparingInt(r -> r.price);
                break;
            case "Quantité":
                comp = Comparator.comparingInt(r -> r.qty);
                break;
            default:
                comp = Comparator.comparing(r -> r.ref == null ? "" : r.ref, String.CASE_INSENSITIVE_ORDER);
        }
        rows.sort(comp);

        // construire UI pour chaque row
        for (Row row : rows) {
            JPanel rpanel = new JPanel(new BorderLayout(8, 0));
            rpanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
            // couleur selon type
            Color bg;
            switch (row.type) {
                case "Terrain":
                    bg = new Color(255, 150, 150);
                    break;
                case "Joueurs":
                    bg = new Color(150, 255, 150);
                    break;
                case "Protection":
                    bg = new Color(150, 150, 255);
                    break;
                default:
                    bg = Color.WHITE;
            }
            rpanel.setBackground(bg);
            // couleur contour
            Color contour;
            switch (row.type) {
                case "Terrain":
                    contour = new Color(200, 0, 0);
                    break;
                case "Joueurs":
                    contour = new Color(0, 200, 0);
                    break;
                case "Protection":
                    contour = new Color(0, 0, 200);
                    break;
                default:
                    contour = Color.GRAY;
            }
            rpanel.setBorder(BorderFactory.createLineBorder(contour, 2));

            rpanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
            rpanel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel desc = new JLabel(String.format("<html><b>%s</b> — %s — %s — Qté: %d — Prix: %d€</html>",
                    row.ref, row.designation, row.type, row.qty, row.price));
            rpanel.add(desc, BorderLayout.CENTER);

            JPanel btns = new JPanel(new GridLayout(1, 4, 4, 0));
            btns.setOpaque(false);
            JButton edit = new JButton("Modifier");
            JButton del = new JButton("Supprimer");
            JButton plus = new JButton("+");
            JButton minus = new JButton("-");

            // listeners : réutilisent les actions existantes (travaillent sur row.raw)
            edit.addActionListener(ae -> {
                Object raw = row.raw;
                if (raw instanceof EquipementsTerrain)
                    showEditDialogTerrain((EquipementsTerrain) raw);
                else if (raw instanceof EquipementsJoueurs)
                    showEditDialogJoueurs((EquipementsJoueurs) raw);
                else if (raw instanceof EquipementsProtectionJoueurs)
                    showEditDialogProtection((EquipementsProtectionJoueurs) raw);
                refreshTable();
            });
            del.addActionListener(ae -> {
                if (JOptionPane.showConfirmDialog(frame, "Supprimer " + row.ref + " ?", "Confirmation",
                        JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                    mag.supprimerEquipement(row.ref);
                    refreshTable();
                    log("Équipement supprimé : " + row.ref);
                }
            });
            plus.addActionListener(ae -> {
                mag.incrementerStock(row.ref, 1);
                refreshTable();
            });
            minus.addActionListener(ae -> {
                mag.decrementerStock(row.ref, 1);
                refreshTable();
            });

            btns.add(edit);
            btns.add(del);
            btns.add(plus);
            btns.add(minus);
            rpanel.add(btns, BorderLayout.EAST);

            rpanel.setName(row.ref);
            listViewPanel.add(rpanel);
        }

        listViewPanel.revalidate();
        listViewPanel.repaint();
    }

    // petit utilitaire pour check d'existence
    private <T extends Equipements> boolean containsInList(java.util.List<T> list, String ref) {
        for (T e : list)
            if (e != null && ref.equals(e.reference))
                return true;
        return false;
    }

    private JPanel buildAjouterPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Ajouter un équipement"));

        JButton terrain = new JButton("Équipement de terrain");
        JButton joueurs = new JButton("Équipement de joueurs");
        JButton protection = new JButton("Équipement de protection");
        JButton retour = new JButton("Retour");

        terrain.addActionListener(e -> showAddDialogWithType("Terrain"));
        joueurs.addActionListener(e -> showAddDialogWithType("Joueurs"));
        protection.addActionListener(e -> showAddDialogWithType("Protection"));
        retour.addActionListener(e -> switchPanel("stock", "Gestion de stock"));

        panel.add(terrain);
        panel.add(joueurs);
        panel.add(protection);
        panel.add(retour);

        return panel;
    }

    private JPanel buildModifierPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Modifier un équipement"));
        panel.add(new JLabel("Choisir un type d’équipement à modifier :"));

        JButton terrain = new JButton("Terrain");
        JButton joueurs = new JButton("Joueurs");
        JButton protection = new JButton("Protection");
        JButton retour = new JButton("Retour");

        terrain.addActionListener(e -> {
            // afficher uniquement les équipements Terrain puis ouvrir l'onglet Afficher
            if (chkTerrain != null)
                chkTerrain.setSelected(true);
            if (chkJoueurs != null)
                chkJoueurs.setSelected(false);
            if (chkProtection != null)
                chkProtection.setSelected(false);
            currentSelectedRefs = null;
            currentSortKey = "Référence";
            switchPanel("afficher", "Afficher les équipements (Terrain)");
            refreshTable();
        });
        joueurs.addActionListener(e -> {
            if (chkTerrain != null)
                chkTerrain.setSelected(false);
            if (chkJoueurs != null)
                chkJoueurs.setSelected(true);
            if (chkProtection != null)
                chkProtection.setSelected(false);
            currentSelectedRefs = null;
            currentSortKey = "Référence";
            switchPanel("afficher", "Afficher les équipements (Joueurs)");
            refreshTable();
        });
        protection.addActionListener(e -> {
            if (chkTerrain != null)
                chkTerrain.setSelected(false);
            if (chkJoueurs != null)
                chkJoueurs.setSelected(false);
            if (chkProtection != null)
                chkProtection.setSelected(true);
            currentSelectedRefs = null;
            currentSortKey = "Référence";
            switchPanel("afficher", "Afficher les équipements (Protection)");
            refreshTable();
        });
        retour.addActionListener(e -> switchPanel("stock", "Gestion de stock"));

        panel.add(terrain);
        panel.add(joueurs);
        panel.add(protection);
        panel.add(retour);

        return panel;
    }

    private JPanel buildRechercherPanel() {
        // remplace le comportement simple par une recherche qui met en évidence la
        // ligne dans la listView
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Rechercher un équipement"));
        JLabel label = new JLabel("Référence :");
        JTextField refField = new JTextField();
        JButton searchBtn = new JButton("Rechercher");

        searchBtn.addActionListener(e -> {
            String ref = refField.getText().trim();
            if (!ref.isEmpty()) {
                int[] found = mag.rechercheEquipement(ref);
                if (found != null && found.length >= 2 && found[0] != -1) {
                    switchPanel("afficher", "Afficher les équipements");
                    refreshTable();
                    selectRowByRef(ref);
                    log("Équipement trouvé : " + ref);
                } else {
                    log("Référence introuvable : " + ref);
                }
            } else {
                log("Veuillez saisir une référence.");
            }
        });

        JPanel top = new JPanel(new GridLayout(1, 2, 5, 5));
        top.add(label);
        top.add(refField);

        panel.add(top, BorderLayout.NORTH);
        panel.add(searchBtn, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel buildSupprimerPanel() {
        // remplace le panneau par un panneau simple qui supprime par référence
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Supprimer un équipement"));
        JLabel label = new JLabel("Référence à supprimer :");
        JTextField refField = new JTextField();
        JButton deleteBtn = new JButton("Supprimer");

        deleteBtn.addActionListener(e -> {
            String ref = refField.getText().trim();
            if (!ref.isEmpty()) {
                int[] r = mag.rechercheEquipement(ref);
                if (r != null && r.length >= 2 && r[0] != -1) {
                    mag.supprimerEquipement(ref);
                    refreshTable();
                    log("Suppression réussie : " + ref);
                } else {
                    log("Référence non trouvée : " + ref);
                }
            } else {
                log("Veuillez saisir une référence.");
            }
        });

        JPanel top = new JPanel(new GridLayout(1, 2, 5, 5));
        top.add(label);
        top.add(refField);

        panel.add(top, BorderLayout.NORTH);
        panel.add(deleteBtn, BorderLayout.SOUTH);

        return panel;
    }

    // Helper : construire ligne pour EquipementsTerrain
    private JPanel buildTerrainRow(EquipementsTerrain et) {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        row.setBackground(Color.WHITE);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel desc = new JLabel(
                String.format("<html><b>%s</b> — %s — %s — Qté: %d — Prix: %d€ — H:%d L:%d P:%d</html>",
                        et.reference, et.designation, et.sport, et.nombre, et.prix, et.hauteur, et.largeur, et.poids));
        row.add(desc, BorderLayout.CENTER);

        JPanel btns = new JPanel(new GridLayout(1, 4, 4, 0));
        btns.setOpaque(false);
        JButton edit = new JButton("Modifier");
        JButton del = new JButton("Supprimer");
        JButton plus = new JButton("+");
        JButton minus = new JButton("-");

        edit.addActionListener(ae -> {
            showEditDialogTerrain(et);
            refreshTable();
        });
        del.addActionListener(ae -> {
            if (JOptionPane.showConfirmDialog(frame, "Supprimer " + et.reference + " ?", "Confirmation",
                    JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                mag.supprimerEquipement(et.reference);
                refreshTable();
                log("Équipement supprimé : " + et.reference);
            }
        });
        plus.addActionListener(ae -> {
            mag.incrementerStock(et.reference, 1);
            refreshTable();
        });
        minus.addActionListener(ae -> {
            mag.decrementerStock(et.reference, 1);
            refreshTable();
        });

        btns.add(edit);
        btns.add(del);
        btns.add(plus);
        btns.add(minus);
        row.add(btns, BorderLayout.EAST);
        row.setName(et.reference);
        return row;
    }

    // Helper : construire ligne pour EquipementsJoueurs
    private JPanel buildJoueurRow(EquipementsJoueurs ej) {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        row.setBackground(Color.WHITE);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel desc = new JLabel(String.format("<html><b>%s</b> — %s — %s — Qté: %d — Prix: %d€ — T:%s C:%s</html>",
                ej.reference, ej.designation, ej.sport, ej.nombre, ej.prix, ej.taille, ej.couleur));
        row.add(desc, BorderLayout.CENTER);

        JPanel btns = new JPanel(new GridLayout(1, 4, 4, 0));
        btns.setOpaque(false);
        JButton edit = new JButton("Modifier");
        JButton del = new JButton("Supprimer");
        JButton plus = new JButton("+");
        JButton minus = new JButton("-");

        edit.addActionListener(ae -> {
            showEditDialogJoueurs(ej);
            refreshTable();
        });
        del.addActionListener(ae -> {
            if (JOptionPane.showConfirmDialog(frame, "Supprimer " + ej.reference + " ?", "Confirmation",
                    JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                mag.supprimerEquipement(ej.reference);
                refreshTable();
                log("Équipement supprimé : " + ej.reference);
            }
        });
        plus.addActionListener(ae -> {
            mag.incrementerStock(ej.reference, 1);
            refreshTable();
        });
        minus.addActionListener(ae -> {
            mag.decrementerStock(ej.reference, 1);
            refreshTable();
        });

        btns.add(edit);
        btns.add(del);
        btns.add(plus);
        btns.add(minus);
        row.add(btns, BorderLayout.EAST);
        row.setName(ej.reference);
        return row;
    }

    // Helper : construire ligne pour EquipementsProtectionJoueurs
    private JPanel buildProtectionRow(EquipementsProtectionJoueurs ep) {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        row.setBackground(Color.WHITE);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel desc = new JLabel(String.format(
                "<html><b>%s</b> — %s — %s — Qté: %d — Prix: %d€ — T:%s C:%s Niv:%s</html>",
                ep.reference, ep.designation, ep.sport, ep.nombre, ep.prix, ep.taille, ep.couleur, ep.niveau));
        row.add(desc, BorderLayout.CENTER);

        JPanel btns = new JPanel(new GridLayout(1, 4, 4, 0));
        btns.setOpaque(false);
        JButton edit = new JButton("Modifier");
        JButton del = new JButton("Supprimer");
        JButton plus = new JButton("+");
        JButton minus = new JButton("-");

        edit.addActionListener(ae -> {
            showEditDialogProtection(ep);
            refreshTable();
        });
        del.addActionListener(ae -> {
            if (JOptionPane.showConfirmDialog(frame, "Supprimer " + ep.reference + " ?", "Confirmation",
                    JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                mag.supprimerEquipement(ep.reference);
                refreshTable();
                log("Équipement supprimé : " + ep.reference);
            }
        });
        plus.addActionListener(ae -> {
            mag.incrementerStock(ep.reference, 1);
            refreshTable();
        });
        minus.addActionListener(ae -> {
            mag.decrementerStock(ep.reference, 1);
            refreshTable();
        });

        btns.add(edit);
        btns.add(del);
        btns.add(plus);
        btns.add(minus);
        row.add(btns, BorderLayout.EAST);
        row.setName(ep.reference);
        return row;
    }

    // selectRowByRef fait défiler jusqu'à la ligne correspondante dans la listView
    private void selectRowByRef(String ref) {
        if (listViewPanel == null || listScrollPane == null)
            return;
        for (Component c : listViewPanel.getComponents()) {
            if (c instanceof JComponent && ref.equals(c.getName())) {
                Rectangle bounds = c.getBounds();
                JViewport vp = listScrollPane.getViewport();
                Point p = SwingUtilities.convertPoint(listViewPanel, bounds.x, bounds.y, vp);
                Rectangle r = new Rectangle(p.x, p.y, bounds.width, bounds.height);
                vp.scrollRectToVisible(r);
                ((JComponent) c).setBorder(BorderFactory.createLineBorder(Color.BLUE, 1));
                return;
            }
        }
    }

    // Remplacer showAddDialogWithType (méthode tronquée) par la version complète
    // ci-dessous
    private void showAddDialogWithType(String type) {
        // champs communs
        JTextField refF = new JTextField();
        JTextField sportF = new JTextField();
        JTextField designationF = new JTextField();
        JTextField prixF = new JTextField("0");
        JTextField nombreF = new JTextField("1");

        java.util.List<JComponent> inputs = new ArrayList<>();
        inputs.add(new JLabel("Référence"));
        inputs.add(refF);
        inputs.add(new JLabel("Sport"));
        inputs.add(sportF);
        inputs.add(new JLabel("Désignation"));
        inputs.add(designationF);
        inputs.add(new JLabel("Prix"));
        inputs.add(prixF);
        inputs.add(new JLabel("Quantité"));
        inputs.add(nombreF);

        // champs spécifiques selon le type
        JTextField fieldA = new JTextField(); // hauteur/taille/...
        JTextField fieldB = new JTextField(); // largeur/couleur/...
        JTextField fieldC = new JTextField(); // poids/niveau/...

        if ("Terrain".equalsIgnoreCase(type)) {
            inputs.add(new JLabel("Hauteur"));
            inputs.add(fieldA);
            inputs.add(new JLabel("Largeur"));
            inputs.add(fieldB);
            inputs.add(new JLabel("Poids"));
            inputs.add(fieldC);
        } else if ("Joueurs".equalsIgnoreCase(type)) {
            inputs.add(new JLabel("Taille"));
            inputs.add(fieldA);
            inputs.add(new JLabel("Couleur"));
            inputs.add(fieldB);
        } else if ("Protection".equalsIgnoreCase(type)) {
            inputs.add(new JLabel("Taille"));
            inputs.add(fieldA);
            inputs.add(new JLabel("Couleur"));
            inputs.add(fieldB);
            inputs.add(new JLabel("Niveau"));
            inputs.add(fieldC);
        }

        int result = JOptionPane.showConfirmDialog(frame, inputs.toArray(new JComponent[0]), "Ajouter équipement",
                JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String ref = refF.getText().trim();
                String sport = sportF.getText().trim();
                String designation = designationF.getText().trim();
                int prix = Integer.parseInt(prixF.getText().trim());
                int nombre = Integer.parseInt(nombreF.getText().trim());
                if (ref.isEmpty() || designation.isEmpty()) {
                    log("Référence et désignation requis.");
                    return;
                }
                if ("Terrain".equalsIgnoreCase(type)) {
                    EquipementsTerrain et = new EquipementsTerrain();
                    et.reference = ref;
                    et.sport = sport;
                    et.designation = designation;
                    et.prix = prix;
                    et.nombre = nombre;
                    et.hauteur = parseIntSafe(fieldA.getText());
                    et.largeur = parseIntSafe(fieldB.getText());
                    et.poids = parseIntSafe(fieldC.getText());
                    mag.addEquipementTerrain(et);
                } else if ("Joueurs".equalsIgnoreCase(type)) {
                    EquipementsJoueurs ej = new EquipementsJoueurs();
                    ej.reference = ref;
                    ej.sport = sport;
                    ej.designation = designation;
                    ej.prix = prix;
                    ej.nombre = nombre;
                    ej.taille = fieldA.getText().trim();
                    ej.couleur = fieldB.getText().trim();
                    mag.addEquipementJoueurs(ej);
                } else { // Protection
                    EquipementsProtectionJoueurs ep = new EquipementsProtectionJoueurs();
                    ep.reference = ref;
                    ep.sport = sport;
                    ep.designation = designation;
                    ep.prix = prix;
                    ep.nombre = nombre;
                    ep.taille = fieldA.getText().trim();
                    ep.couleur = fieldB.getText().trim();
                    ep.niveau = fieldC.getText().trim();
                    mag.addEquipementProtectionJoueurs(ep);
                }
                refreshTable();
                log("Équipement ajouté : " + ref);
            } catch (NumberFormatException ex) {
                log("Prix ou quantité invalide.");
            }
        }
    }

    // méthodes et classes manquantes ajoutées ici
    private void switchPanel(String name, String title) {
        // affiche la carte et met à jour l'aide + log
        if (cardLayout != null && mainPanel != null) {
            cardLayout.show(mainPanel, name);
        }
        if (helpArea != null) {
            helpArea.setText("=== " + title + " ===");
        }
        log("Affichage du panneau : " + title);
    }

    private void log(String text) {
        if (logFile == null)
            return;
        String ts = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(logFile, true)))) {
            pw.println(ts + " - " + text);
        } catch (IOException ex) {
            // fallback silencieux
        }
    }

    private int parseIntSafe(String s) {
        try {
            return Integer.parseInt(s.trim());
        } catch (Exception ex) {
            return 0;
        }
    }

    private JPanel buildQuitterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel bye = new JLabel("Au revoir !", SwingConstants.CENTER);
        bye.setFont(new Font("SansSerif", Font.BOLD, 18));
        panel.add(bye, BorderLayout.CENTER);
        return panel;
    }

    private void showAddDialog() {
        String[] opts = { "Terrain", "Joueurs", "Protection" };
        String sel = (String) JOptionPane.showInputDialog(frame, "Sélectionnez le type :", "Ajouter équipement",
                JOptionPane.PLAIN_MESSAGE, null, opts, opts[0]);
        if (sel != null && !sel.trim().isEmpty()) {
            showAddDialogWithType(sel);
        }
    }

    private void showEditDialogTerrain(EquipementsTerrain et) {
        if (et == null)
            return;
        JTextField designationF = new JTextField(et.designation);
        JTextField sportF = new JTextField(et.sport);
        JTextField prixF = new JTextField(String.valueOf(et.prix));
        JTextField nombreF = new JTextField(String.valueOf(et.nombre));
        JTextField hauteurF = new JTextField(String.valueOf(et.hauteur));
        JTextField largeurF = new JTextField(String.valueOf(et.largeur));
        JTextField poidsF = new JTextField(String.valueOf(et.poids));

        final JComponent[] inputs = new JComponent[] { new JLabel("Référence (non modifiable)"),
                new JLabel(et.reference),
                new JLabel("Désignation"), designationF, new JLabel("Sport"), sportF, new JLabel("Prix"), prixF,
                new JLabel("Quantité"), nombreF, new JLabel("Hauteur"), hauteurF, new JLabel("Largeur"), largeurF,
                new JLabel("Poids"), poidsF };
        int result = JOptionPane.showConfirmDialog(frame, inputs, "Modifier équipement (Terrain)",
                JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                et.designation = designationF.getText().trim();
                et.sport = sportF.getText().trim();
                et.prix = Integer.parseInt(prixF.getText().trim());
                et.nombre = Integer.parseInt(nombreF.getText().trim());
                et.hauteur = parseIntSafe(hauteurF.getText());
                et.largeur = parseIntSafe(largeurF.getText());
                et.poids = parseIntSafe(poidsF.getText());
                mag.changerDesignation(et.reference, et.designation);
                mag.changerSport(et.reference, et.sport);
                mag.changerPrix(et.reference, et.prix);
                mag.changerQuantite(et.reference, et.nombre);
                mag.changerHauteur(et.reference, et.hauteur);
                mag.changerLargeur(et.reference, et.largeur);
                mag.changerPoids(et.reference, et.poids);
                log("Équipement modifié : " + et.reference);
            } catch (NumberFormatException ex) {
                log("Valeur numérique invalide.");
            }
        }
    }

    private void showEditDialogJoueurs(EquipementsJoueurs ej) {
        if (ej == null)
            return;
        JTextField designationF = new JTextField(ej.designation);
        JTextField sportF = new JTextField(ej.sport);
        JTextField prixF = new JTextField(String.valueOf(ej.prix));
        JTextField nombreF = new JTextField(String.valueOf(ej.nombre));
        JTextField tailleF = new JTextField(ej.taille);
        JTextField couleurF = new JTextField(ej.couleur);

        final JComponent[] inputs = new JComponent[] { new JLabel("Référence (non modifiable)"),
                new JLabel(ej.reference),
                new JLabel("Désignation"), designationF, new JLabel("Sport"), sportF, new JLabel("Prix"), prixF,
                new JLabel("Quantité"), nombreF, new JLabel("Taille"), tailleF, new JLabel("Couleur"), couleurF };
        int result = JOptionPane.showConfirmDialog(frame, inputs, "Modifier équipement (Joueurs)",
                JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                ej.designation = designationF.getText().trim();
                ej.sport = sportF.getText().trim();
                ej.prix = Integer.parseInt(prixF.getText().trim());
                ej.nombre = Integer.parseInt(nombreF.getText().trim());
                ej.taille = tailleF.getText().trim();
                ej.couleur = couleurF.getText().trim();
                mag.changerDesignation(ej.reference, ej.designation);
                mag.changerSport(ej.reference, ej.sport);
                mag.changerPrix(ej.reference, ej.prix);
                mag.changerQuantite(ej.reference, ej.nombre);
                mag.changerTaille(ej.reference, ej.taille);
                mag.changerCouleur(ej.reference, ej.couleur);
                log("Équipement modifié : " + ej.reference);
            } catch (NumberFormatException ex) {
                log("Valeur numérique invalide.");
            }
        }
    }

    private void showEditDialogProtection(EquipementsProtectionJoueurs ep) {
        if (ep == null)
            return;
        JTextField designationF = new JTextField(ep.designation);
        JTextField sportF = new JTextField(ep.sport);
        JTextField prixF = new JTextField(String.valueOf(ep.prix));
        JTextField nombreF = new JTextField(String.valueOf(ep.nombre));
        JTextField tailleF = new JTextField(ep.taille);
        JTextField couleurF = new JTextField(ep.couleur);
        JTextField niveauF = new JTextField(ep.niveau);

        final JComponent[] inputs = new JComponent[] { new JLabel("Référence (non modifiable)"),
                new JLabel(ep.reference),
                new JLabel("Désignation"), designationF, new JLabel("Sport"), sportF, new JLabel("Prix"), prixF,
                new JLabel("Quantité"), nombreF, new JLabel("Taille"), tailleF, new JLabel("Couleur"), couleurF,
                new JLabel("Niveau"), niveauF };
        int result = JOptionPane.showConfirmDialog(frame, inputs, "Modifier équipement (Protection)",
                JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                ep.designation = designationF.getText().trim();
                ep.sport = sportF.getText().trim();
                ep.prix = Integer.parseInt(prixF.getText().trim());
                ep.nombre = Integer.parseInt(nombreF.getText().trim());
                ep.taille = tailleF.getText().trim();
                ep.couleur = couleurF.getText().trim();
                ep.niveau = niveauF.getText().trim();
                mag.changerDesignation(ep.reference, ep.designation);
                mag.changerSport(ep.reference, ep.sport);
                mag.changerPrix(ep.reference, ep.prix);
                mag.changerQuantite(ep.reference, ep.nombre);
                mag.changerTaille(ep.reference, ep.taille);
                mag.changerCouleur(ep.reference, ep.couleur);
                mag.changerNiveau(ep.reference, ep.niveau);
                log("Équipement modifié : " + ep.reference);
            } catch (NumberFormatException ex) {
                log("Valeur numérique invalide.");
            }
        }
    }

    private class StockAction implements ActionListener {
        private String option;

        public StockAction(String option) {
            this.option = option;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            log("Action : " + option);
            switch (option) {
                case "Ajouter un équipement":
                    switchPanel("ajouter", "Ajouter un équipement");
                    break;
                case "Afficher les équipements":
                    switchPanel("afficher", "Afficher les équipements");
                    break;
                case "Modifier un équipement":
                    switchPanel("modifier", "Modifier un équipement");
                    break;
                case "Rechercher un équipement":
                    switchPanel("rechercher", "Rechercher un équipement");
                    break;
                case "Supprimer un équipement":
                    switchPanel("supprimer", "Supprimer un équipement");
                    break;
                case "Retour":
                    switchPanel("main", "Menu principal");
                    break;
                default:
                    log("Fonction non implémentée pour : " + option);
            }
        }
    }

    // Ajout du point d'entrée pour lancer l'interface Swing
    public static void main(String[] args) {
        // Permet d'appeler Gui.main depuis GuiLauncher ou tp
        javax.swing.SwingUtilities.invokeLater(() -> new Gui());
    }
}