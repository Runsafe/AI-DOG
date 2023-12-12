package no.runsafe.dog.cortex.language.queries;

import no.runsafe.dog.cortex.language.ChatResponderRule;
import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.IServer;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;
import no.runsafe.framework.api.player.IPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

public class Where extends ChatResponderRule implements IConfigurationChanged
{
	public Where(IServer server)
	{
		super("(?i).*(where\\sis\\s)([a-zA-Z0-9_-]+).*", null, null, null, null, server);
		this.server = server;
	}

	@Override
	public String getResponse(String player, Matcher message)
	{
		IPlayer target = server.getPlayer(message.group(2).toLowerCase());
		if (target == null || !target.isOnline() || target.isVanished())
			return null;

		String worldName = target.getWorldName();
		if (this.worldMessages.containsKey(worldName))
			return String.format(this.worldMessages.get(worldName), target.getName());

		return null;
	}

	@Override
	public void OnConfigurationChanged(IConfiguration configuration)
	{
		this.worldMessages = configuration.getConfigValuesAsMap("where");
	}

	private final IServer server;
	private Map<String, String> worldMessages = new HashMap<>();
}
