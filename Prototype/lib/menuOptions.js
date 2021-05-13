const chalk = require('chalk');
const clear = require('clear');
const figlet = require('figlet');
var readline = require('readline');

class menuOptions{
    //Clears the terminal and places the title at the top of the screen
    title() {
        clear();
        console.log(
            chalk.yellow(
                figlet.textSync('AutoMnS', { horizontalLayout: 'full' })
            )
        );
    }

    //Creates a readline Interface instance
    refreshMenu(menu) {
        if (menu) menu.close();
        return readline.createInterface({
            input: process.stdin,
            output: process.stdout
        });
    }
}

module.exports = menuOptions;