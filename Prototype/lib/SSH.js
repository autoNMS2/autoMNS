var Client = require('ssh2').Client;
const SwarmSSH = require('./SwarmSSH');
const swarmSSH = new SwarmSSH();
var conn = new Client();
var i;
var joinSwarmCommand;
var SwarmArray = new Array();

class SSSH {
  SSH(command, ipAddress, key, returnFunction, menu) {
    var varLength = ipAddress.length;
    if (command == 'sudo docker swarm init --advertise-addr ' + ipAddress[0] && ipAddress.length > 1) {
      for (let x = 1; x < varLength; x++) {
        SwarmArray[x - 1] = ipAddress[x];
      }
      for (let y = 1; y < varLength; y++) {
        ipAddress.pop();
      }
    }
    for (i = 0; i < ipAddress.length; i++) {
      var Data = new Array();
      //    console.log(command);
      //    console.log(ipAddress[i]);
      conn.on('ready', () => {
        conn.exec(command, (err, stream) => {
          if (err) {
            console.log(err)
          };
          stream.on('close', (code, signal) => {
            conn.end();
          }).on('data', (data) => {
            Data.push(data);
            console.log('Data: ' + data);
            if (command == 'sudo docker swarm init --advertise-addr ' + ipAddress[0]) {
              if (Data.length > 1) {
                joinSwarmCommand = Data[1];
                joinSwarmCommand = joinSwarmCommand.toString().replace("To add a worker to this swarm, run the following command:", "");
                joinSwarmCommand = joinSwarmCommand.replace(/(\r\n|\n|\r)/gm, "");
                joinSwarmCommand.trim();
                joinSwarmCommand = 'sudo ' + joinSwarmCommand;
                console.log(joinSwarmCommand);
                swarmSSH.SSH(joinSwarmCommand, SwarmArray, key, returnFunction, menu);
              }
            }
          }).stderr.on('data', (data) => {
            console.log(command);
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
    }
    function checkData() {
      console.log('Please wait...');
      setTimeout(function () {
        menu.question("Virtual machines successfully initialised.\nPress any key to continue...", (input) => {
          returnFunction();   // display return message and wait for any input then go to return function }               
        });
      }, 5000);
    }
      checkData();
    
  }
}

module.exports = SSSH;
