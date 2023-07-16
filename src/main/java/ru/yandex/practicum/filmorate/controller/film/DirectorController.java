package ru.yandex.practicum.filmorate.controller.film;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Marker;
import ru.yandex.practicum.filmorate.model.film.Director;
import ru.yandex.practicum.filmorate.service.film.DirectorService;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/directors")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class DirectorController {
    DirectorService directorService;

    @GetMapping
    public List<Director> getAll() {
        return directorService.getAll();
    }

    @GetMapping("/{id}")
    public Director getById(@Positive @PathVariable("id") Long id) {
        return directorService.getById(id);
    }

    @PostMapping
    public Director create(@Validated(Marker.OnCreate.class) @RequestBody Director director) {
        return directorService.create(director);
    }

    @PutMapping
    public Director update(@Validated(Marker.OnUpdate.class) @RequestBody Director director) {
        return directorService.update(director);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") Long id) {
        directorService.deleteById(id);
    }
}
