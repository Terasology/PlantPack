/*
 * Copyright 2014 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.plantPack.tree;

import com.google.common.collect.Maps;
import org.terasology.gf.tree.PartOfTreeComponent;
import org.terasology.gf.tree.lsystem.AdvanceAxionElementGeneration;
import org.terasology.gf.tree.lsystem.AdvancedLSystemTreeDefinition;
import org.terasology.gf.tree.lsystem.AxionElementGeneration;
import org.terasology.gf.tree.lsystem.AxionElementReplacement;
import org.terasology.gf.tree.lsystem.BlockLengthElementGeneration;
import org.terasology.gf.tree.lsystem.DefaultAxionElementGeneration;
import org.terasology.gf.tree.lsystem.GrowthAxionElementReplacement;
import org.terasology.gf.tree.lsystem.LSystemBasedTreeGrowthDefinition;
import org.terasology.gf.tree.lsystem.SimpleAxionElementReplacement;
import org.terasology.gf.tree.lsystem.SurroundAxionElementGeneration;
import org.terasology.gf.tree.lsystem.SurroundLengthAxionElementGeneration;
import org.terasology.gf.tree.lsystem.TreeBlockDefinition;
import org.terasology.utilities.random.Random;
import org.terasology.world.generator.plugin.RegisterPlugin;

import java.util.Map;

@RegisterPlugin
public class BirchGrowthDefinition extends LSystemBasedTreeGrowthDefinition {
    public static final String ID = "PlantPack:birch";
    public static final String GENERATED_BLOCK = "PlantPack:BirchSaplingGenerated";

    private AdvancedLSystemTreeDefinition treeDefinition;

    public BirchGrowthDefinition() {
        Map<Character, AxionElementReplacement> replacementMap = Maps.newHashMap();

        SimpleAxionElementReplacement sapling = new SimpleAxionElementReplacement("s");
        sapling.addReplacement(1f, "T(0.5)t");

        SimpleAxionElementReplacement trunkTop = new SimpleAxionElementReplacement("t");
        trunkTop.addReplacement(0.6f,
                new AxionElementReplacement() {
                    @Override
                    public String getReplacement(Random rnd, String parameter, String currentAxion) {
                        // 137.5 degrees is a golden ratio
                        int deg = rnd.nextInt(120, 157);
                        return "+(" + deg + ")[&B(0.5)b]T(0.5)t";
                    }
                });
        trunkTop.addReplacement(0.4f,
                new AxionElementReplacement() {
                    @Override
                    public String getReplacement(Random rnd, String parameter, String currentAxion) {
                        // Always generate at least 2 branches
                        if (currentAxion.split("b").length < 2) {
                            // 137.5 degrees is a golden ratio
                            int deg = rnd.nextInt(130, 147);
                            return "+(" + deg + ")[&B(0.5)b]T(0.5)t";
                        }
                        return "t";
                    }
                });

        replacementMap.put('s', sapling);
        replacementMap.put('g', sapling);
        replacementMap.put('t', trunkTop);
        replacementMap.put('T', new GrowthAxionElementReplacement("T", 1.2f));
        replacementMap.put('B', new GrowthAxionElementReplacement("B", 1.1f));

        TreeBlockDefinition birchSapling = new TreeBlockDefinition("PlantPack:BirchSapling", PartOfTreeComponent.Part.SAPLING);
        TreeBlockDefinition birchSaplingGenerated = new TreeBlockDefinition(GENERATED_BLOCK, PartOfTreeComponent.Part.SAPLING);
        TreeBlockDefinition greenLeaf = new TreeBlockDefinition("PlantPack:BirchLeaf", PartOfTreeComponent.Part.LEAF);
        TreeBlockDefinition birchTrunk = new TreeBlockDefinition("PlantPack:BirchTrunk", PartOfTreeComponent.Part.TRUNK);
        TreeBlockDefinition birchBranch = new TreeBlockDefinition("PlantPack:BirchBranch", PartOfTreeComponent.Part.BRANCH);

        float trunkAdvance = 0.6f;
        float branchAdvance = 0.1f;

        Map<Character, AxionElementGeneration> blockMap = Maps.newHashMap();
        blockMap.put('s', new DefaultAxionElementGeneration(birchSapling, trunkAdvance));
        blockMap.put('g', new DefaultAxionElementGeneration(birchSaplingGenerated, trunkAdvance));

        // Trunk building blocks
        blockMap.put('t', new SurroundAxionElementGeneration(greenLeaf, greenLeaf, trunkAdvance, 2f));
        blockMap.put('T', new BlockLengthElementGeneration(birchTrunk, trunkAdvance));

        // Branch building blocks
        SurroundAxionElementGeneration smallBranchGeneration = new SurroundAxionElementGeneration(greenLeaf, greenLeaf, branchAdvance, 2.6f);
        smallBranchGeneration.setMaxZ(0);
        blockMap.put('b', smallBranchGeneration);

        SurroundLengthAxionElementGeneration largeBranchGeneration = new SurroundLengthAxionElementGeneration(birchBranch, greenLeaf, branchAdvance, 1.1f, 3.5f);
        largeBranchGeneration.setMaxZ(0);
        blockMap.put('B', largeBranchGeneration);

        treeDefinition = new AdvancedLSystemTreeDefinition(ID, "g", replacementMap, blockMap, 1.5f);
    }

    @Override
    public String getPlantId() {
        return ID;
    }

    @Override
    protected String getGeneratedBlock() {
        return GENERATED_BLOCK;
    }

    @Override
    protected AdvancedLSystemTreeDefinition getTreeDefinition() {
        return treeDefinition;
    }
}
