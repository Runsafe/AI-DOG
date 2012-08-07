package no.runsafe.dog;

import no.runsafe.framework.server.RunsafeServer;
import no.runsafe.framework.timer.IScheduler;
import org.bukkit.ChatColor;

public class DOG
{
    public DOG(RunsafeServer server, IScheduler scheduler)
    {
        this.server = server;
        this.scheduler = scheduler;
        this.lastResponse = "Docpify should stop refactoring my code.";
    }

    public void Say(String message)
    {
        if (!(this.lastResponse.equals(message) && this.currentResponseLimit > -1))
        {
            this.server.broadcastMessage(ChatColor.BLUE + "[DOG]: " + ChatColor.AQUA + message);
            this.lastResponse = message;
            this.setNewLimit();
        }
    }

    private void setNewLimit()
    {
        if (this.currentResponseLimit > -1)
            this.scheduler.cancelTask(this.currentResponseLimit);

        this.currentResponseLimit = this.scheduler.startSyncTask(new Runnable() {
            public void run()
            {
                currentResponseLimit = -1;
            }
        }, 30);
    }

    private RunsafeServer server;
    private IScheduler scheduler;
    private int currentResponseLimit;
    private String lastResponse;
}
