const main = require('../app')

class Swarm {
    //  input, backFunction, repeateFunction, arg
    //  need to pass functions and argument for return/repeat
    command(input, backFunction, repeateFunction, arg) {
        switch (input) {
            case '1':   
                break;
            case '2':
                break;
            case '3':
                break;
            case '4':
                break;
            case '5':
                break;
            case '0': backFunction(); break;
            default: repeateFunction(arg); break;
        }
    }
}

module.exports = Swarm;

//  Docker Commands

//  commands.runCommand('docker container ls -a', 'Listing Docker Containers', '\nPress Any Key To Continue...', returnFunction, menu);
//  commands.runCommand('docker container prune -f', 'Remove Stopped Containers', '\nPress Any Key To Continue...', returnFunction, menu);
//  we need to run a command, get the return data from it, and run that data as another command
//  var comText = 'docker swarm join --token SWMTKN-1-4d4g7lwgkukzthkzdnk2xlbtyc6u6jjx7cjt1t5r76b3af3iyv-doao4w2bf2gils5ffuqhpcefp 192.168.65.3:2377'
//  commands.runCommand(comText, 'Remove Stopped Containers', '\nPress Any Key To Continue...', returnFunction, menu);
//  commands.runReturnCommand('docker swarm join-token worker', 'Adding Worker', '\nPress Any Key To Continue...', returnFunction, menu);
//  var returnText = commands.getTextFromCommand('docker swarm join-token worker');
//  AppMenuscommands.runCommand('docker swarm join-token worker', 'Adding Worker', '\nPress Any Key To Continue...', returnFunction, menu);
//  let com = 'docker-compose -f ' + __dirname + '\\docker-compose_default.yaml up -d'

//  let com = 'docker run -it ubuntu -d';
//  commands.runCommand(com,
//  'Building Environment (this may take some time...)', '\nPress Any Key To Continue...', returnFunction, menu);

//  list docker images
// commands.runCommand('docker images', 'Listing Docker Images', '\nPress Any Key To Continue...', returnFunction, menu);