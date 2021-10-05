package automnsCLI;

import jade.core.Agent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import automnsCLI.VMFunctions;

public class JAMEScoordinator extends Agent
{
	//overriding the agent setup
    protected void setup()
    {
    	Object[] VMs = getArguments();
		//Defining the path to the AWS key
        String privateKey = "autoMNS/jade/src/test0/AWSKey.ppk";
		//Defining the IP address of the main platform for other agents to join
    	String coordinatorPrivateIp = (String) VMs[1];
    	List<String> workerVMs = new ArrayList<String>();
    	for (int k = 2; k < VMs.length; k++) {
    		workerVMs.add((String) VMs[k]);
    	}
    	
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
    		int commandCounter = 0;
    		int workerCounter = 0;
    		while(commandCounter < agentCommands.length){
    			for(int m = 0; m < 2; m++) {
    				VMFunctions.SSH(workerVMs.get(workerCounter), privateKey, agentCommands[commandCounter]);
    				commandCounter ++;
    			}
    			workerCounter ++;
    		} 		
//			VMFunctions.SSH("54.164.12.150", privateKey, agentCommands[0]);
//	    	VMFunctions.SSH("54.164.12.150", privateKey, agentCommands[1]);
//	    	VMFunctions.SSH("3.86.155.186", privateKey, agentCommands[2]);
//	    	VMFunctions.SSH("3.86.155.186", privateKey, agentCommands[3]);
//	    	VMFunctions.SSH("3.92.197.213", privateKey, agentCommands[4]);
//	    	VMFunctions.SSH("3.92.197.213", privateKey, agentCommands[5]);
//	    	VMFunctions.SSH("52.87.213.182", privateKey, agentCommands[6]);
//	    	VMFunctions.SSH("52.87.213.182", privateKey, agentCommands[7]);
//	    	VMFunctions.SSH("3.83.121.76", privateKey, agentCommands[8]);
//	    	VMFunctions.SSH("3.83.121.76", privateKey, agentCommands[9]);
//	    	VMFunctions.SSH("3.83.141.155", privateKey, agentCommands[10]);
//	    	VMFunctions.SSH("3.83.141.155", privateKey, agentCommands[11]);
//	    	VMFunctions.SSH("18.205.116.211", privateKey, agentCommands[12]);
//	    	VMFunctions.SSH("18.205.116.211", privateKey, agentCommands[13]);
		}
		//Throw a failure in Input & Output operations
        catch (IOException e1)
        {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    }
}
