package automnsCLI;

import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.*;
import jade.lang.acl.*;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.lang.management.OperatingSystemMXBean;


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
    OperatingSystemMXBean bean;

    protected void setup() { // argument[0] is the coordinators id, argument[1] is the process to run

        bean = (com.sun.management.OperatingSystemMXBean) ManagementFactory
                .getOperatingSystemMXBean();

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
        if (cycles > 10000000 )
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

    public long getProcessMemoryUsage()
    {
        MemoryUsage heapMemoryUsage = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
        heapMemoryUsage.getUsed();

        return heapMemoryUsage.getUsed();
    }

    public double getFreePhysicalMemorySize()
    {
        return ((com.sun.management.OperatingSystemMXBean) bean).getFreePhysicalMemorySize();
    }

    public double getProcessCpuLoad()
    {
            return ((com.sun.management.OperatingSystemMXBean) bean).getProcessCpuLoad();
    }

    public double getSystemCpuLoad()
    {
        return ((com.sun.management.OperatingSystemMXBean) bean).getSystemCpuLoad();
    }

    void PrintCost()
    {
        System.out.println("Process Memory: " + getProcessMemoryUsage()/1000000 + "mb");
        System.out.println("Memory Free: " + (long)(getFreePhysicalMemorySize()/1000000) + "mb");

        String processCpuString = (getProcessCpuLoad() + "");
        int maxLength = Math.min(processCpuString.length(), 4);
        processCpuString = (processCpuString.substring(2, maxLength) + "%");

        System.out.println("ProcessCPU: " + processCpuString);

        String systemCpuString = (getSystemCpuLoad() + "");
        maxLength = Math.min(systemCpuString.length(), 4);
        systemCpuString = (systemCpuString.substring(2, maxLength) + "%");

        System.out.println("SystemCPU: " + systemCpuString);
    }

    void PrintLog()
    {
        if (!log.equals(""))
        {
            System.out.println("Log From: " + getName() + "\n    " +  log + "End Log");    //  substring(0, log.length()-2)
            log = ""; // Reset log
            PrintCost();
        }
    }
}