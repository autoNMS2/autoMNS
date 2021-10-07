package automnsCLI.multi;

import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.*;
import jade.lang.acl.*;
import java.io.IOException;
import java.util.Scanner;

public class CoordMenu extends Agent {
    public void menu () throws IOException {
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
                String[] ip = {"3.88.165.202","3.93.17.199"};
                int x = 0;
                int y = 0;
                int z = 1;
                //  try
                //  {
                //      do
                //      {
                //          VMFunctions.noOutputSSH(ip[x], privateKey, agentCommands[y]);
                //          VMFunctions.noOutputSSH(ip[x], privateKey, agentCommands[y+1]);
                //          x++; y+=2; z++;
                //      } while(z<8);
                //  }
                //  //Throw a failure in Input & Output operations
                //  catch (IOException e1)
                //  {
                //      // TODO Auto-generated catch block
                //      e1.printStackTrace();
                //  }
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
                break;
        }

        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        AID dest = new AID("db@172.31.86.85:5000/JADE", AID.ISGUID);
        AID dest1 = new AID("@172.31.92.102:5001/JADE", AID.ISGUID);
        dest.addAddresses("http://172.31.86.85:7778/acc");
        dest1.addAddresses("http://172.31.92.102:7778/acc");
        msg.addReceiver(dest);
        msg.addReceiver(dest1);
        msg.setContent(msgContent);
        send(msg);
    }

    protected void setup(){
        //Defining the path to the AWS key
        String privateKey = "autoMNS/jade/src/test0/test.pem";
        //Defining the IP address of the main platform for other agents to join
        //String coordinatorPrivateIp = "172.31.84.180";
        //Compiling, running, and joining main platform command array (for each agent)
        String[] agentCommands =
                {"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/db_agent.java",
                        "java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host 172.31.86.85 -agents db:automnsCLI.multi.db_agent",
                        "javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/authenticator_agent.java",
                        "java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host 172.31.92.102 -agents Auth:automnsCLI.multi.authenticator_agent"
                };
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