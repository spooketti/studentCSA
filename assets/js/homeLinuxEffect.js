let linuxTerminal = document.getElementById("linuxPre")
let path1 = "<span class='linuxGreen'>jonathan@liu</span>:<span class='linuxBlue'>/studentCSA/sampleProjects</span><span id='linux"
let path2 = "'>$</span>"

function linuxAnimation() {
    let lineNumber = 2
    let newCommand = document.getElementById("linux1")
    typeit(0, newCommand, "cd sampleProjects", lineNumber)
}

let commandList = [`for file in *; do \nif [ -f "$file" ]; then \necho "$file"\nfi \ndone\n Relay: A Realtime chatting web app utilizing Socketio.io https://github.com/spooketti/Relay\n Physiognomy: A machine learning project that guesses the user's gender and age https://github.com/spooketti/Physiognomy\n ConfessionDevice: A project that utilizes the Fabric API for Minecraft Java and electrical engineering \n with Arduino hardware https://github.com/spooketti/ConfessionDevice\nfivenights: The original FNAF 1 game's code base all recreated in HTML, JavaScript, and CSS \n https://github.com/spooketti/fivenights \n`,
    "loadEmbeds.py\n"
]

const delay = ms => new Promise(res => setTimeout(res, ms));

async function typeit(index, element, text, lineNumber) {
    if (index < text.length) {
        if (element == null) {
            element = document.getElementById(`linux${lineNumber - 1}`)
        }
        element.innerText += text.charAt(index)
        index++
        setTimeout(function () {
            typeit(index, element, text, lineNumber)
        }, Math.random()*10)
    }
    else {
        linuxPre.innerHTML += path1 + lineNumber + path2
        lineNumber++
        typeit(0, null, commandList[lineNumber - 3], lineNumber)
        /*
        await delay(1000)
        newElement = document.getElementById(`linux${lineNumber}`)
        console.log(newElement)
        typeit(0,newElement,commandList[lineNumber-2],lineNumber)
        */
    }
}

linuxAnimation()