package no.runsafe.dog.cortex.visual;

import no.runsafe.dog.cortex.Subsystem;
import no.runsafe.dog.cortex.language.Speech;
import no.runsafe.dog.cortex.reason.PlayerChecks;
import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.event.player.IPlayerInteractEvent;
import no.runsafe.framework.minecraft.event.player.RunsafePlayerInteractEvent;
import no.runsafe.framework.minecraft.player.RunsafePlayer;

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
		if (configuration == null)
			return;
		blockedMessages.clear();
		blockedMessages.putAll(configuration.getConfigValuesAsMap("messages.blocked"));
	}

	@Override
	public void OnPlayerInteractEvent(RunsafePlayerInteractEvent event)
	{
		RunsafePlayer player = event.getPlayer();
		if (blockedMessages.containsKey(player.getWorld().getName()) && playerChecks.isGuest(player) && shouldNotify(player))
		{
			speech.Whisper(player, blockedMessages.get(player.getWorld().getName()));
			isNotified(player);
		}
	}

	private boolean shouldNotify(RunsafePlayer player)
	{
		return !notifiedPlayers.containsKey(player.getWorld().getName())
			|| !notifiedPlayers.get(player.getWorld().getName()).contains(player.getName());
	}

	private void isNotified(RunsafePlayer player)
	{
		if (!notifiedPlayers.containsKey(player.getWorld().getName()))
			notifiedPlayers.put(player.getWorld().getName(), new ArrayList<String>());
		if (!notifiedPlayers.get(player.getWorld().getName()).contains(player.getName()))
			notifiedPlayers.get(player.getWorld().getName()).add(player.getName());
	}

	private final PlayerChecks playerChecks;
	private final Speech speech;
	private final HashMap<String, String> blockedMessages;
	private final HashMap<String, ArrayList<String>> notifiedPlayers = new HashMap<String, ArrayList<String>>();
}
