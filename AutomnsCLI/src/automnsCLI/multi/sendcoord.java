package automnsCLI.multi;

import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.*;
import jade.lang.acl.*;
import java.io.IOException;
import java.util.Scanner;

public class sendcoord extends Agent {
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
		//
		AID dest = new AID("db@172.31.83.213:5001/JADE", AID.ISGUID);
		dest.addAddresses("http://172.31.83.213:7778/acc");
		msg.addReceiver(dest);
		//
		AID dest1 = new AID("auth@172.31.94.8:5002/JADE", AID.ISGUID);
		dest1.addAddresses("http://172.31.94.8:7778/acc");
		msg.addReceiver(dest1);
		//
		AID dest2 = new AID("image@172.31.82.12:5003/JADE", AID.ISGUID);
		dest2.addAddresses("http://172.31.82.12:7778/acc");
		msg.addReceiver(dest2);
		//
		AID dest3 = new AID("pers@172.31.89.126:5004/JADE", AID.ISGUID);
		dest3.addAddresses("http://172.31.89.126:7778/acc");
		msg.addReceiver(dest3);
		//
		AID dest4 = new AID("recom@172.31.82.46:5005/JADE", AID.ISGUID);
		dest4.addAddresses("http://172.31.82.46:7778/acc");
		msg.addReceiver(dest4);
		//
		AID dest5 = new AID("reg@172.31.80.110:5006/JADE", AID.ISGUID);
		dest5.addAddresses("http://172.31.80.110:7778/acc");
		msg.addReceiver(dest5);
		//
		AID dest6 = new AID("webui@172.31.24.6:5007/JADE", AID.ISGUID);
		dest6.addAddresses("http://172.31.24.6:7778/acc");
		msg.addReceiver(dest6);
		//
		msg.setContent(msgContent);
		send(msg);
	}

	protected void setup() {
						try {
					menu();
				} catch (IOException e) {
					e.printStackTrace();
				}
		addBehaviour(new CyclicBehaviour(this) {
			public void action() {
				ACLMessage msg = receive();
				if (msg != null) {
					System.out.println("Message" + msg.getContent()
							+ " ( " + msg.getSender().getName() + " )");
				}
//				try {
//					menu();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
			}
		});
	}
}
