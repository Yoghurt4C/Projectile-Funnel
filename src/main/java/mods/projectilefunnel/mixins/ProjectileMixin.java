package mods.projectilefunnel.mixins;

import net.minecraft.block.HopperBlock;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.*;
import net.minecraft.entity.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Projectile.class)
public abstract class ProjectileMixin extends Entity implements ProjectileEntityInvoker {

    public ProjectileMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method="onCollision", at=@At("HEAD"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    public void hopperCollisionCheck(HitResult rtr, CallbackInfo ctx){
        if (rtr.getType().equals(HitResult.Type.BLOCK)){
            BlockHitResult brtr = (BlockHitResult)rtr;
            if (world.getBlockState(brtr.getBlockPos()).getBlock() instanceof HopperBlock
                    && brtr.getSide().equals(Direction.UP)){
                HopperBlockEntity hopper = (HopperBlockEntity)world.getBlockEntity(brtr.getBlockPos());
                projectilefunnel_handleInventory(hopper,this);
            }
        }
    }

    public ItemStack projectilefunnel_hardcodedElseIf(Entity projectile){
        if (projectile instanceof WitherSkullEntity){
            return new ItemStack(Items.WITHER_SKELETON_SKULL);
        } else if (projectile instanceof ProjectileEntity) {
            return ((ProjectileEntityInvoker) projectile).getItemStackFromProjectile();
        } else if (projectile instanceof AbstractFireballEntity){
            return ((AbstractFireballEntity) projectile).getStack();
        } else if (projectile instanceof ThrownItemEntity){
            return ((ThrownItemEntity) projectile).getStack();
        }
        else return ItemStack.EMPTY;
    }

    public void projectilefunnel_handleInventory(HopperBlockEntity hopper, Entity projectile){
        ItemStack stack = projectilefunnel_hardcodedElseIf(projectile);
        Item item = stack.getItem();
        for (int i=0;i<hopper.getInvSize();i++){
            if (hopper.getInvStack(i).isEmpty()){
                hopper.setInvStack(i,stack);
                world.updateNeighbors(hopper.getPos(),hopper.getCachedState().getBlock());
                projectile.remove();
                break;
            } else if (hopper.getInvStack(i).getItem().equals(item)
                    && hopper.getInvStack(i).getMaxCount() > 1
                    && hopper.getInvStack(i).getCount() < 64){
                hopper.setInvStack(i, new ItemStack(item, hopper.getInvStack(i).getCount() + 1));
                world.updateNeighbors(hopper.getPos(),hopper.getCachedState().getBlock());
                projectile.remove();
                break;
            }
        }
    }

}
