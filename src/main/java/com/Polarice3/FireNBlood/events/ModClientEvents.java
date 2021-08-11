package com.Polarice3.FireNBlood.events;

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.entities.hostile.AbstractTaillessEntity;
import com.Polarice3.FireNBlood.entities.neutral.AbstractProtectorEntity;
import com.Polarice3.FireNBlood.init.ModEntityType;
import com.Polarice3.FireNBlood.utils.RegistryHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.EvokerEntity;
import net.minecraft.entity.monster.IllusionerEntity;
import net.minecraft.entity.monster.PillagerEntity;
import net.minecraft.entity.monster.VindicatorEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = FireNBlood.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ModClientEvents {

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void spawnEntities(BiomeLoadingEvent event){
        if (event.getName() != null) {
            Biome biome = ForgeRegistries.BIOMES.getValue(event.getName());
            if (biome != null) {
                if (biome.getCategory() == Biome.Category.NETHER) {

                } else if (biome.getCategory() == Biome.Category.THEEND) {

                } else {
                    if (biome.getCategory() != Biome.Category.OCEAN) {
                            event.getSpawns().getSpawner(EntityClassification.MONSTER).add(new MobSpawnInfo.Spawners(ModEntityType.TAILLESS_WRETCH.get(), 1, 1, 2));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onEntitySpawn(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof PillagerEntity) {
                Entity entity = event.getEntity();
                PillagerEntity illager = (PillagerEntity) entity;
                illager.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(illager, AbstractTaillessEntity.class, false));
                illager.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(illager, AbstractProtectorEntity.class, false));
        }
        if (event.getEntity() instanceof VindicatorEntity) {
                Entity entity = event.getEntity();
                VindicatorEntity illager = (VindicatorEntity) entity;
                illager.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(illager, AbstractTaillessEntity.class, false));
                illager.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(illager, AbstractProtectorEntity.class, false));
        }
        if (event.getEntity() instanceof EvokerEntity) {
                Entity entity = event.getEntity();
                EvokerEntity illager = (EvokerEntity) entity;
                illager.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(illager, AbstractTaillessEntity.class, false));
                illager.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(illager, AbstractProtectorEntity.class, false));
        }
        if (event.getEntity() instanceof IllusionerEntity) {
                Entity entity = event.getEntity();
                IllusionerEntity illager = (IllusionerEntity) entity;
                illager.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(illager, AbstractTaillessEntity.class, false));
                illager.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(illager, AbstractProtectorEntity.class, false));
        }
    }

    @SubscribeEvent
    public static void onPlayerEquipment(TickEvent.PlayerTickEvent event){
        PlayerEntity player = event.player;
        if (player.getItemStackFromSlot(EquipmentSlotType.HEAD).getItem() == RegistryHandler.FURRED_HELMET.get()
                && player.getItemStackFromSlot(EquipmentSlotType.CHEST).getItem() == RegistryHandler.FURRED_CHESTPLATE.get()
                && player.getItemStackFromSlot(EquipmentSlotType.LEGS).getItem() == RegistryHandler.FURRED_LEGGINGS.get()
                && player.getItemStackFromSlot(EquipmentSlotType.FEET).getItem() == RegistryHandler.FURRED_BOOTS.get()
        ){
            player.addPotionEffect(new EffectInstance(Effects.SPEED, 100));
            player.addPotionEffect(new EffectInstance(Effects.HASTE, 100));
        }
    }

    @SubscribeEvent
    public static void DyingTarget(LivingSetAttackTargetEvent event){
        if (event.getTarget() instanceof AbstractProtectorEntity) {
            Entity target = event.getTarget();
            Entity entity = event.getEntity();
            AbstractProtectorEntity protector = (AbstractProtectorEntity) target;
            MobEntity attacker = (MobEntity) entity;
            if (protector.isDying()){
                attacker.setAttackTarget(null);
            }
        }
    }

/*    @SubscribeEvent
    public void SpellcastingPlayer(InputEvent.KeyInputEvent pressed, TickEvent.PlayerTickEvent event, RenderPlayerEvent event2){
        KeyBinding[] keyBindings = RegistryHandler.keyBindings;
        int casttime = 0;
        boolean castingspell = false;
        if (keyBindings[0].isPressed()){
            casttime = 40;
            castingspell = true;
        }
        if (casttime > 0){
            --casttime;
            PlayerEntity player = event2.getPlayer();
            World world = event2.getPlayer().world;
            PlayerModel<AbstractClientPlayerEntity> model = event2.getRenderer().getEntityModel();
            int random = world.rand.nextInt(2);
            MatrixStack matrix = event2.getMatrixStack();
            IVertexBuilder buffer = event2.getBuffers().getBuffer(model.getRenderType(((AbstractClientPlayerEntity) player).getLocationSkin()));
            int light = event2.getLight();
            int texture = OverlayTexture.NO_OVERLAY;
            model.bipedRightArm.rotateAngleX = MathHelper.cos(random * 0.6662F) * 0.25F;
            model.bipedLeftArm.rotateAngleX = MathHelper.cos(random * 0.6662F) * 0.25F;
            model.bipedRightArm.rotateAngleZ = 2.3561945F;
            model.bipedLeftArm.rotateAngleZ = 2.3561945F;
            model.bipedRightArm.showModel = true;
            model.bipedLeftArm.showModel = true;
            model.bipedRightArm.render(matrix, buffer, light, texture);
            model.bipedLeftArm.render(matrix, buffer, light, texture);
            event.player.playSound(SoundEvents.ENTITY_EVOKER_PREPARE_ATTACK, 1.0F, 1.0F);
        } else if (castingspell) {
            Vector3d vector3d = event.player.getLook(1.0F);
            double d0 = Math.min(vector3d.y, event.player.getPosY());
            double d1 = Math.max(vector3d.y, event.player.getPosY()) + 1.0D;
            float f = (float) MathHelper.atan2(vector3d.z + event.player.getPosZ(), vector3d.x + event.player.getPosX());
            for(int l = 0; l < 16; ++l) {
                double d2 = 1.25D * (double)(l + 1);
                int j = 1 * l;
                BlockPos blockpos = new BlockPos(event.player.getPosX() + (double)MathHelper.cos(f) * d2, d1, event.player.getPosZ() + (double)MathHelper.sin(f) * d2);
                boolean flag = false;
                double d3 = 0.0D;

                do {
                    BlockPos blockpos1 = blockpos.down();
                    BlockState blockstate = event.player.world.getBlockState(blockpos1);
                    if (blockstate.isSolidSide(event.player.world, blockpos1, Direction.UP)) {
                        if (!event.player.world.isAirBlock(blockpos)) {
                            BlockState blockstate1 = event.player.world.getBlockState(blockpos);
                            VoxelShape voxelshape = blockstate1.getCollisionShape(event.player.world, blockpos);
                            if (!voxelshape.isEmpty()) {
                                d3 = voxelshape.getEnd(Direction.Axis.Y);
                            }
                        }

                        flag = true;
                        break;
                    }

                    blockpos = blockpos.down();
                } while(blockpos.getY() >= MathHelper.floor(d0) - 1);

                if (flag) {
                    event.player.world.addEntity(new EvokerFangsEntity(event.player.world, event.player.getPosX() + (double)MathHelper.cos(f) * d2, (double)blockpos.getY() + d3, event.player.getPosZ() + (double)MathHelper.sin(f) * d2, f, j, event.player));
                }
            }
        }
    }*/

/*    @SubscribeEvent
    public void CastingASpell(TickEvent.PlayerTickEvent event, RenderPlayerEvent event2){
        if (casttime > 0){
            --casttime;
            PlayerEntity player = event2.getPlayer();
            World world = event2.getPlayer().world;
            PlayerModel<AbstractClientPlayerEntity> model = event2.getRenderer().getEntityModel();
            int random = world.rand.nextInt(2);
            MatrixStack matrix = event2.getMatrixStack();
            IVertexBuilder buffer = event2.getBuffers().getBuffer(model.getRenderType(((AbstractClientPlayerEntity) player).getLocationSkin()));
            int light = event2.getLight();
            int texture = OverlayTexture.NO_OVERLAY;
            model.bipedRightArm.rotateAngleX = MathHelper.cos(random * 0.6662F) * 0.25F;
            model.bipedLeftArm.rotateAngleX = MathHelper.cos(random * 0.6662F) * 0.25F;
            model.bipedRightArm.rotateAngleZ = 2.3561945F;
            model.bipedLeftArm.rotateAngleZ = 2.3561945F;
            model.bipedRightArm.showModel = true;
            model.bipedLeftArm.showModel = true;
            model.bipedRightArm.render(matrix, buffer, light, texture);
            model.bipedLeftArm.render(matrix, buffer, light, texture);
            event.player.playSound(SoundEvents.ENTITY_EVOKER_PREPARE_ATTACK, 1.0F, 1.0F);
        } else if (this.castingspell) {
            Vector3d vector3d = event.player.getLook(1.0F);
            double d0 = Math.min(vector3d.y, event.player.getPosY());
            double d1 = Math.max(vector3d.y, event.player.getPosY()) + 1.0D;
            float f = (float) MathHelper.atan2(vector3d.z + event.player.getPosZ(), vector3d.x + event.player.getPosX());
            for(int l = 0; l < 16; ++l) {
                double d2 = 1.25D * (double)(l + 1);
                int j = 1 * l;
                BlockPos blockpos = new BlockPos(event.player.getPosX() + (double)MathHelper.cos(f) * d2, d1, event.player.getPosZ() + (double)MathHelper.sin(f) * d2);
                boolean flag = false;
                double d3 = 0.0D;

                do {
                    BlockPos blockpos1 = blockpos.down();
                    BlockState blockstate = event.player.world.getBlockState(blockpos1);
                    if (blockstate.isSolidSide(event.player.world, blockpos1, Direction.UP)) {
                        if (!event.player.world.isAirBlock(blockpos)) {
                            BlockState blockstate1 = event.player.world.getBlockState(blockpos);
                            VoxelShape voxelshape = blockstate1.getCollisionShape(event.player.world, blockpos);
                            if (!voxelshape.isEmpty()) {
                                d3 = voxelshape.getEnd(Direction.Axis.Y);
                            }
                        }

                        flag = true;
                        break;
                    }

                    blockpos = blockpos.down();
                } while(blockpos.getY() >= MathHelper.floor(d0) - 1);

                if (flag) {
                    event.player.world.addEntity(new EvokerFangsEntity(event.player.world, event.player.getPosX() + (double)MathHelper.cos(f) * d2, (double)blockpos.getY() + d3, event.player.getPosZ() + (double)MathHelper.sin(f) * d2, f, j, event.player));
                }
            }
            this.castingspell = false;
        }
    }*/

}
