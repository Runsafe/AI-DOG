package no.runsafe.dog.cortex.language.queries;

import no.runsafe.dog.cortex.language.ChatResponderRule;
import no.runsafe.framework.api.IServer;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;

import java.util.TimeZone;
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
		DateTime now = new LocalDateTime(DateTimeZone.UTC).toDateTime(DateTimeZone.forTimeZone(TimeZone.getTimeZone(timezone)));
		return String.format("The time is %s:%s in %s", now.hourOfDay().getAsText(), now.minuteOfHour().getAsText(), timezone);
	}
}
