package no.runsafe.dog.cortex.language.queries;

import no.runsafe.dog.cortex.language.ChatResponderRule;
import no.runsafe.framework.api.IServer;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.regex.Matcher;

public class Calculate extends ChatResponderRule
{
	public Calculate(IServer server)
	{
		super("(?i)^calculate\\s([0-9\\+\\-\\*/\\s\\(\\)\\.]+)$", null, null, null, null, server);
	}

	@Override
	public String getResponse(String player, Matcher message)
	{
		try
		{
			String maths = message.group(1);
			ScriptEngineManager manager = new ScriptEngineManager();
			ScriptEngine engine = manager.getEngineByName("JavaScript");
			Object answer = engine.eval(maths);

			return String.format("That would be %s, %s.", answer, player);
		}
		catch (ScriptException e)
		{
			// Derp.
		}
		return null;
	}
}
