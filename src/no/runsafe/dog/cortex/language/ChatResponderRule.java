package no.runsafe.dog.cortex.language;

import no.runsafe.framework.api.IServer;
import no.runsafe.framework.api.ai.IChatResponseTrigger;
import no.runsafe.framework.api.player.IPlayer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatResponderRule implements IChatResponseTrigger
{
	public ChatResponderRule(String pattern, String response, String alternate, String alternatePermission, String triggerPermission, IServer server)
	{
		this.server = server;
		if (pattern == null)
		{
			this.rule = null;
			this.response = null;
			this.alternate = null;
			this.alternatePermission = null;
			this.triggerPermission = null;
			return;
		}

		this.rule = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
		this.response = response;
		this.alternate = alternate;
		this.alternatePermission = alternatePermission;
		this.triggerPermission = triggerPermission;
	}

	@Override
	public String getResponse(String player, Matcher message)
	{
		if (triggerPermission != null)
		{
			IPlayer p = server.getPlayerExact(player);
			if (p != null && !p.hasPermission(triggerPermission))
				return null;
		}
		if (alternatePermission != null && server.someoneHasPermission(alternatePermission))
			return playerPattern.matcher(alternate).replaceAll(player);

		return playerPattern.matcher(response).replaceAll(player);
	}

	@Override
	public Pattern getRule()
	{
		return rule;
	}

	private final String response;
	private final String alternate;
	private final String alternatePermission;
	private final String triggerPermission;
	private final Pattern rule;
	private final IServer server;
	private static final Pattern playerPattern = Pattern.compile("%player%");
}
