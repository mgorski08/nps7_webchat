let ws = new WebSocket("wss://nps7webchat.herokuapp.com/chat")
let timeouts = {}
ws.onmessage = (e) => handleResponse(e.data)
document.addEventListener('DOMContentLoaded', (e) => {
    document.getElementById("nickInput").value = "user" + Math.floor(Math.random() * 100)
})

function hideRoomSelector() {
    roomSelectorBox.classList.add("hidden")
    userBox.classList.remove("hidden")
    chatBox.classList.remove("hidden")
}

function showRoomSelector() {
    roomSelectorBox.classList.remove("hidden")
    userBox.classList.add("hidden")
    chatBox.classList.add("hidden")
}

function handleResponse(response) {
    console.log(response)
    let responseData = JSON.parse(response)
    switch (responseData.type) {
        case "message":
            handleMessage(responseData)
            break;
        case "rooms":
            handleRoomResponse(responseData.rooms)
            break;
        case "chat":
            handleChatResponse(responseData.messages)
            break;
        case "participants":
            handleParticipantResponse(responseData.participants)
            break;
        case "writing":
            handleWritingResponse(responseData.participant)
            break;
    }
}

function handleMessage(message) {
    messages.appendChild(createMessageElement(message.senderNick, message.text))
}

function handleRoomResponse(rooms) {
    roomGroup.replaceChildren()
    rooms.forEach(room => {
        let roomOption = document.createElement("option")
        roomOption.innerText = room
        roomGroup.appendChild(roomOption)
    })
}

function handleChatResponse(m) {
    messages.replaceChildren()
    m.forEach(message => {
        messages.appendChild(createMessageElement(message.senderNick, message.text))
    })
}

function handleParticipantResponse(participants) {
    onlineList.replaceChildren()
    participants.forEach(p => {
        let participant = document.createElement("li")
        participant.innerText = p
        if (p === nickInput.value) {
            participant.classList.add("me")
        }
        onlineList.appendChild(participant)
    })
}

function getUserLi(p) {
    for (child of onlineList.children) {
        if (child.innerText === p) return child
    }
}

function handleWritingResponse(participant) {
    let liElement = getUserLi(participant)
    if (!liElement) return
    if (timeouts[participant]) {
        clearTimeout(timeouts[participant])
    }
    liElement.classList.add("writing")
    timeouts[participant] = setTimeout(() => {
        liElement.classList.remove("writing")
    }, 3000)
}

function handleRoomSelect(e) {
    if (roomSelect.value === "1") {
        roomInput.disabled = false
    } else {
        roomInput.disabled = true
        roomInput.value = roomSelect.value
    }
}

function join() {
    nickSpan.innerText = nickInput.value
    roomSpan.innerText = roomInput.value
    ws.send(JSON.stringify({
        operation: "join",
        argument1: nickInput.value,
        argument2: roomInput.value
    }))
    hideRoomSelector()
    fetchChat()
    fetchParticipants()
}

function leave() {
    ws.send(JSON.stringify({
        operation: "leave",
        argument1: nickInput.value,
        argument2: roomInput.value
    }))
    showRoomSelector()
}

function sendToGroupChat() {
    ws.send(JSON.stringify({
        operation: "message",
        argument1: messageInput.value,
        argument2: ""
    }))
    messageInput.value = ""
    messageInput.focus()
}

function keyUpInput(event) {
    if (event.key === "Enter") {
        sendToGroupChat()
    }
}

function fetchRooms() {
    ws.send(JSON.stringify({
        operation: "list_rooms",
        argument1: "",
        argument2: ""
    }))
}

function fetchChat() {
    ws.send(JSON.stringify({
        operation: "get_chat",
        argument1: "",
        argument2: ""
    }))
}

function fetchParticipants() {
    ws.send(JSON.stringify({
        operation: "get_participants",
        argument1: "",
        argument2: ""
    }))
}

function messageWriting() {
    ws.send(JSON.stringify({
        operation: "writing",
        argument1: "",
        argument2: ""
    }))
}

function createMessageElement(sender, text) {
    let message = document.createElement("div")
    message.classList.add("message")
    let textElement = document.createElement("p")
    textElement.innerText = text
    textElement.classList.add("text")
    if (sender === nickInput.value) {
        message.appendChild(textElement)
        message.classList.add("own")
    } else {
        let senderElement = document.createElement("p")
        senderElement.innerText = sender
        senderElement.classList.add("nick")
        message.appendChild(senderElement)
        message.appendChild(textElement)
    }
    return message
}
