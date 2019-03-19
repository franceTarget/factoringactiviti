package demo;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileTest {

    @Test
    public void test1() {
        File file = new File("d://generatorConfig.xml");

        try {
            FileInputStream fileInputStream = new FileInputStream(file);

            try {
                FileUtils.copyInputStreamToFile(fileInputStream, new File("src/main/resources/static","test.xml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
