const main = require('../app')
const Commands = require('./Commands');
const SSSH = require('./SSH');
const commands = new Commands();
const serviceName = 'TeaStore';
const sssh = new SSSH();
const VMs = require('./VMs');
const vms = new VMs();
const open = require('open');
const menuOptions = require('./menuOptions');
const MenuOptions = new menuOptions();
var ipAddress = new Array();
var existingIpAddress = new Array();
var key;
var instances;
var i = 0;
var command;
var Question;
var existingKey;
var servicesipAddress = new Array();

class MenuDisplay {   //  Change Class name to Options?
    //  input, backFunction, repeateFunction, arg
    //  need to pass functions and argument for return/repeat    

    machinesOne(input1, backFunction, repeateFunction, arg, menu) {

        switch (input1) {
            case '1':
                MenuOptions.title();
                Question = 'How many Virtual Machines would you like to initialise: ';
                menu.question(Question, (input) => {
                    instances = input;
                    menu.question('Enter the RSA key path of the VM(s):\n', (input) => {
                        key = input;
                        existingKey = key;
                        this.recursiveIPLoop(backFunction, menu, repeateFunction, arg);

                    });
                });
                break;
            /*case '2': 
                if (existingIpAddress[0] != null && existingKey != null){
                this.machinesTwo(existingIpAddress, existingKey, backFunction, menu)
                }
                else {
                    menu.question('No existing Virtual Machines, Please initialise first...', (input) => {
                        backFunction();
                    });
                }
                break;*/
            case '0': backFunction(); break;
            default: repeateFunction(arg); break;
        }
    }


    recursiveIPLoop(backFunction, menu, repeateFunction, arg) {
        menu.question('Enter the IP address of VM ' + (i + 1) + ': ', (input) => {
            ipAddress[i] = input;
            if (existingIpAddress.includes(input)) {
                console.log("Virtual Machine already initialised")
            }
            else {
                existingIpAddress[i] = input;
            }
            if (i == instances - 1) {

                //commands.runReturnCommand('docker swarm join-token worker',
                //    'Adding Worker', '\nPress Any Key To Continue...', returnFunction, menu);
                this.machinesTwo(ipAddress, key, backFunction, menu);
                //sssh.SSH(command, ipAddress, key, returnFunction, menu);
            }
            else {
                i++;
                this.recursiveIPLoop(backFunction, menu, repeateFunction, arg);
            }
        });
    }

    machinesTwo(ipAddress, key, backFunction, menu) {
        var textEnd = "";
        MenuOptions.title();
        if (existingIpAddress[0] != null && existingKey != null) {
            textEnd += 'Existing Virtual Machine IPs: ' + '\n';
            for (let x = 0; x < existingIpAddress.length; x++) {
                textEnd += existingIpAddress[x] + '\n';
            }
            textEnd += '\n';
        }
        textEnd += '1. ' + 'Install Docker on Virtual Machines' + '\n' +
            '2. ' + 'Initialise Swarm on Virtual Machines' + '\n' +
            '3. ' + 'Remove Virtual Machines from Swarm' + '\n' +
            '4. ' + 'Test' + '\n' +
            '5. ' + 'Install repository on Virtual Machines' + '\n' +
            '0. Main Menu' + '\n' +
            'Please select a number:';
        console.log(textEnd);

        menu.question('', (input) => {
            vms.Initialise(backFunction, menu, input, ipAddress, key)
        });
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
        //autoMNS/Prototype/lib/Services
        switch (input) {
            case '1'://  Run a Service
                MenuOptions.title();
                menu.question('Enter the number of a service to run: ' + '\n' +
                    '1. All ' + '\n' +
                    '2. Auth ' + '\n' +
                    '3. Database ' + '\n' +
                    '4. Image ' + '\n' +
                    '5. Persistence ' + '\n' +
                    '6. Recommender ' + '\n' +
                    '7. Registry ' + '\n' +
                    '8. Web UI ' + '\n', (input) => {
                        var choice;
                        switch (input) {
                            case '1':
                                choice = "all";
                                break;
                            case '2':
                                choice = "auth";
                                break;
                            case '3':
                                choice = "db";
                                break;
                            case '4':
                                choice = "image";
                                break;
                            case '5':
                                choice = "persistence";
                                break;
                            case '6':
                                choice = "recommender";
                                break;
                            case '7':
                                choice = "registry";
                                break;
                            case '8':
                                choice = "webui";
                                break;
                        }
                        let comText = 'sudo docker stack deploy --compose-file ' + 'autoMNS/Prototype/lib/Services/' + choice + '.yaml ' + serviceName;
                        this.servicesSSH(comText, backFunction, menu);
                        //commands.runCommand(comText,
                        //    'Adding Image (this may take some time...)', '\nPress Any Key To Continue...', backFunction, menu);

                    });
                break;
            case '2':   //  Scale Service
                MenuOptions.title();
                menu.question('Enter the number of a service to Scale: ' + '\n' +
                    '1. Auth ' + '\n' +
                    '2. Database ' + '\n' +
                    '3. Image ' + '\n' +
                    '4. Persistence ' + '\n' +
                    '5. Recommender ' + '\n' +
                    '6. Registry ' + '\n' +
                    '7. Web UI ' + '\n', (input) => {
                        var choice;
                        switch (input) {
                            case '1':
                                choice = "TeaStore_auth";
                                break;
                            case '2':
                                choice = "TeaStore_db";
                                break;
                            case '3':
                                choice = "TeaStore_image";
                                break;
                            case '4':
                                choice = "TeaStore_persistence";
                                break;
                            case '5':
                                choice = "TeaStore_recommender";
                                break;
                            case '6':
                                choice = "TeaStore_registry";
                                break;
                            case '7':
                                choice = "TeaStore_webui";
                                break;
                        }
                        menu.question('Enter the number of replicas of ' + choice + ' you want to run: ', (number) => {
                            //serviceName
                            let comText = 'sudo docker service scale ' + choice + '=' + number;
                            this.servicesSSH(comText, backFunction, menu);
                        });
                    });
                break;
            case '3':   //  Inspect Service
                MenuOptions.title();
                menu.question('Enter the number of a service to inspect: ' + '\n' +
                    '1. Auth ' + '\n' +
                    '2. Database ' + '\n' +
                    '3. Image ' + '\n' +
                    '4. Persistence ' + '\n' +
                    '5. Recommender ' + '\n' +
                    '6. Registry ' + '\n' +
                    '7. Web UI ' + '\n', (input) => {
                        var choice;
                        switch (input) {
                            case '1':
                                choice = "TeaStore_auth";
                                break;
                            case '2':
                                choice = "TeaStore_db";
                                break;
                            case '3':
                                choice = "TeaStore_image";
                                break;
                            case '4':
                                choice = "TeaStore_persistence";
                                break;
                            case '5':
                                choice = "TeaStore_recommender";
                                break;
                            case '6':
                                choice = "TeaStore_registry";
                                break;
                            case '7':
                                choice = "TeaStore_webui";
                                break;
                        }
                        let comText = 'sudo docker service inspect ' + choice + ' --pretty'
                        this.servicesSSH(comText, backFunction, menu);
                    });
                break;
            case '4':   //  Remove Service
                MenuOptions.title();
                menu.question('Enter the number of a service to remove: ' + '\n' +
                    '1. Auth ' + '\n' +
                    '2. Database ' + '\n' +
                    '3. Image ' + '\n' +
                    '4. Persistence ' + '\n' +
                    '5. Recommender ' + '\n' +
                    '6. Registry ' + '\n' +
                    '7. Web UI ' + '\n', (input) => {
                        var choice;
                        switch (input) {
                            case '1':
                                choice = "TeaStore_auth";
                                break;
                            case '2':
                                choice = "TeaStore_db";
                                break;
                            case '3':
                                choice = "TeaStore_image";
                                break;
                            case '4':
                                choice = "TeaStore_persistence";
                                break;
                            case '5':
                                choice = "TeaStore_recommender";
                                break;
                            case '6':
                                choice = "TeaStore_registry";
                                break;
                            case '7':
                                choice = "TeaStore_webui";
                                break;
                        }
                        let comText = 'sudo docker service rm ' + choice;
                        this.servicesSSH(comText, backFunction, menu);
                    });
                break;
            case '5':   //  Get Logs Service    (Make this print to file)
                MenuOptions.title();
                menu.question('Enter the number of a service to get logs: ' + '\n' +
                    '1. Auth ' + '\n' +
                    '2. Database ' + '\n' +
                    '3. Image ' + '\n' +
                    '4. Persistence ' + '\n' +
                    '5. Recommender ' + '\n' +
                    '6. Registry ' + '\n' +
                    '7. Web UI ' + '\n', (input) => {
                        var choice;
                        switch (input) {
                            case '1':
                                choice = "TeaStore_auth";
                                break;
                            case '2':
                                choice = "TeaStore_db";
                                break;
                            case '3':
                                choice = "TeaStore_image";
                                break;
                            case '4':
                                choice = "TeaStore_persistence";
                                break;
                            case '5':
                                choice = "TeaStore_recommender";
                                break;
                            case '6':
                                choice = "TeaStore_registry";
                                break;
                            case '7':
                                choice = "TeaStore_webui";
                                break;
                        }
                        let comText = 'sudo docker service logs ' + choice;
                        this.servicesSSH(comText, backFunction, menu);
                        //  (If this didnt return an error the command was successful)
                    });
                break;
            case '6':   //  List Service Tasks
            MenuOptions.title();
                menu.question('Enter the number of a service to see tasks: ' + '\n' +
                    '1. Auth ' + '\n' +
                    '2. Database ' + '\n' +
                    '3. Image ' + '\n' +
                    '4. Persistence ' + '\n' +
                    '5. Recommender ' + '\n' +
                    '6. Registry ' + '\n' +
                    '7. Web UI ' + '\n', (input) => {
                        var choice;
                        switch (input) {
                            case '1':
                                choice = "TeaStore_auth";
                                break;
                            case '2':
                                choice = "TeaStore_db";
                                break;
                            case '3':
                                choice = "TeaStore_image";
                                break;
                            case '4':
                                choice = "TeaStore_persistence";
                                break;
                            case '5':
                                choice = "TeaStore_recommender";
                                break;
                            case '6':
                                choice = "TeaStore_registry";
                                break;
                            case '7':
                                choice = "TeaStore_webui";
                                break;
                        }
                        let comText = 'sudo docker service ps ' + choice + '--format';
                        this.servicesSSH(comText, backFunction, menu);
                        //  (If this didnt return an error the command was successful)
                    });
                break;
            case '7':   //  Rollback Service
            MenuOptions.title();
                menu.question('Enter the number of a service to see Rollback: ' + '\n' +
                '1. Auth ' + '\n' +
                '2. Database ' + '\n' +
                '3. Image ' + '\n' +
                '4. Persistence ' + '\n' +
                '5. Recommender ' + '\n' +
                '6. Registry ' + '\n' +
                '7. Web UI ' + '\n', (input) => {
                    var choice;
                        switch (input) {
                            case '1':
                                choice = "TeaStore_auth";
                                break;
                            case '2':
                                choice = "TeaStore_db";
                                break;
                            case '3':
                                choice = "TeaStore_image";
                                break;
                            case '4':
                                choice = "TeaStore_persistence";
                                break;
                            case '5':
                                choice = "TeaStore_recommender";
                                break;
                            case '6':
                                choice = "TeaStore_registry";
                                break;
                            case '7':
                                choice = "TeaStore_webui";
                                break;
                        }
                        let comText = 'sudo docker service rollback ' + choice;
                        this.servicesSSH(comText, backFunction, menu);
                    //  (If this didnt return an error the command was successful)
                });
                break;
            case '8':   //  Update Service
            MenuOptions.title();
                menu.question('Enter the number of a service to see Update: ' + '\n' +
                '1. Auth ' + '\n' +
                '2. Database ' + '\n' +
                '3. Image ' + '\n' +
                '4. Persistence ' + '\n' +
                '5. Recommender ' + '\n' +
                '6. Registry ' + '\n' +
                '7. Web UI ' + '\n', (input) => {
                    var choice;
                        switch (input) {
                            case '1':
                                choice = "TeaStore_auth";
                                break;
                            case '2':
                                choice = "TeaStore_db";
                                break;
                            case '3':
                                choice = "TeaStore_image";
                                break;
                            case '4':
                                choice = "TeaStore_persistence";
                                break;
                            case '5':
                                choice = "TeaStore_recommender";
                                break;
                            case '6':
                                choice = "TeaStore_registry";
                                break;
                            case '7':
                                choice = "TeaStore_webui";
                                break;
                        }
                        let comText = 'sudo docker service update ' + choice;
                        this.servicesSSH(comText, backFunction, menu);
                    //  (If this didnt return an error the command was successful)
                });
                break;
            case '9':   //  Build Environment
            MenuOptions.title();
                let comText = 'sudo docker stack deploy --compose-file ' + 'autoMNS/Prototype/lib/Services/all.yaml ' + serviceName;
                this.servicesSSH(comText, backFunction, menu);
                break;
            case '10':  //  Nuke Environment
            MenuOptions.title();
                let comText2 = 'sudo docker stack rm ' + serviceName;
                this.servicesSSH(comText2, backFunction, menu);
                break;
            case '0': backFunction(); break;
            default: repeateFunction(arg); break;
        }
    }
    servicesSSH(command, backFunction, menu) {
        MenuOptions.title();
        Question = 'How many Virtual Machines would you like to initialise: ';
        menu.question('Enter the RSA key path of the VM(s):\n', (input) => {
            key = input;
            this.servicesGetIp(command, key, backFunction, menu);

        });
    }

    servicesGetIp(command, key, backFunction, menu) {
        menu.question('Enter the IP address of the VM : ', (input) => {
            servicesipAddress[0] = input;
            sssh.SSH(command, servicesipAddress, key, backFunction, menu);
        }
        );
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