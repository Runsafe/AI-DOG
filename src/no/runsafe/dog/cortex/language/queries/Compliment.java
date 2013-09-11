package no.runsafe.dog.cortex.language.queries;

import no.runsafe.dog.cortex.language.ChatResponderRule;
import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;

import java.util.*;
import java.util.regex.Matcher;

public class Compliment extends ChatResponderRule implements IConfigurationChanged
{
	public Compliment()
	{
		super("(?i).*(you\\s+are|you're)\\s+(pretty|beautiful|awesome)\\s+dog", null, null, null);
	}

	@Override
	public String getResponse(String player, Matcher message)
	{
		return String.format("Thank you %s, you are a %s!", player, this.createCompliment());
	}

	@Override
	public void OnConfigurationChanged(IConfiguration configuration)
	{
		this.complimentTiers.clear();

		List<String> sortOrder = new ArrayList<String>();
		Map<String, List<String>> compliments = configuration.getConfigSectionsAsList("compliments");

		sortOrder.addAll(compliments.keySet());
		Collections.sort(sortOrder);

		for (String tier : sortOrder)
			this.complimentTiers.add(compliments.get(tier));
	}

	private String createCompliment()
	{
		StringBuilder insult = new StringBuilder();

		for (List<String> tier : this.complimentTiers)
			insult.append(tier.get(this.random.nextInt(tier.size()))).append(" ");

		return insult.toString().trim();
	}

	private List<List<String>> complimentTiers = new ArrayList<List<String>>();
	private Random random = new Random();
}
