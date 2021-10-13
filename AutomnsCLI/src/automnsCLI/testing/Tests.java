package automnsCLI.testing;

import automnsCLI.VMFunctions;
import org.junit.*;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.*;

public class Tests {

    @Before
    public void setupVMInfo()
    {
        VMFunctions.getVmConfig("C:\\Users\\Computer\\Documents\\GitHub\\autoMNS\\Prototype\\vmsinfo.txt");
    }
    // Test addVMs()
    @Test
    public void testApplyVMConfig() throws IOException
    {
        // integrated
       //   VMFunctions.applyVMConfig();
    }


//    @Test
//    public void testUpdateVMs()
//    {
//        VMFunctions.updateVMs();
//    }
    //  updateVMs();
    //  // add the first vm to a swarm and get the swarm token
    //  addVMsToSwarm();
    //  // install git and pull repository on vms
    //  gitSetup();
    //  // install java on vms
    //  javaSetup();


    @Test
    public void testGetVMConfigCorrectPath()
    {
        assertTrue(VMFunctions.getVmConfig("C:\\Users\\Computer\\Documents\\GitHub\\autoMNS\\Prototype\\vmsinfo.txt"));
    }

    @Test
    public void testGetVMConfigIncorrectPathFalse()
    {
        // This will throw an error in the terminal, that's fine
        assertFalse(VMFunctions.getVmConfig("C:\\Users\\vmsinfo.txt"));
    }

    //  GetVMConfig



    //  Test Case 2


    //

}
