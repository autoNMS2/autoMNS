package automnsCLI;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import automnsCLI.VMFunctions;

public class JAMEScoordinator extends Agent
{
	//overriding the agent setup
    protected void setup1()
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

		//	String[] service = new String[]{
		//			"db",
		//			"auth",
		//			"image",
		//			"persistence",
		//			"recommender",
		//			"registry",
		//			"webui",
		//	};
//
		//	String[] agentCommands = new String[workerVMsPrivate.size()*2];
//
		//	for (int i = 0; i < agentCommands.length; i++)
		//	{
		//		agentCommands[i*2] = "javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/service_agent.java";
		//		agentCommands[i*2+1] = "java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host " + workerVMsPrivate.get(i) + " -agents 'service_agent:automnsCLI.multi.service_agent(" + service[i] + ")'";
		//	}

		//Compiling, running, and joining main platform command array (for each agent)
    	String[] agentCommands =
                {"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/db.java",
				"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host " + workerVMsPrivate.get(0) + " -agents db:automnsCLI.multi.service_agent",
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
    				VMFunctions.shellSSH(workerVMsPublic.get(workerCounter), privateKey, agentCommands[commandCounter]);
    				commandCounter ++;
    			}
    			workerCounter ++;
    		} 		
		}
		//Throw a failure in Input & Output operations
        catch (IOException e1)
        {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    }
    
    protected void setup(){

        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    System.out.println("Message" + msg.getContent()
                            + " ( " + msg.getSender().getName() + " )");
                }
                try {
                    menu();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void menu () throws IOException {
        //Defining the path to the AWS key
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

        System.out.println("select command: " +
                "\n 1. Deploy Agents " +
                "\n 2. Deploy Services " +
                "\n 3. Get Services Update" +
                "");

        Scanner scanner = new Scanner(System.in);
        int cmd = scanner.nextInt();
        String msgContent = null;

        switch (cmd)
        {
            case 1:
            	try
                {
            		int commandCounter = 0;
            		int workerCounter = 0;
            		while(commandCounter < agentCommands.length){
            			for(int m = 0; m < 2; m++) {
            				VMFunctions.noOutputSSH(workerVMsPublic.get(workerCounter), privateKey, agentCommands[commandCounter]);
            				commandCounter ++;
            			}
            			workerCounter ++;
            		} 		
        		}
        		//Throw a failure in Input & Output operations
                catch (IOException e1)
                {
        			// TODO Auto-generated catch block
        			e1.printStackTrace();
        		}
                break;
            case 2:
                //msgContent = "Deploy Services";
                Runtime r = Runtime.getRuntime();
                String cmd1 = "sudo docker stack deploy --compose-file autoMNS/Prototype/lib/Services/all.yaml TeaStore ";
                try
                {
                    r.exec(cmd1);
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
                break;
            case 3:
                msgContent = "Get Services Update";
                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                AID dest = new AID("db@172.31.88.236:1009/JADE", AID.ISGUID);
                //AID dest1 = new AID("auth@172.31.85.143:5001/JADE", AID.ISGUID);
                dest.addAddresses("http://172.31.88.236:7778/acc");
                //dest1.addAddresses("http://172.31.85.143:7778/acc");
                msg.addReceiver(dest);
               // msg.addReceiver(dest1);
                msg.setContent(msgContent);
                send(msg);
                break;
        }
    }
    
}
