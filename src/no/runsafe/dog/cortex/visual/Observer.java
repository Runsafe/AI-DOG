package no.runsafe.dog.cortex.visual;

import no.runsafe.dog.cortex.Subsystem;
import no.runsafe.dog.cortex.language.Speech;
import no.runsafe.dog.cortex.reason.PlayerChecks;
import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.event.player.IPlayerInteractEvent;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.minecraft.event.player.RunsafePlayerInteractEvent;

import java.util.ArrayList;
import java.util.HashMap;

public class Observer implements Subsystem, IPlayerInteractEvent
{
	public Observer(PlayerChecks playerChecks, Speech speech)
	{
		this.speech = speech;
		this.playerChecks = playerChecks;
		blockedMessages = new HashMap<>();
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
		IPlayer player = event.getPlayer();
		if (!blockedMessages.containsKey(player.getWorldName()) || !playerChecks.isGuest(player) || !shouldNotify(player))
			return;

		speech.Whisper(player, blockedMessages.get(player.getWorldName()));
		isNotified(player);
	}

	private boolean shouldNotify(IPlayer player)
	{
		return !notifiedPlayers.containsKey(player.getWorldName())
			|| !notifiedPlayers.get(player.getWorldName()).contains(player.getName());
	}

	private void isNotified(IPlayer player)
	{
		if (!notifiedPlayers.containsKey(player.getWorldName()))
			notifiedPlayers.put(player.getWorldName(), new ArrayList<>());
		if (!notifiedPlayers.get(player.getWorldName()).contains(player.getName()))
			notifiedPlayers.get(player.getWorldName()).add(player.getName());
	}

	private final PlayerChecks playerChecks;
	private final Speech speech;
	private final HashMap<String, String> blockedMessages;
	private final HashMap<String, ArrayList<String>> notifiedPlayers = new HashMap<>();
}
