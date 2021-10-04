package automnsCLI.multi;

import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.*;
import jade.lang.acl.*;

import java.io.IOException;
import java.util.Scanner;

import automnsCLI.VMFunctions;

public class coordinator extends Agent
{
    protected void setup()
    {
        String privateKey = "jade/src/test0/AWSKey.ppk";
    	String coordinatorPrivateIp = "172.31.31.191";
    	String[] agentCommands =
                {"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/db_agent.java",
				"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -container -container-name Database -host " + coordinatorPrivateIp + " -port 1099 -agents db:automnsCLI.multi.db_agent",
				"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/authenticator_agent.java",
				"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -container -container-name Authenticator -host " + coordinatorPrivateIp + " -port 1099 -agents Auth:automnsCLI.multi.authenticator_agent",
				"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/image_agent.java",
				"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -container -container-name Image -host " + coordinatorPrivateIp + " -port 1099 -agents Image:automnsCLI.multi.image_agent",
				"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/persistence_agent.java",
				"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -container -container-name Persistence -host " + coordinatorPrivateIp + " -port 1099 -agents Persistence:automnsCLI.multi.persistence_agent",
				"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/recommender_agent.java",
				"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -container -container-name Recommender -host " + coordinatorPrivateIp + " -port 1099 -agents Recommender:automnsCLI.multi.recommender_agent",
				"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/registry_agent.java",
				"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -container -container-name Registry -host " + coordinatorPrivateIp + " -port 1099 -agents Registry:automnsCLI.multi.registry_agent",
				"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/webui_agent.java",
				"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -container -container-name Webui -host " + coordinatorPrivateIp + " -port 1099 -agents Webui:automnsCLI.multi.webui_agent"
				};
    	try
        {
			VMFunctions.SSH("107.22.154.37", privateKey, agentCommands[3]);
	    	VMFunctions.SSH("107.22.154.37", privateKey, agentCommands[4]);
	    	VMFunctions.SSH("54.205.63.203", privateKey, agentCommands[5]);
	    	VMFunctions.SSH("54.205.63.203", privateKey, agentCommands[6]);
	    	VMFunctions.SSH("34.201.45.122", privateKey, agentCommands[7]);
	    	VMFunctions.SSH("34.201.45.122", privateKey, agentCommands[8]);
	    	VMFunctions.SSH("3.82.120.4", privateKey, agentCommands[9]);
	    	VMFunctions.SSH("3.82.120.4", privateKey, agentCommands[10]);
	    	VMFunctions.SSH("3.83.255.57", privateKey, agentCommands[11]);
	    	VMFunctions.SSH("3.83.255.57", privateKey, agentCommands[12]);
	    	VMFunctions.SSH("54.144.70.212", privateKey, agentCommands[13]);
	    	VMFunctions.SSH("54.144.70.212", privateKey, agentCommands[14]);
	    	VMFunctions.SSH("54.175.174.39", privateKey, agentCommands[15]);
	    	VMFunctions.SSH("54.175.174.39", privateKey, agentCommands[16]);
		}
        catch (IOException e1)
        {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    }
}
