# 💸 Aplikasi Pencatatan Pengeluaran Harian

Aplikasi desktop berbasis **Java Swing** untuk mencatat dan mengelola pengeluaran harian dengan laporan mingguan dan bulanan.

##  Fitur

###  Dashboard
- Statistik real-time (total bulan ini, jumlah transaksi, rata-rata per hari, total hari ini)
- Navigasi cepat ke semua halaman
- Auto-refresh data

### List Data
- Tabel interaktif dengan semua transaksi
- **Search** berdasarkan deskripsi, kategori, atau tanggal
- **Filter** berdasarkan kategori
- **Sorting** berdasarkan tanggal (terbaru) atau jumlah (terbesar)
- Edit dan hapus data langsung dari tabel

### Form Input
- Input pengeluaran baru dengan validasi
- Edit data existing
- Kategori: Makanan, Transport, Pendidikan, Hiburan, Kesehatan, Fashion, Teknologi, dll
- Validasi real-time (jumlah harus angka, tanggal format DD/MM/YYYY)
- Exception handling untuk input invalid

###  Laporan & Statistik
- Laporan **Harian**, **Mingguan**, dan **Bulanan**
- Statistik total pengeluaran, jumlah transaksi, kategori terbanyak
- Breakdown per kategori dengan progress bar
- Export laporan ke **TXT** dan **CSV**

##  Teknologi

- **Java 8+**
- **Java Swing** untuk GUI
- **CSV** untuk penyimpanan data

##  Struktur Project

```
UAP/
├── src/
│   └── main/
│       └── java/
│           └── org/
│               └── example/
│                   ├── model/
│                   │   └── Expense.java          # Model data
│                   ├── service/
│                   │   ├── ExpenseManager.java   # Business logic & CRUD
│                   │   └── FileHandler.java      # CSV operations
│                   ├── ui/
│                   │   ├── MainFrame.java        # Main window
│                   │   ├── DashboardPanel.java   # Dashboard UI
│                   │   ├── ListPanel.java        # List & table UI
│                   │   ├── FormPanel.java        # Input form UI
│                   │   └── ReportPanel.java      # Report UI
│                   └── App.java                  # Entry point
├── data/
│   └── expenses.csv                              # Data storage
└── README.md
```

### Menggunakan IDE
1. **IntelliJ IDEA**:
    - Open Project → Pilih folder project
    - Klik kanan `App.java` → Run 'App.main()'

2. **VSCode**:
    - Install Extension "Java Extension Pack"
    - Open folder project
    - Run → Start Debugging (F5)

3. **NetBeans**:
    - File → Open Project
    - Klik kanan project → Run

##  Implementasi Modul

###  Modul 1: Program Correctness
- Validasi input di `FormPanel` (deskripsi, kategori, jumlah, tanggal)
- Logic CRUD yang benar di `ExpenseManager`
- Data consistency checks

###  Modul 2: Refactoring
- Extract methods untuk reusable code
- Clean code dengan proper naming conventions
- Remove duplicate code dengan helper methods

###  Modul 3: Modern Programming Environment
- Menggunakan IDE (IntelliJ IDEA)
- Proper project structure
- JavaDoc documentation

###  Modul 4: Java API
- **LocalDate** untuk manajemen tanggal
- **ArrayList** untuk data storage sementara
- **Comparator** untuk sorting (by date, by amount)
- **HashMap** untuk grouping by category
- **Stream API** untuk filtering dan aggregation

###  Modul 5: File Handling
- **BufferedReader** untuk membaca CSV
- **BufferedWriter** untuk menulis CSV
- **Exception Handling** (IOException, FileNotFoundException)
- Data persistence (auto-save ke CSV)

###  Modul 6: GUI (Java Swing)
- **JFrame** sebagai main window
- **CardLayout** untuk navigation antar halaman
- **JPanel** untuk layout components
- **JTable** dengan custom renderer untuk data list
- **JButton** dengan event handling (ActionListener)
- **JTextField** untuk input
- **JComboBox** untuk dropdown kategori
- **JProgressBar** untuk visualization
- Custom styling dengan colors & fonts

##  Color Palette

- Background: `#55423d` (Coklat gelap)
- Primary: `#ffc0ad` (Peach)
- Accent: `#e78fb3` (Pink)
- Tertiary: `#9656a1` (Ungu)
- Text: `#fff3ec` (Cream)

##  Format Data CSV

```csv
id,description,category,amount,date,notes
1,Makan siang,Makanan & Minuman,25000,2025-12-19,Kantin kampus
2,Bensin motor,Transport & Bensin,50000,2025-12-18,Full tank
```

##  Testing

### Manual Testing Checklist:
- ✅ Tambah data baru → Berhasil tersimpan ke CSV
- ✅ Edit data existing → Update berhasil
- ✅ Hapus data → Data terhapus dari CSV
- ✅ Search → Hasil sesuai keyword
- ✅ Filter kategori → Hanya tampil kategori yang dipilih
- ✅ Sort by date → Urutan terbaru ke terlama
- ✅ Sort by amount → Urutan terbesar ke terkecil
- ✅ Validasi input angka → Error jika bukan angka
- ✅ Validasi format tanggal → Error jika format salah
- ✅ Export TXT → File berhasil dibuat
- ✅ Laporan multi-periode → Data sesuai periode

### Code Review Points:
- ✅ Penamaan variabel konsisten (`camelCase`)
- ✅ No duplicate code (use helper methods)
- ✅ Exception handling di semua file operations
- ✅ Input validation di form
- ✅ Comments & JavaDoc

##  Tim Pengembang

- **[Sayyi Zaidan Akmal]** - [202410370110517]
- **[Erlin Mariska]** - [202410370110514]

##  Lisensi

Project ini dibuat untuk memenuhi **Ujian Akhir Praktikum Pemrograman Lanjut** Universitas Muhammadiyah Malang.

---

⭐ Jika project ini membantu, jangan lupa kasih star!
