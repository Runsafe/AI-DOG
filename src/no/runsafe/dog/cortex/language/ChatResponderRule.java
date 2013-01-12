package no.runsafe.dog.cortex.language;

import no.runsafe.framework.server.RunsafeServer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatResponderRule
{
	public ChatResponderRule(String pattern, String response, String alternate, String alternatePermission)
	{
		this.rule = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
		this.response = response;
		this.alternate = alternate;
		this.alternatePermission = alternatePermission;
	}

	public String getResponse(String player, String message)
	{
		Matcher matcher = rule.matcher(message);
		if (matcher.matches())
		{
			if (alternatePermission != null && RunsafeServer.Instance.someoneHasPermission(alternatePermission))
				return playerPattern.matcher(alternate).replaceAll(player);

			return playerPattern.matcher(response).replaceAll(player);
		}
		return null;
	}

	private final String response;
	private final String alternate;
	private final String alternatePermission;
	private final Pattern rule;
	private static final Pattern playerPattern = Pattern.compile("%player%");
}
