package no.runsafe.dog.cortex.language.queries;

import no.runsafe.dog.cortex.language.ChatResponderRule;
import no.runsafe.framework.api.IServer;

import java.time.DateTimeException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.regex.Matcher;

public class Time extends ChatResponderRule
{
	public Time(IServer server)
	{
		super("(?i)^dog.*what.*time.*\\s+([A-Z/_]+)[?]+$", null, null, null, null, server);
	}

	@Override
	public String getResponse(String player, Matcher message)
	{
		String timezone = message.group(1);
		if (timezone == null)
			return null;

		ZonedDateTime now;
		try
		{
			now = ZonedDateTime.now(ZoneId.of(timezone));
		}
		catch(DateTimeException e)
		{
			return null; // Invalid time zone
		}

		return String.format("The time is %02d:%02d in %s.", now.getHour(), now.getMinute(), timezone);
	}
}
