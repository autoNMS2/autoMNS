const main = require('../app')
const Commands = require('./Commands');
const commands = new Commands();
const serviceName = 'TeaStore';

//  Wrap a runCommand in a menu.question to add some user input

//  menu.question('Select a node to remove: ', (input) => {
//      commands.runCommand('docker node rm ' + input + ' --pretty',
//          'Removing Node: ' + input,
//          '\nPress Any Key To Continue...', backFunction, menu);
//  });  


class Swarm {
    //  input, backFunction, repeateFunction, arg
    //  need to pass functions and argument for return/repeat
    command(input, backFunction, repeateFunction, arg, menu) {
        switch (input) {
            case '1':   //  init swarm
                commands.runCommand('docker swarm init',
                    'Building Environment (this may take some time...)', '\nPress Any Key To Continue...', backFunction, menu);
                break;
            case '2':   //  join worker to swarm
                commands.runReturnCommand('docker swarm join-token worker',
                    'Adding Worker', '\nPress Any Key To Continue...', backFunction, menu);
                break;
            case '3':   //  Promote
                menu.question('Select a node to promote: ', (input) => {
                    commands.runCommand('docker node promote ' + input,  // add -f
                        'Promoting Node: ' + input,
                        '\nPress Any Key To Continue...', backFunction, menu);
                }); 
                break;
            case '4':   // Demote
                menu.question('Select a node to demote: ', (input) => {
                    commands.runCommand('docker node demote ' + input,  // add -f
                        'Demoting Node: ' + input,
                        '\nPress Any Key To Continue...', backFunction, menu);
                }); 
                break;
            case '5':   //  Remove Node
                menu.question('Select a node to remove: ', (input) => {
                    commands.runCommand('docker node rm ' + input,  // add -f
                        'Removing Node: ' + input,
                        '\nPress Any Key To Continue...', backFunction, menu);
                }); 
                break;
            case '6':   //  Remove Node
                menu.question('Select a node to remove: ', (input) => {
                    commands.runCommand('docker node rm ' + input,  // add -f
                        'Removing Node: ' + input,
                        '\nPress Any Key To Continue...', backFunction, menu);
                });
                break;
            case '9': //  Build Environment LOCALLY

                //  docker stack deploy --compose-file docker-compose.yml stackdemo
                //docker service create --name registry--publish published = 5000, target = 5000 registry: 2


                let comText = 'docker stack deploy --compose-file ' + __dirname + '\\docker-compose_default.yaml ' + serviceName;    //up -d';
                //  let comText = 'docker run -i alpine';   //  can't run environment on my laptop :(
                //  also this will hijack the app 
                commands.runCommand(comText,
                    'Building Environment (this may take some time...)', '\nPress Any Key To Continue...', backFunction, menu);
                break;
            case '0': backFunction(); break;
            default: repeateFunction(arg); break;
        }
    }

    information(input, backFunction, repeateFunction, arg, menu) {
        //  List Nodes
        switch (input) {
            case '1'://  View all tasks
                commands.runCommand('docker service ps ' + serviceName,
                    'Showing All Tasks: ' + input,
                    '\nPress Any Key To Continue...', backFunction, menu);                
                break;
            case '2'://  inspect Node
                menu.question('Select a node to inspect: ', (input) => {
                    //  input, backFunction, repeateFunction, arg
                    //  need to pass functions and argument for return/repeat
                    commands.runCommand('docker node inspect ' + input + ' --pretty',
                        'Inspecting Node: ' + input,
                        '\nPress Any Key To Continue...', backFunction, menu);
                });  
                break;
            case '3':   //  List Node Tasks
                menu.question('Select a node to view its tasks: ', (input) => {
                    //  input, backFunction, repeateFunction, arg
                    //  need to pass functions and argument for return/repeat
                    commands.runCommand('docker node ps ' + input,// + ' --format',
                        'Viewing Node Tasks: ' + input,
                        '\nPress Any Key To Continue...', backFunction, menu);
                });                  
                break;
            case '4':
                break;
            case '5':
                break;
            case '6':
                break;
            case '0': backFunction(); break;
            default: repeateFunction(arg); break;
        }
    }
}

module.exports = Swarm;

//  Docker Commands

    //  Promote/Demote
    //  Promote/Demote
    //  Promote/Demote
    //  Promote/Demote
    //  Promote/Demote
    //  Promote/Demote


//  commands.runCommand('docker container ls -a', 'Listing Docker Containers', '\nPress Any Key To Continue...', returnFunction, menu);
//  commands.runCommand('docker container prune -f', 'Remove Stopped Containers', '\nPress Any Key To Continue...', returnFunction, menu);
//  we need to run a command, get the return data from it, and run that data as another command

// var comText = 'docker swarm join --token SWMTKN-1-4d4g7lwgkukzthkzdnk2xlbtyc6u6jjx7cjt1t5r76b3af3iyv-doao4w2bf2gils5ffuqhpcefp 192.168.65.3:2377'
// commands.runCommand(comText, 'Remove Stopped Containers', '\nPress Any Key To Continue...', returnFunction, menu);
// commands.runReturnCommand('docker swarm join-token worker', 'Adding Worker', '\nPress Any Key To Continue...', returnFunction, menu);
// var returnText = commands.getTextFromCommand('docker swarm join-token worker');

//  AppMenuscommands.runCommand('docker swarm join-token worker', 'Adding Worker', '\nPress Any Key To Continue...', returnFunction, menu);
//  let com = 'docker-compose -f ' + __dirname + '\\docker-compose_default.yaml up -d'


//  let com = 'docker run -it ubuntu -d';
//  commands.runCommand(com,
//  'Building Environment (this may take some time...)', '\nPress Any Key To Continue...', returnFunction, menu);

//  list docker images
//  commands.runCommand('docker images', 'Listing Docker Images', '\nPress Any Key To Continue...', returnFunction, menu);