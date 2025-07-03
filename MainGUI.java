import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;
import javax.swing.*;

public class MainGUI extends JFrame {

    private static final Color BG_COLOR = new Color(254, 250, 246); // Off-white/beige
    private static final Color PRIMARY_ORANGE = new Color(245, 130, 49);
    private static final Color TEXT_COLOR = new Color(51, 51, 51);
    private static final Color BORDER_COLOR = new Color(220, 220, 220);
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font VALUE_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);

    private graphDjikstra graph;
    private Node lapakChicken;
    private List<Node> tujuanList;
    private JComboBox<Node> singleDestinationComboBox;
    private List<JCheckBox> destinationCheckboxes;
    private JTextArea outputArea;
    private JLabel totalDistanceLabel;
    private JLabel fuelCostLabel;
    private JLabel totalOngkirLabel;

    public MainGUI() {
        setTitle("Chick Route - Delivery App");
        setSize(800, 750); // Sedikit lebih tinggi
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initializeGraph();

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(BG_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabbedPane.setBackground(BG_COLOR);

        JPanel singleDestinationPanel = createSingleDestinationInputPanel();
        JPanel multiDestinationPanel = createMultiDestinationInputPanel();

        tabbedPane.addTab("  Satu Tujuan  ", singleDestinationPanel);
        tabbedPane.addTab("  Multi Tujuan (TSP)  ", multiDestinationPanel);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        mainPanel.add(createSharedOutputPanel(), BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(BG_COLOR);
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        ImageIcon chickenIcon = createIcon("chicken_icon.png", 50, 50);
        if (chickenIcon != null) {
            headerPanel.add(new JLabel(chickenIcon));
        }

        JLabel titleLabel = new JLabel("Chick Route Delivery");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(PRIMARY_ORANGE);
        headerPanel.add(titleLabel);
        
        return headerPanel;
    }
    
    private JPanel createSingleDestinationInputPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));
        panel.setBackground(BG_COLOR); 
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BORDER_COLOR), "Pilih Tujuan Tunggal"));
        
        JLabel tujuanLabel = new JLabel("Tujuan:");
        tujuanLabel.setFont(LABEL_FONT); 
        tujuanLabel.setForeground(TEXT_COLOR);

        singleDestinationComboBox = new JComboBox<>(tujuanList.toArray(new Node[0]));
        singleDestinationComboBox.setPreferredSize(new Dimension(250, 35));
        singleDestinationComboBox.setFont(VALUE_FONT); // --- STYLE ---

        JButton calculateSingleButton = new JButton("Hitung Rute");
        // --- STYLE ---
        styleButton(calculateSingleButton);
        // Diasumsikan ada file 'route_icon.png'
        calculateSingleButton.setIcon(createIcon("route_icon.png", 20, 20));

        calculateSingleButton.addActionListener(e -> calculateSingleRoute());

        panel.add(tujuanLabel);
        panel.add(singleDestinationComboBox);
        panel.add(calculateSingleButton);
        
        return panel;
    }

    private JPanel createMultiDestinationInputPanel() {
        JPanel panel = new JPanel(new BorderLayout(10,10));
        panel.setBackground(BG_COLOR); // --- STYLE ---
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(BORDER_COLOR), "Pilih Beberapa Tujuan (Maks 6)"));

        JPanel checkboxPanel = new JPanel();
        checkboxPanel.setLayout(new BoxLayout(checkboxPanel, BoxLayout.Y_AXIS));
        checkboxPanel.setBackground(Color.WHITE); // --- STYLE ---
        
        destinationCheckboxes = new ArrayList<>();
        for (Node tujuan : tujuanList) {
            JCheckBox checkBox = new JCheckBox(tujuan.name);
            checkBox.setFont(VALUE_FONT); // --- STYLE ---
            checkBox.setBackground(Color.WHITE);
            checkBox.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            destinationCheckboxes.add(checkBox);
            checkboxPanel.add(checkBox);
        }
        
        JScrollPane listScroller = new JScrollPane(checkboxPanel);
        listScroller.setBorder(BorderFactory.createLineBorder(BORDER_COLOR)); // --- STYLE ---
        
        JButton calculateMultiButton = new JButton("Hitung Rute TSP");
        // --- STYLE ---
        styleButton(calculateMultiButton);
        // Diasumsikan ada file 'route_icon.png'
        calculateMultiButton.setIcon(createIcon("route_icon.png", 20, 20));

        calculateMultiButton.addActionListener(e -> calculateMultiRoute());
        
        panel.add(listScroller, BorderLayout.CENTER);
        panel.add(calculateMultiButton, BorderLayout.SOUTH);

        return panel;
    }
    
    private JPanel createSharedOutputPanel() {
        // ... (Logika sama, hanya penambahan style)
        JPanel outputPanel = new JPanel(new BorderLayout(10, 10));
        outputPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        outputPanel.setBackground(BG_COLOR);
        
        outputArea = new JTextArea(10, 50);
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        outputArea.setBorder(BorderFactory.createEmptyBorder(10,10,10,10)); // Padding
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(BORDER_COLOR), "Rute Optimal"));
        
        JPanel summaryPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        summaryPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(BORDER_COLOR), "Ringkasan Biaya"));
        summaryPanel.setBackground(BG_COLOR);
        
        totalDistanceLabel = new JLabel("0 meter");
        totalDistanceLabel.setFont(VALUE_FONT);
        fuelCostLabel = new JLabel("Rp 0");
        fuelCostLabel.setFont(VALUE_FONT);
        totalOngkirLabel = new JLabel("Rp 0");
        totalOngkirLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        totalOngkirLabel.setForeground(PRIMARY_ORANGE);

        JLabel jarakTitle = new JLabel("  Total Jarak Tempuh:");
        jarakTitle.setFont(LABEL_FONT);
        JLabel bensinTitle = new JLabel("  Estimasi Biaya Bensin (PP):");
        bensinTitle.setFont(LABEL_FONT);
        JLabel ongkirTitle = new JLabel("  Total Ongkir:");
        ongkirTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));

        summaryPanel.add(jarakTitle);
        summaryPanel.add(totalDistanceLabel);
        summaryPanel.add(bensinTitle);
        summaryPanel.add(fuelCostLabel);
        summaryPanel.add(ongkirTitle);
        summaryPanel.add(totalOngkirLabel);

        outputPanel.add(scrollPane, BorderLayout.CENTER);
        outputPanel.add(summaryPanel, BorderLayout.SOUTH);
        
        return outputPanel;
    }

    private void styleButton(JButton button) {
        button.setFont(BUTTON_FONT);
        button.setBackground(PRIMARY_ORANGE);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_ORANGE.darker()),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
    }

    private ImageIcon createIcon(String path, int width, int height) {

        File iconFile = new File(path);
        if (!iconFile.exists()) {
            System.err.println("Peringatan: File ikon tidak ditemukan di " + path);
            return null;
        }
        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage();
        Image resizedImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImg);
    }
    
    private void calculateSingleRoute() {
        Node tujuan = (Node) singleDestinationComboBox.getSelectedItem();
        if (tujuan == null) return;
        
        Map<Node, Node> prev = new HashMap<>();
        Map<Node, Integer> distances = graph.dijkstra(lapakChicken, prev);
        List<Node> path = graph.getShortestPath(lapakChicken, tujuan, prev);
        
        if (distances.get(tujuan) == Integer.MAX_VALUE) {
            updateOutput(Collections.emptyList(), 0);
            outputArea.setText("Rute tidak ditemukan ke " + tujuan.name);
        } else {
            int totalJarak = distances.get(tujuan);
            updateOutput(path, totalJarak);
            String dotFileName = "rute_satu_tujuan.dot";
            GraphVisualizer.generate(graph, path, dotFileName);
            displayVisualization(dotFileName);
        }
    }
    
    private void calculateMultiRoute() {
        List<Node> selectedDestinations = new ArrayList<>();
        for (JCheckBox checkbox : destinationCheckboxes) {
            if (checkbox.isSelected()) {
                for (Node tujuanNode : tujuanList) {
                    if (tujuanNode.name.equals(checkbox.getText())) {
                        selectedDestinations.add(tujuanNode);
                        break;
                    }
                }
            }
        }

        if (selectedDestinations.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih minimal satu tujuan.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (selectedDestinations.size() > 6) {
            JOptionPane.showMessageDialog(this, "Maksimal 6 tujuan yang bisa dipilih.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Node> bestPath = tspAlgorithm.tspBruteForce(lapakChicken, selectedDestinations, graph);
        int totalJarak = 0;
        Map<Node, Map<Node, Integer>> jarakCache = new HashMap<>();
        List<Node> allNodesForDist = new ArrayList<>(selectedDestinations);
        allNodesForDist.add(lapakChicken);

        for (Node from : allNodesForDist) {
            Map<Node, Node> prev = new HashMap<>();
            jarakCache.put(from, graph.dijkstra(from, prev));
        }
        
        Node current = lapakChicken;
        List<Node> orderedDestinations = new ArrayList<>();
        Set<Node> visited = new HashSet<>();
        for(Node node : bestPath) {
             if(selectedDestinations.contains(node) && !visited.contains(node)) {
                 orderedDestinations.add(node);
                 visited.add(node);
             }
        }
        
        for (Node tujuan : orderedDestinations) {
            if(jarakCache.get(current) != null && jarakCache.get(current).get(tujuan) != null){
                totalJarak += jarakCache.get(current).get(tujuan);
            }
            current = tujuan;
        }
        
        updateOutput(bestPath, totalJarak);
        String dotFileName = "rute_multi_tujuan.dot";
        GraphVisualizer.generate(graph, bestPath, dotFileName);
        displayVisualization(dotFileName);
    }

    private void displayVisualization(String dotFileName) {

        String pngFileName = dotFileName.replace(".dot", ".png");
        try {
            ProcessBuilder pb = new ProcessBuilder("dot", "-Tpng", dotFileName, "-o", pngFileName);
            Process process = pb.start();
            process.waitFor();
            File pngFile = new File(pngFileName);
            if (pngFile.exists()) {
                JFrame imageFrame = new JFrame("Visualisasi Rute Graf");
                ImageIcon imageIcon = new ImageIcon(new ImageIcon(pngFileName).getImage().getScaledInstance(800, 600, Image.SCALE_SMOOTH));
                JLabel imageLabel = new JLabel(imageIcon);
                imageFrame.add(new JScrollPane(imageLabel));
                imageFrame.pack();
                imageFrame.setLocationRelativeTo(this);
                imageFrame.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Gagal membuat gambar.\nPastikan Graphviz terinstall & ada di PATH.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException | InterruptedException ex) {
            JOptionPane.showMessageDialog(this, "Error saat menjalankan Graphviz: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateOutput(List<Node> path, int totalJarak) {

        StringBuilder pathStr = new StringBuilder();
        if (path.isEmpty()) {
            pathStr.append("Tidak ada rute yang ditemukan.");
        } else {
            for (int i = 0; i < path.size(); i++) {
                pathStr.append(path.get(i).name);
                if (i < path.size() - 1) {
                    pathStr.append("\n  -> ");
                }
            }
        }
        outputArea.setText(pathStr.toString());
        int biayaMinimum = 2000;
        double hargaBensin = 13000;
        double konsumsi = 1.0 / 12000.0;
        double biayaBensin = 2 * totalJarak * konsumsi * hargaBensin;
        int totalOngkir = biayaMinimum + (int) Math.ceil(biayaBensin);
        if (path.isEmpty() || totalJarak == 0) { 
            biayaBensin = 0;
            totalOngkir = 0;
        }
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("in", "ID"));
        currencyFormat.setMaximumFractionDigits(0);
        totalDistanceLabel.setText(String.format("%,d meter", totalJarak));
        fuelCostLabel.setText(currencyFormat.format(biayaBensin));
        totalOngkirLabel.setText(currencyFormat.format(totalOngkir));
    }
    
    private void initializeGraph() {

        graph = new graphDjikstra();
        lapakChicken = new Node("Lapak Chicken");
        Node gerbangBelakang = new Node("Gerbang Belakang");
        Node gerbangSamping = new Node("Gerbang Samping");
        Node pertigaanKeGor = new Node("Pertigaan Ke Gor");
        Node gedungJavanologi = new Node("Javanologi");
        Node masjidNH = new Node("Masjid NH");
        Node aFKIP = new Node("Gedung A FKIP");
        Node dFKIP = new Node("Gedung D FKIP");
        Node pascasarjana = new Node("Pascasarjana");
        Node gorUNS = new Node("Gor UNS");
        Node fk = new Node("Fakultas Kedokteran");
        Node puskom = new Node("Puskom UNS");
        Node gerbangFMIPA = new Node("Gerbang FMIPA");
        Node gedungCfmipa = new Node("Gedung C FMIPA");
        Node gedungAfmipa = new Node("Gedung A FMIPA");
        Node fatisda = new Node("Fatisda & Gedung B FMIPA");
        Node fp = new Node("Fakultas Pertanian");
        Node peternakan = new Node("Gedung E Peternakan");
        Node rektorat = new Node("Rektorat");
        Node auditorium = new Node("Auditorium");
        Node fib = new Node("Fakultas Ilmu Budaya");
        Node uptBahasa = new Node("UPT Bahasa");
        Node ft = new Node("Fakultas Teknik");
        Node fsrd = new Node("FSRD");
        Node fh = new Node("Fakultas Hukum");
        Node feb = new Node("Fakultas Ekonomi Bisnis");
        Node fisip = new Node("Fakultas Ilmu Sosial dan Ilmu Politik");
        Node danauUNS = new Node("Danau UNS");

        Node[] allNodes = {lapakChicken, gerbangBelakang, gerbangSamping, pertigaanKeGor, gedungJavanologi, masjidNH, aFKIP,
                dFKIP, pascasarjana, gorUNS, fk, puskom, gerbangFMIPA, gedungCfmipa, gedungAfmipa, fatisda, fp, peternakan, rektorat, auditorium, fib, uptBahasa,
                ft, fsrd, fh, feb, fisip, danauUNS};
        for (Node node : allNodes) {
            graph.addNode(node);
        }
        graph.addUndirectedEdge(lapakChicken, gerbangBelakang, 600);
        graph.addUndirectedEdge(lapakChicken, gerbangSamping, 1000);
        graph.addUndirectedEdge(gerbangSamping, fh, 100);
        graph.addUndirectedEdge(gerbangBelakang, masjidNH, 50);
        graph.addUndirectedEdge(gedungJavanologi, fk, 170);
        graph.addUndirectedEdge(masjidNH, aFKIP, 100);
        graph.addUndirectedEdge(aFKIP, pertigaanKeGor, 80);
        graph.addUndirectedEdge(pertigaanKeGor, gorUNS, 280);
        graph.addUndirectedEdge(pertigaanKeGor, pascasarjana, 60);
        graph.addUndirectedEdge(gorUNS, gedungJavanologi, 90);
        graph.addUndirectedEdge(aFKIP, pascasarjana, 135);
        graph.addUndirectedEdge(fk, pascasarjana, 300);
        graph.addUndirectedEdge(fk, peternakan, 290);
        graph.addUndirectedEdge(gerbangFMIPA, peternakan, 80);
        graph.addUndirectedEdge(pascasarjana, puskom, 200);
        graph.addUndirectedEdge(pascasarjana, fib, 600);
        graph.addUndirectedEdge(dFKIP, pascasarjana, 400);
        graph.addUndirectedEdge(dFKIP, fh, 260);
        graph.addUndirectedEdge(fh, feb, 110);
        graph.addUndirectedEdge(fh, fisip, 160);
        graph.addUndirectedEdge(feb, fsrd, 150);
        graph.addUndirectedEdge(fisip, fsrd, 400);
        graph.addUndirectedEdge(fsrd, fib, 320);
        graph.addUndirectedEdge(fsrd, uptBahasa, 150);
        graph.addUndirectedEdge(fsrd, ft, 400);
        graph.addUndirectedEdge(ft, rektorat, 400);
        graph.addUndirectedEdge(uptBahasa, rektorat, 300);
        graph.addUndirectedEdge(fib, auditorium, 150);
        graph.addUndirectedEdge(fib, uptBahasa, 160);
        graph.addUndirectedEdge(auditorium, rektorat, 200);
        graph.addUndirectedEdge(rektorat, danauUNS, 100);
        graph.addUndirectedEdge(danauUNS, gerbangFMIPA, 210);
        graph.addUndirectedEdge(danauUNS, fp, 140);
        graph.addUndirectedEdge(danauUNS, puskom, 550);
        graph.addUndirectedEdge(puskom, gerbangFMIPA, 400);
        graph.addUndirectedEdge(gerbangFMIPA, gedungCfmipa, 100);
        graph.addDirectedEdge(gedungCfmipa, gedungAfmipa, 70);
        graph.addDirectedEdge(gedungAfmipa, fatisda, 50);
        graph.addDirectedEdge(fatisda, puskom, 100);
        tujuanList = new ArrayList<>(Arrays.asList(allNodes));
        tujuanList.remove(lapakChicken);
    }
    
    public static void main(String[] args) {
        try {
        
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            System.out.println("Nimbus look and feel not found, using default.");
        }
        
        SwingUtilities.invokeLater(() -> new MainGUI().setVisible(true));
    }
}