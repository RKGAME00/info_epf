import java.io.*;
import java.util.*;

public class FileManager {
    private final File dataDir;
    private final File fileTerrain;
    private final File fileJoueurs;
    private final File fileProtection;

    // ========
    /* header */
    // ========

    private static final String HEADER_TERRAINS = "reference,sport,designation,prix,nombre,hauteur,largeur,poids";
    private static final String HEADER_JOUEURS = "reference,sport,designation,prix,nombre,taille,couleur";
    private static final String HEADER_PROTECTIONS = "reference,sport,designation,prix,nombre,taille,couleur,niveau";

    public FileManager(String dirName) {
        dataDir = new File(dirName);
        fileTerrain = new File(dataDir, "EquipementsTerrain.csv");
        fileJoueurs = new File(dataDir, "EquipementsJoueurs.csv");
        fileProtection = new File(dataDir, "EquipementsProtectionJoueurs.csv");
        ensureDataDir();
    }

    private void ensureDataDir() {
        if (!dataDir.exists())
            dataDir.mkdirs();
        try {
            if (!fileTerrain.exists()) {
                // create and write header
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileTerrain, false))) {
                    bw.write(HEADER_TERRAINS);
                    bw.newLine();
                }
            }
            if (!fileJoueurs.exists()) {
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileJoueurs, false))) {
                    bw.write(HEADER_JOUEURS);
                    bw.newLine();
                }
            }
            if (!fileProtection.exists()) {
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileProtection, false))) {
                    bw.write(HEADER_PROTECTIONS);
                    bw.newLine();
                }
            }
        } catch (IOException ex) {
            System.out.println("Erreur création fichiers données: " + ex.getMessage());
        }
    }

    // Terrain
    public List<EquipementsTerrain> loadTerrains() {
        List<EquipementsTerrain> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileTerrain))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty())
                    continue;
                String[] p = line.split(",");
                // skip header if present
                if (firstLine && p.length > 0 && p[0].equalsIgnoreCase("reference")) {
                    firstLine = false;
                    continue;
                }
                firstLine = false;
                EquipementsTerrain et = new EquipementsTerrain();
                et.reference = p.length > 0 ? p[0] : "";
                et.sport = p.length > 1 ? p[1] : "";
                et.designation = p.length > 2 ? p[2] : "";
                et.prix = p.length > 3 ? parseIntSafe(p[3]) : 0;
                et.nombre = p.length > 4 ? parseIntSafe(p[4]) : 0;
                et.hauteur = p.length > 5 ? parseIntSafe(p[5]) : 0;
                et.largeur = p.length > 6 ? parseIntSafe(p[6]) : 0;
                et.poids = p.length > 7 ? parseIntSafe(p[7]) : 0;
                list.add(et);
            }
        } catch (IOException ex) {
            System.out.println("Erreur lecture terrains: " + ex.getMessage());
        }
        return list;
    }

    public void saveTerrains(List<EquipementsTerrain> list) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileTerrain, false))) {
            // write header
            bw.write(HEADER_TERRAINS);
            bw.newLine();
            for (EquipementsTerrain et : list) {
                bw.write(String.join(",",
                        safe(et.reference), safe(et.sport), safe(et.designation), String.valueOf(et.prix),
                        String.valueOf(et.nombre), String.valueOf(et.hauteur), String.valueOf(et.largeur),
                        String.valueOf(et.poids)));
                bw.newLine();
            }
        } catch (IOException ex) {
            System.out.println("Erreur écriture terrains: " + ex.getMessage());
        }
    }

    // Joueurs
    public List<EquipementsJoueurs> loadJoueurs() {
        List<EquipementsJoueurs> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileJoueurs))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty())
                    continue;
                String[] p = line.split(",");
                if (firstLine && p.length > 0 && p[0].equalsIgnoreCase("reference")) {
                    firstLine = false;
                    continue;
                }
                firstLine = false;
                EquipementsJoueurs ej = new EquipementsJoueurs();
                ej.reference = p.length > 0 ? p[0] : "";
                ej.sport = p.length > 1 ? p[1] : "";
                ej.designation = p.length > 2 ? p[2] : "";
                ej.prix = p.length > 3 ? parseIntSafe(p[3]) : 0;
                ej.nombre = p.length > 4 ? parseIntSafe(p[4]) : 0;
                ej.taille = p.length > 5 ? p[5] : "";
                ej.couleur = p.length > 6 ? p[6] : "";
                list.add(ej);
            }
        } catch (IOException ex) {
            System.out.println("Erreur lecture joueurs: " + ex.getMessage());
        }
        return list;
    }

    public void saveJoueurs(List<EquipementsJoueurs> list) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileJoueurs, false))) {
            // write header
            bw.write(HEADER_JOUEURS);
            bw.newLine();
            for (EquipementsJoueurs ej : list) {
                bw.write(String.join(",", safe(ej.reference), safe(ej.sport), safe(ej.designation),
                        String.valueOf(ej.prix), String.valueOf(ej.nombre), safe(ej.taille), safe(ej.couleur)));
                bw.newLine();
            }
        } catch (IOException ex) {
            System.out.println("Erreur écriture joueurs: " + ex.getMessage());
        }
    }

    // Protections
    public List<EquipementsProtectionJoueurs> loadProtections() {
        List<EquipementsProtectionJoueurs> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileProtection))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty())
                    continue;
                String[] p = line.split(",");
                if (firstLine && p.length > 0 && p[0].equalsIgnoreCase("reference")) {
                    firstLine = false;
                    continue;
                }
                firstLine = false;
                EquipementsProtectionJoueurs ep = new EquipementsProtectionJoueurs();
                ep.reference = p.length > 0 ? p[0] : "";
                ep.sport = p.length > 1 ? p[1] : "";
                ep.designation = p.length > 2 ? p[2] : "";
                ep.prix = p.length > 3 ? parseIntSafe(p[3]) : 0;
                ep.nombre = p.length > 4 ? parseIntSafe(p[4]) : 0;
                ep.taille = p.length > 5 ? p[5] : "";
                ep.couleur = p.length > 6 ? p[6] : "";
                ep.niveau = p.length > 7 ? p[7] : "";
                list.add(ep);
            }
        } catch (IOException ex) {
            System.out.println("Erreur lecture protections: " + ex.getMessage());
        }
        return list;
    }

    public void saveProtections(List<EquipementsProtectionJoueurs> list) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileProtection, false))) {
            // write header
            bw.write(HEADER_PROTECTIONS);
            bw.newLine();
            for (EquipementsProtectionJoueurs ep : list) {
                bw.write(String.join(",", safe(ep.reference), safe(ep.sport), safe(ep.designation),
                        String.valueOf(ep.prix), String.valueOf(ep.nombre), safe(ep.taille), safe(ep.couleur),
                        safe(ep.niveau)));
                bw.newLine();
            }
        } catch (IOException ex) {
            System.out.println("Erreur écriture protections: " + ex.getMessage());
        }
    }

    // utilitaires
    private int parseIntSafe(String s) {
        try {
            return Integer.parseInt(s.trim());
        } catch (Exception ex) {
            return 0;
        }
    }

    private String safe(String s) {
        if (s == null)
            return "";
        return s.replaceAll("\r|\n|,", " ");
    }
}
