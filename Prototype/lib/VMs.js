const SSSH = require('./SSH');
const sssh = new SSSH();
var command;
var Question;

class VirtualMachine {

    Initialise(returnFunction, menu, input, ipAddress, key) {
        switch (input){
            //case '1': command = 'sudo apt-get update\nsudo apt-get install docker-ce docker-ce-cli containerd.io\nsudo docker run hello-world'; break;
            case '1': command = 'sudo apt-get update -y\n sudo apt-get install -y\
                apt-transport-https -y\
                ca-certificates -y\
                curl -y\
                gnupg -y\
                lsb-release -y\n sudo apt-get install docker.io -y\nsudo docker -v';
                //Question = 'How many VMs would you like to install Docker on: ';
                break;
            case '2': command = 'sudo docker swarm init --advertise-addr ' + ipAddress[0]; 
                //Question = 'How many VMs would you like to add to the Swarm: ';
                break;
            case '3': command = 'sudo docker swarm leave --force';
                //Question = 'How many VMs would you like to remove from the Swarm: ';
                break;
        }
        sssh.SSH(command, ipAddress, key, returnFunction, menu);
    }
}

module.exports = VirtualMachine;