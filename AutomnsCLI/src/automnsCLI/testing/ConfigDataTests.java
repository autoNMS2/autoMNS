package automnsCLI.testing;

import automnsCLI.VMFunctions;
import org.junit.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class ConfigDataTests {

    String[] parameters;

    // getVmConfig Tests, check the information read from the file is correct and used correctly
    @Before public void setup() {
        VMFunctions.getVmConfig(System.getProperty("user.dir") + "/Prototype/vmsinfo.txt");
        parameters = VMFunctions.getCoordinatorParameters().split(",");
    }

    @Test public void fileExists() {    // can find local key, obviously requires a local key file
        java.io.File f = new java.io.File(VMFunctions.getLocalKeyFilePath());
        assertTrue(f.exists());
    }

    @Test public void testKeyPath() {// check if the repokeyfilepath is a pem or ppk file ie a private key
        assertTrue(VMFunctions.getRepoKeyFilePath().contains(("pem")) ||
                VMFunctions.getRepoKeyFilePath().contains(("ppk")));
    }

    // check parameters are correctly created
    @Test public void testJKeyPathParameter() { // check if the final parameter is a pem or ppk file ie a private key
        assertTrue(parameters[parameters.length - 1].contains(("pem")) ||
                parameters[parameters.length - 1].contains(("ppk")));
    }

    @Test public void testPrivateIPParameter() {
        assertEquals(parameters[0], VMFunctions.getVMPrivateIps().get(1));
    }

    @Test public void testPublicIPParameter() {
        assertEquals(parameters[VMFunctions.getVMPublicIps().size() - 1], VMFunctions.getVMPublicIps().get(1));
    }

    @Test public void testIPWorkerCount() {
        assertEquals(parameters.length % 2, 1); // needs to be an odd number
        assertEquals((parameters.length - 1) / 2, VMFunctions.getVMPrivateIps().size() - 1);   // doesn't take the first ip, (it is the coordinators own ip)
        assertEquals(VMFunctions.getVMPublicIps().size(), VMFunctions.getVMPrivateIps().size());
    }

    @Test public void testCoordinatorCompilationCommand() {
        List<String> ipConfigList = Arrays.asList("localKeyPath", "repoKeyPath", "privateIP1", "publicIP1", "privateIP2", "publicIP2");
        VMFunctions.AssignVMConfig(ipConfigList);
        String[] commands = VMFunctions.getCoordinatorCompilationCommand();
        assertArrayEquals(commands, new String[]
                {"javac -cp autoMNS/jade/lib/jade.jar:autoMNS/jade/lib/jsch-0.1.55.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/*.java",
                        "java -cp autoMNS/jade/lib/jade.jar:autoMNS/jade/lib/jsch-0.1.55.jar:classes jade.Boot -host "
                                + "privateIP1" + " -port 1099 -agents 'coordinator:automnsCLI.JAMEScoordinator("
                                + "privateIP2," + "publicIP2," + "repoKeyPath" + ")'"
                });
    }

    @Test public void testCoordinatorWrongCompilationCommand() {
        List<String> ipConfigList = Arrays.asList("localKeyPath", "repoKeyPath", "privateIP1", "publicIP1", "privateIP2", "publicIP2");
        VMFunctions.AssignVMConfig(ipConfigList);
        String[] commands = VMFunctions.getCoordinatorCompilationCommand();
        assertNotEquals(commands[0], "javac -cp autoMNS/jade/lib/jade.jar:autoMNS/jade/lib/jsch-0.1.55.ERROR HERE -d classes autoMNS/AutomnsCLI/src/automnsCLI/*.java");
        assertNotEquals(commands[1], "java -cp autoMNS/jade/lib/jade.jar:autoMNS/jade/lib/jsch-0.1.55.jar:classes jade.Boot -host "
                + "privateIP1" + " -port 1099 -agents 'coordinator:automnsCLI.JAMEScoordinator("
                + "privateIP2," + "publicIP2," + "ERROR HERE" + ")'"
        );
    }

    @Test public void testAssignVMConfig() {
        List<String> ipConfigList = Arrays.asList("localKeyPath", "repoKeyPath", "privateIP1", "publicIP1", "privateIP2", "publicIP2");
        VMFunctions.AssignVMConfig(ipConfigList);
        assertEquals(ipConfigList.get(0), VMFunctions.getLocalKeyFilePath());
        assertEquals(ipConfigList.get(1), VMFunctions.getRepoKeyFilePath());
        assertEquals(ipConfigList.get(2), VMFunctions.getVMPrivateIps().get(0));
        assertEquals(ipConfigList.get(3), VMFunctions.getVMPublicIps().get(0));
        assertEquals(ipConfigList.get(4), VMFunctions.getVMPrivateIps().get(1));
        assertEquals(ipConfigList.get(5), VMFunctions.getVMPublicIps().get(1));
    }

    //  these are not valid JUnit tests, but one should open google, and the other
    //  should open a page with an url of the second public ip on port 8080
    @Test public void testLaunchApplication() throws IOException {
        //  VMFunctions.launchApplication();
    }

    @Test public void testOpenURL() {
        //  VMFunctions.openWebpage("http://google.com");
    }

    @Test public void testParseJoinToken() {
        String input = "\n\n this line contains docker swarm join\n\n";
        String output = VMFunctions.parseJoinToken(input);
        assertEquals(output, "sudo this line contains docker swarm join");
    }

    @Test public void testParseJoinTokenInvalid() {
        String input = "\n\n this line contains docker swa BUT WITH THIS IN THE MIDDLE rm join\n\n";
        String output = VMFunctions.parseJoinToken(input);
        assertNotEquals(output, "sudo this line contains docker swa BUT WITH THIS IN THE MIDDLE rm join");
    }

//    @Test public void testGetJoinTokenOutput() throws IOException {
//
//        String command = "docker swarm init --advertise-addr " + Inet4Address.getLocalHost();
//        String commandTwo = "docker swarm join-token worker";
//        String output = "";
//
//        try {
//            Runtime r = Runtime.getRuntime();
//            r.exec(command);
//            Process process = r.exec(commandTwo);
//            BufferedReader stdInput = new BufferedReader(new
//                    InputStreamReader(process.getInputStream()));
//            Thread.sleep(5000);
//            String line = null;
//            while ((line = stdInput.readLine()) != null) {
//                output += line;
//            }
//
//            r.exec("docker swarm leave --force");
//
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        assertEquals("12345", output);  // not getting any output
//        assertTrue(output.contains("docker"));
//    }
}
