var Client = require('ssh2').Client;
var conn = new Client();
var i;
var Data = new Array();
var joinSwarmCommand;

class SSSH {
  SSH(command, ipAddress, key, returnFunction, menu) {
    for (i = 0; i < ipAddress.length; i++) {
      //console.log(command);
      //console.log(ipAddress[i]);
      conn.on('ready', () => {
        conn.exec(command, (err, stream) => {
          if (err) {
            console.log(err)
          };
          stream.on('close', (code, signal) => {
            conn.end();
          }).on('data', (data) => {
            Data.push(data);
            console.log('Data: ' + Data[Data.length - 1]);
            if (command == 'sudo docker swarm init'){
              if (Data.length > 1){
                joinSwarmCommand = Data[1];
                joinSwarmCommand = joinSwarmCommand.toString().replace("To add a worker to this swarm, run the following command:","");
                joinSwarmCommand.trim();
                console.log('Final command: ' + joinSwarmCommand);
                
              }
            }
          }).stderr.on('data', (data) => {
            console.log('STDERR: ' + data);
          });
        });
      }).connect({
        host: ipAddress[i],
        port: 22,
        username: 'ubuntu',
        tryKeyboard: true,
        privateKey: require('fs').readFileSync(key)
      });

      function checkData(){
        if(Data.length !== ipAddress.length){
          console.log('Please wait...');
          setTimeout(checkData, 1000);
        }
        else{
          menu.question("Virtual machines successfully initialised.\nPress any key to continue...", (input) => {
            returnFunction();   // display return message and wait for any input then go to return function }               
          });
        }
      }
      checkData();
      /*if (returnFunction) {
        menu.question("Press any key to continue...", (input) => {
          returnFunction();   // display return message and wait for any input then go to return function }               
        });
      }*/
    }
  }
}

//  set your counter to 1



module.exports = SSSH;
  /*recursiveSSHLoop(command, ipAddress, key) {
ipArray = ipAddress;
if (i == (ipArray[i] - 1)) {
  this.SSH(command, ipAddress[i], key);
}
else {
  this.SSH(command, ipAddress[i], key);
  i++;
  this.recursiveSSHLoop();
}
}
*/
// example output:
// Client :: ready
// STDOUT: Last login: Sun Jun 15 09:37:21 2014 from 192.168.100.100
//
// STDOUT: ls -l
// exit
//
// STDOUT: frylock@athf:~$ ls -l
//
// STDOUT: total 8
//
// STDOUT: drwxr-xr-x 2 frylock frylock 4096 Nov 18  2012 mydir
//
// STDOUT: -rw-r--r-- 1 frylock frylock   25 Apr 11  2013 test.txt
//
// STDOUT: frylock@athf:~$ exit
//
// STDOUT: logout
//
// Stream :: close
