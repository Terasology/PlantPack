package org.terasology.plantPack.crop;

import org.terasology.gf.crop.CropGrowthDefinition;
import org.terasology.world.block.BlockUri;
import org.terasology.world.generator.plugin.RegisterPlugin;

import java.util.Arrays;

/**
 * @author Marcin Sciesinski <marcins78@gmail.com>
 */
@RegisterPlugin
public class CornGrowthDefinition extends CropGrowthDefinition {
    public static final String ID = "PlantPack:corn";

    public CornGrowthDefinition() {
        super(ID, Arrays.asList(
                new BlockUri("PlantPack", "Corn1"), new BlockUri("PlantPack", "Corn2"), new BlockUri("PlantPack", "Corn3"),
                new BlockUri("PlantPack", "Corn4"), new BlockUri("PlantPack", "Corn5"), new BlockUri("PlantPack", "Corn6"),
                new BlockUri("PlantPack", "Corn7")), 120 * 1000);
    }
}
