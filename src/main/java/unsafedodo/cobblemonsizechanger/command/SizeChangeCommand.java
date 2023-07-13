package unsafedodo.cobblemonsizechanger.command;

import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.util.PlayerExtensionsKt;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import me.lucko.fabric.api.permissions.v0.*;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;


public class SizeChangeCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment){
        dispatcher.register(CommandManager.literal("pokesize").requires(Permissions.require("pokesize.set", 3))
                .then(CommandManager.argument("player", EntityArgumentType.player())
                        .then(CommandManager.argument("slot", IntegerArgumentType.integer(1,6))
                                .then(CommandManager.argument("scale", DoubleArgumentType.doubleArg(1)).executes(SizeChangeCommand::run)))));
    }

    public static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity playerTarget = EntityArgumentType.getPlayer(context, "player");
        int slot = IntegerArgumentType.getInteger(context, "slot");
        PlayerPartyStore party = PlayerExtensionsKt.party(playerTarget);
        Pokemon selectedPokemon = PlayerExtensionsKt.party(playerTarget).get(slot-1);
        double scale = DoubleArgumentType.getDouble(context,"scale");
        if(selectedPokemon != null){
            selectedPokemon.setScaleModifier((float) scale);
            party.set(slot-1, selectedPokemon);
            context.getSource().sendFeedback(Text.literal(String.format("Pokémon's size for %s has been changed", playerTarget.getEntityName())).formatted(Formatting.GREEN), false);
            return 0;
        } else {
            context.getSource().sendFeedback(Text.literal(String.format("Slot %d does not have a pokémon. Please select another slot", slot)).formatted(Formatting.RED), false);
            return -1;
        }
    }
}
