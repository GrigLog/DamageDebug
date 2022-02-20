package griglog.damage_debug;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.NewChatGui;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.text.DecimalFormat;


public class ClientDamageEvents {
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
        TextFormatting color = TextFormatting.YELLOW;
        if (target instanceof PlayerEntity)
            color = TextFormatting.DARK_RED;
        else {
            boolean playerNear = false;
            for (Entity e : target.level.getEntities(null,
                    new AxisAlignedBB(target.position().add(-10, -10, -10), target.position().add(10, 10, 10)))){
                if (e instanceof PlayerEntity) {
                    playerNear = true;
                    break;
                }
            }
            if (!playerNear)
                return;
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
        printChat(buffer, color);
        buffer = "";
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void entityGotHitBeforeArmor(LivingHealEvent event) {
        if (!DamageDebug.showHeal)
            return;
        LivingEntity target = event.getEntityLiving();
        if (target == null)
            return;
        TextFormatting color = target instanceof PlayerEntity ? TextFormatting.GREEN : TextFormatting.DARK_GREEN;
        printChat("(" + df.format(event.getAmount()) + ")", color);
    }

    static void printChat(String s, TextFormatting color) {
        NewChatGui chat = Minecraft.getInstance().gui.getChat();
        StringTextComponent msg = new StringTextComponent(s);
        chat.addMessage(new StringTextComponent(color + s));
    }
}

