package automnsCLI.other;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import java.io.IOException;

public class ContainerFunctions {

    //  javac -cp C:\Users\Computer\Documents\GitHub\autoMNS\jade\lib\jade.jar -d classes C:\Users\Computer\Documents\GitHub\autoMNS\AutomnsCLI\src\automnsCLI\ServiceAgent.java
    //  javac -cp autoMNS\jade\lib\jade.jar -d classes autoMNS\AutomnsCLI\src\automnsCLI\ServiceAgent.java
    //	java -cp  autoMNS\jade\lib\jade.jar:classes jade.Boot -agents Service:automnsCLI.ServiceAgent


    //  javac -cp C:\Users\Computer\Documents\GitHub\autoMNS\jade\lib\jade.jar -d classes C:\Users\Computer\Documents\GitHub\autoMNS\AutomnsCLI\src\automnsCLI\CoordinatorAgent.java

    //	java -cp C:\Users\Computer\Documents\GitHub\autoMNS\jade\lib\jade.jar:classes jade.Boot -agents Coordinator:automnsCLI.CoordinatorAgent
    //	java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -agents db:automnsCLI.CoordinatorAgent
    public static void CreateRemoteContainer() {

    }

    public static void CreateRemoteAgent() {
    }

    public static String getComposeCommand(String serviceName) {
        String command = "docker compose -f C:\\Users\\Computer\\Documents\\GitHub\\autoMNS\\Prototype\\lib\\Services\\";
        command += serviceName + ".yaml up";
        return command;
    }

    public static void TestContainerAgent() throws IOException {
        AgentContainer agentContainer = CreateContainer();
        // "docker compose -f C:\\Users\\Computer\\Documents\\GitHub\\autoMNS\\Prototype\\lib\\Services\\db.yaml up"
        Object[] services = new Object[]{
                //  getComposeCommand("auth"),
                //  getComposeCommand("db"),
                //  getComposeCommand("image"),
                //  getComposeCommand("persistence"),
                //  getComposeCommand("recommender"),
                getComposeCommand("registry"),
                getComposeCommand("webui"),
        };
        AgentController coordinator = CreateAgent(agentContainer, CoordinatorAgent.class, services, "0");

        //  AgentContainer agentContainer2 = CreateContainer();
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
