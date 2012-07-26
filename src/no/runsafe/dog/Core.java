package no.runsafe.dog;

import no.runsafe.framework.RunsafePlugin;

public class Core extends RunsafePlugin
{
    @Override
    protected void PluginSetup()
    {
        this.addComponent(DOG.class);
        this.addComponent(ChatTriggerHandler.class);
        this.addComponent(EventManager.class);
        this.addComponent(ReloadCommand.class);
        this.addComponent(SayCommand.class);
    }
}
