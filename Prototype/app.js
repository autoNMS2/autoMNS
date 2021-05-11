const chalk = require('chalk');
const clear = require('clear');
const figlet = require('figlet');
const http = require('http');
const prompt = require('prompt-sync')({ sigint: true });

var answer;

// Initialize
function initialize() {
    showMain();
}

function title() {
    clear();
    console.log(
        chalk.yellow(
            figlet.textSync('autoMNS', { horizontalLayout: 'full' })
        )
    );
}

function showMain() {
    title();
    answer = prompt('1. Start Component' + '\n' +
        '2. Stop Component' + '\n' +
        '3. Restart Component' + '\n' +
        '4. New Component' + '\n' +
        '5. Delete Component' + '\n' +
        'Please select a number\n');
    switch (answer) {
        case '1':
            startComponent();
            break;
        case '2':
            stopComponent();
            break;
        case '3':
            restartComponent();
            break;
        case '4':
            newComponent();
            break;
        case '5':
            deleteComponent();
            break;
    };
}


function startComponent() {
    title();
    answer = prompt('1. Start Container' + '\n' +
        '2. Start Instance' + '\n' +
        '3. Start Server' + '\n' +
        '4. Back to Main Menu' + '\n' +
        'Please select a number\n');
    switch (answer) {
        case '1':
            answer = prompt('Feature in progress\n ');
            break;
        case '2':
            answer = prompt('Feature in progress\n ');
            break;
        case '3':
            answer = prompt('Feature in progress\n ');
            break;
        case '4':
            showMain();
            break;
    };
}


function stopComponent() {
    title();
    answer = prompt('Which Component would you like to stop?\n' +
        '1. Stop Container' + '\n' +
        '2. Stop Instance' + '\n' +
        '3. Stop Server' + '\n' +
        '4. Back to Main Menu' + '\n' +
        'Please select a number\n');
    switch (answer) {
        case '1':
            answer = prompt('Feature in progress\n ');
            break;
        case '2':
            answer = prompt('Feature in progress\n ');
            break;
        case '3':
            answer = prompt('Feature in progress\n ');
            break;
        case '4':
            showMain();
            break;
    };
}

function restartComponent() {
    title();
    answer = prompt('Which Component would you like to Restart?\n' +
        '1. Restart Container' + '\n' +
        '2. Restart Instance' + '\n' +
        '3. Restart Server' + '\n' +
        '4. Back to Main Menu' + '\n' +
        'Please select a number\n');
    switch (answer) {
        case '1':
            answer = prompt('Feature in progress\n ');
            break;
        case '2':
            answer = prompt('Feature in progress\n ');
            break;
        case '3':
            answer = prompt('Feature in progress\n ');
            break;
        case '4':
            showMain();
            break;
    };
}

function newComponent() {
    title();
    answer = prompt('Which new Component would you like?\n' +
        '1. New Container' + '\n' +
        '2. New Instance' + '\n' +
        '3. New Server' + '\n' +
        '4. Back to Main Menu' + '\n' +
        'Please select a number\n');
    switch (answer) {
        case '1':
            answer = prompt('Feature in progress\n ');
            break;
        case '2':
            answer = prompt('Feature in progress\n ');
            break;
        case '3':
            answer = prompt('Feature in progress\n ');
            break;
        case '4':
            showMain();
            break;
    };
}

function deleteComponent() {
    title();
    answer = prompt('Which Component would you like to Delete?\n' +
        '1. Delete Container' + '\n' +
        '2. Delete Instance' + '\n' +
        '3. Delete Server' + '\n' +
        '4. Back to Main Menu' + '\n' +
        'Please select a number\n');
    switch (answer) {
        case '1':
            answer = prompt('Feature in progress\n ');
            break;
        case '2':
            answer = prompt('Feature in progress\n ');
            break;
        case '3':
            answer = prompt('Feature in progress\n ');
            break;
        case '4':
            showMain();
            break;
    };
}

initialize();
