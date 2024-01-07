package no.runsafe.dog.events;

import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.minecraft.event.player.RunsafeCustomEvent;

public class DogUncomfortableEvent extends RunsafeCustomEvent
{
	public DogUncomfortableEvent(IPlayer player)
	{
		super(player, "runsafe.dog.uncomfortable");
	}

	@Override
	public Object getData()
	{
		return null;
	}
}