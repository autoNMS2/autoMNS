const main = require('../app')
const Commands = require('./Commands');
const SSSH = require('./SSH');
const commands = new Commands();
const serviceName = 'TeaStore';
const sssh = new SSSH();
const VMs = require('./VMs');
const vms = new VMs();
const open = require('open');

class MenuDisplay {   //  Change Class name to Options?
    //  input, backFunction, repeateFunction, arg
    //  need to pass functions and argument for return/repeat    

    machines(input, backFunction, repeateFunction, arg, menu) {
        switch (input) {
            case '1':   //  Initialise Virtual Machines And Install Docker
                vms.Initialise(repeateFunction, menu, '1');

                break;
            case '2':   //  Initialise Virtual Machines WITHOUT Installing Docker
                vms.Initialise(repeateFunction, menu, '2');

                break;
            case '3':   //  Initialise Virtual Machines WITHOUT Installing Docker
                vms.Initialise(repeateFunction, menu, '3');

                break;
            case '0': backFunction(); break;
            default: repeateFunction(arg); break;
        }
    }

    application(input, backFunction, repeateFunction, arg, menu) {
        switch (input) {
            case '1':
                open('http://localhost:8080');
                repeateFunction();
                break;
            case '2':  
                break;
            case '0': backFunction(); break;
            default: repeateFunction(arg); break;
        }
    }

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
            case '6':   //  inspect Node
                menu.question('Select a node to inspect: ', (input) => {
                    commands.runCommand('docker node inspect ' + input + ' --pretty',
                        'Inspecting Node: ' + input,
                        '\nPress Any Key To Continue...', backFunction, menu);
                });  
                break;
            case '7':   //  View Node Tasks
                menu.question('Select a node to view its tasks: ', (input) => {
                commands.runCommand('docker node ps ' + input,// + ' --format',
                    'Viewing Node Tasks: ' + input,
                    '\nPress Any Key To Continue...', backFunction, menu);
            });
                break;
            case '8': //  Nuke Swarm
                //  leave 
                repeateFunction(arg);
                break;
            //  case '9':
            //      break;
            case '0': backFunction(); break;
            default: repeateFunction(arg); break;
        }
    }

    services(input, backFunction, repeateFunction, arg, menu) {
        //  List Services
        switch (input) {
            case '1'://  Run a Service
                menu.question('Enter a service name to run: ', (input) => {
                    let comText = 'docker stack deploy --compose-file ' + __dirname + '\\Services\\' + input + '.yaml ' + serviceName;
                    commands.runCommand(comText,
                        'Adding Image (this may take some time...)', '\nPress Any Key To Continue...', backFunction, menu);
                });
                break;
            case '2':   //  Scale Service
                menu.question('Enter a service name to scale: ', (service) => {
                    menu.question('Enter the number of replicas of ' + service + ' you want to run: ', (number) => {
                        //serviceName
                        commands.runCommand('docker service scale ' + service + '=' + number,  // add -f
                            'Removing Service: ' + input,
                            '\nPress Any Key To Continue...', backFunction, menu);
                    });
                });
                break;
            case '3':   //  Inspect Service
                menu.question('Select a service to inspect: ', (input) => {
                    commands.runCommand('docker service inspect ' + input + ' --pretty',  // add -f
                        'Inspecting Service: ' + input,
                        '\nPress Any Key To Continue...', backFunction, menu);
                });
                break;
            case '4':   //  Remove Service
                menu.question('Select a service to remove: ', (input) => {
                    commands.runCommand('docker service rm ' + input,  // add -f
                        'Removing Service: ' + input,
                        '\nPress Any Key To Continue... (If this didnt return an error the command was successful)', backFunction, menu);
                });
                break;
            case '5':   //  Get Logs Service    (Make this print to file)
                menu.question('Select a service to get logs: ', (input) => {
                    commands.runCommand('docker service logs ' + input, 
                        'Getting Service Logs: ' + input,
                        '\nPress Any Key To Continue...', backFunction, menu);
                        //  (If this didnt return an error the command was successful)
                });
                break;
            case '6':   //  List Service Tasks
                menu.question('Select a service to see tasks: ', (input) => {
                    commands.runCommand('docker service ps ' + input,
                        'Getting Service Tasks: ' + input,
                        '\nPress Any Key To Continue...', backFunction, menu);
                    //  (If this didnt return an error the command was successful)
                });
                break;
            case '7':   //  Rollback Service
                menu.question('Select a service to rollback: ', (input) => {
                    commands.runCommand('docker service rollback ' + input,
                        'Rolling Back Service: ' + input,
                        '\nPress Any Key To Continue...', backFunction, menu);
                    //  (If this didnt return an error the command was successful)
                });
                break;
            case '8':   //  Update Service
                menu.question('Select a service to update: ', (input) => {
                    commands.runCommand('docker service update ' + input,
                        'Updating Service: ' + input,
                        '\nPress Any Key To Continue...', backFunction, menu);
                    //  (If this didnt return an error the command was successful)
                });
                break;
            case '9':   //  Build Environment
                let comText = 'docker stack deploy --compose-file ' + __dirname + '\\docker-compose_default.yaml ' + serviceName;
                commands.runCommand(comText,
                    'Building Environment (this may take some time...)', '\nPress Any Key To Continue...', backFunction, menu);
                break;
            case '10':  //  Nuke Environment
                let comText3 = 'docker stack rm ' + serviceName;
                commands.runCommand(comText3,
                    'Building Environment (this may take some time...)', '\nPress Any Key To Continue...', backFunction, menu);
                break;
            case '0': backFunction(); break;
            default: repeateFunction(arg); break;
        }
    }
}

module.exports = MenuDisplay;


    //  information(input, backFunction, repeateFunction, arg, menu) {
    //      //  List Nodes
    //      switch (input) {
    //          case '1':           
    //              break;
    //          case '2':
    //              break;
    //          case '3':   //  List Node Tasks
    //              
    //              break;
    //          case '4':  sssh.SSH(); //  search specific service
    //              //  commands.runCommand('docker service ps ' + serviceName,
    //              //      'Showing All Tasks: ' + input,
    //              //      '\nPress Any Key To Continue...', backFunction, menu); 
    //              break;
    //          case '5':
    //              break;
    //          case '6':
    //              break;
    //          case '0': backFunction(); break;
    //          default: repeateFunction(arg); break;
    //      }
    //  }

//  Run Image
//  let comText = 'docker stack deploy --compose-file ' + __dirname + '\\Services\\image.yaml ' + serviceName;
//  commands.runCommand(comText,
//      'Adding Image (this may take some time...)', '\nPress Any Key To Continue...', backFunction, menu);


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