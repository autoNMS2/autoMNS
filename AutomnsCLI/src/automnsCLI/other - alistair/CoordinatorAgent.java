package automnsCLI;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.core.behaviours.*;
import jade.lang.acl.*;

import javax.xml.crypto.dsig.keyinfo.KeyValue;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CoordinatorAgent extends Agent {

    //  Service service;
    int index = 0;
    String log;
    String[] services;
    Map<String, String> serviceStatus;
    //  AgentContainer agentContainer;

    void CreateAgent(String service)
    {
        log += "Creating Agent to run " + service + "\n    ";
        AgentController newAgent = ContainerFunctions.CreateAgent(getContainerController(), ServiceAgent.class, new Object[]{getLocalName(), service}, "" + index);
        try {   // made me do this
            assert newAgent != null;
            serviceStatus.put(newAgent.getName(), "Created");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
        index++;
    }

    protected void setup() {
        services = new String[getArguments().length];
        serviceStatus = new HashMap<>();

        for (int i = 0; i < getArguments().length; i++) {
            services[i] = getArguments()[i].toString();
            CreateAgent(services[i]);
        }

        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                //  System.out.println(" CHECK");
                GetMessages();
                block();
                PrintLog();
            }
        });
    }

    void GetMessages() {
        //  if "Killed" remove from list
        //  if "Dead" Start again or something?
        //  if "Starting/Alive" ignore
        ACLMessage msg = receive();

        if (msg != null) {
            log += "New Message\n      Sender: " + msg.getSender().getLocalName() + "\n      Content: " + msg.getContent() + "\n    ";
            //  System.out.println("New Message\n      Sender: " + msg.getSender().getLocalName() + "\n      Content: " + msg.getContent());
            ProcessMessage(msg);
        }
    }

    void ProcessMessage(ACLMessage msg) {
        String sender = msg.getSender().getName();

        if (!msg.getContent().contains("Received")) {
            if (serviceStatus.containsKey(sender)) {    //  check if its a registered service

                if (msg.getContent().contains("Killed"))
                {
                    log += "Agent " + msg.getSender().getLocalName() + " has been killed\n    ";
                    //  System.out.println();
                    //  Restart service or something
                    serviceStatus.remove(sender);
                }
                else serviceStatus.replace(msg.getSender().getName(), msg.getContent());    //  change status
            }
            SimpleReply(msg);
        }
    }

    void SimpleReply(ACLMessage msg)
    {
        ACLMessage reply = msg.createReply();
        reply.setPerformative(ACLMessage.INFORM);
        reply.setContent(msg.getConversationId() + "Received");
        send(reply);
    }

    void PrintLog()
    {
        if (!log.equals(""))
        {
            for (Map.Entry<String, String> pair : serviceStatus.entrySet())
            {
                log += "Agent " + pair.getKey() + " service status: " + pair.getValue() + "\n    ";
            }
            System.out.println("Log From: " + getName() + "\n    " +  log + "End Log");    //  substring(0, log.length()-2)
            log = ""; // Reset log
        }
    }
}