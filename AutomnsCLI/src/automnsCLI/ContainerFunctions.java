package automnsCLI;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class ContainerFunctions {

    public static void CreateRemoteContainer() {

    }

    public static void CreateRemoteAgent() {

    }

    public static void TestContainerAgent() throws IOException {
        AgentContainer agentContainer = CreateContainer();
        AgentController agent = CreateAgent(agentContainer, ProcessWatcher.class);

    }

    public static AgentController CreateAgent(AgentContainer agentContainer, Class agentType) {
        try {
            AgentController agentController = agentContainer.createNewAgent("a1", agentType.getName(), (Object[]) null);
            agentController.start();
            return agentController;
        } catch (StaleProxyException var9) {
            var9.printStackTrace();
            return null;
        }
    }

    public static AgentContainer CreateContainer() throws IOException {

        String hostIP = GeneralFunctions.GetLocalIP();
        //  String hostIP = Inet4Address.getLocalHost().toString();
        //  hostIP = "192.168.0.4";
        System.out.println(hostIP);
        short port = 1099;
        String parameterIP = hostIP;
        String parameterPort = "7778";
        Runtime runtimeInstance = Runtime.instance();
        ProfileImpl containerProfile = new ProfileImpl(hostIP, port, "PlatformHost", true);

        containerProfile.setParameter("mtps", "jade.mtp.http.MessageTransportProtocol(http://" + parameterIP + ":" + parameterPort + "/acc)");
        return runtimeInstance.createMainContainer(containerProfile);
    }
}
