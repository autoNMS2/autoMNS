class Server{
    Start() {
        console.log('You are inside Start Server');
    }
    Stop() {
        console.log('You are inside Stop Server');
    }
    Restart() {
        console.log('You are inside Restart Server');
    }
    New() {
        console.log('You are inside New Server');
    }
    Delete() {
        console.log('You are inside Delete Server');
    }
}

module.exports = Server;