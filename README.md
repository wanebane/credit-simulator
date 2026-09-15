# Credit Simulator Application

Aplikasi simulasi kredit kendaraan (Mobil & Motor) berbasis **Java 21** dan **Spring Boot 3**. Aplikasi ini menyediakan opsi simulasi melalui **REST API** dan **Command Line Interface (CLI)** dengan perhitungan bunga per tahun.

---

## 💡 Fitur Utama

- **Dual-Mode Execution**:
    - **REST API**: Menyediakan endpoint HTTP JSON yang aktif di port `8082`.
    - **CLI Mode**: Membaca parameter dari file `.txt` lokal via argument dan mencetak tabel hasil simulasi yang dinamis di terminal.
- **Aturan Suku Bunga**:
    - Tahun 1: Bunga dasar (*base rate*) sesuai jenis & kondisi kendaraan.
    - Tahun Genap (>1): Bunga bertambah **+0.1%**.
    - Tahun Ganjil (>1): Bunga bertambah **+0.5%**.
- **Validasi Lengkap**:
    - Validasi batas maksimum pinjaman (Maksimal Rp 10.000.000.000,00).
    - Validasi persentase Uang Muka (DP) minimal berdasarkan tabel aturan.
    - Validasi batas tenor pinjaman (1 - 6 tahun).
    - Validasi batas minimum tahun kendaraan baru.

---

## 📋 Aturan Validasi Uang Muka (DP)

| Jenis Kendaraan | Kondisi Kendaraan | Minimal DP (%) |
| :--- | :--- | :--- |
| **Mobil** | Baru | 35% |
| **Mobil** | Bekas | 25% |
| **Motor** | Baru | 35% |
| **Motor** | Bekas | 25% |

---

## 🛠️ Teknologi & Environment

- **Java**: Version 21
- **Framework**: Spring Boot 3.x
- **Build Tool**: Maven

## 🚀 Cara Menjalankan Aplikasi

### 1. Manage File Request .txt (Mode CLI)

Untuk menjalankan simulasi pada mode CLI, buat atau letakkan file berformat `.txt` di dalam folder `input-file/` pada *root* proyek.

**Struktur Folder**:
```text
credit-simulator/
├── input-file/
│   └── sample-input-case1.txt
├── bin/
├── src/
└── pom.xml
```

Contoh isi `input-folder/sample-input-case1.txt`
```text
vehicleType=Mobil
vehicleCondition=Baru
vehicleYear=2025
totalLoanAmount=500000000
loanTenure=3
downPayment=175000000
```

### 2. Running via Command Prompt (Windows)
#### A. Menjalankan Mode REST API

Buka Command Prompt (cmd) di direktori proyek, lalu jalankan:
```shell
mvnw spring-boot:run
```
Aplikasi akan berjalan sebagai Web Server HTTP di `http://localhost:8082/credit-simulator`

#### B. Menjalankan Mode CLI

Untuk mengeksekusi file JSON dari folder input-file/, gunakan perintah:

```shell
 ./bin/credit_simulator input-file/sample-input-motor1.txt
```

---
## 🛠️ Sample Request dan Response
### 1. Mode REST API
- **Endpoint** : `POST /api/v1/credit/calculate`
#### Sample Request Body
```json
{
  "vehicleType": "Mobil",
  "vehicleCondition": "Baru",
  "vehicleYear": 2025,
  "totalLoanAmount": 500000000.0,
  "loanTenure": 3,
  "downPayment": 175000000.0
}
```
#### Sample Response Body
```json
{
    "timestamp": "2026-09-15T23:56:47.5559637",
    "status": 200,
    "message": "Perhitungan simulasi kredit berhasil",
    "data": {
        "vehicleType": "Mobil",
        "vehicleCondition": "Baru",
        "vehicleYear": 2025,
        "totalLoanAmount": "500000000.00",
        "downPayment": "175000000.00",
        "principalAmount": "325000000.00",
        "loanTenure": 3,
        "averageMonthlyInstallment": "10578639.50",
        "yearlyInstallments": [
            {
                "year": 1,
                "principalAmount": "325000000.00",
                "interestRate": 8.0,
                "totalLoanAmount": "351000000.00",
                "monthlyInstallment": "9750000.00",
                "yearlyInstallment": "117000000.00"
            },
            {
                "year": 2,
                "principalAmount": "234000000.00",
                "interestRate": 8.1,
                "totalLoanAmount": "252954000.00",
                "monthlyInstallment": "10539750.00",
                "yearlyInstallment": "126477000.00"
            },
            {
                "year": 3,
                "principalAmount": "126477000.00",
                "interestRate": 8.6,
                "totalLoanAmount": "137354022.00",
                "monthlyInstallment": "11446168.50",
                "yearlyInstallment": "137354022.00"
            }
        ]
    }
}
```

### 2. Mode CLI
#### Sample Input File
File Input : `input-file/sample-input-motor.txt`
```text
===========================================================================================================================================================
                                                                   HASIL SIMULASI KREDIT
===========================================================================================================================================================
Jenis Kendaraan           : Motor
Kondisi Kendaraan         : Bekas
Tahun Kendaraan           : 2023
Total Pinjaman            : Rp. 100.000.000,00
Uang Muka (DP)            : Rp. 25.000.000,00
Pokok Pinjaman            : Rp. 75.000.000,00
Tenor                     : 6 Tahun
Rata-rata / bulan         : Rp. 1.445.457,88

-----------------------------------------------------------------------------------------------------------------------------------------------------------
                                                                RINCIAN ANGSURAN PER TAHUN
-----------------------------------------------------------------------------------------------------------------------------------------------------------
Komponen             | Tahun 1             | Tahun 2             | Tahun 3             | Tahun 4             | Tahun 5             | Tahun 6
-----------------------------------------------------------------------------------------------------------------------------------------------------------
Pokok Pinjaman       |   Rp. 75.000.000,00 |   Rp. 68.125.000,00 |   Rp. 59.459.500,00 |   Rp. 48.875.709,00 |   Rp. 35.744.435,18 |   Rp. 19.695.183,78
Rate                 |               9,00% |               9,10% |               9,60% |               9,70% |              10,20% |              10,30%
Total Pinjaman       |   Rp. 81.750.000,00 |   Rp. 74.324.375,00 |   Rp. 65.167.612,00 |   Rp. 53.616.652,77 |   Rp. 39.390.367,57 |   Rp. 21.723.787,71
Angsuran / bulan     |    Rp. 1.135.416,67 |    Rp. 1.238.739,58 |    Rp. 1.357.658,58 |    Rp. 1.489.351,47 |    Rp. 1.641.265,32 |    Rp. 1.810.315,64
Angsuran / tahun     |   Rp. 13.625.000,00 |   Rp. 14.864.875,00 |   Rp. 16.291.903,00 |   Rp. 17.872.217,59 |   Rp. 19.695.183,79 |   Rp. 21.723.787,71
===========================================================================================================================================================
```