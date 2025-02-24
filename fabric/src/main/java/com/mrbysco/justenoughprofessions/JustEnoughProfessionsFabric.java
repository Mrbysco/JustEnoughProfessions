package com.mrbysco.justenoughprofessions;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientWorldEvents;

public class JustEnoughProfessionsFabric implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ClientWorldEvents.AFTER_CLIENT_WORLD_CHANGE.register((client, world) -> {
			VillagerCache.clearCache();
		});
	}
}
