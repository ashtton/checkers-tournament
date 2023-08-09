<script>
    import {Socket} from "$lib/socket.js";
    import {goto} from "$app/navigation";


    Socket.on('event2', (msg) => {
        console.log(msg)
    })

    async function hello() {
        await Socket.emit('event1', "sdfsdf")
    }
</script>

<button on:click={() => hello()}>Click meee!</button>

<button on:click={() => goto("/")}>back</button>