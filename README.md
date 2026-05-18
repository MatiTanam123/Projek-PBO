# Projek-PBO

untuk menjalanakan masuk ke src terus

1. javac tokobuku\Signup.java lakukan ke hal yang sama untuk form daftar dan lain lain
2. java tokobuku.Signup

// kompilasi
javac -cp ".;..\lib\sqlite-jdbc-3.45.1.0.jar;..\lib\slf4j-api-2.0.12.jar;..\lib\slf4j-simple-2.0.12.jar;..\lib\itextpdf-5.5.13.5.jar;..\lib\jfreechart-1.5.6.jar" com\pos\config\Koneksi.java com\pos\view\Signup.java com\pos\view\Admin.java com\pos\view\Kasir.java com\pos\dao\KasirDAO.java com\pos\service\TransaksiService.java

// jalankan
java -cp ".;..\lib\sqlite-jdbc-3.45.1.0.jar;..\lib\slf4j-api-2.0.12.jar;..\lib\slf4j-simple-2.0.12.jar;..\lib\itextpdf-5.5.13.5.jar;..\lib\jfreechart-1.5.6.jar" com.pos.view.Signup
