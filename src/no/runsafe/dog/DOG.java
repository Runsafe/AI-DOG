package no.runsafe.dog;

import no.runsafe.framework.server.RunsafeServer;
import org.bukkit.ChatColor;

public class DOG
{
    public DOG(RunsafeServer server)
    {
        this.server = server;
    }

    public void Say(String message)
    {
        this.server.broadcastMessage(ChatColor.BLUE + "[DOG]: " + ChatColor.AQUA + message);
    }

    private RunsafeServer server;
}
