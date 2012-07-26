package no.runsafe.dog;

import no.runsafe.framework.command.RunsafeCommand;
import no.runsafe.framework.server.player.RunsafePlayer;

public class ReloadCommand extends RunsafeCommand
{
    public ReloadCommand(ChatTriggerHandler chatTriggerHandler)
    {
        super("dogreload", null);
        this.chatTriggerHandler = chatTriggerHandler;
    }

    @Override
    public String OnExecute(RunsafePlayer executor, String[] args)
    {
        this.chatTriggerHandler.GetResponses();
        return "DOG has been reloaded.";
    }

    private ChatTriggerHandler chatTriggerHandler;
}
