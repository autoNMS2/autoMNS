
const cp = require('child_process');
const AppMenus = require('./lib/AppMenus');

const appMenus = new AppMenus();

var menu;

// do some initialisation, make sure docker daemon is open etc
appMenus.showSub();
module.exports.menu = menu;
