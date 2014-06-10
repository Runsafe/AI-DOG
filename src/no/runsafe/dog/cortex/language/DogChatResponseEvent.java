package no.runsafe.dog.cortex.language;

import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.minecraft.event.player.RunsafePlayerFakeChatEvent;

public class DogChatResponseEvent extends RunsafePlayerFakeChatEvent
{
	public DogChatResponseEvent(IPlayer player, String message)
	{
		super(player, message);
	}
}
