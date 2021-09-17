package test3;

import jade.core.*;
import jade.core.Runtime;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

class Main2 {

    public static void main(String[] args) {
        String host = "172.31.29.138"; // Platform IP
        String port = "1099"; // default-port 1099

        Runtime runtime = Runtime.instance();

        Profile p = new ProfileImpl();
        p.setParameter(Profile.MAIN_HOST, host);
        p.setParameter(Profile.MAIN_PORT, port);
        ContainerController cc = runtime.createAgentContainer(p);

        try {
            AgentController ac = cc.createNewAgent("a0", send3.class.getName(),null);
            ac.start();
        } catch (StaleProxyException e) {e.printStackTrace();}
    }
}

public class send3 extends Agent {
    private static final long serialVersionUID = 1L;
    @Override
    protected void setup() {

        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        AID dest = new AID("a1@172.31.29.138:1099/JADE", AID.ISGUID);
        dest.addAddresses("http://172.31.29.138:7778/acc");
        msg.addReceiver(dest);
        msg.setContent("Hello!");
        send(msg);
    }}