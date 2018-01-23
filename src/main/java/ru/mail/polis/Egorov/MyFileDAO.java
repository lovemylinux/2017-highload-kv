package ru.mail.polis.Egorov;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.NoSuchElementException;

public class MyFileDAO implements MyDAO {
    @NotNull
    private final File dir;
    private HashMap<String, byte[]> cache = new HashMap<>();

    public MyFileDAO(@NotNull final File dir) {
        this.dir = dir;
    }

    @NotNull
    @Override
    public byte[] get(@NotNull final String key) throws NoSuchElementException, IllegalArgumentException, IOException {
        if (cache.containsKey(key)) {
            return cache.get(key);
        }

        byte[] value = null;

        if (isDataExist(key)) {
            value = Files.readAllBytes(Paths.get(dir + File.separator + key));
            cache.put(key, value);
        } else {
            cache.put(key, null);
        }

        return value;
    }

    @Override
    public void upsert(@NotNull final String key, @NotNull final byte[] value)throws IllegalArgumentException, IOException {
        cache.put(key, value);
        Files.write(Paths.get(dir + File.separator + key), value);
    }

    @Override
    public void delete(@NotNull final String key) {
        try {
            Files.delete(Paths.get(dir + File.separator + key));
            cache.put(key, null);
        } catch (IOException e) {
            // log it
        }
    }

    @Override
    public boolean isDataExist(@NotNull String key) {
        return Files.exists(Paths.get(dir + File.separator + key));
    }
}