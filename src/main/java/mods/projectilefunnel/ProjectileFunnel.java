package mods.projectilefunnel;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class ProjectileFunnel implements ModInitializer {

    @Override
    public void onInitialize(){}

    public static Identifier getId(String name) {
        return new Identifier("projectile_funnel", name);
    }

}
