package no.runsafe.dog.cortex.language.queries;

import no.runsafe.dog.cortex.language.ChatResponderRule;
import no.runsafe.framework.RunsafePlugin;
import no.runsafe.framework.api.IServer;
import no.runsafe.framework.api.command.ICommandHandler;
import no.runsafe.framework.api.command.IPreparedCommand;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.internal.command.PreparedAsynchronousCommand;
import no.runsafe.framework.minecraft.player.RunsafeAmbiguousPlayer;

import java.util.regex.Matcher;

public class Seen extends ChatResponderRule
{
	public Seen(IServer server)
	{
		super("(?i).*(has.*seen|have.*seen|when.*was) ([a-zA-Z0-9_-]+).*", null, null, null, server);
		this.server = server;
	}

	@Override
	public String getResponse(String player, Matcher message)
	{
		IPlayer who = server.getPlayer(message.group(2).toLowerCase());
		if (who == null || (who instanceof RunsafeAmbiguousPlayer))
			return null;

		ICommandHandler command = getSeenCommand();
		if (command == null)
			return null;

		IPreparedCommand seen = command.prepare(server.getPlayerExact(player), new String[]{who.getName()});
		if (seen instanceof PreparedAsynchronousCommand)
			return ((PreparedAsynchronousCommand) seen).executeDirect();
		else
			return seen.execute();
	}

	private ICommandHandler getSeenCommand()
	{
		return RunsafePlugin.getPluginCommand("seen");
	}

	private final IServer server;
}
