package griglog.damage_debug;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ModCommands {
    @SubscribeEvent
    static void register(RegisterCommandsEvent event){
        event.getDispatcher().register(Commands.literal("damagedebug").executes(ModCommands::toggle)
            .then(Commands.literal("heals").executes(ModCommands::toggleHeals)));
    }

    static int toggle(CommandContext<CommandSource> ctx){
        Config.show = !Config.show;
        Config.save();
        ctx.getSource().sendSuccess(new StringTextComponent("Damage Debug output is now " + Config.show), true);
        return 0;
    }

    static int toggleHeals(CommandContext<CommandSource> ctx){
        Config.showHeals = !Config.showHeals;
        Config.save();
        ctx.getSource().sendSuccess(new StringTextComponent("Healing output is now " + Config.showHeals), true);
        return 0;
    }
}
