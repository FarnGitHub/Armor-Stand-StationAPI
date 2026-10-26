package farn.armor_stand;

import farn.armor_stand.config.Config;
import farn.armor_stand.event.MapItemToSlotEvent;
import farn.armor_stand.world.ArmorStandEntity;
import farn.armor_stand.world.ArmorStandItem;
import farn.armor_stand.world.inventory.ArmorStandGuiHandler;
import farn.armor_stand.world.inventory.ArmorStandScreenHandler;
import net.fabricmc.loader.api.FabricLoader;
import net.glasslauncher.mods.alwaysmoreitems.registry.RecipeRegistry;
import net.glasslauncher.mods.gcapi3.api.ConfigRoot;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.minecraft.block.Block;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.entity.EntityRegisterEvent;
import net.modificationstation.stationapi.api.event.mod.InitEvent;
import net.modificationstation.stationapi.api.event.recipe.RecipeRegisterEvent;
import net.modificationstation.stationapi.api.event.registry.GuiHandlerRegistryEvent;
import net.modificationstation.stationapi.api.event.registry.ItemRegistryEvent;
import net.modificationstation.stationapi.api.event.registry.MobHandlerRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EntrypointManager;
import net.modificationstation.stationapi.api.recipe.CraftingRegistry;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;

@SuppressWarnings("unused")
public class ArmorStandMain {

    @Entrypoint.Namespace
    public static Namespace NS;

    public static Item ARMOR_STAND_ITEM;

    @ConfigRoot(value = "armor_stand_config", visibleName = "Armor Stand Config")
    public static Config CONFIG = new Config();

    @EventListener
    public void initEvent(InitEvent event) {
        FabricLoader.getInstance().getEntrypointContainers("armor_stand:event_bus", Object.class).forEach(EntrypointManager::setup);
    }

    @EventListener
    public void registerEntity(EntityRegisterEvent event) {
        event.register(id("armor_stand"), ArmorStandEntity.class);
    }

    @EventListener
    public void registerMobMP(MobHandlerRegistryEvent event) {
        event.register(id("armor_stand"), ArmorStandEntity::new);
    }

    @EventListener
    public void registerItem(ItemRegistryEvent event) {
        ARMOR_STAND_ITEM = new ArmorStandItem().setTranslationKey(id("armor_stand"));
    }

    @EventListener
    public void registerScreenHandler(GuiHandlerRegistryEvent event) {
        event.register(id("armor_stand_screen"), ArmorStandGuiHandler.INSTANCE);
    }

    @EventListener
    public void registerRecipe(RecipeRegisterEvent event) {
        if(event.recipeId == RecipeRegisterEvent.Vanilla.CRAFTING_SHAPED.type()) {
            CraftingRegistry.addShapedRecipe(
                    new ItemStack(ARMOR_STAND_ITEM, 1),
                    "xxx", " x ", "xyx",
                    'x', Item.STICK,
                    'y', new ItemStack(Block.SLAB, 1, 0)
            );
        }
    }

    public static Identifier id(String id) {
        return NS.id(id);
    }

    public static boolean isGuiMode() {
        return CONFIG.GUI_MODE;
    }
}
