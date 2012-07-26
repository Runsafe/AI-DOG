package no.runsafe.dog;

import no.runsafe.framework.event.player.IPlayerChatEvent;
import no.runsafe.framework.server.event.player.RunsafePlayerChatEvent;
import no.runsafe.framework.server.player.RunsafePlayer;
import no.runsafe.framework.timer.IScheduler;

public class EventManager implements IPlayerChatEvent
{
    public EventManager(DOG dog, ChatTriggerHandler chatTriggerHandler, IScheduler scheduler)
    {
        this.dog = dog;
        this.chatTriggerHandler = chatTriggerHandler;
        this.scheduler = scheduler;
    }

    @Override
    public void OnPlayerChatEvent(RunsafePlayerChatEvent event)
    {
        final String response = this.chatTriggerHandler.GetResponse(event.getMessage());

        if (response != null)
        {
            final RunsafePlayer player = event.getPlayer();

            this.scheduler.startSyncTask(new Runnable() {
                public void run()
                {
                    dog.Say(response.replaceAll("%player%", player.getName()));
                }
            }, 1);
        }
    }

    public void test()
    {


    }

    private DOG dog;
    private ChatTriggerHandler chatTriggerHandler;
    private IScheduler scheduler;
}
