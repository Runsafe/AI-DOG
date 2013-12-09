package no.runsafe.dog.cortex.memory;

import no.runsafe.dog.cortex.language.ChatResponderRule;
import no.runsafe.framework.api.IServer;
import no.runsafe.framework.api.database.IDatabase;
import no.runsafe.framework.api.database.IRow;
import no.runsafe.framework.api.database.ISet;
import no.runsafe.framework.api.database.Repository;
import no.runsafe.framework.api.log.IDebug;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.PatternSyntaxException;

public class ChatTriggerRepository extends Repository
{
	public ChatTriggerRepository(IDatabase database, IDebug output, IServer server)
	{
		this.database = database;
		this.debugger = output;
		this.server = server;
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
		sql = new ArrayList<String>();
		sql.add("ALTER TABLE `ai_dog` ADD COLUMN `ID` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT FIRST, ADD PRIMARY KEY (`ID`)");
		queries.put(3, sql);
		return queries;
	}

	public List<ChatResponderRule> getRules()
	{
		ISet data = this.database.Query("SELECT pattern,reply,alternate,alternate_permission FROM ai_dog");
		if (data.isEmpty())
			return null;

		ArrayList<ChatResponderRule> rules = new ArrayList<ChatResponderRule>();
		for (IRow row : data)
		{
			try
			{
				rules.add(
					new ChatResponderRule(
						row.String("pattern"),
						row.String("reply"),
						row.String("alternate"),
						row.String("alternate_permission"),
						server)
				);
				debugger.debugFine("Added pattern '%s'", row.String("pattern"));
			}
			catch (PatternSyntaxException e)
			{
				debugger.writeColoured(
					"Invalid regular expression '&e%1$s&r' - &c%2$s&r",
					Level.WARNING,
					e.getPattern(), e.getDescription()
				);
			}
		}
		return rules;
	}

	private final IDatabase database;
	private final IDebug debugger;
	private final IServer server;
}
