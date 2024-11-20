package com.example.Introduccion.Controller;

import com.example.Introduccion.Entity.Administrador;
import com.example.Introduccion.Repository.AdministradorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/administradores")
public class AdministradorController {

    @Autowired
    private AdministradorRepository administradorRepository;

    @PostMapping("/auth")
    public ResponseEntity<String> autenticarAdministrador(@RequestBody Administrador credenciales) {
        Optional<Administrador> administrador = administradorRepository.findAll().stream()
                .filter(admin -> admin.getCorreo().equals(credenciales.getCorreo()) &&
                        admin.getContrasena().equals(credenciales.getContrasena()))
                .findFirst();

        if (administrador.isPresent()) {
            return ResponseEntity.ok("Autenticación exitosa");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }
    }

    // Obtener todos los administradores
    @GetMapping
    public ResponseEntity<List<Administrador>> getAllAdministradores() {
        List<Administrador> administradores = administradorRepository.findAll();
        return ResponseEntity.ok(administradores);
    }

    // Obtener un administrador por ID
    @GetMapping("/{id}")
    public ResponseEntity<Administrador> getAdministradorById(@PathVariable Long id) {
        Optional<Administrador> administrador = administradorRepository.findById(id);
        return administrador.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Crear un nuevo administrador
    @PostMapping
    public ResponseEntity<Administrador> createAdministrador(@RequestBody Administrador administrador) {
        Administrador savedAdministrador = administradorRepository.save(administrador);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAdministrador);
    }

    // Actualizar un administrador existente
    @PutMapping("/{id}")
    public ResponseEntity<Administrador> updateAdministrador(
            @PathVariable Long id,
            @RequestBody Administrador updatedAdministrador) {
        Optional<Administrador> administradorOptional = administradorRepository.findById(id);
        if (administradorOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Administrador administrador = administradorOptional.get();
        administrador.setNombre(updatedAdministrador.getNombre());
        administrador.setApellido(updatedAdministrador.getApellido());
        administrador.setContrasena(updatedAdministrador.getContrasena());
        administrador.setCorreo(updatedAdministrador.getCorreo());

        Administrador savedAdministrador = administradorRepository.save(administrador);
        return ResponseEntity.ok(savedAdministrador);
    }

    // Eliminar un administrador
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdministrador(@PathVariable Long id) {
        Optional<Administrador> administradorOptional = administradorRepository.findById(id);
        if (administradorOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        administradorRepository.delete(administradorOptional.get());
        return ResponseEntity.noContent().build();
    }
}


