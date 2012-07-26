package no.runsafe.dog.command;

import no.runsafe.dog.DOG;
import no.runsafe.framework.command.RunsafeCommand;
import no.runsafe.framework.server.player.RunsafePlayer;

public class SayCommand extends RunsafeCommand
{
    public SayCommand(DOG dog)
    {
        super("dogsay", null);
        this.dog = dog;
    }

    @Override
    public String OnExecute(RunsafePlayer executor, String[] args)
    {
        if (args.length > 0)
        {
            this.dog.Say(args[0]);
            return "DOG has been commanded. Please use AI puppeting sparingly.";
        }
        return "Requires 1 argument: message";
    }

    private DOG dog;
}
