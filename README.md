<div align="center">
<h3>VIGILANTE</h3>
<img src="/Demo.gif">
</div>

## Overview
My ~~excellent~~ 2D Platformer Engine written in Java 
using [LibGDX](https://libgdx.badlogicgames.com/).

![lol](https://i.imgur.com/vvfQcw3.png)

## Implemented Features
* **Characters** (Player / Enemies)
  * Basic Movements (Walk / Crouch / Jump / Attack)
  * Sheath and Unsheath Weapon (just like Skyrim)
* **Inventory** (Equipment / Consumable / Misc)
* **Equipment** (Helmet / Armor / Gauntlets / Boots / Weapon / Cape)
* **Combat**
  * Basic Enemy AI (They'll start attacking players when attacked)
* **UI**
  * Main Menu
  * Pause Menu (Inventory, Equipment, ~~Skills~~ ... etc)
  * Notification System
  * Floating Damage Indicator System
  * HUD (Bars, Weapon Slot UI)
* **Object Serialization**
  * Player data -> core/assets/Database/[characters.json](https://github.com/aesophor/Vigilante/blob/master/core/assets/Database/characters.json)
  * Equipment data -> core/assets/Database/[equipment.json](https://github.com/aesophor/Vigilante/blob/master/core/assets/Database/equipment.json)
  * Item data ->  core/assets/Database/[items.json](https://github.com/aesophor/Vigilante/blob/master/core/assets/Database/items.json)

## Architectural Pattern
* [Ashley](https://github.com/libgdx/ashley/wiki) (An [Entity-Component-System Framework](https://en.wikipedia.org/wiki/Entity%E2%80%93component%E2%80%93system))
* A custom coded Event System (by myself)

## Requirements
* JDK 8
* LibGDX
* Box2d Lights
* Ashley

## Assets / Credits
Most assets were collected from itch.io:
* Beautiful Scene Backgrounds created by [ansimuz](https://ansimuz.itch.io/)
* [Knight animations](https://lionheart963.itch.io/knight-sprite) created by [Warren Clark](https://lionheart963.itch.io/)
* Music / SFX from opengameart.org

Some assets I've modified:
* Main Character based on [Warren Clark's Axe Bandit](https://lionheart963.itch.io/axe-bandit). I drew the unsheathed version.

## License
I open sourced this game with the aim of helping anyone who wants to learn how to code their own games!

However, this is my (very first) personal game project in which I have put a lot of efforts, so DONT use it else where please :)

When the game is done, I'll submit it to Steam (maybe).



