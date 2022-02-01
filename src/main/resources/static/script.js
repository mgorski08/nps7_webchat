let ws = new WebSocket("ws://localhost:8080/chat")
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
    }
}

function handleMessage(message) {
    let messages = document.getElementById("messages")
    messages.appendChild(createMessageElement(message.senderNick, message.text))
}

function handleRoomResponse(rooms) {
    let roomGroup = document.getElementById("roomGroup")
    roomGroup.replaceChildren()
    rooms.forEach(room => {
        let roomOption = document.createElement("option")
        roomOption.innerText = room
        roomGroup.appendChild(roomOption)
    })
}

function handleChatResponse(messages) {
    let messagesElement = document.getElementById("messages")
    messagesElement.replaceChildren()
    messages.forEach(message => {
        messagesElement.appendChild(createMessageElement(message.senderNick, message.text))
    })
}

function handleParticipantResponse(participants) {
    let onlineList = document.getElementById("onlineList")
    onlineList.replaceChildren()
    participants.forEach(p => {
        let participant = document.createElement("li")
        participant.innerText = p
        onlineList.appendChild(participant)
    })
}

function handleRoomSelect(e) {
    let roomSelect = document.getElementById("roomSelect")
    let roomInput = document.getElementById("roomInput")
    if (roomSelect.value === "1") {
        roomInput.disabled = false
    } else {
        roomInput.disabled = true
        roomInput.value = roomSelect.value
    }
}

function join() {
    let nickInput = document.getElementById("nickInput")
    let roomInput = document.getElementById("roomInput")
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
    let messageInput = document.getElementById("messageInput")
    ws.send(JSON.stringify({
        operation: "message",
        argument1: messageInput.value,
        argument2: ""
    }))
    messageInput.value = ""
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

function createMessageElement(sender, text) {
    let nickInput = document.getElementById("nickInput")
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
