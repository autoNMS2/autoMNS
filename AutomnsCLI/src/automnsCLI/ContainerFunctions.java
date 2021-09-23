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
        //AgentController agent =
        CreateAgent(agentContainer, ServiceAgent.class, "Notepad");
        // agent =
        //  CreateAgent(agentContainer, ServiceAgent.class, "explorer");
    }

    public static AgentController CreateAgent(AgentContainer agentContainer, Class agentType, String runProcess) {
        try {
            Object[] agentParemeters = new Object[]{runProcess};

            AgentController agentController = agentContainer.createNewAgent(agentType.getName() + runProcess, agentType.getName(), agentParemeters);
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
