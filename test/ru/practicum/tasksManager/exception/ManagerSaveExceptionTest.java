package ru.practicum.tasksManager.exception;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ManagerSaveExceptionTest {
    final File file;

    {
        try {
            file = File.createTempFile("data", ".txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void assertThrowsShouldSuccessfullyWriteToFileTest() {
        assertThrows(ManagerSaveException.class, () -> {
            try (FileWriter writer = new FileWriter(file.getAbsolutePath())) {
                writer.write("Some string\n");
                throw new IOException("Test IOException");
            } catch (IOException e) {
                throw new ManagerSaveException("Произошла ошибка во время записи файла.");
            }
        }, "ManagerSaveException не было выброшено");

    }
}