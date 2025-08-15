package me.ichun.mods.googlyeyes.common.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;

/**
 * Googly Eyes - iChun
 * Created using Tabula 5.1.0
 */
public class ModelGooglyEye extends Model
{
    public final ModelPart root;
    public ModelPart cornea1;
    public ModelPart cornea2;
    public ModelPart cornea3;
    public ModelPart cornea4;
    public ModelPart cornea5;
    public ModelPart cornea6;
    public ModelPart[] iris = new ModelPart[3];

    public ModelGooglyEye()
    {
        super(RenderType::entityCutout);


        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();

        partDefinition.addOrReplaceChild("cornea1",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-0.5F, -1.865F, -1.03F, 1, 3.73F, 1),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        partDefinition.addOrReplaceChild("cornea2",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-0.5F, -1.865F, -1.0F, 1, 3.73F, 1),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F,
                        0.0F, 0.0F, 0.5235987755982988F));

        partDefinition.addOrReplaceChild("cornea3",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-0.5F, -1.865F, -1.02F, 1, 3.73F, 1),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F,
                        0.0F, 0.0F, 1.0471975511965976F));

        partDefinition.addOrReplaceChild("cornea4",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-0.5F, -1.865F, -0.99F, 1, 3.73F, 1),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F,
                        0.0F, 0.0F, 1.5707963267948966F));

        partDefinition.addOrReplaceChild("cornea5",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-0.5F, -1.865F, -1.01F, 1, 3.73F, 1),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F,
                        0.0F, 0.0F, 2.0943951023931953F));

        partDefinition.addOrReplaceChild("cornea6",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-0.5F, -1.865F, -0.98F, 1, 3.73F, 1),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F,
                        0.0F, 0.0F, 2.6179938779914944F));

        // Iris parts
        partDefinition.addOrReplaceChild("iris0",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-0.5F, -0.8665F, -1.51F, 1, 1.733F, 1),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F,
                        0.0F, 0.0F, 2.0943951023931953F));

        partDefinition.addOrReplaceChild("iris1",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-0.5F, -0.8665F, -1.5F, 1, 1.733F, 1),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F,
                        0.0F, 0.0F, 1.0471975511965976F));

        partDefinition.addOrReplaceChild("iris2",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-0.5F, -0.8665F, -1.49F, 1, 1.733F, 1),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        this.root = LayerDefinition.create(meshDefinition, 64, 32).bakeRoot();

        this.cornea1 = root.getChild("cornea1");
        this.cornea2 = root.getChild("cornea2");
        this.cornea3 = root.getChild("cornea3");
        this.cornea4 = root.getChild("cornea4");
        this.cornea5 = root.getChild("cornea5");
        this.cornea6 = root.getChild("cornea6");

        this.iris[0] = root.getChild("iris0");
        this.iris[1] = root.getChild("iris1");
        this.iris[2] = root.getChild("iris2");
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha){}

    public void renderCornea(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
    {
        this.cornea1.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.cornea2.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.cornea3.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.cornea4.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.cornea5.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.cornea6.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    public void renderIris(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
    {
        for(int i = 0; i < iris.length; i++)
        {
            iris[i].render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        }
    }

    public void moveIris(float x, float y, float pupilSize)
    {
        //pupilSize is not for scaling.
        float shiftFactor = (1.45F - pupilSize * 0.525F) / pupilSize;
        float rotX = -x * shiftFactor;// * (float)Math.cos(Math.toRadians((y / 1F) * 90F));
        float rotY = -y * shiftFactor * (float)Math.cos(Math.toRadians(x * 90F));

        for(int i = 0; i < iris.length; i++)
        {
            iris[i].x = rotX;
            iris[i].y = rotY;
        }
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelPart modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}
