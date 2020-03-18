package mods.projectilefunnel.mixins;

import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ProjectileEntity.class)
public interface ProjectileEntityInvoker {
    @Invoker(value="asItemStack")
    ItemStack getItemStackFromProjectile();
}
