package edu.lysak.railwaytickets.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

/**
 * Class is required for providing i18n properties to frontend
 */
@Slf4j
@RestController
public class I18nController {

    @GetMapping("/i18n/{fileName}.properties")
    public ResponseEntity<String> getI18nProperties(@PathVariable String fileName) {
        InputStream resource = I18nController.class.getClassLoader().getResourceAsStream(fileName + ".properties");
        if (resource == null) {
            log.warn("No resources file {}.properties", fileName);
            return ResponseEntity.notFound().build();
        }
        String result = new BufferedReader(new InputStreamReader(resource))
                .lines()
                .collect(Collectors.joining("\n"));
        return ResponseEntity.ok(result);
    }
}
