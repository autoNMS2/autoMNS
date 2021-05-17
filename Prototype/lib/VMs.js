const Commands = require('./Commands');
const SSSH = require('./SSH');
const commands = new Commands();
const serviceName = 'TeaStore';

const sssh = new SSSH();

class VirtualMachine {
    Initialise(){
        console.log('Installing Docker on Virtual Machines\nThis may take some time...');
        
    }
}

module.exports = VirtualMachine;