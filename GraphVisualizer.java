import java.util.*;
import java.io.PrintWriter;
import java.io.IOException;

public class GraphVisualizer {

    /**
     * Membuat file .dot yang menyorot rute tertentu pada graf.
     * @param graph Objek graf yang berisi semua node dan edge.
     * @param path Rute (urutan node) yang ingin disorot.
     * @param filename Nama file output (e.g., "rute.dot").
     */
    public static void generate(graphDjikstra graph, List<Node> path, String filename) {
        Set<Node> pathNodes = new HashSet<>(path);
        Set<String> pathEdges = new HashSet<>();
        for (int i = 0; i < path.size() - 1; i++) {
            pathEdges.add(path.get(i).name + "-" + path.get(i + 1).name);
            pathEdges.add(path.get(i + 1).name + "-" + path.get(i).name); // Untuk undirected
        }

        try (PrintWriter writer = new PrintWriter(filename, "UTF-8")) {
            writer.println("digraph G {");
            writer.println("    layout=neato; overlap=false; splines=true; bgcolor=\"#f7f7f7\";");
            writer.println("    node [shape=box, style=\"rounded,filled\", fontname=\"Helvetica\"];");
            writer.println("    edge [fontname=\"Helvetica\", fontsize=10];");

            // Tulis semua node dengan highlight jika ada di path
            for (Node node : graph.adjacencyList.keySet()) {
                if (pathNodes.contains(node)) {
                    writer.println("    \"" + node.name + "\" [fillcolor=\"#ffcdd2\", fontcolor=\"#b71c1c\", style=\"rounded,filled,bold\"];");
                } else {
                    writer.println("    \"" + node.name + "\" [fillcolor=\"#e0f7fa\", fontcolor=\"#005662\"];");
                }
            }
            
            // Tulis semua edge dengan highlight jika ada di path
            List<String[]> allEdges = getAllEdges();
            for (String[] edgeData : allEdges) {
                String from = edgeData[0];
                String to = edgeData[1];
                String label = edgeData[2];
                boolean isDirected = Boolean.parseBoolean(edgeData[3]);
                String style = "[label=\"" + label + "m\"";

                if (pathEdges.contains(from + "-" + to)) {
                    style += ", color=\"#d32f2f\", penwidth=2.5, fontcolor=\"#d32f2f\"";
                }

                if (!isDirected) {
                    style += ", dir=none";
                }
                style += "];";
                writer.println("    \"" + from + "\" -> \"" + to + "\" " + style);
            }

            writer.println("}");
            System.out.println("\n>> File visualisasi '" + filename + "' telah dibuat!");
            System.out.println(">> Jalankan Graphviz pada file tersebut untuk melihat rute yang disorot.");

        } catch (IOException e) {
            System.out.println("Terjadi error saat menulis file DOT: " + e.getMessage());
        }
    }
    
    // Metode helper privat untuk mendapatkan daftar semua edge.
    private static List<String[]> getAllEdges() {
        return Arrays.asList(
            // --- DIKEMBALIKAN KE KODE AWAL SESUAI PETA DESAIN ---
            new String[]{"Lapak Chicken", "Gerbang Belakang", "600", "false"},
            new String[]{"Lapak Chicken", "Gerbang Samping", "1000", "false"}, // <-- Dikembalikan
            new String[]{"Gerbang Samping", "Fakultas Hukum", "100", "false"},
            // --- Batas Perbaikan ---
            new String[]{"Gerbang Belakang", "Masjid NH", "50", "false"},
            new String[]{"Javanologi", "Fakultas Kedokteran", "170", "false"},
            new String[]{"Masjid NH", "Gedung A FKIP", "100", "false"},
            new String[]{"Gedung A FKIP", "Pertigaan Ke Gor", "80", "false"},
            new String[]{"Pertigaan Ke Gor", "Gor UNS", "280", "false"},
            new String[]{"Pertigaan Ke Gor", "Pascasarjana", "60", "false"},
            new String[]{"Gor UNS", "Javanologi", "90", "false"},
            new String[]{"Gedung A FKIP", "Pascasarjana", "135", "false"},
            new String[]{"Fakultas Kedokteran", "Pascasarjana", "300", "false"},
            new String[]{"Fakultas Kedokteran", "Gedung E Peternakan", "290", "false"},
            new String[]{"Gerbang FMIPA", "Gedung E Peternakan", "80", "false"},
            new String[]{"Pascasarjana", "Puskom UNS", "200", "false"},
            new String[]{"Pascasarjana", "Fakultas Ilmu Budaya", "600", "false"},
            new String[]{"Gedung D FKIP", "Pascasarjana", "400", "false"},
            new String[]{"Gedung D FKIP", "Fakultas Hukum", "260", "false"},
            new String[]{"Fakultas Hukum", "Fakultas Ekonomi Bisnis", "110", "false"},
            new String[]{"Fakultas Hukum", "Fakultas Ilmu Sosial dan Ilmu Politik", "160", "false"},
            new String[]{"Fakultas Ekonomi Bisnis", "FSRD", "150", "false"},
            new String[]{"Fakultas Ilmu Sosial dan Ilmu Politik", "FSRD", "400", "false"},
            new String[]{"FSRD", "Fakultas Ilmu Budaya", "320", "false"},
            new String[]{"FSRD", "UPT Bahasa", "150", "false"},
            new String[]{"FSRD", "Fakultas Teknik", "400", "false"},
            new String[]{"Fakultas Teknik", "Rektorat", "400", "false"},
            new String[]{"UPT Bahasa", "Rektorat", "300", "false"},
            new String[]{"Fakultas Ilmu Budaya", "Auditorium", "150", "false"},
            new String[]{"Fakultas Ilmu Budaya", "UPT Bahasa", "160", "false"},
            new String[]{"Auditorium", "Rektorat", "200", "false"},
            new String[]{"Rektorat", "Danau UNS", "100", "false"},
            new String[]{"Danau UNS", "Gerbang FMIPA", "210", "false"},
            new String[]{"Danau UNS", "Fakultas Pertanian", "140", "false"},
            new String[]{"Danau UNS", "Puskom UNS", "550", "false"},
            new String[]{"Puskom UNS", "Gerbang FMIPA", "400", "false"},
            new String[]{"Gerbang FMIPA", "Gedung C FMIPA", "100", "false"},
            new String[]{"Gedung C FMIPA", "Gedung A FMIPA", "70", "true"},
            new String[]{"Gedung A FMIPA", "Fatisda & Gedung B FMIPA", "50", "true"},
            new String[]{"Fatisda & Gedung B FMIPA", "Puskom UNS", "100", "true"}
        );
    }
}