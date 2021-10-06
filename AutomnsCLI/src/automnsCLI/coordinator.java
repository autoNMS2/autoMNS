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
    	//String coordinatorPrivateIp = "172.31.84.180";
		//Compiling, running, and joining main platform command array (for each agent)
    	String[] agentCommands =
                {"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/db_agent.java",
				"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host 172.31.27.189 -agents db:automnsCLI.multi.db_agent",
				"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/authenticator_agent.java",
				"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host 172.31.18.91 -agents Auth:automnsCLI.multi.authenticator_agent",
				"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/image_agent.java",
				"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host 172.31.27.77 -agents Image:automnsCLI.multi.image_agent",
				"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/persistence_agent.java",
				"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host 172.31.25.182 -agents Persistence:automnsCLI.multi.persistence_agent",
				"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/recommender_agent.java",
				"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host 172.31.28.168 -agents Recommender:automnsCLI.multi.recommender_agent",
				"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/registry_agent.java",
				"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host 172.31.19.8 -agents Registry:automnsCLI.multi.registry_agent",
				"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/webui_agent.java",
				"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host 172.31.22.170 -agents Webui:automnsCLI.multi.webui_agent"
				};
		//Initializing SSH sessions to each VM, and executing the agent command array
    	try
        {
			VMFunctions.SSH("3.91.243.239", privateKey, agentCommands[0]);
			VMFunctions.SSH("3.91.243.239", privateKey, agentCommands[1]);
			VMFunctions.SSH("23.22.234.144", privateKey, agentCommands[2]);
			VMFunctions.SSH("23.22.234.144", privateKey, agentCommands[3]);
			VMFunctions.SSH("34.224.25.217", privateKey, agentCommands[4]);
			VMFunctions.SSH("34.224.25.217", privateKey, agentCommands[5]);
			VMFunctions.SSH("34.229.134.229", privateKey, agentCommands[6]);
			VMFunctions.SSH("34.229.134.229", privateKey, agentCommands[7]);
			VMFunctions.SSH("54.91.157.218", privateKey, agentCommands[8]);
			VMFunctions.SSH("54.91.157.218", privateKey, agentCommands[9]);
			VMFunctions.SSH("54.197.8.110", privateKey, agentCommands[10]);
			VMFunctions.SSH("54.197.8.110", privateKey, agentCommands[11]);
			VMFunctions.SSH("54.91.228.138", privateKey, agentCommands[12]);
			VMFunctions.SSH("54.91.228.138", privateKey, agentCommands[13]);
		}
		//Throw a failure in Input & Output operations
        catch (IOException e1)
        {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    }
}
