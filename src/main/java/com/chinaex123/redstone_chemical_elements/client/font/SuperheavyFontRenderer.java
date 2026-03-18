package com.chinaex123.redstone_chemical_elements.client.font;

import com.chinaex123.redstone_chemical_elements.RedstonechanChemicalElements;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SuperheavyFontRenderer extends FontRenderer {
    private static final int FIRST_SUPERHEAVY_CHAR = 0xE100;
    private static final int LAST_SUPERHEAVY_CHAR = 0xE106;
    private static final byte SUPERHEAVY_GLYPH_WIDTH = 0x0D;
    private static final int SUPERHEAVY_CHAR_WIDTH = 8;
    private static final float SUPERHEAVY_TEXTURE_OFFSET = 1.0F;
    private static final float SUPERHEAVY_TEXTURE_SIZE = 14.0F;
    private static final float SUPERHEAVY_RENDER_WIDTH = 8.25F;
    private static final float SUPERHEAVY_RENDER_HEIGHT = 8.5F;
    private static final float SUPERHEAVY_VERTICAL_OFFSET = 0.25F;
    private static final int GL_TEXTURE_2D = 3553;
    private static final int GL_TEXTURE_MIN_FILTER = 10241;
    private static final int GL_TEXTURE_MAG_FILTER = 10240;
    private static final int GL_NEAREST = 9728;
    private static final ResourceLocation SUPERHEAVY_PAGE = new ResourceLocation(
            RedstonechanChemicalElements.MODID,
            "textures/font/unicode_page_e1.png"
    );

    public SuperheavyFontRenderer(GameSettings gameSettingsIn, ResourceLocation location, TextureManager textureManagerIn, boolean unicode) {
        super(gameSettingsIn, location, textureManagerIn, unicode);
        applySuperheavyGlyphWidths();
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        super.onResourceManagerReload(resourceManager);
        applySuperheavyGlyphWidths();
    }

    @Override
    protected float renderUnicodeChar(char ch, boolean italic) {
        if (!isSuperheavyChar(ch)) {
            return super.renderUnicodeChar(ch, italic);
        }

        int cellIndex = ch - FIRST_SUPERHEAVY_CHAR;
        float textureX = (cellIndex % 16) * 16.0F + SUPERHEAVY_TEXTURE_OFFSET;
        float textureY = (cellIndex / 16) * 16.0F + SUPERHEAVY_TEXTURE_OFFSET;
        float italicOffset = italic ? 0.4F : 0.0F;
        float top = this.posY + SUPERHEAVY_VERTICAL_OFFSET;
        float bottom = top + SUPERHEAVY_RENDER_HEIGHT;

        bindTexture(SUPERHEAVY_PAGE);
        GlStateManager.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        GlStateManager.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        GlStateManager.glBegin(5);
        GlStateManager.glTexCoord2f(textureX / 256.0F, textureY / 256.0F);
        GlStateManager.glVertex3f(this.posX + italicOffset, top, 0.0F);
        GlStateManager.glTexCoord2f(textureX / 256.0F, (textureY + SUPERHEAVY_TEXTURE_SIZE) / 256.0F);
        GlStateManager.glVertex3f(this.posX - italicOffset, bottom, 0.0F);
        GlStateManager.glTexCoord2f((textureX + SUPERHEAVY_TEXTURE_SIZE) / 256.0F, textureY / 256.0F);
        GlStateManager.glVertex3f(this.posX + SUPERHEAVY_RENDER_WIDTH + italicOffset, top, 0.0F);
        GlStateManager.glTexCoord2f((textureX + SUPERHEAVY_TEXTURE_SIZE) / 256.0F, (textureY + SUPERHEAVY_TEXTURE_SIZE) / 256.0F);
        GlStateManager.glVertex3f(this.posX + SUPERHEAVY_RENDER_WIDTH - italicOffset, bottom, 0.0F);
        GlStateManager.glEnd();
        return SUPERHEAVY_CHAR_WIDTH;
    }

    @Override
    public int getCharWidth(char character) {
        if (isSuperheavyChar(character)) {
            return SUPERHEAVY_CHAR_WIDTH;
        }
        return super.getCharWidth(character);
    }

    private void applySuperheavyGlyphWidths() {
        for (int codePoint = FIRST_SUPERHEAVY_CHAR; codePoint <= LAST_SUPERHEAVY_CHAR; codePoint++) {
            this.glyphWidth[codePoint] = SUPERHEAVY_GLYPH_WIDTH;
        }
    }

    public static boolean isSuperheavyChar(char character) {
        return character >= FIRST_SUPERHEAVY_CHAR && character <= LAST_SUPERHEAVY_CHAR;
    }
}
