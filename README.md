# Freecam for 1.7.10

A backport of the modern Freecam mod

Features include:  
Creative and Static (no drifting) movement options  
Camera hitbox is below .5 blocks so it can even fit under a top slab and in the same blockspace as conduits  
Tripod system - Saved camera positions by holding the toggle key and pressing a number key  
4 collision modes: Full, Ignore Transparent (non-opaque blocks including openables), Ignore Openables (doors, trapdoors, etc) and None (noclip)  
Config options for fullbright, fog removal, hand rendering, and initial perspective  
Camera is clamped to a sphere inside the player's render distance
Server owners can include the mod to force the collision mode or to disable the mod entirely for players  
The server must have the mod if the client has the mod. Mod is optional if server has the mod

Default Controls (all but the numbers are configurable):  
F4 - Toggle freecam  
F4 + 1-9 - Open or switch to tripod slot  
F4 + C - Toggle player/camera controls  
F4 + R - Reset all tripods  
Mouse wheel (while in freecam) - Adjust flight speed  

### Configuration

All settings are in the **misc** category. Settings marked **Synced** can be overridden by a server that has the mod:

| Setting | Values | Default | Synced | Description |
|---------|--------|---------|--------|-------------|
| `fullBright` | true/false | false | Yes | Enable full brightness while in freecam |
| `disableSubmersionFog` | true/false | false | Yes | Disable fog when camera is submerged in water or lava |
| `overlayVisibility` | HIDE/SHOW | HIDE | Yes | Show mod overlays (WAILA, Thaumcraft goggles/scanning, HoloInventory) while in freecam |
| `initialPerspective` | INSIDE/FIRST_PERSON/THIRD_PERSON/THIRD_PERSON_MIRROR | INSIDE | No | Camera perspective when entering freecam |
| `showHand` | true/false | false | No | Show the player's hand while in freecam |

### Credits:  
Reference - https://github.com/xXseesXx/Freecam-1.7.10  
Some modern features - https://github.com/hashalite/Freecam
