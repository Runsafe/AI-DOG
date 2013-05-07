package no.runsafe.dog.events;

import no.runsafe.dog.cortex.language.Speech;
import no.runsafe.framework.event.player.IPlayerJoinEvent;
import no.runsafe.framework.event.player.IPlayerPreLoginEvent;
import no.runsafe.framework.server.RunsafeServer;
import no.runsafe.framework.server.event.player.RunsafePlayerJoinEvent;
import no.runsafe.framework.server.event.player.RunsafePlayerPreLoginEvent;
import no.runsafe.framework.server.player.RunsafePlayer;

import java.util.ArrayList;
import java.util.List;

public class PlayerLogin implements IPlayerPreLoginEvent, IPlayerJoinEvent
{
	public PlayerLogin(Speech speechCenter)
	{
		this.speechCenter = speechCenter;
	}

	@Override
	public void OnBeforePlayerLogin(RunsafePlayerPreLoginEvent event)
	{
		String playerName = event.getPlayer().getName();
		RunsafePlayer player = RunsafeServer.Instance.getPlayer(playerName);

		if (player.isNew())
			this.playersToWelcome.add(playerName);
	}

	@Override
	public void OnPlayerJoinEvent(RunsafePlayerJoinEvent event)
	{
		String playerName = event.getPlayer().getName();
		if (this.playersToWelcome.contains(playerName))
		{
			this.speechCenter.Speak(String.format("Welcome to the server, %s.", playerName));
			this.playersToWelcome.remove(playerName);
		}
	}

	private Speech speechCenter;
	private List<String> playersToWelcome = new ArrayList<String>();
}
