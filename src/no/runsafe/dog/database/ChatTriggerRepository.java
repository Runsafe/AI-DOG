package no.runsafe.dog.database;

import no.runsafe.framework.database.IDatabase;
import no.runsafe.framework.database.ISchemaChanges;
import no.runsafe.framework.event.IPluginEnabled;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class ChatTriggerRepository implements ISchemaChanges, IPluginEnabled
{
	public ChatTriggerRepository(IDatabase database)
    {
		this.database = database;
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

	@Override
	public void OnPluginEnabled()
    {
		this.GetResponses();
	}

	public String GetResponse(String text)
    {
		for(Map.Entry<Pattern, String> triggerEntry : this.triggers.entrySet())
        {
			if(triggerEntry.getKey().matcher(text).matches())
            {
				return triggerEntry.getValue();
			}
		}
		return null;
	}

	public void GetResponses()
    {
		this.WipeTriggers();
		PreparedStatement select = this.database.prepare(
				"SELECT pattern, reply FROM ai_dog"
		);

		try
        {
			ResultSet data = select.executeQuery();
			while(data.next())
				this.NewTrigger(data.getString(1), data.getString(2));
		}
        catch(SQLException e)
        {
			e.printStackTrace();
		}
	}

	private void NewTrigger(String pattern, String text)
    {
		Pattern compiledPattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
		this.triggers.put(compiledPattern, text);
	}

	private void WipeTriggers()
    {
		this.triggers = new HashMap<Pattern, String>();
	}

	private HashMap<Pattern, String> triggers;

	private IDatabase database;
}
