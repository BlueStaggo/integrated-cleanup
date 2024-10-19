# Integrated Cleanup

Integrated Cleanup is an [Ornithe](https://ornithemc.net) mod for Minecraft versions 1.3 to 1.7 that fixes a bunch of multiplayer bugs to bring the game closer to the intended singleplayer experience.

Integrated Cleanup can be used on clients as well as servers: vanilla players will still be able to connect, but players with the mod will experience the full set of fixes in your server. Likewise, players with the mod can still play on vanilla server just fine, albeit with client-side tweaks only.

## List of Fixes

Legend:
- `(C)` Effective on all servers for clients with the mod installed client-side
- `(I)` Effective on integrated servers only
- `(S)` Effective only on the integrated server or servers that have the mod installed
- `(V)` Effective for all clients with the mod installed server-side

Fixes:
- `(C)` Entities no longer have jittery movement
- `(C)` Entities turn their heads smoothly with quadratic interpolation
- `(C)` Hearts correctly flash when damage is taken 
- `(I)` Increased default view distance by 2 chunks
- `(C)` Joining a server no longer starts with falling into the void and is also faster
- `(S)` Restored various particle effects
- `(I)` Restored loading bar on world load
- `(C)` Restored portal travel sound
    - Plays before the loading screen before 1.7
    - Plays after the loading screen in 1.7+
- `(C)` Restored puffy ghast animation
    - `(S)` Slightly more accurate animation if the mod is installed on the server
- `(S)` Thunder storms now correctly darken the sky
- `(V)` Using the /time command or sleeping instantly changes the time
- `(C)` XP levels are no longer shown in creative mode
