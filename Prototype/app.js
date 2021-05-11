const chalk = require('chalk');
const clear = require('clear');
const figlet = require('figlet');
const http = require('http');
const readline = require('readline').createInterface({
    input: process.stdin,
    output: process.stdout
});


var menuHandler;

// Initialize
function initialize() {
    showMain();
    process.stdin.setEncoding('utf8');
    process.stdin.on('readable', checkMenu);

    function checkMenu() {
        var input = process.stdin.read();
        if(input !== null) {
            menuHandler(input.trim());
        }
    }
}

function title(){
    clear();
    console.log(
        chalk.yellow(
            figlet.textSync('autoMNS', { horizontalLayout: 'full' })
        )
    );
    }

function showMain(){
    title();
    console.log('1. Start Component' + '\n' + 
        '2. Stop Component' + '\n' + 
        '3. Restart Component' + '\n' + 
        '4. New Component' + '\n' + 
        '5. Delete Component' + '\n' + 
        'Please select a number');

        menuHandler = function(input){
            switch(input){
                case '1':
                console.log(
                    readline.question('Which Component would you like to start?\n ', answer => {
                        readline.close();
                    })
                );
                break;
            case '2':
                console.log(
                    readline.question('Which Component would you like to stop?\n ', answer => {
                        readline.close();
                    })
                );
                break;
            case '3':
                console.log(
                    readline.question('Which Component would you like to Restart?\n ', answer => {
                        readline.close();
                    })
                );
                break;
            case '4':
                console.log(
                    readline.question('What new Component would you like to start?\n ', answer => {
                        readline.close();
                    })
                );
                break;
            case '5':;
                console.log(
                    readline.question('Which Component would you like to Delete?\n ', answer => {
                        readline.close();
                    })
                );
                break;
            }
    }
}


initialize();
