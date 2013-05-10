package no.runsafe.dog.cortex.language.queries;

import no.runsafe.dog.cortex.language.ChatResponderRule;
import no.runsafe.framework.configuration.IConfiguration;
import no.runsafe.framework.event.IConfigurationChanged;
import no.runsafe.framework.server.RunsafeServer;
import no.runsafe.framework.server.player.RunsafePlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Where extends ChatResponderRule implements IConfigurationChanged
{
	public Where()
	{
		super(null, null, null, null);
	}

	@Override
	public String getResponse(String player, String message)
	{
		Matcher results = question.matcher(message);

		if (results.matches())
		{
			RunsafePlayer target = RunsafeServer.Instance.getPlayer(results.group(2).toLowerCase());
			if (target.isOnline())
			{
				String worldName = target.getWorld().getName();
				if (this.worldMessages.containsKey(worldName))
					return String.format(this.worldMessages.get(worldName), target.getName());
			}
		}
		return null;
	}

	@Override
	public void OnConfigurationChanged(IConfiguration configuration)
	{
		this.worldMessages = configuration.getConfigValuesAsMap("where");
	}

	private static final Pattern question = Pattern.compile("(?i).*(where\\sis\\s)([a-zA-Z0-9_-]+).*");
	private Map<String, String> worldMessages = new HashMap<String, String>();
}
