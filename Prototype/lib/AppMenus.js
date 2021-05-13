const menuOptions = require('./menuOptions');
const Commands = require('./Commands');
const Container = require('./Container');
const Server = require('./Server');
const Database = require('./Database');
const Swarm = require('./Swarm');
const Name = require('./Name');

const swarm = new Swarm();

const commands = new Commands();
const MenuOptions = new menuOptions();
const container = new Container();
const server = new Server();
const database = new Database();
const name = new Name();

var readline = require('readline');
const menu = readline.createInterface({
    input: process.stdin,
    output: process.stdout
});
//  var menu;

class AppMenus {
    showMainText() {   // displays the menu for the first 4 main menu options
        MenuOptions.title();
        console.log('1. Start Component' + '\n' +
            '2. Stop Component' + '\n' +
            '3. Restart Component' + '\n' +
            '4. New Component' + '\n' +
            '5. Delete Component' + '\n' +
            '0. Main Menu' + '\n'
        );
    }

    showSub() {
        MenuOptions.title();
        console.log('Select component you want to perform actions with:' + '\n' +
            '1. Container' + '\n' +
            '2. Server' + '\n' +
            '3. Database' + '\n' +
            '4. Name placer' + '\n' +
            '5. Swarm Commands' + '\n' +
            '6. Swarm Information' + '\n' +
            '7. NA' + '\n' +
            '8. NA' + '\n' +
            '9. NA' + '\n' +
            '0. Exit Application' + '\n'
        );
    
        //  menu = MenuOptions.refreshMenu(menu);
        // Ask question
        console.log(this);
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
        }
        else if (arguments[0] == 5) {
            MenuOptions.title();

            var textEnd = '1. ' + 'Initialise Swarm' + '\n' +
                '2. ' + 'Join Worker To Swarm' + '\n' +
                '3. ' + 'Promote Worker to Manager' + '\n' +
                '4. ' + 'Demote Manager to Worker' + '\n' +
                '5. ' + 'Remove Node' + '\n' +
                //Build Environment [LOCALLY]
                '0. Main Menu' + '\n' +
                'Please select a number:';

            commands.runCommand('docker node ls',
                'List of Nodes:',
                textEnd, null, menu);

            //  console.log(
            //      '1. ' + 'Initialise Swarm' + '\n' +
            //      '2. ' + 'Join Worker To Swarm' + '\n' +
            //      '3. ' + 'Build Environment [LOCALLY]' + '\n' +
            //      '4. ' + 'Start Service' + '\n' +
            //      '5. ' + '' + '\n' +
            //      '0. Main Menu' + '\n'
            //  );

            menu.question('', (input) => {
                //  input, backFunction, repeateFunction, arg
                //  need to pass functions and argument for return/repeat
                
                swarm.command(input, this.showSub.bind(this), this.showMain.bind(this), arguments[0], menu);
            });
        }
        else if (arguments[0] == 6) {
            MenuOptions.title();

            var textEnd = '1. ' + 'Inspect Node' + '\n' +
                '2. ' + 'View Node Tasks' + '\n' +
                '3. ' + '' + '\n' +
                '4. ' + '' + '\n' +
                '5. ' + '' + '\n' +
                '0. Main Menu' + '\n' +
                'Please select a number:';

            commands.runCommand('docker node ls',
                'List of Nodes:',
                textEnd, null, menu);

            menu.question('', (input) => {
                //  input, backFunction, repeateFunction, arg
                //  need to pass functions and argument for return/repeat
                swarm.information(input, this.showSub.bind(this), this.showMain.bind(this), arguments[0], menu);
            });
        }
        //  else if (arguments[0] == 7) {
        //      
        //  }
        //  else if (arguments[0] == 8) {
        //     
        //  }
        //  else if (arguments[0] == 9) {
        //      
        //  }
        else returnFunction(); // if not a valid argument return to main menu
    }

}

module.exports.menu = menu;
module.exports = AppMenus;

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