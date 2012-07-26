package no.runsafe.dog.event;

import no.runsafe.dog.DOG;
import no.runsafe.dog.database.ChatTriggerRepository;
import no.runsafe.framework.event.player.IPlayerChatEvent;
import no.runsafe.framework.server.event.player.RunsafePlayerChatEvent;
import no.runsafe.framework.server.player.RunsafePlayer;
import no.runsafe.framework.timer.IScheduler;

public class PlayerChatMonitor implements IPlayerChatEvent
{
    public PlayerChatMonitor(DOG dog, ChatTriggerRepository chatTriggerRepository, IScheduler scheduler)
    {
        this.dog = dog;
        this.chatTriggerRepository = chatTriggerRepository;
        this.scheduler = scheduler;
    }

    @Override
    public void OnPlayerChatEvent(RunsafePlayerChatEvent event)
    {
        final String response = this.chatTriggerRepository.GetResponse(event.getMessage());

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
    private ChatTriggerRepository chatTriggerRepository;
    private IScheduler scheduler;
}
