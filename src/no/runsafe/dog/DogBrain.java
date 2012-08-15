package no.runsafe.dog;

import no.runsafe.dog.cortex.Subsystem;
import no.runsafe.framework.event.IConfigurationChanged;
import no.runsafe.framework.event.IPluginEnabled;

public class DogBrain implements IConfigurationChanged
{
	public DogBrain(Subsystem[] subsystems)
	{
		this.subsystems = subsystems;
	}

	@Override
	public void OnConfigurationChanged()
	{
		for (Subsystem system : subsystems)
			system.reload();
	}

	private Subsystem[] subsystems;
}
