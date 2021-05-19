const Commands = require('./Commands');
const SSSH = require('./SSH');
const sssh = new SSSH();
const commands = new Commands();
const serviceName = 'TeaStore';
const AppMenus = require('./AppMenus');
var ipAddress = new Array();
var key;
var instances;
var i = 0;
var command;
var Question;


class VirtualMachine {

    Initialise(returnFunction, menu, input) {
        switch (input){
            //case '1': command = 'sudo apt-get update\nsudo apt-get install docker-ce docker-ce-cli containerd.io\nsudo docker run hello-world'; break;
            case '1': command = 'sudo apt-get update -y\n sudo apt-get install -y\
                apt-transport-https -y\
                ca-certificates -y\
                curl -y\
                gnupg -y\
                lsb-release -y\n sudo apt-get install docker.io -y\nsudo docker -v';
                Question = 'How many VMs would you like to install Docker on: ';
                break;
            case '2': command = 'sudo docker swarm init'; 
                Question = 'How many VMs would you like to add to the Swarm: ';
                break;
            case '3': command = 'sudo docker swarm leave --force';
                Question = 'How many VMs would you like to remove from the Swarm: ';
                break;
        }

        AppMenus.menu.question(Question, (input) => {
            instances = input;
            AppMenus.menu.question('Enter the RSA key path of the VM(s):\n', (input) => {
                key = input;
                this.recursiveIPLoop(returnFunction, menu);
            });
        });
    }
    recursiveIPLoop(returnFunction, menu){
        AppMenus.menu.question('Enter the IP address of VM ' + (i + 1) + ': ', (input) => {
            ipAddress[i] = input; 
            if (i == instances - 1) {

                //commands.runReturnCommand('docker swarm join-token worker',
                //    'Adding Worker', '\nPress Any Key To Continue...', returnFunction, menu);

                sssh.SSH(command, ipAddress, key, returnFunction, menu);
            }
            else {
                i++;
                this.recursiveIPLoop(returnFunction, menu);
            }
        });
    }
}

module.exports = VirtualMachine;