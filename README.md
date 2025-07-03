# **Chick Route - Optimasi Rute Delivery Makanan Kampus UNS**
Sistem Cerdas untuk Menentukan Rute Pengiriman Optimal dengan Efisiensi Jarak dan Biaya Menggunakan Algoritma Dijkstra dan TSP

---

**Dosen Pengampu:** Hasan Dwi Cahyono, S.Kom, M.Kom

**Disusun Oleh:**
* ANISA DEVINA `(L0124002)`
* APRILLIA ALFA `(L0124003)`
* AYU SANIATUS `(L0124005)`

---

## **Deskripsi Proyek**
**Chick Route** adalah sebuah aplikasi desktop yang dirancang untuk menyelesaikan masalah optimasi rute pengiriman makanan di lingkungan kampus UNS. Proyek ini mengatasi inefisiensi yang timbul dari penentuan rute yang mengandalkan insting, dengan menyediakan solusi berbasis data. Sistem ini memodelkan jaringan jalan kampus sebagai sebuah graf berbobot, kemudian menerapkan algoritma pencarian rute terpendek untuk memberikan rekomendasi jalur yang paling efisien, baik dari segi jarak tempuh maupun estimasi biaya.

## **Fitur Utama**
* **Rute Optimal Tujuan Tunggal:** Menghitung jalur terpendek dari titik awal ke satu lokasi tujuan menggunakan **Algoritma Dijkstra**.
* **Rute Optimal Multi-Tujuan:** Menemukan tur paling efisien untuk mengunjungi beberapa lokasi (maksimal 6) dalam sekali jalan menggunakan **Algoritma TSP (Brute Force)**.
* **Estimasi Biaya:** Memberikan rincian biaya pengiriman yang mencakup total jarak, estimasi bahan bakar, dan total ongkos kirim.
* **Antarmuka Grafis (GUI):** Dibangun dengan Java Swing untuk interaksi yang mudah dengan pengguna.
* **Visualisasi Graf:** Secara otomatis menghasilkan dan menampilkan gambar visualisasi rute yang dihasilkan menggunakan integrasi dengan Graphviz.

## **Teknologi yang Digunakan**
* **Bahasa Pemrograman:** Java (dengan JDK)
* **Library:** Java Swing (untuk GUI)
* **Alat Bantu:** Graphviz (untuk visualisasi graf)

## **Struktur Kode**
Proyek ini dibangun dengan arsitektur modular yang terdiri dari 4 file utama:
1.  **`MainGUI.java`**: Otak dan tampilan aplikasi. Mengelola interaksi pengguna dan mengintegrasikan semua modul.
2.  **`graphDjikstra.java`**: Fondasi sistem. Mendefinisikan struktur data `Node` dan `Edge`, serta berisi implementasi Algoritma Dijkstra.
3.  **`tspAlgorithm.java`**: Modul spesialis yang berisi logika untuk menyelesaikan Traveling Salesman Problem.
4.  **`GraphVisualizer.java`**: Utilitas untuk membuat file `.dot` yang akan dirender oleh Graphviz.

## **Cara Menjalankan Proyek**

#### **1. Prasyarat**
* Pastikan **Java Development Kit (JDK)** sudah terinstall.
* Pastikan **Graphviz** sudah terinstall dan `path` ke direktori `bin`-nya sudah ditambahkan ke environment variables sistem Anda.
* (Opsional) Siapkan file ikon `chicken_icon.png` dan `route_icon.png` di folder yang sama untuk tampilan yang lebih menarik.

#### **2. Kompilasi**
* Buka Command Prompt atau Terminal di direktori proyek (folder tempat semua file `.java` berada).
* Jalankan perintah berikut:
    ```bash
    javac *.java
    ```

#### **3. Eksekusi**
* Setelah kompilasi berhasil, jalankan aplikasi dengan perintah:
    ```bash
    java MainGUI
    ```
* Jendela aplikasi GUI "Chick Route" akan muncul.

---
