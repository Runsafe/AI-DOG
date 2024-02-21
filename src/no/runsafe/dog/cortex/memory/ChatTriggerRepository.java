package no.runsafe.dog.cortex.memory;

import no.runsafe.dog.cortex.language.ChatResponderRule;
import no.runsafe.framework.api.IServer;
import no.runsafe.framework.api.database.*;
import no.runsafe.framework.api.log.IConsole;
import no.runsafe.framework.api.log.IDebug;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.PatternSyntaxException;

public class ChatTriggerRepository extends Repository
{
	public ChatTriggerRepository(IDebug output, IConsole console, IServer server)
	{
		this.debugger = output;
		this.console = console;
		this.server = server;
	}

	@Nonnull
	@Override
	public String getTableName()
	{
		return "ai_dog";
	}

	@Nonnull
	@Override
	public ISchemaUpdate getSchemaUpdateQueries()
	{
		ISchemaUpdate update = new SchemaUpdate();

		update.addQueries(
			"CREATE TABLE `ai_dog` (" +
				"`pattern` varchar(255) NOT NULL," +
				"`reply` varchar(255) NOT NULL" +
			')'
		);

		update.addQueries(
			"ALTER TABLE ai_dog ADD COLUMN alternate varchar(255) NULL",
				"ALTER TABLE ai_dog ADD COLUMN alternate_permission varchar(255) NULL"
		);

		update.addQueries(
			"ALTER TABLE `ai_dog` ADD COLUMN `ID` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT FIRST, ADD PRIMARY KEY (`ID`)"
		);

		update.addQueries("ALTER TABLE `ai_dog` ADD COLUMN `trigger_permission` VARCHAR(255) NULL");

		return update;
	}

	public List<ChatResponderRule> getRules()
	{
		ISet data = database.query("SELECT pattern,reply,alternate,alternate_permission FROM ai_dog");
		if (data.isEmpty())
			return null;

		ArrayList<ChatResponderRule> rules = new ArrayList<>();
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
						row.String("trigger_permission"),
						server)
				);
				debugger.debugFine("Added pattern '%s'", row.String("pattern"));
			}
			catch (PatternSyntaxException e)
			{
				console.logWarning(
					"Invalid regular expression '&e%1$s&r' - &c%2$s&r",
					e.getPattern(), e.getDescription()
				);
			}
		}
		return rules;
	}

	private final IDebug debugger;
	private final IConsole console;
	private final IServer server;
}
