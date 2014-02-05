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
package org.terasology.plantPack;

import com.google.common.collect.Maps;
import org.terasology.gf.tree.PartOfTreeComponent;
import org.terasology.gf.tree.lsystem.AdvanceAxionElementGeneration;
import org.terasology.gf.tree.lsystem.AdvancedLSystemTreeDefinition;
import org.terasology.gf.tree.lsystem.AxionElementGeneration;
import org.terasology.gf.tree.lsystem.AxionElementReplacement;
import org.terasology.gf.tree.lsystem.DefaultAxionElementGeneration;
import org.terasology.gf.tree.lsystem.LSystemBasedTreeGrowthDefinition;
import org.terasology.gf.tree.lsystem.SimpleAxionElementReplacement;
import org.terasology.gf.tree.lsystem.SurroundAxionElementGeneration;
import org.terasology.gf.tree.lsystem.TreeBlockDefinition;
import org.terasology.utilities.random.FastRandom;
import org.terasology.world.generator.plugin.RegisterPlugin;

import java.util.Map;

@RegisterPlugin
public class CypressGrowthDefinition extends LSystemBasedTreeGrowthDefinition {
    public static final String ID = "PlantPack:cypress";
    public static final String GENERATED_BLOCK = "PlantPack:CypressSaplingGenerated";

    private AdvancedLSystemTreeDefinition treeDefinition;

    public CypressGrowthDefinition() {
        Map<Character, AxionElementReplacement> replacementMap = Maps.newHashMap();

        SimpleAxionElementReplacement sapling = new SimpleAxionElementReplacement("s");
        sapling.addReplacement(1f, "Tt");

        final FastRandom rnd = new FastRandom();

        SimpleAxionElementReplacement trunkTop = new SimpleAxionElementReplacement("t");
        trunkTop.addReplacement(0.6f,
                new SimpleAxionElementReplacement.ReplacementGenerator() {
                    @Override
                    public String generateReplacement(String currentAxion) {
                        // 137.5 degrees is a golden ratio
                        int deg = rnd.nextInt(132, 143);
                        return "N+(" + deg + ")[&Mb]Wt";
                    }
                });
        trunkTop.addReplacement(0.4f,
                new SimpleAxionElementReplacement.ReplacementGenerator() {
                    @Override
                    public String generateReplacement(String currentAxion) {
                        // Always generate at least 2 branches
                        if (currentAxion.split("b").length < 1) {
                            // 137.5 degrees is a golden ratio
                            int deg = rnd.nextInt(135, 145);
                            return "N+(" + deg + ")[&Mb]Wt";
                        }
                        return "NWt";
                    }
                });

        SimpleAxionElementReplacement smallBranch = new SimpleAxionElementReplacement("b");
        smallBranch.addReplacement(0.8f, "Bb");

        SimpleAxionElementReplacement trunk = new SimpleAxionElementReplacement("T");
        trunk.addReplacement(0.7f, "TN");

        replacementMap.put('s', sapling);
        replacementMap.put('g', sapling);
        replacementMap.put('t', trunkTop);
        replacementMap.put('T', trunk);
        replacementMap.put('b', smallBranch);

        TreeBlockDefinition cypressSapling = new TreeBlockDefinition("PlantPack:CypressSapling", PartOfTreeComponent.Part.SAPLING);
        TreeBlockDefinition cypressSaplingGenerated = new TreeBlockDefinition(GENERATED_BLOCK, PartOfTreeComponent.Part.SAPLING);
        TreeBlockDefinition greenLeaf = new TreeBlockDefinition("PlantPack:CypressLeaf", PartOfTreeComponent.Part.LEAF);
        TreeBlockDefinition cypressTrunk = new TreeBlockDefinition("PlantPack:CypressTrunk", PartOfTreeComponent.Part.TRUNK);
        TreeBlockDefinition cypressBranch = new TreeBlockDefinition("PlantPack:CypressBranch", PartOfTreeComponent.Part.BRANCH);

        float trunkAdvance = 0.4f;
        float branchAdvance = 0.25f;

        Map<Character, AxionElementGeneration> blockMap = Maps.newHashMap();
        blockMap.put('s', new DefaultAxionElementGeneration(cypressSapling, trunkAdvance));
        blockMap.put('g', new DefaultAxionElementGeneration(cypressSaplingGenerated, trunkAdvance));

        // Trunk building blocks
        blockMap.put('t', new SurroundAxionElementGeneration(greenLeaf, greenLeaf, trunkAdvance, 1.2f));
        blockMap.put('T', new DefaultAxionElementGeneration(cypressTrunk, trunkAdvance));
        blockMap.put('N', new DefaultAxionElementGeneration(cypressTrunk, trunkAdvance));
        blockMap.put('W', new SurroundAxionElementGeneration(cypressBranch, greenLeaf, trunkAdvance, 1.2f));

        // Branch building blocks
        SurroundAxionElementGeneration smallBranchGeneration = new SurroundAxionElementGeneration(greenLeaf, greenLeaf, branchAdvance, 1.4f);
        smallBranchGeneration.setMaxZ(0);
        SurroundAxionElementGeneration largeBranchGeneration = new SurroundAxionElementGeneration(cypressBranch, greenLeaf, branchAdvance, 0.8f, 1.8f);
        largeBranchGeneration.setMaxZ(0);
        blockMap.put('b', smallBranchGeneration);
        blockMap.put('B', largeBranchGeneration);
        blockMap.put('M', new AdvanceAxionElementGeneration(branchAdvance));

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
