<script>
    import {Socket} from "$lib/socket.js";
    import {goto} from "$app/navigation";


    Socket.on('event2', (msg) => {
        console.log(msg)
    })

    async function hello() {
        await Socket.emit('event1', "hi")
    }
</script>

<button on:click={() => hello()}>Click me!</button>

<button on:click={() => goto("/hi")}>next page</button>