var Client = require('ssh2').Client;
var conn = new Client();
var i;

class SSSH {  
  SSH(command, ipAddress, key) {
    for (i = 0; i < ipAddress.length; i++){
    console.log(command);
    console.log(ipAddress[i]);
    conn.on('ready', () => {
      console.log('Client :: ready');
      conn.exec(command, (err, stream) => {
        if (err) throw err;
        stream.on('close', (code, signal) => {
          console.log('Stream :: close :: code: ' + code + ', signal: ' + signal);
          conn.end();
        }).on('data', (data) => {
          console.log('STDOUT: ' + data);
        }).stderr.on('data', (data) => {
          console.log('STDERR: ' + data);
        });
      });
    }).connect({
      host: ipAddress[i],
      port: 22,
      username: 'ubuntu',
      privateKey: require('fs').readFileSync(key)
    });
  }
  }

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
}


module.exports = SSSH;

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
