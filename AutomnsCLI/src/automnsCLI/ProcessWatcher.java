package automnsCLI;

import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.*;
import jade.lang.acl.*;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.lang.Process;

public class ProcessWatcher extends Agent {

    int cycles;
    Process watchedProcess;
    String watcherWatcher;
    String status;
    String runProcess = "Notepad";

    void StartProcess()
    {
        status = "Starting";
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
        msg.addReceiver(new AID(watcherWatcher, AID.ISLOCALNAME));
        msg.setContent("Process " + watchedProcess.info() +  "\n Status: " + status);
        System.out.println(msg.getContent());
    }

    String GetNewStatus()
    {
        if (watchedProcess != null) {
            if (watchedProcess.isAlive()) {
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
        if (status == null || status != newStatus)  // status change
        {
            status = newStatus;
            return true;
        }
        else return false;
    }

    protected void setup() {
        System.out.println("Agent Initiated " + this.getLocalName() + ", Fullname: [" + this.getName() + "]");
        //receives and output the reply from a1
        watcherWatcher = this.getLocalName();   // this will be the coordinating agent
        StartProcess();

        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                cycles++;
                if (cycles > 10)
                {
                    doDelete();
                }
                System.out.println("Cycle: " + cycles);
                String newStatus = GetNewStatus();
                if (status == "Dead")
                {
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

