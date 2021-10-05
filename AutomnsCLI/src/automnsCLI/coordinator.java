package automnsCLI;

import jade.core.Agent;
import java.io.IOException;


import automnsCLI.VMFunctions;

public class coordinator extends Agent
{
	//overriding the agent setup
    protected void setup()
    {
		//Defining the path to the AWS key
        String privateKey = "autoMNS/jade/src/test0/test.pem";
		//Defining the IP address of the main platform for other agents to join
    	String coordinatorPrivateIp = "172.31.22.15";
		//Compiling, running, and joining main platform command array (for each agent)
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
		//Initializing SSH sessions to each VM, and executing the agent command array
    	try
        {
			VMFunctions.SSH("54.237.66.183", privateKey, agentCommands[3]);
	    	VMFunctions.SSH("54.237.66.183", privateKey, agentCommands[4]);
	    	VMFunctions.SSH("3.80.22.61", privateKey, agentCommands[5]);
	    	VMFunctions.SSH("3.80.22.61", privateKey, agentCommands[6]);
	    	VMFunctions.SSH("107.21.154.157", privateKey, agentCommands[7]);
	    	VMFunctions.SSH("107.21.154.157", privateKey, agentCommands[8]);
	    	VMFunctions.SSH("52.201.244.70", privateKey, agentCommands[9]);
	    	VMFunctions.SSH("52.201.244.70", privateKey, agentCommands[10]);
	    	VMFunctions.SSH("54.147.209.136", privateKey, agentCommands[11]);
	    	VMFunctions.SSH("54.147.209.136", privateKey, agentCommands[12]);
	    	VMFunctions.SSH("54.159.0.133", privateKey, agentCommands[13]);
	    	VMFunctions.SSH("54.159.0.133", privateKey, agentCommands[14]);
	    	VMFunctions.SSH("3.87.107.66", privateKey, agentCommands[15]);
	    	VMFunctions.SSH("3.87.107.66", privateKey, agentCommands[16]);
		}
		//Throw a failure in Input & Output operations
        catch (IOException e1)
        {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    }
}
