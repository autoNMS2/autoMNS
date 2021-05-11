const chalk = require('chalk');
const clear = require('clear');
const figlet = require('figlet');
const http = require('http');
const readline = require('readline').createInterface({
    input: process.stdin,
    output: process.stdout
});

clear();

console.log(
    chalk.yellow(
        figlet.textSync('autoMNS', { horizontalLayout: 'full' })
    )
);

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

