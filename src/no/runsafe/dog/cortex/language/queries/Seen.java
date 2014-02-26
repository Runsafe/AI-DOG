package no.runsafe.dog.cortex.language.queries;

import no.runsafe.dog.cortex.language.ChatResponderRule;
import no.runsafe.framework.RunsafePlugin;
import no.runsafe.framework.api.IServer;
import no.runsafe.framework.api.command.ICommandHandler;
import no.runsafe.framework.api.command.IPreparedCommand;
import no.runsafe.framework.api.player.IAmbiguousPlayer;
import no.runsafe.framework.api.player.IPlayer;

import java.util.regex.Matcher;

public class Seen extends ChatResponderRule
{
	public Seen(IServer server)
	{
		super("(?i).*(has.*seen|have.*seen|when.*was) ([a-zA-Z0-9_-]+).*", null, null, null, null, server);
		this.server = server;
	}

	@Override
	public String getResponse(String player, Matcher message)
	{
		IPlayer who = server.getPlayer(message.group(2).toLowerCase());
		if (who == null || (who instanceof IAmbiguousPlayer))
			return null;

		ICommandHandler command = getSeenCommand();
		if (command == null)
			return null;

		IPreparedCommand seen = command.prepare(server.getPlayerExact(player), who.getName());
		return seen.executeDirect();
	}

	private ICommandHandler getSeenCommand()
	{
		return RunsafePlugin.getPluginCommand("seen");
	}

	private final IServer server;
}
