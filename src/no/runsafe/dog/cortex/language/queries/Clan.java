package no.runsafe.dog.cortex.language.queries;

import no.runsafe.dog.cortex.language.ChatResponderRule;
import no.runsafe.framework.api.IServer;
import no.runsafe.framework.api.player.IPlayer;

import java.util.Map;
import java.util.regex.Matcher;

public class Clan extends ChatResponderRule
{
	public Clan(IServer server)
	{
		super("(?i).*(which|what)\\s+clan\\s+is\\s+([a-zA-Z0-9_-]+).*", null, null, null, server);
		this.server = server;
	}

	@Override
	public String getResponse(String player, Matcher message)
	{
		IPlayer targetPlayer = server.getPlayerExact(message.group(2));
		if (targetPlayer != null)
		{
			Map<String, String> data = targetPlayer.getData();
			if (data.containsKey("runsafe.clans.clan"))
			{
				String clanName = data.get("runsafe.clans.clan");

				if (!clanName.equals("None"))
					return String.format(
							"%s has been a member of the %s clan for %s.",
							targetPlayer.getPrettyName(),
							data.get("runsafe.clans.clan"),
							data.get("runsafe.clans.joined")
					);
			}
		}
		return null;
	}

	private final IServer server;
}
