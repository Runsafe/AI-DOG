package no.runsafe.dog.cortex.memory;

import no.runsafe.framework.database.IDatabase;
import no.runsafe.framework.database.ISchemaChanges;
import no.runsafe.framework.output.IOutput;
import org.bukkit.ChatColor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Pattern;
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
		return queries;
	}

	public HashMap<Pattern, String> getRules()
	{
		PreparedStatement select = this.database.prepare("SELECT * FROM ai_dog");
		try
		{
			ResultSet data = select.executeQuery();
			HashMap<Pattern, String> rules = new HashMap<Pattern, String>();
			while (data.next())
			{
				try
				{
					rules.put(Pattern.compile(data.getString("pattern")), data.getString("reply"));
					console.fine("Added pattern '" + data.getString("pattern") + "'");
				}
				catch (PatternSyntaxException e)
				{
					console.outputColoredToConsole(
						String.format(
							"Invalid regular expression '%1$s%4$s%3$s' - %2$s%5$s%3$s",
							ChatColor.YELLOW, ChatColor.RED, ChatColor.RESET, e.getPattern(), e.getDescription()
						),
						Level.WARNING
					);
				}
			}
			return rules;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	private IDatabase database;
	private IOutput console;
}