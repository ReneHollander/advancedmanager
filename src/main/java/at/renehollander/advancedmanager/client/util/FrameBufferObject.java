package at.renehollander.advancedmanager.client.util;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

import java.nio.ByteBuffer;

public class FrameBufferObject {

    private int width;
    private int height;
    private boolean depth;

    private int fboId;
    private int texId;
    private int depId;

    public FrameBufferObject(int width, int height, boolean depth) {
        this.width = width;
        this.height = height;
        this.depth = depth;
    }

    public void initialize() {
        this.texId = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.getTexId());
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, this.getWidth(), this.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer) null);
        this.fboId = EXTFramebufferObject.glGenFramebuffersEXT();
        EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, this.getFboId());
        EXTFramebufferObject.glFramebufferTexture2DEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, EXTFramebufferObject.GL_COLOR_ATTACHMENT0_EXT, GL11.GL_TEXTURE_2D, this.getTexId(), 0);
        if (this.isDepth()) {
            this.depId = EXTFramebufferObject.glGenRenderbuffersEXT();
            EXTFramebufferObject.glBindRenderbufferEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT, this.getDepId());
            EXTFramebufferObject.glRenderbufferStorageEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT, GL14.GL_DEPTH_COMPONENT24, this.getWidth(), this.getHeight());
            EXTFramebufferObject.glFramebufferRenderbufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, EXTFramebufferObject.GL_DEPTH_ATTACHMENT_EXT, EXTFramebufferObject.GL_RENDERBUFFER_EXT, this.getDepId());
        }
        int status = EXTFramebufferObject.glCheckFramebufferStatusEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT);
        if (status != EXTFramebufferObject.GL_FRAMEBUFFER_COMPLETE_EXT) {
            throw new RuntimeException("fbo config not supported");
        }
        EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, 0);
        if (this.isDepth())
            EXTFramebufferObject.glBindRenderbufferEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT, 0);
    }

    public void destroy() {

    }

    public void bindFrameBuffer() {
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPushMatrix();
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPushMatrix();

        // setup modelview matrix
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();

        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);

        GL11.glViewport(0, 0, this.getWidth(), this.getHeight());
        GL11.glOrtho(0.0D, 1.0, 1.0, 0.0, -10.0, 10.0);

        EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, this.getFboId());
        if (this.isDepth())
            EXTFramebufferObject.glBindRenderbufferEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT, this.getDepId());
    }

    public void unbindFrameBuffer() {
        EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, 0);
        if (this.isDepth())
            EXTFramebufferObject.glBindRenderbufferEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT, 0);
        GL11.glViewport(0, 0, Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPopMatrix();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPopMatrix();
        GL11.glPopAttrib();
    }

    public void bindTexture() {

    }

    public void unbindTexture() {

    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isDepth() {
        return depth;
    }

    public int getFboId() {
        return fboId;
    }

    public int getTexId() {
        return texId;
    }

    public int getDepId() {
        return depId;
    }

}
