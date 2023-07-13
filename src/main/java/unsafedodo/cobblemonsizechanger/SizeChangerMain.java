package unsafedodo.cobblemonsizechanger;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class SizeChangerMain implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.

	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register(unsafedodo.cobblemonsizechanger.command.SizeChangeCommand::register);
	}
}