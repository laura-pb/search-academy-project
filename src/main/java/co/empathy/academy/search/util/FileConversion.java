package co.empathy.academy.search.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FileConversion {
    public static File convertMultipartToTempFile(MultipartFile multipart, String filename) throws IOException {
        File newFile = File.createTempFile(filename, null);
        OutputStream outputStream = new FileOutputStream(newFile);
        outputStream.write(multipart.getBytes());

        return newFile;
    }
}
