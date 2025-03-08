![This is an image](https://s21.ax1x.com/2025/01/15/pEiWBsx.png)

[![Discord](https://img.shields.io/discord/1321045735055163402?logo=discord&color=949af1)](https://discord.gg/JdrzWQvT3v)
[![This is an image](https://cf.way2muchnoise.eu/versions/1134090.svg)](https://www.curseforge.com/minecraft/mc-mods/sanity-prequel/files)
[![This is an image](https://cf.way2muchnoise.eu/full_1134090_downloads.svg)](https://www.curseforge.com/minecraft/mc-mods/sanity-prequel)
## Introduction
**Inspired by the [Sanity: Descent Into Madness](https://github.com/croissantnova/SanityDescentIntoMadness) mod, players will gain some bad effects when in low sanity. Textures modified from Sanity: Descent Into Madness by croissantnova ([consent](https://s21.ax1x.com/2025/02/02/pEZa9S0.png)). Sound effects provided by [Zapsplat](https://www.zapsplat.com).**
## Features
**Most feature support custom configurations. Turn to mod's config file for more information.**
### Garland
**Each garland has 60 durability in total, use any small flowers to craft it. Be in danger like water, web, fire, lightning, and explosion can lead to damage it while wearing on head. (support Baubles)**
### Umbrella
**Holding an umbrella can prevent player from decreasing sanity and damaging garland by rain. Umbrella has 120 durability, which will damage from lightning, fire, explosion. And break or attack can also damage it.**
![This is an image](https://s21.ax1x.com/2025/02/05/pEeKjN8.png)
### Effects
- **Sanity ≤ 60, at the edges of things, red, green, and blue seem to be separated in your eyes.**
- **Sanity ≤ 50, players will experience auditory hallucinations. (support custom sound list)**
- **Sanity ≤ 45, whispers can be heard.**
- **Sanity ≤ 40, there are a grid of small dots lies in your eyes. The world looks washed out...**
- **Sanity ≤ 10, game's graphics are even more distorted, with details barely legible.**

![This is an image](https://s21.ax1x.com/2025/01/18/pEkiLJU.png)
### Decrease Sanity
- **Attack animals, villagers and other players.**  
- **Eating bad foods such as carrion, raw meat, etc. (support custom configurations)**  
- **Struck by lightning.**  
- **Hunger.**  
- **Thirst. (support Tough As Nails and SimpleDifficulty)**  
- **Get hurt.**  
- **Rain.**  
- **Dark. (Only work when player doesn't have night vision effect)**  
- **Around mobs. (within 8 blocks)**  
- **Choking.**
- **Change dimensions.**  
- **Falling.**  
- **Lost pets.**  
- **Trapped in blocks like web and water. (support custom configurations)**
### Increase Sanity    
- **Sleep.**  
- **Eating healthy foods.**  
- **Wearing garland.**  
- **Gain advancements.**  
- **Compelete quests. (only support FTB Quests)**  
- **Stay with pets. (within 5 blocks)**  
- **Kill mobs.**
### Commands
**Using commands need permission level 2. (support tab completion)**
![This is an image](https://s21.ax1x.com/2025/01/17/pEFjK81.png)
### CraftTweaker
**Add a ZenExpansion for [IPlayer](https://docs.blamejared.com/1.12/en/Vanilla/Players/IPlayer) (`sanity`), which will return player's sanity value in float. Developers can call this method on any [IPlayer](https://docs.blamejared.com/1.12/en/Vanilla/Players/IPlayer) object, including its subtypes.**
```zenscript
import crafttweaker.event.BlockHarvestDropsEvent;

events.onBlockHarvestDrops(function(event as BlockHarvestDropsEvent) {
    if(!event.world.isRemote()) {
        var id = event.block.definition.id;
        if (id == "minecraft:red_flower" && event.player.sanity <= 10) {
            event.drops = [<minecraft:deadbush> % 100];
        }
    }
});
```
**This *example* script makes any small flowers drop a dead bush when broken by a player with sanity of 10 or less.**
## About
**Only the codebase has been open source under the Apache License 2.0, you can use, modify the code to port, distribute this mod under the license. Sound effects retain their respective [license](https://www.zapsplat.com/license-type/standard-license). All rights reserved for all other assets. And there are currently no plans to make a mod like this for a new Minecraft version.**