package co.empathy.academy.search.util;

import co.empathy.academy.search.exceptions.FileReadingException;
import org.jobrunr.utils.io.IOUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class FileConversion {
    public static File convertMultipartToTempFile(MultipartFile multipart)  {
        String filename = UUID.randomUUID().toString();

        File newFile;
        try {
            newFile = File.createTempFile(filename, null);
            OutputStream outputStream = new FileOutputStream(newFile);
            IOUtils.copyStream(multipart.getInputStream(), outputStream);
        } catch (IOException ex) {
            throw new FileReadingException();
        }

        return newFile;
    }

}
