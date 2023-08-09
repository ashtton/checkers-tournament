import {writable} from "svelte/store";

export const game = writable(null)
export const state = writable("HOME")
export const gameCode = writable("")
export const nickname = writable("")