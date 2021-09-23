package automnsCLI;

import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.*;
import jade.lang.acl.*;

import java.lang.Process;

public class ServiceAgent extends Agent {

    String log;
    int cycles;
    Process watchedProcess;
    String coordinatorID;
    String serviceStatus;

    //  void Query() throws OntologyException, jade.content.lang.Codec.CodecException{
    //      QueryAgentsOnLocation ca = new QueryAgentsOnLocation();
    //      ca.setLocation(...); // here is the information about you ontainer
    //      Action actExpr = new Action(this.getAMS(), ca);
    //      ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
    //      request.addReceiver(this.getAMS());
    //      request.setOntology(JADEManagementOntology.getInstance().getName());
    //      request.setLanguage(FIPANames.ContentLanguage.FIPA_SL);
    //      request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
    //      this.getContentManager().fillContent(request, actExpr);
    //      this.send(request);
    //      this.get
    //  }

    protected void setup() { // argument[0] is the coordinators id, argument[1] is the process to run
        String runProcess = getArguments()[1].toString();
        log = "Agent Initiated " + this.getLocalName() + ", Fullname: [" + this.getName() + "] Running: " + runProcess;

        coordinatorID = getArguments()[0].toString();   // this will be the coordinating agent

        PrintLog();

        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                GetMessages();
                String newStatus = GetNewStatus();
                if (StatusChange(newStatus))
                {
                    SendMessage(serviceStatus);
                }
                else if (watchedProcess == null || !watchedProcess.isAlive())//serviceStatus == "Dead")
                {
                    StatusChange("Dead");
                    StartProcess();
                }

                CheckEnd(); // ends after x cycles in case of error

                PrintLog();
            }
        });
    }

    void StartProcess()
    {
        String runProcess = getArguments()[1].toString();
        StatusChange("Starting");
        try {
            String[] runArray = runProcess.split(" ");
            log += "Starting " + runProcess + "\n    ";
            watchedProcess = Runtime.getRuntime().exec(runProcess);
        } catch (Exception err) {
            err.printStackTrace();
            End("Could not start process.");
        }
        SendMessage(serviceStatus);
    }

    void SendMessage(String contents)
    {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(new AID(coordinatorID, AID.ISLOCALNAME));
        //  msg.setContent("Process " + watchedProcess.info() +  "\nStatus: " + serviceStatus);
        msg.setContent(contents);
        log += "Sending Message: " + msg.getContent() + "\n    ";
        send(msg);
    }

    void ProcessMessage(ACLMessage msg) {
        if (!msg.getContent().contains("Received"))
        {
            ACLMessage reply = msg.createReply();
            reply.setPerformative(ACLMessage.INFORM);
            reply.setContent(msg.getConversationId() + "Received");
            send(reply);
        }
    }

    void GetMessages() {
        //  if "Killed" remove from list
        //  if "Dead" Start again or something?
        //  if "Starting/Alive" ignore
        ACLMessage msg = receive();

        if (msg != null) {
            log += "New Message\n      Sender: " + msg.getSender().getLocalName() + "\n      Content: " + msg.getContent() + "\n    ";
            //  System.out.println();
            ProcessMessage(msg);
        }
    }

    String GetNewStatus()
    {
        if (watchedProcess != null) {
            if (watchedProcess.isAlive()) {
                return "Alive";
            }
            else
            {
                return "Dead";
            }
        } else
        {
            return "null";
        }
    }

    boolean StatusChange(String newStatus)
    {
        if (serviceStatus != newStatus)  // status change   serviceStatus == null ||
        {
            log += "Status Changed from " + serviceStatus + " to " + newStatus + "\n    ";
            serviceStatus = newStatus;
            return true;
        }
        else return false;
    }

    void CheckEnd()
    {
        cycles++;
        if (cycles > 100000 )
        {
            End("Exceeded Cycle Maximum.");
        }
    }

    void End(String Reason)
    {
        log += "Killed: " + Reason + "\n    ";
        SendMessage("Killed");
        //  PrintLog();
        doDelete();
    }

    void PrintLog()
    {
        if (!log.equals(""))
        {
            System.out.println("Log From: " + getName() + "\n    " +  log + "End Log");    //  substring(0, log.length()-2)
            log = ""; // Reset log
        }
    }
}

