package shoppinglist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import shoppinglist.entity.DaftarBelanja;
import shoppinglist.entity.DaftarBelanjaDetil;
import shoppinglist.repository.DaftarBelanjaDetilRepo;
import shoppinglist.repository.DaftarBelanjaRepo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@SpringBootApplication
public class DemoShoppingListSpringBootApplication implements CommandLineRunner
{
    @Autowired
    private DaftarBelanjaRepo repo;
    private DaftarBelanjaDetilRepo detilRepo;

    public static void main(String[] args)
    {
        SpringApplication.run(DemoShoppingListSpringBootApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Membaca Semua Record DaftarBelanja : ");
        List<DaftarBelanja> all = repo.findAll();
        for (DaftarBelanja db : all) {
            System.out.println(db.getJudul());

            List<DaftarBelanjaDetil> listBarang = db.getDaftarBarang();
            for(DaftarBelanjaDetil barang : listBarang){
                System.out.println(barang.getNamaBarang()+" "+barang.getByk()+" "+barang.getSatuan()+" "+barang.getMemo());
            }

        }

        Scanner keyb = new Scanner(System.in);

        // Baca berdasarkan ID
        System.out.println("----------------------------------------\nMencari berdasarkan ID");
        System.out.print("Masukkan ID dari objek DaftarBelanja yg ingin ditampilkan: ");
        long id = Long.parseLong(keyb.nextLine());
        System.out.println("Hasil pencarian: ");

        Optional<DaftarBelanja> optDB = repo.findById(id);
        if (optDB.isPresent()) {
            DaftarBelanja db = optDB.get();
            System.out.println("\tJudul: " + db.getJudul());
        }
        else {
            System.out.println("\tTIDAK DITEMUKAN.");
        }

        //Mencari berdasarkan judul
        System.out.println("---------------------------------------\nMencari berdasarkan judul");
        System.out.print("Masukkan Judul : ");
        String judul = keyb.nextLine();
        List<DaftarBelanja> daftarBelanjaList = repo.findByJudul(judul);
        if(daftarBelanjaList.size() > 0) {
            for (DaftarBelanja data : daftarBelanjaList) {
                System.out.println("ID : " + data.getId() + "\nTanggal : " + data.getTanggal() + "\nJudul : "
                        + data.getJudul());
            }
        }else{
            System.out.println("Judul tidak ditemukan :)))");
        }

        //insert data ke daftar belanja
        System.out.println("-----------------------------------\nMembuat daftar belanja baru :)");
        LocalDateTime time = LocalDateTime.now();
        System.out.print("Masukkan judul :");
        String judulDaftar = keyb.nextLine();

        DaftarBelanja daftar = new DaftarBelanja();
        daftar.setTanggal(time);
        daftar.setJudul(judulDaftar);
        repo.save(daftar);

        //delete data daftar belanja
        System.out.println("-------------------------------------\nMenghapus daftar belanja berdasarkan ID");
        System.out.print("Masukkan ID yang akan dihapus : ");
        long idDelete = Long.parseLong(keyb.nextLine());
        optDB = repo.findById(idDelete);
        if(optDB.isPresent()){
            DaftarBelanja db = optDB.get();
            System.out.println(db.getJudul());
            List<DaftarBelanjaDetil> listBarang = db.getDaftarBarang();
            for(DaftarBelanjaDetil barang : listBarang){
                System.out.println(barang.getNamaBarang()+" "+barang.getByk()+" "+barang.getSatuan()+" "+barang.getMemo());
                detilRepo.deleteById(idDelete);
            }
            repo.deleteById(idDelete);
        }else{
            System.out.println("ID yang dimasukkan tidak tersedia :)))))");
        }

    }
}
