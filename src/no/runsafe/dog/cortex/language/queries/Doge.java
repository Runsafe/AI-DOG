package no.runsafe.dog.cortex.language.queries;

import no.runsafe.dog.cortex.language.ChatResponderRule;
import no.runsafe.framework.api.IServer;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;

public class Doge extends ChatResponderRule
{
	public Doge(IServer server)
	{
		super("(?i)#doge$", null, null, null, server);
	}

	@Override
	public String getResponse(String player, Matcher message)
	{
		List<String> phraseBin = new ArrayList<String>();
		phraseBin.addAll(phrases);

		List<String> responses = new ArrayList<String>(0);
		String[] words = message.group(1).trim().toLowerCase().split("\\s+");

		for (String word : words)
		{
			if (word.length() > 3 && !excludedWords.contains(word) && !phrases.contains(word))
			{
				String newPhrase = getPhrase(phraseBin);
				if (newPhrase == null) break;

				responses.add(newPhrase + ' ' + word + '.');
			}
		}

		if (!responses.isEmpty())
			return StringUtils.join(responses, ' ');

		return null;
	}

	private String getPhrase(List<String> phraseBin)
	{
		if (phraseBin.isEmpty())
			return null;

		return phraseBin.get(random.nextInt(phraseBin.size()));
	}

	private final Random random = new Random();
	private final static List<String> phrases = new ArrayList<String>();
	private final static List<String> excludedWords = new ArrayList<String>(6);

	static
	{
		excludedWords.add("and");
		excludedWords.add("then");
		excludedWords.add("when");
		excludedWords.add("where");
		excludedWords.add("from");
		excludedWords.add("have");
	}

	static
	{
		phrases.add("much");
		phrases.add("very");
		phrases.add("so");
		phrases.add("such");
	}
}
