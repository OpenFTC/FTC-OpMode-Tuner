package net.frogbots.ftcopmodetunercommon.networking.udp;

public interface CommandHandler
{
    enum Result
    {
        HANDLED,
        NOT_HANDLED
    }

    Result handleCommand(NetworkCommand command);
}
