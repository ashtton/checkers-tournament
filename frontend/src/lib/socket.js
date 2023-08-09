import {io} from "socket.io-client";
import {game, gameCode, nickname, state} from "$lib/state.js";

export const Socket = io("http://localhost:3465")

Socket.on('updateGame', updatedGame => {
    game.set(updatedGame)
})

Socket.on('lobbyDeleted', ctx => {
    alert("LOBBY DISBANDED")
    state.set("HOME")
    gameCode.set("")
    nickname.set("")
    game.set(null)
})

Socket.on('disconnect', ctx => {
    alert("DISCONNECTED FROM SERVER")
    state.set("HOME")
    gameCode.set("")
    nickname.set("")
    game.set(null)
})