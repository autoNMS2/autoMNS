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
                "");

        Scanner scanner = new Scanner(System.in);
        int cmd = scanner.nextInt();
        String msgContent = null;

        switch (cmd)
        {
            case 1:
                msgContent = "Deploy Agents";
                break;
            case 2:
                msgContent = "Deploy Services";
                break;
        }

        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        AID dest = new AID("db@172.31.86.85:5000/JADE", AID.ISGUID);
        dest.addAddresses("http://172.31.86.85:7778/acc");
        msg.addReceiver(dest);
        msg.setContent(msgContent);
        send(msg);
    }

    protected void setup() {
        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                ACLMessage msg = receive();
                if (msg == null) {
                    try {
                        menu();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    System.out.println("Message"+msg.getContent()
                            + " ( " + msg.getSender().getName()+ " )");
                    menu();
                }
            }
        });
    }
}
