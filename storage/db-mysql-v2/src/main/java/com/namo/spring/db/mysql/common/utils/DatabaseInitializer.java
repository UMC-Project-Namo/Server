package com.namo.spring.db.mysql.common.utils;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.namo.spring.db.mysql.domains.category.entity.Palette;
import com.namo.spring.db.mysql.domains.category.repository.PaletteRepository;

import java.util.Arrays;

@Configuration
public class DatabaseInitializer {

    @Bean
    public CommandLineRunner initPaletteDatabase(PaletteRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                repository.saveAll(Arrays.asList(
                        new Palette(1L, "1", "#DA6022", "Namo Orange"),
                        new Palette(2L, "1", "#DE8989", "Namo Pink"),
                        new Palette(3L, "1", "#E1B000", "Namo Yellow"),
                        new Palette(4L, "1", "#5C8596", "Namo Blue"),
                        new Palette(5L, "1", "#DADADA", "Light Gray"),
                        new Palette(6L, "1", "#EB5353", "Red"),
                        new Palette(7L, "1", "#FFA192", "Pink"),
                        new Palette(8L, "1", "#EC9B3B", "Orange"),
                        new Palette(9L, "1", "#FFE70F", "Yellow"),
                        new Palette(10L, "1", "#B3DF67", "Lime"),
                        new Palette(11L, "1", "#78A756", "Light Green"),
                        new Palette(12L, "1", "#24794F", "Green"),
                        new Palette(13L, "1", "#5AE0BC", "Cyan"),
                        new Palette(14L, "1", "#45C1D4", "Light Blue"),
                        new Palette(15L, "1", "#355080", "Blue"),
                        new Palette(16L, "1", "#8571BF", "Lavendar"),
                        new Palette(17L, "1", "#833286", "Purple"),
                        new Palette(18L, "1", "#FF70DE", "Magenta"),
                        new Palette(19L, "1", "#9C9C9C", "Dark Gray"),
                        new Palette(20L, "1", "#1D1D1D", "Black")
                ));
            }
        };
    }
}
