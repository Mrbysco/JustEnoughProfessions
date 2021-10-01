package com.mrbysco.justenoughprofessions;

import com.mrbysco.justenoughprofessions.compat.CompatHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.IExtensionPoint.DisplayTest;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(JustEnoughProfessions.MOD_ID)
public class JustEnoughProfessions {
    public static final String MOD_ID = "justenoughprofessions";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public JustEnoughProfessions() {
        //Make sure the mod being absent on the other network side does not cause the client to display the server as incompatible
        ModLoadingContext.get().registerExtensionPoint(DisplayTest.class,()->
                new IExtensionPoint.DisplayTest(() -> "Trans Rights Are Human Rights",
                        (remoteVersionString,networkBool) -> networkBool));

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            MinecraftForge.EVENT_BUS.addListener(CompatHelper::handleTooltips);
        });
    }
}
