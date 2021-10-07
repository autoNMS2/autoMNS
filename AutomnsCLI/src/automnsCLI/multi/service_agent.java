package automnsCLI.multi;

import jade.core.Agent;

import java.io.IOException;

public class service_agent extends Agent {
	
    protected void setup()
    {
	    Runtime r = Runtime.getRuntime();
        String service = getArguments()[0].toString();
    	String cmd = "sudo docker stack deploy --compose-file autoMNS/Prototype/lib/Services/" + service + ".yaml TeaStore ";
        try
        {
            r.exec(cmd);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
