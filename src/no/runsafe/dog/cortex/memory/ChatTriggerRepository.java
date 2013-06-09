package no.runsafe.dog.cortex.memory;

import no.runsafe.dog.cortex.language.ChatResponderRule;
import no.runsafe.framework.database.IDatabase;
import no.runsafe.framework.database.ISchemaChanges;
import no.runsafe.framework.output.IOutput;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.regex.PatternSyntaxException;

public class ChatTriggerRepository implements ISchemaChanges
{
	public ChatTriggerRepository(IDatabase database, IOutput output)
	{
		this.database = database;
		this.console = output;
	}

	@Override
	public String getTableName()
	{
		return "ai_dog";
	}

	@Override
	public HashMap<Integer, List<String>> getSchemaUpdateQueries()
	{
		HashMap<Integer, List<String>> queries = new HashMap<Integer, List<String>>();
		ArrayList<String> sql = new ArrayList<String>();
		sql.add(
			"CREATE TABLE ai_dog (" +
				"`pattern` varchar(255) NOT NULL," +
				"`reply` varchar(255) NOT NULL" +
				")"
		);
		queries.put(1, sql);
		sql = new ArrayList<String>();
		sql.add("ALTER TABLE ai_dog ADD COLUMN alternate varchar(255) NULL");
		sql.add("ALTER TABLE ai_dog ADD COLUMN alternate_permission varchar(255) NULL");
		queries.put(2, sql);
		return queries;
	}

	public List<ChatResponderRule> getRules()
	{
		List<Map<String, Object>> data = this.database.Query("SELECT pattern,reply,alternate,alternate_permission FROM ai_dog");
		if (data == null || data.isEmpty())
			return null;
		ArrayList<ChatResponderRule> rules = new ArrayList<ChatResponderRule>();
		for (Map<String, Object> row : data)
		{
			try
			{
				rules.add(
					new ChatResponderRule(
						(String)row.get("pattern"),
						(String)row.get("response"),
						(String)row.get("alternate"),
						(String)row.get("alternate_permission")
					)
				);
				console.fine("Added pattern '%s'", row.get("pattern"));
			}
			catch (PatternSyntaxException e)
			{
				console.writeColoured(
					"Invalid regular expression '&e%1$s&r' - &c%2$s&r",
					Level.WARNING,
					e.getPattern(), e.getDescription()
				);
			}
		}
		return rules;
	}

	private final IDatabase database;
	private final IOutput console;
}
