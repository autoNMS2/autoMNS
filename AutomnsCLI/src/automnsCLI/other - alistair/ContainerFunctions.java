package automnsCLI;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import java.io.IOException;

public class ContainerFunctions {

    public static void CreateRemoteContainer() {

    }

    public static void CreateRemoteAgent() {
    }

    public static void TestContainerAgent() throws IOException {
        AgentContainer agentContainer = CreateContainer();
        AgentController coordinator = CreateAgent(agentContainer, CoordinatorAgent.class, new Object[]{"Notepad", "Notepad"}, "0");

        //AgentContainer agentContainer2 = CreateContainer();
        //  AgentController agent2 = CreateAgent(agentContainer, ServiceAgent.class, new Object[]{"runProcess"}, "1");
    }

    public static AgentController CreateAgent(AgentContainer agentContainer, Class agentType, Object[] agentParameters, String index) {
        try {
            //  Object[] agentParemeters = new Object[]{runProcess};

            AgentController agentController = agentContainer.createNewAgent(agentType.getName() + index, agentType.getName(), agentParameters);
            agentController.start();
            return agentController;
        } catch (StaleProxyException exc) {
            exc.printStackTrace();
            return null;
        }
    }

    public static AgentContainer CreateContainer() throws IOException {

        String hostIP = GeneralFunctions.GetLocalIP();  //  might have trouble on vms, not sure what ip will work, this worked locally
        //  String hostIP = Inet4Address.getLocalHost().toString();
        //  hostIP = null;  // this works I guess
        System.out.println(hostIP);
        short port = 1099;
        String parameterIP = hostIP;
        String parameterPort = "7778";
        Runtime runtimeInstance = Runtime.instance();
        ProfileImpl containerProfile = new ProfileImpl(hostIP, port, "PlatformHost", true);

        //  parameterIP ==  containerProfile.getProperties().get("host")

        containerProfile.setParameter("mtps", "jade.mtp.http.MessageTransportProtocol(http://" + parameterIP + ":" + parameterPort + "/acc)");
        return runtimeInstance.createMainContainer(containerProfile);
    }
}
