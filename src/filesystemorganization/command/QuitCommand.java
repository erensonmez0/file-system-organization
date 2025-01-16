package filesystemorganization.command;

import filesystemorganization.model.FileSystemOrganization;

/**
 * This command quits a {@link CommandHandler command handler}.
 *
 * @author Programmieren-Team
 */
final class QuitCommand implements Command {

    private static final int NUMBER_OF_ARGUMENTS = 0;
    private final CommandHandler commandHandler;

    /**
     * Constructs a new QuitCommand.
     *
     * @param commandHandler the command handler to be quitted
     */
    QuitCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @Override
    public CommandResult execute(FileSystemOrganization ignored, String[] commandArguments) {
        commandHandler.quit();
        return new CommandResult(CommandResultType.SUCCESS, null);
    }

    @Override
    public int getNumberOfArguments() {
        return NUMBER_OF_ARGUMENTS;
    }
}
