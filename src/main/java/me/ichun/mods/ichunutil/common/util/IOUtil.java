package me.ichun.mods.ichunutil.common.util;

import javax.annotation.Nonnull;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class IOUtil {

    public static int extractFiles(@Nonnull Path dir, @Nonnull InputStream inputStream, boolean overwrite) throws IOException {
        int i = 0;
        try (ZipInputStream zipStream = new ZipInputStream(inputStream)) {
            ZipEntry entry;

            while ((entry = zipStream.getNextEntry()) != null) {
                Path path = dir.resolve(entry.getName());
                if (!overwrite && Files.exists(path) && Files.size(path) > 3L) //check if there are at least some bytes written so we know the file isn't empty
                {
                    continue;
                }

                if (entry.isDirectory()) {
                    if (!Files.exists(path)) {
                        Files.createDirectories(path);
                    }
                } else {
                    try (OutputStream out = Files.newOutputStream(path)) {
                        byte[] buffer = new byte[8192];
                        int len;
                        while ((len = zipStream.read(buffer)) != -1) {
                            out.write(buffer, 0, len);
                        }

                        i++;
                    }
                }
            }
        }
        return i;
    }
}
