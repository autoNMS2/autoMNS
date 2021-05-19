var Client = require('ssh2').Client;
var conn = new Client();
var i;
var Data = new Array();
var joinSwarmCommand;

class SwarmSSSH {
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
          menu.question("Swarm successfully initialised.\nPress any key to continue...", (input) => {
            returnFunction();   // display return message and wait for any input then go to return function }               
          });
        }
      }
      checkData();
    }
  }
}

module.exports = SwarmSSSH;