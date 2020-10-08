package gregicadditions;

import com.blakebr0.mysticalagradditions.MysticalAgradditions;
import gregicadditions.blocks.GAMetalCasingItemBlock;
import gregicadditions.blocks.GAOreItemBlock;
import gregicadditions.fluid.GAMetaFluids;
import gregicadditions.integrations.mysticalagriculture.items.MysticalAgricultureItems;
import gregicadditions.item.GAMetaBlocks;
import gregicadditions.item.GAMetaItems;
import gregicadditions.pipelike.cable.ItemBlockCable;
import gregicadditions.recipes.*;
import gregicadditions.utils.GregicalityLogger;
import gregicadditions.worldgen.WorldGenRegister;
import gregtech.common.blocks.VariantItemBlock;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

import java.io.IOException;
import java.util.function.Function;

import static gregicadditions.item.GAMetaBlocks.GA_ORES;

@Mod.EventBusSubscriber(modid = Gregicality.MODID)
public class CommonProxy {


    public void preLoad() {
        GAMetaItems.init();
        GAMetaFluids.init();
        WorldGenRegister.preInit();

    }

    public void onLoad() throws IOException {
        WorldGenRegister.init();
    }

    @SubscribeEvent
    public static void syncConfigValues(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(Gregicality.MODID)) {
            ConfigManager.sync(Gregicality.MODID, Config.Type.INSTANCE);
        }
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        GregicalityLogger.logger.info("Registering blocks...");
        IForgeRegistry<Block> registry = event.getRegistry();
        registry.register(GAMetaBlocks.MUTLIBLOCK_CASING);
        registry.register(GAMetaBlocks.REACTOR_CASING);
        registry.register(GAMetaBlocks.FUSION_CASING);
        registry.register(GAMetaBlocks.MACHINE_CASING);
        registry.register(GAMetaBlocks.TRANSPARENT_CASING);
        registry.register(GAMetaBlocks.CELL_CASING);
        registry.register(GAMetaBlocks.CONVEYOR_CASING);
        registry.register(GAMetaBlocks.FIELD_GEN_CASING);
        registry.register(GAMetaBlocks.MOTOR_CASING);
        registry.register(GAMetaBlocks.PISTON_CASING);
        registry.register(GAMetaBlocks.PUMP_CASING);
        registry.register(GAMetaBlocks.ROBOT_ARM_CASING);
        registry.register(GAMetaBlocks.SENSOR_CASING);
        registry.register(GAMetaBlocks.EMITTER_CASING);
        GAMetaBlocks.METAL_CASING.values().stream().distinct().forEach(registry::register);
        GA_ORES.forEach(registry::register);
    }


    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        GregicalityLogger.logger.info("Registering Items...");
        IForgeRegistry<Item> registry = event.getRegistry();

        registry.register(createItemBlock(GAMetaBlocks.CABLE, ItemBlockCable::new));
        registry.register(createItemBlock(GAMetaBlocks.MUTLIBLOCK_CASING, VariantItemBlock::new));
        registry.register(createItemBlock(GAMetaBlocks.REACTOR_CASING, VariantItemBlock::new));
        registry.register(createItemBlock(GAMetaBlocks.MACHINE_CASING, VariantItemBlock::new));
        registry.register(createItemBlock(GAMetaBlocks.FUSION_CASING, VariantItemBlock::new));
        registry.register(createItemBlock(GAMetaBlocks.TRANSPARENT_CASING, VariantItemBlock::new));
        registry.register(createItemBlock(GAMetaBlocks.CELL_CASING, VariantItemBlock::new));
        registry.register(createItemBlock(GAMetaBlocks.CONVEYOR_CASING, VariantItemBlock::new));
        registry.register(createItemBlock(GAMetaBlocks.FIELD_GEN_CASING, VariantItemBlock::new));
        registry.register(createItemBlock(GAMetaBlocks.MOTOR_CASING, VariantItemBlock::new));
        registry.register(createItemBlock(GAMetaBlocks.PISTON_CASING, VariantItemBlock::new));
        registry.register(createItemBlock(GAMetaBlocks.PUMP_CASING, VariantItemBlock::new));
        registry.register(createItemBlock(GAMetaBlocks.ROBOT_ARM_CASING, VariantItemBlock::new));
        registry.register(createItemBlock(GAMetaBlocks.SENSOR_CASING, VariantItemBlock::new));
        registry.register(createItemBlock(GAMetaBlocks.EMITTER_CASING, VariantItemBlock::new));

        GAMetaBlocks.METAL_CASING.values()
                .stream().distinct()
                .map(block -> createItemBlock(block, GAMetalCasingItemBlock::new))
                .forEach(registry::register);

        GA_ORES.stream()
                .map(block -> createItemBlock(block, GAOreItemBlock::new))
                .forEach(registry::register);
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        GregicalityLogger.logger.info("Registering recipe low...");
        if (Loader.isModLoaded(MysticalAgradditions.MOD_ID) && !GAConfig.mysticalAgriculture.disable) {
            MysticalAgricultureItems.removeMARecipe();
        }
        GAMachineRecipeRemoval.init();
        GARecipeAddition.generatedRecipes();
        RecipeHandler.registerLargeChemicalRecipes();
        RecipeHandler.registerLargeMixerRecipes();
        RecipeHandler.registerLargeForgeHammerRecipes();
        RecipeHandler.registerAlloyBlastRecipes();
        RecipeHandler.registerChemicalPlantRecipes();
        RecipeHandler.registerGreenHouseRecipes();
        RecipeHandler.registerLargeCentrifugeRecipes();
        VoidMinerOres.init();
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void registerLateRecipes(RegistryEvent.Register<IRecipe> event) {
        GregicalityLogger.logger.info("Registering recipe lowest...");
        GAMachineRecipeRemoval.init2();
    }

    @SubscribeEvent
    public static void registerOrePrefix(RegistryEvent.Register<IRecipe> event) {
        GregicalityLogger.logger.info("Registering ore prefix...");
        RecipeHandler.register();
        OreRecipeHandler.register();
        GARecipeAddition.init();
        GAMetaItems.registerOreDict();
        GAMetaBlocks.registerOreDict();
        GAMetaItems.registerRecipes();
        GARecipeAddition.init2();
        GARecipeAddition.init3();
        GARecipeAddition.forestrySupport();
        MatterReplication.init();
        MachineCraftingRecipes.init();
        GeneratorFuels.init();

        if (Loader.isModLoaded(MysticalAgradditions.MOD_ID) && !GAConfig.mysticalAgriculture.disable) {
            MysticalAgricultureItems.registerOreDict();
        }

    }

    private static <T extends Block> ItemBlock createItemBlock(T block, Function<T, ItemBlock> producer) {
        ItemBlock itemBlock = producer.apply(block);
        itemBlock.setRegistryName(block.getRegistryName());
        return itemBlock;
    }
}
