package no.runsafe.dog.cortex.language;

import no.runsafe.dog.cortex.Subsystem;
import no.runsafe.framework.server.RunsafeServer;
import org.bukkit.ChatColor;

public class Speech implements Subsystem
{
	public Speech(RunsafeServer server)
	{
		this.server = server;
	}

	@Override
	public void reload()
	{
	}

	public void Speak(String message)
	{
		this.server.broadcastMessage(ChatColor.BLUE + "[DOG]: " + ChatColor.AQUA + message);
	}

	private RunsafeServer server;
}
