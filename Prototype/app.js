const chalk = require('chalk');
const clear = require('clear');
const figlet = require('figlet');
const http = require('http');
const readline = require('readline').createInterface({
    input: process.stdin,
    output: process.stdout
});

function title(){
    clear();
    console.log(
        chalk.yellow(
            figlet.textSync('autoMNS', { horizontalLayout: 'full' })
        )
    );
    }

console.log(
    readline.question('1. Start Component\n2. Stop Component\n3. Restart Component\n4. New Component\n5. Delete Component\nPlease select a number', answer => {
        switch (answer) {
            case '1':
                clear();
                console.log(
                    chalk.yellow(
                        figlet.textSync('autoMNS', { horizontalLayout: 'full' })
                    )
                );
                console.log(
                    readline.question('Which Component would you like to start?\n ', answer => {
                        readline.close();
                    })
                );
                break;
            case '2':
                clear();
                console.log(
                    chalk.yellow(
                        figlet.textSync('autoMNS', { horizontalLayout: 'full' })
                    )
                );
                console.log(
                    readline.question('Which Component would you like to stop?\n ', answer => {
                        readline.close();
                    })
                );
                break;
            case '3':
                clear();

                console.log(
                    chalk.yellow(
                        figlet.textSync('autoMNS', { horizontalLayout: 'full' })
                    )
                );
                console.log(
                    readline.question('Which Component would you like to Restart?\n ', answer => {
                        readline.close();
                    })
                );
                break;
            case '4':
                clear();
                console.log(
                    chalk.yellow(
                        figlet.textSync('autoMNS', { horizontalLayout: 'full' })
                    )
                );
                console.log(
                    readline.question('What new Component would you like to start?\n ', answer => {
                        readline.close();
                    })
                );
                break;
            case '5':
                clear();
                console.log(
                    chalk.yellow(
                        figlet.textSync('autoMNS', { horizontalLayout: 'full' })
                    )
                );
                console.log(
                    readline.question('Which Component would you like to Delete?\n ', answer => {
                        readline.close();
                    })
                );
                break;
        }
    })
);


function showMain(){
    console.log('1. Start Componen' + '\n' + 
    '2. Stop Component' + '\n' + 
    '3. Restart Component' + '\n' + 
    '4. New Component' + '\n' + 
    '5. Delete Component' + '\n' + 
    'Please select a number');


}

