package org.terasology.plantPack.crop;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import org.terasology.anotherWorld.LocalParameters;
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
                new BlockUri("PlantPack", "Corn7")), 10 * 1000,
                new Predicate<LocalParameters>() {
                    @Override
                    public boolean apply(LocalParameters input) {
                        return input.getHumidity() > 0.2f && input.getTemperature() > 0.4f;
                    }
                },
                new Function<LocalParameters, Float>() {
                    @Override
                    public Float apply(LocalParameters input) {
                        return input.getHumidity();
                    }
                }
        );
    }
}
