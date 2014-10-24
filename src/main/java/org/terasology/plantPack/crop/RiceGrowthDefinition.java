package org.terasology.plantPack.crop;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import org.terasology.anotherWorld.LocalParameters;
import org.terasology.gf.grass.ReplaceBlockGrowthDefinition;
import org.terasology.world.block.BlockUri;
import org.terasology.world.generator.plugin.RegisterPlugin;

import java.util.Arrays;

/**
 * @author Marcin Sciesinski <marcins78@gmail.com>
 */
@RegisterPlugin
public class RiceGrowthDefinition extends ReplaceBlockGrowthDefinition {
    public static final String ID = "PlantPack:rice";

    public RiceGrowthDefinition() {
        super(ID, Arrays.asList(
                new BlockUri("PlantPack", "Rice1"), new BlockUri("PlantPack", "Rice2"), new BlockUri("PlantPack", "Rice3"),
                new BlockUri("PlantPack", "Rice4")), 10 * 1000,
                new Predicate<LocalParameters>() {
                    @Override
                    public boolean apply(LocalParameters input) {
                        return input.getHumidity() > 0.4f && input.getTemperature() > 15f;
                    }
                },
                new Function<LocalParameters, Float>() {
                    @Override
                    public Float apply(LocalParameters input) {
                        return 0.2f * input.getHumidity();
                    }
                }
        );
    }
}
