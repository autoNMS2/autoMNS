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
        String privateKey = (String) VMs[14];
        System.out.println(privateKey);
		//Defining the IP address of the main platform for other agents to join
    	List<String> workerVMsPrivate = new ArrayList<String>();
    	for (int k = 0; k < 7; k++) {
    		workerVMsPrivate.add((String) VMs[k]);
    	}
    	List<String> workerVMsPublic = new ArrayList<String>();
    	for (int k = 7; k < VMs.length - 1; k++) {
    		workerVMsPublic.add((String) VMs[k]);
    	}
    	
		//Compiling, running, and joining main platform command array (for each agent)
    	String[] agentCommands =
                {"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/db_agent.java",
				"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host " + workerVMsPrivate.get(0) + " -agents db:automnsCLI.multi.db_agent",
				"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/authenticator_agent.java",
				"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host " + workerVMsPrivate.get(1) + " -agents Auth:automnsCLI.multi.authenticator_agent",
				"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/image_agent.java",
				"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host " + workerVMsPrivate.get(2) + " -agents Image:automnsCLI.multi.image_agent",
				"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/persistence_agent.java",
				"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host " + workerVMsPrivate.get(3) + " -agents Persistence:automnsCLI.multi.persistence_agent",
				"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/recommender_agent.java",
				"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host " + workerVMsPrivate.get(4) + " -agents Recommender:automnsCLI.multi.recommender_agent",
				"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/registry_agent.java",
				"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host " + workerVMsPrivate.get(5) + " -agents Registry:automnsCLI.multi.registry_agent",
				"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/webui_agent.java",
				"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host " + workerVMsPrivate.get(6) + " -agents Webui:automnsCLI.multi.webui_agent"
				};
		//Initializing SSH sessions to each VM, and executing the agent command array
    	try
        {
    		int commandCounter = 0;
    		int workerCounter = 0;
    		while(commandCounter < agentCommands.length){
    			for(int m = 0; m < 2; m++) {
    				VMFunctions.SSH(workerVMsPublic.get(workerCounter), privateKey, agentCommands[commandCounter]);
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
