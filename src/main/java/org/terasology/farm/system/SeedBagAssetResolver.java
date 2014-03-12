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

import com.google.common.primitives.UnsignedBytes;
import org.terasology.asset.AssetFactory;
import org.terasology.asset.AssetResolver;
import org.terasology.asset.AssetType;
import org.terasology.asset.AssetUri;
import org.terasology.asset.Assets;
import org.terasology.math.Rect2i;
import org.terasology.rendering.assets.texture.Texture;
import org.terasology.rendering.assets.texture.TextureData;
import org.terasology.rendering.assets.texture.TextureRegion;

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

        BufferedImage resultImage = convertToImage(Assets.getTextureRegion("PlantPack:farming.Pouch"));
        BufferedImage seedTexture = convertToImage(Assets.getTextureRegion(split[1].substring(0, split[1].length() - 1)));

        Graphics2D gr = (Graphics2D) resultImage.getGraphics();
        try {
            gr.drawImage(seedTexture, 0, 0, seedTexture.getWidth() / 2, seedTexture.getHeight() / 2, null);
        } finally {
            gr.dispose();
        }

        final ByteBuffer byteBuffer = convertToByteBuffer(resultImage);
        return factory.buildAsset(uri, new TextureData(resultImage.getWidth(), resultImage.getHeight(), new ByteBuffer[]{byteBuffer}, Texture.WrapMode.REPEAT, Texture.FilterMode.NEAREST));
    }

    private BufferedImage convertToImage(TextureRegion textureRegion) {
        final int width = textureRegion.getWidth();
        final int height = textureRegion.getHeight();

        final Rect2i pixelRegion = textureRegion.getPixelRegion();
        final Texture texture = textureRegion.getTexture();
        ByteBuffer textureBytes = texture.getData().getBuffers()[0];
        int stride = texture.getWidth() * 4;
        int posX = pixelRegion.minX();
        int posY = pixelRegion.minY();

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int r = UnsignedBytes.toInt(textureBytes.get((posY + y) * stride + (posX + x) * 4));
                int g = UnsignedBytes.toInt(textureBytes.get((posY + y) * stride + (posX + x) * 4 + 1));
                int b = UnsignedBytes.toInt(textureBytes.get((posY + y) * stride + (posX + x) * 4 + 2));
                int a = UnsignedBytes.toInt(textureBytes.get((posY + y) * stride + (posX + x) * 4 + 3));

                int argb = (a << 24) + (r << 16) + (g << 8) + b;
                image.setRGB(x, y, argb);
            }
        }
        return image;
    }

    private ByteBuffer convertToByteBuffer(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        ByteBuffer data = ByteBuffer.allocateDirect(4 * width * height);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int argb = image.getRGB(x, y);
                int r = (argb & 0x00FF0000) >> 16;
                int g = (argb & 0x0000FF00) >> 8;
                int b = (argb & 0x000000FF);
                int a = (argb & 0xFF000000) >> 24;
                data.put(UnsignedBytes.checkedCast(r));
                data.put(UnsignedBytes.checkedCast(g));
                data.put(UnsignedBytes.checkedCast(b));
                data.put(UnsignedBytes.checkedCast(a));
            }
        }
        data.rewind();
        return data;
    }
}