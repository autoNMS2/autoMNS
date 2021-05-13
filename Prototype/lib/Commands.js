const menuOptions = require('./menuOptions');
const cp = require('child_process');
const MenuOptions = new menuOptions();

class Commands{
    //  runs the command in cmd, returns to returnfunction
    //  titleMessage and returnMessage can be null
    runCommand(command, titleMessage, returnMessage, returnFunction, menu) {
        MenuOptions.title();
        if (titleMessage) console.log(titleMessage);
    
        console.log(command);
        if (returnMessage);
        else returnMessage = 'Press Any Key to Continue...';
    
       //    menu = MenuOptions.refreshMenu(menu);
    
        //  return function is the function to return to after performinig the command
        //  ie running a command in the main menu should return you to the main menu
        var commandArray = command.split(' ');
        var command = commandArray[0];  // first substring is the command
        commandArray.shift();           // first substring removed, remaining substrings are options
    
        console.log('');    // linebreak
        
        var ls = cp.spawn(command, commandArray);   // run the command and options in console
        
        ls.stdout.on('data', (data) => {      // output return data
            console.log('' + data);
        });
    
        //  Show errors somehow
        
        ls.on('close', (code, signal) => {
            if (returnFunction) {
                // use question to wait for input before returning, this is wrapped in the close function so
                // it will wait for the data to print before continuing
                menu.question(returnMessage, (input) => {
                    returnFunction();   // display return message and wait for any input then go to return function }               
                });
            }
        });
    }
}

module.exports = Commands;