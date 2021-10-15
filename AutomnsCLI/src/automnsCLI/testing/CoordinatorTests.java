package automnsCLI.testing;

import automnsCLI.JAMEScoordinator;
import automnsCLI.VMFunctions;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.junit.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class CoordinatorTests{

    String[] parameters;

    @Before public void setup()
    {
        VMFunctions.getVmConfig(System.getProperty("user.dir") + "/Prototype/vmsinfo.txt");
        parameters = VMFunctions.getCoordinatorParameters().split(",");
    }

    // getlocalkey
    // getrepokey
    //

    @Test public void testGetRepoFilePath() {
        //  feel like this is a terrible test but...
        assertEquals(VMFunctions.getRepoKeyFilePath(), System.getProperty("user.dir") + "/automnskey.pem");
        assertEquals(parameters[parameters.length - 1], System.getProperty("user.dir") + "/automnskey.pem");
        assertEquals(parameters[0], VMFunctions.getVMPrivateIps().get(1));
        assertEquals(parameters[VMFunctions.getVMPublicIps().size() - 1], VMFunctions.getVMPublicIps().get(1));
    }

    @Test public void testIPWorkerCount()
    {
        assertEquals(parameters.length % 2, 1); // needs to be an odd number
        assertEquals ((parameters.length - 1) / 2, VMFunctions.getVMPrivateIps().size() - 1);   // doesn't take the first ip, (it is the coordinators own ip)
        assertEquals (VMFunctions.getVMPublicIps().size(), VMFunctions.getVMPrivateIps().size());
    }

//    public void getWorkerPublicIPs() {
//        //Defining the path to the AWS key
//
//        List<String> workerVMsPublic = new ArrayList<String>();
//        int count = VMFunctions.getIPWorkerCount();
//        for (int k = count; k < count * 2; k++) {
//            workerVMsPublic.add(getArguments()[k].toString());
//        }
//    }
//
//    public List<String> getWorkerPrivateIPs() {
//        Object[] VMs = getArguments();
//
//        //Defining the path to the AWS key
//        String privateKey = (String) VMs[VMs.length - 1];    // dynamic
//
//        List<String> workerVMsPrivate = new ArrayList<String>();
//        int count = getIPWorkerCount();
//        for (int k = 0; k < count; k++) {
//            workerVMsPrivate.add(getArguments()[k].toString());
//        }
//        return workerVMsPrivate;
//    }

//    public String[] getAgentCommands() {
//        List<String> workerPrivateIPs = JAMEScoordinator.getWorkerPrivateIPs();
//
//        String[] agentCommands = new String[workerPrivateIPs.size()];
//        String[] names = {"db", "Auth", "Image", "Persistence", "Recommender", "Registry", "Webui"};
//        String name;
//        for (int i = 0; i < workerPrivateIPs.size(); i++) {
//            agentCommands[i * 2] = "javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/receive0.java";
//            if (i < names.length) {
//                name = names[i];
//            } else name = "worker" + i;
//            agentCommands[i * 2 + 1] = "java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host " + workerPrivateIPs.get(i) + " -port " + (5001 + i) + " -agents " + name + ":automnsCLI.multi.receive0";
//        }
//
//        //	//Compiling, running, and joining main platform command array (for each agent)
//        //	String[] agentCommands =
//        //			{"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/receive0.java",
//        //					"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host " + workerVMsPrivate.get(0) + " -port 5001 -agents db:automnsCLI.multi.receive0",
//        //					"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/receive0.java",
//        //					"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host " + workerVMsPrivate.get(1) + " -port 5002 -agents Auth:automnsCLI.multi.receive0",
//        //					"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/receive0.java",
//        //					"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host " + workerVMsPrivate.get(2) + " -port 5003 -agents Image:automnsCLI.multi.receive0",
//        //					"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/receive0.java",
//        //					"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host " + workerVMsPrivate.get(3) + " -port 5004 -agents Persistence:automnsCLI.multi.receive0",
//        //					"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/receive0.java",
//        //					"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host " + workerVMsPrivate.get(4) + " -port 5005 -agents Recommender:automnsCLI.multi.receive0",
//        //					"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/receive0.java",
//        //					"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host " + workerVMsPrivate.get(5) + " -port 5006 -agents Registry:automnsCLI.multi.receive0",
//        //					"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/receive0.java",
//        //					"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host " + workerVMsPrivate.get(6) + " -port 5007 -agents Webui:automnsCLI.multi.receive0"
//        //			};
//        return agentCommands;
//    }

//    public void deployAgents() {
//        String[] agentCommands = getAgentCommands();
//        try {
//            int commandCounter = 0;
//            int workerCounter = 0;
//            while (commandCounter < agentCommands.length) {
//                for (int m = 0; m < 2; m++) {
//                    VMFunctions.noOutputSSH(getWorkerPublicIPs().get(workerCounter), VMFunctions.getPrivateKeyPath(), agentCommands[commandCounter]);
//                    commandCounter++;
//                }
//                workerCounter++;
//            }
//        }
//        //Throw a failure in Input & Output operations
//        catch (IOException e1) {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//        }
//    }
}
