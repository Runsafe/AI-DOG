package no.runsafe.dog;

import no.runsafe.framework.database.IDatabase;
import no.runsafe.framework.database.ISchemaUpdater;
import no.runsafe.framework.database.SchemaRevisionRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Pattern;

public class ChatTriggerHandler implements ISchemaUpdater
{
    public ChatTriggerHandler(SchemaRevisionRepository revisionRepository, IDatabase database)
    {
        this.repository = revisionRepository;
        this.database = database;

        this.GetResponses();
    }

    private void NewTrigger(String pattern, String text)
    {
        Pattern compiledPattern = Pattern.compile(pattern);
        this.triggers.put(compiledPattern, text);
    }

    private void WipeTriggers()
    {
        this.triggers = new HashMap<Pattern, String>();
    }

    public String GetResponse(String text)
    {
        for (Map.Entry<Pattern, String> triggerEntry : this.triggers.entrySet())
        {
            if (text.toLowerCase().matches(triggerEntry.getKey().pattern()))
            {
                return triggerEntry.getValue();
            }
        }
        return null;
    }

    @Override
    public void Run()
    {
        int revision = repository.getRevision("ai_dog");
        if (revision < 1)
        {
            PreparedStatement create = database.prepare(
                    "CREATE TABLE ai_dog (" +
                            "`pattern` varchar(255) NOT NULL," +
                            "`reply` varchar(255) NOT NULL" +
                            ")"
            );

            try
            {
                create.execute();
                revision = 1;
            }
            catch(SQLException e)
            {
                e.printStackTrace();
            }
        }
        repository.setRevision("player_db", revision);
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
            while (data.next())
                this.NewTrigger(data.getString(1), data.getString(2));
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    private HashMap<Pattern, String> triggers;

    private SchemaRevisionRepository repository;
    private IDatabase database;
}
