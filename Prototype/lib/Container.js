
class Container {
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