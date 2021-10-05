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
		//Assigning the private ip for each agent
		String db_ip = "172.31.94.34";
		String authenticator_ip = "172.31.94.242";
		String image_ip = "172.31.88.187";
		String persistence_ip = "172.31.94.105";
		String recommender_ip = "172.31.90.248";
		String registry_ip = "172.31.86.119";
		String webui_ip = "172.31.94.94";

		//Compiling, running, and joining main platform command array (for each agent)
    	String[] agentCommands =
                {"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/db_agent.java",
				"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host " + db_ip + " -port 1099 -agents db:automnsCLI.multi.db_agent",
				"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/authenticator_agent.java",
				"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host " + authenticator_ip + " -port 1099 -agents Auth:automnsCLI.multi.authenticator_agent",
				"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/image_agent.java",
				"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host " + image_ip + " -port 1099 -agents Image:automnsCLI.multi.image_agent",
				"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/persistence_agent.java",
				"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host " + persistence_ip + " -port 1099 -agents Persistence:automnsCLI.multi.persistence_agent",
				"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/recommender_agent.java",
				"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host " + recommender_ip + " -port 1099 -agents Recommender:automnsCLI.multi.recommender_agent",
				"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/registry_agent.java",
				"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host " + registry_ip + " -port 1099 -agents Registry:automnsCLI.multi.registry_agent",
				"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/webui_agent.java",
				"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host " + webui_ip + " -port 1099 -agents Webui:automnsCLI.multi.webui_agent"
				};
		//Initializing SSH sessions using public ip to each VM, and executing the agent command array
    	try
        {
			VMFunctions.SSH("3.85.29.60", privateKey, agentCommands[0]);
	    	VMFunctions.SSH("3.85.29.60", privateKey, agentCommands[1]);
	    	VMFunctions.SSH("52.91.100.173", privateKey, agentCommands[2]);
	    	VMFunctions.SSH("52.91.100.173", privateKey, agentCommands[3]);
	    	VMFunctions.SSH("35.175.243.185", privateKey, agentCommands[4]);
	    	VMFunctions.SSH("35.175.243.185", privateKey, agentCommands[5]);
	    	VMFunctions.SSH("54.209.82.120", privateKey, agentCommands[6]);
	    	VMFunctions.SSH("54.209.82.120", privateKey, agentCommands[7]);
	    	VMFunctions.SSH("54.146.135.222", privateKey, agentCommands[8]);
	    	VMFunctions.SSH("54.146.135.222", privateKey, agentCommands[9]);
	    	VMFunctions.SSH("174.129.161.236", privateKey, agentCommands[10]);
	    	VMFunctions.SSH("174.129.161.236", privateKey, agentCommands[11]);
	    	VMFunctions.SSH("54.152.173.110", privateKey, agentCommands[12]);
	    	VMFunctions.SSH("54.152.173.110", privateKey, agentCommands[13]);
		}
		//Throw a failure in Input & Output operations
        catch (IOException e1)
        {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    }
}
