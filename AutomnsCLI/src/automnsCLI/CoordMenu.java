package automnsCLI;

import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.*;
import jade.lang.acl.*;
import java.io.IOException;
import java.util.Scanner;
import automnsCLI.VMFunctions;

public class CoordMenu extends Agent {
    public void menu () throws IOException {
        //Defining the path to the AWS key
        String privateKey = "autoMNS/jade/src/test0/test.pem";
        //Defining the IP address of the main platform for other agents to join
        //String coordinatorPrivateIp = "172.31.84.180";
        //Compiling, running, and joining main platform command array (for each agent)
        String[] agentCommands =
                {"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/db_agent.java",
                        "java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host 172.31.88.236 -port 5000 -agents db:automnsCLI.multi.db_agent",
                        "javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/authenticator_agent.java",
                        "java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host 172.31.85.143 -port 5001 -agents auth:automnsCLI.multi.authenticator_agent"
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
                //msgContent = "Deploy Agents";
                String[] ip = {"54.211.149.7","54.234.172.183"};
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
                    } while(z<3);
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
                AID dest = new AID("db@172.31.88.236:5004/JADE", AID.ISGUID);
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
}