const menuOptions = require('./menuOptions');
const Commands = require('./Commands');
const MenuDisplay = require('./MenuDisplay');

const display = new MenuDisplay();
const commands = new Commands();
const MenuOptions = new menuOptions();

const serviceName = 'TeaStore';

var readline = require('readline');
const menu = readline.createInterface({
    input: process.stdin,
    output: process.stdout
});

class MainMenu {
    showSub() {
        MenuOptions.title();
        console.log('Select the action you wish to take:' + '\n' +
            '1. Virtual Machines' + '\n' +   //Initialise Virtual Machines
            '2. Docker Swarm Management' + '\n' +
            '3. Service Options' + '\n' +
            '4. Application Menu' + '\n' +
            '0. Exit' + '\n'
        );
    
        //  menu = MenuOptions.refreshMenu(menu);
        // Ask question
        //console.log(this);
        menu.question('Enter the number: ', (input) => {
            console.log(input);
            this.showMain(input);    // little neater
        });
    }

    showMain() {
        var returnFunction = this.showSub.bind(this);

        // this could be a case statement

        if (arguments[0] === '0') { // instead of == 0 , == will compare different types and it considers an empty string == 0, === compares the value and the type are the same
            menu.question('Goodbye! press any key to exit...', (input) => {
                process.exit(); // show goodbye message then wait for input
            });
        }
        else if (arguments[0] == 1) {
            MenuOptions.title();
            var textEnd = '1. ' + 'Add new Virtual Machines' + '\n' +
                '2. ' + 'Use Existing Virtual Machines' + '\n' +
                '0. Main Menu' + '\n' +
                'Please select a number:';
            console.log(textEnd);
            menu.question('', (input) => {
                display.machinesOne(input, this.showSub.bind(this), this.showMain.bind(this), arguments[0], menu);
            });
        }
        else if (arguments[0] == 2) {
            MenuOptions.title();
            var textEnd = '1. ' + 'Initialise Swarm' + '\n' +
                '2. ' + 'Add Worker To Swarm' + '\n' +
                '3. ' + 'Promote Worker to Manager' + '\n' +
                '4. ' + 'Demote Manager to Worker' + '\n' +
                '5. ' + 'Remove Node' + '\n' +
                '6. ' + 'Inspect Node' + '\n' +
                '7. ' + 'View Node Tasks' + '\n' +
                '8. ' + 'Nuke Swarm' + '\n' +
                '0. Main Menu' + '\n' +
                'Please select a number:';

            commands.runCommand('docker node ls',
                'List of Nodes:',
                textEnd, null, menu);
            menu.question('', (input) => {
                display.command(input, this.showSub.bind(this), this.showMain.bind(this), arguments[0], menu);
            });
        }
        else if (arguments[0] == 3) {
            MenuOptions.title();
            var textEnd = '1. ' + 'Create Service' + '\n' +
                '2. ' + 'Scale Service' + '\n' +     //  duplicate
                '3. ' + 'Inspect Service' + '\n' +
                '4. ' + 'Remove Service' + '\n' +
                '5. ' + 'Get Service Logs' + '\n' +
                '6. ' + 'List Service Tasks' + '\n' +
                '7. ' + 'Rollback Service' + '\n' +
                '8. ' + 'Update Service' + '\n' +
                '9. ' + 'Build Environment' + '\n' +
                '10. ' + 'Nuke Environment' + '\n' +
                '0. Main Menu' + '\n' +
                'Please select a number:';

            commands.runCommand('docker service ls',
                'List of Services:',
                textEnd, null, menu);

            menu.question('', (input) => {
                display.services(input, this.showSub.bind(this), this.showMain.bind(this), arguments[0], menu);
            });
        }
        else if (arguments[0] == 4) {   //  Application Menu
            MenuOptions.title();
            var textEnd = '1. ' + 'Open Application' + '\n' +
                '2. ' + '' + '\n' +
                '0. Main Menu' + '\n' +
                'Please select a number:';

            console.log(textEnd);

            menu.question('', (input) => {
                display.application(input, this.showSub.bind(this), this.showMain.bind(this), arguments[0], menu);   
            });         
        }
        else returnFunction(); // if not a valid argument return to main menu
    }
}

module.exports.menu = menu;
module.exports = MainMenu;

    //  showMainText() {   // displays the menu for the first 4 main menu options
    //      MenuOptions.title();
    //      console.log('1. Start Component' + '\n' +
    //          '2. Stop Component' + '\n' +
    //          '3. Restart Component' + '\n' +
    //          '4. New Component' + '\n' +
    //          '5. Delete Component' + '\n' +
    //          '0. Main Menu' + '\n'
    //      );
    //  }


            //  MenuOptions.title();
            //  var textEnd = '1. ' + 'View All Tasks' + '\n' +
            //      '2. ' + 'Inspect Node' + '\n' +
            //      '3. ' + 'View Node Tasks' + '\n' +
            //      '4. ' + 'SSH' + '\n' +
            //      '0. Main Menu' + '\n' +
            //      'Please select a number:';
            //  commands.runCommand('docker node ls',
            //      'List of Nodes:',
            //      textEnd, null, menu);
            //  
            //  menu.question('', (input) => {
            //      //  input, backFunction, repeateFunction, arg
            //      //  need to pass functions and argument for return/repeat
            //      swarm.information(input, this.showSub.bind(this), this.showMain.bind(this), arguments[0], menu);
            //  });


//  var ls = commands.returnCommand('docker swarm join-token worker');
            //  
            //  var returnText = '';
            //  var lines;
            //  
            //  ls.stdout.on('data', (data) => {
            //      returnText += data;
            //  
            //      lines = returnText.split('\n');
            //  
            //      for (var i = 0; i < lines.length; i++) {
            //          console.log('length ' + i + ': ' + lines[i].length);
            //      }
            //  
            //      console.log('length: ' + lines.length);
            //  
            //      console.log(returnText);
            //  });
            //  
            //  ls.on('close', (code, signal) => {
            //      console.log('lines[2]: ' + lines[2]);
            //      commands.runCommand(lines[2], 'Adding Worker', '\nPress Any Key To Continue...', returnFunction, menu);
            //  });
            /*else if (arguments[0] == 1) {
            this.showMainText();
            //  menu = MenuOptions.refreshMenu(menu);
            menu.question('Please select a number: ', (input) => {
                switch (input) {
                    case '1': container.Start(); break;
                    case '2': container.Stop(); break;
                    case '3': container.Restart(); break;
                    case '4': container.New(); break;
                    case '5': container.Delete(); break;
                    case '0': this.showSub(); break;
                    default: this.showMain(arguments[0]); break;
                }    
            });
        }
        else if (arguments[0] == 2) {
            this.showMainText();
            //  menu = MenuOptions.refreshMenu(menu);
            menu.question('Please select a number: ', (input) => {
                switch (input) {
                    case '1': server.Start(); break;
                    case '2': server.Stop(); break;
                    case '3': server.Restart(); break;
                    case '4': server.New(); break;
                    case '5': server.Delete(); break;
                    case '0': this.showSub(); break;
                    default: this.showMain(arguments[0]); break;
                }
            });
        }
        else if (arguments[0] == 3) {
            this.showMainText();
            //  menu = MenuOptions.refreshMenu(menu);
            menu.question('Please select a number: ', (input) => {
                switch (input) {
                    case '1': database.Start(); break;
                    case '2': database.Stop(); break;
                    case '3': database.Restart(); break;
                    case '4': database.New(); break;
                    case '5': database.Delete(); break;
                    case '0': this.showSub(); break;
                    default: this.showMain(arguments[0]); break;
                }
            });
        }
        else if (arguments[0] == 4) {
            this.showMainText();
            //  menu = MenuOptions.refreshMenu(menu);
            menu.question('Please select a number: ', (input) => {
                switch (input) {
                    case '1': name.Start(); break;
                    case '2': name.Stop(); break;
                    case '3': name.Restart(); break;
                    case '4': name.New(); break;
                    case '5': name.Delete(); break;
                    case '0': this.showSub(); break;
                    default: this.showMain(arguments[0]); break;
                }
            });
        }*/