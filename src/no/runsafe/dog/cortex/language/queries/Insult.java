package no.runsafe.dog.cortex.language.queries;

import no.runsafe.dog.cortex.language.ChatResponderRule;
import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;

import java.util.*;
import java.util.regex.Matcher;

public class Insult extends ChatResponderRule implements IConfigurationChanged
{
	public Insult()
	{
		super("(?i).*(darn|fuck|screw|damn)\\s(you\\s|off\\s|)dog.*", null, null, null);
	}

	@Override
	public String getResponse(String player, Matcher message)
	{
		return String.format("%s, you're nothing but a %s.", player, this.createInsult());
	}

	@Override
	public void OnConfigurationChanged(IConfiguration configuration)
	{
		this.insultTiers.clear();

		List<String> sortOrder = new ArrayList<String>();
		Map<String, List<String>> insults = configuration.getConfigSectionsAsList("insults");

		sortOrder.addAll(insults.keySet());
		Collections.sort(sortOrder);

		for (String tier : sortOrder)
			this.insultTiers.add(insults.get(tier));
	}

	private String createInsult()
	{
		StringBuilder insult = new StringBuilder();

		for (List<String> tier : this.insultTiers)
			insult.append(tier.get(this.random.nextInt(tier.size()))).append(" ");

		return insult.toString().trim();
	}

	private List<List<String>> insultTiers = new ArrayList<List<String>>();
	private Random random = new Random();
}
