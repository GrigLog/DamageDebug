package griglog.damage_debug;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

@Mod("damage_debug")
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DamageDebug {
    public static boolean showHeal;
    @SubscribeEvent
    static void setup(FMLCommonSetupEvent event){
        String path = "config/damage_debug.txt";
        try {
            if (!Files.exists(Paths.get(path))) {
                FileWriter w = new FileWriter(path);
                w.write("show healed:true");
                w.close();
            }
            String file = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
            if (file.equals("show healed:true"))
                showHeal = true;
            else if (file.equals("show healed:false"))
                showHeal = false;
            else
                throw new IOException();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
