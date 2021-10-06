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
<<<<<<< Updated upstream
=======
<<<<<<< HEAD
    	String[] ip = {"3.91.243.239","23.22.234.144","34.224.25.217","34.229.134.229","54.91.157.218","54.197.8.110","54.91.228.138" };
    	int x = 0;
    	int y = 0;
	int z = 1;
		//Initializing SSH sessions to each VM, and executing the agent command array
    	try
        {
    		do{
    			
			VMFunctions.SSH(ip[x], privateKey, agentCommands[y]);
			VMFunctions.SSH(ip[x], privateKey, agentCommands[y+1]);
    	x++; y+=2; z++;
		}while(z<8);
        }
=======
>>>>>>> Stashed changes
		try
		{
			VMFunctions.noOutputSSH("3.85.29.60", privateKey, agentCommands[0]);
			VMFunctions.noOutputSSH("3.85.29.60", privateKey, agentCommands[1]);
			VMFunctions.noOutputSSH("52.91.100.173", privateKey, agentCommands[2]);
			VMFunctions.noOutputSSH("52.91.100.173", privateKey, agentCommands[3]);
			VMFunctions.noOutputSSH("35.175.243.185", privateKey, agentCommands[4]);
			VMFunctions.noOutputSSH("35.175.243.185", privateKey, agentCommands[5]);
			VMFunctions.noOutputSSH("54.209.82.120", privateKey, agentCommands[6]);
			VMFunctions.noOutputSSH("54.209.82.120", privateKey, agentCommands[7]);
			VMFunctions.noOutputSSH("54.146.135.222", privateKey, agentCommands[8]);
			VMFunctions.noOutputSSH("54.146.135.222", privateKey, agentCommands[9]);
			VMFunctions.noOutputSSH("174.129.161.236", privateKey, agentCommands[10]);
			VMFunctions.noOutputSSH("174.129.161.236", privateKey, agentCommands[11]);
			VMFunctions.noOutputSSH("54.152.173.110", privateKey, agentCommands[12]);
			VMFunctions.noOutputSSH("54.152.173.110", privateKey, agentCommands[13]);
		}
<<<<<<< Updated upstream
=======
>>>>>>> 90dc42f28e3be4775121b654ab8da3a0624c52c3
>>>>>>> Stashed changes
		//Throw a failure in Input & Output operations
        catch (IOException e1)
        {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    }
}
