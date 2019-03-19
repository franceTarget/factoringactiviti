package activiti;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class DeployedServiceTest {

    @Autowired
    RepositoryService repositoryService;

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    TaskService taskService;

    @Test
    public void deployTest() throws Exception{
        //请假流程的模型
        Model model = repositoryService.createModelQuery().modelKey("modelKey").singleResult();

        //数据库保存的是模型的元数据，不是XMl格式--需要将元数据转换为XML格式，再进行部署
        ObjectNode modelNode = (ObjectNode) new ObjectMapper().readTree(repositoryService.getModelEditorSource(model.getId()));

        BpmnModel bpmnModel = new BpmnJsonConverter().convertToBpmnModel(modelNode);

        byte[] bytes = new BpmnXMLConverter().convertToXML(bpmnModel,"UTF-8");

        String processName = model.getName() + ".bpmn20.xml";

        //部署流程
        Deployment deployment = repositoryService.createDeployment().name(model.getName()).addString(
                processName, new String(bytes,"UTF-8")).deploy();

        Map<String,Object> var = new HashMap<>();
        //设置组长
        var.put("groupLeader","lisi");
        //启动流程
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("vacation",var);

        //查询刚刚启动的流程的lisi待办
        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstanceId()).taskAssignee("lisi").singleResult();

        //组长添加审批意见，以及经理人选 --如果选择false的话，则不用选择经理人选 --提交任务
        var.put("groupLeaderOpinion",true);
        var.put("manager","zhangsan");
        taskService.complete(task.getId(),var);

        //查询刚刚启动的流程的zhangsan待办
        Task task1 = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstanceId()).taskAssignee("zhangsan").singleResult();

        //经理提交任务，到这里，整个流程结束
        taskService.complete(task1.getId(),var);
    }
}
