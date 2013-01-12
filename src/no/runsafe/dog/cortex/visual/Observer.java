package no.runsafe.dog.cortex.visual;

import no.runsafe.dog.cortex.Subsystem;
import no.runsafe.dog.cortex.language.Speech;
import no.runsafe.dog.cortex.reason.PlayerChecks;
import no.runsafe.framework.configuration.IConfiguration;
import no.runsafe.framework.event.player.IPlayerInteractEvent;
import no.runsafe.framework.output.IOutput;
import no.runsafe.framework.server.event.player.RunsafePlayerInteractEvent;
import no.runsafe.framework.server.player.RunsafePlayer;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.HashMap;

public class Observer implements Subsystem, IPlayerInteractEvent
{
	public Observer(PlayerChecks playerChecks, Speech speech)
	{
		this.speech = speech;
		this.playerChecks = playerChecks;
		blockedMessages = new HashMap<String, String>();
	}

	@Override
	public void reload(IConfiguration configuration)
	{
		ConfigurationSection blockedMessages = configuration.getSection("messages.blocked");
		this.blockedMessages.clear();
		for (String world : blockedMessages.getKeys(false))
			this.blockedMessages.put(world, blockedMessages.getString(world));
	}

	@Override
	public void OnPlayerInteractEvent(RunsafePlayerInteractEvent event)
	{
		RunsafePlayer player = event.getPlayer();
		if (playerChecks.isGuest(player) && !wasNotified(player))
		{
			speech.Whisper(player, blockedMessages.get(player.getWorld().getName()));
			isNotified(player);
		}
	}

	private boolean wasNotified(RunsafePlayer player)
	{
		return notifiedPlayers.containsKey(player.getWorld().getName())
			&& notifiedPlayers.get(player.getWorld().getName()).contains(player.getName());
	}

	private void isNotified(RunsafePlayer player)
	{
		if (!notifiedPlayers.containsKey(player.getWorld().getName()))
			notifiedPlayers.put(player.getWorld().getName(), new ArrayList<String>());
		if (!notifiedPlayers.get(player.getWorld().getName()).contains(player.getName()))
			notifiedPlayers.get(player.getWorld().getName()).add(player.getName());
	}

	private PlayerChecks playerChecks;
	private Speech speech;
	private final HashMap<String, String> blockedMessages;
	private final HashMap<String, ArrayList<String>> notifiedPlayers = new HashMap<String, ArrayList<String>>();
}
