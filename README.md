[![Discord](https://img.shields.io/discord/1321045735055163402?logo=discord&color=949af1)](https://discord.gg/JdrzWQvT3v)
[![This is an image](https://cf.way2muchnoise.eu/versions/1134090.svg)](https://www.curseforge.com/minecraft/mc-mods/sanity-prequel/files)
[![This is an image](https://cf.way2muchnoise.eu/full_1134090_downloads.svg)](https://www.curseforge.com/minecraft/mc-mods/sanity-prequel)
## Introduction
**Inspired by the [Sanity: Descent Into Madness](https://github.com/croissantnova/SanityDescentIntoMadness) mod, players will gain some bad effects when in low sanity. Textures modified from Sanity: Descent Into Madness by croissantnova ([consent](https://s21.ax1x.com/2025/02/02/pEZa9S0.png)). Sound effects provided by [Zapsplat](https://www.zapsplat.com).**
## Features
**Most feature support custom configurations. Turn to mod's config file for more information. _Since version 1.3.3, you need Baubles mod as dependency._**
### Garland
**Each garland has 60 durability in total, use any small flowers to craft it. Water, fire, lightning, and explosion can lead to damage it while wearing on head. (Equipped in armor or baubles slot)**
### Umbrella
**Holding an umbrella can prevent player from decreasing sanity and damaging garland by rain. Umbrella has 120 durability, which will damage from lightning, fire, explosion. And break or attack can also damage it.**
![This is an image](https://s21.ax1x.com/2025/02/05/pEeKjN8.png)
### Potion
**Add composure potion effect, which can recover 1 sanity every `60 >> amplifier` ticks. Brew any small flowers with an regeneration potion.**
### Effects
- **Sanity < 60, at the edges of things, red, green, and blue seem to be separated in your eyes.**
- **Sanity < 55, blood texture flicker on the screen when sanity decrease.**
- **Sanity < 50, players will experience auditory hallucinations. (support custom sound list)**
- **Sanity < 45, players experiences hallucinations and will see ghosts. (support custom ghost list)**
- **Sanity < 40, brain overlay starts to shake.**
- **Sanity < 35, there are a grid of small dots lies in your eyes. The world looks washed out...**
- **Sanity < 30, whispers can be heard.**
- **Sanity < 10, game's graphics are even more distorted, with details barely legible.**
![This is an image](https://s21.ax1x.com/2025/01/18/pEkiLJU.png)

**Using potion of composure or morphine in First Aid mod grants temporary immunity to all negative effects.**
### Decrease Sanity
- **Attack animals, villagers and other players.**  
- **Eating bad foods such as carrion, raw meat, etc. (support custom items)**  
- **Struck by lightning.**  
- **Hunger.**  
- **Thirst. (support Tough As Nails and SimpleDifficulty)**  
- **Get hurt.**  
- **Rain.**  
- **Dark. (only work when player doesn't have night vision effect)**  
- **Around mobs. (within 5 blocks)**  
- **Around players with sanity < 50. (within 5 blocks)**  
- **Food spoiled in inventory. (only work with Food Spoiling mod)**
- **Choking.**
- **Change dimensions.**
- **Lost pets.**  
- **Trapped in blocks like web and water. (support custom blocks)**
### Increase Sanity    
- **Sleep.**  
- **Bred animals.**  
- **Eating healthy foods. (support custom items)**  
- **Wearing garland.**  
- **Gain advancements.**  
- **Complete quests. (only support FTB Quests)**  
- **Stay with pets. (within 5 blocks)**  
- **Around players with sanity ≥ 50. (within 5 blocks)**  
- **Kill mobs.**  
- **Composure effect.**
### Commands
**Using commands need permission level 2. (support tab completion)**
![This is an image](https://s21.ax1x.com/2025/01/17/pEFjK81.png)
### CraftTweaker
**Add a ZenExpansion for [IPlayer](https://docs.blamejared.com/1.12/en/Vanilla/Players/IPlayer) (`sanity`), which will return player's sanity value as float. Developers can call this method on any [IPlayer](https://docs.blamejared.com/1.12/en/Vanilla/Players/IPlayer) object, including its subtypes.**
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
### Thaumcraft 6
**Warp in [Thaumcraft 6](https://ftbwiki.org/Thaumcraft_6) can affect player's max sanity. `max = 100 - warp` (permanent value and normal value, no more than 50)**
## About
**Only the codebase has been open source under the Apache License 2.0, you can use, modify the code to port, distribute this mod under the license. Sound effects retain their respective [license](https://www.zapsplat.com/license-type/standard-license). All rights reserved for all other assets. And there are currently no plans to make a mod like this for a new Minecraft version.**