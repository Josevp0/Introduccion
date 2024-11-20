package com.example.Introduccion.Controller;

import com.example.Introduccion.Entity.Producto;
import com.example.Introduccion.Repository.ProductoRepository;
import com.example.Introduccion.Repository.AdministradorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private AdministradorRepository administradorRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Producto> getProductoById(@PathVariable Long id) {
        Optional<Producto> producto = productoRepository.findById(id);
        if (producto.isPresent()) {
            return ResponseEntity.ok(producto.get());
        }
        return ResponseEntity.notFound().build();
    }
    @GetMapping
    public List<Map<String, Object>> getAllProductos() {
        return productoRepository.findAll().stream().map(producto -> {
            Map<String, Object> productoMap = new HashMap<>();
            productoMap.put("id", producto.getId());
            productoMap.put("nombreProducto", producto.getNombreProducto() != null ? producto.getNombreProducto() : "Sin nombre");
            productoMap.put("descripcion", producto.getDescripcion() != null ? producto.getDescripcion() : "Sin descripción");
            productoMap.put("imagen", producto.getImagen() != null ? Base64.getEncoder().encodeToString(producto.getImagen()) : null);
            productoMap.put("precio", producto.getPrecio() != null ? producto.getPrecio() : "Sin precio");
            return productoMap;
        }).collect(Collectors.toList());
    }

    // Crear un producto
    @PostMapping
    public ResponseEntity<String> createProducto(
            @RequestParam String nombreProducto,
            @RequestParam String descripcion,
            @RequestParam MultipartFile imagen,
            @RequestParam(required = false) Double precio) {
        try {
            Producto producto = new Producto();
            producto.setNombreProducto(nombreProducto.isEmpty() ? "Sin nombre" : nombreProducto);
            producto.setDescripcion(descripcion.isEmpty() ? "Sin descripción" : descripcion);

            if (!imagen.isEmpty()) {
                producto.setImagen(imagen.getBytes());
            }

            if (precio != null && precio >= 0) {
                producto.setPrecio(precio);
            } else {
                return ResponseEntity.badRequest().body("El precio debe ser un valor positivo");
            }

            productoRepository.save(producto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Producto creado exitosamente");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar el producto");
        }
    }

    // Eliminar un producto
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProducto(@PathVariable Long id) {
        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
            return ResponseEntity.ok("Producto eliminado correctamente");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producto no encontrado");
        }
    }
}



