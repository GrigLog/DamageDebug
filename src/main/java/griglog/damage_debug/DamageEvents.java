package griglog.damage_debug;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.text.DecimalFormat;

import static net.minecraft.util.Util.NIL_UUID;

@Mod.EventBusSubscriber
public class DamageEvents {
    static String buffer;
    static float absorbtion = 0;
    static DecimalFormat df = new DecimalFormat("#.###");

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void entityGotHitBeforeArmor(LivingHurtEvent event) {
        LivingEntity target = event.getEntityLiving();
        if (target == null)
            return;
        buffer = "(" + df.format(event.getAmount()) + " : ";
        absorbtion = target.getAbsorptionAmount();
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void entityGotHitAfterArmor(LivingDamageEvent event) {
        LivingEntity target = event.getEntityLiving();
        TextFormatting color;
        PlayerEntity player;
        if (target instanceof PlayerEntity) {
            player = (PlayerEntity) target;
            color = TextFormatting.DARK_RED;
        } else {
            player = target.level.getNearestPlayer(target, 10);
            if (player == null)
                return;
            color = TextFormatting.YELLOW;
        }
        float damage = event.getAmount() + absorbtion - target.getAbsorptionAmount();
        buffer += df.format(damage) + ") ";
        DamageSource ds = event.getSource();
        if (ds.isMagic())
            buffer += "magic ";
        if (ds.isBypassArmor())
            buffer += "unbl ";
        if (ds.isBypassMagic())
            buffer += "abs ";
        send(player, buffer, color);
        buffer = "";
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void entityGotHitBeforeArmor(LivingHealEvent event) {
        if (!DamageDebug.showHeal)
            return;
        LivingEntity target = event.getEntityLiving();
        if (target == null)
            return;
        if (target instanceof PlayerEntity){
            send((PlayerEntity) target, "("+df.format(event.getAmount())+")", TextFormatting.GREEN);
        } else {
            PlayerEntity player = target.level.getNearestPlayer(target, 10);
            if (player == null)
                return;
            send(player, "("+df.format(event.getAmount())+")", TextFormatting.DARK_GREEN);
        }
    }

    static void send(PlayerEntity player, String text, TextFormatting color){
        player.sendMessage(new StringTextComponent(color + text), NIL_UUID);
    }
}

