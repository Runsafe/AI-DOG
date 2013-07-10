package no.runsafe.dog.cortex.language.queries;

import no.runsafe.dog.cortex.language.ChatResponderRule;
import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Insult extends ChatResponderRule implements IConfigurationChanged
{
	public Insult()
	{
		super(null, null, null, null);
	}

	@Override
	public String getResponse(String player, String message)
	{
		Matcher results = question.matcher(message);

		if (results.matches())
			return String.format("%s, you're nothing but a %s.", player, this.createInsult());

		return null;
	}

	private String createInsult()
	{
		StringBuilder insult = new StringBuilder();

		for (List<String> tier : this.insultTiers)
			insult.append(tier.get(this.random.nextInt(tier.size()))).append(" ");

		return insult.toString().trim();
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

	private static final Pattern question = Pattern.compile("(?i).*(darn|fuck|screw|damn)\\s(you\\s|off\\s|)dog.*");
	private List<List<String>> insultTiers = new ArrayList<List<String>>();
	private Random random = new Random();
}
