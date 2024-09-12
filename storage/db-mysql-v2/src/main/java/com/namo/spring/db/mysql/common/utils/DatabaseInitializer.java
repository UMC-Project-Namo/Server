package com.namo.spring.db.mysql.common.utils;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.namo.spring.db.mysql.domains.category.entity.Palette;
import com.namo.spring.db.mysql.domains.category.repository.PaletteRepository;

@Configuration
public class DatabaseInitializer {

    @Bean
    public CommandLineRunner initDatabase(PaletteRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                repository.save(new Palette(1L, "1", 2131034732));
                repository.save(new Palette(2L, "1", 2131034733));
                repository.save(new Palette(3L, "1", 2131034735));
                repository.save(new Palette(4L, "1", 2131034734));
                repository.save(new Palette(5L, "2", 2131034708));
                repository.save(new Palette(6L, "2", 2131034710));
                repository.save(new Palette(7L, "2", 2131034711));
                repository.save(new Palette(8L, "2", 2131034712));
                repository.save(new Palette(9L, "2", 2131034713));
                repository.save(new Palette(10L, "2", 2131034714));
                repository.save(new Palette(11L, "2", 2131034715));
                repository.save(new Palette(12L, "2", 2131034716));
                repository.save(new Palette(13L, "2", 2131034717));
                repository.save(new Palette(14L, "2", 2131034709));
            }
        };
    }
}
