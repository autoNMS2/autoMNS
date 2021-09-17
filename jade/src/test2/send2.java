package test2;

import jade.core.*;
import jade.core.Runtime;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

class Main {
    public static void main(String[] args) {
        String host = "172.31.29.138"; // Platform IP
        int port = 1099; // default-port 1099

        String MTP_hostIP = "172.31.29.138";
        String MTP_Port = "7778";

        Runtime runtime = Runtime.instance();

        Profile profile = new ProfileImpl(host, port, null, true);
        profile.setParameter(Profile.MTPS, "jade.mtp.http.MessageTransportProtocol(http://"+MTP_hostIP+":"+MTP_Port+"/acc)");

        // create container
        AgentContainer container = runtime.createMainContainer(profile);

        try {
            AgentController ac = container.createNewAgent("a1", send2.class.getName(),null);
            ac.start();
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }}}


public class send2 extends Agent {

    //private static final long serialVersionUID = 1L;

    @Override
    protected void setup() {

        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        AID dest = new AID("a1@172.31.29.138:1099/JADE", AID.ISGUID);
        dest.addAddresses("http://172.31.29.138:7778/acc");
        msg.addReceiver(dest);
        msg.setContent("Hello!");
        send(msg);
    }}