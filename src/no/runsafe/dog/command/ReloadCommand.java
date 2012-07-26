package no.runsafe.dog.command;

import no.runsafe.dog.database.ChatTriggerRepository;
import no.runsafe.framework.command.RunsafeCommand;
import no.runsafe.framework.server.player.RunsafePlayer;

public class ReloadCommand extends RunsafeCommand
{
    public ReloadCommand(ChatTriggerRepository chatTriggerRepository)
    {
        super("dogreload", null);
        this.chatTriggerRepository = chatTriggerRepository;
    }

    @Override
    public String OnExecute(RunsafePlayer executor, String[] args)
    {
        this.chatTriggerRepository.GetResponses();
        return "DOG has been reloaded.";
    }

    private ChatTriggerRepository chatTriggerRepository;
}
