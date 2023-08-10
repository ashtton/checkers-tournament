<script>
    import ChatBox from '$lib/components/chatbox.svelte'

    import {game} from "$lib/state.js";
    import {Socket} from "$lib/socket.js";

    async function disband() {
        Socket.emit("disband", "")
    }

</script>

<h1>Game code: {$game.matchId}</h1>
<h1>Players</h1>
<ul>
    {#each $game.players as player}
        <li>{player.username} {(player.owner ? "(*)" : "")}</li>
    {/each}
</ul>

{#if $game.matchOwner === Socket.id}
    <button>Start tournament</button>
    <button on:click={() => disband()}>Disband lobby</button>
{/if}

<ChatBox />