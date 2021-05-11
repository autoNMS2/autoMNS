const chalk = require('chalk');
const clear = require('clear');
const figlet = require('figlet');
const http = require('http');
const Container = require('./lib/Container');
const Server = require('./lib/Server');
const Database = require('./lib/Database');
const Name = require('./lib/Name');

const container = new Container();
const server = new Server();
const database = new Database();
const name = new Name();
var readline = require('readline');
var menu;
// Initialize
function title() {
    clear();
    console.log(
        chalk.yellow(
            figlet.textSync('autoMNS', { horizontalLayout: 'full' })
        )
    );
}


function showMain() {
    title();
    console.log('1. Start Component' + '\n' +
        '2. Stop Component' + '\n' +
        '3. Restart Component' + '\n' +
        '4. New Component' + '\n' +
        '5. Delete Component' + '\n' +
        '6. Main Menu' + '\n'
    );
    if (menu) menu.close();

    //Creates a readline Interface instance
    menu = readline.createInterface({
        input: process.stdin,
        output: process.stdout
    });
    if (arguments[0] == 1) {
        menu.question('Plesase select a number: ', function (input) {
            switch (input) {
                case '1': container.Start(); break;
                case '2': container.Stop(); break;
                case '3': container.Restart(); break;
                case '4': container.New(); break;
                case '5': container.Delete(); break;
                default: showSub();
            }
        });
    };
    if (arguments[0] == 2) {
        menu.question('Plesase select a number: ', function (input) {
            switch (input) {
                case '1': server.Start(); break;
                case '2': server.Stop(); break;
                case '3': server.Restart(); break;
                case '4': server.New(); break;
                case '5': server.Delete(); break;
                default: showSub();
            }
        });
    };
    if (arguments[0] == 3) {
        menu.question('Plesase select a number: ', function (input) {
            switch (input) {
                case '1': database.Start(); break;
                case '2': database.Stop(); break;
                case '3': database.Restart(); break;
                case '4': database.New(); break;
                case '5': database.Delete(); break;
                default: showSub();
            }
        });
    };
    if (arguments[0] == 4) {
        menu.question('Plesase select a number: ', function (input) {
            switch (input) {
                case '1': name.Start(); break;
                case '2': name.Stop(); break;
                case '3': name.Restart(); break;
                case '4': name.New(); break;
                case '5': name.Delete(); break;
                default: showSub();
            }
        });

    };
}

function showSub() {
    title();
    console.log('Select component you want to perform actions with:' + '\n' +
        '1. Container' + '\n' +
        '2. Server' + '\n' +
        '3. Database' + '\n' +
        '4. Name placer' + '\n' +
        '5. Exit Application'
    );
    // Check if there is already a menu active. If true, close it.
    if (menu) menu.close();

    // Creates a readline Interface instance
    menu = readline.createInterface({
        input: process.stdin,
        output: process.stdout
    });

    // Ask question
    menu.question('Enter the number: ', function (input) {
        switch (input) {
            case '1':
                showMain(1);
                break;
            case '2':
                showMain(2);
                break;
            case '3':
                showMain(3);
                break;
            case '4':
                showMain(4);
                break;
            default: process.exit();
                break;
        }
    });
}

showSub();

