package edu.lysak.railwaytickets.controllers;

import edu.lysak.railwaytickets.exceptions.BusinessLogicException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Class is required for providing i18n properties to frontend
 */
@Slf4j
@RestController
public class I18nController {

    @GetMapping("/i18n/{fileName}.properties")
    public String getI18nProperties(@PathVariable String fileName) throws IOException, URISyntaxException {
        URL resource = I18nController.class.getClassLoader().getResource(fileName + ".properties");
        if (resource == null) {
            log.warn("No resources file {}", fileName + ".properties");
            throw new BusinessLogicException("No resources file");
        }
        return Files.readString(Paths.get(resource.toURI()));
    }
}
