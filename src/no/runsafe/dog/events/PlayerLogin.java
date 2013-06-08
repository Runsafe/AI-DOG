package no.runsafe.dog.events;

import no.runsafe.dog.cortex.language.Speech;
import no.runsafe.framework.event.player.IPlayerJoinEvent;
import no.runsafe.framework.event.player.IPlayerPreLoginEvent;
import no.runsafe.framework.server.RunsafeServer;
import no.runsafe.framework.server.event.player.RunsafePlayerJoinEvent;
import no.runsafe.framework.server.event.player.RunsafePlayerPreLoginEvent;
import no.runsafe.framework.server.player.RunsafePlayer;
import no.runsafe.framework.timer.IScheduler;

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
		RunsafePlayer player = event.getPlayer();

		if (player.isNew())
			this.playersToWelcome.add(player.getName());
	}

	@Override
	public void OnPlayerJoinEvent(RunsafePlayerJoinEvent event)
	{
		final String playerName = event.getPlayer().getName();
		if (this.playersToWelcome.contains(playerName))
			this.scheduler.startSyncTask(new Runnable() {
				@Override
				public void run()
				{
					welcomePlayer(playerName);
				}
			}, 2);
	}

	private void welcomePlayer(String playerName)
	{
		this.speechCenter.Speak(String.format("Welcome to the server, %s.", playerName));
		this.playersToWelcome.remove(playerName);
	}

	private final Speech speechCenter;
	private final List<String> playersToWelcome = new ArrayList<String>();
	private final IScheduler scheduler;
}
