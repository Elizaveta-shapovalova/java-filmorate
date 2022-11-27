package ru.yandex.practicum.filmorate.controller.film;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.film.Director;
import ru.yandex.practicum.filmorate.model.groupInterfaces.Create;
import ru.yandex.practicum.filmorate.model.groupInterfaces.Update;
import ru.yandex.practicum.filmorate.service.film.DirectorService;

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
    public Director getById(@PathVariable Long id) {
        return directorService.getById(id);
    }

    @PostMapping
    public Director create(@Validated(Create.class) @RequestBody Director director) {
        return directorService.create(director);
    }

    @PutMapping
    public Director update(@Validated(Update.class) @RequestBody Director director) {
        return directorService.update(director);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") Long id) {
        directorService.deleteById(id);
    }
}
