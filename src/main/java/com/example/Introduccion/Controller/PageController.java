package com.example.Introduccion.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String homePage() {
        return "index"; // Busca index.html en templates
    }

    @GetMapping("/view/administradores")
    public String administradoresPage() {
        return "administradores"; // Busca administradores.html en templates
    }

    @GetMapping("/view/productos")
    public String productosPage() {
        return "productos"; // Busca productos.html en templates
    }

    @GetMapping("/view/menu")
    public String menuPage() {
        return "menu"; // Busca productos.html en templates
    }
}

