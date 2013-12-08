package no.runsafe.dog.cortex.language;

import no.runsafe.framework.api.IServer;
import no.runsafe.framework.api.ai.IChatResponseTrigger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatResponderRule implements IChatResponseTrigger
{
	public ChatResponderRule(String pattern, String response, String alternate, String alternatePermission, IServer server)
	{
		this.server = server;
		if (pattern == null)
		{
			this.rule = null;
			this.response = null;
			this.alternate = null;
			this.alternatePermission = null;
		}
		else
		{
			this.rule = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
			this.response = response;
			this.alternate = alternate;
			this.alternatePermission = alternatePermission;
		}
	}

	@Override
	public String getResponse(String player, Matcher message)
	{
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
	private final Pattern rule;
	private final IServer server;
	private static final Pattern playerPattern = Pattern.compile("%player%");
}
