const menuOptions = require('./menuOptions');
const cp = require('child_process');
const MenuOptions = new menuOptions();

class Commands{
    //  runs the command in cmd, returns to returnfunction
    //  titleMessage and returnMessage can be null
    runCommand(command, titleMessage, returnMessage, returnFunction, menu) {
        MenuOptions.title();
        if (titleMessage) console.log(titleMessage);
    
        console.log('Command: ' + command);
        if (returnMessage);
        else returnMessage = 'Press Any Key to Continue...';
   
        var commandArray = command.split(' ');
        var command = commandArray[0];  // first substring is the command
        commandArray.shift();           // first substring removed, remaining substrings are options
  
        
        var ls = cp.spawn(command, commandArray);   // run the command and options in console

        ls.stderr.on('data', (data) => {
            console.log('ERROR: ' + data);
        });

        ls.stdout.on('data', (data) => {      // output return data
            console.log('' + data);
        });
        
        ls.on('close', (code, signal) => {
            if (returnFunction) {
                // use question to wait for input before returning, this is wrapped in the close function so
                // it will wait for the data to print before continuing
                menu.question(returnMessage, (input) => {
                    returnFunction();   // display return message and wait for any input then go to return function }               
                });
            }
            else console.log(returnMessage);
        });
    }

    runReturnCommand(command, titleMessage, returnMessage, returnFunction, menu) {
        // returns the text from the command
        var commandArray = command.split(' ');
        var command = commandArray[0];  // first substring is the command
        commandArray.shift();           // first substring removed, remaining substrings are options

        var ls = cp.spawn(command, commandArray);   // run the command and options in console

        var datastring;
        var lines;

        ls.stdout.on('data', (data) => {      // output return data
            //  we only want one line of the return text, should be a better way to find it
            datastring = '' + data;
            lines = datastring.split('\n');
            console.log('length: ' + lines.length);
        });

        ls.stderr.on('data', (data) => {
            console.log('ERROR[' + data + ']');
        });

        ls.on('close', (code, signal) => {
            for (var i = 0; i < lines.length; i++) {
                console.log('length ' + i + ': ' + lines[i].length);
                if (i == lines.length - 1) {
                    //  console.log('lines[2]: ' + lines[2]);
                    this.runCommand(lines[2].trim(), titleMessage, returnMessage, returnFunction, menu);
                }
            }
        });
    }
}

module.exports = Commands;





    //  returnCommand(command) {
    //      // returns the text from the command
    //      var commandArray = command.split(' ');
    //      var command = commandArray[0];  // first substring is the command
    //      commandArray.shift();           // first substring removed, remaining substrings are options
    //  
    //      return cp.spawn(command, commandArray);   // run the command and options in console
    //  }

    //  getTextFromCommand(command) {
    //      // returns the text from the command
    //      var commandArray = command.split(' ');
    //      var command = commandArray[0];  // first substring is the command
    //      commandArray.shift();           // first substring removed, remaining substrings are options
    //  
    //      var ls = cp.spawn(command, commandArray);   // run the command and options in console
    //  
    //      ls.stdout.on('data', (data) => {      // output return data
    //          var datastring = '' + data;
    //          var lines = datastring.split('\n');
    //  
    //          for (var i = 0; i < lines.length; i++) {
    //              console.log('length ' + i + ': ' +  lines[i].length);
    //          }
    //  
    //          console.log('length: ' + lines.length);
    //          this.runCommand('' + data, titleMessage, returnMessage, returnFunction, menu);
    //      });
    //  
    //      //  Show errors somehow
    //  
    //      ls.on('close', (code, signal) => {
    //          return datastring;
    //          //  this.runCommand(data, titleMessage, returnMessage, returnFunction, menu);
    //      });
    //  }