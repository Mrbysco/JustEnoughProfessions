package com.mrbysco.justenoughprofessions;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLevelEvents;

public class JustEnoughProfessionsFabric implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ClientLevelEvents.AFTER_CLIENT_LEVEL_CHANGE.register((client, world) -> {
			VillagerCache.clearCache();
		});
	}
}
