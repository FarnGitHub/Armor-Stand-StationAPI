package farn.armor_stand.client.renderer.model.backport;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.Quad;
import net.minecraft.client.model.Vertex;

public class ReleaseModelPart extends ModelPart {
    public TextureSize texSize;

    public ReleaseModelPart(TextureSize armor, int u, int v) {
        super(u, v);
        this.texSize = armor;
    }

    public void addCuboid(float x, float y, float z, int sizeX, int sizeY, int sizeZ, float dilation) {
        this.corners = new Vertex[8];
        this.faces = new Quad[6];
        float currentX = x + (float)sizeX;
        float currentY = y + (float)sizeY;
        float currentZ = z + (float)sizeZ;
        x -= dilation;
        y -= dilation;
        z -= dilation;
        currentX += dilation;
        currentY += dilation;
        currentZ += dilation;
        if (this.mirror) {
            float prevX = currentX;
            currentX = x;
            x = prevX;
        }

        Vertex v1 = new Vertex(x, y, z, 0.0F, 0.0F);
        Vertex v2 = new Vertex(currentX, y, z, 0.0F, 8.0F);
        Vertex v3 = new Vertex(currentX, currentY, z, 8.0F, 8.0F);
        Vertex v4 = new Vertex(x, currentY, z, 8.0F, 0.0F);
        Vertex v5 = new Vertex(x, y, currentZ, 0.0F, 0.0F);
        Vertex v6 = new Vertex(currentX, y, currentZ, 0.0F, 8.0F);
        Vertex v7 = new Vertex(currentX, currentY, currentZ, 8.0F, 8.0F);
        Vertex v8 = new Vertex(x, currentY, currentZ, 8.0F, 0.0F);
        this.corners[0] = v1;
        this.corners[1] = v2;
        this.corners[2] = v3;
        this.corners[3] = v4;
        this.corners[4] = v5;
        this.corners[5] = v6;
        this.corners[6] = v7;
        this.corners[7] = v8;
        this.faces[0] = new TexturedQuad(new Vertex[]{v6, v2, v3, v7}, this.u + sizeZ + sizeX, this.v + sizeZ, this.u + sizeZ + sizeX + sizeZ, this.v + sizeZ + sizeY, this.texSize.getTextureWidth(), this.texSize.getTextureHeight());
        this.faces[1] = new TexturedQuad(new Vertex[]{v1, v5, v8, v4}, this.u, this.v + sizeZ, this.u + sizeZ, this.v + sizeZ + sizeY, this.texSize.getTextureWidth(), this.texSize.getTextureHeight());
        this.faces[2] = new TexturedQuad(new Vertex[]{v6, v5, v1, v2}, this.u + sizeZ, this.v, this.u + sizeZ + sizeX, this.v + sizeZ, this.texSize.getTextureWidth(), this.texSize.getTextureHeight());
        this.faces[3] = new TexturedQuad(new Vertex[]{v3, v4, v8, v7}, this.u + sizeZ + sizeX, this.v, this.u + sizeZ + sizeX + sizeX, this.v + sizeZ, this.texSize.getTextureWidth(), this.texSize.getTextureHeight());
        this.faces[4] = new TexturedQuad(new Vertex[]{v2, v1, v4, v3}, this.u + sizeZ, this.v + sizeZ, this.u + sizeZ + sizeX, this.v + sizeZ + sizeY, this.texSize.getTextureWidth(), this.texSize.getTextureHeight());
        this.faces[5] = new TexturedQuad(new Vertex[]{v5, v6, v7, v8}, this.u + sizeZ + sizeX + sizeZ, this.v + sizeZ, this.u + sizeZ + sizeX + sizeZ + sizeX, this.v + sizeZ + sizeY, this.texSize.getTextureWidth(), this.texSize.getTextureHeight());
        if (this.mirror)
            for(Quad quad : this.faces)
                quad.flip();

    }

    private static class TexturedQuad extends Quad {

        public TexturedQuad(Vertex[] vertices, int u1, int v1, int u2, int v2, int width, int height) {
            super(vertices, u1, v1, u2, v2);
            vertices[0] = vertices[0].remap((float)u2 / width, (float)v1 / height);
            vertices[1] = vertices[1].remap((float)u1 / width, (float)v1 / height);
            vertices[2] = vertices[2].remap((float)u1 / width, (float)v2 / height);
            vertices[3] = vertices[3].remap((float)u2 / width, (float)v2 / height);
        }
    }
}
