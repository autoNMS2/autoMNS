const Container = require('./lib/Container');
const Server = require('./lib/Server');
const Database = require('./lib/Database');
const Name = require('./lib/Name');
const cp = require('child_process');
const menuOptions = require('./lib/menuOptions');
var readline = require('readline');

const MenuOptions = new menuOptions();
const container = new Container();
const server = new Server();
const database = new Database();
const name = new Name();

var menu;


function showMainText() {   // also refreshes menu which should be its own function really
    MenuOptions.title();
    console.log('1. Start Component' + '\n' +
        '2. Stop Component' + '\n' +
        '3. Restart Component' + '\n' +
        '4. New Component' + '\n' +
        '5. Delete Component' + '\n' +
        '6. Main Menu' + '\n'
    );
    menu = MenuOptions.refreshMenu(menu);
}


function showMain() {
    if (arguments[0] == 0) {
        menu.question('Goodbye! press any key to exit...', (input) => {
            process.exit(); // show goodbye message then wait for input
        });
    }
    else if (arguments[0] == 1) {
        showMainText();
        menu.question('Please select a number: ', (input) => {
            switch (input) {
                case '1': container.Start(); break;
                case '2': container.Stop(); break;
                case '3': container.Restart(); break;
                case '4': container.New(); break;
                case '5': container.Delete(); break;
                default: showSub();
            }

        });
    }
    else if (arguments[0] == 2) {
        showMainText();
        menu.question('Please select a number: ', function (input) {
            switch (input) {
                case '1': server.Start(); break;
                case '2': server.Stop(); break;
                case '3': server.Restart(); break;
                case '4': server.New(); break;
                case '5': server.Delete(); break;
                default: showSub();
            }
        });
    }
    else if (arguments[0] == 3) {
        showMainText();
        menu.question('Please select a number: ', function (input) {
            switch (input) {
                case '1': database.Start(); break;
                case '2': database.Stop(); break;
                case '3': database.Restart(); break;
                case '4': database.New(); break;
                case '5': database.Delete(); break;
                default: showSub();
            }
        });
    }
    else if (arguments[0] == 4) {
        showMainText();
        menu.question('Please select a number: ', function (input) {
            switch (input) {
                case '1': name.Start(); break;
                case '2': name.Stop(); break;
                case '3': name.Restart(); break;
                case '4': name.New(); break;
                case '5': name.Delete(); break;
                default: showSub();
            }
        });
    }
    else if (arguments[0] == 5) {   //[Images Returned Successfully] 
        runCommand('docker images', 'Listing Docker Images', '\nPress Any Key To Continue...', showSub);
    }
    else if (arguments[0] == 6) {
        runCommand('docker container ls -a', 'Listing Docker Containers', '\nPress Any Key To Continue...', showSub);
    }
    else if (arguments[0] == 7) {
        runCommand('docker container prune -f', 'Remove Stopped Containers', '\nPress Any Key To Continue...', showSub);
        //  runCommand('y', '\nPress Any Key To Continue...', showSub);
    }
    else if (arguments[0] == 8) {
        runCommand('', '', '\nPress Any Key To Continue...', showSub);
    }
    else if (arguments[0] == 9) {
        //let com = 'docker-compose -f ' + __dirname + '\\docker-compose_default.yaml up -d'

        let com = 'docker run -it ubuntu -d';
        runCommand(com,
            'Building Environment (this may take some time...)', '\nPress Any Key To Continue...', showSub);
    }
    else showSub(); // if not a valid argument return to main menu
}

//  runs the command in cmd, returns to returnfunction
//  titleMessage and returnMessage can be null
function runCommand(command, titleMessage, returnMessage, returnFunction) {
    MenuOptions.title();
    if (titleMessage) console.log(titleMessage);

    console.log(command);
    if (returnMessage);
    else returnMessage = 'Press Any Key to Continue...';

    menu = MenuOptions.refreshMenu(menu);

    //  return function is the function to return to after performinig the command
    //  ie running a command in the main menu should return you to the main menu
    var commandArray = command.split(' ');
    var command = commandArray[0];  // first substring is the command
    commandArray.shift();           // first substring removed, remaining substrings are options

    console.log('');    // linebreak
    
    var ls = cp.spawn(command, commandArray);   // run the command and options in console
    
    ls.stdout.on('data', function (data) {      // output return data
        console.log('' + data);
    });

    //  Show errors somehow
    
    ls.on('close', function (code, signal) {
        if (returnFunction) {
            // use question to wait for input before returning, this is wrapped in the close function so
            // it will wait for the data to print before continuing
            menu.question(returnMessage, function (input) {

                returnFunction();   // display return message and wait for any input then go to return function }               
            });
        }
    });
}

function showSub() {
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

    menu = MenuOptions.refreshMenu(menu);

    // Ask question
    menu.question('Enter the number: ', function (input) {
        showMain(input);    // little neater
        //  I tried doing the same thing with the classes but couldn't get it to work
        //  don't really understand js classes
    });
}

// do some initialisation, make sure docker daemon is open etc
module.exports.menu = menu;
showSub();