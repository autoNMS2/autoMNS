package automnsCLI;

import jade.core.Agent;
import java.io.IOException;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;


import automnsCLI.VMFunctions;

public class coordTest extends Agent
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
				"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host 172.31.24.218 -port 5000 -agents a1:automnsCLI.multi.db_agent"
				};
		//Initializing SSH sessions to each VM, and executing the agent command array
    	try
        {
			VMFunctions.SSH("50.19.17.234", privateKey, agentCommands[0]);
			VMFunctions.SSH("50.19.17.234", privateKey, agentCommands[1]);
		}
		//Throw a failure in Input & Output operations
        catch (IOException e1)
        {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    }
}
