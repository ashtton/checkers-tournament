<script>
    import {Socket} from "$lib/socket.js";

    let messages = []
    let message = "";

    Socket.on('newMessage', msg => {
        messages.push(msg)
        messages = messages
        console.log(msg)
    })

    async function sendMessage() {
        Socket.emit("sendMessage", message)
        console.log("sent " + message)
        message = ""
    }

</script>

<div class="chatbox">
    {#each messages as message}
        <div>{message.author} - {message.message}</div>
    {/each}
    <input bind:value={message} placeholder="enter a message" on:keypress={(e) => e.key ==="Enter" ? sendMessage() : ""}>
</div>

<style>
    .chatbox {
        float: right;
    }
</style>
