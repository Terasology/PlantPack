package org.terasology.plantPack.crop;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import org.terasology.anotherWorld.LocalParameters;
import org.terasology.gf.grass.AdvancedStagesGrowthDefinition;
import org.terasology.world.block.BlockUri;
import org.terasology.world.generator.plugin.RegisterPlugin;
import org.terasology.world.time.WorldTimeImpl;

import java.util.Arrays;

/**
 * @author Marcin Sciesinski <marcins78@gmail.com>
 */
@RegisterPlugin
public class CornGrowthDefinition extends AdvancedStagesGrowthDefinition {
    public static final String ID = "PlantPack:corn";

    public CornGrowthDefinition() {
        super(ID,
                new Predicate<LocalParameters>() {
                    @Override
                    public boolean apply(LocalParameters input) {
                        return input.getHumidity() > 0.2f && input.getTemperature() > 10f;
                    }
                },
                new Function<LocalParameters, Long>() {
                    @Override
                    public Long apply(LocalParameters input) {
                        // Corn growth depends on temperature
                        float temperature = input.getTemperature();
                        long yearLength = 24 * WorldTimeImpl.DAYS_TO_MS;
                        float minGrowthLength = yearLength / 7f;
                        int stageCount = 7;
                        float minStageGrowthLength = minGrowthLength / (stageCount - 1);
                        float maxTemperature = 30;
                        float minTemperature = 10;
                        float maxMultiplier = 2;
                        if (temperature >= maxTemperature) {
                            return (long) minStageGrowthLength;
                        } else if (temperature <= minTemperature) {
                            return (long) (minStageGrowthLength * maxMultiplier);
                        } else {
                            return (long) ((1 + 1f * (maxMultiplier - 1) * (maxTemperature - temperature) / (maxTemperature - minTemperature)) * minStageGrowthLength);
                        }
                    }
                },
                Arrays.asList(
                        new BlockUri("PlantPack", "Corn1"), new BlockUri("PlantPack", "Corn2"), new BlockUri("PlantPack", "Corn3"),
                        new BlockUri("PlantPack", "Corn4"), new BlockUri("PlantPack", "Corn5"), new BlockUri("PlantPack", "Corn6"),
                        new BlockUri("PlantPack", "Corn7")),
                new Predicate<LocalParameters>() {
                    @Override
                    public boolean apply(LocalParameters input) {
                        return input.getHumidity() < 0.2f || input.getTemperature() < 0f;
                    }
                }, new BlockUri("PlantPack", "DeadBush"));
    }
}
