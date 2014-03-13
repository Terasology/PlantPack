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
package org.terasology.farm.system;

import org.terasology.asset.AssetFactory;
import org.terasology.asset.AssetResolver;
import org.terasology.asset.AssetType;
import org.terasology.asset.AssetUri;
import org.terasology.asset.Assets;
import org.terasology.rendering.assets.texture.Texture;
import org.terasology.rendering.assets.texture.TextureData;
import org.terasology.rendering.assets.texture.TextureUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

public class SeedBagAssetResolver implements AssetResolver<Texture, TextureData> {
    @Override
    public AssetUri resolve(String partialUri) {
        String[] parts = partialUri.split("\\(", 2);
        if (parts.length > 1) {
            AssetUri uri = Assets.resolveAssetUri(AssetType.TEXTURE, parts[0]);
            if (uri != null) {
                return new AssetUri(AssetType.TEXTURE, uri.getModuleName(), partialUri);
            }
        }
        return null;
    }

    @Override
    public Texture resolve(AssetUri uri, AssetFactory<TextureData, Texture> factory) {
        if (!"plantpack".equals(uri.getNormalisedModuleName())
                || !uri.getNormalisedAssetName().startsWith("seedbag(")) {
            return null;
        }
        String assetName = uri.getAssetName();
        String[] split = assetName.split("\\(");

        BufferedImage resultImage = TextureUtil.convertToImage(Assets.getTextureRegion("PlantPack:farming.Pouch"));
        BufferedImage seedTexture = TextureUtil.convertToImage(Assets.getTextureRegion(split[1].substring(0, split[1].length() - 1)));

        Graphics2D gr = (Graphics2D) resultImage.getGraphics();
        try {
            int resultWidth = resultImage.getWidth();
            int resultHeight = resultImage.getHeight();
            gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            gr.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            gr.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            gr.drawImage(seedTexture, resultWidth / 4, 3 * resultHeight / 8, resultWidth / 2, resultWidth / 2, null);
        } finally {
            gr.dispose();
        }

        final ByteBuffer byteBuffer = TextureUtil.convertToByteBuffer(resultImage);
        return factory.buildAsset(uri, new TextureData(resultImage.getWidth(), resultImage.getHeight(), new ByteBuffer[]{byteBuffer}, Texture.WrapMode.REPEAT, Texture.FilterMode.NEAREST));
    }
}