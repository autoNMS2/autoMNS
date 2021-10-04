package automnsCLI.multi;

import java.io.IOException;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.*;

public class db_agent extends Agent
{
    protected void setup()
    {
	    Runtime r = Runtime.getRuntime();
    	String cmd = "sudo docker stack deploy --compose-file autoMNS/Prototype/lib/Services/db.yaml TeaStore ";
	    try
        {
		    r.exec(cmd);
	    } catch (IOException e)
        {
		    e.printStackTrace();
	    }
    }
}
