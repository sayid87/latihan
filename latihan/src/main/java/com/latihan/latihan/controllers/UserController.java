package com.latihan.latihan.controllers;

import java.io.File;
import java.util.ArrayList;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.latihan.latihan.entities.UserEntities;
import com.latihan.latihan.repositories.UserRepository;
import com.latihan.latihan.requests.UserRequest;
import com.latihan.latihan.responses.WebResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@CrossOrigin(origins = "*")
public class UserController {
    @Autowired
    private UserRepository uRep;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @GetMapping("/users")
    public WebResponse getListUser() {
        ArrayList<UserEntities> data = (ArrayList<UserEntities>) uRep.findAll();
        // return new String("Halo Dunia");
        return WebResponse.builder().data(data).sukses(1).build();
    }

    @GetMapping("/users/{id}")
    public WebResponse<Object> getDetailUser(@PathVariable int id) {
        try {
            UserEntities user = uRep.findById(id).get();
            return WebResponse.builder().data(user).sukses(1).build();
        } catch (Exception e) {
            // TODO: handle exception
            return WebResponse.builder().sukses(0).message(e.getMessage()).build();
        }

    }

    @DeleteMapping("/users/{id}")
    public WebResponse<Object> deleteUser(@PathVariable int id) {
        try {
            uRep.deleteById(id);
            return WebResponse.builder().sukses(1).message("Data Berhasil dihapus").build();
        } catch (Exception e) {
            // TODO: handle exception
            return WebResponse.builder().sukses(0).message(e.getMessage()).build();
        }
    }

    @PostMapping(path = "/users", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public WebResponse<Object> register(@ModelAttribute UserRequest.RegisterRequest request) {
        // TODO: process POST request
        String nama = request.getNama();
        String email = request.getEmail();
        String password = request.getPassword();
        String ulangi = request.getUlangi();
        String telp = request.getTelp();
        MultipartFile foto = request.getFoto();
        int errorCount = 0;
        ArrayList<String> arrErrors = new ArrayList<String>();
        if (nama.isEmpty()) {
            errorCount++;
            arrErrors.add("Nama harus diisi");
        }
        if (email.isEmpty()
                || !email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
            errorCount++;
            arrErrors.add("Email harus diisi dengan format yang valid");
        } else {
            if (uRep.findByEmail(email) != null) {
                errorCount++;
                arrErrors.add("Email sudah terdaftar");
            }
        }
        if (password.isEmpty()) {
            errorCount++;
            arrErrors.add("Password harus diisi");
        }
        if (!ulangi.equals(password)) {
            errorCount++;
            arrErrors.add("Password dan Ulangi Password harus sama");
        }
        if (telp.isEmpty() || telp.length() < 10 || telp.length() > 15 || telp.matches("[^0-9]")) {
            errorCount++;
            arrErrors.add("Nomor Telepon harus diisi denganantara 10-15 angka");
        }
        if (foto.isEmpty()
                || (!foto.getContentType().equals("image/jpeg") && !foto.getContentType().equals("image/png"))) {
            errorCount++;
            arrErrors.add("Foto harus diisi dengan format jpeg/png");
        }

        if (errorCount > 0) {
            return WebResponse.builder().sukses(0).errorsMsg(arrErrors)
                    .build();

        } else {
            try {

                String filepath = System.getProperty("user.dir") + "/uploads/users/";
                File directory = new File(filepath);
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                File destination = new File(filepath + foto.getOriginalFilename());
                foto.transferTo(destination);
                UserEntities user = new UserEntities();
                user.setEmail(email);
                user.setPassword(encoder.encode(password));
                user.setNama(nama);
                user.setTelp(telp);
                user.setFoto(foto.getOriginalFilename());
                uRep.save(user);
                return WebResponse.builder().data(null).message("Registrasi Sukses").sukses(1).build();
            } catch (Exception e) {
                // TODO: handle exception
                return WebResponse.builder().data(null).message(e.getMessage()).sukses(0).build();
            }

        }

    }

    @PutMapping("users/{id}")
    public WebResponse<Object> ubahUser(@PathVariable String id, @ModelAttribute UserRequest.UpdateRequest request) {
        // TODO: process PUT request

        String nama = request.getNama();
        String telp = request.getTelp();
        MultipartFile foto = request.getFoto();
        int errorCount = 0;
        ArrayList<String> arrErrors = new ArrayList<String>();
        if (nama.isEmpty()) {
            errorCount++;
            arrErrors.add("Nama harus diisi");
        }
        if (telp.isEmpty() || telp.length() < 10 || telp.length() > 15 || telp.matches("[^0-9]")) {
            errorCount++;
            arrErrors.add("Nomor Telepon harus diisi denganantara 10-15 angka");
        }
        if (!foto.isEmpty()
                && (!foto.getContentType().equals("image/jpeg") && !foto.getContentType().equals("image/png"))) {
            errorCount++;
            arrErrors.add("Foto harus diisi dengan format jpeg/png");
        }
        if (errorCount > 0) {
            return WebResponse.builder().sukses(0).errorsMsg(arrErrors).build();
        } else {

            try {
                UserEntities user = uRep.findById(Integer.parseInt(id)).get();
                user.setNama(nama);
                user.setTelp(telp);

                if (!foto.isEmpty()) {
                    try {
                        String filepath = System.getProperty("user.dir") + "/uploads/users/";
                        File directory = new File(filepath);
                        if (!directory.exists()) {
                            directory.mkdirs();
                        }

                        File destination = new File(filepath + foto.getOriginalFilename());
                        foto.transferTo(destination);

                        user.setFoto(foto.getOriginalFilename());

                        return WebResponse.builder().data(null).message("Ubah Data Sukses").sukses(1).build();
                    } catch (Exception e) {
                        // TODO: handle exception
                        return WebResponse.builder().data(null).message(e.getMessage()).sukses(0).build();
                    }
                }
                uRep.save(user);
                return WebResponse.builder().data(null).message("Ubah Data Sukses").sukses(1).build();
            } catch (Exception e) {
                // TODO: handle exception
                return WebResponse.builder().data(null).message(e.getMessage()).sukses(0).build();
            }

        }
    }

    @PostMapping(path = "/login", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public WebResponse<Object> loginUser(@ModelAttribute UserRequest.LoginRequest request) {
        // TODO: process POST request
        UserEntities user = uRep.findByEmail(request.getEmail());
        if (user != null) {
            String password = request.getPassword();
            if (encoder.matches(password, user.getPassword())) {
                return WebResponse.builder().data(user).message("Login Berhasil").sukses(1).build();
            } else {
                return WebResponse.builder().sukses(0).message("Email dan Password Salah").build();
            }
        } else {
            return WebResponse.builder().sukses(0).message("Email dan Password Salah").build();
        }

    }

}
