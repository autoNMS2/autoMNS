const menuOptions = require('./menuOptions');
const Commands = require('./Commands');
const Container = require('./Container');
const Server = require('./Server');
const Database = require('./Database');
const Name = require('./Name');

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
            '6. Main Menu' + '\n'
        );
    }

    showSub() {
        MenuOptions.title();
        console.log('Select component you want to perform actions with:' + '\n' +
            '1. Container' + '\n' +
            '2. Server' + '\n' +
            '3. Database' + '\n' +
            '4. Name placer' + '\n' +
            '5. List Docker Images' + '\n' +
            '6. List Running Containers' + '\n' +
            '7. Prune Stopped Containers' + '\n' +
            '8. NA' + '\n' +
            '9. Build Environment' + '\n' +
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
                    default: this.showSub();
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
                    default: this.showSub();
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
                    default: this.showSub();
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
                    default: this.showSub();
                }
            });
        }
        else if (arguments[0] == 5) {   //[Images Returned Successfully] 
            //  returnfunction.bind(this);
            // this.shoSub.bind(this)
            commands.runCommand('docker images', 'Listing Docker Images', '\nPress Any Key To Continue...', returnFunction, menu);
        }
        else if (arguments[0] == 6) {
            commands.runCommand('docker container ls -a', 'Listing Docker Containers', '\nPress Any Key To Continue...', returnFunction, menu);
        }
        else if (arguments[0] == 7) {
            commands.runCommand('docker container prune -f', 'Remove Stopped Containers', '\nPress Any Key To Continue...', returnFunction, menu);
            //  runCommand('y', '\nPress Any Key To Continue...', showSub);
        }
        else if (arguments[0] == 8) {
            commands.runCommand('', '', '\nPress Any Key To Continue...', returnFunction, menu);
        }
        else if (arguments[0] == 9) {
            //let com = 'docker-compose -f ' + __dirname + '\\docker-compose_default.yaml up -d'
    
            let com = 'docker run -it ubuntu -d';
            commands.runCommand(com,
                'Building Environment (this may take some time...)', '\nPress Any Key To Continue...', returnFunction, menu);
        }
        else returnFunction(); // if not a valid argument return to main menu
    }

}

module.exports.menu = menu;
module.exports = AppMenus;