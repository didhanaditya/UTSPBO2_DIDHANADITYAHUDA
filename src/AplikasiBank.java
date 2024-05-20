import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

class Transaksi {
    String jenisTransaksi;
    double jumlah;
    String bulan;

    public Transaksi(String jenisTransaksi, double jumlah, String bulan) {
        this.jenisTransaksi = jenisTransaksi;
        this.jumlah = jumlah;
        this.bulan = bulan;
    }

    @Override
    public String toString() {
        return "Transaksi{" +
                "jenisTransaksi='" + jenisTransaksi + '\'' +
                ", jumlah=" + jumlah +
                ", bulan='" + bulan + '\'' +
                '}';
    }
}

class AkunBank {
    private String nama;
    private double saldo;
    private String bank;
    private List<Transaksi> transaksiList;

    public AkunBank(String nama, String bank) {
        this.nama = nama;
        this.saldo = 0.0;
        this.bank = bank;
        this.transaksiList = new ArrayList<>();
    }

    public double getSaldo() {
        return saldo;
    }

    public void deposit(double jumlah, String bulan) {
        saldo += jumlah;
        transaksiList.add(new Transaksi("Deposit", jumlah, bulan));
    }

    public boolean withdraw(double jumlah, String bulan) {
        if (saldo >= jumlah) {
            saldo -= jumlah;
            transaksiList.add(new Transaksi("Withdraw", jumlah, bulan));
            return true;
        }
        return false;
    }

    public boolean transfer(AkunBank penerima, double jumlah, String bulan) {
        double biayaTransfer = this.bank.equals(penerima.getBank()) ? 0 : 5000;
        if (saldo >= jumlah + biayaTransfer) {
            saldo -= (jumlah + biayaTransfer);
            penerima.deposit(jumlah, bulan);
            transaksiList.add(new Transaksi("Transfer keluar", jumlah + biayaTransfer, bulan));
            penerima.transaksiList.add(new Transaksi("Transfer masuk", jumlah, bulan));
            return true;
        }
        return false;
    }

    public String getBank() {
        return bank;
    }

    public void rekapTransaksi(String bulan) {
        System.out.println("Rekap Transaksi untuk bulan " + bulan + ":");
        for (Transaksi transaksi : transaksiList) {
            if (transaksi.bulan.equals(bulan)) {
                System.out.println(transaksi);
            }
        }
    }
}

public class AplikasiBank {
    private static Map<String, AkunBank> akunBankMap = new HashMap<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            System.out.println("\nSELAMAT DATANG DI BANKrut");

            System.out.println("\n1. Cek Saldo");
            System.out.println("2. Input Saldo");
            System.out.println("3. Rekap Transaksi Per Bulan");
            System.out.println("4. Transfer Antar Bank");
            System.out.println("5. Keluar");
            System.out.print("Pilih opsi: ");
            int pilihan = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (pilihan) {
                case 1:
                    cekSaldo();
                    break;
                case 2:
                    inputSaldo();
                    break;
                case 3:
                    rekapTransaksi();
                    break;
                case 4:
                    transfer();
                    break;
                case 5:
                    running = false;
                    System.out.println( "TERIMA KASIH SUDAH MENGGUNAKAN LAYANAN BANKrut INI");
                    break;
                default:
                    System.out.println("Pilihan tidak valid.");
            }
        }
    }

    private static void cekSaldo() {
        System.out.print("Masukkan nama akun: ");
        String nama = scanner.nextLine();
        AkunBank akun = akunBankMap.get(nama);
        if (akun != null) {
            System.out.println("Saldo saat ini: " + akun.getSaldo());
        } else {
            System.out.println("Akun tidak ditemukan.");
        }
    }

    private static void inputSaldo() {
        System.out.print("Masukkan nama akun: ");
        String nama = scanner.nextLine();
        System.out.print("Masukkan nama bank: ");
        String bank = scanner.nextLine();
        System.out.print("Masukkan jumlah deposit: ");
        double jumlah = scanner.nextDouble();
        System.out.print("Masukkan bulan: ");
        String bulan = scanner.next();

        AkunBank akun = akunBankMap.get(nama);
        if (akun == null) {
            akun = new AkunBank(nama, bank);
            akunBankMap.put(nama, akun);
        }

        akun.deposit(jumlah, bulan);
        System.out.println("Saldo berhasil ditambahkan.");
    }

    private static void rekapTransaksi() {
        System.out.print("Masukkan nama akun: ");
        String nama = scanner.nextLine();
        System.out.print("Masukkan bulan: ");
        String bulan = scanner.nextLine();

        AkunBank akun = akunBankMap.get(nama);
        if (akun != null) {
            akun.rekapTransaksi(bulan);
        } else {
            System.out.println("Akun tidak ditemukan.");
        }
    }

    private static void transfer() {
        System.out.print("Masukkan nama akun pengirim: ");
        String namaPengirim = scanner.nextLine();
        System.out.print("Masukkan nama akun penerima: ");
        String namaPenerima = scanner.nextLine();
        System.out.print("Masukkan jumlah transfer: ");
        double jumlah = scanner.nextDouble();
        System.out.print("Masukkan bulan: ");
        String bulan = scanner.next();

        AkunBank pengirim = akunBankMap.get(namaPengirim);
        AkunBank penerima = akunBankMap.get(namaPenerima);

        if (pengirim != null && penerima != null) {
            if (pengirim.transfer(penerima, jumlah, bulan)) {
                System.out.println("Transfer berhasil.");
            } else {
                System.out.println("Transfer gagal. Saldo tidak mencukupi.");
            }
        } else {
            System.out.println("Akun pengirim atau penerima tidak ditemukan.");
        }
    }
}