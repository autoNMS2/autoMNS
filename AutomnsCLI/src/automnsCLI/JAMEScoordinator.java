package automnsCLI;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import automnsCLI.VMFunctions;

public class JAMEScoordinator extends Agent
{
	public boolean checkmsg() {

		ACLMessage msg = receive();
		if (msg != null) {

			System.out.println("Message" + msg.getContent()
					+ " ( " + msg.getSender().getName() + " )");

			return true;
		}
		return false;
	}
	protected void setup() {
		addBehaviour(new CyclicBehaviour(this) {

			public void action() {
				try {
					TimeUnit.SECONDS.sleep(1);
				}catch (InterruptedException e) {
				}
				while (checkmsg());
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
                {"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/Worker.java",
				"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host " + workerVMsPrivate.get(0) + " -port 5001 -agents db:automnsCLI.multi.Worker",
				"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/Worker.java",
				"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host " + workerVMsPrivate.get(1) + " -port 5002  -agents Auth:automnsCLI.multi.Worker",
				"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/Worker.java",
				"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host " + workerVMsPrivate.get(2) + " -port 5003  -agents Image:automnsCLI.multi.Worker",
				"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/Worker.java",
				"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host " + workerVMsPrivate.get(3) + " -port 5004  -agents Persistence:automnsCLI.multi.Worker",
				"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/Worker.java",
				"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host " + workerVMsPrivate.get(4) + " -port 5005  -agents Recommender:automnsCLI.multi.Worker",
				"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/Worker.java",
				"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host " + workerVMsPrivate.get(5) + " -port 5006  -agents Registry:automnsCLI.multi.Worker",
				"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/Worker.java",
				"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host " + workerVMsPrivate.get(6) + " -port 5007  -agents Webui:automnsCLI.multi.Worker"
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

        		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        		//
        		AID dest = new AID("db@172.31.81.121:5001/JADE", AID.ISGUID);
        		dest.addAddresses("http://172.31.81.121:7778/acc");
        		msg.addReceiver(dest);
        		//
        		AID dest1 = new AID("auth@172.31.90.166:5002/JADE", AID.ISGUID);
        		dest1.addAddresses("http://172.31.90.166:7778/acc");
        		msg.addReceiver(dest1);
        		//
        		AID dest2 = new AID("image@172.31.82.29:5003/JADE", AID.ISGUID);
        		dest2.addAddresses("http://172.31.82.29:7778/acc");
        		msg.addReceiver(dest2);
        		//
        		AID dest3 = new AID("pers@172.31.89.127:5004/JADE", AID.ISGUID);
        		dest3.addAddresses("http://172.31.89.127:7778/acc");
        		msg.addReceiver(dest3);
        		//
        		AID dest4 = new AID("recom@172.31.89.44:5005/JADE", AID.ISGUID);
        		dest4.addAddresses("http://172.31.89.44:7778/acc");
        		msg.addReceiver(dest4);
        		//
        		AID dest5 = new AID("reg@172.31.91.28:5006/JADE", AID.ISGUID);
        		dest5.addAddresses("http://172.31.91.28:7778/acc");
        		msg.addReceiver(dest5);
        		//
        		AID dest6 = new AID("webui@172.31.94.95:5007/JADE", AID.ISGUID);
        		dest6.addAddresses("http://172.31.94.95:7778/acc");
        		msg.addReceiver(dest6);
        		//
        		msg.setContent(msgContent);
        		send(msg);
                break;
        }
    } 
}
