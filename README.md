PlantPack
=========
A collection of trees and plants for dynamic plants. These plants grow over time or change in other ways.


Plants
------
The plants are divided into 2 types.
 1. Crops
 2. Trees

**Crops** are able to grow (change texture) over time and they can have several stages of growth. Crops is a similar to a normal block.
**Trees** however, does not have the capability to slowly grow over time that crops have. Trees are usually a structure, composed of many blocks.

Making a New Crop
=================

*For a good example you can see `assets/blocks/crop`  & `assets/prefabs/crop`*

Prefab File
-----------

To start making a new crop you need to decide how many stages of growth you want to have. Then you need to create a prefab file to indicate those stages. 

*Here is an example of a crop with 7 stages.*

    {
      "ChangingBlocks": {
        "blockFamilyStages": {
          "PlantPack:Corn1": 30000,
          "PlantPack:Corn2": 30000,
          "PlantPack:Corn3": 30000,
          "PlantPack:Corn4": 30000,
          "PlantPack:Corn5": 30000,
          "PlantPack:Corn6": 30000,
          "PlantPack:Corn7": 30000
        },
        "loops": false
      }
    }
Each line in `blockFamilyStages` indicates a growth stage for the crop, and the numbers next to it represents the growth time it requires. Loops indicated whether or not you want the plant to loop the growth stages after it finishes.

After you finish making the stages, you MUST remember the name of those stages so that you can create the .block file for each stage.

Block File
----------
After you finished the prefab file, you need to create a .block file. All .block files are located in `assets/blocks/crop`.

*Here is an example of a .block file.*

    {
        "author": "metouto",
        "basedOn": "core:plant",
        "entity": {
            "prefab": "PlantPack:Corn",
            "keepActive": true
        }
    }

Make sure you type-in the prefab in the .block file in accordance to the name of the prefab that you made.

Texture Files
-------
After creating the prefab and block files, you need to create the texture for each crop growth stages. All texture files for crops are located at `assets/blockTiles/crop/`

In this folder, insert texture files (.png) for each growth stages. Make sure you name the texture files in accordance to the growth stages that you want to assign it to.

Developing With PlantPack
=======
**PlantPack** assets can bring a good addition to world generation modules, you can utilize the plants that this module have to *spice up* the world that your module generates.

A good example would be, [AnotherWorld](https://github.com/Terasology/AnotherWorld/) module that works together with [AnotherWorldPlant](https://github.com/Terasology/AnotherWorldPlants) that utilizes PlantPack assets.
