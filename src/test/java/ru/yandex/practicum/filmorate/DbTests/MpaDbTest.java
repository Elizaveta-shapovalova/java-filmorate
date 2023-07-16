package ru.yandex.practicum.filmorate.DbTests;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.film.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase()
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MpaDbTest {
    MpaDbStorage mpaDbStorage;

    @Test
    void testFindMpaById() {
        assertEquals(Mpa.builder().id(1L).name("G").build(), mpaDbStorage.getById(1L));
    }

    @Test
    void testFindUnknownMpa() {
        assertThrows(ObjectNotFoundException.class, () -> mpaDbStorage.getById(-1L), "Mpa с id -1 не найден.");
    }

    @Test
    void testFindAllMpa() {
        assertEquals(Mpa.builder().id(1L).name("G").build(), mpaDbStorage.getAll().get(0));
        assertEquals(5, mpaDbStorage.getAll().size());
    }
}
