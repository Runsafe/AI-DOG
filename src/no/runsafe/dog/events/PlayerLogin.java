package no.runsafe.dog.events;

import no.runsafe.dog.cortex.language.Speech;
import no.runsafe.framework.api.IScheduler;
import no.runsafe.framework.api.event.player.IPlayerJoinEvent;
import no.runsafe.framework.api.event.player.IPlayerPreLoginEvent;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.minecraft.event.player.RunsafePlayerJoinEvent;
import no.runsafe.framework.minecraft.event.player.RunsafePlayerPreLoginEvent;

import java.util.ArrayList;
import java.util.List;

public class PlayerLogin implements IPlayerPreLoginEvent, IPlayerJoinEvent
{
	public PlayerLogin(Speech speechCenter, IScheduler scheduler)
	{
		this.speechCenter = speechCenter;
		this.scheduler = scheduler;
	}

	@Override
	public void OnBeforePlayerLogin(RunsafePlayerPreLoginEvent event)
	{
		IPlayer player = event.getPlayer();

		if (player.isNew())
			this.playersToWelcome.add(player.getName());
	}

	@Override
	public void OnPlayerJoinEvent(RunsafePlayerJoinEvent event)
	{
		final String playerName = event.getPlayer().getName();
		if (this.playersToWelcome.contains(playerName))
			this.scheduler.startSyncTask(() -> welcomePlayer(playerName), 2);
	}

	private void welcomePlayer(String playerName)
	{
		this.speechCenter.Speak(String.format("Welcome to the server, %s.", playerName));
		this.playersToWelcome.remove(playerName);
	}

	private final Speech speechCenter;
	private final List<String> playersToWelcome = new ArrayList<>();
	private final IScheduler scheduler;
}
