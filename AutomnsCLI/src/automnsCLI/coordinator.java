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
				"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host 172.31.94.231 -agents db:automnsCLI.multi.db_agent",
				"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/authenticator_agent.java",
				"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host 172.31.85.80 -agents Auth:automnsCLI.multi.authenticator_agent",
				"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/image_agent.java",
				"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host 172.31.83.130 -agents Image:automnsCLI.multi.image_agent",
				"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/persistence_agent.java",
				"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host 172.31.83.157 -agents Persistence:automnsCLI.multi.persistence_agent",
				"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/recommender_agent.java",
				"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host 172.31.82.207 -agents Recommender:automnsCLI.multi.recommender_agent",
				"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/registry_agent.java",
				"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host 172.31.85.39 -agents Registry:automnsCLI.multi.registry_agent",
				"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/webui_agent.java",
				"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host 172.31.95.28 -agents Webui:automnsCLI.multi.webui_agent"
				};

    	String[] ip = {"18.212.74.131","35.171.161.216","35.174.111.173","3.83.106.197","18.233.166.97","3.83.193.82","3.87.208.125" };
    	int x = 0;
    	int y = 0;
		int z = 1;
    	try
        {
    		do
			{
				VMFunctions.noOutputSSH(ip[x], privateKey, agentCommands[y]);
				VMFunctions.noOutputSSH(ip[x], privateKey, agentCommands[y+1]);
				x++; y+=2; z++;
			} while(z<8);
        }
		//Throw a failure in Input & Output operations
        catch (IOException e1)
        {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    }
}
