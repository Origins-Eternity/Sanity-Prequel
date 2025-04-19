package com.origins_eternity.sanity.compat;

import com.feed_the_beast.ftbquests.events.ObjectCompletedEvent;
import com.origins_eternity.sanity.content.capability.sanity.ISanity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Arrays;

import static com.origins_eternity.sanity.config.Configuration.Mechanics;
import static com.origins_eternity.sanity.content.capability.Capabilities.SANITY;

public class FTBQuests {
    @SubscribeEvent
    public static void onQuestCompleted(ObjectCompletedEvent.QuestEvent event) {
        for (EntityPlayerMP player : event.getNotifiedPlayers()) {
            if (Arrays.stream(Mechanics.dimensions).anyMatch(num -> num == player.dimension)) {
                ISanity sanity = player.getCapability(SANITY, null);
                sanity.recoverSanity(Mechanics.quest);
            }
        }
    }
}