package com.Polarice3.FireNBlood.inventory.container;

import com.Polarice3.FireNBlood.FireNBlood;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = FireNBlood.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModContainerType {
    public static DeferredRegister<ContainerType<?>> CONTAINER_TYPE = DeferredRegister.create(ForgeRegistries.CONTAINERS, FireNBlood.MOD_ID);

/*    public static final RegistryObject<ContainerType<SoulForgeContainer>> SOULFORGE = CONTAINER_TYPE.register("soulforge",
            () -> IForgeContainerType.create(SoulForgeContainer::new));*/

    public static final RegistryObject<ContainerType<SoulItemContainer>> WAND = CONTAINER_TYPE.register("wand",
        () -> IForgeContainerType.create(SoulItemContainer::createContainerClientSide));

    public static final RegistryObject<ContainerType<FocusBagContainer>> FOCUSBAG = CONTAINER_TYPE.register("focusbag",
            () -> IForgeContainerType.create(FocusBagContainer::createContainerClientSide));

    public static final RegistryObject<ContainerType<WandandBagContainer>> WANDANDBAG = CONTAINER_TYPE.register("wandandbag",
            () -> IForgeContainerType.create(WandandBagContainer::createContainerClientSide));


}
