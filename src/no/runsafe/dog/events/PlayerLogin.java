package no.runsafe.dog.events;

import no.runsafe.dog.cortex.language.Speech;
import no.runsafe.framework.event.player.IPlayerPreLoginEvent;
import no.runsafe.framework.server.RunsafeServer;
import no.runsafe.framework.server.event.player.RunsafePlayerPreLoginEvent;
import no.runsafe.framework.server.player.RunsafePlayer;

public class PlayerLogin implements IPlayerPreLoginEvent
{
	public PlayerLogin(Speech speechCenter)
	{
		this.speechCenter = speechCenter;
	}

	@Override
	public void OnBeforePlayerLogin(RunsafePlayerPreLoginEvent event)
	{
		String playerName = event.getPlayer().getName();
		try
		{
			RunsafePlayer player = RunsafeServer.Instance.getPlayer(playerName);

			if (player == null)
				speechCenter.Speak("Welcome to the server, " + playerName);
		}
		catch (NullPointerException e)
		{
			speechCenter.Speak("Welcome to the server, " + playerName);
		}
	}

	private Speech speechCenter;
}
