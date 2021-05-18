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
    Initialise(returnFunction) {

        AppMenus.menu.question('How many VMs would you like to initialise: ', (input) => {
            instances = input;
            AppMenus.menu.question('Enter the RSA key path of the VMs:\n', (input) => {
                key = input;
                this.recursiveIPLoop(returnFunction);

            });

        });
    }
    recursiveIPLoop(returnFunction){
        AppMenus.menu.question('Enter the IP address of VM ' + (i + 1) + ': ', (input) => {
            ipAddress[i] = input; 
            if (i == instances - 1){
                sssh.SSH('whoami', ipAddress, key, returnFunction);
            }
            else {
                i++;
                this.recursiveIPLoop();
            }
        });
        
    }
}

module.exports = VirtualMachine;