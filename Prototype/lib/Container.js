const main = require('../app')

class Container {
    Menu(input){
        switch (input) {
            case '1': this.Start(); break;
            case '2': this.Stop(); break;
            case '3': this.Restart(); break;
            case '4': this.New(); break;
            case '5': this.Delete(); break;
            default: main.showSub();
        }
    }

    Start(){
        console.log('You are inside Start Container');
    }
    Stop() {
        console.log('You are inside Stop Container');
    }
    Restart() {
        console.log('You are inside Restart Container');
    }
    New() {
        console.log('You are inside New Container');
    }
    Delete() {
        console.log('You are inside Delete Container');
    }
}

module.exports = Container;