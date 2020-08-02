# FirePixlo
### What is FirePixlo?
It's a [PaperMC](https://github.com/PaperMC/Paper) Plugin, which can create custom World, that behaves like standalone Servers. It also adds new Items, Commands, Game mechanics, etc.
### Commands:

**Tip** most commands can be tab-completed!

* /abwassermaps:
  * Use: This command can create top-down images of your map
  * Permission: fire.abwasserMaps
  * Only for players
  * [``Java``](https://github.com/Abwasser1/FirePixlo/blob/master/src/me/abwasser/FirePixlo/cmd/CMD_AbwasserMaps.java)
* /cameraSlide:
  * Use: This command can create cinematic & smooth camera slides
  * Permission: fire.cameraslide
  * Only for players
  * [``Java``](https://github.com/Abwasser1/FirePixlo/blob/master/src/me/abwasser/FirePixlo/cmd/CMD_CameraSlide.java)
* ~/dev~:
  * Use: This command can be used to get verbose / warning / error messages from the plugin
  * Permission: fire.dev
  * [``Java``](https://github.com/Abwasser1/FirePixlo/blob/master/src/me/abwasser/FirePixlo/cmd/CMD_Dev.java)
* /eft:
  * Use: This is the main command, to change EFT specific rules, etc.
  * Permission: fire.eft
  * Only for players
  * Subcommands:
    * registerExits: This command can be used to register exits for the game
    * fixmap: This command can be used to fix underground lava / water sources
  * [``Java``](https://github.com/Abwasser1/FirePixlo/blob/master/src/me/abwasser/FirePixlo/cmd/CMD_EFT.java)
* /fov:
  * Use: This command can be used to change a players field of view
  * Permission: fire.fov
  * Only for players
  * [``Java``](https://github.com/Abwasser1/FirePixlo/blob/master/src/me/abwasser/FirePixlo/cmd/CMD_FOV.java)
* /get:
  * Use: This command is the equivalent of the ``/give`` command, but it's for custom items
  * Permission: fire.get
  * Only for players
  * [``Java``](https://github.com/Abwasser1/FirePixlo/blob/master/src/me/abwasser/FirePixlo/cmd/CMD_Get.java)
* /heal:
  * Use: This command can be used, to heal yourself or a specific player
  * Permission: fire.heal
  * [``Java``](https://github.com/Abwasser1/FirePixlo/blob/master/src/me/abwasser/FirePixlo/cmd/CMD_Heal.java)
* /hub:
  * Use: This command teleports you to the main lobby
  * Aliases: ``/l``, ``/lobby``, ``/quit``, ``/bye``
  * [``Java``](https://github.com/Abwasser1/FirePixlo/blob/master/src/me/abwasser/FirePixlo/cmd/CMD_Hub.java)
* /map:
  * Use: This command can be used to create maps with images / videos on them
  * Permission: fire.map
  * Only for players
  * [``Java``](https://github.com/Abwasser1/FirePixlo/blob/master/src/me/abwasser/FirePixlo/cmd/CMD_Map.java)
* ~/npc~:
  * Use: This command can be used to create custom NPCs
  * Permission: fire.npc
  * Only for players
  * [``Java``](https://github.com/Abwasser1/FirePixlo/blob/master/src/me/abwasser/FirePixlo/cmd/CMD_NPC.java)
* /packet:
  * Use: This command can be used to send packets to a certain client
  * Permission: fire.packet
  * Subcommands:
    * gameStateChange:
      * invalid_bed: sends the player the message, that his home bed it missing
      * end_raining: instantly end the raining for the player
      * begin_raining: instantly makes it rain around the player
      * change_gamemode: changes the gamemode of the player, on the clientside (Warning both creative / spectator are like fly hacks for the server) 
      * exit_end:
        * no_credits: just teleports you to your spawn point
        * show_credits: shows end credits, then teleport you to your spawn point
      * demo_message:
        * show_welcome_screen: show the [Minecraft demo mode screen](https://minecraft.gamepedia.com/Demo_mode)
        * tell_movement_controls: sends instruction on how to move
        * tell_jump_control: sends instruction on how to jump
        * tell_inventory_control: sends instruction on how to open inventory
        * demo_over: sends a message, saying that the demo is over
    * animationexit_end: 
      * swing_main_arm: plays the swing main arm animation
      * take_damage: plays the take damage animation
      * leave_bed: plays the leaving bed animation
      * swing_offhand: plays the swing offhand animation
      * critical_effect: spawns critical particles around the player
      * magical_critical_effect: spawns magical critical particles around the player
    * disconnect: disconnects the player, without telling him, that he was kicked
    * plugin-channel:brand: can be used to change the "Vanilla" or "Spigot" or "PaperMC" text in the upper left corner of the F3 Debug screen
    * timeUpdate: changes the time only for a specific player
  * [``Java``](https://github.com/Abwasser1/FirePixlo/blob/master/src/me/abwasser/FirePixlo/cmd/CMD_Packet.java)
* /play:
  * Use: This command can be used to play recordings made with the ``/rec`` command
  * Permission: fire.play
  * Only for players
  * [``Java``](https://github.com/Abwasser1/FirePixlo/blob/master/src/me/abwasser/FirePixlo/cmd/CMD_Play.java)
* /playertime:
  * Use: This command can be used to change the time for a specific player, just like ``/packet timeUpdate @p %time%``, but it also supports daylight cycles with custom speeds
  * Permission: fire.playertime
  * [``Java``](https://github.com/Abwasser1/FirePixlo/blob/master/src/me/abwasser/FirePixlo/cmd/CMD_PlayerTime.java)
* /pluginrule:
  * Use: This command is the equivalent of the ``/gamerule`` command, but it's plugin-specific
  * Permission: fire.pluginrule
  * Options: 
    * DO_PERFMON: Logs the time needed by the plugin to intercept events
    * SMOOTHER_RELOAD: **Experimental!** Teleports the player back to his location, after a reload
    * REDUCE_DEBUG_INFO: If true this will reduce the debug info (hide coordinates) on specific servers
    * VERBOSE_DEBUG_OUTPUT: Enables verbose messages
    * BUILD_MODE: Disables the automatic server moving
  * [``Java``](https://github.com/Abwasser1/FirePixlo/blob/master/src/me/abwasser/FirePixlo/cmd/CMD_PluginRule.java)
* /rank:
  * Use: This command can be used to give yourself a rank on the server
  * Permission: fire.rank
  * Only for players
  * [``Java``](https://github.com/Abwasser1/FirePixlo/blob/master/src/me/abwasser/FirePixlo/cmd/CMD_Rank.java)
* /rec:
  * Use: This command can create a recording which can be played by using the ``/play`` command
  * Permission: fire.rec
  * Only for Players
  * [``Java``](https://github.com/Abwasser1/FirePixlo/blob/master/src/me/abwasser/FirePixlo/cmd/CMD_Rec.java)
* /server:
  * Use: This command can be used to manage servers
  * Permission: fire.server
  * [``Java``](https://github.com/Abwasser1/FirePixlo/blob/master/src/me/abwasser/FirePixlo/cmd/CMD_Server.java)
* /shutdown:
  * Use: This command is used to shut down a server
  * Permission: fire.shutdown
  * [``Java``](https://github.com/Abwasser1/FirePixlo/blob/master/src/me/abwasser/FirePixlo/cmd/CMD_Shutdown.java)
* /start:
  * Use: This command can be used to start a server
  * Permission: fire.start
  * [``Java``](https://github.com/Abwasser1/FirePixlo/blob/master/src/me/abwasser/FirePixlo/cmd/CMD_Start.java)
* /stopplugin:
  * Use: This command can be used to stop a plugin
  * Permission: fire.stopplugin
  * [``Java``](https://github.com/Abwasser1/FirePixlo/blob/master/src/me/abwasser/FirePixlo/cmd/CMD_StopPlugin.java)
* /sysloc:
  * Use: This command can be used to save / recall / delete a Location (It's like an ``/warp`` command)
  * Permission: fire.sysloc
  * Only for players
  * [``Java``](https://github.com/Abwasser1/FirePixlo/blob/master/src/me/abwasser/FirePixlo/cmd/CMD_SysLoc.java)
* /varo:
  * Use: This is the main command, to change Varo specific rules, etc.
  * Permission: fire.varo
  * Only for players
  * Subcommands:
    * start: This command can be used to start varo
    * reset: This command can be used to reset varo
    * info: This command can be used to get info about varo
  * [``Java``](https://github.com/Abwasser1/FirePixlo/blob/master/src/me/abwasser/FirePixlo/cmd/CMD_Varo.java)
* /world:
  * Use: This command can be used to manage worlds
  * Permission: fire.world
  * Only for players
  * Subcommands:
    * create: This command is used to create worlds
    * teleport: 
      * Use: This command is used to teleport to a World
      * Aliases: ``/tp``
    * delete: This command is used to delete worlds
    * folder: This command opens a FileExplorer like GUI
  * [``Java``](https://github.com/Abwasser1/FirePixlo/blob/master/src/me/abwasser/FirePixlo/cmd/CMD_World.java)
  
### Games:
  - [x] Varo
  - [x] CraftAttack
  - [ ] EFT
  - [ ] LaserTag
  - [ ] MiniGames
    
