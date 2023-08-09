<script>
    import {gameCode, nickname, state} from "$lib/state.js";
    import {Socket} from "$lib/socket.js";

    let choosingName = false;
    let value = "";

    async function check() {
        if (value.length === 0) {
            return
        }

        let res = await fetch('http://localhost:7070/room/' + (choosingName ? $gameCode : value))

        if (!choosingName) {
            if (res.status === 404) {
                alert("Invalid room number")
                return
            }

            console.log("game code is right")
            gameCode.set(value)
            value = ""
            choosingName = true;
            return
        }

        if (res.status !== 200) {
            alert("error joining")
            gameCode.set("")
            choosingName = false
            value = ""
            return
        }

        nickname.set(value)
        value = ""
        choosingName = false
        Socket.emit("join", $gameCode + "," + $nickname)
        state.set("LOBBY")
    }
</script>

<h1>{choosingName ? "enter a nickname" : "enter a room number"}</h1>
<input bind:value={value} type="text" placeholder={choosingName ? "enter a nickname" : "enter a room number"} on:keypress={(e) => e.key === "Enter" ? check() : ""}>
