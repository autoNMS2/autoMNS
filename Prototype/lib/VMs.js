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


class VirtualMachine {

    Initialise(returnFunction, menu) {
        AppMenus.menu.question('How many VMs would you like to initialise: ', (input) => {
            instances = input;
            AppMenus.menu.question('Enter the RSA key path of the VMs:\n', (input) => {
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

                sssh.SSH('whoami', ipAddress, key, returnFunction, menu);
            }
            else {
                i++;
                this.recursiveIPLoop(returnFunction, menu);
            }
        });
    }
}

module.exports = VirtualMachine;