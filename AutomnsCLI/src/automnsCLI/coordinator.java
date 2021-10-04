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
        String privateKey = "autoMNS/jade/src/test0/test.pem";
    	String coordinatorPrivateIp = "172.31.88.244";
    	String[] agentCommands =
                {"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/db_agent.java",
				"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -container -container-name Database -host " + coordinatorPrivateIp + " -port 2377 -detect-main * -agents db:automnsCLI.multi.db_agent",
				"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/authenticator_agent.java",
				"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -container -container-name Authenticator -host " + coordinatorPrivateIp + " -port 2377 -detect-main * -agents Auth:automnsCLI.multi.authenticator_agent",
				"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/image_agent.java",
				"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -container -container-name Image -host " + coordinatorPrivateIp + " -port 2377 -detect-main * -agents Image:automnsCLI.multi.image_agent",
				"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/persistence_agent.java",
				"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -container -container-name Persistence -host " + coordinatorPrivateIp + " -port 2377 -detect-main * -agents Persistence:automnsCLI.multi.persistence_agent",
				"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/recommender_agent.java",
				"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -container -container-name Recommender -host " + coordinatorPrivateIp + " -port 2377 -detect-main * -agents Recommender:automnsCLI.multi.recommender_agent",
				"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/registry_agent.java",
				"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -container -container-name Registry -host " + coordinatorPrivateIp + " -port 2377 -detect-main * -agents Registry:automnsCLI.multi.registry_agent",
				"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/webui_agent.java",
				"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -container -container-name Webui -host " + coordinatorPrivateIp + " -port 2377 -detect-main * -agents Webui:automnsCLI.multi.webui_agent"
				};
    	try
        {
			VMFunctions.SSH("54.164.12.150", privateKey, agentCommands[3]);
	    	VMFunctions.SSH("54.164.12.150", privateKey, agentCommands[4]);
	    	VMFunctions.SSH("3.86.155.186", privateKey, agentCommands[5]);
	    	VMFunctions.SSH("3.86.155.186", privateKey, agentCommands[6]);
	    	VMFunctions.SSH("3.92.197.213", privateKey, agentCommands[7]);
	    	VMFunctions.SSH("3.92.197.213", privateKey, agentCommands[8]);
	    	VMFunctions.SSH("52.87.213.182", privateKey, agentCommands[9]);
	    	VMFunctions.SSH("52.87.213.182", privateKey, agentCommands[10]);
	    	VMFunctions.SSH("3.83.121.76", privateKey, agentCommands[11]);
	    	VMFunctions.SSH("3.83.121.76", privateKey, agentCommands[12]);
	    	VMFunctions.SSH("3.83.141.155", privateKey, agentCommands[13]);
	    	VMFunctions.SSH("3.83.141.155", privateKey, agentCommands[14]);
	    	VMFunctions.SSH("18.205.116.211", privateKey, agentCommands[15]);
	    	VMFunctions.SSH("18.205.116.211", privateKey, agentCommands[16]);
		}
        catch (IOException e1)
        {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    }
}
