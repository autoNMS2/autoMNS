var Client = require('ssh2').Client;
const SwarmSSH = require('./SwarmSSH');
const swarmSSH = new SwarmSSH();
var conn = new Client();
var i;
var Data = new Array();
var joinSwarmCommand;
var SwarmArray = new Array();

class SSSH {
  SSH(command, ipAddress, key, returnFunction, menu) {
    if (command == 'sudo docker swarm init' && ipAddress.length > 1){
      for (let x = ipAddress.length; x >  1 ; x--){
        SwarmArray.push(ipAddress[x]);
        ipAddress.pop();
      }
    }
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
                swarmSSH.SSH(joinSwarmCommand, SwarmArray, key, returnFunction, menu);
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
    }
  }
}



module.exports = SSSH;
