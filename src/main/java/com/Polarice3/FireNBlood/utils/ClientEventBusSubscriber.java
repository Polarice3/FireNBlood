package com.Polarice3.FireNBlood.utils;

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.client.gui.screen.inventory.FocusBagScreen;
import com.Polarice3.FireNBlood.client.gui.screen.inventory.SoulItemScreen;
import com.Polarice3.FireNBlood.client.gui.screen.inventory.WandandBagScreen;
import com.Polarice3.FireNBlood.client.render.*;
import com.Polarice3.FireNBlood.client.render.tileentities.FangTotemTileEntityRenderer;
import com.Polarice3.FireNBlood.client.render.tileentities.MutateTotemTileEntityRenderer;
import com.Polarice3.FireNBlood.client.render.tileentities.TLightningTotemTileEntityRenderer;
import com.Polarice3.FireNBlood.client.render.tileentities.WindTotemTileEntityRenderer;
import com.Polarice3.FireNBlood.entities.ally.CreeperlingMinionEntity;
import com.Polarice3.FireNBlood.init.ModEntityType;
import com.Polarice3.FireNBlood.inventory.container.ModContainerType;
import com.Polarice3.FireNBlood.items.ModSpawnEggItem;
import com.Polarice3.FireNBlood.tileentities.ModTileEntityType;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = FireNBlood.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventBusSubscriber {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event){
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.TAILLESS_WRETCH.get(), TaillessWretchRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.TAILLESS_DRUID.get(), TaillessDruidRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.TAILLESS_HORROR.get(), TaillessHorrorRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.BLACK_BULL.get(), BlackBullRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.BULLET.get(), BulletRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.MINOTAUR.get(), MinotaurRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.TAILLESS_PROPHET.get(), BullmanProphetRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.ANATHEMA.get(), AnathemaRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.ROYALBULLET.get(), RoyalBulletRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.TANK.get(), TankRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.FRIENDTANK.get(), FriendTankRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.CALLER.get(), CallerRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.SOUL_FIREBALL.get(), SoulFireballRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.WARPED_SPEAR.get(), WarpedSpearRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.SPEAR.get(), SpearRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.WITCHBOMB.get(), WitchBombRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.SLOWBOMB.get(), SlowBombRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.NETHERBALL.get(), NetherBallRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.SOULSKULL.get(), SoulSkullRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.WITCHGALE.get(), WitchGaleRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.REDEMPTOR.get(), RedemptorRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.PROTECTOR.get(), ProtectorRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.BREWER.get(), BrewerRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.HEXER.get(), HexerRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.MIRAGE.get(), MirageRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.SAVAGER.get(), SavagerRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.NEOPHYTE.get(), NeophyteRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.ACOLYTE.get(), AcolyteRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.CHANNELLER.get(), ChannellerRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.FANATIC.get(), FanaticRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.ZEALOT.get(), ZealotRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.DISCIPLE.get(), DiscipleRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.APOSTLE.get(), ApostleRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.ZOMBIE_VILLAGER_MINION.get(), ZombieVillagerMinionRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.SKELETON_VILLAGER_MINION.get(), SkeletonVillagerMinionRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.ENVIOKER.get(), EnviokerRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.MUTATED_COW.get(), MutatedCowRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.MUTATED_CHICKEN.get(), MutatedChickenRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.MUTATED_SHEEP.get(), MutatedSheepRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.MUTATED_PIG.get(), MutatedPigRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.MUTATED_RABBIT.get(), MutantRabbitRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.SACRED_FISH.get(), SacredFishRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.PARASITE.get(), ParasiteRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.FRIENDLY_VEX.get(), FriendlyVexRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.FRIENDLY_SCORCH.get(), FriendlyScorchRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.ZOMBIE_MINION.get(), ZombieMinionRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.SKELETON_MINION.get(), SkeletonMinionRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.SPIDERLING_MINION.get(), SpiderlingMinionRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.CREEPERLING_MINION.get(), CreeperlingMinionRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.VIZIER.get(), VizierRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.IRK.get(), IrkRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.SCORCH.get(), ScorchRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.NETHERNAL.get(), NethernalRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.PENANCE.get(), PenanceRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.FAKESEAT.get(), FakeSeatRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.LIGHTNINGTRAP.get(), LightningTrapRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntityType.FANG_TOTEM.get(), FangTotemTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntityType.MUTATE_TOTEM.get(), MutateTotemTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntityType.TLIGHTNING_TOTEM.get(), TLightningTotemTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntityType.WIND_TOTEM.get(), WindTotemTileEntityRenderer::new);
        RenderTypeLookup.setRenderLayer(RegistryHandler.CURSED_CAGE_BLOCK.get(), RenderType.translucent());
        RenderTypeLookup.setRenderLayer(RegistryHandler.CURSED_BARS_BLOCK.get(), RenderType.translucent());
        ScreenManager.register(ModContainerType.WAND.get(), SoulItemScreen::new);
        ScreenManager.register(ModContainerType.FOCUSBAG.get(), FocusBagScreen::new);
        ScreenManager.register(ModContainerType.WANDANDBAG.get(), WandandBagScreen::new);
    }

    @SubscribeEvent
    public static void onRegisterEntities(final RegistryEvent.Register<EntityType<?>> event){
        ModSpawnEggItem.initSpawnEggs();
    }

}
