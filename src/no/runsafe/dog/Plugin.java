package no.runsafe.dog;

import no.runsafe.dog.command.ReloadCommand;
import no.runsafe.dog.command.SayCommand;
import no.runsafe.dog.database.ChatTriggerRepository;
import no.runsafe.dog.event.PlayerChatMonitor;
import no.runsafe.framework.RunsafePlugin;

public class Plugin extends RunsafePlugin
{
    @Override
    protected void PluginSetup()
    {
        this.addComponent(DOG.class);
        this.addComponent(ChatTriggerRepository.class);
        this.addComponent(PlayerChatMonitor.class);
        this.addComponent(ReloadCommand.class);
        this.addComponent(SayCommand.class);
    }
}
