package automnsCLI;

import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.*;
import jade.lang.acl.*;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.lang.Process;

public class ServiceAgent extends Agent {

    int cycles;
    Process watchedProcess;
    String coordinatorID;
    String serviceStatus;
    //  String runProcess = "Notepad";

    void StartProcess()
    {
        String runProcess = getArguments()[0].toString();
        serviceStatus = "Starting";
        try {
            //  String line;
            watchedProcess = Runtime.getRuntime().exec(runProcess);
            //  BufferedReader input =
            //          new BufferedReader(new InputStreamReader(watchedProcess.getInputStream()));
            //  while ((line = input.readLine()) != null) {
            //      System.out.println(line); //<-- Parse data here.
            //  }
            //  input.close();
        } catch (Exception err) {
            err.printStackTrace();
        }
        Message();
    }

    void Message()
    {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(new AID(coordinatorID, AID.ISLOCALNAME));
        msg.setContent("Process " + watchedProcess.info() +  "\n Status: " + serviceStatus);
        System.out.println("Message: " + msg.getContent());
    }

    String GetNewStatus()
    {
        if (watchedProcess != null) {
            if (watchedProcess.isAlive()) {
                System.out.println(watchedProcess.toString() + " is alive");
                return "Alive";
            }
            else return "Dead";
        } else
        {
            return "Null";//
        }
    }

    boolean StatusChange(String newStatus)
    {
        if (serviceStatus == null || serviceStatus != newStatus)  // status change
        {
            serviceStatus = newStatus;
            return true;
        }
        else return false;
    }

    protected void setup() {
        System.out.println("Agent Initiated " + this.getLocalName() + ", Fullname: [" + this.getName() + "]");
        //receives and output the reply from a1
        coordinatorID = this.getLocalName();   // this will be the coordinating agent
        StartProcess();

        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                cycles++;
                if (cycles % 10000 == 0)
                {
                    System.out.println("Megacycle: " + cycles/1000);
                }

                String newStatus = GetNewStatus();
                if (watchedProcess.isAlive() == false)//serviceStatus == "Dead")
                {
                    if (cycles > 100000 )
                    {
                        System.out.println(watchedProcess.toString());
                        doDelete();
                    }
                    StartProcess();
                } else
                if (StatusChange(newStatus))
                {
                    Message();
                }
            }
            //          System.out.println(msg.getContent());
            //          send(msg);
            //          //doDelete();
            //          //block();
            //      }
            //  });
            //  // creates a msg and send to a specified agent (a1)
            //  //  ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            //  //  msg.setContent(" Hi from Sender ");
            //  //  msg.addReceiver(new AID("a1", AID.ISLOCALNAME));
            //  //  send(msg);
        });
    }
}

