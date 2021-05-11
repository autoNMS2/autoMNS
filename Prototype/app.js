const chalk = require('chalk');
const clear = require('clear');
const figlet = require('figlet');
const http = require('http');
var readline = require('readline');
var menu;
// Initialize
function title(){
    clear();
    console.log(
        chalk.yellow(
            figlet.textSync('autoMNS', { horizontalLayout: 'full' })
        )
    );
    }
    

function showMain(){
    title();
    console.log('1. Start Component' + '\n' + 
        '2. Stop Component' + '\n' + 
        '3. Restart Component' + '\n' + 
        '4. New Component' + '\n' + 
        '5. Delete Component' + '\n' 
        );
        if(menu) menu.close();

        //Creates a readline Interface instance
        menu = readline.createInterface({
            input: process.stdin,
            output: process.stdout
        });
        if (arguments[0] == 1){
        menu.question('Plesase select a number: ', function(input) {
            switch(input) {
                case '1': Container.Start(); break;
                case '2': Container.Stop(); break;
                case '3': Container.Restart(); break;
                case '4': Container.New(); break;
                case '5': Container.Delete(); break;
                default: showSub();
            }
        });
    };
       if (arguments[0] == 2){
            menu.question('Plesase select a number: ', function(input) {
                switch(input) {
                    case '1': Server.Start(); break;
                    case '2': Server.Stop(); break;
                    case '3': Server.Restart(); break;
                    case '4': Server.New(); break;
                    case '5': Server.Delete(); break;
                    default: showSub();
                }
            });
    
         };   
         if (arguments[0] == 3){
            menu.question('Plesase select a number: ', function(input) {
                switch(input) {
                    case '1': Datebase.Start(); break;
                    case '2': Datebase.Stop(); break;
                    case '3': Datebase.Restart(); break;
                    case '4': Datebase.New(); break;
                    case '5': Datebase.Delete(); break;
                    default: showSub();
                }
            });
    
         }; 
         if (arguments[0] == 4){
            menu.question('Plesase select a number: ', function(input) {
                switch(input) {
                    case '1': Name.Start(); break;
                    case '2': Name.Stop(); break;
                    case '3': Name.Restart(); break;
                    case '4': Name.New(); break;
                    case '5': Name.Delete(); break;
                    default: showSub();
                }
            });
    
         };   
}   
function showSub() {
    title();
    console.log('Select component you want to perform actions with:'+'\n'+
        '1 = Container'+'\n'+
        '2 = Server'+'\n'+
        '3 = Database'+'\n'+
        '4 = Name placer'+'\n'+
        '5 = Go back to main'
        );
    // Check if there is already a menu active. If true, close it.
    if(menu) menu.close();

    // Creates a readline Interface instance
    menu = readline.createInterface({
        input: process.stdin,
        output: process.stdout
    });

    // Ask question
    menu.question('Enter the number: ', function(input) {
    switch(input){
        case '1':
            showMain(1);

            break;
        case '2':
            showMain(2);
            break;
        case '3':
            showMain(3);
             break;
         case '4':
            showMain(4);
             break;
             default: showSub();
             break;
            }
        });   
}
        var Container = {
            Start: function(){
                console.log('You are inside Start Container'
                );
            },
            Stop: function(){
                console.log('You are inside Stop Container'
                );
            },
            Restart: function(){
                console.log('You are inside Restart Container'
                );
            },
            New: function(){
                console.log('You are inside New Container'
                );
            },
            Delete: function(){
                console.log('You are inside Delete Container'
                );
            }
        }
        var Server = {
            Start: function(){
                console.log('You are inside Start Server'
                );
            },
            Stop: function(){
                console.log('You are inside Stop Server'
                );
            },
            Restart: function(){
                console.log('You are inside Restart Server'
                );
            },
            New: function(){
                console.log('You are inside New Server'
                );
            },
            Delete: function(){
                console.log('You are inside Delete Server'
                );
            }
        }
        var Datebase = {
            Start: function(){
                console.log('You are inside Start Datebase'
                );
            },
            Stop: function(){
                console.log('You are inside Stop Datebase'
                );
            },
            Restart: function(){
                console.log('You are inside Restart Datebase'
                );
            },
            New: function(){
                console.log('You are inside New Datebase'
                );
            },
            Delete: function(){
                console.log('You are inside Delete Datebase'
                );
            }
        }
        var Name = {
            Start: function(){
                console.log('You are inside Start Name'
                );
            },
            Stop: function(){
                console.log('You are inside Stop Name'
                );
            },
            Restart: function(){
                console.log('You are inside Restart Name'
                );
            },
            New: function(){
                console.log('You are inside New Name'
                );
            },
            Delete: function(){
                console.log('You are inside Delete Name'
                );
            }
        }
          
showSub();

