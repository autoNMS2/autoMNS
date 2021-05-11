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
        figlet.textSync('autoMNS', {horizontalLayout: 'full'})
    )
);

console.log(
    readline.question('1. Start Component\n2. Stop Component\n3. Restart Component\n4. New Component\n5. Delete Component', answer => {
        readline.close();
      })
);

switch(answer){
    case 1:
        console.log(
            readline.question('Which Component would you like to start?\n ', answer => {
                readline.close();
              })
        );
    break;
    case 2:
        console.log(
            readline.question('Which Component would you like to stop?\n ', answer => {
                readline.close();
              })
        );
    break;
    case 3:

    break;
    case 4:

    break;
    case 5:

    break;        
}