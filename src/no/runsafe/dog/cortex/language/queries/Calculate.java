package no.runsafe.dog.cortex.language.queries;

import no.runsafe.dog.cortex.language.ChatResponderRule;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculate extends ChatResponderRule
{
	public Calculate()
	{
		super(null, null, null, null);
	}

	@Override
	public String getResponse(String player, String message)
	{
		Matcher result = question.matcher(message);
		if (result.matches())
		{
			try
			{
				String maths = result.group(1);
				ScriptEngineManager manager = new ScriptEngineManager();
				ScriptEngine engine = manager.getEngineByName("JavaScript");
				Object answer = engine.eval(maths);

				return maths;
				//return String.format("That would be %s, %s.", String.valueOf(answer), player);
			}
			catch (ScriptException e)
			{
				// Derp.
			}
		}
		return null;
	}

	private static final Pattern question = Pattern.compile(".*calculate.*([0-9\\+\\-\\*/\\s\\(\\)]*)$");
}
