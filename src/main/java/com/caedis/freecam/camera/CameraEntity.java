package com.caedis.freecam.camera;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import lombok.Setter;

public class CameraEntity extends EntityLivingBase {

    @Setter
    private CollisionMode collisionMode = CollisionMode.FULL;

    public CameraEntity(World world, EntityPlayer player) {
        super(world);
        setSize(0.4F, 0.2F);
        yOffset = 1.62F;
        setPositionAndRotation(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        lastTickPosX = posX;
        lastTickPosY = posY;
        lastTickPosZ = posZ;
        prevRotationYaw = rotationYaw;
        prevRotationPitch = rotationPitch;
    }

    @Override
    public void setPosition(double x, double y, double z) {
        posX = x;
        posY = y;
        posZ = z;
        float hw = width / 2.0F;
        float hh = height / 2.0F;
        boundingBox.setBounds(x - hw, y - hh, z - hw, x + hw, y + hh, z + hw);
    }

    @Override
    public void onUpdate() {
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        lastTickPosX = posX;
        lastTickPosY = posY;
        lastTickPosZ = posZ;
        prevRotationYaw = rotationYaw;
        prevRotationPitch = rotationPitch;
        prevRotationYawHead = rotationYawHead;
    }

    @Override
    public void moveEntity(double dx, double dy, double dz) {
        if (collisionMode == CollisionMode.NONE) {
            noClip = true;
            setPosition(posX + dx, posY + dy, posZ + dz);
        } else if (collisionMode == CollisionMode.FULL) {
            noClip = false;
            super.moveEntity(dx, dy, dz);
            // Fix posY: super derives it as bb.minY + yOffset - ySize, but we want bb center
            posY = (boundingBox.minY + boundingBox.maxY) / 2.0D;
        } else {
            noClip = false;
            moveEntityFiltered(dx, dy, dz);
        }
    }

    private void moveEntityFiltered(double dx, double dy, double dz) {
        AxisAlignedBB targetBB = boundingBox.addCoord(dx, dy, dz);
        List<AxisAlignedBB> collisions = getFilteredCollisionBoxes(targetBB);

        double origDy = dy;

        for (AxisAlignedBB aabb : collisions) {
            dy = aabb.calculateYOffset(boundingBox, dy);
        }
        boundingBox.offset(0.0D, dy, 0.0D);

        for (AxisAlignedBB aabb : collisions) {
            dx = aabb.calculateXOffset(boundingBox, dx);
        }
        boundingBox.offset(dx, 0.0D, 0.0D);

        for (AxisAlignedBB aabb : collisions) {
            dz = aabb.calculateZOffset(boundingBox, dz);
        }
        boundingBox.offset(0.0D, 0.0D, dz);

        posX = (boundingBox.minX + boundingBox.maxX) / 2.0D;
        posY = (boundingBox.minY + boundingBox.maxY) / 2.0D;
        posZ = (boundingBox.minZ + boundingBox.maxZ) / 2.0D;

        isCollidedHorizontally = dx != dx || dz != dz;
        isCollidedVertically = origDy != dy;
        onGround = isCollidedVertically && origDy < 0.0D;
        isCollided = isCollidedHorizontally || isCollidedVertically;
    }

    private List<AxisAlignedBB> getFilteredCollisionBoxes(AxisAlignedBB area) {
        List<AxisAlignedBB> result = new ArrayList<>();

        int minX = MathHelper.floor_double(area.minX);
        int maxX = MathHelper.floor_double(area.maxX + 1.0D);
        int minY = MathHelper.floor_double(area.minY);
        int maxY = MathHelper.floor_double(area.maxY + 1.0D);
        int minZ = MathHelper.floor_double(area.minZ);
        int maxZ = MathHelper.floor_double(area.maxZ + 1.0D);

        for (int bx = minX; bx < maxX; ++bx) {
            for (int bz = minZ; bz < maxZ; ++bz) {
                if (!worldObj.blockExists(bx, 64, bz)) continue;

                for (int by = minY - 1; by < maxY; ++by) {
                    Block block = worldObj.getBlock(bx, by, bz);

                    if (shouldIgnoreBlock(block)) continue;

                    block.addCollisionBoxesToList(worldObj, bx, by, bz, area, result, this);
                }
            }
        }

        return result;
    }

    private boolean shouldIgnoreBlock(Block block) {
        return switch (collisionMode) {
            case IGNORE_TRANSPARENT -> !block.isOpaqueCube();
            case IGNORE_OPENABLE -> block instanceof BlockDoor || block instanceof BlockTrapDoor
                || block instanceof BlockFenceGate;
            default -> false;
        };
    }

    @Override
    public float getEyeHeight() {
        return 0.0F;
    }

    @Override
    public boolean isEntityAlive() {
        return true;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }

    @Override
    protected void damageEntity(DamageSource source, float amount) {}

    @Override
    public ItemStack getHeldItem() {
        return null;
    }

    @Override
    public ItemStack getEquipmentInSlot(int slot) {
        return null;
    }

    @Override
    public void setCurrentItemOrArmor(int slot, ItemStack stack) {}

    @Override
    public ItemStack[] getLastActiveItems() {
        return new ItemStack[0];
    }
}
