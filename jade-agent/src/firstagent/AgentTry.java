package firstagent;


import jade.core.Agent;

public class AgentTry extends Agent {
    protected void setup() {
        System.out.println("Hello World");
        System.out.println("My name is " + getAID().getName());
    }

}
//agent1:firstagent.AgentTry