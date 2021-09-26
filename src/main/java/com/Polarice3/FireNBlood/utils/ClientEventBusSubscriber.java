package com.Polarice3.FireNBlood.utils;

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.client.render.*;
import com.Polarice3.FireNBlood.init.ModEntityType;
import com.Polarice3.FireNBlood.items.ModSpawnEggItem;
import net.minecraft.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = FireNBlood.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventBusSubscriber {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event){
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.TAILLESS_WRETCH.get(), BullmanRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.TAILLESS_DRUID.get(), BullmanDruidRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.TAILLESS_HORROR.get(), BulldrakeRenderer::new);
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
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.ETHEREAL_PUNCH.get(), EtherealPunchRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.WARPED_SPEAR.get(), WarpedSpearRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.SLOWBOMB.get(), SlowBombRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.REDEMPTOR.get(), RedemptorRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.PROTECTOR.get(), ProtectorRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.BREWER.get(), BrewerRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.HEXER.get(), HexerRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.MIRAGE.get(), MirageRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.QUELL.get(), QuellRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.SAVAGER.get(), SavagerRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.NEOPHYTE.get(), NeophyteRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.VIZIER.get(), VizierRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.IRK.get(), IrkRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.PENANCE.get(), PenanceRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.FAKESEAT.get(), FakeSeatRenderer::new);

    }

    @SubscribeEvent
    public static void onRegisterEntities(final RegistryEvent.Register<EntityType<?>> event){
        ModSpawnEggItem.initSpawnEggs();
    }

}
