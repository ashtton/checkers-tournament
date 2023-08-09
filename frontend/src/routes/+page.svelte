<script>
    import {Socket} from "$lib/socket.js";
    import {goto} from "$app/navigation";

    import Home from '$lib/pages/home.svelte'
    import Lobby from '$lib/pages/lobby.svelte'

    import {state} from '$lib/state'
    import {game} from "$lib/state.js";

</script>

{#if $state === "HOME"}
    <Home />
{/if}

{#if $state === "LOBBY" && $game !== null}
    <Lobby />
{/if}