package automnsCLI.testing;

import automnsCLI.VMFunctions;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.junit.*;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class SSHTests {

    String vmip;
    String privateKey;
    String command;
    Channel channel;
    Session session;

//    addVMs
//    applyVMConfig
//    updateVMs
//    addVMsToSwarm
//    gitSetup
//    javaSetup
//    inputVMConfigPath
//    getVmConfig
//    AssignVMConfig
//    initialiseAgents
//    launchApplication
//    openWebpage

    @Before public void setupVMInfo()
    {
        //  These tests require
        //  running vms
        //  correctly configured vm file
        //  matching private keys on local host and vms

        VMFunctions.getVmConfig("C:\\Users\\Computer\\Documents\\GitHub\\autoMNS\\Prototype\\vmsinfo.txt");
        command = "echo hello"; // nice easy command to test
        privateKey = "C:\\Users\\Computer\\Documents\\GitHub\\autoMNS\\automnskey.pem";
        vmip = VMFunctions.getVMPublicIps().get(0);
    }

    // Test individual modules of ssh connections
    @Test public void testSSHInvalidIP() throws IOException { //  test if the command is correctly run and returned
        assertThrows( RuntimeException.class, () -> {VMFunctions.SSH("invalid", privateKey, command, false);} );
    }   // invalid key, invalid command

    // getSSHSession
    @Test public void testGetSSHSession() { //  tests if a session with the correct host is created
        session = VMFunctions.getSSHSession(vmip, privateKey);
        assertEquals(session.getHost(), vmip);
    }   // test other characteristics to determine if session is valid

    // runSSHCommand
    @Test public void testRunSSHCommand() { // returns a channel whose connection is tested
        session = VMFunctions.getSSHSession(vmip, privateKey);
        assertTrue(VMFunctions.runSSHCommand(session, command).isConnected());
    }
    @Test public void testRunSSHCommandInvalidIP() { // incorrect connection should throw an exception
        session = VMFunctions.getSSHSession("invalid", privateKey);
        assertThrows( RuntimeException.class, () -> {VMFunctions.runSSHCommand(session, command);} );
    }

    // runShellCommand
    @Test public void testRunShellCommand() throws IOException { // returns if a shell channel is connected
        session = VMFunctions.getSSHSession(vmip, privateKey);
        assertTrue(VMFunctions.runShellCommand(session, command).isConnected());
    }
    @Test public void testRunShellCommandInvalidIP() throws IOException { // returns if a shell channel is connected
        session = VMFunctions.getSSHSession("invalid", privateKey);
        assertThrows( RuntimeException.class, () -> {VMFunctions.runShellCommand(session, command);} );
    }

    // getSSHOutput
    @Test public void testGetSSHOutput() throws IOException { // returns if a shell channel is connected
        command = "echo hello"; // output should just be hello (with a new line)
        session = VMFunctions.getSSHSession(vmip, privateKey);
        channel = VMFunctions.runSSHCommand(session, command);
        assertEquals("hello\n", VMFunctions.getSSHOutput(channel, session, false));
    }
    @Test public void testGetSSHOutputInvalid() throws IOException { // returns if a shell channel is connected
        command = "echo hello"; // output should just be hello (with a new line)
        session = VMFunctions.getSSHSession(vmip, privateKey);
        channel = null;
        assertThrows( RuntimeException.class, () -> {VMFunctions.getSSHOutput(channel, session, false);} );
    }

    // test integrated methods that use above modules
    // SSH
    @Test public void testSSH() throws IOException { // command run correctly should return an output
        command = "echo hello"; // output should just be hello (with a new line)
        assertEquals("hello\n", VMFunctions.SSH(vmip, privateKey, command, false));
    }

    // noOutputSSH
    // shellSSH
}
