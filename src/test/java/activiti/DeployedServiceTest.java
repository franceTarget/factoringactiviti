package activiti;

import com.ren.factoring.flow.ActivitiApplication;
import com.ren.factoring.flow.dao.ActGeBytearrayDao;
import com.ren.factoring.flow.models.model.ActGeBytearray;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ActivitiApplication.class)
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class,
        org.activiti.spring.boot.SecurityAutoConfiguration.class})
@Slf4j
public class DeployedServiceTest {

    @Autowired
    private ActGeBytearrayDao actGeBytearrayDao;

    @Test
    public void test1() {
        ActGeBytearray actGeBytearray = actGeBytearrayDao.selectByDeployId("628250165437792256");
        byte[] bytes = actGeBytearray.getBytes();
        File file = new File("E:\\activiti\\jbpm.txt");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bytes);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
