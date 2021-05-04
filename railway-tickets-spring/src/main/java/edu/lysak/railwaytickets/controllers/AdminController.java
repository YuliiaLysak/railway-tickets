package edu.lysak.railwaytickets.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/admin/stations")
    public String adminStations() {
        return "admin/stations";
    }

    @GetMapping("/admin/routes")
    public String adminRoutes() {
        return "admin/routes";
    }

}
