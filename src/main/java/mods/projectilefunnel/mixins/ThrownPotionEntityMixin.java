package mods.projectilefunnel.mixins;

import net.minecraft.block.HopperBlock;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.entity.thrown.ThrownItemEntity;
import net.minecraft.entity.thrown.ThrownPotionEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ThrownPotionEntity.class)
public abstract class ThrownPotionEntityMixin extends ThrownItemEntity implements FlyingItemEntity {

    public ThrownPotionEntityMixin(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "onCollision",at = @At("HEAD"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    public void pleaseLetMeCancelThis(HitResult rtr, CallbackInfo ctx){
        BlockHitResult brtr = (BlockHitResult)rtr;
        if (world.getBlockState(brtr.getBlockPos()).getBlock() instanceof HopperBlock){
            HopperBlockEntity hopper = (HopperBlockEntity)world.getBlockEntity(brtr.getBlockPos());
            boolean isFull = true;
            for (int i = 0; i < hopper.getInvSize(); i++) {
                ItemStack stack = hopper.getInvStack(i);
                if (stack.isEmpty()) {
                    isFull = false;
                    break;
                }
            }
            if (!isFull) {
                ctx.cancel();
                this.remove();
                super.onCollision(rtr);
            }
        }
    }
}
